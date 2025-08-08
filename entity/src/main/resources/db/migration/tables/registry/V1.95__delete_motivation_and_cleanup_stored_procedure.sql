-- ------------------------------------------------
-- Version: v1.95
--
-- Description: Stored Procedure to delete a Motivation and related assignments,
--              and cleanup orphaned principles, criteria, metrics, and tests.
-- ------------------------------------------------

CREATE OR REPLACE PROCEDURE DeleteMotivationAndCleanup(
    targetMotivationId VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Delete all metric-test relations for this motivation
    DELETE FROM p_Metric_Test
    WHERE motivation_lodMTV = targetMotivationId;

    -- Delete all criterion-metric relations for this motivation
    DELETE FROM p_Criterion_Metric
    WHERE motivation_lodMTV = targetMotivationId;

    -- Delete all criterion-actor relations for this motivation
    DELETE FROM p_Criterion_Actor
    WHERE motivation_lodMTV = targetMotivationId;

    -- Delete all principle-criterion relations for this motivation
    DELETE FROM p_Principle_Criterion
    WHERE motivation_lodMTV = targetMotivationId;

    -- Delete all actor assignments for this motivation
    DELETE FROM t_Motivation_Actor
    WHERE motivation_lodMTV = targetMotivationId;

    -- Delete the motivation itself
    DELETE FROM t_Motivation
    WHERE lodMTV = targetMotivationId;

    -- Cleanup orphaned tests
    DELETE FROM p_Test
    WHERE lodTES NOT IN (
        SELECT test_lodTES FROM p_Metric_Test
    );

    -- Cleanup orphaned metrics
    DELETE FROM p_Metric
    WHERE lodMTR NOT IN (
        SELECT metric_lodMTR FROM p_Criterion_Metric
        UNION
        SELECT metric_lodMTR FROM p_Metric_Test
    );

    -- Cleanup orphaned criteria
    DELETE FROM p_Criterion
    WHERE lodCRI NOT IN (
        SELECT criterion_lodCRI FROM p_Criterion_Actor
        UNION
        SELECT criterion_lodCRI FROM p_Criterion_Metric
        UNION
        SELECT criterion_lodCRI FROM p_Principle_Criterion
    );

    -- Cleanup orphaned principles
    DELETE FROM p_Principle
    WHERE lodPRI NOT IN (
        SELECT principle_lodPRI FROM p_Principle_Criterion
        UNION
        SELECT principle_lodPRI FROM p_Motivation_Principle
    );

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Error while deleting motivation: %', SQLERRM;
END;
$$;
