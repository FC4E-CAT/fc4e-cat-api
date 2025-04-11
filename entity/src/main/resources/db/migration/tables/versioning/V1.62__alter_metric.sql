-- ------------------------------------------------
-- Version: v1.62
--
-- Description: Migration that introduces versioning support to the p_Metric and t_Type_Algorithm and t_Type_Metric tables
-- -------------------------------------------------

ALTER TABLE t_Type_Algorithm
  ADD COLUMN version INT DEFAULT 1;

UPDATE t_Type_Algorithm
    SET lodTAL_V = lodTAL;

ALTER TABLE t_Type_Metric
  ADD COLUMN version INT DEFAULT 1;

UPDATE t_Type_Metric
    SET lodTMT_V = lodTMT;

ALTER TABLE p_Metric
  ADD COLUMN version INT DEFAULT 1;

UPDATE p_Metric
    SET lodMTR_V = lodMTR;

