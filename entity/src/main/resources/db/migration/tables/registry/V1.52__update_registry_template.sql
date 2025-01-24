-- ------------------------------------------------
-- Version: v1.44
--
-- Description: Migration that creates a View for registry templates
-- -------------------------------------------------

CREATE OR REPLACE VIEW registry_templates AS
SELECT
        p.PRI,
        p.labelPrinciple,
        p.descPrinciple,
        c.CRI,
        c.labelCriterion,
        c.descCriterion,
        m.lodMTR,
        m.MTR,
        m.labelMetric,
        ta.labelAlgorithmType,
        ttm.labelTypeMetric,
        t.TES,
        t.labelTest,
        t.descTest,
        md.valueBenchmark,
        pc.motivation_lodMTV as principle_criterion_motivation_id,
        tb.labelBenchmarkType,
        ca.actor_lodActor AS lodActor,
        i.labelImperative,
        tm.labelTestMethod,
        td.testQuestion,
        td.testParams,
        td.toolTip,
        md.motivation_lodMTV as md_mtv,
        mt.motivation_lodMTV as mt_mtv,
        cm.motivation_lodMTV as cm_mtv

    FROM
        t_Type_Benchmark tb
        INNER JOIN p_Metric_Definition md ON tb.lodTBN = md.type_benchmark_lodTBN
        INNER JOIN p_Metric m ON md.metric_lodMTR = m.lodMTR
        INNER JOIN p_Metric_Test mt ON m.lodMTR = mt.metric_lodMTR
        INNER JOIN t_Type_Algorithm ta ON m.lodTAL = ta.lodTAL
        INNER JOIN t_Type_Metric ttm ON m.lodTMT = ttm.lodTMT
        INNER JOIN p_Test_Definition td ON mt.test_definition_lodTDF = td.lodTDF
        INNER JOIN t_TestMethod tm ON td.lodTME = tm.lodTME
        INNER JOIN p_Test t ON mt.test_lodTES = t.lodTES
        INNER JOIN p_Criterion_Metric cm ON m.lodMTR = cm.metric_lodMTR
        INNER JOIN p_Criterion c ON cm.criterion_lodCRI = c.lodCRI
        INNER JOIN p_Criterion_Actor ca ON c.lodCRI = ca.criterion_lodCRI
        INNER JOIN s_Imperative i ON ca.imperative_lodIMP = i.lodIMP
        INNER JOIN p_Principle_Criterion pc ON c.lodCRI = pc.criterion_lodCRI
        INNER JOIN p_Principle p ON pc.principle_lodPRI = p.lodPRI;