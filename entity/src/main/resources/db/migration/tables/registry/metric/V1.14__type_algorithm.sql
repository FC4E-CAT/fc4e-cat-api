-- ------------------------------------------------
-- Version: v1.14
--
-- Description: Migration that introduces the t_Type_Algorithm table
-- -------------------------------------------------

-- type algorithm table
CREATE TABLE t_Type_Algorithm (
    lodTAL VARCHAR(255) NOT NULL,
    TAL VARCHAR(255) NOT NULL,
    labelAlgorithmType VARCHAR(255) NOT NULL,
    descAlgorithmType VARCHAR(255) NOT NULL,
    uriAlgorithmType VARCHAR(255) DEFAULT NULL,
    functionPattern TEXT NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTAL_V VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (lodTAL)
);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:2050775C', 'WS', 'Weighted Sum', 'Weighted sum of two or more normalised test results, by applying an array of weights', NULL,
'function execAlgorithm(values, weights) {
  if (!Array.isArray(values) || !Array.isArray(weights)) {
    throw new Error("Both inputs must be arrays.");
  }
  if (values.length !== weights.length) {
    throw new Error("Both arrays must have the same length.");
  }
  for (let value of values) {
    if (typeof value !== "number" || value < 0 || value > 1) {
      throw new Error("All elements in the values array must be numbers between 0 and 1.");
    }
  }
  for (let weight of weights) {
    if (typeof weight !== "number" || weight < 0 || weight > 1) {
      throw new Error("All elements in the weights array must be numbers between 0 and 1.");
    }
  }
  let sum = 0;
  for (let i = 0; i < values.length; i++) {
    sum += values[i] * weights[i];
  }
  return sum;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:5CF01298', 'SMax', 'Simple Maximum', 'The algorithm determines the maximum value of a series of tests', NULL,
'function execAlgorithm(values) {
  if (!Array.isArray(values)) {
    throw new Error("Input must be an array.");
  }
  if (values.length === 0) {
    throw new Error("Array cannot be empty.");
  }
  for (let value of values) {
    if (typeof value !== "number" || isNaN(value)) {
      throw new Error("All elements in the array must be valid numbers.");
    }
  }
  const maxValue = Math.max(...values);
  return maxValue;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:682883EA', 'WA', 'Weighted Average', 'Weighted average of two or more normalised test results, by applying an array of weights', NULL,
'function execAlgorithm(values, weights) {
  if (!Array.isArray(values) || !Array.isArray(weights)) {
    throw new Error("Both inputs must be arrays.");
  }
  if (values.length !== weights.length) {
    throw new Error("Both arrays must have the same length.");
  }
  for (let value of values) {
    if (typeof value !== "number" || value < 0 || value > 1) {
      throw new Error("All elements in the values array must be numbers between 0 and 1.");
    }
  }
  for (let weight of weights) {
    if (typeof weight !== "number" || weight < 0 || weight > 1) {
      throw new Error("All elements in the weights array must be numbers between 0 and 1.");
    }
  }
  let weightedSum = 0;
  let weightSum = 0;
  for (let i = 0; i < values.length; i++) {
    weightedSum += values[i] * weights[i];
    weightSum += weights[i];
  }
  if (weightSum === 0) {
    throw new Error("The sum of the weights must not be zero.");
  }
  const weightedAvg = weightedSum / weightSum;
  return weightedAvg;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:746B68DE', 'SMin', 'Simple Minimum', 'The algorithm determines the minimum value of a series of tests', NULL,
'function execAlgorithm(values) {
  if (!Array.isArray(values)) {
    throw new Error("Input must be an array.");
  }
  if (values.length === 0) {
    throw new Error("Array cannot be empty.");
  }
  for (let value of values) {
    if (typeof value !== "number" || isNaN(value)) {
      throw new Error("All elements in the array must be valid numbers.");
    }
  }
  const minValue = Math.min(...values);
  return minValue;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:7A976659', 'TM', 'Test = Metric', 'The metric is the same as the test result without any modification', NULL,
'function execAlgorithm(value) {
  return value;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:AE39C968', 'SS', 'Simple Sum', 'The summation of two or more test results', NULL,
