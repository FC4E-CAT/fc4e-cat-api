-- ------------------------------------------------
-- Version: v1.21
--
-- Description: Migration that introduces the p_Test table
-- -------------------------------------------------

-- Metric table
CREATE TABLE p_Test (
    lodTES VARCHAR(255) NOT NULL,
    TES VARCHAR(255) NOT NULL,
    labelTest VARCHAR(255) NOT NULL,
    descTest VARCHAR(255) NOT NULL,
    lodMTV VARCHAR(255) DEFAULT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTES_V VARCHAR(255) DEFAULT NULL,
    upload DATE DEFAULT NULL,
    dataType VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (lodTES)
);


INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:030489E8', 'T13,1', 'PID Persistence - Service - Evidence', 'Public evidence is provided by the Provider (Service) that PIDs cannot be deleted.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:03626415', 'T33', 'Global Governance', 'Publicly confirm global governance participation', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:07064CA5', 'T24,2', 'Statement - TRL - labs or experimental', 'Public statement of new service available as test, experiment, or labs version.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:0E7400CB', 'T14', 'Resolution Efficiency/ Integrity', 'This test is equivalent to T35, but the result applies to a Manager.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:0FB9EC33', 'T15,1', 'Machine-actionable type information', 'A pathway or published API call to verify the type is available.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:166A398F', 'T18', 'Metadata is machine readable', 'Metadata is available in machine-readable format from the resolution target. This is publicly available by providing a URL pattern, API, or code example.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:1C02006A', 'T15,2', 'Machine-actionable management policy', 'A pathway or published API call to obtain the management policy is available.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:1E984FD2', 'T30', 'Global Resolution Possible', 'Public declaration of the countries, if any, where services are not available (count=c). If the number exceeds b, the provision is not satisfied.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:29D74907', 'T1,1', 'CREATE', 'Create a PID and provide kernel information: API exists and evidence (URL) is available', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:30DC2F93', 'T31,1', 'Representative Governance - EU Researchers', 'Public declaration of representation on governance structure by member(s) of EU research community', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:380DFA45', 'T24,1', 'Statement - TRL - beta', 'Public statement of new service in beta testing is provided', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:393D1ABE', 'T32', 'Justifiable Cost', 'Publicly confirm that time-limited funds are used only for time-limited activities and that operational services are funded from membership and subscription fees.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:3DBD31CE', 'T22', 'No end user cost', 'Public evidence of cost structure or free services offered.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:42E8F8E3', 'T2,2', 'Sensitive - Indication', 'Sensitive PID Kernel Metadata can be defined - evidence is provided.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:4795479D', 'T23', 'Service Version Age - TRL', 'Number of months of operational availability of the current and previous version of PID registration service (m), compared to a benchmark b.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:4BF85947', 'T5', 'UPDATE', 'Test is the same as test T2,1', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:4FA9E52E', 'T26', 'Maintenance and Availability Provisions', 'Publish public evidence of relevant provisions', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:56666571', 'T27,3', 'Continuity Provisions - exit strategy', 'Public declaration that continued resolution is addressed in the plan', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:5AB8E036', 'T19', 'Assuring accurate entity metadata', 'Public evidence of procedures or policies at Managers.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:5C7B03BF', 'T27,2', 'Continuity Provisions - exit strategy', 'Public declaration that an exit strategy is presented in the plan', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:680AD6FD', 'T16', 'Digital Representation Exists', 'Public evidence is provided that digital representations of one or more physical entity is maintained', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:763787BB', 'T2,4', 'Secure - Access', 'Sensitive PID Kernel Metadata requires access to be granted - evidence is provided.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:7A5BB547', 'T25,1', 'Availability', 'Public assertion of availability expressed as average annual downtime d, less than 8.77 hours per year.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:7B262333', 'T6', 'Ownership Transfer', 'Public evidence of a contract or procedure that specifies ownership transfer provisions.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:803EE3F7', 'T11', 'Versioning Policy', 'Public evidence of versioning policy.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:84C20BC3', 'T8', 'Use case guidance', 'Public evidence is available of community guidance on appropriate granularity level and application in one or more use cases.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:87B31839', 'T10', 'Versioning support', 'Public evidence of versioning support in Kernel Information Profile or in user guidance.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:8948E1FF', 'T21', 'Integration with EU RIs', 'Test is one or more evidentiary URLs to demonstrate use of the Service in a RI. Each instance is counted.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:8CC932F5', 'T17', 'Community Involvement - Kernel Information Profiles', 'Public evidence of community involvement exists.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V)
VALUES ('pid_graph:8E780AE0', 'T34', 'Persistence Mean', 'The test involves a random statistically significant sample for a provider, and determining a distribution of resolvable PIDs as a function of time since creation.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:9F1A6267', 'T31,2', 'Representative Governance - Evidence', 'Public evidence is available of composition of governance structures', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:A3DB5ACC', 'T27,1', 'Continuity Provisions - plan', 'Publish public evidence of a continuity plan', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:A4289B84', 'T2,1', 'Secure - Encrypted', 'API services are encrypted (HTTPS)', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:A78D63FF', 'T20', 'Services are Open', 'Services (Providers) need to supply public evidence of open availability of services.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:A9BC1377', 'T7', 'Conformance Test', 'Testing that the relation between PID and entity, maintained by a manager, is conformant with Authority requirements. Existence of public evidence (declaration) is required.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:B0DD9C10', 'T1,3', 'Resolution Service', 'Resolution API (URL) or URI Pattern exists, evidence is provided', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:B1078ED4', 'T4', 'Maintenance', 'A test to determine if the entity (PID) attributes are being maintained.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:B2BE4D4A', 'T1,2', 'UPDATE', 'Update kernel information for existing PID: API exists and evidence (URL) is available', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:BB904261', 'T12', 'PID cannot be deleted', 'Public evidence is provided of the fact that the PID will never be deleted.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:C4CF831F', 'T25,2', 'Availability', 'Heartbeat monitoring of a service endpoint designated by the Service, expressed as average annual downtime d, less than 8.77 hours per year.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:CE66C45C', 'T9,2', 'Community Engagement - Subscriber Assembly', 'Public evidence is provided of periodic member or subscriber assemblies.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:D9B829C4', 'T3', 'Ownership is visible', 'A test determines if an ownership attribute is available for the PID. Evidence is provided of a mechanism to retrieve this information.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:E54B2EEA', 'T35', 'Resolution Percentage', 'The test involves determining the percentage f of resolved PIDs that result in a viable entity, compared to a community expectation p.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:F3AE497B', 'T2,3', 'Secure - Encrypted Kernel Metadata', 'Sensitive PID Kernel metadata can be encrypted - evidence is provided.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:F5AB7638', 'T28', 'Certification', 'Public declaration of willingness to be certified', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:F5E89C19', 'T9,1', 'Community Engagement - User Forum', 'Public evidence is provided of a periodic user forum.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:F8D89E9B', 'T2,5', 'Secure - Authentication', 'Sensitive PID Kernel Metadata requires users to be authenticated - evidence is provided.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:FCDFFCBD', 'T29', 'Contract - Services and Managers', 'Evidence of a contract between Services and Managers exists - URL is available.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, lodTES_V)
VALUES ('pid_graph:9772C39A', 'T13,2', 'PID Persistence - Service - Auto Evidence', 'An inventory of PIDs issued by the Authority on behalf of the Service a is compared to the inventory of PIDs published by the service s and the ratio is larger than a benchmark b determined by the community.', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Test (lodTES, TES, labelTest, descTest, lodMTV, upload, dataType, lodTES_V)
VALUES ('pid_graph:B00F4C34', 'T36,1', 'ORCID Use', 'The test determines whether ORCIDs are in use to reference researchers.', 'pid_graph:34A9189F', '2024-09-09', 'Test', NULL);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:AB6189E8', 'Auto-1', 'Auto-1', 'The automated ', 'pid_graph:5EB0885A', 1);

INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:3A4592E8', 'Test 3', 'Publish Community Documents', 'AA Operator collected and published the community documents for the benefit of Relying Parties',  'pid_graph:5EB0885A',1);


