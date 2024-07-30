-- ------------------------------------------------
-- Version: v1.75
--
-- Description: Migration that introduces the Criteria table
-- -------------------------------------------------

-- criteria table
CREATE TABLE Criteria (
    id BIGINT AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    criteria_code VARCHAR(255) NOT NULL UNIQUE,
    label VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    imperative VARCHAR(255) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    modified_by VARCHAR(255) NULL,
    PRIMARY KEY (id)
)