-- ------------------------------------------------
-- Version: v1.15
--
-- Description: Migration that introduces the t_Type_Metric table
-- -------------------------------------------------

-- type metric table
CREATE TABLE t_Type_Metric (
    lodTMT VARCHAR(255) NOT NULL,
    TMT VARCHAR(255) NOT NULL,
    labelTypeMetric VARCHAR(255) NOT NULL,
    descTypeMetric VARCHAR(255) NOT NULL,
    descMetricApproach TEXT NOT NULL,
    descBenchmarkApproach TEXT NOT NULL,
    lodTCO VARCHAR(255) NOT NULL,
    uriTypeMetric VARCHAR(255) DEFAULT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTMT_V VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (lodTMT)
);

ALTER TABLE t_Type_Metric ADD CONSTRAINT fk_type_metric_reproducibility FOREIGN KEY (lodTCO) REFERENCES t_Type_Reproducibility (lodTCO);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:03615660', 'OBR', 'Objective Ranking', 'Ranking against peers through quantitative methods, e.g. pairwise comparison',
'The test method assumes that all instances of a population are measured together, or measurements for all exist. By comparing an individual instance against each other instance and selecting the best performing instance, an objective ranking is obtained. Pairwise comparisons are also often combined for more than one assessor or observer.',
'The benchmark determines whether performance is acceptable, often with a mapping to benchmark categories.', 'pid_graph:2BD477E1',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/727ac779-e0d5-451c-ad69-975337bc17ac', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:191014CA', 'SRV', 'Subjective Ranking', 'Assessment by assigning a ranking to a compliance claim or description (subjectively)',
'The metric is documented in the sense that the assessor is presented with one or more considerations and ranks them subjectively.',
'The assessor determines whether performance across the typical considerations is acceptable or not based on knowledge and experience.', 'pid_graph:8D254805',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/7efe56be-96f4-4707-873c-47aa8bbe519b', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:35966E2B', 'ORV', 'Open Review', 'No guidelines are present, and reviewers make a completely subjective and independent conclusion about performance.',
'The metric is undocumented, the assessor makes a subjective ''measurement'' of performance',
'The assessor determines whether performance is acceptable or not based on knowledge and experience.', 'pid_graph:8D254805',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/a1f53889-4a3d-46f7-852d-8929e41b5591', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:8720C485', 'CLS', 'Classification', 'Supervised or unsupervised classification based on ML mechanisms or heuristics',
'A number of test values or characteristics of a subject are combined into a classification heuristic, often by developing such a heuristic from learning examples. Neural networks are one example of such an approach.',
'The benchmark determines whether performance is acceptable, often with a mapping to benchmark categories.', 'pid_graph:4F179B32',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/1078e3c9-2c9a-4589-b5b9-512f0950c8ab', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:8C72C972', 'COM', 'Composite Assessment', 'Aggregation of a collection/ hierarchy of weighted and normalised measures. Includes ranking, multicriteria analysis, and analytic hierarchy methods.',
'A mechanism is provided whereby a metric is composed of other metrics and/ or tests, and the combination of results is based on some form of weighted aggregation. Simple methods sum the results over several tests and/ or metrics and implicitly weight all equally.',
'The benchmark determines whether performance is acceptable, often with a mapping to benchmark categories.', 'pid_graph:2BD477E1',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/7265c2e4-2532-4e8d-b522-3239e57d7804', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:8C8B207B', 'BRV', 'Benchmarked Review', 'As for guided review, but it is possible to select a level of compliance by matching with described performance or examples',
'The metric is documented in the sense that the assessor is advised on which considerations apply to a subjective evaluation of performance, and the result is not always documented.',
'The benchmark describes which of the considerations are considered to be important, non-negotiable, or optional, and the assessment is made by the assessor based on experience and mapping of subjective considerations to the benchmarks - which may describe recommended or best practices.', 'pid_graph:2BD477E1',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/2f9d84d8-3170-42ef-ab1a-986eb5f2f900', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:8D79984F', 'BIN', 'Binary', 'True or false, for example by determining whether there is evidence supporting a compliance claim',
'The metric is documented, based on reproducible tests, and it is clear what will constitute a true or false result when considering performance across one or more tests.',
'The benchmark determines whether performance is acceptable, but it is often a trivial case - true being acceptable (''Pass''), and false being unacceptable (''Fail'').', 'pid_graph:AAC19C15',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/182172d5-5cb4-4e68-8a0c-2148af706dbe', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:C62DD0BB', 'VAL', 'Measured or Computed Value', 'Measuring objectively using an instrument and/ or a method, result is a defined variable - for example ''number of employees''',
'The metric is documented, including reproducible tests, and it is clear how a value will be computed for performance across one or more tests.',
'The metric is documented, including reproducible tests, and it is clear how a value for performance will be computed from one or more tests.', 'pid_graph:4F179B32',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/4946e842-1db6-4cb6-910a-b61ed8905528', NULL);

INSERT INTO t_Type_Metric (lodTMT, TMT, labelTypeMetric, descTypeMetric, descMetricApproach, descBenchmarkApproach, lodTCO, uriTypeMetric, lodTMT_V)
VALUES ('pid_graph:DFAB1578', 'GRV', 'Guided Review', 'Guidance on how to approach assessment of compliance is available',
'The metric is documented in the sense that the assessor is advised on which considerations are in scope for a subjective evaluation of performance, and the result is not always documented.',
'The assessor determines whether performance across the typical considerations is acceptable or not based on knowledge and experience.', 'pid_graph:2BD477E1',
'https://mscr-test.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/1dd3b7d7-24e2-4e27-9a63-f0974f43279b', NULL);
