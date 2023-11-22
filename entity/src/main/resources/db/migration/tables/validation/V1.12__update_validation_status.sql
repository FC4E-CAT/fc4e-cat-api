-- ------------------------------------------------
-- Version: v1.12
--
-- Description: Migration that updates the validation status column in the Validation table
-- -------------------------------------------------

UPDATE Validation
SET status = CASE
                 WHEN status = 'PENDING' THEN '1'
                 WHEN status = 'REVIEW' THEN '2'
                 WHEN status = 'APPROVED' THEN '3'
                 WHEN status = 'REJECTED' THEN '4'
                 ELSE '1'
                 END;

ALTER TABLE Validation MODIFY status tinyint NOT NULL;