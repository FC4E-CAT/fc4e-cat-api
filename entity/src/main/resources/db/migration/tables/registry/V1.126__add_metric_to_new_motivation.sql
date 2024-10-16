DELETE FROM p_Criterion_Metric where metric_lodMTR ='pid_graph:8884DBA5' and criterion_lodCRI= 'pid_graph:153D4203';

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V) VALUES ('CAA2', 'pid_graph:9984DBA7', 'Community and AA Operator Guidelines', 'Community and AA Operator Guidelines', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F','pid_graph:5EB0885A',  NULL);
UPDATE p_Metric set  labelMetric='Attribute Management Guidelines' , descrMetric='Attribute Management Guidelines' where lodMTR= 'pid_graph:8884DBA5';

INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:9984DBA7', 'pid_graph:153D4203', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', 1);

UPDATE p_Metric_Definition set metric_lodMTR='pid_graph:9984DBA7' where lodMDF='pid_graph:N81DF936';

UPDATE p_Metric_Test set metric_lodMTR='pid_graph:9984DBA7' where  test_lodTES='pid_graph:76489E11';


UPDATE p_Test_Definition set testParams='commPol|evidence', testQuestion='Has the Community defined and documented the semantics, lifecycle, data protection, and release policy of attributes stored or asserted by the AA? | Provide evidence of that via a URL to a page or to a documentation' , toolTip=' data protection, and release policy|A document, web page, or publication describing provisions' where lodTDF='pid_graph:4A1F1EA5';
UPDATE p_Test_Definition set testParams='opCommDef', testQuestion='Has the AA Operator implement the community definitions as defined and documented, for all the AAs it operates.' , toolTip='AA Operator community definitions as defined and documented' where lodTDF='pid_graph:123F1EA5';
UPDATE p_Test_Definition set testParams='commDocs|evidence', testQuestion='Has the AA Operator collected and published the community documents for the benefit of Relying Parties.?|Provide evidence of that via a URL to a page or to a documentation.' , toolTip='Commnunity documetns are available for the Relying Parties|A document, web page, or publication describing provisions' where lodTDF='pid_graph:5D1F1EA5';
