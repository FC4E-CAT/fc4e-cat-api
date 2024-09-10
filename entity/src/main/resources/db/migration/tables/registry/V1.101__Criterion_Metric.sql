-- ------------------------------------------------
-- Version: v1.101
--
-- Description: Migration that introduces the Criterion-Metric table
-- -------------------------------------------------

CREATE TABLE p_Criterion_Metric (
    metric_lodMTR VARCHAR(255) NOT NULL,
    criterion_lodCRI VARCHAR(255) NOT NULL,
    motivation_lodMTV VARCHAR(255) NOT NULL,
    lodM_C_V INT NOT NULL,
    populatedBy VARCHAR(255) DEFAULT  NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodMTV_X VARCHAR(255) NOT NULL,
    lodREL VARCHAR(255) NOT NULL,
    PRIMARY KEY (metric_lodMTR, criterion_lodCRI, motivation_lodMTV, lodM_C_V),
    FOREIGN KEY (motivation_lodMTV) REFERENCES t_Motivation(lodMTV) ON DELETE CASCADE,
    FOREIGN KEY (metric_lodMTR) REFERENCES p_Metric(lodMTR) ON DELETE CASCADE,
    FOREIGN KEY (criterion_lodCRI) REFERENCES p_Criterion(lodCRI) ON DELETE CASCADE,
    FOREIGN KEY (lodREL) REFERENCES s_Relation(REL)
 );
 
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:0A42DC0E', 'pid_graph:420EC62B', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:13FD7AF1', 'pid_graph:27346307', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:2FE87345', 'pid_graph:F9FC6BE3', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:39B0F5FE', 'pid_graph:024DEF14', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:3D5621E1', 'pid_graph:213337DB', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:4453BF54', 'pid_graph:4ED69013', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:524995ED', 'pid_graph:317587D0', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:5DD1CDF0', 'pid_graph:B9A31AE3', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:70DCE869', 'pid_graph:D7C00C05', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:77496A8D', 'pid_graph:81D22297', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:7784DBA5', 'pid_graph:91214486', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:78B5C3AD', 'pid_graph:3BB55CAF', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:82F03288', 'pid_graph:A87D0578', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:844E01C9', 'pid_graph:694CB39D', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:94CCFDA2', 'pid_graph:5EA1B0C5', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:97ECCC27', 'pid_graph:A5A41F03', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:98F6C30C', 'pid_graph:59DE77D5', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:A87CFC4F', 'pid_graph:D0339C6A', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:A9F1EEE3', 'pid_graph:10E055C4', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:B110D515', 'pid_graph:9A184C36', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:B683FFBE', 'pid_graph:7FA9929C', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:CA112C33', 'pid_graph:3DBBBFBF', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:CF9B6EDF', 'pid_graph:954437E3', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:D0495F98', 'pid_graph:07BEDE5D', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:D4463A62', 'pid_graph:28D9C36B', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:D61CE934', 'pid_graph:004D4203', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:D8C4E63E', 'pid_graph:134EA685', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:DC52DEA4', 'pid_graph:1F4D6BEF', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:E1B1DB5C', 'pid_graph:8503588B', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:E277222A', 'pid_graph:D969810C', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:E8AFCC21', 'pid_graph:DF512939', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:EBCEBED1', 'pid_graph:A8EA1C61', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:EF23504B', 'pid_graph:959B7284', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:F0D9746D', 'pid_graph:135BBF9C', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);
INSERT INTO p_Criterion_Metric (metric_lodMTR, criterion_lodCRI, lodREL, motivation_lodMTV, lodMTV_X, lodM_C_V) VALUES ('pid_graph:FFC56559', 'pid_graph:5F81AEA8', 'isMeasuredAs', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA', 1);


