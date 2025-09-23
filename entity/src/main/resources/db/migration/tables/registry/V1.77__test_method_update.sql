-- ------------------------------------------------
-- Version: v1.77
--
-- Description: Migration that updates the description of 'String-Auto' Test Method
-- -------------------------------------------------
UPDATE t_TestMethod
SET descTestMethod = 'A test is executed automatically, returning a string value as a result'
WHERE lodtme = 'pid_graph:03615660';
