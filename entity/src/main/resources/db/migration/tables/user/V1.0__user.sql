-- ------------------------------------------------
-- Version: v1.0
--
-- Description: Migration that introduces the user table
-- -------------------------------------------------

-- user table
CREATE TABLE User (
   user_type varchar(31) NOT NULL,
   id varchar(255) NOT NULL,
   registered_on datetime(6) DEFAULT NULL,
   validated_on datetime(6) DEFAULT NULL,
   PRIMARY KEY (id)
);