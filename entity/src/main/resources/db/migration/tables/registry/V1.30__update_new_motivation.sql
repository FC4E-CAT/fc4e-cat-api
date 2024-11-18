
update p_Principle set labelPrinciple='Attribute Management and Attribute Release' where lodPRI='pid_graph:A1141612';
update p_Principle set labelPrinciple='Site security' where lodPRI='pid_graph:5PR0882I';

update p_Criterion set labelCriterion='Attribute Policy' where lodCRI='pid_graph:123D4203';

update p_Criterion set labelCriterion='Community Documents' where lodCRI='pid_graph:153D4203';

update p_Criterion set labelCriterion='Site Security Controls' where lodCRI='pid_graph:5CR0881I';

INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:8884DBA5', 'pid_graph:153D4203', 'dcterms:isRequiredBy', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', 1);

DELETE FROM p_Metric_Definition where lodMDF='pid_graph:M61DF936';

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, Upload, DataType, lodM_TB_V)
VALUES ('pid_graph:M61DF936', 'pid_graph:8884DBA5', 1, 'pid_graph:7085006F', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', NULL, NULL, NULL, '2024-10-10', 'Test', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, Upload, DataType, lodM_TB_V)
VALUES ('pid_graph:N81DF936', 'pid_graph:8884DBA5', 2, 'pid_graph:0917EC0D', 'pid_graph:5EB0885A', 'pid_graph:5EB0885A', NULL, NULL, NULL, '2024-10-10', 'Test', 1);