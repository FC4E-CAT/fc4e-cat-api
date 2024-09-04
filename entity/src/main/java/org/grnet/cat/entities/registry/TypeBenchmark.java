package org.grnet.cat.entities.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.generator.RegistryId;

@Entity
@Table(name = "t_Type_Benchmark")
@Getter
@Setter
public class TypeBenchmark extends Registry {

    @Id
    @RegistryId
    @Column(name = "lodTBN")
    private String id;

    @Column(name = "TBN")
    private String tbn;

    @Column(name = "labelBenchmarkType")
    private String labelBenchmarkType;

    @Column(name = "descBenchmarkType")
    private String descBenchmarkType;

    @Column(name = "functionPattern")
    private String functionPattern;

    @Column(name = "pattern")
    private String pattern;

    @Column(name = "example")
    private String example;

    @Column(name = "lodMTV")
    private String lodMTV;

    @Column(name = "lodTBN_V")
    private String lodTBNV;

}