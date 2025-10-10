-- ------------------------------------------------
-- Version: v1.102
--
-- Description: Adds a `doi` column to `ZenodoAssessmentInfo` table
-- ------------------------------------------------

ALTER TABLE ZenodoAssessmentInfo
ADD COLUMN doi VARCHAR(255) NULL;