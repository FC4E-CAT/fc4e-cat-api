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

import java.util.Objects;

@Entity(name = "PrincipleCriterionJunction")
@Table(name = "p_Principle_Criterion")
@Getter
@Setter
public class PrincipleCriterionJunction extends Registry{

    @EmbeddedId
    private PrincipleCriterionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("motivationId")
    private Motivation motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("principleId")
    private Principle principle;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("criterionId")
    private Criterion criterion;

    @Column(name = "annotationText")
    private String annotationText;

    @Column(name = "annotationURL")
    private String annotationURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lodREL")
    @NotNull
    private Relation relation;

    @Column(name = "lodMTV_X")
    @NotNull
    private String motivationX;

    public PrincipleCriterionJunction(Motivation motivation, Principle principle, Criterion criterion, String annotationText, String annotationURL, Relation relation, String motivationX, Integer lodMpV) {

        this.motivation = motivation;
        this.principle = principle;
        this.criterion = criterion;
        this.annotationURL = annotationURL;
        this.motivationX = motivationX;
        this.annotationText = annotationText;
        this.relation = relation;
        this.id = new PrincipleCriterionId(motivation.getId(), principle.getId(), criterion.getId(), lodMpV);
    }

    public PrincipleCriterionJunction() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        PrincipleCriterionJunction that = (PrincipleCriterionJunction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
