-- ------------------------------------------------
-- Version: v1.119
--
-- Description: Migration that modifies the p_Test table with the current db status
-- --
ALTER TABLE p_Test
ADD COLUMN upload DATE,
ADD COLUMN dataType VARCHAR(255);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, upload, dataType, lodTES_V)
VALUES
('pid_graph:B00F4C34', 'T36,1', 'ORCID Use', 'The test determines whether ORCIDs are in use to reference researchers.', 'pid_graph:34A9189F', '2024-09-09', 'Test', NULL);
