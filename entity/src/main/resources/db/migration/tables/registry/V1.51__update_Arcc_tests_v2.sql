-- ------------------------------------------------
-- Version: v1.51
--
-- Description: Migration that updates the new arcc tests v2
-- --
UPDATE p_Test_Definition set testParams='xmlUrlValidation',testQuestion='Do administrative contact details for the AA Operator, including at least one role-based email address and one postal contact address exist?',
 toolTip='Please provide a valid metadata xml url to be compared with the schema' where lodTDF='pid_graph:C4CF813A';

UPDATE p_Test_Definition set testParams='xmlUrlValidation',testQuestion='Do an operational security contact for the AA Operator, being at least a role-based email address exist?',
 toolTip='Please provide a valid metadata xml url to be compared with the schema' where lodTDF='pid_graph:23GF813A';

UPDATE p_Test_Definition set testParams='xmlUrlValidation',testQuestion='Do an operational security contact for the AA Operator, preferably including a telephone number, exist?',
 toolTip='Please provide a valid metadata xml url to be compared with the schema' where lodTDF='pid_graph:23GF848A';