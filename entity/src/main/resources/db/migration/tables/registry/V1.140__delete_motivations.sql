-- ------------------------------------------------
-- Version: v1.140
--
-- Description: Migration that deletes all motivations except for specified IDs and their relations
-- --

SET @table_collation = (
    SELECT COLLATION_NAME
    FROM information_schema.columns
    WHERE TABLE_NAME = 'p_Metric_Test'
    AND COLUMN_NAME = 'motivation_lodMTV'
);

SET @sql = CONCAT('SET @keep1 = ''pid_graph:3E109BBA'' COLLATE ', @table_collation, ';');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = CONCAT('SET @keep2 = ''pid_graph:5EB0885A'' COLLATE ', @table_collation, ';');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = CONCAT('SET @keep3 = ''pid_graph:34A9189F'' COLLATE ', @table_collation, ';');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Delete related entries in all relation tables
DELETE FROM p_Metric_Test WHERE motivation_lodMTV NOT IN (@keep1, @keep2, @keep3);
DELETE FROM p_Metric_Definition WHERE motivation_lodMTV NOT IN (@keep1, @keep2, @keep3);
DELETE FROM p_Criterion_Metric WHERE motivation_lodMTV NOT IN (@keep1, @keep2, @keep3);
DELETE FROM p_Criterion_Actor WHERE motivation_lodMTV NOT IN (@keep1, @keep2, @keep3);
DELETE FROM p_Principle_Criterion WHERE motivation_lodMTV NOT IN (@keep1, @keep2, @keep3);
DELETE FROM t_Motivation_Actor WHERE motivation_lodMTV NOT IN (@keep1, @keep2, @keep3);
DELETE FROM p_Motivation_Principle WHERE motivation_lodMTV NOT IN (@keep1, @keep2, @keep3);

-- Delete the motivations themselves
DELETE FROM t_Motivation WHERE lodMTV NOT IN (@keep1, @keep2, @keep3);
