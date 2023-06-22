-- ------------------------------------------------
-- Version: v1.5
--
-- -------------------------------------------------

ALTER TABLE Validation
ADD COLUMN validated_by varchar(255) DEFAULT NULL,
ADD COLUMN validated_on datetime(6) DEFAULT NULL;