-- ------------------------------------------------
-- Version: v1.70
--
-- Description: Migration that adds a new template for the PID Manager actor for PID NL National Policy
-- -------------------------------------------------

INSERT INTO
Template(id, created_on,actor_id,assessment_type_id, template_doc)
VALUES
      (6, now(), 5,2,'{
  "name": "",
  "assessment_type": {
    "id": 2,
    "name": "PID NL National Policy"
  },
  "version": "",
  "status": "",
  "published": false,
  "timestamp": "",
  "actor": {
    "id": 5,
    "name": "PID Manager"
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
          "id": "C5",
          "name": "Update Functionality",
          "description": "The PID manager MUST provide the functionality required to maintain PID attributes.",
          "imperative": "must",
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
                "id": "T5",
                "name": "Update",
                "description": "Test is the same as test T2.1",
                "text": "Is test same as test 2.1?",
                "value": null,
                "result":null,
                "guidance": {
                  "id": "G5",
                  "description": "This test is the same as T2.1 - in cases where the Provider updates relevant attribute changes on behalf of the owner with the Authority. FOr now, we assume that this will always be the case."
                }
              }
            ]
          }
        },
        {
          "id": "C6",
          "name": "Ownership Transfer",
          "description": "The PID manager SHOULD provide policies and contractual arrangements for transfer of ownership should the owner no longer be able to assume responsibilities in compliance with the policy.",
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
                "id": "T6",
                "name": "Ownership Transfer",
                "description":"Public evidence of a contract or procedure that specifies ownership transfer provisions.",
                "text": "Do you provide public evidence of a contract or procedure that specifies ownership transfer provisions?",
                "value": null,
                "result":null,
                "evidence_url": [],
                "guidance": {
                  "id": "G6",
                  "description": "Public evidence is available of contractual or procedural provisions for ownership transfer. This will be a self-assessment for the foreseeable future."
                }
              }
            ]
          }
        },
        {
          "id": "C7",
          "name": "Resolution Integrity",
          "description": "The PID Manager MUST maintain the integrity of the relationship between entities and their PIDs, in conformance to a PID Scheme defined by a PID Authority.",
          "imperative": "must",
          "metric": {
            "type": "number",
            "result": null,
            "value": null,
            "benchmark": {
              "equal_greater_than": 2
            },
            "algorithm": "sum",
            "tests": [
              {
                "type": "binary",
                "id": "T7",
                "name": "Conformance Test",
                "description":"Testing that the relation between PID and entity, maintained by a manager, is conformat with Authority requirements. Existence of public evidence (declaration) is required.",
                "text": "Is the relation betweeen PID and entity, conformat with Authority requirements?",
                "value": null,
                "result":null,
                "evidence_url": [],
                "guidance": {
                  "id": "G7",
                  "description": "This test is proposed initially to be based on evidence provided by the Manager by way of a public declaration (e.g. on their website). Measuring conformance with authority resolution requirements will be much more complex if it is automated - depends on authority and possibly on Manager implementations. "
                }
              },  {
                "id": "T35",
                "name": "Resolution Percentage",
                "text": "Given the percentage f of resolved PIDs that result in a viable entity, compared to a community expectation p. Please provide values for f and p. ",
                "type": "value",
                "value": null,
                "threshold": null,
                "threshold_name": "p",
                "value_name": "f",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G1",
                  "description": "In practice, evaluation is very difficult, due to two factors: \\n - It requires that a sample of millions of PID owners be evaluated across all services, and \\n - Some entities may never have to be maintained and are, despite years of non-maintenance, up to date. \\n A measure of the mean update frequency of PIDs across a specific service, and monitoring its change over time against a benchmark, may be the only realistic assessment mechanism."
                },
                "description": "The test involves determining the percentage f of resolved PIDs that result in a viable entity, compared to a community expectation p.",
                "evidence_url": []
              }
            ]
          }
        },
                {
          "id": "C11",
          "name": "Versioning",
          "description": "PID services and PID Managers SHOULD have clear versioning policies.",
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
                "id": "T11",
                "name": "Versioning Policy",
                "description":"Public evidence of versioning policy.",
                "text": "Do you provide public evidence of versionning policy?",
                "value": null,
                "result":null,
                "evidence_url": [],
                "guidance": {
                  "id": "G11",
                  "description": "Managers can indicate public evidence of versioning policies or procedures."
                }
              }
            ]
          }
        }
      ]
    }

  ]


}');
