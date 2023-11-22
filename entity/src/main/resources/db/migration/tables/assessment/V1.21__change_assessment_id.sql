-- ------------------------------------------------
-- Version: v1.21
--
-- Description: Migration that changes the Assessment ID type from bigint to uuid
-- -------------------------------------------------

ALTER TABLE Assessment MODIFY COLUMN id varchar(36) NOT NULL;
ALTER TABLE Assessment DROP PRIMARY KEY;
ALTER TABLE Assessment ADD PRIMARY KEY (id);