-- ------------------------------------------------
-- Version: v1.2
--
-- Description: Migration that introduces the Validation table
-- -------------------------------------------------

-- validation table
CREATE TABLE Validation (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   user_id varchar(255) NOT NULL,
   organisation_role varchar(255) NOT NULL,
   organisation_id varchar(255) NOT NULL,
   organisation_source VARCHAR(8) NOT NULL,
   organisation_name varchar(255) NOT NULL,
   organisation_website varchar(255) DEFAULT NULL,
   created_on datetime(6) NOT NULL,
   status VARCHAR(8) NOT NULL,
   FOREIGN KEY (user_id) REFERENCES User(id)
);