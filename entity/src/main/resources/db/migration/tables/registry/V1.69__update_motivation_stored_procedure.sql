-- ------------------------------------------------
-- Version: v1.69
--
-- Description: Stored Procedure to copy all relations from an existing motivation to a new motivation.
-- The relations copied include PrincipleCriterion, CriterionMetric, MetricTest, and MetricDefinition.
-- This procedure updates the 'motivation_id' to the new one while keeping other data the same,
-- including setting the 'lastTouch' to the current timestamp for the copied records.
-- --

    -- Drop existing procedure if exists
    DROP PROCEDURE IF EXISTS CopyRelationsToNewMotivation;

    CREATE OR REPLACE PROCEDURE CopyRelationsToNewMotivation(
    newMotivationId VARCHAR,
    sourceMotivationId VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Copy MotivationActorJunction
    INSERT INTO t_Motivation_Actor (motivation_lodMTV, actor_lodActor, lodREL, lodM_A_V, lodMTV_X, lastTouch)
    SELECT newMotivationId, actor_lodActor, lodREL, lodM_A_V, sourceMotivationId, CURRENT_TIMESTAMP
    FROM t_Motivation_Actor
    WHERE motivation_lodMTV = sourceMotivationId;

    -- Copy ActorCriterionJunction
    INSERT INTO p_Criterion_Actor (motivation_lodMTV, actor_lodActor, criterion_lodCRI, imperative_lodIMP, lodC_A_V, lodMTV_X, lastTouch)
    SELECT newMotivationId, actor_lodActor, criterion_lodCRI, imperative_lodIMP, lodC_A_V, sourceMotivationId, CURRENT_TIMESTAMP
    FROM p_Criterion_Actor
    WHERE motivation_lodMTV = sourceMotivationId;

    -- Copy PrincipleCriterionJunction
    INSERT INTO p_Principle_Criterion (motivation_lodMTV, principle_lodPRI, criterion_lodCRI, lodP_C_V, lodREL, lodMTV_X, lastTouch, annotationURL, annotationText)
    SELECT newMotivationId, principle_lodPRI, criterion_lodCRI, lodP_C_V, lodREL, sourceMotivationId, CURRENT_TIMESTAMP, annotationURL, annotationText
    FROM p_Principle_Criterion
    WHERE motivation_lodMTV = sourceMotivationId;

    -- Copy CriterionMetricJunction
    INSERT INTO p_Criterion_Metric (motivation_lodMTV, criterion_lodCRI, metric_lodMTR, lodM_C_V, lodREL, lodMTV_X, lastTouch)
    SELECT newMotivationId, criterion_lodCRI, metric_lodMTR, lodM_C_V, lodREL, sourceMotivationId, CURRENT_TIMESTAMP
    FROM p_Criterion_Metric
    WHERE motivation_lodMTV = sourceMotivationId;

    -- Copy MetricTestJunction
    INSERT INTO p_Metric_Test (motivation_lodMTV, test_lodTES, metric_lodMTR, lodREL, lodMTV_X, lastTouch, lodM_T_TD_V)
    SELECT newMotivationId, test_lodTES, metric_lodMTR, lodREL, sourceMotivationId, CURRENT_TIMESTAMP, lodM_T_TD_V
    FROM p_Metric_Test
    WHERE motivation_lodMTV = sourceMotivationId;

    -- Copy MetricDefinitionJunction
    INSERT INTO p_Metric_Definition (metric_lodMTR, type_benchmark_lodTBN, motivation_lodMTV, lodMTV_X, valueBenchmark, lodMDF, lodMDF_V, lodReference2, lodReference, upload, dataType, lastTouch, lodM_TB_V)
    SELECT metric_lodMTR, type_benchmark_lodTBN, newMotivationId, sourceMotivationId, valueBenchmark, lodMDF, lodMDF_V, lodReference2, lodReference, upload, dataType, CURRENT_TIMESTAMP, lodM_TB_V
    FROM p_Metric_Definition
    WHERE motivation_lodMTV = sourceMotivationId;

EXCEPTION
    WHEN OTHERS THEN
        -- Error handling, without rolling back the transaction
        RAISE EXCEPTION 'An error occurred while copying relations: %', SQLERRM;
END;
$$;