-- ------------------------------------------------
-- Version: v1.11
--
-- Description: Migration that updates the json document in the Template table
-- -------------------------------------------------
UPDATE Template set template_doc = '{
  "id": "",
  "status": "",
  "version": "",
  "name": "",
  "timestamp": "",
  "subject": {
    "id": "",
    "type": "",
    "name": ""
  },
  "assessment_type": "eosc pid policy",
  "actor": "owner",
  "organisation": {
    "id": "",
    "name": ""
  },
  "result": {
    "compliance": false,
    "ranking": 0.0
  },
  "principles": [
    {
      "name": "Principle 1",
      "criteria": [
        {
          "name": "Measurement",
          "type": "optional",
          "metric": {
            "type": "number",
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 1
            },
            "value": 0,
            "result": 0,
            "tests": [
              {
                "type": "binary",
                "text": "Do you regularly maintain the metadata for your object",
                "value": 0,
                "evidence_url": []
              }
            ]
          }
        }
      ]
    }
  ]
}'