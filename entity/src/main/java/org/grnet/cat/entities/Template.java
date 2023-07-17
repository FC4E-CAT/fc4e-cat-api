package org.grnet.cat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

/**
 * This entity represents the Template table in database.
 *
 */
@Entity
public class Template {

    /**
     * As id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "template_doc", columnDefinition = "json")

    private String template_doc;

    @ManyToOne
    @JoinColumn(name = "assessment_type_id",referencedColumnName = "id")
    private AssessmentType type;
    @ManyToOne
    @JoinColumn(name = "actor_id",referencedColumnName = "id")
    private Actor actor;

    @Column(name = "created_on")
    @NotNull
    private Timestamp createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getTemplate_doc() {
        return template_doc;
    }

    public void setTemplate_doc(String template_doc) {
        this.template_doc = template_doc;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }


}