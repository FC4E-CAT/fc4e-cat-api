package org.grnet.cat.entities.registry;


import com.google.errorprone.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "registry_templates")
public class RegistryTemplate {

    @EmbeddedId
    private RegistryTemplateId id;

    private String labelPrinciple;

    private String descPrinciple;

    private String labelCriterion;

    private String descCriterion;

    private String lodMTR;

    private String labelMetric;

    private String labelAlgorithmType;

    private String labelTypeMetric;

    private String labelTest;

    private String descTest;

    private String valueBenchmark;

    private String labelBenchmarkType;

    private String labelImperative;

    private String labelTestMethod;

    private String testQuestion;

    private String testParams;

    private String toolTip;
    private String md_mtv;

    private String mt_mtv;
    private String cm_mtv;

    private String ca_mtv;
}
