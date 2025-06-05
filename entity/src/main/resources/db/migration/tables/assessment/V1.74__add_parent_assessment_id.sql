-- ------------------------------------------------
-- Version: v1.74
--
-- Description: Migration that introduces the parent_assessment_id column in MotivationAssessment table
-- ------------------------------------------------

-- Add the column
ALTER TABLE MotivationAssessment ADD COLUMN parent_assessment_id VARCHAR(255);

-- Set each assessment to be its own parent for now
UPDATE MotivationAssessment SET parent_assessment_id = id;