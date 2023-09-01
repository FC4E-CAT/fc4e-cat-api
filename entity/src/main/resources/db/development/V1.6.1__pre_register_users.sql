-- ------------------------------------------------
-- Version: v1.6.1
--
-- Description: This script runs only in dev mode. Actually, it registers dev users in local database.
-- -------------------------------------------------

DROP PROCEDURE IF EXISTS InsertUsers;

DELIMITER $$

CREATE PROCEDURE InsertUsers()
BEGIN
    -- exit if the duplicate key occurs
    DECLARE EXIT HANDLER FOR 1062 SELECT 'Duplicate keys error encountered' Message;

    INSERT INTO
            User(id, registered_on)
    VALUES
            ('identified_voperson_id', now()),
            ('bob_voperson_id', now()),
            ('validated_voperson_id', now()),
            ('admin_voperson_id', now()),
            ('alice_voperson_id', now());
END$$

DELIMITER ;

CALL InsertUsers();
