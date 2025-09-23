package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.Test;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @IterableMapping(qualifiedByName="mapTest")
    List<TestResponseDto> testToDtos(List<Test> entities);

    @Named("mapTest")
    @Mapping(target = "testMethodId", expression = "java(test.getTestMethod().getId())")
    @Mapping(target = "motivations", ignore = true)
    @Mapping(target = "versions", ignore = true)
    TestResponseDto testToDto(Test test);

    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    Test testToEntity(TestRequestDto request);

    @Mapping(target = "TES", expression = "java(StringUtils.isNotEmpty(request.TES) ? request.TES : test.getTES())")
    @Mapping(target = "labelTest", expression = "java(StringUtils.isNotEmpty(request.labelTest) ? request.labelTest : test.getLabelTest())")
    @Mapping(target = "descTest", expression = "java(StringUtils.isNotEmpty(request.descTest) ? request.descTest : test.getDescTest())")
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "labelTestDefinition", expression = "java(StringUtils.isNotEmpty(request.labelTestDefinition) ? request.labelTestDefinition : test.getLabelTestDefinition())")
    @Mapping(target = "paramType", expression = "java(StringUtils.isNotEmpty(request.paramType) ? request.paramType : test.getParamType())")
    @Mapping(target = "testParams", expression = "java(StringUtils.isNotEmpty(request.testParams) ? request.testParams : test.getTestParams())")
    @Mapping(target = "testQuestion", expression = "java(StringUtils.isNotEmpty(request.testQuestion) ? request.testQuestion : test.getTestQuestion())")
    @Mapping(target = "toolTip", expression = "java(StringUtils.isNotEmpty(request.tooltip) ? request.tooltip : test.getToolTip())")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lodMTV", ignore = true)
    @Mapping(target = "lodTES_V", ignore = true)
    @Mapping(target = "upload", ignore = true)
    @Mapping(target = "dataType", ignore = true)
    @Mapping(target = "version",ignore = true)
    void updateTestFromDto(TestUpdateDto request, @MappingTarget Test test);

    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "toolTip", expression = "java(StringUtils.isNotEmpty(request.toolTip) ? request.toolTip : test.getToolTip())")
    Test versionTestToEntity(TestVersionRequestDto request);


}
