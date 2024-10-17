package org.grnet.cat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.grnet.cat.entities.registry.RegistryTemplateProjection;

import java.sql.Timestamp;

/**
 * This entity represents the Template table in database.
 *
 */

@SqlResultSetMapping(
        name = "registry-template",
        classes = @ConstructorResult(
                targetClass = RegistryTemplateProjection.class,
                columns = {
                        @ColumnResult(name = "PRI", type = String.class),
                        @ColumnResult(name = "labelPrinciple", type = String.class),
                        @ColumnResult(name = "descPrinciple", type = String.class),
                        @ColumnResult(name = "CRI", type = String.class),
                        @ColumnResult(name = "labelCriterion", type = String.class),
                        @ColumnResult(name = "descCriterion", type = String.class),
                        @ColumnResult(name = "lodMTR", type = String.class),
                        @ColumnResult(name = "MTR", type = String.class),
                        @ColumnResult(name = "labelMetric", type = String.class),
                        @ColumnResult(name = "TES", type = String.class),
                        @ColumnResult(name = "labelTest", type = String.class),
                        @ColumnResult(name = "descTest", type = String.class),
                        @ColumnResult(name = "valueBenchmark", type = String.class),
                        @ColumnResult(name = "labelBenchmarkType", type = String.class),
                        @ColumnResult(name = "lodActor", type = String.class),
                        @ColumnResult(name = "labelImperative", type = String.class),
                        @ColumnResult(name = "labelTestMethod", type = String.class),
                        @ColumnResult(name = "testQuestion", type = String.class),
                        @ColumnResult(name = "testParams", type = String.class),
                        @ColumnResult(name = "toolTip", type = String.class)
                }
        )
)
@Entity
public class Template {

    /**
     * As id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_doc", columnDefinition = "json")
    private String templateDoc;

    @ManyToOne
    @JoinColumn(name = "assessment_type_id", referencedColumnName = "id")
    private AssessmentType type;

    @ManyToOne
    @JoinColumn(name = "actor_id",referencedColumnName = "id")
    private Actor actor;

    @Column(name = "created_on")
    @NotNull
    private Timestamp createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getTemplateDoc() {
        return templateDoc;
    }

    public void setTemplateDoc(String templateDoc) {
        this.templateDoc = templateDoc;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }
}