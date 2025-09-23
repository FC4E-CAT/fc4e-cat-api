-- ------------------------------------------------
-- Version: v1.63
--
-- Description: Migration that introduces versioning support to the p_Test and p_Test_Definition tables
-- -------------------------------------------------
ALTER TABLE p_Test
  ADD COLUMN version INT DEFAULT 1;

UPDATE p_Test
  SET lodTES_V = lodTES;

ALTER TABLE p_Test_Definition
  ADD COLUMN version INT DEFAULT 1;

UPDATE p_Test_Definition
  SET lodTDF_V = lodTDF;
