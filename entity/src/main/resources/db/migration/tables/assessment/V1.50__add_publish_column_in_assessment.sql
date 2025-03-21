-- ------------------------------------------------
-- Version: v1.50
--
-- Description: Migration that introduces the published column in MotivationAssessment table
-- --
ALTER TABLE MotivationAssessment ADD COLUMN published BOOLEAN DEFAULT FALSE;

UPDATE MotivationAssessment SET published = FALSE WHERE published IS NULL;

ALTER TABLE MotivationAssessment ALTER COLUMN published SET NOT NULL;
ALTER TABLE MotivationAssessment ALTER COLUMN published SET DEFAULT FALSE;