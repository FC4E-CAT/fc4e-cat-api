-- ------------------------------------------------
-- Version: v1.78
--
-- Description: Migration that updates the t_TypeMetric, t_TypeBenchmark, t_TypeAlgorithm table with enable column
-- -------------------------------------------------

-- Add "enabled" column to t_type_metric
ALTER TABLE t_type_metric
ADD COLUMN enabled BOOLEAN DEFAULT TRUE;

UPDATE t_type_metric SET enabled = TRUE;

-- Add "enabled" column to t_type_algorithm
ALTER TABLE t_type_algorithm
ADD COLUMN enabled BOOLEAN DEFAULT TRUE;

UPDATE t_type_algorithm SET enabled = TRUE;

-- Add "enabled" column to t_type_benchmark
ALTER TABLE t_type_benchmark
ADD COLUMN enabled BOOLEAN DEFAULT TRUE;

UPDATE t_type_benchmark SET enabled = TRUE;

