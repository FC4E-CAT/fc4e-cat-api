-- ------------------------------------------------
-- Version: v1.8
--
-- Description: Migration that introduces the Template table
-- -------------------------------------------------

-- template table
CREATE TABLE Template(
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   template_doc JSON NOT NULL,
   created_on datetime(6) NOT NULL,
   actor_id BIGINT NOT NULL ,
   assessment_type_id BIGINT NOT NULL ,
   FOREIGN KEY (actor_id) REFERENCES Actor(id),

   FOREIGN KEY (assessment_type_id) REFERENCES AssessmentType(id),
   UNIQUE(actor_id,assessment_type_id)
);

INSERT INTO
      Template(id, created_on,actor_id,assessment_type_id, template_doc)
VALUES
      (1, now(), 6,1,'{
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
  "assesment_type": "eosc pid policy",
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
}');
