-- ------------------------------------------------
-- Version: v1.7
--
-- Description: Migration that introduces the AssessmentType table
-- -------------------------------------------------

-- template table
CREATE TABLE AssessmentType(
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name varchar(255) NOT NULL,
   label varchar(255) NOT NULL
 );

 INSERT INTO
      AssessmentType(id,name, label)
VALUES
      (1,'EOSC PID POLICY', 'eosc pid policy');