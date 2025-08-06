-- ------------------------------------------------
-- Version: v1.92
--
-- Description: Recreates the `assessment_type_template` view to support. This updated view includes additional fields such as `metric_test_created_on`
-- ------------------------------------------------

DROP VIEW IF EXISTS assessment_type_template;

CREATE VIEW assessment_type_template AS
SELECT
    p.lodpri,
    p.pri,
    p.labelprinciple,
    p.descprinciple,

    c,lodcri,
    c.cri,
    c.labelcriterion,
    c.desccriterion,

    m.lodmtr,
    m.mtr,
    m.labelmetric,

    ta.lodtal,
    ta.labelalgorithmtype,
    ttm.lodtmt,
    ttm.labeltypemetric,

    t.tes,
    t.lodtes,
    t.labeltest,
    t.desctest,
    t.testquestion,
    t.testparams,
    t.tooltip,

    m.valuebenchmark,
    mt.created_on AS metric_test_created_on,
    tb.lodtbn,
    tb.labelbenchmarktype,

    pc.motivation_lodmtv AS principle_criterion_motivation_id,
    ca.actor_lodactor AS lodactor,
    i.labelimperative,

    tm.lodtme,
    tm.labeltestmethod,

    mt.motivation_lodmtv AS mt_mtv,
    cm.motivation_lodmtv AS cm_mtv,
    ca.motivation_lodmtv AS ca_mtv,

    md5(
        COALESCE(p.lodpri, '') ||
        COALESCE(c.lodcri, '') ||
        COALESCE(m.lodmtr, '') ||
        COALESCE(t.lodtes, '') ||
        COALESCE(ca.actor_lodactor, '') ||
        COALESCE(pc.motivation_lodmtv, '')
      )::uuid AS lod_id

FROM p_principle_criterion pc
JOIN p_principle p ON pc.principle_lodpri::text = p.lodpri::text
JOIN p_criterion c ON pc.criterion_lodcri::text = c.lodcri::text
JOIN p_criterion_actor ca ON ca.criterion_lodcri::text = c.lodcri::text

LEFT JOIN s_imperative i ON ca.imperative_lodimp::text = i.lodimp::text

LEFT JOIN p_criterion_metric cm
    ON cm.criterion_lodcri::text = c.lodcri::text
    AND cm.motivation_lodmtv::text = pc.motivation_lodmtv::text

LEFT JOIN p_metric m ON cm.metric_lodmtr::text = m.lodmtr::text
LEFT JOIN t_type_benchmark tb ON m.lodtbn::text = tb.lodtbn::text
LEFT JOIN t_type_algorithm ta ON m.lodtal::text = ta.lodtal::text
LEFT JOIN t_type_metric ttm ON m.lodtmt::text = ttm.lodtmt::text

LEFT JOIN p_metric_test mt
    ON mt.metric_lodmtr::text = m.lodmtr::text
    AND mt.motivation_lodmtv::text = pc.motivation_lodmtv::text

LEFT JOIN p_test t ON mt.test_lodtes::text = t.lodtes::text
LEFT JOIN t_testmethod tm ON t.lodtme::text = tm.lodtme::text;

