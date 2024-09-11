-- ------------------------------------------------
-- Version: v1.98
--
-- Description: Migration that introduces the Criterion table
-- -------------------------------------------------

CREATE TABLE p_Criterion (
    lodCRI VARCHAR(255) NOT NULL PRIMARY KEY,
    CRI VARCHAR(255) NOT NULL UNIQUE,
    labelCriterion VARCHAR(255) NOT NULL,
    descCriterion TEXT NOT NULL,
    urlCriterion TEXT DEFAULT NULL,
    lodIMP VARCHAR(255) NOT NULL,
    lodTCR VARCHAR(255) NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lodCRI_V VARCHAR(255) DEFAULT NULL,
    lodCRI_P VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lodIMP) REFERENCES s_Imperative(lodIMP),
    FOREIGN KEY (lodTCR) REFERENCES t_Type_Criterion(lodTCR)
);

INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C21', 'pid_graph:004D4203', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'RI Integration', 'Services SHOULD allow integration with European Research Infrastructures.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C18', 'pid_graph:024DEF14', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Automation', 'The PID Service SHOULD maintain entity metadata as part of its PID Kernel information, but this source is not authoritative. Its main purpose is automation.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C9', 'pid_graph:07BEDE5D', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Community Engagement', 'The PID Service SHOULD engage the end user community to determine changes in needs and practices and adjust their services and guidance accordingly.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C32', 'pid_graph:10E055C4', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Justifiable Cost', 'PID Services SHOULD be provided at justifiable cost to PID Owners and PID Managers within EOSC.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C27', 'pid_graph:134EA685', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Continuity', 'PID Service Providers MUST have a clear sustainability and succession plan with an exit strategy that guarantees the continuity of the resolution of its PIDs registered with the service.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C6', 'pid_graph:135BBF9C', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Ownership Transfer', 'The PID manager SHOULD provide policies and contractual arrangements for transfer of ownership should the owner no longer be able to assume responsibilities in compliance with the policy.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C35', 'pid_graph:1F4D6BEF', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Resolution Percentage', 'PID Service SHOULD resolve at least p percent of PIDs in a randomised sample, where p is determined by community and dependency expectations.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C4', 'pid_graph:213337DB', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Maintenance', 'The PID owner SHOULD maintain PID attributes.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C30', 'pid_graph:27346307', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Global Resolution', 'PID Service Providers MUST ensure their system supports the necessary API for global resolution services.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C16', 'pid_graph:28D9C36B', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Digital Representation', 'Physical and conceptual entities MUST be represented via a digital representation (e.g. landing page, metadata, attribute set, database index) to have a presence in the digital landscape.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C31', 'pid_graph:317587D0', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Community Inclusion', 'PID Services MUST include representatives of the EU research community.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C2', 'pid_graph:3BB55CAF', 'pid_graph:293B1DEE', 'pid_graph:4A47BB1A', 'Sensitive Metadata', 'Sensitive kernel metadata MAY require access control and/or encryption of the Kernel Information.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C28', 'pid_graph:3DBBBFBF', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Certification', 'PID Authorities and Services MUST agree to be certified with a mutually agreed frequency in respect of policy compliance.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C11', 'pid_graph:420EC62B', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Versioning - Procedure', 'PID services and PID Managers SHOULD have clear versioning policies.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C19', 'pid_graph:4ED69013', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Accurate Entity Metadata', 'The PID Manager MUST maintain entity metadata as accurately as possible in collaboration with the PID Owner. This copy is the authoritative version.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C13', 'pid_graph:59DE77D5', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Persistence - Service', 'PID Service MUST ensure that the PID issued by the PID Authority cannot be deleted in its records.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C12', 'pid_graph:5EA1B0C5', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Persistence - Authority', 'PID Authority MUST ensure that the PID cannot be deleted.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C25', 'pid_graph:5F81AEA8', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Availability - Measure', 'PID Services MUST meet 999 availability and uptime.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C7', 'pid_graph:694CB39D', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Resolution Integrity', 'The PID Manager MUST maintain the integrity of the relationship between entities and their PIDs, in conformance to a PID Scheme defined by a PID Authority.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C26', 'pid_graph:7FA9929C', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Availability - Procedure', 'PID Service Providers SHOULD document a summary of their maintenance and availability provisions publicly.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C34', 'pid_graph:81D22297', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Persistence Median', 'PID Services SHOULD aim for a persistence median that is acceptable to and aligns with community and dependency expectations.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C14', 'pid_graph:8503588B', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Resolution Authenticity or Efficiency', 'PID Manager MUST ensure that the entity remains linked to the PID. In case that the entity being identified is deleted or ceases to exist, tombstone information needs to be included in the PID attribute set.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C3', 'pid_graph:91214486', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Ownership', 'PID ownership MUST be visible to other actors in the ecosystem.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C5', 'pid_graph:954437E3', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Update Functionality', 'The PID manager MUST provide the functionality required to maintain PID attributes.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C29', 'pid_graph:959B7284', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Agreed Responsibilities', 'PID Services SHOULD agree with PID Managers the responsibilities for Kernel Information maintenance, preferably via contract.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C8', 'pid_graph:9A184C36', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Guidance', 'The PID Service SHOULD publish guidance on the use cases, levels of granularity, and community best practices that are satisfied by their PID services.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C10', 'pid_graph:A5A41F03', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Versioning - Schema', 'PID services SHOULD support versioning.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C15', 'pid_graph:A87D0578', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Type Information', 'The PID Authority SHOULD provide information on the referenced object’s fundamental type and management policy in a machine-actionable way.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C1', 'pid_graph:A8EA1C61', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Minimum Operations', 'Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C24', 'pid_graph:B9A31AE3', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Maturity - Value Added Services', 'Added value services MAY be offered at technology readiness levels lower than 8 OR Added value services SHOULD be offered at technology readiness level 8.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C20', 'pid_graph:D0339C6A', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Openly Available', 'Services MUST be available to all researchers in the EU.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C17', 'pid_graph:D7C00C05', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Kernel Information Profiles', 'PID Services MUST engage the community to develop one or more Kernel Information Profiles appropriate to the use cases addressed by their services.');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C23', 'pid_graph:D969810C', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'Basic Service Maturity', 'A PID Service infrastructure MUST be at a minimum technology readiness level of 8. This applies to basic services (registration, resolution).');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C33', 'pid_graph:DF512939', 'pid_graph:2981F3DD', 'pid_graph:4A47BB1A', 'Global Governance', 'PID Service governance structures SHOULD align or be embedded in global governance structures');
INSERT INTO p_Criterion (CRI, lodCRI, lodIMP, lodTCR, labelCriterion, descCriterion) VALUES ('C22', 'pid_graph:F9FC6BE3', 'pid_graph:BED209B9', 'pid_graph:4A47BB1A', 'No End User Cost', 'The basic services of PID registration and resolution SHALL have no cost to end users.');

DROP TABLE Criteria;
