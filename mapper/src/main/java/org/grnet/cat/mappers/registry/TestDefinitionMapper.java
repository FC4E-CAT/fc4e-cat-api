package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.TestDefinition;
import org.grnet.cat.utils.TestParamsTransformer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class, TestParamsTransformer.class}, uses = {TestMethodMapper.class})
public interface TestDefinitionMapper {

    TestDefinitionMapper INSTANCE = Mappers.getMapper(TestDefinitionMapper.class);

    @IterableMapping(qualifiedByName="mapTestDefinition")
    List<TestDefinitionResponseDto> testDefinitionToDtos(List<TestDefinition> entities);

    @Named("mapTestDefinition")
    @Mapping(target = "testMethodId", expression = "java(testDefinition.getTestMethod().getId())")
    @Mapping(target = "lodTESV", ignore = true)
    @Mapping(target = "testParams", expression = "java(TestParamsTransformer.transformTestParams(testDefinition.getTestParams()))")
    TestDefinitionResponseDto testDefinitionToDto(TestDefinition testDefinition);

    @Named("mapWithExpression")
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "labelTestDefinition", ignore = true)
//    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
//    @Mapping(target = "testMethod", ignore = true)
//    @Mapping(target = "lodMTV", ignore = true)
//    @Mapping(target = "lodTES", ignore = true)
//    @Mapping(target = "lodDFV", ignore = true)
//    @Mapping(target = "upload", ignore = true)
//    @Mapping(target = "dataType", ignore = true)
//    @Mapping(target = "toolTip", ignore = true)
//    @Mapping(target = "testQuestion", ignore = true)
//    @Mapping(target = "testParams", ignore = true)
//    @Mapping(target = "paramType", ignore = true)
    TestDefinition testDefinitionToEntity(TestDefinitionRequestDto request);

    @Mapping(target = "labelTestDefinition", expression = "java(StringUtils.isNotEmpty(request.labelTestDefinition) ? request.labelTestDefinition : testDefinition.getLabelTestDefinition())")
    @Mapping(target = "paramType", expression = "java(StringUtils.isNotEmpty(request.paramType) ? request.paramType : testDefinition.getParamType())")
    @Mapping(target = "testParams", expression = "java(StringUtils.isNotEmpty(request.testParams) ? TestParamsTransformer.transformTestParams(request.testParams) : testDefinition.getTestParams())")
    @Mapping(target = "testQuestion", expression = "java(StringUtils.isNotEmpty(request.testQuestion) ? request.testQuestion : testDefinition.getTestQuestion())")
    @Mapping(target = "toolTip", expression = "java(StringUtils.isNotEmpty(request.toolTip) ? request.toolTip : testDefinition.getToolTip())")
    @Mapping(target = "testMethod", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lodMTV", ignore = true)
    @Mapping(target = "lodTES", ignore = true)
    @Mapping(target = "lodDFV", ignore = true)
    @Mapping(target = "upload", ignore = true)
    @Mapping(target = "dataType", ignore = true)
    void updateTestDefinitionFromDto(TestDefinitionUpdateDto request, @MappingTarget TestDefinition testDefinition);
}
