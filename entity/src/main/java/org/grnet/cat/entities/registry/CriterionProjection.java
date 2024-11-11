package org.grnet.cat.entities.registry;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CriterionProjection {

    private String lodMTR;

    private String MTR;

    private String labelMetric;

    private String TES;

    private String labelTest;

    private String descTest;

    private String valueBenchmark;

    private String labelBenchmarkType;

    private String labelTestMethod;

    private String testQuestion;

    private String testParams;

    private String toolTip;

    private String labelAlgorithmType;

    private String labelTypeMetric;
}
