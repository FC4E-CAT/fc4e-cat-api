package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
public class MotivationPrincipleId {

    @Column(name = "motivation_lodMTV")
    private String motivationId;

    @Column(name = "principle_lodPRI")
    private String principleId;

    @Column(name = "lodM_P_V")
    private Integer lodMpV;

    public MotivationPrincipleId(String motivationId, String principleId, Integer lodMpV) {
        this.motivationId = motivationId;
        this.principleId = principleId;
        this.lodMpV = lodMpV;
    }
    public MotivationPrincipleId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MotivationPrincipleId that = (MotivationPrincipleId) o;
        return Objects.equals(motivationId, that.motivationId) &&
                Objects.equals(principleId, that.principleId) &&
                Objects.equals(lodMpV, that.lodMpV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motivationId, principleId, lodMpV);
    }
}
