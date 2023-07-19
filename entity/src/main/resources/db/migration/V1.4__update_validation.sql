-- ------------------------------------------------
-- Version: v1.4
--
-- -------------------------------------------------

ALTER TABLE Validation ADD COLUMN actor_id BIGINT NOT NULL;
ALTER TABLE Validation ADD FOREIGN KEY (actor_id) REFERENCES Actor(id);