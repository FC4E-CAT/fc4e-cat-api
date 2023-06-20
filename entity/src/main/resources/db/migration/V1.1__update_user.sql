-- ------------------------------------------------
-- Version: v1.1
--
-- Description: Migration that introduces the user's metadata
-- -------------------------------------------------

ALTER TABLE User
ADD COLUMN name VARCHAR(255) DEFAULT NULL,
ADD COLUMN surname VARCHAR(255) DEFAULT NULL,
ADD COLUMN email VARCHAR(100) DEFAULT NULL,
ADD COLUMN updated_on datetime(6) DEFAULT NULL;
