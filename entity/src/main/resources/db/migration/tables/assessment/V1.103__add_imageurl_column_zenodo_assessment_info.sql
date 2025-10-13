-- ------------------------------------------------
-- Version: v1.103
--
-- Description: Adds a `image_url` column to `ZenodoAssessmentInfo` table
-- ------------------------------------------------

ALTER TABLE ZenodoAssessmentInfo
ADD COLUMN image_url VARCHAR(255) NULL;