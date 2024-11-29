-- ------------------------------------------------
-- Version: v1.5
--
-- Description: Migration that introduces the t_Type_Motivation and t_Motivation tables
-- -------------------------------------------------

CREATE TABLE t_Type_Motivation (
    lodTMT VARCHAR(255) NOT NULL PRIMARY KEY,
    TMT VARCHAR(255),
    labelMotivationType VARCHAR(255),
    descMotivationType TEXT,
    urlMotivationType VARCHAR(255),
    populatedBy VARCHAR(255),
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTMT_V VARCHAR(255)
);

INSERT INTO t_Type_Motivation (lodTMT, TMT, labelMotivationType, descMotivationType, urlMotivationType, lodTMT_V) VALUES ('pid_graph:5AF642D8', 'GDS', 'Good Digital Systems', 'These motivations include risk aversion (for example by requiring two-factor authentication, open source code, and the like). For systems engineering, these will be important considerations - elements such as interoperability, modularity, topology, and scalability are included in this set of motivations.', 'https://mscr-test.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/55bdb433-3fa9-44b1-871e-a99f24bbd647', NULL);
INSERT INTO t_Type_Motivation (lodTMT, TMT, labelMotivationType, descMotivationType, urlMotivationType, lodTMT_V) VALUES ('pid_graph:5EB0883B', 'M&R', 'Mandatory, Policy, and Regulatory', 'Compliance is often mandatory, and is intended to ensure legality or a minimum level of performance. Examples include the EOSC PID Policy, or GDPR.', 'https://mscr-test.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/8dd8890d-3e29-45c6-ac13-52a858b32c90', NULL);
INSERT INTO t_Type_Motivation (lodTMT, TMT, labelMotivationType, descMotivationType, urlMotivationType, lodTMT_V) VALUES ('pid_graph:8733CC95', 'CEX', 'Community Expectation', 'These are broad community expectations of the performance of the ecosystem, and some of these are formalised in sets of principles (FAIR, TRUST, CARE) and in some cases, realised as sets of criteria and/ or elaborated as expected behaviour or levels of performance (guidance, best practices).', 'https://mscr-test.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/a81169e9-7e52-4fb4-ad0f-7096d70a169c', NULL);
INSERT INTO t_Type_Motivation (lodTMT, TMT, labelMotivationType, descMotivationType, urlMotivationType, lodTMT_V) VALUES ('pid_graph:8882700E', 'COM', 'Composite', 'A motivation that includes aspects of more than one type, or is composed of motivations that span multiple concerns.', 'https://mscr-test.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/8dfdf546-8327-4fae-98eb-89480451e732', NULL);
INSERT INTO t_Type_Motivation (lodTMT, TMT, labelMotivationType, descMotivationType, urlMotivationType, lodTMT_V) VALUES ('pid_graph:AD9D854B', 'ROP', 'Rules of Participation', 'These ensure a minimum level of performance, readiness, or maturity as a prerequisite for participation in a network, an infrastructure, or a consortium.', 'https://mscr-test.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/e69a9f20-3726-422d-9c8a-0078c56d64d0', NULL);
INSERT INTO t_Type_Motivation (lodTMT, TMT, labelMotivationType, descMotivationType, urlMotivationType, lodTMT_V) VALUES ('pid_graph:DFE640B9', 'A&S', 'Architecture and Specifications', 'Compliance may be required for a wide variety of published and well-known standards and conventions - e.g. ISO 27001, OpenAPI, HATEOAS , and similar.', 'https://mscr-test.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/e6559d74-5434-4888-b629-f2a0ffff4601', NULL);

CREATE TABLE t_Motivation (
    lodMTV VARCHAR(255) NOT NULL PRIMARY KEY,
    MTV VARCHAR(255) NOT NULL,
    lodTMT VARCHAR(255) NOT NULL,
    labelMotivation VARCHAR(255) NOT NULL,
    decMotivation TEXT NOT NULL,
    lodMTV_P VARCHAR(255) DEFAULT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodMTV_V VARCHAR(255) DEFAULT NULL,
    FOREIGN KEY (lodTMT) REFERENCES t_Type_Motivation(lodTMT)
);

INSERT INTO t_Motivation (lodMTV, MTV, lodTMT, labelMotivation, decMotivation, lodMTV_P, populatedBy, lodMTV_V) VALUES ('pid_graph:3E109BBA', 'EOSC-PID', 'pid_graph:5EB0883B', 'EOSC PID Policy', 'A policy developed for PID ecosystem in EOSC, supplemented by refinements and extensions from the EOSC Task Force on PID Policy', NULL, NULL, NULL);
INSERT INTO t_Motivation (lodMTV, MTV, lodTMT, labelMotivation, decMotivation, lodMTV_P, lodMTV_V) VALUES ('pid_graph:34A9189F', 'NL-PID', 'pid_graph:5EB0883B', 'Netherlands National PID Policy', 'A candidate policy for the Netherlands, extending the EOSC PID Policy.', 'pid_graph:3E109BBA', NULL);
INSERT INTO t_Motivation (lodMTV,lodTMT, MTV, labelMotivation, decMotivation) VALUES ('pid_graph:5EB0885A','pid_graph:5EB0883B', 'G048',  'AAI-G048', 'G048: keeping users & communities protected');

