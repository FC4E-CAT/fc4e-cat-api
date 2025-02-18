-- ------------------------------------------------
-- Version: v1.7
--
-- Description: Migration that introduces the assessment comment table
-- -------------------------------------------------

CREATE TABLE Comment (
    id SERIAL PRIMARY KEY,
    assessment_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);