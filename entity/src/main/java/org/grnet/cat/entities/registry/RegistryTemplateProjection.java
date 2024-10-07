package org.grnet.cat.entities.registry;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistryTemplateProjection {

    private String PRI;

    private String labelPrinciple;

    private String descPrinciple;

    private String CRI;

    private String labelCriterion;

    private String descCriterion;

    private String lodMTR;

    private String MTR;

    private String labelMetric;

    private String TES;

    private String labelTest;

    private String descTest;

    private String valueBenchmark;

    private String labelBenchmarkType;

    private String lodActor;

    private String labelImperative;

    private String labelTestMethod;
}
