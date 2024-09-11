-- ------------------------------------------------
-- Version: v1.72
--
-- Description: Migration that adds new column
-- -------------------------------------------------

ALTER TABLE Assessment ADD COLUMN updated_by varchar(100) DEFAULT NULL;