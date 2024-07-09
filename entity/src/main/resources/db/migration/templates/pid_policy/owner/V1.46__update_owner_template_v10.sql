-- ------------------------------------------------
-- Version: v1.46
--
-- Description: Migration that updates the PID Owner template json document in version 10
-- -------------------------------------------------
UPDATE Template set template_doc ='{
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
    "db_id": null,
    "id": "",
    "name": "",
    "type": ""
  },
   "result": {
    "compliance": null,
    "ranking": null
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
          "imperative": "should",
          "metric": {
            "type": "number",
            "result": null,
            "value": null,
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
                "value": null,
                "result":null,
                "evidence_url": [],
                "guidance": {
                  "id": "G4",
                  "description": "Inventory of public evidence of processes and operations. Subjective evaluation of the completeness of the inventory compared to the infrastructures stated products and services."
                }
              }
            ]
          }
        }
      ]
    }
  ]
}' where id=1;
