-- ------------------------------------------------
-- Version: V1.100
-- Description: Add "Subjects × Motivations/Actors" report definition
-- ------------------------------------------------

INSERT INTO t_Report_Definition (label, description, row_dimension, column_dimension, value_type, filters)
VALUES
(
    'Subjects × Motivations/Actors',
    'The latest assessment compliance for each subject per motivation/actor',
    'subject', 'motivation_actor', 'compliance',
    $$[
        {"name":"subject","type":"String","required":false},
        {"name":"motivation","type":"String","required":false},
        {"name":"actor","type":"String","required":false},
        {"name":"publication_status","type":"String","required":false}
    ]$$::jsonb
);
