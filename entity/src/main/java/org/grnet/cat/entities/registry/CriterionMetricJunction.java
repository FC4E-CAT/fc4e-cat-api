package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.metric.Metric;

import java.util.Objects;

@Entity(name = "CriterionMetricJunction")
@Table(name = "p_Criterion_Metric")
@Getter
@Setter
public class CriterionMetricJunction extends Registry{

    @EmbeddedId
    private CriterionMetricId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("motivationId")
    private Motivation motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("metricId")
    private Metric metric;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("criterionId")
    private Criterion criterion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodREL")
    @NotNull
    private Relation relation;

    @Column(name = "lodMTV_X")
    @NotNull
    private String motivationX;

    public CriterionMetricJunction(Motivation motivation, Criterion criterion, Metric metric, Relation relation, String motivationX, Integer lodMcV) {

        this.motivation = motivation;
        this.criterion = criterion;
        this.metric = metric;
        this.motivationX = motivationX;
        this.relation = relation;
        this.id = new CriterionMetricId(motivation.getId(), criterion.getId(), metric.getId(), lodMcV);
    }

    public CriterionMetricJunction() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CriterionMetricJunction that = (CriterionMetricJunction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
