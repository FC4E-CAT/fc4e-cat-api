ALTER TABLE t_Motivation_Actor ADD COLUMN automated_group_test jsonb;

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment)
VALUES
('pid_graph:G0A5FWLC', 'G0A5FWLC', 'Fully-Automated-Validation', 'This test method executes a fully automated validation. It ensures that all required aspects and attributes defined properly and accessible without manual intervention.', 'pid_graph:binary', 'pid_graph:auto', 1,
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

INSERT INTO p_Test (lodTES , TES , lodTME, testparams, labelTest, descTest, lodMTV , lodTES_V, version, labeltestdefinition, paramtype, testquestion, tooltip) VALUES ('pid_graph:056489G8', 'Userinfo-Name-Claim-Available', 'pid_graph:G0A5FWLC', 'name_user_info', 'Name claim available in Userinfo endpoint.', 'This test verifies that the name claim is available at the Userinfo endpoint of an AARC-compliant Identity Provider.',
null, 1, 1, 'Automated confirmation of AARC-G056 compliance', 'String', 'Does the OpenID Connect UserInfo endpoint of the AAI service return Display Name information within the name claim as required by AARC-G056?', '"Automatically checks if the required claim is released in compliance with the AARC-G056 specification, with no manual steps involved."');

INSERT INTO p_Test (lodTES , TES , lodTME, testparams, labelTest, descTest, lodMTV , lodTES_V, version, labeltestdefinition, paramtype, testquestion, tooltip) VALUES ('pid_graph:890489L8', 'Userinfo-Given-Name-Claim-Available', 'pid_graph:G0A5FWLC', 'given_name_user_info', 'Given name claim available in Userinfo endpoint.', 'This test verifies that the given name claim is available at the Userinfo endpoint of an AARC-compliant Identity Provider.',
null, 1, 1, 'Automated confirmation of AARC-G056 compliance', 'String', 'Does the OpenID Connect UserInfo endpoint of the AAI service return Given Name information within the given_name claim as required by AARC-G056?', '"Automatically checks if the required claim is released in compliance with the AARC-G056 specification, with no manual steps involved."');