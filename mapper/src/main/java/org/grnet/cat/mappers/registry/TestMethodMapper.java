package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.test.*;
import org.grnet.cat.entities.registry.Test;
import org.grnet.cat.entities.registry.TestMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface TestMethodMapper {

    TestMethodMapper INSTANCE = Mappers.getMapper(TestMethodMapper.class);

    List<TestMethodResponseDto> testMethodToDtos(List<TestMethod> entities);

    @Named("map")
    TestMethodResponseDto testMethodToDto(TestMethod testMethod);

    @Named("mapWithExpression")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    TestMethod testMethodToEntity(TestMethodRequestDto request);

    @Mapping(target = "UUID", expression = "java(StringUtils.isNotEmpty(request.UUID) ? request.UUID : testMethod.getUUID())")
    @Mapping(target = "labelTestMethod", expression = "java(StringUtils.isNotEmpty(request.labelTestMethod) ? request.labelTestMethod : testMethod.getLabelTestMethod())")
    @Mapping(target = "descTestMethod", expression = "java(StringUtils.isNotEmpty(request.descTestMethod) ? request.descTestMethod : testMethod.getDescTestMethod())")
    @Mapping(target = "lodTypeValue", expression = "java(StringUtils.isNotEmpty(request.lodTypeValue) ? request.lodTypeValue : testMethod.getLodTypeValue())")
    @Mapping(target = "lodTypeProcess", expression = "java(StringUtils.isNotEmpty(request.lodTypeProcess) ? request.lodTypeProcess : testMethod.getLodTypeProcess())")
    @Mapping(target = "numParams", expression = "java(request.numParams != null ? request.numParams : testMethod.getNumParams())")
    @Mapping(target = "requestFragment", expression = "java(StringUtils.isNotEmpty(request.requestFragment) ? request.requestFragment : testMethod.getRequestFragment())")
    @Mapping(target = "codeFragment", expression = "java(StringUtils.isNotEmpty(request.codeFragment) ? request.codeFragment : testMethod.getCodeFragment())")
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateTestMethodFromDto(TestMethodUpdateDto request, @MappingTarget TestMethod testMethod);

}
