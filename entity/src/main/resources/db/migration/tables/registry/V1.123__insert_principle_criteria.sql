
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:A1141612','PA1', 'PA1', 'Attribute Management and Attribute Release');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:5PR0882I','PA2', 'Site security', 'There is a need for a generic, global PID resolution system across all PID systems and service providers.');


INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('CA1', 'pid_graph:123D4203', 'pid_graph:293B1DEE', 'pid_graph:4A47BB1A', 'CA1', 'semantics, lifecycle, data protection, and release policy of attributes');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('CA2', 'pid_graph:153D4203', 'pid_graph:293B1DEE', 'pid_graph:4A47BB1A', 'CA2', 'Community documents for the benefit of Relying Parties.');
INSERT INTO p_Criterion (lodCRI, CRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('pid_graph:5CR0881I', 'CA3', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Criterion', 'The AA Operator should document the physical site security controls and maintain them in a state consistent with the security requirements of the hosted Communities.');

INSERT INTO p_Principle_Criterion (principle_lodPRI, criterion_lodCRI, lodMTV_X, lodREL, motivation_lodMTV, annotationURL, lodP_C_V) VALUES ('pid_graph:A1141612', 'pid_graph:123D4203', 'pid_graph:5EB0885A', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A', NULL, 1);
INSERT INTO p_Principle_Criterion (principle_lodPRI, criterion_lodCRI, lodMTV_X, lodREL, motivation_lodMTV, annotationURL, lodP_C_V) VALUES ('pid_graph:A1141612', 'pid_graph:153D4203', 'pid_graph:5EB0885A', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A', NULL, 1);
INSERT INTO p_Principle_Criterion (principle_lodPRI, criterion_lodCRI, lodMTV_X, lodREL, motivation_lodMTV, annotationURL, lodP_C_V) VALUES ('pid_graph:5PR0882I', 'pid_graph:5CR0881I', 'pid_graph:5EB0885A', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A',NULL, 1);



INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:E92B9B49', 'pid_graph:123D4203', 'pid_graph:293B1DEE','pid_graph:5EB0885A','pid_graph:5EB0885A',1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:E92B9B49', 'pid_graph:153D4203', 'pid_graph:293B1DEE','pid_graph:5EB0885A','pid_graph:5EB0885A',1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP, motivation_lodMTV, lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:E92B9B49', 'pid_graph:5CR0881I', 'pid_graph:2981F3DD','pid_graph:5EB0885A','pid_graph:5EB0885A',1);


INSERT p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:121489E8', 'Test A1.1', 'Test A1.1', 'The Community must define and document the semantics, lifecycle, data protection, and release policy of attributes stored or asserted by the AA',  'pid_graph:5EB0885A',1);
INSERT p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:123489E1', 'Test A1.2', 'Test A1.2', 'The AA Operator must implement the community definitions as defined and documented, for all the AAs it operates', 'pid_graph:5EB0885A',1);
INSERT p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:76489E11', 'Test A2.1', 'Test A2.1', 'Has the AA Operator collected and published the community documents for the benefit of Relying Parties.', 'pid_graph:5EB0885A',1);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V) VALUES ('pid_graph:4A1F1EA5', 'pid_graph:8D79984F', 'Community semantics, lifecycle, data protection, and release policy of attributes', 'onscreen', '"commPol"|"evidence"', '"Has the Has the Community defined and documented the semantics, lifecycle, data protection, and release policy of attributes stored or asserted by the AA?"| "Provide evidence of that via a URL to a page or to a documentation"','"Commnunity documetns  for semantics, lifecycle, data protection, and release policy"|"A document, web page, or publication describing provisions"', 'pid_graph:5EB0885A', 'pid_graph:121489E8', NULL);
INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V) VALUES ('pid_graph:123F1EA5', 'pid_graph:8D79984F', 'Implementation of  the community definitions for the AAs community operates, defined by the AA Operator', 'onscreen', '"opCommDef"|"evidence"', '"Has the AA Operator implement the community definitions as defined and documented, for all the AAs it operates."','"AA Operator community definitions as defined and documented"','pid_graph:5EB0885A', 'pid_graph:123489E1', NULL);
INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V) VALUES ('pid_graph:5D1F1EA5', 'pid_graph:8D79984F', 'AA Operator collected and published the community documents for the benefit of Relying Parties', 'onscreen', '"commDocs"|"evidence"', '"Has the AA Operator collected and published the community documents for the benefit of Relying Parties.? |Provide evidence of that via a URL to a page or to a documentation."','"Commnunity documetns are available for the Relying Parties"|"A document, web page, or publication describing provisions"', 'pid_graph:5EB0885A', 'pid_graph:5D1F1EA5', NULL);


INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V) VALUES ('CAA', 'pid_graph:8884DBA5', 'Community and AA Operator Guidelines', 'Community and AA Operator Guidelines', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F','pid_graph:5EB0885A',  NULL);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:8884DBA5', 'pid_graph:123D4203', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, Upload, DataType, lodM_TB_V)
VALUES ('pid_graph:M61DF936', 'pid_graph:8884DBA5', 1, 'pid_graph:7085006F', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', NULL, NULL, NULL, '2024-10-10', 'Test', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, Upload, DataType, lodM_TB_V)
VALUES ('pid_graph:M61DF936', 'pid_graph:8884DBA5', 2, 'pid_graph:0917EC0D', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', NULL, NULL, NULL, '2024-10-10', 'Test', 1);
