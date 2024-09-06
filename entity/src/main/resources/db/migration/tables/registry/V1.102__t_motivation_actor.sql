-- ------------------------------------------------
-- Version: v1.102
--
-- Description: Migration that create the t_Motivation_Actor table
-- -------------------------------------------------

CREATE TABLE t_Motivation_Actor (
    motivation_lodMTV VARCHAR(255),
    actor_lodActor VARCHAR(255),

    lodM_A_V INT NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodMTV_X VARCHAR(255) NOT NULL,
    lodREL VARCHAR(255) NOT NULL,

    PRIMARY KEY (motivation_lodMTV, actor_lodActor, lodM_A_V),
    FOREIGN KEY (motivation_lodMTV) REFERENCES t_Motivation(lodMTV) ON DELETE CASCADE,
    FOREIGN KEY (actor_lodActor) REFERENCES t_Actor(lodActor) ON DELETE CASCADE,
    FOREIGN KEY (lodREL) REFERENCES s_Relation(REL)
);



INSERT INTO t_Motivation_Actor  (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:0E00C332', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA','pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:1A718108', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:20A7A125', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:234B60D8', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:566C01F6', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA','pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:7835EF43', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:B5CC396B', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA','pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:D42428D7', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA',1);
INSERT INTO t_Motivation_Actor (actor_lodActor, lodREL, motivation_lodMTV,lodMTV_X, lodM_A_V) VALUES ( 'pid_graph:E92B9B49', 'dcterms:isRequiredBy', 'pid_graph:3E109BBA', 'pid_graph:3E109BBA',1);
