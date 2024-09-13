-- ------------------------------------------------
-- Version: v1.110
--
-- Description: Migration that updates the t_Motivation_Actor
-- -------------------------------------------------
ALTER TABLE t_Motivation_Actor MODIFY COLUMN motivation_lodMTV  VARCHAR(255) NOT NULL;
ALTER TABLE t_Motivation_Actor MODIFY COLUMN actor_lodActor  VARCHAR(255) NOT NULL;
