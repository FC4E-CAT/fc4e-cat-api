-- ------------------------------------------------
-- Version: v1.63
--
-- Description: Migration that introduces versioning support to the p_Metric
-- -------------------------------------------------
ALTER TABLE p_Metric
  ADD COLUMN version INT DEFAULT 1;

UPDATE p_Metric
  SET lodMTR_V = lodMTR;