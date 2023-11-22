-- ------------------------------------------------
-- Version: v1.42
--
-- Description: Migration that introduces the subject table
-- -------------------------------------------------

CREATE TABLE Subject (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name varchar(50) NOT NULL,
   type varchar(50) NOT NULL,
   subject_id varchar(50) NOT NULL,
   created_by varchar(100) NOT NULL,
   CONSTRAINT UC_Subject UNIQUE (name,type,subject_id,created_by)
);