-- ------------------------------------------------
-- Version: v1.79
--
-- Description: Migration that updates the authority template json document in version 4
-- -------------------------------------------------
UPDATE Template set template_doc ='{
  "name": "",
  "version": "",
  "status": "",
  "published": false,
  "timestamp": "",
  "actor": {
    "id": 2,
    "name": "PID Authority"
  },
  "organisation": {
    "id": "",
    "name": ""
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
      "id": "P2",
      "name": "Secure",
      "criteria": [
        {
          "id": "C2",
          "name": "Sensitive Metadata",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T2.1",
                "name": "Secure - Encrypted",
                "text": "Are the API services offered, encrypted using https?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G2",
                  "description": "A series of 5 tests are proposed, all of which need to be satisfied to enable encryption and access control for sensitive metadata. Some of these tests may not apply to all use cases - a topic for future refinement."
                },
                "description": "Test for service encryption"
              },
              {
                "id": "T2.2",
                "name": "Sensitive - Indication",
                "text": "“Is sensitive PID Kernel Metadata defined and supported?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G2",
                  "description": "A series of 5 tests are proposed, all of which need to be satisfied to enable encryption and access control for sensitive metadata. Some of these tests may not apply to all use cases - a topic for future refinement."
                },
                "description": "Test to determine the ability to define PID Kernel Metadata - evidence is provided.",
                "evidence_url": []
              },
              {
                "id": "T2.3",
                "name": "Secure - Encrypted",
                "text": "“Is sensitive PID Kernel Metadata encryption supported?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G2",
                  "description": "A series of 5 tests are proposed, all of which need to be satisfied to enable encryption and access control for sensitive metadata. Some of these tests may not apply to all use cases - a topic for future refinement."
                },
                "description": "A test to determine the ability to encrypt PID Kernel Metadata - evidence is provided.",
                "evidence_url": []
              },
              {
                "id": "T2.4",
                "name": "Secure - Access",
                "text": "Is Access to Sensitive PID Kernel Metadata password protected?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G2",
                  "description": "A series of 5 tests are proposed, all of which need to be satisfied to enable encryption and access control for sensitive metadata. Some of these tests may not apply to all use cases - a topic for future refinement."
                },
                "description": "A test to determine if the Sensitive PID Kernel metadata are protected - evidence is provided.",
                "evidence_url": []
              },
              {
                "id": "T2.5",
                "name": "Secure - Authentication",
                "text": "Does access to Sensitive PID Kernel Metadata require User Authentication?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G2",
                  "description": "A series of 5 tests are proposed, all of which need to be satisfied to enable encryption and access control for sensitive metadata. Some of these tests may not apply to all use cases - a topic for future refinement."
                },
                "description": "A test to determine if PID Kernel Metadata require user authentication - evidence is provided.",
                "evidence_url": []
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 5
            }
          },
          "imperative": "may",
          "description": "Sensitive PID Kernel Metadata requires users to be authenticated - evidence is provided."
        }
      ],
      "description": "PID services for EOSC need to address (a wide variety of) applications (including those) that require secure mechanisms built into the PID Infrastructure."
    },
    {
      "id": "P1",
      "name": "Application",
      "criteria": [
        {
          "id": "C3",
          "name": "Ownership",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T3",
                "name": "Ownership is Visible",
                "text": "Does the PID have an ownership attribute?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G3",
                  "description": "In practice, this may require multiple tests, since in some cases, the ownership is encoded in metadata at the Manager (entity metadata), and in some cases with the Provider or the Authority. In addition, it will require definition of the path or retrieval mechanism for the information, which may be different for each scheme, authority, service, or manager. It could also be different depending on the Kernel Information Profile. Suggestion: store the retrieval instruction/ path as an attribute of a Service."
                },
                "description": "A test determines if an ownership attribute is available for the PID. Evidence is provided of a mechanism to retrieve this information.",
                "evidence_url": []
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 1
            }
          },
          "imperative": "must",
          "description": "The PID owner SHOULD maintain PID attributes."
        },
        {
          "id": "C15",
          "name": "Type Information",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T15.1",
                "name": "Machine-actionable type information",
                "text": "Do you provide a pathway or published API call to verify the type?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G15",
                  "description": "The mechanisms whereby the information can be obtained are unlikely to be standardised or interoperable between authorities. A published API call or code example will be acceptable."
                },
                "description": "A pathway or published API call to verify the type is available.",
                "evidence_url": []
              },
              {
                "id": "T15.2",
                "name": "Machine-actionable management  policy",
                "text": "Do you provide a pathway or published API call to obtain the management policy ?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G15",
                  "description": "The mechanisms whereby the information can be obtained are unlikely to be standardised or interoperable between authorities. A published API call or code example will be acceptable."
                },
                "description": "A pathway or published API call to obtain the management policy is available.",
                "evidence_url": []
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 2
            }
          },
          "imperative": "should",
          "description": "The PID Authority SHOULD provide information on the referenced object’s fundamental type and management policy in a machine-actionable way."
        }
      ],
      "description": "PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced."
    },
    {
      "id": "P13",
      "name": "Persistence",
      "criteria": [
        {
          "id": "C12",
          "name": "Peristence - Authority",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T12",
                "name": "PID cannot be deleted",
                "text": "Can you provide public evidence that the PID will never be deleted?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G12",
                  "description": "Authorities will usually state this fact prominently on their websites, or it may be contained in the published specification for the schema."
                },
                "description": "Public evidence is provided of the fact that the PID will never be deleted.",
                "evidence_url": []
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 1
            }
          },
          "imperative": "should",
          "description": "The PID owner SHOULD maintain PID attributes."
        },
        {
          "id": "C34",
          "name": "Persistence Mean",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T34",
                "name": "Persistence Mean",
                "text": "Using a random but statistically significant sample from the provider, determine the distribution of resolvable PIDs as a function of the time (in units of years) since creation. Extract the mean (m) and the norm (n) and insert below.",
                "type": "value",
                "value": null,
                "threshold": null,
                "value_name": "m",
                "threshold_name": "n",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G34",
                  "description": "Authorities will usually state this fact prominently on their websites, or it may be contained in the published specification for the schema."
                },
                "description": "Published literature [63], [64] suggest that the mean time that PIDs remain resolvable (a measure of persistence) varies quite significantly with the service in question. A randomised, statistically significant sample needs to be evaluated on a periodic basis (annually?) to gauge the persistence of services.",
                "evidence_url": []
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 1
            }
          },
          "imperative": "should",
          "description": "PID Services SHOULD aim for a persistence median that is acceptable to and aligns with community and dependency expectations. "
        }
      ],
      "description": "Persistence description"
    },
    {
      "id": "P9",
      "name": "Resolution",
      "criteria": [
        {
          "id": "C35",
          "name": "Resolution Percentage",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T35",
                "name": "Resolution Percentage",
                "text": "Provide the actual percentage of PIDs that resolve to a viable entity from a random statistically significant sample (f) and the community’s expectation of the percentage of PIDs that resolve to a viable entity from a random statistically significant sample (p) below.",
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
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 1
            }
          },
          "imperative": "should",
          "description": "The PID owner SHOULD maintain PID attributes."
        }
      ],
      "description": "PID Service SHOULD resolve at least p percent of PIDs in a randomised sample, where p is determined by community and dependency expectations."
    },
    {
      "id": "P10",
      "name": "Governance",
      "criteria": [
        {
          "id": "C28",
          "name": "Certification",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T28",
                "name": "Certification",
                "text": "Do you currently have a publicly available declaration of your willingness to be certified?",
                "type": "binary",
                "result": null,
                "value":null,
                "guidance": {
                  "id": "G28",
                  "description": "In practice, evaluation is very difficult, due to two factors: \\n - It requires that a sample of millions of PID owners be evaluated across all services, and \\n - Some entities may never have to be maintained and are, despite years of non-maintenance, up to date. \\n A measure of the mean update frequency of PIDs across a specific service, and monitoring its change over time against a benchmark, may be the only realistic assessment mechanism."
                },
                "description": "Public declaration of willingness to be certified"
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 1
            }
          },
          "imperative": "should",
          "description": "PID Authorities and Services MUST agree to be certified with a mutually agreed frequency in respect of policy compliance."
        }
      ],
      "description": "PID Service Providers should apply appropriate community governance to ensure that their PID Services and Systems adhere to these policies, and are agile and responsive to the needs of research, Open Science and EOSC."
    }
  ],
  "assessment_type": {
    "id": 1,
    "name": "eosc pid policy"
  }
}' where id=2;
