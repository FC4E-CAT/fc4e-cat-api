-- ------------------------------------------------
-- Version: v1.149
--
-- Description: Migration that updates the url of the existing tables related to the vocabulary service
-- -------------------------------------------------

UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/e5b07c01-3a86-4ceb-b59b-1df91f1be391' where lodActor= 'pid_graph:0E00C332';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/921a4876-d61b-4699-92d2-3c562333a934' where lodActor= 'pid_graph:1A718108';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/b560cad3-4875-453a-822a-07686039a118' where lodActor= 'pid_graph:20A7A125';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/ce148a85-ee85-4c88-8e6c-34b9f281dd61' where lodActor= 'pid_graph:234B60D8';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/88efe9f8-6f08-4b31-b6b6-2324d1d9d604' where lodActor= 'pid_graph:566C01F6';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/0158d2b1-0ad8-45af-a94b-592c0d16bf64' where lodActor= 'pid_graph:7835EF43';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/54569135-6729-4e18-8eb3-a2217be7e62f' where lodActor= 'pid_graph:B5CC396B';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/f7f844ad-ecd5-4a51-926e-4760ad6e9580' where lodActor= 'pid_graph:D42428D7';
UPDATE  t_Actor set uriActor= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/9c735525-960e-4f13-a74d-4eb23ea9c308/concept/88efe9f8-6f08-4b31-b6b6-2324d1d9d604' where lodActor= 'pid_graph:E92B9B49';

UPDATE  t_Type_Motivation set  urlMotivationType= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/55bdb433-3fa9-44b1-871e-a99f24bbd647' where lodTMT='pid_graph:5AF642D8';
UPDATE  t_Type_Motivation set  urlMotivationType= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/8dd8890d-3e29-45c6-ac13-52a858b32c90' where lodTMT='pid_graph:5EB0883B';
UPDATE  t_Type_Motivation set  urlMotivationType= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/a81169e9-7e52-4fb4-ad0f-7096d70a169c' where lodTMT='pid_graph:8733CC95';
UPDATE  t_Type_Motivation set  urlMotivationType= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/8dfdf546-8327-4fae-98eb-89480451e732' where lodTMT='pid_graph:8882700E';
UPDATE  t_Type_Motivation set  urlMotivationType= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/e69a9f20-3726-422d-9c8a-0078c56d64d0' where lodTMT='pid_graph:AD9D854B';
UPDATE  t_Type_Motivation set  urlMotivationType= 'https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/a9ceb5a3-15ca-4e95-908f-36cab72014a2/concept/e6559d74-5434-4888-b629-f2a0ffff4601' where lodTMT='pid_graph:DFE640B9';


UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/727ac779-e0d5-451c-ad69-975337bc17ac' where lodTMT='pid_graph:03615660';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/7efe56be-96f4-4707-873c-47aa8bbe519b' where lodTMT='pid_graph:191014CA';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/a1f53889-4a3d-46f7-852d-8929e41b5591' where lodTMT='pid_graph:35966E2B';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/1078e3c9-2c9a-4589-b5b9-512f0950c8ab' where lodTMT='pid_graph:8720C485';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/2f9d84d8-3170-42ef-ab1a-986eb5f2f900' where lodTMT='pid_graph:8C8B207B';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/4946e842-1db6-4cb6-910a-b61ed8905528' where lodTMT='pid_graph:C62DD0BB';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/1dd3b7d7-24e2-4e27-9a63-f0974f43279b' where lodTMT='pid_graph:DFAB1578';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/182172d5-5cb4-4e68-8a0c-2148af706dbe' where lodTMT='pid_graph:8D79984F';
UPDATE  t_Type_Metric set uriTypeMetric ='https://mscr-vocabularies-test.2.rahtiapp.fi/vocabularies/terminology/e30f5d62-9b60-49c0-bf4f-d5e58eb2b1e1/concept/7265c2e4-2532-4e8d-b522-3239e57d7804' where lodTMT='pid_graph:8C72C972';


