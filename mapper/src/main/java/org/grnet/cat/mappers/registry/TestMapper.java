package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.test.TestRequestDto;
import org.grnet.cat.dtos.registry.test.TestResponseDto;
import org.grnet.cat.dtos.registry.test.TestUpdateDto;
import org.grnet.cat.entities.registry.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    List<TestResponseDto> testToDtos(List<Test> entities);

    @Named("map")
    TestResponseDto testToDto(Test test);

    @Named("mapWithExpression")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    Test testToEntity(TestRequestDto request);

    @Mapping(target = "TES", expression = "java(StringUtils.isNotEmpty(request.TES) ? request.TES : test.getTES())")
    @Mapping(target = "labelTest", expression = "java(StringUtils.isNotEmpty(request.labelTest) ? request.labelTest : test.getLabelTest())")
    @Mapping(target = "descTest", expression = "java(StringUtils.isNotEmpty(request.descTest) ? request.descTest : test.getDescTest())")
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateTestFromDto(TestUpdateDto request, @MappingTarget Test test);

}
