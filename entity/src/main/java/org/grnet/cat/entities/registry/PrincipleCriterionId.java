package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class PrincipleCriterionId {

    @Column(name = "motivation_lodMTV")
    private String motivationId;

    @Column(name = "principle_lodPRI")
    private String principleId;

    @Column(name = "criterion_lodCRI")
    private String criterionId;

    @Column(name = "lodP_C_V")
    private Integer lodPcV;

    public PrincipleCriterionId(String motivationId, String principleId, String criterionId, Integer lodPcV) {
        this.motivationId = motivationId;
        this.principleId = principleId;
        this.criterionId = criterionId;
        this.lodPcV = lodPcV;
    }
    public PrincipleCriterionId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        PrincipleCriterionId that = (PrincipleCriterionId) o;
        return Objects.equals(motivationId, that.motivationId) &&
                Objects.equals(principleId, that.principleId) &&
                Objects.equals(criterionId, that.criterionId) &&
                Objects.equals(lodPcV, that.lodPcV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motivationId, principleId, criterionId, lodPcV);
    }
}
