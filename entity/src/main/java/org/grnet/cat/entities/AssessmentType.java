package org.grnet.cat.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class AssessmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "label")
    private String label;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    private List<Template> templates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
