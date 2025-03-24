package org.grnet.cat.mappers;
import org.grnet.cat.dtos.assessment.ZenodoAssessmentInfoResponse;
import org.grnet.cat.dtos.assessment.zenodo.ZenodoCreatorDto;
import org.grnet.cat.dtos.assessment.zenodo.ZenodoDepositResponse;
import org.grnet.cat.dtos.assessment.zenodo.ZenodoFileInfoDto;
import org.grnet.cat.entities.ZenodoAssessmentInfo;
import org.grnet.cat.enums.ZenodoState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(imports = {Objects.class})
public interface ZenodoAssessmentInfoMapper {

    ZenodoAssessmentInfoMapper INSTANCE = Mappers.getMapper(ZenodoAssessmentInfoMapper.class);

    @Mapping(source = "id.assessmentId", target = "assessmentId")
    @Mapping(source = "id.depositId", target = "depositId")
    @Mapping(target = "publishedAt", expression = "java(mapTimestampToString(entity.getPublishedAt()))")
    @Mapping(target = "uploadedAt", expression = "java(mapTimestampToString(entity.getUploadedAt()))")
    @Mapping(target = "isPublished", expression = "java(entity.getPublished())")
    @Mapping(target = "zenodoState", expression = "java(mapZenodoStateToString(entity.getZenodoState()))") // New mapping

    ZenodoAssessmentInfoResponse zenodoAssessmentInfoToResponse(ZenodoAssessmentInfo entity);

    default String mapTimestampToString(Timestamp timestamp) {
        return timestamp != null ? timestamp.toInstant().toString() : null;
    }
    // Updated method to return the custom string value
    default String mapZenodoStateToString(ZenodoState state) {
        return state != null ? state.getType() : null;  // Use getType() instead of name()
    }

    @Mapping( target = "depositId",expression = "java(String.valueOf(response.get(\"id\")))")
    @Mapping(target = "title", expression = "java(mapTitle(response))")
    @Mapping(target = "description", expression = "java(mapDescription(response))")
    @Mapping(target = "doi", expression = "java(mapDoi(response))")
    @Mapping(target = "createdAt", expression = "java(String.valueOf(response.get(\"created\")))")
    @Mapping(target = "submitted", expression = "java((Boolean) response.get(\"submitted\"))")

    @Mapping(target = "publicationDate", expression = "java(mapPublicationDate(response))")
    @Mapping(target = "creators", expression = "java(mapCreators(response))")
    @Mapping(target = "contributors", expression = "java(mapContributors(response))")
    @Mapping(target = "files", expression = "java(mapFiles(response))")
    ZenodoDepositResponse entityToZenodoDepositResponse(Map<String, Object> response);

    default String mapDoi(Map<String, Object> response) {
        var doiObj = response.get("doi");

        if (doiObj == null || "null".equals(doiObj.toString())) {
            return null; // Ensures null is returned instead of "null" string
        }
        return doiObj.toString();
    }

    default String mapPublicationDate(Map<String, Object> response) {
        var metadataObj = response.get("metadata");
        if (metadataObj instanceof Map) {
            Map<String, Object> metadata = (Map<String, Object>) metadataObj;
            Object publicationDate = metadata.get("publication_date");
            return String.valueOf(publicationDate);
        }
        return null;
    }

    default String mapTitle(Map<String, Object> response) {
        var metadataObj = response.get("metadata");
        if (metadataObj instanceof Map) {
            Map<String, Object> metadata = (Map<String, Object>) metadataObj;
            Object title = metadata.get("title");
            return String.valueOf(title);
        }
        return null;
    }

    default String mapDescription(Map<String, Object> response) {
        var metadataObj = response.get("metadata");
        if (metadataObj instanceof Map) {
            Map<String, Object> metadata = (Map<String, Object>) metadataObj;
            Object title = metadata.get("description");
            return String.valueOf(title);
        }
        return null;
    }

    default List<ZenodoCreatorDto> mapCreators(Map<String, Object> response) {
        var metadataObj = response.get("metadata");
        if (metadataObj instanceof Map) {
            Map<String, Object> metadata = (Map<String, Object>) metadataObj;
            Object creatorsObj = metadata.get("creators");

            if (creatorsObj instanceof List<?>) {
                return ((List<?>) creatorsObj).stream()
                        .filter(Map.class::isInstance)
                        .map(obj -> {
                            Map<String, Object> creatorMap = (Map<String, Object>) obj;
                            ZenodoCreatorDto creator = new ZenodoCreatorDto();
                            creator.setName((String) creatorMap.get("name"));
                            creator.setOrcid((String) creatorMap.getOrDefault("orcid", null));
                            return creator;
                        }).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    default List<ZenodoCreatorDto> mapContributors(Map<String, Object> response) {
        var metadataObj = response.get("metadata");
        if (metadataObj instanceof Map) {
            Map<String, Object> metadata = (Map<String, Object>) metadataObj;
            Object creatorsObj = metadata.get("contributors");

            if (creatorsObj instanceof List<?>) {
                return ((List<?>) creatorsObj).stream()
                        .filter(Map.class::isInstance)
                        .map(obj -> {
                            Map<String, Object> creatorMap = (Map<String, Object>) obj;
                            ZenodoCreatorDto creator = new ZenodoCreatorDto();
                            creator.setName((String) creatorMap.get("name"));
                            creator.setOrcid((String) creatorMap.getOrDefault("orcid", "N/A"));
                            return creator;
                        }).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    default List<ZenodoFileInfoDto> mapFiles(Map<String, Object> response) {
            Object files = response.get("files");
            System.out.println("files? "+files);

            if (files instanceof List<?>) {
                return ((List<?>) files).stream()
                        .filter(Map.class::isInstance)
                        .map(obj -> {
                            Map<String, Object> fileMap = (Map<String, Object>) obj;
                            System.out.println("file map: "+fileMap);
                            ZenodoFileInfoDto fileInfo = new ZenodoFileInfoDto();
                            System.out.println("filename is : "+fileMap.get("filename"));
                            fileInfo.setFileName((String) fileMap.get("filename"));
                            fileInfo.setFileSize(((Number) fileMap.getOrDefault("filesize", 0L)).longValue());
                             Map<String,Object> links=(Map<String,Object>)fileMap.get("links");

                            fileInfo.setFileUrl((String) links.get("download"));

                            return fileInfo;
                        }).collect(Collectors.toList());
        }
        return null;
    }

}
