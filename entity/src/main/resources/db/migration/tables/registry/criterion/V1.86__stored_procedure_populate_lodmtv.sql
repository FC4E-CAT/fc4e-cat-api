-- ------------------------------------------------
-- Version: v1.86
--
-- Description: Stored procedure to populate lodmtv field
-- ------------------------------------------------

CREATE OR REPLACE PROCEDURE PopulateLodMTVOnCriterion()
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE p_criterion
    SET lodMTV = tmp.lodMTV
    FROM (
        VALUES
        ('AI-TEST-CRIT', 'pid_graph:5EB0885A'),
        ('AMR-1', 'pid_graph:5EB0885A'),
        ('AMR-2', 'pid_graph:5EB0885A'),
        ('ASDASD', 'pid_graph:8F59FADD'),
        ('AUTO-1', 'pid_graph:5EB0885A'),
        ('AUTO-ARCC-TEST-CRI', 'pid_graph:3EDCB21F'),
        ('C-TEST', 'pid_graph:FE3A1997'),
        ('C1', 'pid_graph:3E109BBA'),
        ('C10', 'pid_graph:3E109BBA'),
        ('C11', 'pid_graph:3E109BBA'),
        ('C12', 'pid_graph:3E109BBA'),
        ('C14', 'pid_graph:3E109BBA'),
        ('C15', 'pid_graph:3E109BBA'),
        ('C16', 'pid_graph:3E109BBA'),
        ('C17', 'pid_graph:3E109BBA'),
        ('C18', 'pid_graph:D0E19F50'),
        ('C19', 'pid_graph:3E109BBA'),
        ('C2', 'pid_graph:3E109BBA'),
        ('C20', 'pid_graph:3E109BBA'),
        ('C21', 'pid_graph:D0E19F50'),
        ('C22', 'pid_graph:3E109BBA'),
        ('C23', 'pid_graph:3E109BBA'),
        ('C24', 'pid_graph:3E109BBA'),
        ('C25', 'pid_graph:3E109BBA'),
        ('C26', 'pid_graph:D0E19F50'),
        ('C27', 'pid_graph:3E109BBA'),
        ('C28', 'pid_graph:3E109BBA'),
        ('C29', 'pid_graph:3E109BBA'),
        ('C3', 'pid_graph:3E109BBA'),
        ('C30', 'pid_graph:3E109BBA'),
        ('C31', 'pid_graph:3E109BBA'),
        ('C32', 'pid_graph:D0E19F50'),
        ('C33', 'pid_graph:3E109BBA'),
        ('C34', 'pid_graph:3E109BBA'),
        ('C35', 'pid_graph:3E109BBA'),
        ('C36', 'pid_graph:D0E19F50'),
        ('C4', 'pid_graph:3E109BBA'),
        ('C5', 'pid_graph:3E109BBA'),
        ('C6', 'pid_graph:3E109BBA'),
        ('C7', 'pid_graph:3E109BBA'),
        ('C8', 'pid_graph:3E109BBA'),
        ('CRITEST', 'pid_graph:95560D23'),
        ('CRITESTMOT', 'pid_graph:95560D23'),
        ('DASDAS', 'pid_graph:95C6D0D7'),
        ('G056-C1.1', 'pid_graph:88398F6D'),
        ('G056-C2.1', 'pid_graph:88398F6D'),
        ('G056-C2.2', 'pid_graph:88398F6D'),
        ('G056-C2.3', 'pid_graph:88398F6D'),
        ('G056-C3.1', 'pid_graph:88398F6D'),
        ('G056-C4.1', 'pid_graph:88398F6D'),
        ('G056-C4.2', 'pid_graph:88398F6D'),
        ('G056-C5.1', 'pid_graph:88398F6D'),
        ('G056-C6.1', 'pid_graph:88398F6D'),
        ('G056-C7.1', 'pid_graph:88398F6D'),
        ('G056-C8.1', 'pid_graph:88398F6D'),
        ('G069-C1', 'pid_graph:95C6D0D7'),
        ('G069-C2', 'pid_graph:95C6D0D7'),
        ('MD-1', 'pid_graph:5EB0885A'),
        ('NET-1 CRITERION', 'pid_graph:5EB0885A'),
        ('NETWORK-CONF-CRI', 'pid_graph:3EDCB21F'),
        ('NEWCRITEST', 'pid_graph:C08780DD'),
        ('SITE-1', 'pid_graph:5EB0885A'),
        ('TEST', 'pid_graph:8F59FADD'),
        ('TESTCR', 'pid_graph:1216B340')
    ) AS tmp(cri, lodmtv)
    JOIN t_motivation m ON m.lodMTV = tmp.lodMTV
    WHERE p_criterion.cri = tmp.cri;

    RAISE NOTICE 'lodMTV values updated successfully.';

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Failed to populate lodMTV field in p_criterion: %', SQLERRM;
END;
$$;

-- Run the procedure
CALL PopulateLodMTVOnCriterion();