package org.grnet.cat.entities.registry;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

@Entity
@Table(name = "t_Type_Criterion")
@Getter
@Setter
public class TypeCriterion extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodTCR")
    private String id;

    @Column(name = "labelTypeCriterion")
    @NotNull
    private String labelTypeCriterion;

    @Column(name = "descTypeCriterion")
    @NotNull
    private String descTypeCriterion;

    @Column(name = "urlTypeCriterion")
    private String urlTypeCriterion;

    @Column(name = "lodTCR_V")
    private String lodTCRV;

}