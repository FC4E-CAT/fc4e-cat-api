-- ------------------------------------------------
-- Version: v1.77
--
-- Description: Migration that updates the manager template json document in version 4
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
                "id": "T2.1",
                "name": "Secure - Encrypted",
                "text": "Are the API services offered, encrypted using https?",
                "type": "binary",
                "value": null,
                "result": null,
                "guidance": {
                  "id": "G2",
                  "description": "BP14- Managers SHOULD consider implementation of practices to deal with sensitive metadata in cases where it is required. Remember that sensitive metadata cannot be indexed. As such, it is not useful for discovery, but only for reuse. The exception may occur when it is possible to grant access to an entire catalogue or collection. Some ideas:\\n (1)  Avoid Sensitive Metadata: Owners are asked not to include sensitive metadata when describing an object or concept. \\n (2) Compartmentalise: Sensitive metadata is submitted as an encrypted or protected file, and is not indexed. Access is granted on request by the Owner or Curator. \\n (3) \\n BP15- Managers MUST develop and implement mechanisms to ensure continued access should their services need to wind down or change, and preferably have access to sustainable funding. If applicable, certification as a trustworthy repository ensures that adequate measures are in place. Sustainability aspects to consider are: \\n (1) Technical: Data must be open, accessible, and adequately mirrored and backed up. Software used for metadata and PID management should preferably be open source.\\n (2) Financial: Managers should have a sustainable business model.\\n (3) Social and Governance: A continuity plan that makes provision for transfer of custom metadata, digital objects, and associated supplementary materials to a suitable custodian environment should be a strong consideration.\\n Continuity options vary, ands depend on the nature of the digital objects. \\n (1) Open digital content, with simple content types, can typically just be exported as static web resources that require little further curation. \\n (2) If the digital objects are large or complex and need specialised technology to be maintained, the continuity options also become more complex. \\n (3) If some of the digital objects and metadata are sensitive, active management of access requests will be required. \\n"
                },
                "description": "Test for service encryption"
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
                "text": "Are the specifics of the ownership transfer provisions publicly available through a contract or procedure?",
                "value": null,
                "result":null,
                "evidence_url": [],
                "guidance": {
                  "id": "G6",
                  "description": "BP7- Managers SHOULD consider the type of identifier and determine its stability (preferably a published and managed standard), as well as its implications for migration and its scope. Also consider human readability - important in some use cases!"
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
                "text": "Can you provide evidence that the relation between PIDs and entities, as maintained by the PID manager, conforms to the Authority requirements.",
                "value": null,
                "result":null,
                "evidence_url": [],
                "guidance": {
                  "id": "G7",
                  "description": "BP8- Managers SHOULD consider the type of resolution mechanism offered to users and owners when selecting a service and creating their own infrastructure. Impacts on usability for humans and machines, and on interoperability. \\n BB9- Managers MUST maintain the link between the identifier and its resolution mechanism, and the object or concept being referenced. In most cases, Managers offer custom metadata for the object or concept that is authoritative, and this MUST be maintained. Tombstones MUST be offered in cases where objects or concepts are no longer available, based on rational cases - see below."
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
                  "id": "G35",
                  "description": "BP1- Select the PID Stack with persistence, uniqueness, and resolution characteristics appropriate to the use case."
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
                "text": "Is your versioning policy publicly available?",
                "value": null,
                "result":null,
                "evidence_url": [],
                "guidance": {
                  "id": "G11",
                  "description": "BP3- Managers MUST have a clear policy on version management, and the provisions of the policy depends on the purpose of referencing resources by the persistent identifier. User expectations in respect of content variance is not a single concept, although there is often a perception that the resource referenced by a PID should ‘remain unchanged forever’. There are several generic scenarios. \\n (1) Stable Citation/ Reference: Metadata and non-critical data enhancements lead to minor versions with the same PID and provenance \\n (2) Reproducibility and Authenticity: Data amendments that change the checksum of the referenced object leads to a new PID with provenance links to the previous version \\n (3) Content Evolution and Manifestations: All previous versions must be available, and there is a choice \\n (3.1) Same PID, resolving to the latest version but with previous versions easily available (e.g. Zenodo) \\n (3.2) Each ‘manifestation’ has a unique identifier with version links to other manifestations.\\n (4) Dynamic Content Growth: Community recommendations from RDA, and published formally,  represents good practice. \\n BP10- Managers SHOULD manage metadata in alignment with community and disciplinary standards, and MUST maintain an authoritative version of the metadata - either as kernel or custom metadata - in collaboration with the owner (depositor)."
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
                  "description":"BP1- Select the PID Stack with persistence, uniqueness, and resolution characteristics appropriate to the use case. \\n BP8- Managers SHOULD consider the type of resolution mechanism offered to users and owners when selecting a service and creating their own infrastructure. Impacts on usability for humans and machines, and on interoperability. \\n                   BP9-Managers MUST maintain the link between the identifier and its resolution mechanism, and the object or concept being referenced. In most cases, Managers offer custom metadata for the object or concept that is authoritative, and this MUST be maintained. Tombstones MUST be offered in cases where objects or concepts are no longer available, based on rational cases - see below."

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
                  "description": "BP1-Select the PID Stack with persistence, uniqueness, and resolution characteristics appropriate to the use case. \\n BP2- Guaranteeing persistence requires effort - usually from the registry (Authority) and from the Manager. Managers MUST develop policies and procedures to guarantee maintenance of the correct link between the identifier and the resolution target, and make sure the responsibilities are well defined."
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
                  "description":"BP9- Managers MUST maintain the link between the identifier and its resolution mechanism, and the object or concept being referenced. In most cases, Managers offer custom metadata for the object or concept that is authoritative, and this MUST be maintained. Tombstones MUST be offered in cases where objects or concepts are no longer available, based on rational cases - see below."
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
                "text": "Can you provide evidence of policies and/or procedures aimed at managers?",
                 "type": "binary",
                "value": null,
                "result":null,

                "guidance": {
                  "id": "G19",
                  "description":"BP10 - Managers SHOULD manage metadata in alignment with community and disciplinary standards, and MUST maintain an authoritative version of the metadata - either as kernel or custom metadata - in collaboration with the owner (depositor)."
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
                "text": "Can you provide public documentation of the PID’s cost structure or fee model and how it results in no costs for end users wishing to requisition, register and resolve PIDs?",
                 "type": "binary",
                "value": null,
                "result":null,

                "guidance": {
                  "id": "G22",
                  "description":"BP6- Managers MUST consider the scale at which PIDs will be used - this can range from 100s (for research outputs) to hundreds of millions (for graph-like nodes and relations with versioning and authenticity). Two interrelated considerations: scalability of the service, and the cost. Also consider future migration and annual growth. \\n BP12- Managers SHOULD consider implementation of content negotiation and machine-actionable links to improve the usability of the resource across the research enterprise (‘mediations’). \\n BP13- Managers SHOULD consider implementation of mechanisms (procedures) to verify the integrity of resolution - for large collections this could be based on sampling. Integrity verification includes two elements: link rot and content drift, and the latter is partly dependent on versioning strategy."
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
                "text": "Can you provide evidence of a contract requirement between service providers and managers?",
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
                "text": "Do you currently have a publicly available declaration of your willingness to be certified?",
                "type": "binary",
                "result": null,
                "value": null,
                "guidance": {
                  "id": "G28",
                  "description": "BP15- Managers MUST develop and implement mechanisms to ensure continued access should their services need to wind down or change, and preferably have access to sustainable funding. If applicable, certification as a trustworthy repository ensures that adequate measures are in place. Sustainability aspects to consider are: \\n (1) Technical: Data must be open, accessible, and adequately mirrored and backed up. Software used for metadata and PID management should preferably be open source. \\n (2) Financial: Managers should have a sustainable business model.\\n (3) Social and Governance: A continuity plan that makes provision for transfer of custom metadata, digital objects, and associated supplementary materials to a suitable custodian environment should be a strong consideration. \\n Continuity options vary, ands depend on the nature of the digital objects. \\n (1) Open digital content, with simple content types, can typically just be exported as static web resources that require little further curation.\\n (2) If the digital objects are large or complex and need specialised technology to be maintained, the continuity options also become more complex. \\n (3) If some of the digital objects and metadata are sensitive, active management of access requests will be required. \\n Managers MUST develop and implement mechanisms to ensure continued access should their services need to wind down or change, and preferably have access to sustainable funding. If applicable, certification as a trustworthy repository ensures that adequate measures are in place. Sustainability aspects to consider are: \\n (1) Technical: Data must be open, accessible, and adequately mirrored and backed up. Software used for metadata and PID management should preferably be open source.\\n (2) Financial: Managers should have a sustainable business model. \\n (3) Social and Governance: A continuity plan that makes provision for transfer of custom metadata, digital objects, and associated supplementary materials to a suitable custodian environment should be a strong consideration. \\n Continuity options vary, ands depend on the nature of the digital objects. \\n (1) Open digital content, with simple content types, can typically just be exported as static web resources that require little further curation. \\n (2) If the digital objects are large or complex and need specialised technology to be maintained, the continuity options also become more complex. \\n (3) If some of the digital objects and metadata are sensitive, active management of access requests will be required. \\n "
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
                  "description": "BP1- Select the PID Stack with persistence, uniqueness, and resolution characteristics appropriate to the use case."
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
}
'  where id=4;
