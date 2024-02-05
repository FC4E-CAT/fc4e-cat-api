-- ------------------------------------------------
-- Version: v1.6.3
--
-- Description: This script runs only in dev mode. Actually, it registers dev users in local database.
-- -------------------------------------------------

DROP PROCEDURE IF EXISTS ContinueStoreUserEvenIfExist;

DELIMITER $$

CREATE PROCEDURE ContinueStoreUserEvenIfExist()
BEGIN
    -- continue if the duplicate key occurs
   DECLARE CONTINUE HANDLER FOR 1062 SELECT 'Duplicate keys error encountered' Message;
   INSERT INTO User(id, registered_on) VALUES ('admin_ui_voperson_id', now());
    INSERT INTO User(id, registered_on) VALUES ('identified_ui_voperson_id', now());

END$$

DELIMITER ;

CALL ContinueStoreUserEvenIfExist;

DROP PROCEDURE IF EXISTS ContinueStoreUserEvenIfExist;
DROP PROCEDURE IF EXISTS InsertUsers;


