-- ------------------------------------------------
-- Version: v1.15
--
-- Description: Migration that updates the json document in the Template table for version 4
-- -------------------------------------------------
UPDATE Template set template_doc = '{
  "id": "",
  "name": "",
  "assessment_type": {
      "id": 1,
      "name": "eosc pid policy"
  },
  "version": "",
  "status": "",
  "published": false,
  "timestamp": "",
  "actor": {
    "id": 6,
    "name": "PID Owner"
  },
  "organisation": {
    "name": "",
    "id": ""
  },
  "subject": {
    "name": "",
    "id": "",
    "type": ""
  },
  "result": {
    "compliance": false,
    "ranking": 0
  },
  "principles": [
    {
      "id": "P1",
      "name": "Application",
      "description": "PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced.",
      "criteria": [
        {
          "id": "C4",
          "name": "Measurement",
          "description": "The PID owner SHOULD maintain PID attributes.",
          "imperative": "may",
          "metric": {
            "type": "number",
            "result": 0,
            "value": 0,
            "benchmark": {
              "equal_greater_than": 1
            },
            "algorithm": "sum",
            "tests": [
              {
                "type": "binary",
                "id": "T4",
                "name": "Maintenance",
                "description": "A test to determine if the entity (PID) attributes are being maintained.",
                "text": "Do you regularly maintain the metadata for your object?",
                "value": 0,
                "evidence_url": [],
                "guidance": {
                  "id": "G1",
                  "description": "In practice, evaluation is very difficult, due to two factors: \\n - It requires that a sample of millions of PID owners be evaluated across all services, and \\n - Some entities may never have to be maintained and are, despite years of non-maintenance, up to date. \\n A measure of the mean update frequency of PIDs across a specific service, and monitoring its change over time against a benchmark, may be the only realistic assessment mechanism."
                }
              }
            ]
          }
        }
      ]
    }
  ]
}' where id=1;