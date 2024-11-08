-- ------------------------------------------------
-- Version: v1.147
--
-- Description: Migration that introduces the Motivation Assessment table
-- -------------------------------------------------

CREATE TABLE MotivationAssessment(
   id varchar(36) NOT NULL PRIMARY KEY,
   assessment_doc JSON NOT NULL,
   created_on datetime(6) NOT NULL,
   updated_on datetime(6),
   validation_id BIGINT NOT NULL ,
   subject_id BIGINT NOT NULL,
   updated_by varchar(100) DEFAULT NULL,
   shared BOOLEAN DEFAULT FALSE
   );

ALTER TABLE MotivationAssessment ADD CONSTRAINT fk_motivation_assessment_subject_id FOREIGN KEY (subject_id) REFERENCES Subject(id);
ALTER TABLE MotivationAssessment ADD CONSTRAINT fk_motivation_assessment_validation_id FOREIGN KEY (validation_id) REFERENCES Validation(id);

ALTER TABLE Comment ADD COLUMN  motivation_assessment_id VARCHAR(36) DEFAULT NULL;
ALTER TABLE Comment ADD CONSTRAINT fk_motivation_assessment_id FOREIGN KEY (motivation_assessment_id) REFERENCES MotivationAssessment(id);
ALTER TABLE Comment MODIFY COLUMN  assessment_id VARCHAR(36) DEFAULT NULL;
