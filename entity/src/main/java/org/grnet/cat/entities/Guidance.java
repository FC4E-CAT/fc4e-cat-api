package org.grnet.cat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Entity
public class Guidance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String uuid;

    @Column(unique = true)
    @NotNull
    private String gdn;

    @Column
    @NotNull
    private String label;

    @Column
    @NotNull
    private String description;

    @Column(name = "status_code")
    @NotNull
    private String statusCode;

    @Column(name = "created_on", updatable = false)
    @NotNull
    private Timestamp createdOn;

    @Column(name = "modified_on")
    private Timestamp modifiedOn;

    @Column(name = "created_by")
    @NotNull
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    public void setId(Long id) { this.id = id;}

    public void setUuid(String uuid) { this.uuid = uuid;}

    public void setGdn(String gdn) { this.gdn = gdn;}

    public void setLabel(String label) { this.label = label;}

    public void setDescription(String description) { this.description = description;}

    public void setStatusCode(String statusCode) { this.statusCode = statusCode;}

    public void setCreatedOn(Timestamp createdOn) { this.createdOn = createdOn;}

    public void setModifiedOn(Timestamp modifiedOn) { this.modifiedOn = modifiedOn;}

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy;}

    public void setModifiedBy(String modifiedBy) {this.modifiedBy = modifiedBy;}
}
