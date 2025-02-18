-- ------------------------------------------------
-- Version: v1.10.1
--
-- Description: This script runs only in dev mode. Actually, it adds validations and assessments in local database.
-- -------------------------------------------------

DO $$
DECLARE
    validation_id BIGINT;
BEGIN
    -- Insert into Validation table
    INSERT INTO Validation(user_id, organisation_role, organisation_id, organisation_source, organisation_name, organisation_website, registry_actor_id, created_on, status, validated_on, validated_by)
    VALUES
        ('bob_voperson_id', 'Manager', '05tcasm11', 'ROR', 'National Infrastructures for Research and Technology -  GRNET S.A', 'https://grnet.gr/en/', 'pid_graph:D42428D7', now(), 3, now(), 'admin_voperson_id')
    RETURNING id INTO validation_id;

    -- Insert into Subject table
    INSERT INTO Subject(id, name, type, subject_id, created_by, created_on)
    VALUES
        (1000, 'test-name', 'test', 'test-id', 'admin_voperson_id', now());

    -- Insert second row into Validation table
    INSERT INTO Validation(user_id, organisation_role, organisation_id, organisation_source, organisation_name, organisation_website, registry_actor_id, created_on, status, validated_on, validated_by)
    VALUES
        ('validated_voperson_id', 'Owner', '05tcasm11', 'ROR', 'National Infrastructures for Research and Technology -  GRNET S.A', 'https://www.grnet.gr', 'pid_graph:B5CC396B', now(), 3, now(), 'admin_voperson_id')
    RETURNING id INTO validation_id;
END $$;