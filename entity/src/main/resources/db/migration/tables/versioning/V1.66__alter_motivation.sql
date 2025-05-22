-- ------------------------------------------------
-- Version: v1.63
--
-- Description: Migration that introduces versioning support to the Motivation table
-- -------------------------------------------------
ALTER TABLE t_Motivation
  ADD COLUMN version INT DEFAULT 1;

UPDATE t_Motivation
  SET lodMTV_V = lodMTV;