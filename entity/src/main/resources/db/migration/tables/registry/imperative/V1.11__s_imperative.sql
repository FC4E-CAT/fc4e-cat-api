-- ------------------------------------------------
-- Version: v1.11
--
-- Description: Migration that introduces the s_Imperative table
-- -------------------------------------------------

CREATE TABLE s_Imperative (
    lodIMP VARCHAR(255) NOT NULL PRIMARY KEY,
    IMP VARCHAR(255) NOT NULL,
    labelImperative  VARCHAR(255) NOT NULL,
    descImperative TEXT NOT NULL,
    populatedBy VARCHAR(255),
    lastTouch  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO s_Imperative (lodIMP, IMP, labelImperative, descImperative) VALUES ( 'pid_graph:293B1DEE', 'Optional', 'MAY', 'This word, or the adjective "OPTIONAL", means that an item is truly optional. One vendor may choose to include the item because a particular marketplace requires it or because the vendor feels that it enhances the product while another vendor may omit the same item. An implementation which does not include a particular option MUST be prepared to interoperate with another implementation which does include the option, though perhaps with reduced functionality. In the same vein an implementation which does include a particular option MUST be prepared to interoperate with another implementation which does not include the option (except, of course, for the feature the option provides.)');
INSERT INTO s_Imperative (lodIMP, IMP, labelImperative, descImperative) VALUES ( 'pid_graph:2981F3DD', 'Optional', 'SHOULD', 'This word, or the adjective "RECOMMENDED", means that there may exist valid reasons in particular circumstances to ignore a particular item, but the full implications must be understood and carefully weighed before choosing a different course.');
INSERT INTO s_Imperative (lodIMP, IMP, labelImperative, descImperative) VALUES ( 'pid_graph:34F8B2A9', 'Mandatory', 'MUST NOT', 'This phrase, or the phrase "SHALL NOT", means that the definition is an absolute prohibition of the specification.');
INSERT INTO s_Imperative (lodIMP, IMP, labelImperative, descImperative) VALUES ( 'pid_graph:A4E528D8', 'Optional', 'SHOULD NOT', 'This phrase, or the phrase "NOT RECOMMENDED" means that there may exist valid reasons in particular circumstances when the particular behavior is acceptable or even useful, but the full implications should be understood and the case carefully weighed before implementing any behavior described with this label.');
INSERT INTO s_Imperative (lodIMP, IMP, labelImperative, descImperative) VALUES ( 'pid_graph:BED209B9', 'Mandatory', 'MUST', 'This word, or the terms "REQUIRED" or "SHALL", mean that the definition is an absolute requirement of the specification.')