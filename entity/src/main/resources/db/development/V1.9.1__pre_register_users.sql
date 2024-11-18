-- ------------------------------------------------
-- Version: v1.9.1
--
-- Description: This script runs only in dev mode. Actually, it registers dev users in local database.
-- -------------------------------------------------

DO $$
BEGIN
    -- Insert Users and handle duplicate key errors using EXCEPTION block
    BEGIN
        INSERT INTO CatUser (id, registered_on)
        VALUES
            ('identified_voperson_id', now()),
            ('bob_voperson_id', now()),
            ('validated_voperson_id', now()),
            ('admin_voperson_id', now()),
            ('alice_voperson_id', now()),
            ('admin_ui_voperson_id', now()),
            ('identified_ui_voperson_id', now());
    EXCEPTION
        WHEN unique_violation THEN
            RAISE NOTICE 'Duplicate keys error encountered';
    END;
END $$;
