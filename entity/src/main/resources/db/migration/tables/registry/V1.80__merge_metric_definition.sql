-- ------------------------------------------------
-- Version: v1.80
--
-- Description: Migration that merges the p_Metric and p_Metric_Definition table
-- -------------------------------------------------

ALTER TABLE p_Metric
ADD COLUMN lodTBN character varying(255),
ADD COLUMN valueBenchmark character varying(255),
ADD COLUMN lodReference character varying(255),
ADD COLUMN lodReference2 character varying(255),
ADD CONSTRAINT fk_lodTBN FOREIGN KEY (lodTBN) REFERENCES t_Type_Benchmark(lodTBN) ON DELETE CASCADE;

UPDATE p_Metric
SET
    lodTBN = md.type_benchmark_lodTBN,
    valueBenchmark = md.valueBenchmark,
    lodReference = md.lodReference,
    lodReference2 = md.lodReference2
FROM p_Metric_Definition md
WHERE p_Metric.lodmtr = md.metric_lodMTR;