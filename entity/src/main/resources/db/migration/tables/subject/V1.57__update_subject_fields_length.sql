-- ------------------------------------------------
-- Version: v1.57
--
-- Description: Migration that modifies fields length
-- -------------------------------------------------

ALTER TABLE Subject MODIFY name varchar(200) NOT NULL;

ALTER TABLE Subject MODIFY type varchar(200) NOT NULL;

ALTER TABLE Subject MODIFY subject_id varchar(200) NOT NULL;