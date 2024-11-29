UPDATE p_Test_Definition set testParams='commPol', testQuestion='Has the Community defined and documented the semantics, lifecycle, data protection, and release policy of attributes stored or asserted by the AA?' , toolTip='Community documents  for semantics, lifecycle, data protection, and release policy' where lodTDF='pid_graph:4A1F1EA5';

UPDATE p_Test_Definition set testParams='commDocs|evidence', testQuestion='Has the AA Operator collected and published the community documents for the benefit of Relying Parties?|Provide evidence of that via a URL to a page or to a documentation.' , toolTip='Community documents are available for the Relying Parties|A document, web page, or publication describing provisions' where lodTDF='pid_graph:5D1F1EA5';

UPDATE p_Test set labelTest='Attribute Documentation' where lodTES='pid_graph:121489E8';
UPDATE p_Test set labelTest='Implementation of Community Definitions' where lodTES='pid_graph:123489E1';
UPDATE p_Test set labelTest='Collection and Publication of Community Documents' where lodTES='pid_graph:76489E11';