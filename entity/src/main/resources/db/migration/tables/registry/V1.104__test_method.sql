-- ------------------------------------------------
-- Version: v1.104
--
-- Description: Migration that introduces the t_TestMethod table
-- -------------------------------------------------

-- t_TestMethod table
CREATE TABLE t_TestMethod (
    lodTME VARCHAR(255) NOT NULL,
    UUID VARCHAR(255) NOT NULL,
    labelTestMethod VARCHAR(255) NOT NULL,
    descTestMethod VARCHAR(255) DEFAULT NULL,
    lodTypeValue VARCHAR(255) NOT NULL,
    lodTypeProcess VARCHAR(255) NOT NULL,
    numParams INT DEFAULT NULL,
    requestFragment TEXT DEFAULT NULL,
    responseFragment VARCHAR(255) DEFAULT NULL,
    codeFragment TEXT DEFAULT NULL,
    lodMTV VARCHAR(255) DEFAULT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTME_V VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (lodTME)
);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:03615660', '03615660', 'String-Auto', NULL, 'pid_graph:string', 'pid_graph:auto', NULL, NULL, NULL, NULL, 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:17B5AFED', '17B5AFED', 'Years-Manual', 'A test is completed to indicate a duration in months, and converted to decimal years', 'pid_graph:number', 'pid_graph:manual', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "number","minimum": 0, "maximum": 360},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "number"
        }
      }
    ]
}',
'function execTest(#p1) {
    if (typeof #p1 !== "number" || isNaN(#p1)) {
      throw new Error("Input must be a valid number.");
    }
    if (#p1 < 0 || #p1 > 360) {
      throw new Error("Input must be a number between 0 and 360.");
    }
    const years = #p1 / 12;
    return years;
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:23DBAC12', '23DBAC12', 'TRL-Manual', 'A test is completed to indicate a technology readiness level', 'pid_graph:number', 'pid_graph:manual', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "integer", "minimum": 1, "maximum": 9},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "integer"
        }
      }
    ]
}',
'function execTest(#p1) {
    if (typeof #p1 !== "integer" || isNaN(#p1)) {
      throw new Error("Input must be a valid integer.");
    }
    if (#p1 < 1 || #p1 > 9) {
      throw new Error("Input must be an integer between 1 and 9.");
    }
    return #p1;
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:8720C485', '8720C485', 'String-Manual', 'This test is completed manually, selecting a value from a parameter-configurable list', 'pid_graph:string', 'pid_graph:manual', 2,
'{
    "parameters": [
      {
        "name": "#p1",
        "values": "#p2",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "boolean"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "integer"
        }
      }
    ]
}',
'function findStringInArray(#p1, #p2) {
    if (typeof #p1 !== "string") {
      throw new Error("The first parameter must be a string.");
    }
    if (!Array.isArray(#p2)) {
      throw new Error("The second parameter must be an array.");
    }
    for (let i = 0; i < array.length; i++) {
      if (typeof #p2[i] !== "string") {
        throw new Error("The array must contain only strings.");
      }
    }
    const index = #p2.indexOf(#p1);
    return index !== -1 ? index : -1;
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:8C72C972', '8C72C972', 'Percent-Manual', 'A test is completed manually, providing an estimated percentage value for a property or characteristic', 'pid_graph:number', 'pid_graph:manual', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "number","minimum": 0, "maximum": 100},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "number"
        }
      }
    ]
}',
'function execTest(#p1) {
    if (typeof #p1 !== "number" || isNaN(#p1)) {
      throw new Error("Input must be a valid number.");
    }
    if (#p1 < 0 || #p1 > 100) {
      throw new Error("Input must be a number between 0 and 100.");
    }
    return #p1 / 100;
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:8D79984F', '8D79984F', 'Binary-Manual', 'A test is completed manually, returning a single binary value as a result', 'pid_graph:binary', 'pid_graph:manual', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "boolean"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "boolean"
        }
      }
    ]
}',
'async function execTest(#p1) {
    if (!#p1) {
      return false;
    }
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:A57F7707', 'A57F7707', 'Number-Manual', 'A test is completed to indicate any positive number (decimal)', 'pid_graph:number', 'pid_graph:manual', 1,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "number","minimum": 0},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "number"
        }
      }
    ]
}',
'function execTest(#p1) {
    if (typeof #p1 !== "number" || isNaN(#p1)) {
      throw new Error("Input must be a valid integer.");
    }
    if (#p1 < 0) {
      throw new Error("Input must be a positive number.");
    }
    return #p1;
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:B733A7D5', 'B733A7D5', 'Binary-Manual-Evidence', 'A test is completed manually, returning a single binary value with evidence in the result', 'pid_graph:binary', 'pid_graph:manual', 2,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "boolean"},
        "style": "simple"
      },
      {
        "name": "#p2",
        "in": "onscreen",
        "description": "#q2",
        "tooltip": "#t2",
        "required": true,
        "schema": {"type": "url"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "boolean"
        }
      },
      {
        "name": "#p2",
        "schema": {
          "type": "url"
        }
      }
    ]
}',
'async function execTest(#p1, #p2) {
    if (!#p1) {
      return false;
    }
    try {
      const response = await fetch(#p2);
      if (response.status === 200) {
        return true;
      } else {
        return false;
      }
    } catch (error) {
      console.error("Error fetching the URL:", error);
      return false;
    }
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:C62DD0BB', 'C62DD0BB', 'Number-Auto', 'A test is invoked on an external API to return a response variable, from which the test value is extracted.', 'pid_graph:number', 'pid_graph:auto', 3,
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
      },
      {
        "name": "#p2",
        "in": "path",
        "description": "#q2",
        "tooltip": "#t2",
        "required": true,
        "schema": {"type": "string"},
        "style": "simple"
      },
      {
        "name": "#p3",
        "in": "path",
        "description": "#q3",
        "tooltip": "#t3",
        "required": true,
        "schema": {"type": "string"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p3",
        "schema": {
          "type": "number"
        }
      }
    ]
}',
'async function execTest(method, requestData, responseVariable) {
    const apiUrl = `https://api.example.com/" + method + "`;
    try {
      const response = await fetch(apiUrl, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(requestData)
      });
      if (!response.ok) {
        throw new Error("Error: " + response.status + " " + response.statusText);
      }
      const data = await response.json();
      responseVariable.data = data;
    } catch (error) {
      console.error("Error during API call:", error);
    }
}', 'pid_graph:3E109BBA', NULL);



INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:D2D2AAEC', 'D2D2AAEC', 'Binary-Auto', 'A test is executed automatically, returning a single binary value as a result', 'pid_graph:binary', 'pid_graph:auto', 1,
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
      },
      {
        "name": "#p2",
        "in": "path",
        "description": "#q2",
        "tooltip": "#t2",
        "required": true,
        "schema": {"type": "string"},
        "style": "simple"
      },
      {
        "name": "#p3",
        "in": "path",
        "description": "#q3",
        "tooltip": "#t3",
        "required": true,
        "schema": {"type": "string"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p3",
        "schema": {
          "type": "boolean"
        }
      }
    ]
}',
'async function execTest(method, requestData, responseVariable) {
    const apiUrl = `https://api.example.com/" + method + "`;
    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestData)
        });
        if (!response.ok) {
            throw new Error("Error: " + response.status + " " + response.statusText);
        }
        const data = await response.json();
        responseVariable.data = data;
    } catch (error) {
        console.error("Error during API call:", error);
    }
}', 'pid_graph:3E109BBA', NULL);

INSERT INTO t_TestMethod
(lodTME, UUID, labelTestMethod, descTestMethod, lodTypeValue, lodTypeProcess, numParams, requestFragment, responseFragment, codeFragment, lodMTV, lodTME_V)
VALUES
('pid_graph:F164D1F8', 'F164D1F8', 'Ratio-Manual', 'A test is completed manually, returning a single ratio as a result based on two parameter values', 'pid_graph:number', 'pid_graph:manual', 2,
'{
    "parameters": [
      {
        "name": "#p1",
        "in": "onscreen",
        "description": "#q1",
        "tooltip": "#t1",
        "required": true,
        "schema": {"type": "boolean"},
        "style": "simple"
      },
      {
        "name": "#p2",
        "in": "onscreen",
        "description": "#q2",
        "tooltip": "#t2",
        "required": true,
        "schema": {"type": "url"},
        "style": "simple"
      }
    ]
}',
'{
    "response": [
      {
        "name": "#p1",
        "schema": {
          "type": "boolean"
        }
      },
      {
        "name": "#p2",
        "schema": {
          "type": "url"
        }
      }
    ]
}',
'function execTest(#p1, #p2) {
    if (typeof #p1 !== "number" || isNaN(#p1)) {
        throw new Error("Numerator must be a valid number.");
    }
    if (typeof #p2 !== "number" || isNaN(#p2)) {
        throw new Error("Denominator must be a valid number.");
    }
    if (#p2 === 0) {
        throw new Error("Denominator cannot be zero.");
    }
    const ratio = #p1 / #p2;
    return ratio;
}', 'pid_graph:3E109BBA', NULL);