'function execAlgorithm(values) {
  if (!Array.isArray(values)) {
    throw new Error("Input must be an array.");
  }
  for (let value of values) {
    if (typeof value !== "number" || isNaN(value)) {
      throw new Error("All elements in the array must be valid numbers.");
    }
  }
  const sum = values.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
  return sum;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:C82C49FB', 'SM', 'Simple Median', 'The median of two or more test results', NULL,
'function execAlgorithm(values) {
  if (!Array.isArray(values)) {
    throw new Error("Input must be an array.");
  }
  if (values.length === 0) {
    throw new Error("Array cannot be empty.");
  }
  values.sort((a, b) => a - b);
  const mid = Math.floor(values.length / 2);
  return values.length % 2 === 0 ? (values[mid - 1] + values[mid]) / 2 : values[mid];
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:D8A7D9E6', 'SA', 'Simple Average', 'The average of two or more test results', NULL,
'function execAlgorithm(values) {
  if (!Array.isArray(values)) {
    throw new Error("Input must be an array.");
  }
  const sum = values.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
  return sum / values.length;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:E7C09039', 'IFTTT', 'If-This-Then-That', 'Depending on the value of the first test, the function returns the value of either the second or third test', NULL,
'function execAlgorithm(binary, varTrue, varFalse) {
  if (typeof binary !== "boolean") {
    throw new Error("The first parameter must be a boolean.");
  }
  return binary ? varTrue : varFalse;
}', NULL);

INSERT INTO t_Type_Algorithm (lodTAL, TAL, labelAlgorithmType, descAlgorithmType, uriAlgorithmType, functionPattern, lodTAL_V)
VALUES ('pid_graph:F9F44A85', 'EXT', 'External Processing', 'An array of test results is passed to an external API, and the method calculates a single result as a response', NULL,
'async function execAlgorithm(endpointUrl, method, identifier, inputData, responseKey) {
  let fullUrl = endpointUrl + "/" + method;
  if (identifier) {
    fullUrl += "/" + identifier;
  }
  if (!Array.isArray(inputData)) {
    throw new Error("Input data must be an array.");
  }
  const requestBody = { data: inputData };
  try {
    const response = await fetch(fullUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(requestBody)
    });
    if (!response.ok) {
      throw new Error("HTTP error! status: " + response.status);
    }
    const responseData = await response.json();
    if (responseKey in responseData) {
      return responseData[responseKey];
    } else {
      throw new Error("Key \\"" + responseKey + "\\" not found in response.");
    }
  } catch (error) {
    console.error("Error:", error.message);
    throw error;
  }
}', NULL);


CREATE TABLE t_Type_Reproducibility (
    lodTCO VARCHAR(255) NOT NULL,
    labelTypeConfidence VARCHAR(255) NOT NULL,
    descTypeConfidence VARCHAR(255) NOT NULL,
    populatedBy VARCHAR(255) DEFAULT NULL,
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodReference VARCHAR(255) NOT NULL,
    lodTCO_V VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (lodTCO)
);

INSERT INTO t_Type_Reproducibility (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:1BA2356B', 'Highly likely', 'Two independent assessors, observers, or instruments are highly likely to obtain the same results',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:2BD477E1', 'Somewhat Likely', 'Two independent assessors, observers, or instruments are somewhat likely to obtain the same results',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:4F179B32', 'Guaranteed with interval', 'Results are guaranteed to be the same within a confidence interval or level of precision',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:8D254805', 'Unlikely', 'Two independent assessors, observers, or instruments are unlikely to obtain the same results',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:AAC19C15', 'Guaranteed', 'Results are guaranteed to be exactly the same',
 'pid_graph:207965C148', NULL);

INSERT INTO t_Type_Reproducibility  (lodTCO, labelTypeConfidence, descTypeConfidence, lodReference, lodTCO_V)
VALUES ('pid_graph:BEBE61D4', 'Standardised', 'Based on subjective and objective measures, but standardised to such a degree that two assessors will very likely obtain the same result.',
'pid_graph:207965C148', NULL);

