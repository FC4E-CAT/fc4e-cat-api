-- ------------------------------------------------
-- Version: v1.91
--
-- Description: Migration that introduces the t_Type_Reproducibility table
-- -------------------------------------------------

-- type metric table
CREATE TABLE t_Type_Reproducibility (
    lodTCO VARCHAR(255) NOT NULL,
    labelTypeConfidence VARCHAR(255) NOT NULL,
    descTypeConfidence VARCHAR(255) NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodReference VARCHAR(255) NOT NULL,
    lodTCO_V VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (lodTCO)
);

INSERT INTO t_Type_Reproducibility (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:1BA2356B', 'Highly likely', 'Two independent assessors, observers, or instruments are highly likely to obtain the same results',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:2BD477E1', 'Somewhat Likely', 'Two independent assessors, observers, or instruments are somewhat likely to obtain the same results',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:4F179B32', 'Guaranteed with interval', 'Results are guaranteed to be the same within a confidence interval or level of precision',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:8D254805', 'Unlikely', 'Two independent assessors, observers, or instruments are unlikely to obtain the same results',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:AAC19C15', 'Guaranteed', 'Results are guaranteed to be exactly the same',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:BEBE61D4', 'Standardised', 'Based on subjective and objective measures, but standardised to such a degree that two assessors will very likely obtain the same result.',
'pid_graph:207965C148', NULL);
