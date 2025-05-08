package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.Test;
import org.grnet.cat.entities.registry.TestDefinition;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class}, uses = {TestDefinitionMapper.class})
public interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @IterableMapping(qualifiedByName="mapTest")
    List<TestResponseDto> testToDtos(List<Test> entities);

    @Named("mapTest")
    TestResponseDto testToDto(Test test);

    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    Test testToEntity(TestRequestDto request);

    @Mapping(target = "TES", expression = "java(StringUtils.isNotEmpty(request.TES) ? request.TES : test.getTES())")
    @Mapping(target = "labelTest", expression = "java(StringUtils.isNotEmpty(request.labelTest) ? request.labelTest : test.getLabelTest())")
    @Mapping(target = "descTest", expression = "java(StringUtils.isNotEmpty(request.descTest) ? request.descTest : test.getDescTest())")
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lodMTV", ignore = true)
    @Mapping(target = "lodTES_V", ignore = true)
    @Mapping(target = "upload", ignore = true)
    @Mapping(target = "dataType", ignore = true)
    @Mapping(target = "version",ignore = true)
    void updateTestFromDto(TestUpdateDto request, @MappingTarget Test test);

    @Named("mapTestAndTestDefinition")
    @Mapping(source = "test", target = "testResponse", qualifiedByName = "mapTest")
    @Mapping(source = "testDefinition", target = "testDefinitionResponse", qualifiedByName = "mapTestDefinition")
    @Mapping(target = "motivations", ignore = true)
    @Mapping(target = "versions", ignore = true)
    TestAndTestDefinitionResponse testAndTestDefinitionToDto(Test test, TestDefinition testDefinition);

    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    Test versionTestToEntity(TestVersionRequestDto request);


}
