-- ------------------------------------------------
-- Version: v1.43
--
-- Description: Migration that adds new column in Subject table
-- -------------------------------------------------

ALTER TABLE Subject
ADD COLUMN created_on datetime(6) DEFAULT NULL;