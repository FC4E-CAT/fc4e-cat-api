-- ------------------------------------------------
-- Version: v1.81
--
-- Description: Migration that creates the validation rejection reason column in the Validation table
-- -------------------------------------------------
ALTER TABLE Validation
ADD COLUMN rejection_reason VARCHAR(255);
