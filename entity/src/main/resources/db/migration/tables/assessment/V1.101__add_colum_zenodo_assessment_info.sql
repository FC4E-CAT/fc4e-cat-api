-- ------------------------------------------------
-- Version: v1.101
--
-- Description: Adds a `file_url` column to `ZenodoAssessmentInfo` table
-- ------------------------------------------------

ALTER TABLE ZenodoAssessmentInfo
ADD COLUMN file_url VARCHAR(255) NULL;