package org.grnet.cat.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.enums.ZenodoState;

import java.sql.Timestamp;
/**
 * This entity represents the Zenodo Info for an Assessment published.
 *
 */
@Entity
@Getter
@Setter
@Table(name = "ZenodoAssessmentInfo")
public class ZenodoAssessmentInfo {

    @EmbeddedId
    private ZenodoAssessmentInfoId id;

    @Column(name="is_published")
    @NotNull
    private  Boolean isPublished;

    @Column(name = "uploaded_at")
    @NotNull
    private Timestamp uploadedAt;

    @Column(name = "published_at")
    private Timestamp publishedAt;

    @ManyToOne
    @MapsId("assessmentId")
    @JoinColumn(name = "assessment_id", nullable = false)
    private MotivationAssessment assessment;

    @Enumerated(EnumType.STRING) // Persist the enum as a string
    @Column(name = "zenodo_state")
    private ZenodoState zenodoState;


    public Boolean getPublished() {
        return isPublished;
    }
}
