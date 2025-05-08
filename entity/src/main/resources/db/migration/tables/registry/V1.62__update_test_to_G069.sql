-- ------------------------------------------------
-- Version: v1.62
--
-- Description: Migration that updates the description on Test and parameters on TestDefinition.
-- ------------------------------------------------

UPDATE p_Test
SET descTest = 'Fetches metadata from NACO and checks whether the ''entitlements'' claim is present in both ''user_info'' and ''introspection_info'', and conforms to the URN format defined in AARC-G069.'
WHERE lodTES = 'pid_graph:069489G8';

UPDATE p_Test_Definition
SET testParams = 'aaiProviderId',
    testQuestion = 'The AAI Provider ID to validate AARC-G069 compliance.',
    toolTip = 'Please provide a valid AAI Provider ID'
WHERE lodTDF = 'pid_graph:G0AR813A';