ALTER TABLE s_Imperative
ADD COLUMN vocabulary_id VARCHAR(255)  NULL;

ALTER TABLE t_Actor
ADD COLUMN vocabulary_id VARCHAR(255)  NULL;

ALTER TABLE t_Type_Motivation
ADD COLUMN vocabulary_id VARCHAR(255)  NULL;

ALTER TABLE t_Type_Metric
ADD COLUMN vocabulary_id VARCHAR(255)  NULL;
