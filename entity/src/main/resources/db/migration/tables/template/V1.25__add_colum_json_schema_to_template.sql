-- ------------------------------------------------
-- Version: v1.25
--
-- Description: Migration that adds a new column called json_schema.
-- -------------------------------------------------

ALTER TABLE Template
ADD COLUMN json_schema JSON DEFAULT NULL;