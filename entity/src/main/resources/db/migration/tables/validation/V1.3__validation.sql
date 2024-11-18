-- ------------------------------------------------
-- Version: v1.3
--
-- Description: Migration that introduces the Validation table
-- -------------------------------------------------

-- validation table
CREATE TABLE Validation (
   id SERIAL PRIMARY KEY,
   user_id varchar(255) NOT NULL,
   organisation_role varchar(255) NOT NULL,
   organisation_id varchar(255) NOT NULL,
   organisation_source VARCHAR(8) NOT NULL,
   organisation_name varchar(255) NOT NULL,
   organisation_website varchar(255) DEFAULT NULL,
   created_on timestamp NOT NULL,
   validated_by varchar(255) DEFAULT NULL,
   validated_on timestamp DEFAULT NULL,
   rejection_reason VARCHAR(255) DEFAULT NULL,
   status SMALLINT NOT NULL,
   registry_actor_id VARCHAR(255) NOT NULL
   );

ALTER TABLE Validation ADD CONSTRAINT fk_validation_user FOREIGN KEY (user_id) REFERENCES CatUser (id);
ALTER TABLE Validation ADD CONSTRAINT fk_validation_actor FOREIGN KEY (registry_actor_id) REFERENCES t_Actor (lodActor);

CREATE INDEX idx_validation_organisation_name ON Validation (organisation_name);