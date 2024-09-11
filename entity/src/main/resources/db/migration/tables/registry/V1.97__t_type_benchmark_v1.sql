-- ------------------------------------------------
-- Version: v1.97
--
-- Description: Migration that updates the s_Imperative table
-- -------------------------------------------------

ALTER TABLE t_Type_Benchmark MODIFY COLUMN TBN  VARCHAR(255) NOT NULL;
ALTER TABLE t_Type_Benchmark MODIFY COLUMN labelBenchmarkType  VARCHAR(255) NOT NULL;
ALTER TABLE t_Type_Benchmark MODIFY COLUMN descBenchmarkType TEXT NOT NULL;

UPDATE  t_Type_Benchmark SET populatedBy = NULL WHERE lodTBN='pid_graph:0917EC0D' or lodTBN= 'pid_graph:0A6A8E5A' or lodTBN='pid_graph:16108D9B' or lodTBN='pid_graph:6EBC59CF'
 or lodTBN= 'pid_graph:7085006F' or lodTBN='pid_graph:84E7AE14' or lodTBN='pid_graph:8BA6B11D' or lodTBN='pid_graph:AF8098B8' or lodTBN='pid_graph:BBB9ABD5' or lodTBN='pid_graph:C4D0F2B1';
