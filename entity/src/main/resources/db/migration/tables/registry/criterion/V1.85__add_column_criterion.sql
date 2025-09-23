-- ------------------------------------------------
-- Version: v1.83.1
--
-- Description: Adds lodMTV column to p_criterion
-- ------------------------------------------------

ALTER TABLE p_criterion ADD COLUMN lodMTV VARCHAR(255) DEFAULT NULL;
