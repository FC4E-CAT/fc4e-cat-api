DELIMITER //

DROP PROCEDURE IF EXISTS DropForeignKeyIfExists; //

CREATE PROCEDURE DropForeignKeyIfExists(IN fk_name VARCHAR(255))
BEGIN
    DECLARE fk_exists INT DEFAULT 0;
    DECLARE current_db VARCHAR(255);

    -- Retrieve the current database name
    SET current_db = DATABASE();

    -- Check if the foreign key exists
    SELECT COUNT(*)
    INTO fk_exists
    FROM information_schema.REFERENTIAL_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = current_db
      AND TABLE_NAME = 'p_Criterion_Actor'
      AND CONSTRAINT_NAME = fk_name;

    -- If the foreign key exists, drop it
    IF fk_exists > 0 THEN
        SET @sql = CONCAT('ALTER TABLE `', current_db, '`.p_Criterion_Actor DROP FOREIGN KEY `', fk_name, '`');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END //

DELIMITER ;
