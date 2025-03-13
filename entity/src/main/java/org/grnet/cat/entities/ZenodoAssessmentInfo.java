package org.grnet.cat.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grnet.cat.entities.registry.Motivation;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public Boolean getPublished() {
        return isPublished;
    }
}
