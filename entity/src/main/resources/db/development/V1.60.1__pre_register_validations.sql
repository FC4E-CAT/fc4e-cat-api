-- ------------------------------------------------
-- Version: v1.60.1
--
-- Description: This script runs only in dev mode. Actually, it adds validations and assessments in local database.
-- -------------------------------------------------

    INSERT INTO
        Validation(user_id, organisation_role, organisation_id, organisation_source, organisation_name, organisation_website, actor_id, created_on, status, validated_on, validated_by)
    VALUES
            ('bob_voperson_id', 'Manager', '008pnp284', 'ROR', 'Data Archiving and Networked Services', 'https://dans.knaw.nl/en', 6, now(), 3, now(), 'admin_voperson_id');

    SET @validation_id := LAST_INSERT_ID();

    INSERT INTO
        Subject(id, name, type, subject_id, created_by)
    VALUES
           (1000, 'test-name', 'test', 'test-id', 'admin_voperson_id');

    INSERT INTO
        Assessment(id, validation_id, template_id, created_on, assessment_doc)
    VALUES
        ('6er488a3-c043-53a7-355a-2ffa7b029e79', @validation_id, 1, now(), '{"id":"6er488a3-c043-53a7-355a-2ffa7b029e79","name":"Test Assessment Manager","version":"","status":"","published":false,"timestamp":"2024-10-21 09:39:20.857015","actor":{"name":"PID Owner","id":6},"organisation":{"name":"Data Archiving and Networked Services","id":"008pnp284"},"subject":{"db_id":1000,"name":"test-name","type":"test","id":"test-id"},"result":{"compliance":true,"ranking":1},"principles":[{"criteria":[{"id":"C4","name":"Measurement","description":"The PID owner SHOULD maintain PID attributes.","imperative":"should","metric":{"type":"number","value":1,"result":1,"algorithm":"sum","benchmark":{"equal_greater_than":1},"tests":[{"type":"binary","id":"T4","name":"Maintenance","description":"A test to determine if the entity (PID) attributes are being maintained.","text":"Do you regularly maintain the metadata for your object?","value":true,"result":1,"evidence_url":[{"url":"https://api.cat.argo.grnet.gr"}],"guidance":{"id":"G4","description":"Inventory of public evidence of processes and operations. Subjective evaluation of the completeness of the inventory compared to the infrastructures stated products and services."}}]}}],"id":"P1","name":"Application","description":"PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced."}],"assessment_type":{"name":"eosc pid policy","id":1}}');

    INSERT INTO
        Validation(user_id, organisation_role, organisation_id, organisation_source, organisation_name, organisation_website, actor_id, created_on, status, validated_on, validated_by)
    VALUES
            ('validated_voperson_id', 'Owner', '05tcasm11', 'ROR', 'National Infrastructures for Research and Technology -  GRNET S.A', 'https://www.grnet.gr', 6, now(), 3, now(), 'admin_voperson_id');

    SET @validation_id := LAST_INSERT_ID();

    INSERT INTO
        Assessment(id, validation_id, template_id, created_on, assessment_doc)
    VALUES
        ('2gr488a3-c043-53a7-355a-2ffa7b029e79', @validation_id, 1, now(), '{"id":"2gr488a3-c043-53a7-355a-2ffa7b029e79","name":"Test Assessment Owner","version":"","status":"","published":false,"timestamp":"2024-10-21 10:39:20.857015","actor":{"name":"PID Owner","id":6},"organisation":{"name":"National Infrastructures for Research and Technology -  GRNET S.A","id":"05tcasm11"},"subject":{"db_id":1000,"name":"test-name","type":"test","id":"test-id"},"result":{"compliance":true,"ranking":1},"principles":[{"criteria":[{"id":"C4","name":"Measurement","description":"The PID owner SHOULD maintain PID attributes.","imperative":"should","metric":{"type":"number","value":1,"result":1,"algorithm":"sum","benchmark":{"equal_greater_than":1},"tests":[{"type":"binary","id":"T4","name":"Maintenance","description":"A test to determine if the entity (PID) attributes are being maintained.","text":"Do you regularly maintain the metadata for your object?","value":true,"result":1,"evidence_url":[{"url":"https://api.cat.argo.grnet.gr"}],"guidance":{"id":"G4","description":"Inventory of public evidence of processes and operations. Subjective evaluation of the completeness of the inventory compared to the infrastructures stated products and services."}}]}}],"id":"P1","name":"Application","description":"PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced."}],"assessment_type":{"name":"eosc pid policy","id":1}}');