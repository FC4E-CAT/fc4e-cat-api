package org.grnet.cat.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Entity
public class Principle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true)
    private String pri;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_on", nullable = false, updatable = false)
    private Timestamp createdOn;

    @Column(name = "modified_on", nullable = false)
    private Timestamp modifiedOn;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    public void setId(Long id) { this.id = id;}

    public void setUuid(String uuid) { this.uuid = uuid;}

    public void setPri(String pri) { this.pri = pri;}

    public void setLabel(String label) { this.label = label;}

    public void setDescription(String description) { this.description = description;}


    public void setCreatedOn(Timestamp createdOn) { this.createdOn = createdOn;}

    public void setModifiedOn(Timestamp modifiedOn) { this.modifiedOn = modifiedOn;}

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy;}

    public void setModifiedBy(String modifiedBy) {this.modifiedBy = modifiedBy;}
}

