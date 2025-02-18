-- ------------------------------------------------
-- Version: v1.47
--
-- Description: Migration that updates the labelBenchmarkType to 'Binary-Binary'
-- -------------------------------------------------

-- Update the labelBenchmarkType to the correct value
UPDATE t_Type_Benchmark
SET labelBenchmarkType = 'Binary-Binary'
WHERE lodTBN = 'pid_graph:7085006F';