
INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:069489G8', 'Auto-AAI-Check-Entitlements', 'Validate AARC-G069 compliance for a specific AAI provider', 'Fetches metadata from NACO and checks whether the `entitlements` claim is present in both `user_info` and `introspection_info`, and conforms to the URN format defined in AARC-G069.',  null,1);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment)
VALUES
('pid_graph:G0A6ADLC', 'G0A6ADLC', 'Auto-Check-String-Binary', 'A test is executed automatically, returning a single binary value as a result', 'pid_graph:binary', 'pid_graph:auto', 1,
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

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:G0AR813A', 'pid_graph:G0A6ADLC', 'Automated confirmation of AARC-G069 compliance', 'String', '“aaiProviderId”', '“The AAI Provider ID to validate AARC-G069 compliance.”', '“Please provide a valid AAI Provider ID”', NULL, 'pid_graph:069489G8', NULL);