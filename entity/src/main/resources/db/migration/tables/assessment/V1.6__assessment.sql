-- ------------------------------------------------
-- Version: v1.6
--
-- Description: Migration that introduces the Assessment table
-- -------------------------------------------------
-- assessment table
CREATE TABLE MotivationAssessment(
   id varchar(36) PRIMARY KEY,
   assessment_doc JSON NOT NULL,
   created_on timestamp NOT NULL,
   updated_on timestamp,
   validation_id BIGINT NOT NULL,
   subject_id BIGINT NOT NULL,
   updated_by varchar(100) DEFAULT NULL,
   shared BOOLEAN DEFAULT FALSE,
   motivation_id VARCHAR(255) NOT NULL
);

ALTER TABLE MotivationAssessment ADD CONSTRAINT fk_assessment_subject_id FOREIGN KEY (subject_id) REFERENCES Subject(id);
ALTER TABLE MotivationAssessment ADD CONSTRAINT fk_assessment_validation_id FOREIGN KEY (validation_id) REFERENCES Validation(id);
ALTER TABLE MotivationAssessment ADD CONSTRAINT fk_assessment_motivation_id FOREIGN KEY (motivation_id) REFERENCES t_Motivation(lodMTV);