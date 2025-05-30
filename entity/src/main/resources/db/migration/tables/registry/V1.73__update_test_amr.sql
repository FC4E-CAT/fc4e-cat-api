-- ------------------------------------------------
-- Version: v1.73
--
-- Description: Migration that updates the amr-2.1 test
-- -------------------------------------------------

-- Update the test
UPDATE p_Test
SET paramType = 'onscreen',
labelTestDefinition = 'AA Operator collected and published the community documents for the benefit of Relying Parties',
testParams = 'commDocs|evidence',
testQuestion = 'Has the AA Operator collected and published the community documents for the benefit of Relying Parties?|Provide evidence of that via a URL to a page or to a documentation.' ,
toolTip = 'Community documents are available for the Relying Parties|A document, web page, or publication describing provisions'
WHERE lodTES = 'pid_graph:76489E11';