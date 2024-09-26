-- ------------------------------------------------
-- Version: v1.116
--
-- Description: Migration that modifies the p_Metric_Definition table with the current db status
-- --
-- Add new columns 'upload' (as DATE) and 'dataType' (as VARCHAR)
ALTER TABLE p_Metric
ADD COLUMN upload DATE DEFAULT NULL,
ADD COLUMN dataType VARCHAR(255) DEFAULT NULL;

INSERT INTO p_Metric
(MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V, upload, dataType)
VALUES
('M36', 'pid_graph:2604D968', 'PID Recommendation Compliance', 'Evidence is provided of compliance with recommended PIDs for a number of entities', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:34A9189F', NULL, '2024-09-09', 'Test');
