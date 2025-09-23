-- ------------------------------------------------
-- Version:     V1.98
-- Description: Refactor t_setting table
-- ------------------------------------------------

-- Add new JSONB column
ALTER TABLE t_Setting
ADD COLUMN setting_data JSONB;

-- Zenodo setting (id = 1)
UPDATE t_Setting
SET setting_data = jsonb_build_object(
    'label', 'Zenodo',
    'description', 'Settings for publishing assessments to Zenodo.',
    'config', jsonb_build_object(
        'zenodo.api.key', NULL,
        'zenodo.enabled', true
    ),
    'auth', jsonb_build_object('mail', NULL, 'password', NULL)
)
WHERE id = 1;

-- AAI registration setting (id = 2)
UPDATE t_Setting
SET setting_data = jsonb_build_object(
    'label', setting_label,
    'description', 'Auto-filled from user info (AAI registration).',
    'config', jsonb_build_object(setting_key, setting_value)
)
WHERE id = 2;

-- Drop old columns
ALTER TABLE t_Setting
DROP COLUMN setting_key,
DROP COLUMN setting_value,
DROP COLUMN setting_label;
