-- ------------------------------------------------
-- Version: v1.51
--
-- Description: Migration that introduces the History table
-- -------------------------------------------------

CREATE TABLE History (
   id SERIAL PRIMARY KEY,
   user_id varchar(255) NOT NULL,
   action varchar(10000) NOT NULL,
   performed_on timestamp NOT NULL
);