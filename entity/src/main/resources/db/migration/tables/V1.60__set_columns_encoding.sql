
-- ------------------------------------------------
-- Version: v1.60
--
-- Description: Sets table columns characters to utf 8
-- -------------------------------------------------

  ALTER TABLE Validation MODIFY   organisation_role VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
  ALTER TABLE Validation MODIFY   organisation_id VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE Validation MODIFY   organisation_source VARCHAR(8) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE Validation MODIFY    organisation_name VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
ALTER TABLE Validation MODIFY    organisation_website VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
ALTER TABLE Validation MODIFY    validated_by VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;


ALTER TABLE User MODIFY    orcid_id VARCHAR(25) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
ALTER TABLE User MODIFY    name VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
ALTER TABLE User MODIFY    surname VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
ALTER TABLE User MODIFY    email VARCHAR(100) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

 ALTER TABLE Subject MODIFY    name VARCHAR(200) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE Subject MODIFY    type VARCHAR(200) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE Subject MODIFY    subject_id VARCHAR(200) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE Subject MODIFY    created_by VARCHAR(100) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

 ALTER TABLE Actor MODIFY    name VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE Actor MODIFY    description VARCHAR(455) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE History MODIFY    user_id VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE History MODIFY    action VARCHAR(10000) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

 ALTER TABLE AssessmentType MODIFY    name VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
 ALTER TABLE AssessmentType MODIFY    label VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;


 ALTER TABLE JsonSchema MODIFY    id VARCHAR(255) CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
