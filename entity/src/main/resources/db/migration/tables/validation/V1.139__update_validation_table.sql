-- ------------------------------------------------
-- Version: v1.139
--
-- -------------------------------------------------

UPDATE Validation v INNER JOIN Actor a ON v.actor_id = a.id
SET v.registry_actor_id = CASE
    WHEN a.name = 'PID Scheme' THEN 'pid_graph:0E00C332'
    WHEN a.name = 'PID Authority' THEN 'pid_graph:1A718108'
    WHEN a.name = 'End User' THEN 'pid_graph:20A7A125'
    WHEN a.name = 'Compliance Monitoring' THEN 'pid_graph:234B60D8'
    WHEN a.name = 'PID Owner' THEN 'pid_graph:B5CC396B'
    WHEN a.name = 'PID Manager' THEN 'pid_graph:D42428D7'
    WHEN a.name = 'PID Service Provider' THEN 'pid_graph:E92B9B49'
    WHEN a.name = 'Multi-Primary Administrator' THEN 'pid_graph:7835EF43'
    ELSE 'pid_graph:566C01F6'
END;


ALTER TABLE Validation MODIFY actor_id BIGINT NULL;
