-- ------------------------------------------------
-- Version: V1.96
-- Description: Create settings table for dynamic runtime config
-- ------------------------------------------------

CREATE TABLE t_Setting (
    id SERIAL PRIMARY KEY,
    setting_key VARCHAR(255) UNIQUE NOT NULL,
    setting_value VARCHAR(255) DEFAULT NULL,
    setting_label VARCHAR(255) DEFAULT NULL,
    setting_enable BOOLEAN DEFAULT FALSE,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255)
);

INSERT INTO t_Setting (setting_key, setting_value, setting_label, setting_enable, updated_by)
VALUES ('zenodo.api.key', NULL, 'Zenodo', false, NULL);


