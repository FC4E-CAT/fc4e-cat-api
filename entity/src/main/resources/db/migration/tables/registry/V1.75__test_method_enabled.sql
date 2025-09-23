-- ------------------------------------------------
-- Version: v1.75
--
-- Description: Migration that adds the enabled column into Test Method table
-- -------------------------------------------------

-- Add "enabled" column to TestMethod table
ALTER TABLE t_TestMethod ADD COLUMN enabled BOOLEAN DEFAULT TRUE;

UPDATE t_TestMethod SET enabled = TRUE;

