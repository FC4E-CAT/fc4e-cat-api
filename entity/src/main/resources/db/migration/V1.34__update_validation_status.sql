-- ------------------------------------------------
-- Version: v1.34
--
-- Description: Migration that updates the validation status column in the Validation table
-- -------------------------------------------------

ALTER TABLE Validation MODIFY status INTEGER NOT NULL;