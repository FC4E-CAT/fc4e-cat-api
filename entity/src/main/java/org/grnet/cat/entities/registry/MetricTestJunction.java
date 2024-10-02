package org.grnet.cat.entities.registry;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.metric.Metric;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity(name = "MetricTestJunction")
@Table(name = "p_Metric_Test")
@Getter
@Setter
public class MetricTestJunction extends Registry{

    @EmbeddedId
    private MetricTestId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("metricId")
    private Metric metric;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("testId")
    private Test test;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("testDefinitionId")
    @JoinColumn(name = "test_definition_lodTDF")
    private TestDefinition testDefinition;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("motivationId")
    private Motivation motivation;

    @Column(name = "lodMTV_X")
    @NotNull
    private String motivationX;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodREL")
    @NotNull
    private Relation relation;

    public MetricTestJunction(Metric metric, Test test, TestDefinition testDefinition, Motivation motivation, Relation relation, String motivationX, Integer lodMTTDV) {

        this.motivation = motivation;
        this.metric = metric;
        this.test = test;
        this.testDefinition = testDefinition;
        this.motivationX = motivationX;
        this.relation = relation;
        this.id = new MetricTestId(motivation.getId(),metric.getId(), test.getId(), testDefinition.getId(), lodMTTDV);
    }


    public MetricTestJunction() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MetricTestJunction that = (MetricTestJunction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id);}

}