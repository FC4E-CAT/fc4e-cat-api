-- ------------------------------------------------
-- Version: v1.90
--
-- Description: Migration that updates `numParams` for Binary-Evidence in the `t_TestMethod` table from 2 to 1.
-- -------------------------------------------------

UPDATE t_TestMethod
SET numParams = 1
WHERE lodtme = 'pid_graph:B733A7D5';