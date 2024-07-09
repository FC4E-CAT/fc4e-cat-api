-- ------------------------------------------------
-- Version: v1.69
--
-- Description: Migration that inserts a new Assessment type
-- -------------------------------------------------

 INSERT INTO
      AssessmentType(id,name, label)
VALUES
      (2,'PID NL NATIONAL POLICY', 'PID NL National Policy');