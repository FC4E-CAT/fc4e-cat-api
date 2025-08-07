-- ------------------------------------------------
-- Version: v1.93
--
-- Description: Adds a `created_on` column to `p_Principle_Criterion` and `p_Criterion_Actor` and `p_Metric_Test` table
-- ------------------------------------------------

ALTER TABLE p_Criterion_Metric
ADD COLUMN created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE p_Criterion_Metric
SET created_on = lasttouch
WHERE created_on IS NULL;

ALTER TABLE p_Principle_Criterion
ADD COLUMN created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE p_Principle_Criterion
SET created_on = lasttouch
WHERE created_on IS NULL;

ALTER TABLE p_Criterion_Actor
ADD COLUMN created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE p_Criterion_Actor
SET created_on = lasttouch
WHERE created_on IS NULL;


