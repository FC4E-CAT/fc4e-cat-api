package org.grnet.cat.entities.registry.metric;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;

@Entity
@Table(name = "p_Metric")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Metric {

    @Id
    @Column(name = "lodMTR")
    @RegistryId
    private String id;

    @Column(name = "MTR")
    @NotNull
    private String MTR;

    @Column(name = "labelMetric")
    @NotNull
    private String labelMetric;

    @Column(name = "descrMetric")
    @NotNull
    private String descrMetric;

    @Column(name = "urlMetric")
    @NotNull
    private String urlMetric;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "lodTAL")
    private TypeAlgorithm typeAlgorithm;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodTMT")
    private TypeMetric typeMetric;

    @Column(name = "lodMTV")
    @NotNull
    private String lodMTV;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;
}
