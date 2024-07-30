-- ------------------------------------------------
-- Version: v1.74
--
-- Description: Migration that introduces the Principles table
-- -------------------------------------------------

-- guidance table
CREATE TABLE Principle (
    id BIGINT AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    pri VARCHAR(255) NOT NULL UNIQUE,
    label VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    modified_by VARCHAR(255)  NULL,
    PRIMARY KEY (id)
)