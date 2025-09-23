-- ------------------------------------------------
-- Version: v1.67
--
-- Description: Migration that merges the p_Test table and the p_Test_Definition table
-- -------------------------------------------------

ALTER TABLE p_Test
ADD COLUMN lodtme character varying(255),
ADD COLUMN labeltestdefinition character varying(255),
ADD COLUMN paramtype character varying(255),
ADD COLUMN testparams character varying(255),
ADD COLUMN testquestion character varying(500),
ADD COLUMN tooltip text,
ADD CONSTRAINT fk_lodTME FOREIGN KEY (lodTME) REFERENCES t_TestMethod(lodTME) ON DELETE CASCADE;

UPDATE p_Test
SET
    lodtme = td.lodtme,
    labeltestdefinition = td.labeltestdefinition,
    paramtype = td.paramtype,
    testparams = td.testparams,
    testquestion = td.testquestion,
    tooltip = td.tooltip
FROM p_Test_Definition td
WHERE p_Test.lodtes = td.lodtes;

UPDATE p_Test
SET lodtme = 'pid_graph:8D79984F'
WHERE lodtes = 'pid_graph:76489E11' AND tes = 'AMR-2.1';

DELETE FROM p_Test
WHERE tes = 'T5' OR tes = 'T36,1';