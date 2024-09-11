-- ------------------------------------------------
-- Version: v1.71
--
-- Description: Migration that updates the manager template json document for PID National Policy in version 2
-- -------------------------------------------------
UPDATE Template set template_doc ='{
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
    }]
        }







' where id=6;
