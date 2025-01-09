INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:221489E8', 'Auto-MD-1a', 'Administrative Contact Details', 'administrative contact details for the AA Operator, including at least one role-based email address and one postal contact address',  'pid_graph:5EB0885A',1);
INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:39148A01', 'Auto-MD-1b1', 'Operational Security Contact Email', 'an operational security contact for the AA Operator, being at least a role-based email address',  'pid_graph:5EB0885A',1);
INSERT INTO p_Test (lodTES , TES , labelTest, descTest, lodMTV , lodTES_V) VALUES ('pid_graph:12A3BBE8', 'Auto-MD-1b2', 'Operational Security Contact Phone Number','an operational security contact for the AA Operator, preferably including a telephone number',  'pid_graph:5EB0885A',1);


INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment)
VALUES
('pid_graph:B1C2ABA', 'B1C2ABA', 'Auto-Check-Xml-MD1a', 'A test is executed automatically to validate MD-1a, returning a single binary value as a result', 'pid_graph:binary', 'pid_graph:auto', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "path",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "url"},
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
('pid_graph:34S2ABA', '34S2ABA', 'Auto-Check-Xml-MD1b1', 'A test is executed automatically to validate MD-1b1, returning a single binary value as a result', 'pid_graph:binary', 'pid_graph:auto', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "path",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "url"},
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
('pid_graph:AF122ABA', 'AF122ABA', 'Auto-Check-Xml-MD1b2', 'A test is executed automatically to validate MD-1b2, returning a single binary value as a result', 'pid_graph:binary', 'pid_graph:auto', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "path",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "url"},
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
VALUES ('pid_graph:C4CF813A', 'pid_graph:B1C2ABA', 'Automated confirmation of MD-1a validation', 'path', '“xmlUrlValidation”', '“Url of metadata xml  to check if it validates with MD-1a schema requirements”', '“Please provide a valid metadata xml url to be compared with the schema”', NULL, 'pid_graph:221489E8', NULL);

INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:23GF813A', 'pid_graph:34S2ABA', 'Automated confirmation of MD-1b1 validation', 'path', '“xmlUrlValidation”', '“Url of metadata xml  to check if it validates with MD-1b1 schema requirements”', '“Please provide a valid metadata xml url to be compared with the schema”',NULL, 'pid_graph:39148A01', NULL);


INSERT INTO p_Test_Definition (lodTDF, lodTME, labelTestDefinition, paramType, testParams, testQuestion, toolTip, lodMTV, lodTES, lodTDF_V)
VALUES ('pid_graph:23GF848A', 'pid_graph:AF122ABA', 'Automated confirmation of MD-1b2 validation', 'path', '“xmlUrlValidation”', '“Url of metadata xml  to check if it validates with MD-1b2 schema requirements”', '“Please provide a valid metadata xml url to be compared with the schema”', NULL, 'pid_graph:12A3BBE8', NULL);

