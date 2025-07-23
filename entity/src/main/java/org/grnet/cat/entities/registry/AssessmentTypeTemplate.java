package org.grnet.cat.entities.registry;

import com.google.errorprone.annotations.Immutable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "assessment_type_template")
public class AssessmentTypeTemplate {
    @Id
    @Column(name = "lod_id")
    private UUID lodId;

    private String PRI;

    private String CRI;

    @Column(name = "principle_criterion_motivation_id")
    private String principleCriterionMotivationId;

    private String lodActor;

    private String lodPri;
    private String labelPrinciple;
    private String descPrinciple;

    private String lodCri;
    private String labelCriterion;
    private String descCriterion;
    private String labelImperative;

    private String lodMTR;
    private String MTR;
    private String labelMetric;
    private String lodTAL;
    private String labelAlgorithmType;
    private String lodTMT;
    private String labelTypeMetric;
    private String valueBenchmark;
    private String lodTBN;
    private String labelBenchmarkType;

    private String lodTES;
    private String TES;
    private String labelTest;
    private String descTest;
    private String lodTME;
    private String labelTestMethod;
    private String testQuestion;
    private String testParams;
    private String toolTip;

    private String mt_mtv;
    private String cm_mtv;
    private String ca_mtv;
}
