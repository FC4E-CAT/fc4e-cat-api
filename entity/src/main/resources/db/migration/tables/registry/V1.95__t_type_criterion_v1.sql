-- ------------------------------------------------
-- Version: v1.95
--
-- Description: Migration that updates the t_Type_Criterion table
-- -------------------------------------------------
ALTER TABLE t_Type_Criterion MODIFY COLUMN labelTypeCriterion  VARCHAR(255) NOT NULL;
ALTER TABLE t_Type_Criterion MODIFY COLUMN descTypeCriterion TEXT  NOT NULL;

UPDATE  t_Type_Criterion SET populatedBy = NULL
where lodTCR='pid_graph:07CA8184' or lodTCR= 'pid_graph:0D9310AC' or lodTCR='pid_graph:4A47BB1A' or lodTCR='pid_graph:65B56ED3' or lodTCR='pid_graph:7253AFC1' or lodTCR= 'pid_graph:A2719B92' or lodTCR='pid_graph:D2769B37';

