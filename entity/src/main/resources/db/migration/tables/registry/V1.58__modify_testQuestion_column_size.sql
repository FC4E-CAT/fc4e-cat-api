-- ------------------------------------------------
-- Version: v1.58
--
-- Description: Migration that modifies the testQuestion column from VARCHAR(255) to VARCHAR(500) in the p_Test_Definition table.
-- ------------------------------------------------

-- Drop the dependent view
DROP VIEW IF EXISTS registry_templates CASCADE;

-- Modify the column type
ALTER TABLE p_Test_Definition
ALTER COLUMN testQuestion TYPE VARCHAR(500);
