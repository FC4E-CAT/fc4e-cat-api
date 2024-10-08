-- ------------------------------------------------
-- Version: v1.65
--
-- Description: Migration that updates the json schema to version 5
-- -------------------------------------------------

UPDATE JsonSchema SET json_schema = '{
  "$schema": "http://json-schema.org/draft-07/schema",
  "$id": "https://api.cat.argo.grnet.gr/schemas/assessment_schema.json",
  "title": "Assessment Json schema",
  "type": "object",
  "additionalProperties": false,
  "required": [
    "name",
    "assessment_type",
    "version",
    "status",
    "published",
    "timestamp",
    "actor",
    "organisation",
    "subject",
    "result",
    "principles"
  ],
  "properties": {
    "name": {
      "$id": "#root/name",
      "title": "Name",
      "type": "string",
      "minLength": 1
    },
    "assessment_type": {
      "$id": "#root/assessment_type",
      "title": "Assessment_type",
      "additionalProperties": false,
      "type": "object",
      "required": [
        "id",
        "name"
      ],
      "properties": {
        "id": {
          "$id": "#root/assessment_type/id",
          "title": "Id",
          "type": "integer",
          "minimum": 1
        },
        "name": {
          "$id": "#root/assessment_type/name",
          "title": "Name",
          "type": "string",
          "minLength": 1
        }
      }
    },
    "version": {
      "$id": "#root/version",
      "title": "Version",
      "type": "string"
    },
    "status": {
      "$id": "#root/status",
      "title": "Status",
      "type": "string"
    },
    "published": {
      "$id": "#root/published",
      "title": "Published",
      "type": "boolean"
    },
    "timestamp": {
      "$id": "#root/timestamp",
      "title": "Timestamp",
      "anyOf": [
        {
          "type": "string",
          "format": "date-time"
        },
        {
          "type": "string"
        }
      ]
    },
    "actor": {
      "$id": "#root/actor",
      "title": "Actor",
      "type": "object",
      "additionalProperties": false,
      "required": [
        "id",
        "name"
      ],
      "properties": {
        "id": {
          "$id": "#root/actor/id",
          "title": "Id",
          "type": "integer",
          "minimum": 1
        },
        "name": {
          "$id": "#root/actor/name",
          "title": "Name",
          "type": "string",
          "minLength": 1
        }
      }
    },
    "organisation": {
      "$id": "#root/organisation",
      "title": "Organisation",
      "type": "object",
      "additionalProperties": false,
      "required": [
        "name",
        "id"
      ],
      "properties": {
        "name": {
          "$id": "#root/organisation/name",
          "title": "Name",
          "type": "string",
          "minLength": 1
        },
        "id": {
          "$id": "#root/organisation/id",
          "title": "Id",
          "type": "string",
          "minLength": 1
        }
      }
    },
    "subject": {
      "$id": "#root/subject",
      "title": "Subject",
      "type": "object",
      "additionalProperties": false,
      "required": [
        "name",
        "id",
        "type"
      ],
      "properties": {
        "name": {
          "$id": "#root/subject/name",
          "title": "Name",
          "type": "string",
          "minLength": 1
        },
        "id": {
          "$id": "#root/subject/id",
          "title": "Id",
          "type": "string",
          "minLength": 1
        },
        "type": {
          "$id": "#root/subject/type",
          "title": "Type",
          "type": "string",
          "minLength": 1
        },
        "db_id": {
          "$id": "#root/subject/db_id",
          "title": "Database ID",
          "type": [
            "number",
            "null"
          ],
          "minimum": 1
        }
      }
    },
    "result": {
      "$id": "#root/result",
      "title": "Result",
      "type": "object",
      "additionalProperties": false,
      "required": [
        "compliance",
        "ranking"
      ],
      "properties": {
        "compliance": {
          "$id": "#root/result/compliance",
          "title": "Compliance",
          "type": [
            "boolean",
            "null"
          ]
        },
        "ranking": {
          "$id": "#root/result/ranking",
          "title": "Ranking",
          "type": [
            "number",
            "null"
          ],
          "minimum": 0
        }
      }
    },
    "principles": {
      "$id": "#root/principles",
      "title": "Principles",
      "type": "array",
      "minItems": 1,
      "items": {
        "$id": "#root/principles/items",
        "title": "Items",
        "additionalProperties": false,
        "type": "object",
        "required": [
          "id",
          "name",
          "description",
          "criteria"
        ],
        "properties": {
          "id": {
            "$id": "#root/principles/items/id",
            "title": "Id",
            "type": "string"
          },
          "name": {
            "$id": "#root/principles/items/name",
            "title": "Name",
            "type": "string"
          },
          "description": {
            "$id": "#root/principles/items/description",
            "title": "Description",
            "type": "string"
          },
          "criteria": {
            "$id": "#root/principles/items/criteria",
            "title": "Criteria",
            "type": "array",
            "minItems": 1,
            "items": {
              "$id": "#root/principles/items/criteria/items",
              "title": "Items",
              "additionalProperties": false,
              "type": "object",
              "required": [
                "id",
                "name",
                "description",
                "imperative",
                "metric"
              ],
              "properties": {
                "id": {
                  "$id": "#root/principles/items/criteria/items/id",
                  "title": "Id",
                  "type": "string"
                },
                "name": {
                  "$id": "#root/principles/items/criteria/items/name",
                  "title": "Name",
                  "type": "string"
                },
                "description": {
                  "$id": "#root/principles/items/criteria/items/description",
                  "title": "Description",
                  "type": "string"
                },
                "imperative": {
                  "$id": "#root/principles/items/criteria/items/imperative",
                  "title": "Imperative",
                  "type": "string"
                },
                "metric": {
                  "$id": "#root/principles/items/criteria/items/metric",
                  "title": "Metric",
                  "type": "object",
                  "additionalProperties": false,
                  "required": [
                    "type",
                    "result",
                    "value",
                    "benchmark",
                    "algorithm",
                    "tests"
                  ],
                  "properties": {
                    "type": {
                      "$id": "#root/principles/items/criteria/items/metric/type",
                      "title": "Type",
                      "type": "string"
                    },
                    "result": {
                      "$id": "#root/principles/items/criteria/items/metric/result",
                      "title": "Result",
                      "type": [
                        "number",
                        "null"
                      ],
                      "minimum": 0
                    },
                    "value": {
                      "$id": "#root/principles/items/criteria/items/metric/value",
                      "title": "Value",
                      "type": [
                        "number",
                        "null"
                      ],
                      "minimum": 0
                    },
                    "benchmark": {
                      "$id": "#root/principles/items/criteria/items/metric/benchmark",
                      "title": "Benchmark",
                      "type": "object",
                      "additionalProperties": false,
                      "required": [
                        "equal_greater_than"
                      ],
                      "properties": {
                        "equal_greater_than": {
                          "$id": "#root/principles/items/criteria/items/metric/benchmark/equal_greater_than",
                          "title": "Equal_greater_than",
                          "type": "number",
                          "minimum": 1
                        }
                      }
                    },
                    "algorithm": {
                      "$id": "#root/principles/items/criteria/items/metric/algorithm",
                      "title": "Algorithm",
                      "type": "string"
                    },
                    "tests": {
                      "$id": "#root/principles/items/criteria/items/metric/tests",
                      "title": "Tests",
                      "type": "array",
                      "minItems": 1,
                      "items": {
                        "$id": "#root/principles/items/criteria/items/metric/tests/items",
                        "title": "Item",
                        "type": "object",
                        "required": [
                          "type",
                          "id",
                          "name",
                          "description",
                          "text",
                          "result",
                          "guidance"
                        ],
                        "allOf": [
                          {
                            "if": {
                              "properties": {
                                "type": {
                                  "const": "binary"
                                }
                              }
                            },
                            "then": {
                              "required": [
                                "value"
                              ],
                              "additionalProperties": false,
                              "properties": {
                                "type": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_type",
                                  "title": "Type",
                                  "enum": [
                                    "binary",
                                    "value"
                                  ]
                                },
                                "id": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_id",
                                  "title": "Id",
                                  "type": "string"
                                },
                                "name": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_name",
                                  "title": "Name",
                                  "type": "string"
                                },
                                "description": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_description",
                                  "title": "Description",
                                  "type": "string"
                                },
                                "text": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_text",
                                  "title": "Text",
                                  "type": "string"
                                },
                                "result": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_result",
                                  "title": "Result",
                                  "type": [
                                    "number",
                                    "null"
                                  ],
                                  "minimum": 0,
                                  "maximum": 1
                                },
                                   "evidence_url": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url",
                                  "title": "Evidence",
                                  "type": "array",
                                  "minItems": 0,
                                  "items": {
                                    "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url",
                                    "title": "Item",
                                    "type": "object",
                                    "required": [
                                      "url"
                                    ],
                                    "properties": {
                                      "url": {
                                        "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url/url",
                                        "title": "url",
                                        "type": "string",
                                        "format": "uri",
                                        "pattern": "^https?://"
                                      }
                                    },
                                    "description": {
                                      "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url/description",
                                      "title": "Description",
                                      "type": "string"
                                    }
                                  }
                                },
                                "guidance": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_guidance",
                                  "title": "Guidance",
                                  "type": "object",
                                  "additionalProperties": false,
                                  "required": [
                                    "id",
                                    "description"
                                  ],
                                  "properties": {
                                    "id": {
                                      "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_guidance/id",
                                      "title": "Id",
                                      "type": "string"
                                    },
                                    "description": {
                                      "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_guidance/description",
                                      "title": "Description",
                                      "type": "string"
                                    }
                                  }
                                },
                                "value": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_value",
                                  "title": "Value",
                                  "type": [
                                    "boolean",
                                    "null"
                                  ]
                                }
                              }
                            }
                          },
                          {
                            "if": {
                              "properties": {
                                "type": {
                                  "const": "value"
                                }
                              }
                            },
                            "then": {
                              "additionalProperties": false,
                              "required": [
                                "threshold",
                                "value_name",
                                "threshold_name",
                                "benchmark"
                              ],
                              "properties": {
                                "type": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_type",
                                  "title": "Type",
                                  "enum": [
                                    "binary",
                                    "value"
                                  ]
                                },
                                "id": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_id",
                                  "title": "Id",
                                  "type": "string"
                                },
                                "name": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_name",
                                  "title": "Name",
                                  "type": "string"
                                },
                                "description": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_description",
                                  "title": "Description",
                                  "type": "string"
                                },
                                "text": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_text",
                                  "title": "Text",
                                  "type": "string"
                                },
                                "result": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_result",
                                  "title": "Result",
                                  "type": [
                                    "number",
                                    "null"
                                  ],
                                  "minimum": 0,
                                  "maximum": 1
                                },
                                 "evidence_url": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url",
                                  "title": "Evidence",
                                  "type": "array",
                                  "minItems": 0,
                                  "items": {
                                    "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url",
                                    "title": "Item",
                                    "type": "object",
                                    "required": [
                                      "url"
                                    ],
                                    "properties": {
                                      "url": {
                                        "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url/url",
                                        "title": "url",
                                        "type": "string",
                                        "format": "uri",
                                        "pattern": "^https?://"
                                      }
                                    },
                                    "description": {
                                      "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url/description",
                                      "title": "Description",
                                      "type": "string"
                                    }
                                  }
                                },
                                "guidance": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_guidance",
                                  "title": "Guidance",
                                  "type": "object",
                                  "additionalProperties": false,
                                  "required": [
                                    "id",
                                    "description"
                                  ],
                                  "properties": {
                                    "id": {
                                      "$id": "#root/principles/items/criteria/items/metric/tests/items/number_guidance/id",
                                      "title": "Id",
                                      "type": "string"
                                    },
                                    "description": {
                                      "$id": "#root/principles/items/criteria/items/metric/tests/items/number_guidance/description",
                                      "title": "Description",
                                      "type": "string"
                                    }
                                  }
                                },
                                "value": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_value",
                                  "title": "Value",
                                  "type": [
                                    "number",
                                    "null"
                                  ]
                                },
                                "threshold": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/number_threshold",
                                  "title": "Threshold",
                                  "type": [
                                    "number",
                                    "null"
                                  ]
                                },
                                "value_name": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/value_name",
                                  "title": "Value Name",
                                  "type": "string"
                                },
                                "threshold_name": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/threshold_name",
                                  "title": "Threshold Name",
                                  "type": "string"
                                },
                                "threshold_locked": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/locked_threshold",
                                  "title": "Threshold Locked",
                                  "type": "boolean"
                                },
                                "benchmark": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/value/benchmark",
                                  "title": "Benchmark",
                                  "anyOf": [
                                    {
                                      "type": "object",
                                      "additionalProperties": false,
                                      "required": [
                                        "equal"
                                      ],
                                      "properties": {
                                        "equal": {
                                          "$id": "#root/principles/items/criteria/items/metric/tests/value/benchmark/equal",
                                          "title": "Equal",
                                          "type": "string"
                                        }
                                      }
                                    },
                                    {
                                      "type": "object",
                                      "additionalProperties": false,
                                      "required": [
                                        "equal_greater_than"
                                      ],
                                      "properties": {
                                        "equal_greater_than": {
                                          "$id": "#root/principles/items/criteria/items/metric/tests/value/benchmark/equal_greater_than",
                                          "title": "Equal Greater Than",
                                          "type": "string"
                                        }
                                      }
                                    },
                                    {
                                      "type": "object",
                                      "additionalProperties": false,
                                      "required": [
                                        "equal_less_than"
                                      ],
                                      "properties": {
                                        "equal_less_than": {
                                          "$id": "#root/principles/items/criteria/items/metric/tests/value/benchmark/equal_less_than",
                                          "title": "Equal Less Than",
                                          "type": "string"
                                        }
                                      }
                                    }
                                  ]
                                }
                              }
                            }
                          }
                        ],
                        "properties": {
                          "type": {
                            "$id": "#root/principles/items/criteria/items/metric/tests/items/type",
                            "title": "Type",
                            "enum": [
                              "binary",
                              "value"
                            ]
                          },
                          "id": {
                            "$id": "#root/principles/items/criteria/items/metric/tests/items/id",
                            "title": "Id",
                            "type": "string"
                          },
                          "name": {
                            "$id": "#root/principles/items/criteria/items/metric/tests/items/name",
                            "title": "Name",
                            "type": "string"
                          },
                          "description": {
                            "$id": "#root/principles/items/criteria/items/metric/tests/items/description",
                            "title": "Description",
                            "type": "string"
                          },
                          "text": {
                            "$id": "#root/principles/items/criteria/items/metric/tests/items/text",
                            "title": "Text",
                            "type": "string"
                          },
                          "result": {
                            "$id": "#root/principles/items/criteria/items/metric/tests/items/result",
                            "title": "Result",
                            "type": [
                              "number",
                              "null"
                            ],
                            "minimum": 0,
                            "maximum": 1
                          },
                            "evidence_url": {
                                  "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url",
                                  "title": "Evidence",
                                  "type": "array",
                                  "minItems": 0,
                                  "items": {
                                    "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url",
                                    "title": "Item",
                                    "type": "object",
                                    "required": [
                                      "url"
                                    ],
                                    "properties": {
                                      "url": {
                                        "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url/url",
                                        "title": "url",
                                        "type": "string",
                                        "format": "uri",
                                        "pattern": "^https?://"
                                      }
                                    },
                                    "description": {
                                      "$id": "#root/principles/items/criteria/items/metric/tests/items/binary_evidence_url/description",
                                      "title": "Description",
                                      "type": "string"
                                    }
                                  }
                                },
                          "guidance": {
                            "$id": "#root/principles/items/criteria/items/metric/tests/items/guidance",
                            "title": "Guidance",
                            "type": "object",
                            "additionalProperties": false,
                            "required": [
                              "id",
                              "description"
                            ],
                            "properties": {
                              "id": {
                                "$id": "#root/principles/items/criteria/items/metric/tests/items/guidance/id",
                                "title": "Id",
                                "type": "string"
                              },
                              "description": {
                                "$id": "#root/principles/items/criteria/items/metric/tests/items/guidance/description",
                                "title": "Description",
                                "type": "string"
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}' WHERE id = 'assessment_json_schema';