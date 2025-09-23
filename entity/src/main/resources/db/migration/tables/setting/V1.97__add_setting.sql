-- ------------------------------------------------
-- Version: V1.97
-- Description: Add user info update from token setting
-- ------------------------------------------------

INSERT INTO t_Setting (setting_key, setting_value, setting_label, setting_enable, updated_by)
VALUES ( 'api.cat.user.info.update.from.token', NULL, 'Registration', false, NULL);