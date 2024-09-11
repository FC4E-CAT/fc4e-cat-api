-- ------------------------------------------------
-- Version: v1.89
--
-- Description: Migration that introduces the t_type_benchmark table
-- -------------------------------------------------
CREATE TABLE t_Type_Benchmark(

    lodTBN VARCHAR(255) NOT NULL,
    TBN VARCHAR(255),
    labelBenchmarkType VARCHAR(255),
    descBenchmarkType TEXT,
    functionPattern TEXT,
    pattern VARCHAR(255),
    example VARCHAR(255),
    lodMTV VARCHAR(255),
    populatedBy VARCHAR(255),
    lastTouch TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lodTBN_V VARCHAR(255)
);





INSERT INTO t_Type_Benchmark (lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ( 'pid_graph:0917EC0D', 'I-B-A', 'Integer-Binary-AND', 'The benchmark maps the aggregate of a number of binary tests, expressed as an integer, to a binary assessment and all tests must pass. The benchmark equals the number of tests.', 'function applyBenchmark(value, benchmark) {
  // Check if the input parameters are integers
  if (!Number.isInteger(value) || !Number.isInteger(benchmark)) {
    throw new Error(''Both parameters must be integers.'');
  }
  // Return true if the value is equal to the benchmark, otherwise false
  return value === benchmark;
}', 'M → [Fail, Pass]', 'M<n → 0 (‘Fail’)
M=n → 1 (‘Pass’)', 'pid_graph:3E109BBA', '0000-0002-0255-5101',  NULL);
INSERT INTO t_Type_Benchmark ( lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ('pid_graph:0A6A8E5A', 'S-S', 'String-String', 'Qualitative metric expressed as a string (derived from an opinion) is mapped to the closest semantically similar description of performance', 'TBC - Manual process at present', 'M(n) → P(n)', 'N/A', 'pid_graph:3E109BBA', '0000-0002-0255-5101',  NULL);
INSERT INTO t_Type_Benchmark (lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ( 'pid_graph:16108D9B', 'N-P', 'Normalised Value-Performance', 'The benchmark maps a value (which can be normalised) to a multi-faceted  assessment outcome (e.g. maturity level, performance level)', 'function applyBenchmark(M, testArray, outcomeArray) {
  // Validate input parameters
  if (typeof M !== ''number'') {
    throw new Error(''The first parameter must be a number.'');
  }
  if (!Array.isArray(testArray) || !Array.isArray(outcomeArray)) {
    throw new Error(''The second and third parameters must be arrays.'');
  }
  if (testArray.length !== outcomeArray.length + 1) {
    throw new Error(''The outcome array must have one less element than the test array.'');
  }
  // Determine the appropriate outcome
  for (let i = 0; i < testArray.length - 1; i++) {
    if (M >= testArray[i] && M < testArray[i + 1]) {
      return outcomeArray[i];
    }
  }
  // Handle the last condition separately: c <= M <= d
  if (M >= testArray[testArray.length - 2] && M <= testArray[testArray.length - 1]) {
    return outcomeArray[outcomeArray.length - 1];
  }
  // If no condition is met, return undefined or an appropriate message
  return undefined;
}', 'M → P(n)', '0<=M<a → 0 (‘Poor’)
a<=M<b → 1 (‘Fair’)
b<=M<=1→ 2 (‘Good’)', 'pid_graph:3E109BBA', '0000-0002-0255-5101',  NULL);
INSERT INTO t_Type_Benchmark (lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ('pid_graph:6EBC59CF', 'N-B', 'Normalised Value-Binary', 'The benchmark maps a normalised integer or float value to binary assessment outcome (Pass/ Fail)', 'function applyBenchmark(value, benchmark) {
  // Check if the input parameters are numbers
  if (typeof value !== ''number'' || typeof benchmark !== ''number'') {
    throw new Error(''Both parameters must be numbers.'');
  }
  // Return true if the value is equal to or larger than the benchmark, otherwise false
  return value >= benchmark;
}', 'M → [Fail, Pass]', '0<=M<a→ 0 (‘Fail’)
a<=M<=1 → 1 (‘Pass’)', 'pid_graph:3E109BBA', '0000-0002-0255-5101', NULL);
INSERT INTO t_Type_Benchmark (lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ( 'pid_graph:7085006F', 'B-B', 'Binary-Binary ', 'The benchmark maps a binary metric value to binary assessment outcome (Pass/ Fail). The inverse can also be done.', 'function applyBenchmark(value, invert) {
  // Check if the input parameters are booleans
  if (typeof value !== ''boolean'' || typeof invert !== ''boolean'') {
    throw new Error(''Both parameters must be boolean values.'');
  }
  // Return the value or its inverse based on the invert parameter
  return invert ? !value : value;
}', 'M[0,1] → [Fail, Pass]', 'M=0 (''False'') → 0 (‘Fail’)
M=1 (''True'') → 1 (‘Pass’)', 'pid_graph:3E109BBA', '0000-0002-0255-5101',  NULL);
INSERT INTO t_Type_Benchmark ( lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy,  lodTBN_V) VALUES ( 'pid_graph:84E7AE14', 'S-B', 'String-Binary', 'Qualitative metric expressed as a string (opinion) is mapped to the closest semantically similar description of ''Pass'' and ''Fail''', 'TBC - Manual process at present', 'M → [Fail, Pass]', 'N/A', 'pid_graph:3E109BBA', '0000-0002-0255-5101', NULL);
INSERT INTO t_Type_Benchmark ( lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ( 'pid_graph:8BA6B11D', 'I-B-A', 'Integer-Binary-OR', 'The benchmark maps the aggregate of a number of binary tests, expressed as an integer, to a binary assessment and at least one test must pass. The function imoplementation is generalised - if the benchmark is 1, the above applies, if the benchmark is greater than 1 but smaller than n (number of tests) other results are possible (for example if 3 of 5 symptoms are present, a specific condition is implied).', 'function applyBenchmark(value, benchmark) {
  // Check if the input parameters are integers
  if (!Number.isInteger(value) || !Number.isInteger(benchmark)) {
    throw new Error(''Both parameters must be integers.'');
  }
  // Return true if the value is equal to or larger than the benchmark, otherwise false
  return value >= benchmark;
}', 'M → [Fail, Pass]', 'M<n → 0 (‘Fail’)
M>=n → 1 (‘Pass’)', 'pid_graph:3E109BBA', '0000-0002-0255-5101',  NULL);

INSERT INTO t_Type_Benchmark (lodTBN,TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ( 'pid_graph:AF8098B8', 'V-B-I', 'Value-Binary-Inverse', 'The benchmark maps a numeric value to a becnhmark - smaller or equal to the benhcmark is desired ', 'function applyBenchmark(value, benchmark) {
  // Check if the input parameters are integers
  if (typeof value !== ''number'' || typeof benchmark !== ''number'') {
    throw new Error(''Both parameters must be numbers.'');
  }
  // Return true if the value is equal to or larger than the benchmark, otherwise false
  return value <= benchmark;
}', NULL, NULL, 'pid_graph:3E109BBA', '0000-0002-0255-5101',  NULL);

INSERT INTO t_Type_Benchmark (lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ( 'pid_graph:BBB9ABD5', 'ASO', 'Assessment Outcome', 'A complex benchmark is applied, with an array of values evaluated against benchmark values, and compliance is either mandatory or optional.', 'function evaluateMetrics(metrics, benchmarks, imperatives) {
  // Validate input parameters
  if (!Array.isArray(metrics) || !Array.isArray(benchmarks) || !Array.isArray(imperatives)) {
    throw new Error(''All parameters must be arrays.'');
  }
  if (metrics.length !== benchmarks.length || metrics.length !== imperatives.length) {
    throw new Error(''All arrays must have the same length.'');
  }
  let outcome = true;
  let rank = 0;
  for (let i = 0; i < metrics.length; i++) {
    // Check if imperative is mandatory (true)
    if (imperatives[i]) {
      // If mandatory and does not match, set outcome to false
      if (metrics[i] !== benchmarks[i]) {
        outcome = false;
      }
    } else {
      // If optional and matches, increment rank
      if (metrics[i] === benchmarks[i]) {
        rank++;
      }
    }
  }
  return {
    outcome,
    rank
  };
}', NULL, NULL, 'pid_graph:3E109BBA', '0000-0002-0255-5101', NULL);
INSERT INTO t_Type_Benchmark( lodTBN, TBN, labelBenchmarkType, descBenchmarkType, functionPattern, pattern, example, lodMTV, populatedBy, lodTBN_V) VALUES ( 'pid_graph:C4D0F2B1', 'V-B', 'Value-Binary', 'The benchmark maps a numeric value to a becnhmark - greater or equal to the benhcmark is desired ', 'function applyBenchmark(value, benchmark) {
  // Check if the input parameters are integers
  if (typeof value !== ''number'' || typeof benchmark !== ''number'') {
    throw new Error(''Both parameters must be numbers.'');
  }
  // Return true if the value is equal to or larger than the benchmark, otherwise false
  return value >= benchmark;
}', NULL, NULL, 'pid_graph:3E109BBA', '0000-0002-0255-5101',  NULL);
