package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class RegistryTemplateId implements Serializable {

    private String PRI;

    private String CRI;

    private String MTR;

    private String TES;

    @Column(name = "principle_criterion_motivation_id")
    private String principleCriterionMotivationId;

    private String lodActor;
}
