-- V1.40 Add published column to t_Motivation

ALTER TABLE t_Motivation ADD COLUMN published BOOLEAN DEFAULT FALSE;

UPDATE t_Motivation SET published = FALSE WHERE published IS NULL;

ALTER TABLE t_Motivation ALTER COLUMN published SET NOT NULL;
ALTER TABLE t_Motivation ALTER COLUMN published SET DEFAULT FALSE;