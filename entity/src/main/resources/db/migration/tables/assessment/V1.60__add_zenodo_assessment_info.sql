-- ------------------------------------------------
-- Version: v1.6
--
-- Description: Migration that introduces the Assessment table
-- -------------------------------------------------
CREATE TABLE ZenodoAssessmentInfo (
    assessment_id VARCHAR(100) NOT NULL,
    deposit_id VARCHAR(100) NOT NULL,
    is_published BOOLEAN DEFAULT FALSE,
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (assessment_id, deposit_id),
    FOREIGN KEY (assessment_id) REFERENCES MotivationAssessment(id) ON DELETE CASCADE
);