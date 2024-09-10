-- ------------------------------------------------
-- Version: v1.114
--
-- Description: Migration that introduces the p_Metric_Definition
-- --

-- metric definition table

CREATE TABLE p_Metric_Definition (
    lodMDF VARCHAR(255) DEFAULT NULL,
    metric_lodMTR VARCHAR(255) NOT NULL,
    type_benchmark_lodTBN VARCHAR(255) NOT NULL,
    motivation_lodMTV VARCHAR(255) DEFAULT NULL,
    lodM_TB_V INT NOT NULL,
    valueBenchmark TEXT NOT NULL,
    lodMTV_X VARCHAR(255) NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodMDF_V VARCHAR(255) DEFAULT NULL,
    lodReference2 VARCHAR(255) NOT NULL,
    lodReference VARCHAR(255) NOT NULL,
    PRIMARY KEY (metric_lodMTR, type_benchmark_lodTBN, lodM_TB_V),
    FOREIGN KEY (type_benchmark_lodTBN) REFERENCES t_Type_Benchmark(lodTBN) ON DELETE CASCADE,
    FOREIGN KEY (metric_lodMTR) REFERENCES p_Metric(lodMTR) ON DELETE CASCADE,
    INDEX Benchmark (type_benchmark_lodTBN)
);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference,lodM_TB_V)
VALUES ('pid_graph:D61CE91', 'pid_graph:EBCEBED1', '3', 'pid_graph:0917EC0D', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference,lodM_TB_V)
VALUES ('pid_graph:D61CE910', 'pid_graph:97ECCC27', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference,lodM_TB_V)
VALUES ('pid_graph:D61CE911', 'pid_graph:0A42DC0E', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference,lodM_TB_V)
VALUES ('pid_graph:D61CE912', 'pid_graph:94CCFDA2', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference,lodM_TB_V)
VALUES ('pid_graph:D61CE913', 'pid_graph:98F6C30C', '1', 'pid_graph:8BA6B11D', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, lodM_TB_V)
VALUES ('pid_graph:D61CE914', 'pid_graph:E1B1DB5C', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, lodM_TB_V)
VALUES ('pid_graph:D61CE915', 'pid_graph:82F03288', '2', 'pid_graph:0917EC0D', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, lodM_TB_V)
VALUES ('pid_graph:D61CE916', 'pid_graph:D4463A62', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, lodM_TB_V)
VALUES ('pid_graph:D61CE917', 'pid_graph:70DCE869', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, lodM_TB_V)
VALUES ('pid_graph:D61CE918', 'pid_graph:39B0F5FE', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, lodM_TB_V)
VALUES ('pid_graph:D61CE919', 'pid_graph:4453BF54', '1', 'pid_graph:7085006F', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

INSERT INTO p_Metric_Definition (lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, lodM_TB_V)
VALUES ('pid_graph:D61CE92', 'pid_graph:78B5C3AD', '5', 'pid_graph:0917EC0D', NULL, 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', 1);

