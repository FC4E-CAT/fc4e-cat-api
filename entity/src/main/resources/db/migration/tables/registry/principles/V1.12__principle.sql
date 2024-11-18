-- ------------------------------------------------
-- Version: v1.12
--
-- Description: Migration that introduces the p_Principle table
-- -------------------------------------------------

CREATE TABLE p_Principle (
    lodPRI VARCHAR(255) NOT NULL PRIMARY KEY,
    PRI VARCHAR(255) NOT NULL UNIQUE,
    labelPrinciple VARCHAR(255) NOT NULL,
    descPrinciple TEXT NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodPRI_V VARCHAR(255) DEFAULT NULL,
    lodMTV VARCHAR(255) DEFAULT NULL
);

INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:0C11BA29', 'P9', 'Resolution', 'There is a need for a generic, global PID resolution system across all PID systems and service providers.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:215EFC78', 'P10', 'Governed', 'PID Service Providers should apply appropriate community governance to ensure that their PID Services and Systems adhere to these policies, and are agile and responsive to the needs of research, Open Science and EOSC.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:333F1C6A', 'P6', 'Diversity', 'PIDs can identify many different entities. These can be born digital (e.g. documents, data, software, services - otherwise known as digital objects - and collections made of them), physical (e.g. people, instruments, artefacts, samples), or conceptual (e.g. organisations, projects, vocabularies).');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:52EAC55F', 'P5', 'Type Support', 'Classes of digital objects may need different attribute sets a PID is resolved to. It is the responsibility of a community of practice to define and document these attribute sets (PID Kernel Information Profiles).');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:5CDE41B8', 'P17', 'Innovation', 'The Policy should encourage new and innovative services and tools, which use and build on the PID Infrastructure.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:6279AE43', 'P2', 'Secure', 'PID services for EOSC need to address (a wide variety of) applications (including those) that require secure mechanisms built into the PID Infrastructure.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:6890514A', 'P18', 'Interoperability', 'To create a functioning PID ecosystem and to realise the relevant FAIR Principles, PID Services should be interoperable with each other and between PID Service Providers, as well as across research infrastructure.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:6DA48AC0', 'P12', 'Viable, Trusted', 'The PID Policy concentrates on principles, desired results and governance which are designed to establish a viable, trusted PID infrastructure suitable for the long-term sustainability of the EOSC.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:6F00A944', 'P19', 'Maturity', 'The policy seeks to accommodate mature and established PID practice, schemes, technologies and providers, which have a global presence. The policy also needs to be balanced and not prefer one approach or technology over another.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:704DEDC5', 'P20', 'Preferred Reference to Entities', 'The policy should result in a future where PIDs can be used as the preferred method of referring to its assigned entity, where appropriate, alongside human-readable means e.g. the common name. Multiple PIDs may identify any given entity and users should be able to use whichever they are most comfortable with.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:869E5028', 'P8', 'Integrated', 'Services need to integrate well with European Research Infrastructures, but not at the exclusion of the broader research community.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:AE92131B', 'P1', 'Unambiguous (Ownership and Identification)', 'PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:C6842E03', 'P16', 'Technology Independence', 'Technology independence of PIDs is required to allow for technological change. PID services will vary in maturity over time and the PID policy should identify the level of service maturity suitable for EOSC adoption.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:C855355A', 'P7', 'Global, Mature Services', 'Services are mature, managed with high availability and uptime, and are capable of integration into research and data infrastructures. Services have a global presence (Includes original 2.7).');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:CB6A85C9', 'P4', 'Levels of Granularity', 'The PID ecosystem ideally supports multiple levels of granularity and encourages/ fosters links between them.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:E3087302', 'P14', 'Support humans and machines', 'PIDs can be used as the preferred method of referring to its assigned entity, where appropriate, alongside human-readable means (supports original 2.3).');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:E533DD9B', 'P3', 'Ecosystem', 'There are a range of current use cases for PIDs in research and scholarship. The full range of use cases covers a wide variety of direct human use and automated machine processing and actionability. This PID Policy should accommodate a wide range of use cases and not put in place barriers to the effective use of PIDs as needed by the research community. An ecosystem of PID Infrastructures is needed to support the wide variety of scientific applications and offers sufficient flexibility (service providers, scheme, attribute set) and capacity. The PID Policy should accommodate a wide range of use cases and not put in place barriers to the effective use of PIDs as needed by the research community. (supports original 2.2)');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:E7C00DBA', 'P15', 'Sustainable', 'In adhering to the Policy, PID providers and users are expected to commit to sustaining the PID infrastructures and digital objects in the long term.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:EEF238FC', 'P21', 'Review', 'The policy should be maintained by the EOSC Legal Entity and reviewed by a dedicated group of experts and stakeholders on a regular basis and at least in 3 years time. All of those who perform Roles as defined in Section 4 should be consulted and involved in the review process.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:F9141635', 'P11', 'FAIR', 'The PID Policy should enable an environment of research practice, and services that satisfy the FAIR principles as appropriate for the particular domains of use. Central to the realisation of FAIR are FAIR Digital Objects and PIDs are core to the idea of FAIR Digital Objects, as highlighted in the Turning FAIR Into Reality report (FAIR Expert Group, 2018).');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:32ED4718','AUTO', 'Automated tests', 'Ensuring that some of the required rules are tested.');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:A1141612','PA1', 'PA1', 'Attribute Management and Attribute Release');
INSERT INTO p_Principle (lodPRI, PRI, labelPrinciple, descPrinciple) VALUES ('pid_graph:5PR0882I','PA2', 'Site security', 'There is a need for a generic, global PID resolution system across all PID systems and service providers.');

