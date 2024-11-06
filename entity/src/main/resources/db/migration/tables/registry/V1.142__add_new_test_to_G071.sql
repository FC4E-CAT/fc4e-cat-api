INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:32ED4718','AUTO', 'Automated tests', 'Ensuring that some of the required rules are tested.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('AUTO-1', 'pid_graph:145C7355', 'pid_graph:293B1DEE', 'pid_graph:4A47BB1A', 'Check Validity', 'Validity Checks');

INSERT INTO p_Principle_Criterion (principle_lodPRI, criterion_lodCRI, lodMTV_X, lodREL, motivation_lodMTV, annotationURL, lodP_C_V) VALUES ('pid_graph:32ED4718', 'pid_graph:145C7355', 'pid_graph:5EB0885A', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A', NULL, 1);
INSERT p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:AB6189E8', 'Auto-1', 'Auto-1', 'The automated ',  'pid_graph:5EB0885A',1);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V) VALUES ('AUTO-Url-Check', 'pid_graph:3334DBA5', 'Automated Http check', 'Automated Http check', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F','pid_graph:5EB0885A',  NULL);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:3334DBA5', 'pid_graph:145C7355', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, Upload, DataType, lodM_TB_V)
VALUES ('pid_graph:T31DF931', 'pid_graph:3334DBA5', 1, 'pid_graph:7085006F', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', NULL, NULL, NULL, '2024-10-10', 'Test', 1);
INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment)
VALUES
('pid_graph:B1C2AAEC', 'B1C2AAEC', 'Auto-Check-Url-Binary', 'A test is executed automatically, returning a single binary value as a result', 'pid_graph:binary', 'pid_graph:auto', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "path",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "url"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p3",
        "schema": {
          "type": "boolean"
        }
      }
    ]
}');

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:C4CF824E', 'pid_graph:B1C2AAEC', 'Automated confirmation of https validity', 'path', '“urlCheck”', '“Url to check if it under https”', '“Please provide a valid url that is used by the service”', 'pid_graph:5EB0885A', 'pid_graph:AB6189E8', NULL);

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:3334DBA5', 'dcterms:isRequiredBy', 'pid_graph:AB6189E8', 'pid_graph:C4CF824E', 'pid_graph:5EB0885A', 1, 'pid_graph:5EB0885A');
