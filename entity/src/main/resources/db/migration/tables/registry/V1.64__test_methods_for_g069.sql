-- ------------------------------------------------
-- Version: v1.64
--
-- Description: Migration that inserts new test methods for G069
-- ------------------------------------------------

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment)
VALUES
('pid_graph:U0A6ASER', 'U0A6ASER', 'Auto-Check-AARC-G069-User-Info', 'A test is executed automatically, and checks if the entitlements claim is present in user_info and conforms to the URN format defined in AARC-G069.', 'pid_graph:binary', 'pid_graph:auto', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "path",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "String"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p2",
        "schema": {
          "type": "boolean"
        }
      }
    ]
}');

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment)
VALUES
('pid_graph:T0A6OKEN', 'T0A6OKEN', 'Auto-Check-AARC-G069-Token-Introspection', 'A test is executed automatically, and checks if the entitlements claim is present in introspection_info and conforms to the URN format defined in AARC-G069.', 'pid_graph:binary', 'pid_graph:auto', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "path",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "String"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p2",
        "schema": {
          "type": "boolean"
        }
      }
    ]
}');