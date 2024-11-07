ALTER TABLE t_Motivation_Actor ADD COLUMN published BOOLEAN DEFAULT FALSE;

UPDATE t_Motivation_Actor SET published = FALSE WHERE published IS NULL;

ALTER TABLE t_Motivation_Actor MODIFY COLUMN published BOOLEAN NOT NULL DEFAULT FALSE;
