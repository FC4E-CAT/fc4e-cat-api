-- ------------------------------------------------
-- Version: v1.93
--
-- Description: Migration that introduces the p_Metric table
-- -------------------------------------------------

-- Metric table
CREATE TABLE p_Metric (
    MTR VARCHAR(255) NOT NULL,
    lodMTR VARCHAR(255) NOT NULL,
    labelMetric VARCHAR(255) NOT NULL,
    descrMetric TEXT NOT NULL,
    urlMetric TEXT DEFAULT NULL,
    lodTAL VARCHAR(255) NOT NULL,
    lodTMT VARCHAR(255) NOT NULL,
    lodMTV VARCHAR(255) NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodMTR_V VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (lodMTR),
    FOREIGN KEY (lodTAL) REFERENCES t_Type_Algorithm(lodTAL),
    FOREIGN KEY (lodTMT) REFERENCES t_Type_Metric(lodTMT),
    INDEX Type_Metric (lodTMT)
);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M11', 'pid_graph:0A42DC0E', 'Versioning Procedure', 'Evidence is available that a clear versioning procedure or policy exists and is available', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M30', 'pid_graph:13FD7AF1', 'Globally Resolvable', 'Provider supplies evidence that their system supports global resolution services.', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M22', 'pid_graph:2FE87345', 'No End User Costs', 'Evidence that basic end user costs are free', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M18', 'pid_graph:39B0F5FE', 'Entity Metadata Automation', 'Evidence that entity metadata is maintained as part of PID Kernel information and is machine-readable.', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M4', 'pid_graph:3D5621E1', 'Owner Maintenance', 'Evidence of owner maintenance', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M19', 'pid_graph:4453BF54', 'Entity Metadata Accuracy', 'Entity (custom) metadata is maintained and evidence of processes, guidance, and technical support to achieve this is available', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M31', 'pid_graph:524995ED', 'Community Inclusion', 'Providers have evidence that representatives of the EU research community participate in activities.', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M24', 'pid_graph:5DD1CDF0', 'Value Added Services TRL', 'Value added services TRL are asserted or measured', NULL, 'pid_graph:5CF01298', 'pid_graph:C62DD0BB', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M17', 'pid_graph:70DCE869', 'Kernel Information Profile Adequate', 'Evidence is supplied that Kernel Information Profiles are appropriate to the use cases addressed by the service(s).', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M34', 'pid_graph:77496A8D', 'Persistence Median', 'Evidence is provided of the mean persistence of holdings in scope for a manager, provider, or authority over time', NULL, 'pid_graph:7A976659', 'pid_graph:C62DD0BB', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M3', 'pid_graph:7784DBA5', 'Visible Ownership', 'Ownership is visible to others', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M2', 'pid_graph:78B5C3AD', 'Sensitive Metadata Access Control', 'Access control in place for sensitive metadata', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M15', 'pid_graph:82F03288', 'Type Information Available', 'Evidence is provided on how type information can be obtained', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M7', 'pid_graph:844E01C9', 'Resolution Integrity In Place', 'PID Manager has measures in place to manage resolution integrity', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M12', 'pid_graph:94CCFDA2', 'Authority Persistence Adequate', 'Evidence is available that the PID Authority safeguards persistence of PID records', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M10', 'pid_graph:97ECCC27', 'Versioning Supported', 'Evidence is available that one or more versioning approaches can be implemented', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M13', 'pid_graph:98F6C30C', 'Provider Persistence Adequate', 'Evidence is available that the PID Provider safeguards persistence of PID records', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M20', 'pid_graph:A87CFC4F', 'Openly Available Services', 'Evidence that services are openly available to researchers in the EU', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M32', 'pid_graph:A9F1EEE3', 'Acceptable Cost of Service', 'Providers supply evidence of costs to managers, or publicly asserts that it is minimized and justifiable', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M8', 'pid_graph:B110D515', 'Guidance Available', 'Evidence is supplied of guidance to managers and owners', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M26', 'pid_graph:B683FFBE', 'Availability Procedures Provided', 'Evidence by way of a summary of maintenance and availability provisions is publicly available.', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M28', 'pid_graph:CA112C33', 'Certification Status', 'Evidence if certification is provided or public intention to be certified is available', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M5', 'pid_graph:CF9B6EDF', 'Update Functionality Available', 'Evidence of functionality is provided', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M9', 'pid_graph:D0495F98', 'Community Engagement In Place', 'Evidence that the end user community is engaged to determine needs and practices and adjust services and guidance.', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M16', 'pid_graph:D4463A62', 'Digital Representation Exists', 'Digital representation of physical objects is possible and supported', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M21', 'pid_graph:D61CE934', 'RI Integration Possible', 'Evidence or examples of European RI integration is available', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M27', 'pid_graph:D8C4E63E', 'Continuity Plan Available', 'Evidence is provided of a sustainability and succession plan with an exit strategy that guarantees the continuity of the service.', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M35', 'pid_graph:DC52DEA4', 'Resolution Percentage', 'Evidence is provided of the resolvability percentage of holdings in scope for a manager, provider, or authority over time', NULL, 'pid_graph:7A976659', 'pid_graph:C62DD0BB', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M14', 'pid_graph:E1B1DB5C', 'Resolution Management Adequate', 'Evidence is available that the PID Manager ensures continued resolution', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M23', 'pid_graph:E277222A', 'Basic Service TRL', 'Basic services TRL are asserted or measured', NULL, 'pid_graph:7A976659', 'pid_graph:C62DD0BB', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M33', 'pid_graph:E8AFCC21', 'Global Governance Participation', 'Evidence is provided participation in global governance structures', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M1', 'pid_graph:EBCEBED1', 'Minimum Operations Available', 'API Availability', NULL, 'pid_graph:AE39C968', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M29', 'pid_graph:EF23504B', 'Responsibilities Clear', 'Evidence that Providers and Managers have agreed respective responsibilities for Kernel Information maintenance', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M6', 'pid_graph:F0D9746D', 'Ownership Transfer Allowed', 'Evidence is provided of ownership transfer mechanism(s)', NULL, 'pid_graph:7A976659', 'pid_graph:8D79984F', 'pid_graph:3E109BBA', NULL);

INSERT INTO p_Metric (MTR, lodMTR, labelMetric, descrMetric, urlMetric, lodTAL, lodTMT, lodMTV, lodMTR_V)
VALUES ('M25', 'pid_graph:FFC56559', 'Service Availability', 'Uptime is measured and reporting is available, or is asserted with evidence', NULL, 'pid_graph:5CF01298', 'pid_graph:C62DD0BB', 'pid_graph:3E109BBA', NULL);



