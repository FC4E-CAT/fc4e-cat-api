-- ------------------------------------------------
-- Version: v1.86
--
-- Description: Migration that introduces the t_Type_Criterion table
-- -------------------------------------------------

create TABLE t_Type_Criterion (
    lodTCR VARCHAR(255) NOT NULL PRIMARY KEY,
    labelTypeCriterion VARCHAR(255),
    descTypeCriterion VARCHAR(255),
    urlTypeCriterion VARCHAR(255),
    populatedBy VARCHAR(255),
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTCR_V VARCHAR(255)
);



insert into t_Type_Criterion ( lodTCR, labelTypeCriterion, descTypeCriterion, urlTypeCriterion, populatedBy, lodTCR_V) values ( 'pid_graph:07CA8184', 'Best Practice', 'A method or technique that has been generally accepted as superior to alternatives either because it produces results that are better or because it has become a standard way of doing things', 'https://www.wikidata.org/wiki/Q830382', '0000-0002-0255-5101', null);
insert into t_Type_Criterion ( lodTCR, labelTypeCriterion, descTypeCriterion, urlTypeCriterion, populatedBy,  lodTCR_V) values ( 'pid_graph:0D9310AC', 'Specification', 'Explicit implementation of requirements to be satisfied by a material, design, product, or service', 'https://www.wikidata.org/wiki/Q2101564', '0000-0002-0255-5101', null);
insert into t_Type_Criterion ( lodTCR, labelTypeCriterion, descTypeCriterion, urlTypeCriterion, populatedBy,  lodTCR_V) values ( 'pid_graph:4A47BB1A', 'Criterion', 'A feature used for distinguishing and selecting entities of interest', 'https://www.wikidata.org/wiki/Q1789452', '0000-0002-0255-5101',  null);
insert into t_Type_Criterion ( lodTCR, labelTypeCriterion, descTypeCriterion, urlTypeCriterion, populatedBy,  lodTCR_V) values ( 'pid_graph:65B56ED3', 'Request for Comment', 'Publication of the development of and standards for the Internet', 'https://www.wikidata.org/wiki/Q212971', '0000-0002-0255-5101',  null);
insert into t_Type_Criterion ( lodTCR, labelTypeCriterion, descTypeCriterion, urlTypeCriterion, populatedBy, lodTCR_V) values ( 'pid_graph:7253AFC1', 'Recommendation', 'Insistent advice or exhortation suggested by a reputable, trusted person or authority, in situations proven by experience or the most frequent or most appropriate use', 'https://www.wikidata.org/wiki/Q17637604', '0000-0002-0255-5101', null);
insert into t_Type_Criterion ( lodTCR, labelTypeCriterion, descTypeCriterion, urlTypeCriterion, populatedBy,  lodTCR_V) values ( 'pid_graph:A2719B92', 'Guideline', 'Instruction as to the preferred, recommended, or approved way of performing a task', 'https://www.wikidata.org/wiki/Q1630279', '0000-0002-0255-5101',  null);
insert into t_Type_Criterion ( lodTCR, labelTypeCriterion, descTypeCriterion, urlTypeCriterion, populatedBy,  lodTCR_V) values ( 'pid_graph:D2769B37', 'Standard', 'Required norm or standard established to make it easier to do something in a consistent way', 'https://www.wikidata.org/wiki/Q317623', '0000-0002-0255-5101',  null);
