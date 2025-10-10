-- ------------------------------------------------
-- Version: v1.103
--
-- Description: Adds a `target_url` column to `ZenodoAssessmentInfo` table
-- ------------------------------------------------

ALTER TABLE ZenodoAssessmentInfo
ADD COLUMN target_url VARCHAR(255) NULL;