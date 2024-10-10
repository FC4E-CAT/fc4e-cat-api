-- ------------------------------------------------
-- Version: v1.148
--
-- Description: Migration that introduces a new entry at t_Type_Reproducibility table
-- -------------------------------------------------

INSERT INTO t_Type_Reproducibility (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:AAA1111', 'Initial Reproducibility Type', 'Initial reproducibility type to be replaced',
 'pid_graph:207965C148', NULL);
