-- ------------------------------------------------
-- Version: v1.73
--
-- Description: Migration that introduces the Guidance table
-- -------------------------------------------------

-- guidance table
CREATE TABLE Guidance (
    id BIGINT AUTO_INCREMENT,
    uuid CHAR(36) NOT NULL UNIQUE,
    gdn VARCHAR(255) NOT NULL UNIQUE,
    label VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status_code VARCHAR(255) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    modified_by VARCHAR(255) NULL,
    PRIMARY KEY (id)
)