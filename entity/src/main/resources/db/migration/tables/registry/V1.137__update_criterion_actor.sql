-- update p_Criteron_Actor to add motivation_lodMTV as part of the primary key

UPDATE p_Criterion_Actor
SET motivation_lodMTV = 'pid_graph:3E109BBA'
WHERE motivation_lodMTV IS NULL AND populatedBy IS NULL;

ALTER TABLE p_Criterion_Actor
MODIFY motivation_lodMTV VARCHAR(255) NOT NULL;


CALL DropForeignKeyIfExists('p_Criterion_Actor_ibfk_1');
CALL DropForeignKeyIfExists('p_Criterion_Actor_ibfk_2');
CALL DropPrimaryKeyIfExists('p_Criterion_Actor');


ALTER TABLE p_Criterion_Actor
ADD   FOREIGN KEY (actor_lodActor) REFERENCES t_Actor(lodActor) ON DELETE CASCADE;

ALTER TABLE p_Criterion_Actor
ADD FOREIGN KEY (criterion_lodCRI) REFERENCES p_Criterion(lodCRI) ON DELETE CASCADE;

ALTER TABLE p_Criterion_Actor
ADD PRIMARY KEY (actor_lodActor, criterion_lodCRI, lodC_A_V, motivation_lodMTV);
