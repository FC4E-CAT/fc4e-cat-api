-- ------------------------------------------------
-- Version: v1.108
--
-- Description: Migration that introduces the Metric_Test table
-- -------------------------------------------------

CREATE TABLE p_Metric_Test (
    metric_lodMTR VARCHAR(50) NOT NULL,
    lodREL VARCHAR(50) NOT NULL,
    test_lodTES VARCHAR(50) NOT NULL,
    test_definition_lodTDF VARCHAR(50) NOT NULL,
    motivation_lodMTV VARCHAR(50) NOT NULL,
    lodM_T_TD_V INT NOT NULL,
    lodMTV_X VARCHAR(255) DEFAULT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (motivation_lodMTV, metric_lodMTR, test_lodTES, test_definition_lodTDF, lodM_T_TD_V),
    FOREIGN KEY (motivation_lodMTV) REFERENCES t_Motivation(lodMTV) ON DELETE CASCADE,
    FOREIGN KEY (metric_lodMTR) REFERENCES p_Metric(lodMTR) ON DELETE CASCADE,
    FOREIGN KEY (test_lodTES) REFERENCES p_Test(lodTES) ON DELETE CASCADE,
    FOREIGN KEY (test_definition_lodTDF) REFERENCES p_Test_Definition(lodTDF) ON DELETE CASCADE,
    FOREIGN KEY (lodREL) REFERENCES s_Relation(REL),
    INDEX Test (test_lodTES),
    INDEX Definition (test_definition_lodTDF)
 );

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:EBCEBED1', 'IsDerivedFrom', 'pid_graph:29D74907', 'pid_graph:529154B3', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:EBCEBED1', 'IsDerivedFrom', 'pid_graph:B2BE4D4A', 'pid_graph:E5C931F6', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:EBCEBED1', 'IsDerivedFrom', 'pid_graph:B0DD9C10', 'pid_graph:40C30444', 'pid_graph:3E109BBA', 3, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:78B5C3AD', 'IsDerivedFrom', 'pid_graph:A4289B84', 'pid_graph:C1776C75', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:78B5C3AD', 'IsDerivedFrom', 'pid_graph:42E8F8E3', 'pid_graph:402853DB', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:78B5C3AD', 'IsDerivedFrom', 'pid_graph:F3AE497B', 'pid_graph:4F0F1EA5', 'pid_graph:3E109BBA', 3, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:78B5C3AD', 'IsDerivedFrom', 'pid_graph:763787BB', 'pid_graph:BE43C039', 'pid_graph:3E109BBA', 4, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:78B5C3AD', 'IsDerivedFrom', 'pid_graph:F8D89E9B', 'pid_graph:03591DB3', 'pid_graph:3E109BBA', 5, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:7784DBA5', 'IsDerivedFrom', 'pid_graph:D9B829C4', 'pid_graph:C9C1D620', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

 INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
 VALUES ('pid_graph:3D5621E1', 'IsDerivedFrom', 'pid_graph:B1078ED4', 'pid_graph:5697A0FC', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:F0D9746D', 'IsDerivedFrom', 'pid_graph:7B262333', 'pid_graph:A2F9F95B', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:844E01C9', 'IsDerivedFrom', 'pid_graph:A9BC1377', 'pid_graph:84A46E80', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:B110D515', 'IsDerivedFrom', 'pid_graph:84C20BC3', 'pid_graph:7B428EA4', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:D0495F98', 'IsDerivedFrom', 'pid_graph:F5E89C19', 'pid_graph:5E148A19', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:D0495F98', 'IsDerivedFrom', 'pid_graph:CE66C45C', 'pid_graph:77D54507', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:97ECCC27', 'IsDerivedFrom', 'pid_graph:87B31839', 'pid_graph:ECE3D887', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:0A42DC0E', 'IsDerivedFrom', 'pid_graph:803EE3F7', 'pid_graph:A322B4E9', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:94CCFDA2', 'IsDerivedFrom', 'pid_graph:BB904261', 'pid_graph:81E333ED', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:98F6C30C', 'IsDerivedFrom', 'pid_graph:030489E8', 'pid_graph:1EC6CFCB', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:98F6C30C', 'IsDerivedFrom', 'pid_graph:9772C39A', 'pid_graph:260CABBE', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:E1B1DB5C', 'IsDerivedFrom', 'pid_graph:0E7400CB', 'pid_graph:8C72C972', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:82F03288', 'IsDerivedFrom', 'pid_graph:0FB9EC33', 'pid_graph:3E748FA1', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:82F03288', 'IsDerivedFrom', 'pid_graph:1C02006A', 'pid_graph:5C4D034D', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:D4463A62', 'IsDerivedFrom', 'pid_graph:680AD6FD', 'pid_graph:0CE21FB1', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:70DCE869', 'IsDerivedFrom', 'pid_graph:8CC932F5', 'pid_graph:12B17921', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:39B0F5FE', 'IsDerivedFrom', 'pid_graph:166A398F', 'pid_graph:087DE082', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:4453BF54', 'IsDerivedFrom', 'pid_graph:5AB8E036', 'pid_graph:B1CC8397', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:A87CFC4F', 'IsDerivedFrom', 'pid_graph:A78D63FF', 'pid_graph:445FE1FD', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:D61CE934', 'IsDerivedFrom', 'pid_graph:8948E1FF', 'pid_graph:7D7BA336', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:2FE87345', 'IsDerivedFrom', 'pid_graph:3DBD31CE', 'pid_graph:35A86F42', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:E277222A', 'IsDerivedFrom', 'pid_graph:4795479D', 'pid_graph:4E775F93', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:5DD1CDF0', 'IsDerivedFrom', 'pid_graph:380DFA45', 'pid_graph:3C71F9F7', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:5DD1CDF0', 'IsDerivedFrom', 'pid_graph:07064CA5', 'pid_graph:EA18FF7F', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:FFC56559', 'IsDerivedFrom', 'pid_graph:7A5BB547', 'pid_graph:729336E7', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:FFC56559', 'IsDerivedFrom', 'pid_graph:C4CF831F', 'pid_graph:36B9D20E', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:B683FFBE', 'IsDerivedFrom', 'pid_graph:4FA9E52E', 'pid_graph:CD14BCFA', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:D8C4E63E', 'IsDerivedFrom', 'pid_graph:A3DB5ACC', 'pid_graph:F68AEF90', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:D8C4E63E', 'IsDerivedFrom', 'pid_graph:5C7B03BF', 'pid_graph:71AD9FFE', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:D8C4E63E', 'IsDerivedFrom', 'pid_graph:56666571', 'pid_graph:374E9272', 'pid_graph:3E109BBA', 3, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:CA112C33', 'IsDerivedFrom', 'pid_graph:F5AB7638', 'pid_graph:E4CED2E9', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:EF23504B', 'IsDerivedFrom', 'pid_graph:FCDFFCBD', 'pid_graph:C04A702B', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:13FD7AF1', 'IsDerivedFrom', 'pid_graph:1E984FD2', 'pid_graph:0EFD4F79', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:524995ED', 'IsDerivedFrom', 'pid_graph:30DC2F93', 'pid_graph:FB5CB3D4', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:524995ED', 'IsDerivedFrom', 'pid_graph:9F1A6267', 'pid_graph:F5B74BA4', 'pid_graph:3E109BBA', 2, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:A9F1EEE3', 'IsDerivedFrom', 'pid_graph:393D1ABE', 'pid_graph:88937FC8', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:E8AFCC21', 'IsDerivedFrom', 'pid_graph:03626415', 'pid_graph:D5210088', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:77496A8D', 'IsDerivedFrom', 'pid_graph:8E780AE0', 'pid_graph:6741174B', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');

INSERT INTO `p_Metric_Test` (`metric_lodMTR`, `lodREL`, `test_lodTES`, `test_definition_lodTDF`, `motivation_lodMTV`, `lodM_T_TD_V`, `lodMTV_X`)
VALUES ('pid_graph:DC52DEA4', 'IsDerivedFrom', 'pid_graph:E54B2EEA', 'pid_graph:4079C258', 'pid_graph:3E109BBA', 1, 'pid_graph:3E109BBA');