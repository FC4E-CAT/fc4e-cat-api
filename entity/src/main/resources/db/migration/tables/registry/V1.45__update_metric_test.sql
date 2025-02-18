-- ------------------------------------------------
-- Version: V1.45
--
-- Description: Migration to update Metric and Metric-Test entries.
--              Metric M14 and its related Test T14 are
--              updated to align with Metric M35 and Test T35.
-- ------------------------------------------------

-- Update the p_Metric table
UPDATE p_Metric
SET lodTMT = 'pid_graph:C62DD0BB'
WHERE MTR = 'M14'
  AND lodMTR = 'pid_graph:E1B1DB5C';

-- Update the p_Metric_Test table
UPDATE p_Metric_Test
SET test_definition_lodTDF = 'pid_graph:4079C258'
WHERE metric_lodMTR = 'pid_graph:E1B1DB5C'
  AND test_lodTES = 'pid_graph:0E7400CB';