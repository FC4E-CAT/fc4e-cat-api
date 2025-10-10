-- ------------------------------------------------
-- Version: v1.103
--
-- Description: Adds a `deposit_url` column to `ZenodoAssessmentInfo` table,
-- Remove  `file_url` column to `ZenodoAssessmentInfo` table
-- ------------------------------------------------

ALTER TABLE ZenodoAssessmentInfo
ADD COLUMN deposit_url VARCHAR(255) NULL;


ALTER TABLE ZenodoAssessmentInfo
DROP COLUMN file_url;