CREATE TABLE s_Relation(
    REL VARCHAR(255) NOT NULL PRIMARY KEY,
    labelRelation VARCHAR(255) NOT NULL,
    descRelation TEXT NOT NULL,
    urlRelation TEXT DEFAULT NULL
);

INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('maintainedBy', 'Maintained By', 'The organisation responsible for maintaining a standard, schema, specfication, or protocol', NULL);
INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('referencedBy', 'Referenced By', 'The subject is referenced by the object', NULL);
INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('isCitedBy', 'Is Cited By', 'The subject is citing the reference or annotation as a source', 'https://www.wikidata.org/wiki/Q57317839');
INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('dcterms:isRequiredBy', 'Is Required By', '	A related resource that requires the described resource to support its function, delivery, or coherence.', 'https://www.dublincore.org/specifications/dublin-core/dcmi-terms/');
INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('isMeasuredAs', 'Is Measured As', 'Used to describe the numerical value of something or to indicate a comparison.', NULL);
INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('IsDerivedFrom', 'Is Derived From', 'Used to describe a derivation of output from one or more inputs', 'https://schema.datacite.org/meta/kernel-4.4/doc/DataCite-MetadataKernel_v4.4.pdf');
INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('isSupportedBy', 'Is Supported By', 'A principle, objective, or ideal that is supported by a criterion, provision, or achievement', NULL);
INSERT INTO s_Relation (REL, labelRelation, descRelation, urlRelation) VALUES ('isUsedBy', 'Is Used By', 'item or concept that makes use of the subject (use sub-properties when appropriate)', 'https://www.wikidata.org/wiki/Property:P1535');


CREATE TABLE p_Motivation_Principle (
    motivation_lodMTV VARCHAR(255) NOT NULL,
    principle_lodPRI VARCHAR(255) NOT NULL,
    lodM_P_V INT NOT NULL,
    annotationText TEXT DEFAULT NULL,
    annotationURL TEXT DEFAULT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodMTV_X VARCHAR(255) NOT NULL,
    lodREL VARCHAR(255) NOT NULL,
    PRIMARY KEY (motivation_lodMTV, principle_lodPRI, lodM_P_V),
    FOREIGN KEY (motivation_lodMTV) REFERENCES t_Motivation(lodMTV) ON DELETE CASCADE,
    FOREIGN KEY (principle_lodPRI) REFERENCES p_Principle(lodPRI) ON DELETE CASCADE,
    FOREIGN KEY (lodREL) REFERENCES s_Relation(REL)
 );

INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:0C11BA29', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:215EFC78', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:333F1C6A', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:52EAC55F', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:5CDE41B8', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:6279AE43', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:6890514A', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:6DA48AC0', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:6F00A944', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:704DEDC5', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:869E5028', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:AE92131B', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:C6842E03', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:C855355A', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:CB6A85C9', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:E3087302', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:E533DD9B', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:E7C00DBA', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:EEF238FC', NULL, NULL, 'pid_graph:3E109BBA', 1);
INSERT INTO p_Motivation_Principle (motivation_lodMTV, lodREL, principle_lodPRI, annotationText, annotationURL, lodMTV_X, lodM_P_V) VALUES ('pid_graph:3E109BBA', 'isSupportedBy', 'pid_graph:F9141635', NULL, NULL, 'pid_graph:3E109BBA', 1);