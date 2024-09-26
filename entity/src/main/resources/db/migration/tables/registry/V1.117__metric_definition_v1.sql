-- ------------------------------------------------
-- Version: v1.117
--
-- Description: Migration that modifies the p_Metric_Definition table with the current db status
-- --
DELETE FROM p_Metric_Definition;

ALTER TABLE p_Metric_Definition
ADD COLUMN upload DATE,
ADD COLUMN dataType VARCHAR(255);

ALTER TABLE p_Metric_Definition
MODIFY COLUMN motivation_lodMTV VARCHAR(255) NOT NULL,
MODIFY COLUMN lodMTV_X VARCHAR(255) DEFAULT NULL,
MODIFY COLUMN lodReference2 VARCHAR(255) DEFAULT NULL,
MODIFY COLUMN lodReference VARCHAR(255) DEFAULT NULL;

ALTER TABLE p_Metric_Definition
DROP PRIMARY KEY,
ADD PRIMARY KEY (metric_lodMTR, type_benchmark_lodTBN, motivation_lodMTV, lodM_TB_V);

ALTER TABLE p_Metric_Definition
ADD FOREIGN KEY (motivation_lodMTV) REFERENCES t_Motivation(lodMTV) ON DELETE CASCADE;

INSERT INTO p_Metric_Definition
(lodMDF, metric_lodMTR, valueBenchmark, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, lodMDF_V, lodReference2, lodReference, Upload, DataType, lodM_TB_V)
VALUES
('pid_graph:D61CE936', 'pid_graph:2604D968', 1, 'pid_graph:0917EC0D', 'pid_graph:34A9189F', NULL, NULL, NULL, NULL, '2024-10-10', 'Test', 1),
('pid_graph:D61CE937', 'pid_graph:78B5C3AD', 5, 'pid_graph:0917EC0D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE938', 'pid_graph:7784DBA5', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE939', 'pid_graph:3D5621E1', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE940', 'pid_graph:CF9B6EDF', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE941', 'pid_graph:F0D9746D', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE942', 'pid_graph:844E01C9', 2, 'pid_graph:0917EC0D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE943', 'pid_graph:B110D515', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE944', 'pid_graph:D0495F98', 1, 'pid_graph:8BA6B11D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE945', 'pid_graph:97ECCC27', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE946', 'pid_graph:0A42DC0E', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE947', 'pid_graph:94CCFDA2', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE948', 'pid_graph:98F6C30C', 1, 'pid_graph:8BA6B11D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE949', 'pid_graph:E1B1DB5C', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE950', 'pid_graph:82F03288', 2, 'pid_graph:0917EC0D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE951', 'pid_graph:D4463A62', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE952', 'pid_graph:70DCE869', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE953', 'pid_graph:39B0F5FE', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE954', 'pid_graph:4453BF54', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE955', 'pid_graph:A87CFC4F', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE956', 'pid_graph:D61CE934', 1, 'pid_graph:8BA6B11D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE957', 'pid_graph:2FE87345', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE958', 'pid_graph:E277222A', 8, 'pid_graph:8BA6B11D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE959', 'pid_graph:5DD1CDF0', 6, 'pid_graph:8BA6B11D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE960', 'pid_graph:FFC56559', 8.77, 'pid_graph:AF8098B8', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE961', 'pid_graph:B683FFBE', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE962', 'pid_graph:D8C4E63E', 3, 'pid_graph:0917EC0D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE963', 'pid_graph:CA112C33', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE964', 'pid_graph:EF23504B', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE965', 'pid_graph:13FD7AF1', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE966', 'pid_graph:524995ED', 2, 'pid_graph:0917EC0D', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE967', 'pid_graph:A9F1EEE3', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE968', 'pid_graph:E8AFCC21', 1, 'pid_graph:7085006F', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE969', 'pid_graph:77496A8D', 12, 'pid_graph:C4D0F2B1', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE970', 'pid_graph:DC52DEA4', 0.95, 'pid_graph:6EBC59CF', 'pid_graph:34A9189F', 'pid_graph:3E109BBA', NULL, NULL, NULL, '2024-09-12', 'Test', 1),
('pid_graph:D61CE91', 'pid_graph:EBCEBED1', 3, 'pid_graph:0917EC0D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE910', 'pid_graph:97ECCC27', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE911', 'pid_graph:0A42DC0E', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE912', 'pid_graph:94CCFDA2', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE913', 'pid_graph:98F6C30C', 1, 'pid_graph:8BA6B11D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE914', 'pid_graph:E1B1DB5C', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE915', 'pid_graph:82F03288', 2, 'pid_graph:0917EC0D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE916', 'pid_graph:D4463A62', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE917', 'pid_graph:70DCE869', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE918', 'pid_graph:39B0F5FE', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE919', 'pid_graph:4453BF54', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE92', 'pid_graph:78B5C3AD', 5, 'pid_graph:0917EC0D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE920', 'pid_graph:A87CFC4F', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE921', 'pid_graph:D61CE934', 1, 'pid_graph:8BA6B11D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE922', 'pid_graph:2FE87345', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE923', 'pid_graph:E277222A', 8, 'pid_graph:8BA6B11D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE924', 'pid_graph:5DD1CDF0', 6, 'pid_graph:8BA6B11D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE925', 'pid_graph:FFC56559', 8.77, 'pid_graph:AF8098B8', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE926', 'pid_graph:B683FFBE', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE927', 'pid_graph:D8C4E63E', 3, 'pid_graph:0917EC0D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE928', 'pid_graph:CA112C33', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE929', 'pid_graph:EF23504B', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE93', 'pid_graph:7784DBA5', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE930', 'pid_graph:13FD7AF1', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE931', 'pid_graph:524995ED', 2, 'pid_graph:0917EC0D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE932', 'pid_graph:A9F1EEE3', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE933', 'pid_graph:E8AFCC21', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE934', 'pid_graph:77496A8D', 12, 'pid_graph:C4D0F2B1', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE935', 'pid_graph:DC52DEA4', 0.95, 'pid_graph:6EBC59CF', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE94', 'pid_graph:3D5621E1', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE95', 'pid_graph:CF9B6EDF', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE96', 'pid_graph:F0D9746D', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE97', 'pid_graph:844E01C9', 2, 'pid_graph:0917EC0D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE98', 'pid_graph:B110D515', 1, 'pid_graph:7085006F', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1),
('pid_graph:D61CE99', 'pid_graph:D0495F98', 1, 'pid_graph:8BA6B11D', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', NULL, 'pid_graph:207965C138', 'pid_graph:207965C1101', NULL, NULL, 1);