-- ------------------------------------------------
-- Version: V1.99
-- Description: Create table for fixed report definitions
-- ------------------------------------------------

CREATE TABLE t_Report_Definition (
    id SERIAL PRIMARY KEY,
    label VARCHAR(255) NOT NULL,
    description TEXT,
    row_dimension VARCHAR(50) NOT NULL,
    column_dimension VARCHAR(50) NOT NULL,
    value_type VARCHAR(50) NOT NULL,
    filters JSONB
);
INSERT INTO t_Report_Definition (label, description, row_dimension, column_dimension, value_type, filters)
VALUES
(
    'Actors × Assessments',
    'Assessment results per actor',
    'actor', 'assessment', 'compliance',
    $$[{"name":"actor","type":"String","required":false},
       {"name":"motivation","type":"String","required":false},
       {"name":"publication_status","type":"String","required":false}]$$::jsonb
),
(
    'Organisations × Assessments',
    'Assessment results per organisation',
    'organisation', 'assessment', 'compliance',
    $$[{"name":"organisation","type":"String","required":false},
       {"name":"motivation","type":"String","required":false},
       {"name":"publication_status","type":"String","required":false}]$$::jsonb
);