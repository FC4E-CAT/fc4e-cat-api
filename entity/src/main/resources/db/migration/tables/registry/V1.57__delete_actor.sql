----- ------------------------------------------------
   -- Version: v1.57
   --
   -- Description: Migration that deletes the PID Service (Component) actor
   -- -------------------------------------------------
UPDATE p_Criterion_Actor set actor_lodActor='pid_graph:E92B9B49' where actor_lodActor='pid_graph:566C01F6';

DELETE FROM t_Motivation_Actor where actor_lodActor='pid_graph:566C01F6';
DELETE FROM p_Criterion_Actor where actor_lodActor='pid_graph:566C01F6';
DELETE FROM t_actor where lodActor='pid_graph:566C01F6';
