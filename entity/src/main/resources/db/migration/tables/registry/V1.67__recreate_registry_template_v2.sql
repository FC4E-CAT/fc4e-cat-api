-- ------------------------------------------------
-- Version: v1.66
--
-- Description: Recreate the registry_templates view to reflect the updated testQuestion column size from p_Test_Definition
-- ------------------------------------------------
DROP VIEW IF EXISTS registry_templates;

-- Recreate the view with its original definition
CREATE VIEW registry_templates AS
SELECT p.pri,
    p.labelprinciple,
    p.descprinciple,
    c.cri,
    c.labelcriterion,
    c.desccriterion,
    m.lodmtr,
    m.mtr,
    m.labelmetric,
    ta.labelalgorithmtype,
    ttm.labeltypemetric,
    t.tes,
    t.labeltest,
    t.desctest,
    md.valuebenchmark,
    pc.motivation_lodmtv AS principle_criterion_motivation_id,
    tb.labelbenchmarktype,
    ca.actor_lodactor AS lodactor,
    i.labelimperative,
    tm.labeltestmethod,
    t.testquestion,
    t.testparams,
    t.tooltip,
    md.motivation_lodmtv AS md_mtv,
    mt.motivation_lodmtv AS mt_mtv,
    cm.motivation_lodmtv AS cm_mtv,
    ca.motivation_lodmtv AS ca_mtv
   FROM t_type_benchmark tb
     JOIN p_metric_definition md ON tb.lodtbn::text = md.type_benchmark_lodtbn::text
     JOIN p_metric m ON md.metric_lodmtr::text = m.lodmtr::text
     JOIN p_metric_test mt ON m.lodmtr::text = mt.metric_lodmtr::text
     JOIN t_type_algorithm ta ON m.lodtal::text = ta.lodtal::text
     JOIN t_type_metric ttm ON m.lodtmt::text = ttm.lodtmt::text
     JOIN p_test t ON mt.test_lodtes::text = t.lodtes::text
     JOIN t_testmethod tm ON t.lodtme::text = tm.lodtme::text
     JOIN p_criterion_metric cm ON m.lodmtr::text = cm.metric_lodmtr::text
     JOIN p_criterion c ON cm.criterion_lodcri::text = c.lodcri::text
     JOIN p_criterion_actor ca ON c.lodcri::text = ca.criterion_lodcri::text
     JOIN s_imperative i ON ca.imperative_lodimp::text = i.lodimp::text
     JOIN p_principle_criterion pc ON c.lodcri::text = pc.criterion_lodcri::text
     JOIN p_principle p ON pc.principle_lodpri::text = p.lodpri::text;
