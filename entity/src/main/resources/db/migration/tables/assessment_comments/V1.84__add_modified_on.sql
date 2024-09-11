-- ------------------------------------------------
-- Version: v1.84
--
-- Description: Migration that introduces a new column
-- -------------------------------------------------

ALTER TABLE Comment
ADD COLUMN modified_on TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;