-- ------------------------------------------------
-- Version: v1.149
--
-- Description: Migration that changes vocabulary service related tables to add a verified column
-- -------------------------------------------------

ALTER  TABLE t_Actor ADD COLUMN verified BOOLEAN DEFAULT TRUE;
ALTER  TABLE t_Type_Motivation ADD COLUMN verified BOOLEAN DEFAULT TRUE;
ALTER  TABLE s_Imperative ADD COLUMN verified BOOLEAN DEFAULT TRUE;
ALTER  TABLE t_Type_Algorithm ADD COLUMN verified BOOLEAN DEFAULT TRUE;
ALTER  TABLE t_Type_Metric ADD COLUMN verified BOOLEAN DEFAULT TRUE;


UPDATE t_Actor SET verified = TRUE WHERE verified IS NULL;
UPDATE t_Type_Motivation SET verified = TRUE WHERE verified IS NULL;
UPDATE s_Imperative SET verified = TRUE WHERE verified IS NULL;
UPDATE t_Type_Algorithm SET verified = TRUE WHERE verified IS NULL;
UPDATE t_Type_Metric SET verified = TRUE WHERE verified IS NULL;


ALTER TABLE t_Actor MODIFY COLUMN verified BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE t_Type_Motivation MODIFY COLUMN verified BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE s_Imperative MODIFY COLUMN verified BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE t_Type_Algorithm MODIFY COLUMN verified BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE t_Type_Metric MODIFY COLUMN verified BOOLEAN NOT NULL DEFAULT TRUE;

