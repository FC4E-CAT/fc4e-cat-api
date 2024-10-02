-- ------------------------------------------------
-- Version: v1.120
--
-- Description: Migration that modifies the p_Test_Definition table with the current db status
-- --
ALTER TABLE p_Test_Definition
ADD COLUMN upload DATE,
ADD COLUMN dataType VARCHAR(255);

-- Inserting values into p_Test_Definition
INSERT INTO p_Test_Definition
(lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V, upload, dataType)
VALUES
('pid_graph:92116C38', 'pid_graph:B733A7D5', 'Manual confirmation of implementation of ORCID for Research References',
 'onscreen', '"use_orcid"|"evidence"', '"Can you confirm that you are using and promoting ORCID for identification of Researchers?"|"Provide a URL to public evidence of such use and/or promotion."',
 'A document, web page, or publication describing or demonstrating use of ORCID', 'pid_graph:34A9189F', NULL, NULL, NULL, NULL);
