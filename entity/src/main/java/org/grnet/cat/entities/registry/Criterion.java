package org.grnet.cat.entities.registry;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;
import org.grnet.cat.entities.registry.metric.Metric;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SqlResultSetMapping(
        name = "detailed-criterion",
        classes = @ConstructorResult(
                targetClass = CriterionProjection.class,
                columns = {
                        @ColumnResult(name = "lodMTR", type = String.class),
                        @ColumnResult(name = "MTR", type = String.class),
                        @ColumnResult(name = "labelMetric", type = String.class),
                        @ColumnResult(name = "TES", type = String.class),
                        @ColumnResult(name = "labelTest", type = String.class),
                        @ColumnResult(name = "descTest", type = String.class),
                        @ColumnResult(name = "valueBenchmark", type = String.class),
                        @ColumnResult(name = "labelBenchmarkType", type = String.class),
                        @ColumnResult(name = "labelTestMethod", type = String.class),
                        @ColumnResult(name = "testQuestion", type = String.class),
                        @ColumnResult(name = "testParams", type = String.class),
                        @ColumnResult(name = "toolTip", type = String.class),
                        @ColumnResult(name = "labelAlgorithmType", type = String.class),
                        @ColumnResult(name = "labelTypeMetric", type = String.class)
                }
        )
)
@Entity
@Getter
@Setter
@Table(name = "p_Criterion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Criterion extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodCRI")
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "CRI", unique = true)
    @NotNull
    private String cri;

    @Column(name = "labelCriterion")
    @NotNull
    private String label;

    @Column(name = "descCriterion")
    @NotNull
    private String description;

    @Column(name = "urlCriterion")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodIMP")
    @NotNull
    private Imperative imperative;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodTCR")
    @NotNull
    private TypeCriterion typeCriterion;

    @Column(name = "lodCRI_V")
    private String lodCriV;

    @Column(name = "lodCRI_P")
    private String lodCriP;

    @OneToMany(
            mappedBy = "criterion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PrincipleCriterionJunction> principles = new HashSet<>();

    @OneToMany(
            mappedBy = "criterion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CriterionMetricJunction> metrics = new HashSet<>();
    @OneToMany(
            mappedBy = "criterion",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<CriterionActorJunction> actors = new HashSet<>();

    public void addMetric(Motivation motivation, Metric metric, Relation relation, String motivationX, Integer lodMcV) {

        var criterionMetric = new CriterionMetricJunction(motivation, this, metric, relation, motivationX, lodMcV);
        metrics.add(criterionMetric);
        metric.getCriteria().add(criterionMetric);
    }

    public void removeMetric(Metric metric) {
        for (Iterator<CriterionMetricJunction> iterator = metrics.iterator();
             iterator.hasNext(); ) {
            var criterionMetric = iterator.next();

            if (criterionMetric.getCriterion().equals(this) &&
                    criterionMetric.getMetric().equals(metric)) {
                iterator.remove();
                criterionMetric.getMetric().getCriteria().remove(criterionMetric);
                criterionMetric.setCriterion(null);
                criterionMetric.setMetric(null);
            }
        }
    }

    public String getId() {
        return id;
    }

    public Set<CriterionActorJunction> getActors() {
        return actors;
    }

    public Set<PrincipleCriterionJunction> getPrinciples() {
        return principles;
    }

    public @NotNull String getCri() {
        return cri;
    }
}
