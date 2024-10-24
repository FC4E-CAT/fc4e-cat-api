-- ------------------------------------------------
-- Version: v1.138
--
-- -------------------------------------------------

SET @table_name = 't_Actor';
SET @existing_column = 'lodActor';
SET @new_column = 'registry_actor_id';

SET @collation_name = (
    SELECT COLLATION_NAME
    FROM information_schema.columns
    WHERE TABLE_NAME = @table_name
      AND COLUMN_NAME = @existing_column
    LIMIT 1
);

SET @sql = CONCAT('ALTER TABLE Validation ADD COLUMN ', @new_column,
                  ' VARCHAR(255) CHARACTER SET utf8mb4 COLLATE ', @collation_name);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE Validation ADD FOREIGN KEY (registry_actor_id) REFERENCES t_Actor(lodActor);