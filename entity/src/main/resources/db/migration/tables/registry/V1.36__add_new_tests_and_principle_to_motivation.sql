INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:B3341612','PA3', 'Metadata publication', 'Ensuring publication of metadata to the Community and related Relying Parties');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('MD-1', 'pid_graph:053V4203', 'pid_graph:293B1DEE', 'pid_graph:4A47BB1A', 'AA Host Metadata Publication', ' The AA Operator must publish, to the Community and related Relying Parties, at least the following metadata for each AA it hosts');
INSERT INTO p_Principle_Criterion (principle_lodPRI, criterion_lodCRI, lodMTV_X, lodREL, motivation_lodMTV, annotationURL, lodP_C_V) VALUES ('pid_graph:B3341612', 'pid_graph:053V4203', 'pid_graph:5EB0885A', 'isSupportedBy', 'pid_graph:5EB0885A', NULL, 1);

INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:E92B9B49', 'pid_graph:053V4203', 'pid_graph:293B1DEE','pid_graph:5EB0885A','pid_graph:5EB0885A',1);


INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:333489E8', 'MD-1a', 'Administrative Contact Details', 'administrative contact details for the AA Operator, including at least one role-based email address and one postal contact address',  'pid_graph:5EB0885A',1);
INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:391489E8', 'MD-1b1', 'Operational Security Contact Email', 'an operational security contact for the AA Operator, being at least a role-based email address',  'pid_graph:5EB0885A',1);
INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:341399E8', 'MD-1b2', 'Operational Security Contact Phone Number','an operational security contact for the AA Operator, preferably including a telephone number',  'pid_graph:5EB0885A',1);
INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:661267E8', 'MD-1c', 'Cryptographic Key Material', 'cryptographic key material required to verify signed messages, where that is required to validate issued attribute assertions',  'pid_graph:5EB0885A',1);


INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V) VALUES ('pid_graph:15MF1NC8', 'pid_graph:8D79984F', 'administrative contact details for the AA Operator', 'onscreen', 'admContDetails|evidence', 'Do administrative contact details for the AA Operator, including at least one role-based email address and one postal contact address exist? |Provide evidence of that via a URL to a page or to a documentation', 'Administrative contact details | A document, web page, or publication describing provisions','pid_graph:5EB0885A', 'pid_graph:333489E8', NULL);
INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V) VALUES ('pid_graph:78BF1EC1', 'pid_graph:8D79984F', 'operational security contact for the AA Operator (email)', 'onscreen', 'OpSecDetailsEmail|evidence', 'Do an operational security contact for the AA Operator, being at least a role-based email address exist? |Provide evidence of that via a URL to a page or to a documentation','operational security contact for the AA Operator|A document, web page, or publication describing provisions', 'pid_graph:5EB0885A', 'pid_graph:391489E8', NULL);
INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V) VALUES ('pid_graph:83BF1EB7', 'pid_graph:8D79984F', 'operational security contact for the AA Operator (phone)', 'onscreen', 'OpSecDetailsPhone|evidence', 'Do an operational security contact for the AA Operator, preferably including a telephone number, exist? |Provide evidence of that via a URL to a page or to a documentation','operational security contact for the AA Operator|A document, web page, or publication describing provisions', 'pid_graph:5EB0885A', 'pid_graph:341399E8', NULL);
INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V) VALUES ('pid_graph:13BF1EA9', 'pid_graph:8D79984F', 'cryptographic key material for the AA Operator', 'onscreen', 'CryptKey|evidence', 'Do cryptographic key material required to verify signed messages, where that is required to validate issued attribute assertions? |Provide evidence of that via a URL to a page or to a documentation', 'cryptographic key material required to verify signed messages|A document, web page, or publication describing provisions','pid_graph:5EB0885A', 'pid_graph:661267E8', NULL);


INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V) VALUES ('C4A', 'pid_graph:646DBA5', 'C4A Metric', 'Metadata Publication',NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F','pid_graph:5EB0885A',  NULL);

INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:646DBA5', 'pid_graph:053V4203', 'isSupportedBy', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, Upload, DataType, lodM_TB_V)
VALUES ('pid_graph:M72DF936', 'pid_graph:646DBA5', 3, 'pid_graph:7085006F', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', NULL, NULL, NULL, '2024-10-10', 'Test', 1);


INSERT INTO p_Metric_Test (metric_lodMTR, lodREL, test_lodTES, test_definition_lodTDF, motivation_lodMTV, lodM_T_TD_V, lodMTV_X)
 VALUES ('pid_graph:646DBA5', 'isSupportedBy', 'pid_graph:333489E8', 'pid_graph:15MF1NC8', 'pid_graph:5EB0885A', 1, 'pid_graph:5EB0885A');
INSERT INTO p_Metric_Test (metric_lodMTR, lodREL, test_lodTES, test_definition_lodTDF, motivation_lodMTV, lodM_T_TD_V, lodMTV_X)
 VALUES ('pid_graph:646DBA5', 'isSupportedBy', 'pid_graph:391489E8', 'pid_graph:78BF1EC1', 'pid_graph:5EB0885A', 1, 'pid_graph:5EB0885A');
INSERT INTO p_Metric_Test (metric_lodMTR, lodREL, test_lodTES, test_definition_lodTDF, motivation_lodMTV, lodM_T_TD_V, lodMTV_X)
 VALUES ('pid_graph:646DBA5', 'isSupportedBy', 'pid_graph:341399E8', 'pid_graph:83BF1EB7', 'pid_graph:5EB0885A', 1, 'pid_graph:5EB0885A');
INSERT INTO p_Metric_Test (metric_lodMTR, lodREL, test_lodTES, test_definition_lodTDF, motivation_lodMTV, lodM_T_TD_V, lodMTV_X)
VALUES ('pid_graph:646DBA5', 'isSupportedBy', 'pid_graph:661267E8', 'pid_graph:13BF1EA9', 'pid_graph:5EB0885A', 1, 'pid_graph:5EB0885A');
