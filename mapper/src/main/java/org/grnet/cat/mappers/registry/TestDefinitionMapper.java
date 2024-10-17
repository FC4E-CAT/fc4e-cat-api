package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.TestDefinition;
import org.grnet.cat.entities.registry.TestMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class}, uses = {TestMethod.class})
public interface TestDefinitionMapper {

    TestDefinitionMapper INSTANCE = Mappers.getMapper(TestDefinitionMapper.class);

    List<TestDefinitionResponseDto> testDefinitionToDtos(List<TestDefinition> entities);

    @Named("map")
    @Mapping(target = "testMethodId", expression = "java(testDefinition.getTestMethod().getId())")
    TestDefinitionResponseDto testDefinitionToDto(TestDefinition testDefinition);

    @Named("mapWithExpression")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "testMethod", ignore = true)
    TestDefinition testDefinitionToEntity(TestDefinitionRequestDto request);

    @Mapping(target = "labelTestDefinition", expression = "java(StringUtils.isNotEmpty(request.labelTestDefinition) ? request.labelTestDefinition : testDefinition.getLabelTestDefinition())")
    @Mapping(target = "paramType", expression = "java(StringUtils.isNotEmpty(request.paramType) ? request.paramType : testDefinition.getParamType())")
    @Mapping(target = "testParams", expression = "java(StringUtils.isNotEmpty(request.testParams) ? request.testParams : testDefinition.getTestParams())")
    @Mapping(target = "testQuestion", expression = "java(StringUtils.isNotEmpty(request.testQuestion) ? request.testQuestion : testDefinition.getTestQuestion())")
    @Mapping(target = "toolTip", expression = "java(StringUtils.isNotEmpty(request.toolTip) ? request.toolTip : testDefinition.getToolTip())")
    @Mapping(target = "testMethod", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateTestDefinitionFromDto(TestDefinitionUpdateDto request, @MappingTarget TestDefinition testDefinition);
}
