-- ------------------------------------------------
-- Version: v1.148
--
-- Description: Migration that introduces the Motivation Assessment table
-- -------------------------------------------------

ALTER TABLE MotivationAssessment ADD COLUMN motivation_id VARCHAR(255) DEFAULT NULL;

ALTER TABLE MotivationAssessment ADD CONSTRAINT fk_motivation_assessment_motivation_id FOREIGN KEY (motivation_id) REFERENCES t_Motivation(lodMTV);