package org.grnet.cat.services.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.grnet.cat.dtos.report.*;
import org.grnet.cat.mappers.ReportMapper;
import org.grnet.cat.repositories.ReportRepository;
import org.grnet.cat.repositories.registry.MotivationRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReportService {

    @Inject
    ReportRepository reportRepository;
    @Inject
    ObjectMapper objectMapper;

    @Inject
    MotivationRepository motivationRepository;

    /**
     * Returns all available report definitions from the database.
     */
    @Transactional
    public List<ReportDefinitionDto> getAllDefinitions() {

        var def = reportRepository.listAllDefinitions();

        return ReportMapper.INSTANCE.entitiesToDtos(def);
    }

    /**
     * Returns all available report definitions from the database.
     */
    @Transactional
    public ReportDefinitionDto getDefinitionById(String id) {

        var def = reportRepository.findById(Long.valueOf(id));

        return ReportMapper.INSTANCE.entityToDto(def);
    }

    /**
     * Returns available filter options.
     */
    public ReportFiltersListDto getAllFilters() {

        var dto = new ReportFiltersListDto();

        dto.motivations = motivationRepository.findAll().stream()
                .map(m -> {
                    var opt = new MotivationPartialDto();
                    opt.id = m.getId();
                    return opt;
                })
                .collect(Collectors.toList());

        dto.publicationStatus = List.of("published", "not_published", "all");

        return dto;
    }

    /**
     * Executes a report definition by ID with the given filters and user context.
     *
     * @param request DTO containing the reportDefinitionId and optional filters
     * @param userId  ID of the user running the report
     * @return a populated ReportResponseDto
     */
    @Transactional
    public ReportResponseDto run(Long id, ReportRequestDto request, String userId) {

        var def = reportRepository.findDefinitionById(id)
                .map(ReportMapper.INSTANCE::entityToDto);

        // String motivationId = null;
        // String published = null;
        if (request.filters != null) {

//            if (!request.filters.motivations.isEmpty()) {
//                motivationId = request.filters.motivations;
//            }
            //  published = request.filters.publicationStatus;
        }

        var raw = reportRepository.fetchReportData(request.filters.motivations, request.filters.publicationStatus, id);

        var matrix = new LinkedHashMap<String, Map<String, String>>();
        var colSet = new LinkedHashSet<String>();
        buildMatrix(raw, matrix, colSet);

        return buildResponse(def.get(), request.filters, userId, matrix, colSet);
    }

    /**
     * Builds the report matrix (row×column → compliance value).
     */
    private void buildMatrix(List<Object[]> raw,
                             Map<String, Map<String, String>> matrix,
                             Set<String> colSet) {
        for (Object[] row : raw) {
            var rowLabel = safeLabel((String) row[0]);
            var colLabel = safeLabel((String) row[1]);
            var assessmentDoc = (String) row[2];

            var value = extractCompliance(assessmentDoc);

            matrix.computeIfAbsent(rowLabel, k -> new LinkedHashMap<>())
                    .put(colLabel, value);

            colSet.add(colLabel);
        }
    }

    /**
     * Builds the response DTO from a definition, userId, and table data.
     */
    private ReportResponseDto buildResponse(ReportDefinitionDto def,
                                            ReportFilterDto filters,
                                            String userId,
                                            LinkedHashMap<String, Map<String, String>> matrix,
                                            LinkedHashSet<String> colSet) {

        var table = matrixToDto(matrix, colSet);

        var response = new ReportResponseDto();
        response.name = def.label;
        response.description = def.description;
        response.rowsDimension = def.rowDimension;
        response.columnsDimension = def.columnDimension;
        response.rows = table.rows;
        response.columns = table.columns;
        response.data = table.data;
        response.valueType = def.valueType;
        response.createdBy = userId;
        response.createdOn = Instant.now().toString();

        if (filters != null) {
            var filterDto = new ReportFilterDto();
            if (!filters.motivations.isEmpty()) {
                filterDto.motivations = filters.motivations;
            }
            if (!filters.publicationStatus.isEmpty()) {
                filterDto.publicationStatus = filters.publicationStatus;
            }
            response.filters = filterDto;
        }

        return response;
    }


    /**
     * Builds the response DTO from definition, userId, and table data.
     */
    ReportResponseDto matrixToDto(Map<String, Map<String, String>> matrix, Set<String> assessments) {
        var result = new ReportResponseDto();
        result.rows = new ArrayList<>(matrix.keySet());
        result.columns = new ArrayList<>(assessments);

        List<List<String>> tableData = new ArrayList<>();
        for (String org : result.rows) {
            List<String> rowData = new ArrayList<>();
            for (String assess : result.columns) {
                String val = "N/A";
                if (matrix.containsKey(org) && matrix.get(org).containsKey(assess)) {
                    val = matrix.get(org).get(assess);
                }
                rowData.add(val);
            }
            tableData.add(rowData);
        }
        result.data = tableData;
        return result;
    }

    /**
     * Extracts compliance result from the assessment JSON.
     * Returns PASS/FAIL/IN PROGRESS/N/A depending on the data.
     */
    private String extractCompliance(String assessmentDoc) {
        try {
            JsonNode root = objectMapper.readTree(assessmentDoc);
            JsonNode node = root.path("result").path("compliance");

            if (!node.isMissingNode() && !node.isNull()) {
                if (node.isBoolean()) {
                    return node.asBoolean() ? "PASS" : "FAIL";
                } else if (node.isInt()) {
                    return node.asInt() == 1 ? "PASS" : "FAIL";
                } else if (node.isTextual()) {
                    String s = node.asText().trim();
                    return s.isEmpty() ? "IN PROGRESS" : s;
                }
            }
            return "IN PROGRESS";
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Ensures labels are non-null and trimmed.
     */
    private String safeLabel(String s) {
        return (s == null || s.trim().isEmpty()) ? "(unknown)" : s.trim();
    }
}