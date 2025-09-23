-- ------------------------------------------------
-- Version: v1.66
--
-- Description: Migration that drops test_definition from p_Metric_Test table
-- -------------------------------------------------

ALTER TABLE p_Metric_Test
DROP CONSTRAINT p_metric_test_test_definition_lodtdf_fkey;

ALTER TABLE p_Metric_Test
DROP CONSTRAINT p_Metric_Test_pkey;

ALTER TABLE p_Metric_Test
DROP COLUMN test_definition_lodTDF;

ALTER TABLE p_Metric_Test
ADD CONSTRAINT p_Metric_Test_pkey PRIMARY KEY (motivation_lodMTV, metric_lodMTR, test_lodTES, lodM_T_TD_V);