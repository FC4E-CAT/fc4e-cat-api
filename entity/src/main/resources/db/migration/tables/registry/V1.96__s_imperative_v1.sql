-- ------------------------------------------------
-- Version: v1.96
--
-- Description: Migration that updates the s_Imperative table
-- -------------------------------------------------

ALTER TABLE s_Imperative MODIFY COLUMN IMP  VARCHAR(255) NOT NULL;
ALTER TABLE s_Imperative MODIFY COLUMN labelImperative  VARCHAR(255) NOT NULL;
ALTER TABLE s_Imperative MODIFY COLUMN descImperative  TEXT NOT NULL;

UPDATE  s_Imperative SET populatedBy = NULL WHERE lodIMP='pid_graph:293B1DEE'  or lodIMP= 'pid_graph:2981F3DD' or lodIMP='pid_graph:34F8B2A9' or lodIMP='pid_graph:A4E528D8' or lodIMP= 'pid_graph:BED209B9';