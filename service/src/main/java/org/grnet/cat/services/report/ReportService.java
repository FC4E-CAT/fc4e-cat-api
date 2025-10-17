package org.grnet.cat.services.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.grnet.cat.converter.FilterDefinition;
import org.grnet.cat.dtos.report.*;
import org.grnet.cat.mappers.ReportMapper;
import org.grnet.cat.repositories.ReportRepository;
import org.grnet.cat.repositories.SubjectRepository;
import org.grnet.cat.repositories.ValidationRepository;
import org.grnet.cat.repositories.registry.MotivationActorRepository;
import org.grnet.cat.repositories.registry.MotivationRepository;
import org.grnet.cat.services.utils.FilterType;

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
    @Inject
    MotivationActorRepository motivationActorRepository;
    @Inject
    ValidationRepository validationRepository;

    @Inject
    SubjectRepository subjectRepository;


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
    public ReportDefinitionDto getDefinitionById(Long id) {

        var def = reportRepository.findById(id);

        return ReportMapper.INSTANCE.entityToDto(def);
    }

    /**
     * Returns available filter options.
     */
    public List<FilterWithValuesResponseDto> getAllFilters(Long reportId) {
        var reportDefinitionOpt = reportRepository.findDefinitionById(reportId);
        var filters = reportDefinitionOpt.get().getFilters();

        var repos = new FilterType.Repositories(motivationRepository, motivationActorRepository, validationRepository, subjectRepository, reportRepository);

        return filters.stream()
                .map(def -> {
                    var dto = new FilterWithValuesResponseDto();
                    dto.setDefinition(ReportMapper.INSTANCE.filterToDto(def));

                    FilterType.fromName(def.getName())
                            .ifPresent(ft -> dto.setValues(ft.getValues(repos)));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Executes a report definition by ID with the given filters and user context.
     *
     * @param request DTO containing the reportDefinitionId and optional filters
     * @param userId  ID of the user running the report
     * @return a populated ReportResponseDto
     */
    @Transactional
    public ReportResponseDto run(Long id, ReportFilterDto request, String userId) {

        // 1. Load the report definition
        var reportDefinition = reportRepository.findDefinitionById(id)
                .orElseThrow(() -> new NotFoundException("Report definition not found for id " + id));

        var def = ReportMapper.INSTANCE.entityToDto(reportDefinition);

        // 2. Validate filters against definition
        if (request != null && request.getFilters() != null) {
            Map<String, List<String>> provided = request.getFilters();
            List<FilterDefinition> expectedFilters = reportDefinition.getFilters();

            for (FilterDefinition expected : expectedFilters) {
                List<String> values = provided.get(expected.getName());

                // required check
                if (Boolean.TRUE.equals(expected.getRequired()) &&
                        (values == null || values.isEmpty())) {
                    throw new BadRequestException("Missing required filter: " + expected.getName());
                }

                // type check
                if (values != null && !validateListType(values, expected.getType())) {
                    throw new BadRequestException(
                            "Filter '" + expected.getName() + "' must be of type " + expected.getType()
                    );
                }
            }
        }

        // 3. Extract specific filters (if needed by repository)
        List<String> motivations = request.getFilters().getOrDefault("motivation", List.of());
        List<String> publicationStatus = request.getFilters().getOrDefault("publication_status", List.of());
        List<String> actors = request.getFilters().getOrDefault("actor", List.of());
        List<String> organisations = request.getFilters().getOrDefault("organisation", List.of());
        List<String> subjects = request.getFilters().getOrDefault("subject", List.of());


        // 4. Fetch raw data
        var raw = reportRepository.fetchReportData(motivations, publicationStatus, actors, organisations, subjects, id);

        // 5. Build matrix
        var matrix = new LinkedHashMap<String, Map<String, String>>();
        var colSet = new LinkedHashSet<String>();
        buildMatrix(raw, matrix, colSet);

        // 6. Return response
        return buildResponse(def, request, userId, matrix, colSet);
    }

    private boolean validateListType(List<String> values, String expectedType) {
        switch (expectedType.toLowerCase()) {
            case "string":
                return true; // always valid
            case "number":
                return values.stream().allMatch(v -> v.matches("-?\\d+(\\.\\d+)?"));
            case "boolean":
                return values.stream().allMatch(v -> v.equalsIgnoreCase("true") || v.equalsIgnoreCase("false"));
            default:
                return false;
        }
    }

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
     * Validate filter types from request against expected definitions.
     */
    private boolean validateType(Object value, String expectedType) {
        switch (expectedType.toLowerCase()) {
            case "string":
                return value instanceof String ||
                        (value instanceof List && ((List<?>) value).stream().allMatch(v -> v instanceof String));
            case "number":
                return value instanceof Number ||
                        (value instanceof List && ((List<?>) value).stream().allMatch(v -> v instanceof Number));
            case "boolean":
                return value instanceof Boolean ||
                        (value instanceof List && ((List<?>) value).stream().allMatch(v -> v instanceof Boolean));
            default:
                return true; // unknown type → let it pass
        }
    }

    /**
     * Builds the response DTO from a definition, userId, and table data.
     */
    private ReportResponseDto buildResponse(ReportDefinitionDto def,
                                            ReportFilterDto request,
                                            String userId,
                                            LinkedHashMap<String, Map<String, String>> matrix,
                                            LinkedHashSet<String> colSet) {

        var table = matrixToDto(matrix, colSet);

        var response = new ReportResponseDto();
        response.label = def.label;
        response.description = def.description;
        response.rowsDimension = def.rowDimension;
        response.columnsDimension = def.columnDimension;
        response.rows = table.rows;
        response.columns = table.columns;
        response.data = table.data;
        response.valueType = def.valueType;
        response.createdBy = userId;
        response.createdOn = Instant.now().toString();

        // Attach filters back into response
        if (request != null && request.getFilters() != null && !request.getFilters().isEmpty()) {
            var filterDto = new ReportFilterDto();
            filterDto.setFilters(new HashMap<>(request.getFilters())); // copy to avoid mutation
            response.filters = filterDto;
        }

        return response;
    }

    /**
     * Converts a ReportResponseDto to CSV.
     * First row is the header: <label>,<col1>,<col2>,...
     */
    public String exportToCsv(ReportResponseDto report) {
        StringBuilder sb = new StringBuilder();

        // Header: first cell = report label (no separate title row)
        sb.append(escapeCsv(report.label != null ? report.label : ""));
        for (String col : report.columns) {
            sb.append(",").append(escapeCsv(col));
        }
        sb.append("\n");

        // Data rows
        for (int i = 0; i < report.rows.size(); i++) {
            sb.append(escapeCsv(report.rows.get(i))); // row label in first column
            for (String val : report.data.get(i)) {
                sb.append(",").append(escapeCsv(val));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
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