DELIMITER //

DROP PROCEDURE IF EXISTS DropPrimaryKeyIfExists; //

CREATE PROCEDURE DropPrimaryKeyIfExists(IN table_name VARCHAR(255))
BEGIN
    DECLARE pk_exists INT DEFAULT 0;
    DECLARE current_db VARCHAR(255);
    -- Get the current database name
    SET current_db = DATABASE();

    -- Check if the primary key exists for the given table
    SELECT COUNT(*)
    INTO pk_exists
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE TABLE_SCHEMA = current_db
      AND TABLE_NAME = table_name
      AND CONSTRAINT_TYPE = 'PRIMARY KEY';
-- Print the value of pk_exists for debugging
    SELECT pk_exists AS primary_key_exists;
    -- If it exists, drop the primary key
    IF pk_exists > 0 THEN
     SET @sql =  CONCAT('ALTER TABLE `', current_db, '`.`', table_name, '` DROP PRIMARY KEY');
     SELECT @sql AS sql_statement;  -- This will show you the generated SQL

     PREPARE stmt FROM @sql;
     EXECUTE stmt;
     DEALLOCATE PREPARE stmt;
     END IF;
END //

DELIMITER ;