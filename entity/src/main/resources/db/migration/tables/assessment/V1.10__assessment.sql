-- ------------------------------------------------
-- Version: v1.8
--
-- Description: Migration that introduces the Assessment table
-- -------------------------------------------------
-- assessment table
CREATE TABLE Assessment(
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   assessment_doc JSON NOT NULL,
   created_on datetime(6) NOT NULL,
   updated_on datetime(6),
   validation_id BIGINT NOT NULL ,
   template_id BIGINT NOT NULL ,
   FOREIGN KEY (validation_id) REFERENCES Validation(id),
   FOREIGN KEY (template_id) REFERENCES Template(id)
);

