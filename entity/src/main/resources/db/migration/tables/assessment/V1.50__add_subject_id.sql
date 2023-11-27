-- ------------------------------------------------
-- Version: v1.50
--
-- Description: Migration that adds a new column
-- -------------------------------------------------

ALTER TABLE Assessment
  ADD COLUMN subject_id BIGINT DEFAULT NULL,
  ADD FOREIGN KEY (subject_id) REFERENCES Subject(id);