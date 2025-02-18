-- ------------------------------------------------
-- Version: v1.23
--
-- Description: Migration that introduces the p_Test_Definition table
-- -------------------------------------------------

-- p_Test_Definition table
CREATE TABLE p_Test_Definition (
    lodTDF VARCHAR(255) NOT NULL,
    lodTME VARCHAR(255) NOT NULL,
    labelTestDefinition VARCHAR(255) NOT NULL,
    paramType VARCHAR(255) NOT NULL,
    testParams VARCHAR(255) NOT NULL,
    testQuestion VARCHAR(255) NOT NULL,
    toolTip TEXT NOT NULL,
    lodMTV VARCHAR(255) DEFAULT NULL,
    lodTES VARCHAR(255) DEFAULT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTDF_V VARCHAR(255) DEFAULT NULL,
    upload DATE,
    dataType VARCHAR(255),
    PRIMARY KEY (lodTDF),
    FOREIGN KEY (lodTME) REFERENCES t_TestMethod(lodTME)
);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:03591DB3', 'pid_graph:B733A7D5', 'Manual confirmation of user authentication required for access to sensitive metadata.', 'onscreen', 'userAuth|evidence', 'Does access to Sensitive PID Kernel Metadata require user authentication?|Provide evidence of this provision via a link to a specification, user guide, or API definition.', 'Users need to be authenticated and requisite permissions must apply for access to sensitive metadata|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:F8D89E9B', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:087DE082', 'pid_graph:B733A7D5', 'Manual confirmation of machine-readable metadata availability', 'onscreen', 'machineMeta|evidence', 'Can you provide publicly available instructions (URL pattern, API call, code example) to prove that metadata is available in machine-readable format from the resolution target?|Link to evidence:', 'Metadata should be available in machine-accessible format|A document, web page, or publication describing the options', 'pid_graph:3E109BBA', 'pid_graph:166A398F', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:0CE21FB1', 'pid_graph:B733A7D5', 'Manual confirmation of physical entity representation', 'onscreen', 'physEntity|evidence', 'Does your service allow physical entities to be represented digitally? |Can you provide public evidence that describes how this is done? ', 'If you allow physical entities to be referenced by PIDs, they should have a reasonably well-defined digital equivalent in metadata|A document, web page, or publication describing the approach is needed', 'pid_graph:3E109BBA', 'pid_graph:680AD6FD', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:0EFD4F79', 'pid_graph:A57F7707', 'Manual confirmation of country count where service is not available', 'onscreen', 'numCountries', 'Please indicate the number of countries world-wide where your service is not available', 'This will be compared to a reasonable benchmark.', 'pid_graph:3E109BBA', 'pid_graph:1E984FD2', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:12B17921', 'pid_graph:B733A7D5', 'Manual confirmation of periodic user engagement', 'onscreen', 'communityEngage|evidence', 'Do you engage the community?|Can you provide public evidence of community engagement via a link to a description or similar?', 'Any general community engagement applies|A document, web page, or publication describing events or arrangements', 'pid_graph:3E109BBA', 'pid_graph:8CC932F5', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:1EC6CFCB', 'pid_graph:B733A7D5', 'Manual confirmation of PIDs cannot be deleted - Provider', 'onscreen', 'neverDelete|evidence', 'Do you ensure that the PID will never be deleted?|Can you provide public evidence of this?', 'PIDs must always remain registered in the PID Stack, usually with the Authority and/ or Provider|A document, web page, or publication describing measures taken', 'pid_graph:3E109BBA', 'pid_graph:030489E8', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:260CABBE', 'pid_graph:F164D1F8', 'Manual confirmation of PID non-deletion', 'onscreen', 'numerator|denominator', 'Provide the number of PIDs recorded to date by the Authority on behalf of your provider service (A)|Provide the number of PIDs recorded to date by your Service (S)', 'The ratio of S/A will be compared with the community expectation, reflecting that a large percentage of PIDs registered with the authority is used and maintained by the service.', 'pid_graph:3E109BBA', 'pid_graph:9772C39A', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:35A86F42', 'pid_graph:B733A7D5', 'Manual confirmation of free end use', 'onscreen', 'costModel|evidence', 'Can you provide public documentation of your cost structure or fee model and how it results in no costs for end users wishing to requisition, register and resolve PIDs?|Link to evidence:', 'End user costs are typically free, with premium services requiring paid membership or subscription|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:3DBD31CE', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:36B9D20E', 'pid_graph:C62DD0BB', 'Automated confirmation of availability', 'path', 'method|requestData|responseVariable', 'API call - hours of unplanned average annual downtime|Request JSON object|Response element that contains the unplanned downtime value', 'Please provide the API call that will return the number of unplanned average annual downtime in hours for the most recent measurement period.|Provide the JSON object with relevant input data to extract the downtime value from the API|Indicate which element in the JSON response contains the unplanned downtime value', 'pid_graph:3E109BBA', 'pid_graph:C4CF831F', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:374E9272', 'pid_graph:8D79984F', 'Manual confirmation of continued resolution provisions', 'onscreen', 'ctdResolution', 'Do you confirm that continued resolution is addressed in the plan?', 'Consult the guidance for more details', 'pid_graph:3E109BBA', 'pid_graph:56666571', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:3C71F9F7', 'pid_graph:23DBAC12', 'Manual confirmation of technology readiness level - value-added services', 'onscreen', 'trLevel', 'Please provide a value for the technology readiness of any new or value-added services that are available.', 'Technology Readiness Levels: TRL8 (Value=8): Services are usually made available after they have been fully beta-tested, and are ready for operational release TRL7 (Value=7): Services are usually made available for community testing or testing by subscribers as a beta version', 'pid_graph:3E109BBA', 'pid_graph:380DFA45', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:3E748FA1', 'pid_graph:B733A7D5', 'Manual confirmation that type information is available', 'onscreen', 'typeVerify|evidence', 'Do you provide a pathway (guidance) or published API call to verify the type?|If so, please provide a link to documentation, API specifications, or guidance.', 'Authorities and/ or Providers should provide a means of verifying the PID type|A document, web page, or publication describing how to do this', 'pid_graph:3E109BBA', 'pid_graph:0FB9EC33', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:402853DB', 'pid_graph:B733A7D5', 'Can sensitive PID Kernel Metadata be indicated and is it use supported?', 'onscreen', 'sensitiveMeta|evidence', 'Can sensitive PID Kernel Metadata be indicated and is it use supported?|Please provide a link to documentation or a schema where this feature can be confirmed.', 'Sensitive metadata can be indicated in kernel metadata-either by default or on demand|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:42E8F8E3', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:4079C258', 'pid_graph:C62DD0BB', 'Automated confirmation of resolution percentage', 'path', 'method|requestData|responseVariable', 'API call - resolvability percentage|Request JSON object|Response element that contains the resolvability percentage value', 'Please provide the API call that will return the resolvability percentage.|Provide the JSON object with relevant input data to extract the resolvability percentage from the API|Indicate which element in the JSON response contains the resolution percentage value. The test involves determining the percentage f of resolved PIDs that result in a viable entity, compared to a community expectation p.', 'pid_graph:3E109BBA', 'pid_graph:E54B2EEA', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:40C30444', 'pid_graph:B733A7D5', 'Manual confirmation of existence of a resolver API or a web page that serves as a UI for resolution.', 'onscreen', 'resolverExists|evidence', 'Can you provide evidence that a resolution API (URL) or a URI pattern that will resolve the PID via a web page exists?|Provide evidence of that via a URL to an API definition or a web page for manual resolution.', 'A resolution API or HTTP pattern that resolves to the target is available|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:B0DD9C10', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:445FE1FD', 'pid_graph:B733A7D5', 'Manual confirmation that PID-related data is openly available', 'onscreen', 'openData|evidence', 'Can you supply public evidence of open availability of your service?|Link to evidence:', 'PID kernel metadata should be openly available, except for sensitive elements|A document, web page, or publication describing the plan', 'pid_graph:3E109BBA', 'pid_graph:A78D63FF', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:4E775F93', 'pid_graph:A57F7707', 'Manual confirmation of technology readiness level - main services', 'onscreen', 'operationMonths', 'For your main PID services (creation, maintenance, and resolution), can you indicate how many months it has been operationally available?', 'Provide a number in months', 'pid_graph:3E109BBA', 'pid_graph:4795479D', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:4F0F1EA5', 'pid_graph:8D79984F', 'Manual confirmation of encryption of sensitive metadata.', 'onscreen', 'metaEncryption', 'Is sensitive PID Kernel Metadata encryption supported?', 'Sensitive metadata is encrypted and managed separately, or all metadata is encrypted and open elements decrypted by default', 'pid_graph:3E109BBA', 'pid_graph:F3AE497B', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:529154B3', 'pid_graph:B733A7D5', 'Manual confirmation of API for PID creation and kernel metadata capability', 'onscreen', 'apiExists|evidence', 'Do you support creation of PIDs with kernel information via an API? |Provide evidence of that via a URL to an API definition or documentation.', 'API-based methods are available for creation of a PID and associated kernel metadata|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:29D74907', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:5697A0FC', 'pid_graph:8D79984F', 'Manual confirmation of metadata maintenance', 'onscreen', 'metaMaintain', 'Do you maintain the metadata for your object as and when required?', 'Owners maintain and update metadata as and when required', 'pid_graph:3E109BBA', 'pid_graph:B1078ED4', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:5C4D034D', 'pid_graph:B733A7D5', 'Manual confirmation that a management policy is available', 'onscreen', 'mnmtPolicy|evidence', 'Do you provide a pathway (guidance) or published API call to obtain the management policy?|If so, please provide a link to documentation, API specifications, or guidance.', 'Management and retention policies should be easily available|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:1C02006A', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:5E148A19', 'pid_graph:B733A7D5', 'Manual confirmation of periodic user forums', 'onscreen', 'userForum|evidence', 'Do you arrange periodic user forums or similar events?|Can you provide public evidence of such events?', 'Many providers arrange periodic user forums or present such forums at regional and international events|A document, web page, or publication describing such events', 'pid_graph:3E109BBA', 'pid_graph:F5E89C19', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:6741174B', 'pid_graph:C62DD0BB', 'Automated confirmation of mean persistence', 'path', 'method|requestData|responseVariable', 'API call - median persistence|Request JSON object|Response element that contains the median persistence value', 'Please provide the API call that will return the median persistence value.|Provide the JSON object with relevant input data to extract the persistence value from the API|Indicate which element in the JSON response contains the persistence value. The value reflects the mean time it takes for persistence to fail for all PIDs issued on behalf of an Actor (your organization)', 'pid_graph:3E109BBA', 'pid_graph:8E780AE0', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:71AD9FFE', 'pid_graph:8D79984F', 'Manual confirmation of exit strategy', 'onscreen', 'exitStrategy', 'Do you confirm that an exit strategy is presented in the plan?', 'Consult the guidance for more details', 'pid_graph:3E109BBA', 'pid_graph:5C7B03BF', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:729336E7', 'pid_graph:17B5AFED', 'Manual confirmation of availability', 'onscreen', 'downTime', 'Please provide the unplanned average annual downtime in hours for the most recent measurement period.', 'Number of hours per year of unplanned downtime or service unavailability', 'pid_graph:3E109BBA', 'pid_graph:7A5BB547', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:77D54507', 'pid_graph:B733A7D5', 'Manual confirmation of periodic member or subscriber engagement', 'onscreen', 'memberEvents|evidence', 'Do you arrange periodic member or subscriber assemblies?|Can you provide public evidence of such arrangements?', 'Typically member or subscriber annual general meetings|A document, web page, or publication describing such meetings', 'pid_graph:3E109BBA', 'pid_graph:CE66C45C', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:7B428EA4', 'pid_graph:B733A7D5', 'Manual confirmation of granularity guidance', 'onscreen', 'useGranularity|evidence', 'Is community guidance on appropriate granularity of PID use and application in one or more use cases available?|Can you provide public evidence of this?', 'Users are guided to selection of appropriate granularity for entities described by PIDs|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:84C20BC3', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:7D7BA336', 'pid_graph:B733A7D5', 'Manual confirmation of use of service by ERICs', 'onscreen', 'ericUse|evidence', 'Can you provide one or more evidentiary URLs to demonstrate use of the Service in a European Research Infrastructure?|Link to evidence:', 'European research infrastructures should use your services, for example ERICs|A document, web page, search result, or publication describing a use by an ERIC, for example', 'pid_graph:3E109BBA', 'pid_graph:8948E1FF', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:81E333ED', 'pid_graph:B733A7D5', 'Manual confirmation that PIDs cannot be deleted - Authority', 'onscreen', 'neverDelete|evidence', 'Do you ensure that the PID will never be deleted?|Can you provide public evidence of this?', 'PIDs must always remain registered in the PID Stack, usually with the Authority and/or Provider|A document, web page, or publication describing measures taken', 'pid_graph:3E109BBA', 'pid_graph:BB904261', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:84A46E80', 'pid_graph:B733A7D5', 'Manual confirmation of resolution conformance', 'onscreen', 'relationConforms|evidence', 'Do the relations between PIDs and entities, as maintained by the PID manager, conform to the Authority requirements?|Can you provide evidence of this?', 'Authorities usually prescribe the mechanisms and expectations for maintenance of the link between PIDs and the target(s) for resolution|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:A9BC1377', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:88937FC8', 'pid_graph:8D79984F', 'Manual confirmation of adequacy of structural funding', 'onscreen', 'strucFunds', 'Can you confirm that time-limited funds are used only for time-limited activities and that operational services are funded from structural income such as membership and subscription fees?', 'Consult the guidance for more details', 'pid_graph:3E109BBA', 'pid_graph:393D1ABE', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:8C72C972', 'pid_graph:8C72C972', 'Manual confirmation of resolution percentage', 'onscreen', 'percentResolution', 'Please provide an estimate of the percentage of PIDs managed by you that resolve at the present time. This will be compared to a community benchmark.', 'Provide a percentage value - between 0 and 100', 'pid_graph:3E109BBA', 'pid_graph:0E7400CB', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:A2F9F95B', 'pid_graph:B733A7D5', 'Manual confirmation of ownership transfer options', 'onscreen', 'ownerTransfer|evidence', 'Are the specifics of the ownership transfer provisions publicly available through a contract or procedure, or is guidance provided on how to do this?|Please provide evidence of documentation, an API specification, procedure, or other mechanism', 'Ownership can be transferred and it is clear how to do so|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:7B262333', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:A322B4E9', 'pid_graph:B733A7D5', 'Manual confirmation of versioning policy availability', 'onscreen', 'versionPolicy|evidence', 'Is your versioning policy publicly available?|Please provide a link', 'Examples would be a policy or guidance aimed at PID managers and owners|A document, web page, or publication describing guidance', 'pid_graph:3E109BBA', 'pid_graph:803EE3F7', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:B1CC8397', 'pid_graph:B733A7D5', 'Manual confirmation of policies and procedures aimed at Managers', 'onscreen', 'managerPolicy|evidence', 'Can you provide evidence of policies and/or procedures aimed at managers?|Link to evidence:', 'A document, web page, or publication describing the plan|A document, web page, or publication describing the plan', 'pid_graph:3E109BBA', 'pid_graph:5AB8E036', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:BE43C039', 'pid_graph:8D79984F', 'Manual confirmation of password protection for sensitive metadata', 'onscreen', 'pwNeeded', 'Is access to Sensitive PID Kernel Metadata password protected?', 'Users need to sign in to view sensitive metadata', 'pid_graph:3E109BBA', 'pid_graph:763787BB', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:C04A702B', 'pid_graph:B733A7D5', 'Manual confirmation of clear division of responsibilities', 'onscreen', 'contractExists|evidence', 'Do you clearly define contractual responsibilities of your service and what is expected of managers?|Can you provide evidence of this via a website, document, or publication?', 'A document, web page, or publication describing the responsibilities|A document, web page, or publication describing the responsibilities', 'pid_graph:3E109BBA', 'pid_graph:FCDFFCBD', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:C1776C75', 'pid_graph:8D79984F', 'Manual confirmation of HTTPS', 'onscreen', 'httpsAPI', 'Do you provide secure API services?', 'API services with a valid certificate and availability via HTTPS will qualify', 'pid_graph:3E109BBA', 'pid_graph:A4289B84', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:C9C1D620', 'pid_graph:B733A7D5', 'Manual confirmation that PID ownership is visible', 'onscreen', 'pidOwner|evidence', 'Does the PID have an ownership attribute, and can it be retrieved by anyone?|Provide evidence by way of a link to documentation, API definition, user guides, or schema definition where this can be confirmed.', 'The owner of a PID is publicly visible or can be retrieved by anyone|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:D9B829C4', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:CD14BCFA', 'pid_graph:B733A7D5', 'Manual confirmation of maintenance and availability provisions', 'onscreen', 'mtnProvision|evidence', 'Do you follow documented procedures to ensure maintenance and availability of services?|Can you provide public evidence of relevant provisions?', 'A document, web page, or publication describing provisions|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:4FA9E52E', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:D5210088', 'pid_graph:B733A7D5', 'Manual confirmation of participation in global governance structures', 'onscreen', 'glblGovernance|evidence', 'Can you confirm global governance participation?|Provide a URL to public evidence of your involvement in such structures.', 'A document, web page, or publication describing such involvement|A document, web page, or publication describing such involvement', 'pid_graph:3E109BBA', 'pid_graph:03626415', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:E4CED2E9', 'pid_graph:B733A7D5', 'Manual confirmation of willingness to be certified', 'onscreen', 'declareCertification|evidence', 'Do you currently have a publicly available declaration of your willingness to be certified in respect of EOSC PID Policy compliance?|Can you provide public evidence of such a declaration?', 'A document, web page, or publication describing the intention|A document, web page, or publication describing the intention', 'pid_graph:3E109BBA', 'pid_graph:F5AB7638', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:E5C931F6', 'pid_graph:B733A7D5', 'Manual confirmation of API for updates to kernel metadata', 'onscreen', 'apiUpdate|evidence', 'Do you support updating of PID kernel information via an API?|Provide evidence of that via a URL to an API definition or documentation.', 'API methods are available for kernel metadata updates|A document, web page, or publication describing provisions', 'pid_graph:3E109BBA', 'pid_graph:B2BE4D4A', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:EA18FF7F', 'pid_graph:23DBAC12', 'Manual confirmation of technology readiness level - experimental services', 'onscreen', 'trLevel', 'Do you ever release systems or services on an experimental basis? If so, please note a Technology Readiness Level of at least 6 for these.', 'Technology Readiness Levels: TRL8 (Value=8): Services are usually made available after they have been fully beta-tested, and are ready for operational release TRL7 (Value=7): Services are usually made available for community testing or testing by subscribers as a beta version TRL6 (Value=6): Technology demonstrated in relevant environment', 'pid_graph:3E109BBA', 'pid_graph:07064CA5', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:ECE3D887', 'pid_graph:B733A7D5', 'Manual confirmation of versioning support', 'onscreen', 'versionSupport|evidence', 'Do you provide versioning support in your Kernel Information Profile or in user guidance?|Can you provide public evidence of this?', 'Guidance and advice on how to implement versioning, depending on the user''s objectives|A document, web page, or publication describing guidance', 'pid_graph:3E109BBA', 'pid_graph:87B31839', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:F5B74BA4', 'pid_graph:B733A7D5', 'Manual confirmation of community participation in governance', 'onscreen', 'cmtyGovernance|evidence', 'Do you confirm that community participation is possible in governance structures?|Please provide a link to evidence', 'A document, web page, or publication describing such arrangements|A document, web page, or publication describing such arrangements', 'pid_graph:3E109BBA', 'pid_graph:9F1A6267', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:F68AEF90', 'pid_graph:B733A7D5', 'Manual confirmation of continuity planning', 'onscreen', 'planContinuity|evidence', 'Have you developed a continuity plan?|Can you provide public evidence of such a continuity plan?', 'A document, web page, or publication describing the plan|A document, web page, or publication describing the plan', 'pid_graph:3E109BBA', 'pid_graph:A3DB5ACC', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:FB5CB3D4', 'pid_graph:8D79984F', 'Manual confirmation of EU research involvement in governance', 'onscreen', 'euResearch', 'Are member(s) of the EU research community represented in your governance structures?', 'Consult the guidance for more details', 'pid_graph:3E109BBA', 'pid_graph:30DC2F93', NULL);

INSERT INTO p_Test_Definition
(lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V, upload, dataType)
VALUES  ('pid_graph:92116C38', 'pid_graph:B733A7D5', 'Manual confirmation of implementation of ORCID for Research References',
 'onscreen', '"use_orcid"|"evidence"', '"Can you confirm that you are using and promoting ORCID for identification of Researchers?"|"Provide a URL to public evidence of such use and/or promotion."',
 'A document, web page, or publication describing or demonstrating use of ORCID', 'pid_graph:34A9189F', NULL, NULL, NULL, NULL);

