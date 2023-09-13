-- ------------------------------------------------
-- Version: v1.31
--
-- Description: Migration that updates the manager template json document in the Template table
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
    },
       {
      "id": "P13",
      "name": "Persistence",
      "criteria": [
        {
          "id": "C14",
          "name": "Resolution Authenticity or Efficiency",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T14",
                "name": "Resolution Efficiency/ Integrity",
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
                  "id": "G14",
                  "description":"A randomised, statistically significant sample needs to be evaluated on a periodic basis (annually?) to gauge the efficiency of resolution of services. See also G35. For a Manager, it will be difficult to determine the percentage of tombstones in the total resolvable PIDs."
                },
                "description": "This test is equivalent to T35, but the result applies to a Manager.",
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
          "description": "PID Manager MUST ensure that the entity remains linked to the PID. In case that the entity being identified is deleted or ceases to exist, tombstone information needs to be included in the PID attribute set."
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
                "text": "Given a random statistically significant sample for a provider and determining a distribution of resolvable PIDs as a function of time since creation, evaluate a mean m against a norm n. Please provide the values for m and n",
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
      "id": "P6",
      "name": "Diversity",
      "criteria": [
        {
          "id": "C16",
          "name": "Digital Representation",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T16",
                "name": "Digital Representation Exists",
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
                  "id": "G16",
                  "description":"This test can be the same as the Resolution Percentage test (T35), but the scope of computation is by Manager."
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
          "imperative": "must",
          "description": "Physical and conceptual entities MUST be represented via a digital representation (e.g. landing page, metadata, attribute set, database index) to have a presence in the digital landscape."
        }

      ],
      "description": "PIDs can identify many different entities. These can be born digital (e.g. documents, data, software, services - otherwise known as digital objects - and collections made of them), physical (e.g. people, instruments, artefacts, samples), or conceptual (e.g. organisations, projects, vocabularies)."
    },
     {
      "id": "P3",
      "name": "Ecosystem",
      "criteria": [
        {
          "id": "C19",
          "name": "Accurate Entity Metadata",
          "metric": {
            "type": "number",
            "tests": [
              {

                "id": "T19",
                "name": "Assuring accurate entity metadata",
                "text": "Can you provide public evidence of procedures or policies at Managers?",
                 "type": "binary",
                "value": null,
                "result":null,

                "guidance": {
                  "id": "G19",
                  "description":"It is not practically feasible to assess compliance from basic principles - hence public evidence from Managers that policies or processes exist will be the most suitable alternative."
                },
                "description": "Public evidence of procedures or policies at Managers.",
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
          "description": "The PID Service MUST maintain entity metadata as accurately as possible in collaboration with the PID Owner. This copy is the authoritative version."
        }

      ],
      "description": "An ecosystem of PID Infrastructures is needed to support the wide variety of scientific applications and offers sufficient flexibility (service providers, scheme, attribute set) and capacity."
    },
      {
      "id": "P7",
      "name": "Services",
      "criteria": [
        {
          "id": "C22",
          "name": "No End User Cost",
          "metric": {
            "type": "number",
            "tests": [
              {

                "id": "T22",
                "name": "No end user cost",
                "text": "Can you provide public evidence of cost structure or free services offered.?",
                 "type": "binary",
                "value": null,
                "result":null,

                "guidance": {
                  "id": "G22",
                  "description":"Evidence of the absence of costs for basic services to end users (PID requisition and registration, resolution)."
                },
                "description": "Public evidence of cost structure or free services offered.",
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
          "imperative": "shall",
          "description": "The basic services of PID registration and resolution SHALL have no cost to end users."
        },
           {
          "id": "C29",
          "name": "Agreed Responsibilities",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T29",
                "name": "Contract - Services and Managers",
                "text": "Does evidence of a contract between Services and Managers exists?",
                "type": "binary",
                "result": null,
                "value": null,
                "guidance": {
                  "id": "G29",
                  "description": "Evidence of contracting between Services and Managers need not involve individual contracts, since these may be confidential - but could point to the standard text of such a contract. The assumption is that Managers cannot operate without entering into a contract based on the standard text."
                },
                "description": "Evidence of a contract between Services and Managers exists - URL is available.",
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
          "description": "PID Services SHOULD agree with PID Managers the responsibilities for Kernel Information maintenance, preferably via contract."
        }


      ],
      "description": "Services are mature, managed with high availability and uptime, and are capable of integration into research and data infrastructures."
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
                "text": "Are you declaring publicly that you are willing to get certified?",
                "type": "binary",
                "result": null,
                "value": null,
                "guidance": {
                  "id": "G28",
                  "description": "In practice, evaluation is very difficult, due to two factors: \\n - It requires that a sample of millions of PID owners be evaluated across all services, and \\n - Some entities may never have to be maintained and are, despite years of non-maintenance, up to date. \\n A measure of the mean update frequency of PIDs across a specific service, and monitoring its change over time against a benchmark, may be the only realistic assessment mechanism."
                },
                "description": "Public declaration of willingness to be certified",
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
          "description": "PID Authorities and Services MUST agree to be certified with a mutually agreed frequency in respect of policy compliance."
        }
      ],
      "description": "PID Service Providers should apply appropriate community governance to ensure that their PID Services and Systems adhere to these policies, and are agile and responsive to the needs of research, Open Science and EOSC."
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
                  "id": "G35",
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
    }
  ]
}'  where id=4;
