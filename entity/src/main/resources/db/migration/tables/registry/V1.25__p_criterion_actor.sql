-- ------------------------------------------------
-- Version: v1.25
--
-- Description: Migration that introduces the p_Test table
-- -------------------------------------------------

CREATE TABLE p_Criterion_Actor (
     actor_lodActor VARCHAR(255) NOT NULL,
     criterion_lodCRI VARCHAR(255) NOT NULL,
     imperative_lodIMP VARCHAR(255) NOT NULL,
     motivation_lodMTV VARCHAR(255) NOT NULL,
     lodMTV_X VARCHAR(255) ,
     populatedBy VARCHAR(255) DEFAULT NULL,
     lastTouch  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     lodC_A_V INT NOT NULL,
     PRIMARY KEY (actor_lodActor, criterion_lodCRI, lodC_A_V, motivation_lodMTV),
     FOREIGN KEY (actor_lodActor) REFERENCES t_Actor(lodActor) ON DELETE CASCADE,
     FOREIGN KEY (criterion_lodCRI) REFERENCES p_Criterion(lodCRI) ON DELETE CASCADE,
     FOREIGN KEY (imperative_lodIMP) REFERENCES s_Imperative(lodIMP),
     FOREIGN KEY (motivation_lodMTV) REFERENCES t_Motivation(lodMTV) ON DELETE CASCADE,
     FOREIGN KEY (lodMTV_X) REFERENCES t_Motivation(lodMTV)
);


INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:004D4203', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:024DEF14', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:07BEDE5D', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:10E055C4', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:134EA685', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:135BBF9C', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:1F4D6BEF', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:1F4D6BEF', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:1F4D6BEF', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:B5CC396B', 'pid_graph:213337DB', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:27346307', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:28D9C36B', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:317587D0', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:3BB55CAF', 'pid_graph:293B1DEE', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:3BB55CAF', 'pid_graph:293B1DEE','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:0E00C332', 'pid_graph:3DBBBFBF', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:3DBBBFBF', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:3DBBBFBF', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ('pid_graph:D42428D7', 'pid_graph:3DBBBFBF', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:420EC62B', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:4ED69013', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:59DE77D5', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:5EA1B0C5', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ('pid_graph:566C01F6', 'pid_graph:5F81AEA8', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ('pid_graph:D42428D7', 'pid_graph:694CB39D', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:7FA9929C', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:81D22297', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:81D22297', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:81D22297', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:8503588B', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:91214486', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:91214486', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:954437E3', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:954437E3', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:0E00C332', 'pid_graph:959B7284', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:959B7284', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:959B7284', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:9A184C36', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:A5A41F03', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:A5A41F03', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:A87D0578', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:A8EA1C61', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:B9A31AE3', 'pid_graph:2981F3DD','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:B5CC396B', 'pid_graph:D0339C6A', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:D0339C6A', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:D7C00C05', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:1A718108', 'pid_graph:D969810C', 'pid_graph:BED209B9','pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:D969810C', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:D969810C', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:566C01F6', 'pid_graph:DF512939', 'pid_graph:2981F3DD', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:D42428D7', 'pid_graph:F9FC6BE3', 'pid_graph:BED209B9', 'pid_graph:3E109BBA',NULL,1);
INSERT INTO p_Criterion_Actor(actor_lodActor,criterion_lodCRI,imperative_lodIMP,motivation_lodMTV,lodMTV_X,lodC_A_V) VALUES ( 'pid_graph:E92B9B49', 'pid_graph:145C7355', 'pid_graph:293B1DEE','pid_graph:5EB0885A','pid_graph:5EB0885A',1);



