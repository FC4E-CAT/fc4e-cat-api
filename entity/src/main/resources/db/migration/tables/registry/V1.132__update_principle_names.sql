UPDATE p_Principle set PRI='MD' where lodPRI='pid_graph:B3341612';
UPDATE  p_Principle set PRI='AMR' where lodPRI='pid_graph:A1141612';
UPDATE p_Principle  set PRI='SITE' where lodPRI='pid_graph:5PR0882I';

UPDATE t_Motivation set decMotivation='These guidelines describe the minimum requirements and recommendations for the secure operation of attribute authorities and similar services that make statements about an entity based on well-defined attributes. Adherence to these guidelines may help to establish trust between communities, operators of attribute authorities and issuers, and Relying Parties, infrastructures, and service providers. This document does not define an accreditation process.' where lodMTV='pid_graph:5EB0885A';

UPDATE p_Criterion set CRI='AMR-1' where  lodCRI= 'pid_graph:123D4203';
UPDATE p_Criterion set CRI='AMR-2' where  lodCRI= 'pid_graph:153D4203';
UPDATE p_Criterion set CRI='SITE-1' where lodCRI= 'pid_graph:5CR0881I';


UPDATE p_Test set TES='AMR-1.1' where lodTES ='pid_graph:121489E8';
UPDATE p_Test set TES='AMR-1.2' where lodTES ='pid_graph:123489E1';
UPDATE p_Test set TES='AMR-2.1' where lodTES = 'pid_graph:76489E11';

UPDATE p_Test set TES='SITE-1' where lodTES ='pid_graph:3A4592E8';

UPDATE p_Principle set descPrinciple='For example this can be based on contractual agreements, available audits of the site security controls of a provider, or on actual control and inspections, e.g. the system is in a locked room with auditable physical access controls, protected against intrusions and be at least tamper-evident in case of such intrusions. Additional elements such as flood and fire protection, or protection against threats emerging as a result of providing service to specific communities, should be considered as well.' where lodPRI='pid_graph:5PR0882I';