-- ------------------------------------------------
-- Version: v1.4
--
-- Description: Migration that introduces the subject table
-- -------------------------------------------------

CREATE TABLE Subject (
   id SERIAL PRIMARY KEY,
   name varchar(200) NOT NULL,
   type varchar(200) NOT NULL,
   subject_id varchar(200) NOT NULL,
   created_by varchar(100) NOT NULL,
   created_on timestamp NOT NULL,
   CONSTRAINT unique_subject UNIQUE (name, type, subject_id, created_by)
);