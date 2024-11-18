-- ------------------------------------------------
-- Version: v1.0
--
-- Description: Migration that introduces the user table
-- -------------------------------------------------

-- user table
CREATE TABLE CatUser (
   id varchar(255) NOT NULL,
   name varchar(255) DEFAULT NULL,
   surname varchar(255) DEFAULT NULL,
   email varchar(100) DEFAULT NULL,
   updated_on timestamp DEFAULT NULL,
   registered_on timestamp DEFAULT NULL,
   validated_on timestamp DEFAULT NULL,
   orcid_id varchar(25) DEFAULT NULL,
   banned BOOLEAN DEFAULT false,
   PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_unique_user_email ON CatUser (email);
CREATE UNIQUE INDEX idx_unique_user_orcid ON CatUser (orcid_id);
CREATE INDEX idx_user_name ON CatUser (name);
CREATE INDEX idx_user_surname ON CatUser (surname);
