-- ------------------------------------------------
-- Version: v1.50
--
-- Description: Migration that introduces a new column
-- -------------------------------------------------

ALTER TABLE Assessment ADD COLUMN subject_id BIGINT DEFAULT NULL;
ALTER TABLE Assessment ADD CONSTRAINT fk_subject_id FOREIGN KEY (subject_id) REFERENCES Subject(id);