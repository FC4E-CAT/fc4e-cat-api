-- ------------------------------------------------
-- Version: v1.89
--
-- Description: Migration that adds a userFriendlyTME column to the t_TestMethod table
-- -------------------------------------------------

ALTER TABLE t_TestMethod
ADD COLUMN friendlyLabelTestMethod VARCHAR(255) DEFAULT NULL;

-- Binary-Manual
UPDATE t_TestMethod
SET friendlyLabelTestMethod = 'Binary',
    descTestMethod = 'This test is performed manually by a user who typically selects yes/no or true/false.'
WHERE labelTestMethod = 'Binary-Manual';

UPDATE t_TestMethod
SET friendlyLabelTestMethod = 'Binary-Evidence',
    descTestMethod = 'This test is performed manually by a user who typically selects yes/no or true/false, and provides supporting evidence.'
WHERE labelTestMethod = 'Binary-Manual-Evidence';

-- TRL-Manual
UPDATE t_TestMethod
SET friendlyLabelTestMethod = 'TRL',
    descTestMethod = 'This test requires to fill the input with the value of the technology readiness level.'
WHERE labelTestMethod = 'TRL-Manual';

-- Percent-Manual
UPDATE t_TestMethod
SET friendlyLabelTestMethod = 'Percent',
    descTestMethod = 'This test requires the user to manually assign a percentage value to the input.'
WHERE labelTestMethod = 'Percent-Manual';

-- Ratio-Manual
UPDATE t_TestMethod
SET friendlyLabelTestMethod = 'Ratio',
    descTestMethod = 'This test requires the user to manually assign 2 numbers in the corresponding inputs to calculate the percentage of them.'
WHERE labelTestMethod = 'Ratio-Manual';