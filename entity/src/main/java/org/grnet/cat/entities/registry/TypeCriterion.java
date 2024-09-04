package org.grnet.cat.entities.registry;
import jakarta.persistence.*;
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
    private String labelTypeCriterion;

    @Column(name = "descTypeCriterion")
    private String descTypeCriterion;

    @Column(name = "urlTypeCriterion")
    private String urlTypeCriterion;

    @Column(name = "lodTCR_V")
    private String lodTCRV;

}