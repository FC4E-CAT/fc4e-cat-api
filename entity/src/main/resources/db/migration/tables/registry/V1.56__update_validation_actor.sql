----- ------------------------------------------------
   -- Version: v1.56
   --
   -- Description: Migration that updates the PID Service (Component) actor to PID Service Provider(Role) from  existing validations
   -- -------------------------------------------------
UPDATE Validation set registry_actor_id= 'pid_graph:E92B9B49' where registry_actor_id='pid_graph:566C01F6';
