-- ------------------------------------------------
-- Version:     V1.105
-- Description: Add zenodo.url to Zenodo setting
-- ------------------------------------------------

-- ✅ Update Zenodo setting with zenodo.url placeholder
UPDATE t_Setting
SET setting_data = jsonb_set(
    setting_data,
    '{config}',
    (
        setting_data->'config' || jsonb_build_object(
            'zenodo.api.key', NULL,
            'zenodo.enabled', true,
            'zenodo.url', NULL
        )
    )
)
WHERE id = 1;
