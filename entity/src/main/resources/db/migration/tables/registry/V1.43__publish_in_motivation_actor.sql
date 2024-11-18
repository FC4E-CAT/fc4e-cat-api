ALTER TABLE t_Motivation_Actor ADD COLUMN published BOOLEAN DEFAULT FALSE;

UPDATE t_Motivation_Actor SET published = FALSE WHERE published IS NULL;

ALTER TABLE t_Motivation_Actor ALTER COLUMN published SET NOT NULL;
ALTER TABLE t_Motivation_Actor ALTER COLUMN published SET DEFAULT FALSE;

UPDATE t_Motivation SET published = TRUE WHERE lodMTV='pid_graph:3E109BBA';

UPDATE t_Motivation_Actor SET published = TRUE WHERE motivation_lodMTV='pid_graph:3E109BBA';