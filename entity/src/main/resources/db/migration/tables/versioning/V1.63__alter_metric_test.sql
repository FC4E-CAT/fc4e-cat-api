-- ------------------------------------------------
-- Version: v1.63
--
-- Description: Migration that Adding a JSONB column to store version information on p_Metric_Test table
-- -------------------------------------------------

ALTER TABLE p_Metric_Test
    ADD COLUMN version_info text[];
