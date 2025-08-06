-- ------------------------------------------------
-- Version: v1.91
--
-- Description: Adds a `created_on` column to `p_metric_test` table
-- ------------------------------------------------

ALTER TABLE p_metric_test
ADD COLUMN created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE p_metric_test
SET created_on = lasttouch
WHERE created_on IS NULL;