UPDATE t_Motivation SET decMotivation = 'The tool is discipline-agnostic, making it relevant to any scientific field. … The self-assessment consists of 10 questions with additional guidance texts to help you become more aware of what you can do to make your data(set) as FAIR as possible. The assessment will take between 10-30 minutes, after which you will receive an overview of your awareness level and additional tips on how you can further improve your FAIR skills.' WHERE lodMTV = 'pid_graph:262F3417';
UPDATE t_Motivation SET decMotivation = 'F-UJI is a web service to programmatically assess FAIRness of research data objects at the dataset level based on the FAIRsFAIR Data Object Assessment Metrics.' WHERE lodMTV = 'pid_graph:42455014';
UPDATE t_Motivation SET decMotivation = 'Fair EVA is an open source project that is gathering resources and building tools to help researchers and developers, technology activists and voice technology users evaluate and audit bias and discrimination in voice technologies.' WHERE lodMTV = 'pid_graph:54B11253';
UPDATE t_Motivation SET decMotivation = 'FAIR data are data which meet principles of findability, accessibility, interoperability, and reusability. The acronym and principles were defined in a March 2016 paper in the journal Scientific Data by a consortium of scientists and organisations. The FAIR principles emphasise machine-actionability.' WHERE lodMTV = 'pid_graph:5A120D3A';
UPDATE t_Motivation SET decMotivation = 'Signposting is an approach to make the scholarly web more friendly to machines. It uses Typed Links as a means to clarify patterns that occur repeatedly in scholarly portals. For resources of any media type, these typed links are provided in HTTP Link headers. For HTML resources, they may additionally be provided in HTML link elements.' WHERE lodMTV = 'pid_graph:6915DF88';
UPDATE t_Motivation SET decMotivation = 'The CoreTrustSeal Trustworthy Data Repositories Requirements reflect the characteristics of trustworthy repositories. As such, all Requirements are mandatory and are equally weighted, standalone items. Although some overlap is unavoidable, duplication of evidence sought among Requirements has been kept to a minimum where possible.' WHERE lodMTV = 'pid_graph:76655FA7';
UPDATE t_Motivation SET decMotivation = 'The TRUST Principles provide a common framework to facilitate discussion and implementation of best practice in digital preservation by all stakeholders.' WHERE lodMTV = 'pid_graph:791E870C';
UPDATE t_Motivation SET decMotivation = '17 preliminary recommendations related to one or more of the FAIR principles, and 10 best practice recommendations on semantic artefacts were documented.' WHERE lodMTV = 'pid_graph:89082276';
UPDATE t_Motivation SET decMotivation = 'The current movement toward open data and open science does not fully engage with Indigenous Peoples rights and interests. Existing principles within the open data movement (e.g. FAIR: findable, accessible, interoperable, reusable) primarily focus on characteristics of data that will facilitate increased data sharing among entities while ignoring power differentials and historical contexts. The emphasis on greater data sharing alone creates a tension for Indigenous Peoples who are also asserting greater control over the application and use of Indigenous data and Indigenous Knowledge for collective benefit.' WHERE lodMTV = 'pid_graph:97171FB2';
UPDATE t_Motivation SET decMotivation = 'The Federated FAIR Data Space (F2DS) provides tools for both data producers and data consumers contributing to enhance the overall FAIRness of datasets natively dispersed across heterogeneous repositories by realising services for datasets homogenisation, enrichment and onboarding and services for seamless discovery and access.' WHERE lodMTV = 'pid_graph:BC8C2D2E';
UPDATE t_Motivation SET decMotivation = 'An RO-Crate is a structured archive of all the items that contributed to a research outcome, including their identiﬁers, provenance, relations and annotations … RO-Crate simpliﬁes the process of making research outputs FAIR while also enhancing research reproducibility.' WHERE lodMTV = 'pid_graph:BE36CD9E';
UPDATE t_Motivation SET decMotivation = 'Solid is a specification that lets people store their data securely in decentralised data stores called Pods. Pods are like secure personal web servers for data. When data is stored in someone''s Pod, they control which people and applications can access it. Evaluating these access requests provides a standardised basis for compliance assessment.' WHERE lodMTV = 'pid_graph:C6B2D50E';
UPDATE t_Motivation SET decMotivation = 'The FAIR Implementation Profiles representing the implementation strategies of various communities can be used as the basis to optimise the reuse of existing FAIR-enabling resources and interoperation within and between domains. Ready-made and well-tested FAIR Implementation Profiles created by trusted communities can find widespread reuse among other communities, and vastly accelerate convergence onto well-informed FAIR implementations. As such, the FIPs represent an encoding and implicit vocabulary for identification of FAIR criteria and measures.' WHERE lodMTV = 'pid_graph:E3DF4CB4';
UPDATE t_Motivation SET decMotivation = 'A FAIR Data Point (sometimes abbreviated to FDP) is the realisation of the vision of a group of authors of the original paper on FAIR on how (meta)data could be presented on the web using existing standards, and without the need of APIs.' WHERE lodMTV = 'pid_graph:F03AE5EB';
UPDATE t_Motivation SET decMotivation = 'To remedy the proliferation of FAIRness measurements based on different interpretations of the principles, an RDA Working Group … established a set of indicators and maturity levels for those indicators.' WHERE lodMTV = 'pid_graph:0ACD7064';
