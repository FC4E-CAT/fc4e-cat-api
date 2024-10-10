package org.grnet.cat.entities.registry.metric;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_Type_Metric")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TypeMetric {

    @Id
    @Column(name = "lodTMT")
    @RegistryId
    private String id;

    @Column(name = "TMT")
    @NotNull
    private String TMT;

    @Column(name = "labelTypeMetric")
    @NotNull
    private String labelTypeMetric;

    @Column(name = "descTypeMetric")
    @NotNull
    private String descTypeMetric;

    @Column(name = "descMetricApproach")
    @NotNull
    private String descMetricApproach;

    @Column(name = "descBenchmarkApproach")
    @NotNull
    private String descBenchmarkApproach;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lodTCO")
    private TypeReproducibility typeReproducibility;

    @Column(name = "uriTypeMetric")
    @NotNull
    private String uriTypeMetric;

    @Column(name = "populatedBy")
    private String populatedBy;

    @Column(name = "lastTouch")
    private Timestamp lastTouch;

    @Column(name = "lodTMT_V")
    private String lodTMT_V;

    @Column
    private Boolean  verified;
}
