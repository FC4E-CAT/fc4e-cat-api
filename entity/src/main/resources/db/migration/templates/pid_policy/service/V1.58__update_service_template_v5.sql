-- ------------------------------------------------
-- Version: v1.58
--
-- Description: Migration that updates the service template json document for version 5
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
    "id": 3,
    "name": "PID Service Provider"
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
      "id": "P1",
      "name": "Application",
      "criteria": [
        {
          "id": "C1",
          "name": "Minimum Operations",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T1.1",
                "name": "CREATE",
                "text": "Do you support CREATE PID? Provide evidence of kernel information.",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Create a PID and provide kernel information: API exists and evidence (URL) is available.",
                "guidance": {
                  "id": "G1",
                  "description": "One may extend the tests to recognise typical and popular standards for API implementation, such as REST, SmartAPI, and the like."
                },
                "evidence_url": []
              },
              {
                "id": "T1.2",
                "name": "UPDATE",
                "text": "Can you support UPDATE PID? Provide evidence of kernel information.",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Update kernel information for existing PID: \\n API exists and evidence (URL) is available.",
                "guidance": {
                  "id": "G1",
                  "description": "One may extend the tests to recognise typical and popular standards for API implementation, such as REST, SmartAPI, and the like."
                },
                "evidence_url": []
              },
              {
                "id": "T1.3",
                "name": "Resolution Service",
                "text": "Can you provide evidence that Resolution API (URL) or URI Pattern exists?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Resolution API (URL) or URI Pattern exists, evidence is provided ",
                "guidance": {
                  "id": "G1",
                  "description": "One may extend the tests to recognise typical and popular standards for API implementation, such as REST, SmartAPI, and the like."
                },
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
          "description": "Service providers SHOULD provide a common Application Programming Interface to interact with PIDs, supporting a minimum set of operations (create, resolve and modify PID and PID Kernel Information)"
        },
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
          "description": "PID ownership MUST be visible to other actors in the ecosystem."
        },
        {
          "id": "C5",
          "name": "Update Functionality",
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
                  "description": "A series of 5 tests are proposed, all of which need to be satisfied to enable encryption and access control for sensitive metadata. Some of these tests may not apply to all use cases - a topic for future refinement"
                },
                "evidence_url": [],
                "description": "Test for service encryption"
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
          "description": "The PID manager MUST provide the functionality required to maintain PID attributes."
        },
        {
          "id": "C10",
          "name": "Versioning - Schema",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T10",
                "name": "Versioning support",
                "text": "Can you provide public evidence of versioning support in Kernel Information Profile or in user guidance?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Public evidence of versioning support in Kernel Information Profile or in user guidance.",
                "guidance": {
                  "id": "G10",
                  "description": "Proposal: Financial information is not always publicly available, and that affects the test.  A ratio of contingency funds (liquid assets) to monthly operational expenditure can be calculated from financial statements, if not available, a public assertion of months of contingency cover will be adequate."
                },
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
          "description": "PID services SHOULD support versioning."
        },
        {
          "id": "C11",
          "name": "Versioning - Procedure",
          "metric": {
            "type": "number",
            "tests": [
              {
                "type": "binary",
                "id": "T11",
                "name": "Versioning Policy",
                "description": "Public evidence of versioning policy.",
                "text": "Is your versioning policy publicly available?",
                "value": null,
                "result": null,
                "evidence_url": [],
                "guidance": {
                  "id": "G11",
                  "description": "Managers can indicate public evidence of versioning policies or procedures."
                }
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
          "description": "PID services and PID Managers SHOULD have clear versioning policies."
        }
      ],
      "description": "PID application depends on unambiguous ownership, proper maintenance, and unambiguous identification of the entity being referenced."
    },
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
                  "description": "A series of 5 tests are proposed, all of which need to be satisfied to enable encryption and access control for sensitive metadata. Some of these tests may not apply to all use cases - a topic for future refinement"
                },
                "evidence_url": [],
                "description": "Test for service encryption"
              },
              {
                "id": "T2.2",
                "name": "Sensitive - Indication",
                "text": "Is sensitive PID Kernel Metadata defined and supported?",
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
                "text": "Is sensitive PID Kernel Metadata encryption supported?",
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
          "description": "Sensitive kernel metadata MAY require access control and/or encryption of the Kernel Information."
        }
      ],
      "description": "PID services for EOSC need to address (a wide variety of) applications (including those) that require secure mechanisms built into the PID Infrastructure."
    },
    {
      "id": "P3",
      "name": "Ecosystem",
      "criteria": [
        {
          "id": "C9",
          "name": "Community Engagement",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T9.1",
                "name": "Community Engagement - User Forum",
                "text": "Can you provide public evidence of a periodic user forum?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Public evidence is provided of a periodic user forum.",
                "guidance": {
                  "id": "G9",
                  "description": "A minimum level of community involvement requires user forums and/ or member and subscriber assemblies."
                },
                "evidence_url": []
              },
              {
                "id": "T9.2",
                "name": "Community Engagement - User Forum",
                "text": "Can you provide public evidence of periodic member or subscriber assemblies?",
                "type": "binary",
                "result": null,
                "value": null,

                "description": "Public evidence is provided of periodic member or subscriber assemblies.",
                "guidance": {
                  "id": "G9",
                  "description": "A minimum level of community involvement requires user forums and/ or member and subscriber assemblies."
                },
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
          "description": "The PID Service SHOULD engage the end user community to determine changes in needs and practices and adjust their services and guidance accordingly."
        }
      ],
      "description": "An ecosystem of PID Infrastructures is needed to support the wide variety of scientific applications and offers sufficient flexibility (service providers, scheme, attribute set) and capacity."
    },
    {
      "id": "P4",
      "name": "Levels of Granularity",
      "criteria": [
        {
          "id": "C8",
          "name": "Guidance",
          "metric": {
            "type": "number",
            "tests": [
              {
                "type": "binary",
                "id": "T8",
                "name": "Use case guidance",
                "description": "Public evidence is available of community guidance on appropriate granularity and application in one or more use cases.",
                "text": "Can you provide public evidence that is available of community guidance on appropriate granularity and application in one or more use cases?",
                "value": null,
                "result": null,
                "evidence_url": [],
                "guidance": {
                  "id": "G6",
                  "description": "Test is initially proposed to be the provision of public evidence."
                }
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
          "description": "The PID Service SHOULD publish guidance on the use cases, levels of granularity, and community best practices that are satisfied by their PID services."
        }
      ],
      "description": "The PID ecosystem ideally supports multiple levels of granularity and encourages/ fosters links between them."
    },
    {
      "id": "P13",
      "name": "Persistance",
      "criteria": [
        {
          "id": "C13",
          "name": "Persistence - Service",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T13.1",
                "name": "PID Persistence - Service - Evidence",
                "text": "Can you provide public evidence that PIDs cannot be deleted.?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Public evidence is provided by the Provider (Service) that PIDs cannot be deleted.",
                "guidance": {
                  "id": "G13",
                  "description": "Proposal: one or more references to publicly available software repositories (Github, Bitbucket, …) verified as deployable. Long-term repositories are better (Software Heritage, …)."
                },
                "evidence_url": []
              },
              {
                "id": "T13.2",
                "name": "PID Persistence - Service - Evidence",
                "text": "Provide the ratio s/a,  where a is the number of PIDs recorded by the Authority and s the number of PIDs recorded by the Service, to be compared with the community expectation b.",
                "type": "value",
                "value": null,
                "threshold": null,
                "threshold_name": "b",
                "value_name": "s/a",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G13",
                  "description": "Initially, it may be simpler to base the test on publicly available evidence, but an automated test could also be possible in future. The number of PIDs recorded by the Authority and those recorded by the Service will be different in practice, since it may include tests, for example. These known differences should be accommodated in an automated test."
                },
                "description": "An inventory of PIDs issued by the Authority on behalf of the Service a is compared to the inventory of PIDs published by the service s and the ratio is larger than a benchmark b determined by the community."
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
          "description": "PID Service MUST ensure that the PID issued by the PID Authority cannot be deleted in its records."
        },
        {
          "id": "C34",
          "name": "Persistence",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T34",
                "name": "Persistence Mean",
                "text": "Using a random but statistically significant sample from the provider, determine the distribution of resolvable PIDs as a function of the time (in units of [INSERT HERE]) since creation. Extract the mean (m) and the norm (n) and insert below.",
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
                "description": "Published literature [63], [64] suggest that the mean time that PIDs remain resolvable (a measure of persistence) varies quite significantly with the service in question. A randomised, statistically significant sample needs to be evaluated on a periodic basis (annually?) to gauge the persistence of services."
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
          "description": "PID Services SHOULD aim for a persistence median that is acceptable to and aligns with community and dependency expectations."
        }
      ],
      "description": "Persistence Description"
    },
    {
      "id": "P6",
      "name": "Diversity",
      "criteria": [
        {
          "id": "C17",
          "name": "Kernel Information Profiles",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T17",
                "name": "Community Involvement - Kernel Information Profiles",
                "text": "Can you provide public evidence that  community involvement exists?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Public evidence of community involvement exists.",
                "guidance": {
                  "id": "G17",
                  "description": "Community involvement can include working groups with community representation, or a community consultation process prior to release of schema."
                },
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
          "description": "PID Services MUST engage the community to develop one or more Kernel Information Profiles appropriate to the use cases addressed by their services."
        }
      ],
      "description": "PIDs can identify many different entities. These can be born digital (e.g. documents, data, software, services - otherwise known as digital objects - and collections made of them), physical (e.g. people, instruments, artefacts, samples), or conceptual (e.g. organisations, projects, vocabularies)."
    },
    {
      "id": "P14",
      "name": "Machine Readability",
      "criteria": [
        {
          "id": "C18",
          "name": "Automation",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T18",
                "name": "Metadata is machine readable",
                "text": "Can you provide publicly available instructions (URL pattern, API call, code example) to prove that metadata is availaible in machine-readable format from the resolution target?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Metadata is available in machine-readable format from the resolution target. This is publicly available by providing a URL pattern, API, or code example.",
                "guidance": {
                  "id": "G18",
                  "description": "There will be significant variation between Services, but publicly available instructions (URL pattern, API call, code example) will be adequate."
                },
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
          "description": "The PID Service SHOULD maintain entity metadata as part of its PID Kernel information, but this source is not authoritative. Its main purpose is automation."
        }
      ],
      "description": "Machine Readability description is missing"
    },
    {
      "id": "P8",
      "name": "Integrated",
      "criteria": [
        {
          "id": "C20",
          "name": "Openly Available",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T20",
                "name": "Services are Open",
                "text": "Can you supply public evidence of open availability of service?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Services (Providers) need to supply public evidence of open availability of services.",
                "guidance": {
                  "id": "G20",
                  "description": "This is best achieved by pointing to a publicly available statement, or endorsement of a set of principles such as POSI [57]."
                },
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
          "description": "Services MUST be available to all researchers in the EU."
        },
        {
          "id": "C21",
          "name": "RI Integration",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T21",
                "name": "Integration with EU RIs",
                "text": "Can you provide one or more evidentiary URLs to demonstrate use of the Service in a RI?",
                "type": "binary",
                "result": null,
                "value": null,
                "description": "Test is one or more evidentiary URLs to demonstrate use of the Service in a RI. Each instance is counted.",
                "guidance": {
                  "id": "G21",
                  "description": "This evidence can be based on example URLs that illustrate the use of the Service in a European Research Infrastructure. It may require a validated list of such Research Infrastructures. If the measure and tests are implemented in this way, it can also be applied to gauge penetration of a Service in RIs."
                },
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
          "description": "Services SHOULD allow integration with European Research Infrastructures."
        }
      ],
      "description": "Services need to integrate well with European Research Infrastructures, but not at the exclusion of the broader research community."
    },
    {
      "id": "P7",
      "name": "Services",
      "criteria": [
        {
          "id": "C23",
          "name": "Basic Service Maturity",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T23",
                "name": "Service Version Age - TRL",
                "text": "How many months is the PID registered service, in operational availability? ",
                "type": "value",
                "value": null,
                "threshold": 9,
                "threshold_name": "b",
                "value_name": "p",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G23",
                  "description": "Services that have been in operation for some months can generally be regarded as production-tested (TRL9). Objective assessment of technology readiness level is somewhat more complex and is not being considered. "
                },
                "description": "Number of months of operational availability of the current and previous version of PID registration service (m), compared to a benchmark b."
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 8
            }
          },
          "imperative": "must",
          "description": "A PID Service infrastructure MUST be at a minimum technology readiness level of 8. This applies to basic services (registration, resolution)."
        },
        {
          "id": "C24",
          "name": "Maturity - Value Added Services",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T23",
                "name": "Service Version Age - TRL",
                "text": "How many months is the PID registered service, in operational availability? ",
                "type": "value",
                "value": null,
                "threshold": 9,
                "threshold_name": "b",
                "value_name": "p",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G23",
                  "description": "Services that have been in operation for some months can generally be regarded as production-tested (TRL9). Objective assessment of technology readiness level is somewhat more complex and is not being considered. "
                },
                "description": "Number of months of operational availability of the current and previous version of PID registration service (m), compared to a benchmark b."
              },
              {
                "id": "T24.1",
                "name": "Statement - TRL - beta",
                "text": "How many services in beta testing is provided?",
                "type": "value",
                "value": null,
                "threshold": 7,
                "threshold_name": "threshold",
                "value_name": "value",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G24",
                  "description": "Public statements about services in beta testing, labs, or experimental use are feasible mechanisms for determining other technology readiness levels."
                },
                "description": "Public statement of new service in beta testing is provided."
              },
              {
                "id": "T24.2",
                "name": "Statement - TRL - labs or experimental",
                "text": "How many services are available as test, experiment, or labs version?",
                "type": "value",
                "value": null,
                "threshold": 6,
                "threshold_name": "threshold",
                "value_name": "value",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G24",
                  "description": "Public statements about services in beta testing, labs, or experimental use are feasible mechanisms for determining other technology readiness levels."
                },
                "description": "Public statement of new service available as test, experiment, or labs version."
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "max",
            "benchmark": {
              "equal_greater_than": 6
            }
          },
          "imperative": "should",
          "description": "Added value services MAY be offered at technology readiness levels lower than 8. \\n OR Added value services SHOULD be offered at technology readiness level 8."
        },
        {
          "id": "C25",
          "name": "Availability - Measure",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T25.1",
                "name": "Availability",
                "text": "Please provide the average annual downtime.",
                "type": "value",
                "value": null,
                "threshold": 8.77,
                "threshold_name": "average annual d",
                "value_name": "d",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G25",
                  "description": "Uptime and availability can be measured by declaration (publicly available value) or by monitoring (this could be expensive and time-consuming)."
                },
                "description": "Public assertion of availability expressed as average annual downtime d, less than 8.77 hours per year."
              },
              {
                "id": "T25.2",
                "name": "Availability",
                "text": "Please provide the average annual downtime of a service endpoint , designated by the Service",
                "type": "value",
                "value": null,
                "threshold": 8.77,
                "threshold_name": "average annual d",
                "value_name": "d",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G25",
                  "description": "Uptime and availability can be measured by declaration (publicly available value) or by monitoring (this could be expensive and time-consuming)."
                },
                "description": "Heartbeat monitoring of a service endpoint designated by the Service, expressed as average annual downtime d, less than 8.77 hours per year."
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "max",
            "benchmark": {
              "equal_greater_than": 8.77
            }
          },
          "imperative": "must",
          "description": "PID Services MUST meet 999 availability and uptime."
        },
        {
          "id": "C26",
          "name": "Availability - Procedure",
          "metric": {
            "type": "number",
            "tests": [
              {
                "type": "binary",
                "id": "T26",
                "name": "Maintenance and Availability Provisions",
                "text": "Can you provide public evidence of relevant provisions?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G26",
                  "description": "Public evidence of maintenance, uptime, and availability provisions is the most feasible option."
                },
                "description": "Publish public evidence of relevant provisions",
                "evidence_url": []
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "max",
            "benchmark": {
              "equal_greater_than": 1
            }
          },
          "imperative": "should",
          "description": "PID Service Providers SHOULD document a summary of their maintenance and availability provisions publicly."
        },
        {
          "id": "C29",
          "name": "Agreed Responsibilities",
          "metric": {
            "type": "number",
            "tests": [
              {
                "type": "binary",
                "id": "T29",
                "name": "Contract - Services and Managers",
                "text": "Can you provide evidence of a contract requirement between service providers and managers?",
                "value": null,
                "result": null,
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
          "imperative": "must",
          "description": "PID Services SHOULD agree with PID Managers the responsibilities for Kernel Information maintenance, preferably via contract."
        }
      ],
      "description": "Services are mature, managed with high availability and uptime, and are capable of integration into research and data infrastructures."
    },
    {
      "id": "P15",
      "name": "Sustainable",
      "criteria": [
        {
          "id": "C27",
          "name": "Continuity",
          "metric": {
            "type": "number",
            "tests": [
              {
                "type": "binary",
                "id": "T27.1",
                "name": "Continuity Provisions - plan",
                "text": "Can you provide public evidence of a continuity plan?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G27",
                  "description": "Public evidence of a continuity plan needs to be provided, as well as declarations that such planning makes provision for an exit strategy and continued resolution."
                },
                "description": "PPublish public evidence of a continuity plan",
                "evidence_url": []
              },
              {
                "type": "binary",
                "id": "T27.2",
                "name": "Continuity Provisions - exit strategy",
                "text": "Do you declare that an exit strategy is presented in the plan?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G27",
                  "description": "Public evidence of a continuity plan needs to be provided, as well as declarations that such planning makes provision for an exit strategy and continued resolution."
                },
                "description": "Public declaration that an exit strategy is presented in the plan",
                "evidence_url": []
              },
              {
                "type": "binary",
                "id": "T27.3",
                "name": "Continuity Provisions - exit strategy",
                "text": "Do you declare that continued resolution is addressed in the plan?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G27",
                  "description": "Public evidence of a continuity plan needs to be provided, as well as declarations that such planning makes provision for an exit strategy and continued resolution."
                },
                "description": "Public declaration that continued resolution is addressed in the plan",
                "evidence_url": []
              }
            ],
            "value": null,
            "result": null,
            "algorithm": "sum",
            "benchmark": {
              "equal_greater_than": 3
            }
          },
          "imperative": "must",
          "description": "PID Service Providers MUST have a clear sustainability and succession plan with an exit strategy that guarantees the continuity of the resolution of its PIDs registered with the service."
        }
      ],
      "description": "Sustainable description."
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
                "value": null,
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
          "imperative": "must",
          "description": "PID Authorities and Services MUST agree to be certified with a mutually agreed frequency in respect of policy compliance."
        },
        {
          "id": "C31",
          "name": "Community Inclusion",
          "metric": {
            "type": "number",
            "tests": [
              {
                "type": "binary",
                "id": "T31.1",
                "name": "Representative Governance - EU Researchers",
                "text": "Do  member(s) of EU research community represented on governance structure?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G31",
                  "description": "Provision can be adequately assessed by asserting publicly that EU researchers are included into governance structures, and by providing public evidence of composition."
                },
                "description": "Public declaration of representation on governance structure by member(s) of EU research community",
                "evidence_url": []
              },
              {
                "type": "binary",
                "id": "T31.2",
                "name": "Representative Governance - Evidence",
                "text": "Do you confirm evidence is available of composition of governance structures?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G31",
                  "description": "Provision can be adequately assessed by asserting publicly that EU researchers are included into governance structures, and by providing public evidence of composition."
                },
                "description": "Public evidence is available of composition of governance structures",
                "evidence_url": []
              },
              {
                "type": "binary",
                "id": "T31.2",
                "name": "Representative Governance - Evidence",
                "text": "Do you confirm evidence is available of composition of governance structures?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G31",
                  "description": "Provision can be adequately assessed by asserting publicly that EU researchers are included into governance structures, and by providing public evidence of composition."
                },
                "description": "Public evidence is available of composition of governance structures",
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
          "imperative": "must",
          "description": "PID Services MUST include representatives of the EU research community."
        },
        {
          "id": "C32",
          "name": "Justifiable Cost",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T32",
                "name": "Justifiable Cost",
                "text": " Please provide structural income (s) and operational expenditure (o) to compute the ratio that decides if time-limited funds are used only for time-limited activities and if operational services are funded from membership and subscription fees",
                "type": "value",
                "value": null,
                "threshold": null,
                "threshold_name": "o",
                "value_name": "s",
                "benchmark": {
                  "equal_greater_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G32",
                  "description": "Determining whether costs to EU or EOSC users is justifiable is not simple: Providers may not be in a position to explain their cost structures, new services may have higher unit costs, and it may be difficult to obtain a representative assessment from all Managers that are in scope should one consider asking them via survey. \\n POSI [10] provides two criteria that can be publicly attested as an alternative: \\n “Time-limited funds are used only for time-limited activities. The day to day operations of the infrastructure should be supported by day to day sustainable revenue sources.” This confirms that Manager costs fees are not subsidising non-service activities. \\n  “Mission-consistent revenue generation. The infrastructure revenue should be consistent with the mission.” This confirms that Manager costs/fees are not subsidising non-PID activities. \\n There are two cases to consider in the tests: \\n Publicly available financial statements are reviewed. \\n A statement is made if financial records are not public. "
                },
                "description": "Publicly confirm that time-limited funds are used only for time-limited activities and that operational services are funded from membership and subscription fees. An appropriate test is formulated for POSI [62] ratio of structural income vs operational expenditure is computed",
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
          "description": "PID Service Providers should apply appropriate community governance to ensure that their PID Services and Systems adherePID Services SHOULD be provided at justifiable cost to PID Owners and PID Managers within EOSC. to these policies, and are agile and responsive to the needs of research, Open Science and EOSC."
        },
        {
          "id": "C33",
          "name": "Global Governance",
          "metric": {
            "type": "number",
            "tests": [
              {
                "type": "binary",
                "id": "T33",
                "name": "Global Governance",
                "description": "Publicly confirm global governance participation.",
                "text": "Do you confirm global governance participation?",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G33",
                  "description": "A public statement about global governance participation will be the simplest option for validation."
                },
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
          "description": "PID Service governance structures SHOULD align or be embedded in global governance structures."
        }
      ],
      "description": "PID Service Providers should apply appropriate communityT30 governance to ensure that their PID Services and Systems adhere to these policies, and are agile and responsive to the needs of research, Open Science and EOSC."
    },
    {
      "id": "P9",
      "name": "Resolution",
      "criteria": [
        {
          "id": "C30",
          "name": "Global Resolution",
          "metric": {
            "type": "number",
            "tests": [
              {
                "id": "T30",
                "name": "Resolution Percentage",
                "text": "Given the percentage f of resolved PIDs that result in a viable entity, compared to a community expectation p. Please provide values for f and p. ",
                "type": "value",
                "value": null,
                "threshold": null,
                "threshold_name": "b",
                "value_name": "c",
                "benchmark": {
                  "equal_less_than": "threshold"
                },
                "result": null,
                "guidance": {
                  "id": "G30",
                  "description": "This provision is somewhat problematic to evaluate objectively. In principle, any HTTP-based service should be available wherever the internet is accessible. In some countries, DNS may not resolve, inter alia due to network configuration or internet censorship policies. As a first step, Services are asked to indicate if they are aware of any countries where their service is not available. This count (or list) has to be compared to a community goal or benchmark. "
                },
                "description": "Public declaration of the countries, if any, where services are not available (count=c). If the number exceeds b, the provision is not satisfied.",
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
          "description": "PID Service Providers MUST ensure their system supports the necessary API for global resolution services."
        },
        {
          "id": "C35",
          "name": "Resolution",
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
          "description": "PID Service SHOULD resolve at least p percent of PIDs in a randomised sample, where p is determined by community and dependency expectations."
        }
      ],
      "description": "There is a need for a generic, global PID resolution system across all PID systems and service providers."
    }
  ]
}'  where id=5;
