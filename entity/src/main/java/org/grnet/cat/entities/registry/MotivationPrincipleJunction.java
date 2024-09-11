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

@Entity(name = "MotivationPrincipleJunction")
@Table(name = "p_Motivation_Principle")
@Getter
@Setter
public class MotivationPrincipleJunction extends Registry{

    @EmbeddedId
    private MotivationPrincipleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("motivationId")
    private Motivation motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("principleId")
    private Principle principle;

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

    public MotivationPrincipleJunction(Motivation motivation, Principle principle, String annotationText, String annotationURL, Relation relation, String motivationX, Integer lodMpV) {

        this.motivation = motivation;
        this.principle = principle;
        this.annotationURL = annotationURL;
        this.motivationX = motivationX;
        this.annotationText = annotationText;
        this.relation = relation;
        this.id = new MotivationPrincipleId(motivation.getId(), principle.getId(), lodMpV);
    }
    public MotivationPrincipleJunction() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MotivationPrincipleJunction that = (MotivationPrincipleJunction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
