-- ------------------------------------------------
-- Version: v1.32
--
-- Description: Migration that drops a column
-- -------------------------------------------------
ALTER TABLE Template DROP COLUMN json_schema;
