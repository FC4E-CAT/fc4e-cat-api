-- ------------------------------------------------
-- Version: v1.83
--
-- Description: Migration that adds new column
-- -------------------------------------------------

ALTER TABLE Assessment ADD COLUMN shared BOOLEAN DEFAULT FALSE;