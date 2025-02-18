-- ------------------------------------------------
-- Version: v1.8
--
-- Description: Migration that updates the assessment comment table
-- -------------------------------------------------

ALTER TABLE Comment ADD CONSTRAINT fk_comment_assessment_id FOREIGN KEY (assessment_id) REFERENCES MotivationAssessment(id);
ALTER TABLE Comment ADD CONSTRAINT fk_user_assessment_id FOREIGN KEY (user_id) REFERENCES CatUser (id);
