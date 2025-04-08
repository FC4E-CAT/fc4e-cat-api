# Changelog

---

All notable changes to this project will be documented in this file.

According to [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) , the `Unreleased` section serves the following purposes:

-   People can see what changes they might expect in upcoming releases.
-   At release time, you can move the `Unreleased` section changes into a new release version section.

## Types of changes

---

-   `Added` for new features.
-   `Changed` for changes in existing functionality.
-   `Removed` for now removed features.
-   `Fixed` for any bug fixes.
-   `Security` in case of vulnerabilities.
-   `Deprecated` for soon-to-be removed features.

## Unreleased
---

### Added
- [#475](https://github.com/FC4E-CAT/fc4e-cat-api/pull/475) CAT-859 Dynamically Update Tests When Fetching stored Assessment.


## 2.0.0 - 2025-03-31
---

### Added
- [#465](https://github.com/FC4E-CAT/fc4e-cat-api/pull/465) CAT-822 Assessment type view and fill put order in criteria.
- [#467](https://github.com/FC4E-CAT/fc4e-cat-api/pull/467) CAT-832 Add 127.0.0.1 to Keycloak Public Client to Accept Requests.
- [#468](https://github.com/FC4E-CAT/fc4e-cat-api/pull/468) CAT-827 CAT-829 CAT-828 Publish Assessment in Zenodo / Store zenodo info in database/ Notifications to creator.
- [#469](https://github.com/FC4E-CAT/fc4e-cat-api/pull/469) CAT-834 Publish deposit to Zenodo
- [#470](https://github.com/FC4E-CAT/fc4e-cat-api/pull/470) CAT-836 Get deposit info from Zenodo 
- [#471](https://github.com/FC4E-CAT/fc4e-cat-api/pull/471) CAT-830 Configure zenodo feature in cat

### Fixed 
- [#472](https://github.com/FC4E-CAT/fc4e-cat-api/pull/472) CAT-856 Fix keycloak CORS policy towards 127.0.0.1


## 1.9.2 - 2025-02-26
---

### Fix
- Fix POM version number

## 1.9.1 - 2025-02-25
---
#### Added

### Fix
- [#458](https://github.com/FC4E-CAT/fc4e-cat-api/pull/458) CAT-825 Missing label_algorithm , label_type_metric from assessment doc.


## 1.9.0 - 2025-02-17
---

### Added

- [#330](https://github.com/FC4E-CAT/fc4e-cat-api/pull/330) CAT-432 Add demo data to local installation.
- [#334](https://github.com/FC4E-CAT/fc4e-cat-api/pull/334) CAT-593 Add registry_actor_id Column and Perform Update in Validation Table.
- [#336](https://github.com/FC4E-CAT/fc4e-cat-api/pull/336) CAT-595 Delete Actor from Motivation.
- [#338](https://github.com/FC4E-CAT/fc4e-cat-api/pull/338) CAT-599 Add published as status in a Motivation.
- [#339](https://github.com/FC4E-CAT/fc4e-cat-api/pull/339) CAT-600 Permit actions depending on Motivation publish status.
- [#340](https://github.com/FC4E-CAT/fc4e-cat-api/pull/340) CAT-603 Unpublish a published motivation.
- [#343](https://github.com/FC4E-CAT/fc4e-cat-api/pull/343) CAT-605 Unpublish a published motivation.
- [#345](https://github.com/FC4E-CAT/fc4e-cat-api/pull/345) CAT-608 Implement Search and Retrieve Associated Motivations for Principles and Criteria.
- [#346](https://github.com/FC4E-CAT/fc4e-cat-api/pull/346) CAT-613 Publish/Unpublish motivation actor relationship.
- [#350](https://github.com/FC4E-CAT/fc4e-cat-api/pull/350) CAT-612 Include Related Metrics in Criterion Response.
- [#355](https://github.com/FC4E-CAT/fc4e-cat-api/pull/355) CAT-617 API Create a principle under motivation.
- [#351](https://github.com/FC4E-CAT/fc4e-cat-api/pull/351) CAT-616 Create New Version Endpoint for Generating Assessments Based on Motivation Actor Templates.
- [#361](https://github.com/FC4E-CAT/fc4e-cat-api/pull/361) CAT-628 Assessment Update.
- [#360](https://github.com/FC4E-CAT/fc4e-cat-api/pull/360) CAT-622 Create an endpoint to check if a url is a valid https.
- [#396](https://github.com/FC4E-CAT/fc4e-cat-api/pull/396) CAT-680 Create an API endpoint to establish a relationship between a criterion and a metric.
- [#400](https://github.com/FC4E-CAT/fc4e-cat-api/pull/400) CAT-689 Change POST /registry/metrics to insert full metric/metric-definition relation.
- [#401](https://github.com/FC4E-CAT/fc4e-cat-api/pull/401) CAT-693 Create POST and PUT Endpoints for Metric-Test Relationship.
- [#405](https://github.com/FC4E-CAT/fc4e-cat-api/pull/405) CAT-695 Implement GET and PUT /metric - definition by Motivation API Endpoint.
- [#398](https://github.com/FC4E-CAT/fc4e-cat-api/pull/398) CAT-653 Delete & Update Principle in a motivation.
- [#404](https://github.com/FC4E-CAT/fc4e-cat-api/pull/404) CAT-650 Automated Tests for ARCC-G071.
- [#413](https://github.com/FC4E-CAT/fc4e-cat-api/pull/413) CAT-720 Create Endpoint to Update Metric and Metric Definition.
- [#415](https://github.com/FC4E-CAT/fc4e-cat-api/pull/415) CAT-641 Should we keep extra info on automated tests.
- [#418](https://github.com/FC4E-CAT/fc4e-cat-api/pull/418) CAT-727 Publish assessment.
- [#420](https://github.com/FC4E-CAT/fc4e-cat-api/pull/420) CAT-729 Implement Search Functionality for Test Library.
- [#427](https://github.com/FC4E-CAT/fc4e-cat-api/pull/427) Admin view Validations / Order by status with pending on top.
- [#434](https://github.com/FC4E-CAT/fc4e-cat-api/pull/434) CAT-750 Retrieve Associated Motivations for Tests.
- [#437](https://github.com/FC4E-CAT/fc4e-cat-api/pull/434) CAT-759 API - healthcheck.
- [#438](https://github.com/FC4E-CAT/fc4e-cat-api/pull/438) CAT-755 [API] - Make statistics publicly available
- [#445](https://github.com/FC4E-CAT/fc4e-cat-api/pull/445) CAT-804 Library Metric View: Type Algorithm and Type Metric description

### Fix

- [#326](https://github.com/FC4E-CAT/fc4e-cat-api/pull/326) CAT-584 Resolve Unique Constraint Violation When Updating Principle with Existing PRI.
- [#331](https://github.com/FC4E-CAT/fc4e-cat-api/pull/331) CAT-589 Change copy Motivation to insert value on db field lodMTV_P.
- [#329](https://github.com/FC4E-CAT/fc4e-cat-api/pull/329) CAT-573 Jenkins warnings.
- [#332](https://github.com/FC4E-CAT/fc4e-cat-api/pull/332) CAT-590 Fix CriterionActorJunction key to keep Motivation id.
- [#333](https://github.com/FC4E-CAT/fc4e-cat-api/pull/333) CAT-586 Principles are displayed multiple times in motivation/actor/criteria selection.
- [#342](https://github.com/FC4E-CAT/fc4e-cat-api/pull/342) CAT-604 CRI duplicate warning on criterion update with same CRI.
- [#344](https://github.com/FC4E-CAT/fc4e-cat-api/pull/344) CAT-607 Template's view issue when unpublished motivation , during preview motivation.
- [#352](https://github.com/FC4E-CAT/fc4e-cat-api/pull/352) CAT-619 Permit actions on published/unpublished relation motivation-actor.
- [#365](https://github.com/FC4E-CAT/fc4e-cat-api/pull/365) CAT-628 Shared Users.
- [#367](https://github.com/FC4E-CAT/fc4e-cat-api/pull/367) CAT-633 Public Objects.
- [#369](https://github.com/FC4E-CAT/fc4e-cat-api/pull/369) CAT-627 public assessments.
- [#366](https://github.com/FC4E-CAT/fc4e-cat-api/pull/366) CAT-632 Change GET motivation response to show the published status of motivation-actor.
- [#370](https://github.com/FC4E-CAT/fc4e-cat-api/pull/370) CAT-626 Show subjects per assessment.
- [#373](https://github.com/FC4E-CAT/fc4e-cat-api/pull/373) CAT-635 Subject (objects) filters for non-public assessments.
- [#374](https://github.com/FC4E-CAT/fc4e-cat-api/pull/374) CAT-636 Error when filtering public assessment per subject.
- [#375](https://github.com/FC4E-CAT/fc4e-cat-api/pull/375) CAT-638 Change call GET /v1/registry/tests/check-automated-test/https-url definition.
- [#378](https://github.com/FC4E-CAT/fc4e-cat-api/pull/378) CAT-640 Auto-Check-Url-Binary Test value - allow to be string.
- [#376](https://github.com/FC4E-CAT/fc4e-cat-api/pull/376) CAT-643 Strange json response in /v2/assessments/objects.
- [#377](https://github.com/FC4E-CAT/fc4e-cat-api/pull/377) CAT-645 Create AutomatedCheck resource.
- [#381](https://github.com/FC4E-CAT/fc4e-cat-api/pull/381) CAT-644 Fix Test T14 to Value-Based Percentage Test.
- [#383](https://github.com/FC4E-CAT/fc4e-cat-api/pull/383) CAT-652 Principles created under a motivation dont show the motivation item in used_by_motivation field.
- [#384](https://github.com/FC4E-CAT/fc4e-cat-api/pull/384) CAT-662 Missing junit tests for publish/unpublish motivation/motivation-actor to be added.
- [#386](https://github.com/FC4E-CAT/fc4e-cat-api/pull/386) CAT-660 A principle with the identifier '...' already exists should return conflict 409 not 200.
- [#387](https://github.com/FC4E-CAT/fc4e-cat-api/pull/387) CAT-661 Consistency in backend responses.
- [#388](https://github.com/FC4E-CAT/fc4e-cat-api/pull/388) CAT-651 REJECTED Validation email should follow the other emails.
- [#389](https://github.com/FC4E-CAT/fc4e-cat-api/pull/389) CAT-669 Fix 404 Error Response for Assessment Comments API Endpoint.
- [#391](https://github.com/FC4E-CAT/fc4e-cat-api/pull/391) CAT-671 Fix Uniqueness Constraint on MTR field in Metric.
- [#395](https://github.com/FC4E-CAT/fc4e-cat-api/pull/395) CAT-679 Script to delete secondary principles from EOSC PID POLICY motivation.
- [#397](https://github.com/FC4E-CAT/fc4e-cat-api/pull/397) CAT-570 DB: Cannot find user friendly that describe the parameters for tests.
- [#399](https://github.com/FC4E-CAT/fc4e-cat-api/pull/399) CAT-687 Publish/Unpublish motivation not to change motivation-actor publish status.
- [#403](https://github.com/FC4E-CAT/fc4e-cat-api/pull/403) CAT-689 Change POST /registry/metrics to insert full metric/metric-definition relation (fix).
- [#406](https://github.com/FC4E-CAT/fc4e-cat-api/pull/406) CAT-698 Support test-metric relations of a specific metric in a motivation.
- [#407](https://github.com/FC4E-CAT/fc4e-cat-api/pull/407) CAT-697 Update /registry/tests Endpoints to Include All test and test-definition Fields.
- [#409](https://github.com/FC4E-CAT/fc4e-cat-api/pull/409) CAT-699 Cleanup DB: in benchmark type Binary-Binary has a whitespace at the end.
- [#410](https://github.com/FC4E-CAT/fc4e-cat-api/pull/410) CAT-703 Provide MTR id in motivation/{id}/metric-definition call.
- [#411](https://github.com/FC4E-CAT/fc4e-cat-api/pull/411) CAT-702 Pagination info in motivation-metric-tests call.
- [#412](https://github.com/FC4E-CAT/fc4e-cat-api/pull/412) CAT-706 Give the ability to update tests with the same info as CREATE.
- [#413](https://github.com/FC4E-CAT/fc4e-cat-api/pull/413) CAT-618 Change metadata xml validation automated checks 
- [#416](https://github.com/FC4E-CAT/fc4e-cat-api/pull/416) CAT-722 Update /registry/metric Endpoints to Include All metric and metric-definition Fields
- [#419](https://github.com/FC4E-CAT/fc4e-cat-api/pull/419) CAT-726 CAT-725 CAT-724 Quote typos in MD1 related tests - Auto MD1 related tests inform about wrong parameters-Auto MD1 related test return 500 when metadata not valid.
- [#417](https://github.com/FC4E-CAT/fc4e-cat-api/pull/417) CAT-728 Extend Search Functionality for Principles and Criteria
- [#424](https://github.com/FC4E-CAT/fc4e-cat-api/pull/424) CAT-734 Change assessment_doc to include published.
- [#426](https://github.com/FC4E-CAT/fc4e-cat-api/pull/426) CAT-554 API: Motivation Clone Missing MotivationActors in Cloned Item
- [#428](https://github.com/FC4E-CAT/fc4e-cat-api/pull/428) CAT-741 Motivation preview displays wrong json when add/remove elements in motivation
- [#429](https://github.com/FC4E-CAT/fc4e-cat-api/pull/429) CAT-744 Issue when actor- criteria vary betwen 2 motivations which are parent-child(cloned)
- [#431](https://github.com/FC4E-CAT/fc4e-cat-api/pull/431) CAT-745 C7 criterion fails due to wrong benchmark value
- [#439](https://github.com/FC4E-CAT/fc4e-cat-api/pull/439) CAT-749 API call: admin publish assessment gets 403 when admin
- [#443](https://github.com/FC4E-CAT/fc4e-cat-api/pull/443) CAT-792 Retrieve Associated Motivations for Metrics
- [#441](https://github.com/FC4E-CAT/fc4e-cat-api/pull/441) CAT-783 CAT-791 API- GET Public Assessment 400 error/Assessment's id inside assessment_doc is null 
- [#442](https://github.com/FC4E-CAT/fc4e-cat-api/pull/442) CAT-788 Wrong sort values for /registry/metrics
- [#444](https://github.com/FC4E-CAT/fc4e-cat-api/pull/444/) CAT-792 Retrieve Associated Motivations for Metrics (fix)
- [#450](https://github.com/FC4E-CAT/fc4e-cat-api/pull/450) CAT-806 Metrics view displays the same metric multiple times
- [#445](https://github.com/FC4E-CAT/fc4e-cat-api/pull/445/) CAT-800 Cannot change test that belongs to unpublished motivation - still says published #446
- [#449](https://github.com/FC4E-CAT/fc4e-cat-api/pull/449/) CAT-749 API call: admin publish assessment gets 403 when admin -version 2
- [#451](https://github.com/FC4E-CAT/fc4e-cat-api/pull/451) CAT-810 Test Update Not Working Properly
- [#452](https://github.com/FC4E-CAT/fc4e-cat-api/pull/452/) CAT-807 Metrics search doesnt return results

### Removed

- [#335](https://github.com/FC4E-CAT/fc4e-cat-api/pull/335) CAT-592 Delete All Motivations Except Specific Entries. 
- [#435](https://github.com/FC4E-CAT/fc4e-cat-api/pull/435) CAT-766 Update validation actors to PID Service Provider.
- [#436](https://github.com/FC4E-CAT/fc4e-cat-api/pull/436) CAT-767 Delete the PID Service(Component) from CAT.

## 1.8.1 - 2024-10-17
---

### Removed

- [#320](https://github.com/FC4E-CAT/fc4e-cat-api/pull/320) CAT-579 Remove criteria ordering logic based on unique names.

## 1.8.0 - 2024-10-16
---

### Added

- [#262](https://github.com/FC4E-CAT/fc4e-cat-api/pull/262) CAT-485: Metrics.
- [#270](https://github.com/FC4E-CAT/fc4e-cat-api/pull/270) CAT-482 Tests.
- [#271](https://github.com/FC4E-CAT/fc4e-cat-api/pull/271) CAT-483 Metrics & Tests.
- [#269](https://github.com/FC4E-CAT/fc4e-cat-api/pull/269) CAT-476 Motivations and actors (motivation-actor relation).
- [#268](https://github.com/FC4E-CAT/fc4e-cat-api/pull/268) CAT-480 Metrics & Metric Definition.
- [#273](https://github.com/FC4E-CAT/fc4e-cat-api/pull/273) CAT-498 GET endpoints for motivation types.
- [#276](https://github.com/FC4E-CAT/fc4e-cat-api/pull/276) CAT-499 Metric-Test Relations Table
- [#274](https://github.com/FC4E-CAT/fc4e-cat-api/pull/274) CAT-500 API: Get list of relations (codelist).
- [#278](https://github.com/FC4E-CAT/fc4e-cat-api/pull/278) CAT-479 Criteria & actors.
- [#283](https://github.com/FC4E-CAT/fc4e-cat-api/pull/283) CAT-504 Create API Endpoint to Assign Principles to a Motivation.
- [#284](https://github.com/FC4E-CAT/fc4e-cat-api/pull/284) CAT-508 Filters on Motivation.
- [#287](https://github.com/FC4E-CAT/fc4e-cat-api/pull/287) CAT-512 CAT-515 GET list of Criteria by Motivation, Pagination issue in Motivations.
- [#291](https://github.com/FC4E-CAT/fc4e-cat-api/pull/291) CAT-519 API provide a call to update the criteria of a motivation actor (assessment type).
- [#297](https://github.com/FC4E-CAT/fc4e-cat-api/pull/297) CAT-527 Transform Database Relationships into JSON for Actor Template.
- [#285](https://github.com/FC4E-CAT/fc4e-cat-api/pull/285) CAT-507 Implement Search Filtering Across Metric-Related Entities.
- [#290](https://github.com/FC4E-CAT/fc4e-cat-api/pull/290) CAT-511 Create API Enpoints to List and Copy Relations for a Motivation.
- [#293](https://github.com/FC4E-CAT/fc4e-cat-api/pull/293) CAT-537 Implement GET /principles by Motivation API Endpoint.
- [#295](https://github.com/FC4E-CAT/fc4e-cat-api/pull/295) CAT-543 Implement Email Notification for User on Shared Assessment.
- [#302](https://github.com/FC4E-CAT/fc4e-cat-api/pull/302) CAT-550 Add/Update Relationship: Principle-Criterion-Motivation in Database and Generate POST/PUT Endpoints.
- [#304](https://github.com/FC4E-CAT/fc4e-cat-api/pull/304) CAT-558 Add Sorting, Ordering, and Search to GET motivations/{id}/principles Endpoint.
- [#305](https://github.com/FC4E-CAT/fc4e-cat-api/pull/305) CAT-563 Returns the Metrics and associated Metric Tests of a Criterion.
- [#306](https://github.com/FC4E-CAT/fc4e-cat-api/pull/306) CAT-560 Motivation Name and Actor Name in Motivation/Criteria api call.


### Fix

- [#275](https://github.com/FC4E-CAT/fc4e-cat-api/pull/275) CAT-501 Unwrap description column values that are wrapped with double quotes in the database.
- [#282](https://github.com/FC4E-CAT/fc4e-cat-api/pull/282) CAT-506 lodMTV missing column from t_Actor.
- [#286](https://github.com/FC4E-CAT/fc4e-cat-api/pull/286) CAT-509 Weird Principles local pagination issue when page size=5.
- [#288](https://github.com/FC4E-CAT/fc4e-cat-api/pull/288) CAT-518 Pagination issues in Motivation > Actor > Criteria.
- [#289](https://github.com/FC4E-CAT/fc4e-cat-api/pull/289) CAT-516 Criteria items under motivation are represented with a bit different schema under two different GET calls - Missing criterion id.
- [#292](https://github.com/FC4E-CAT/fc4e-cat-api/pull/292) CAT-538 Regression: Get /registry/motivation/{motivation_id}/criteria missing
- [#296](https://github.com/FC4E-CAT/fc4e-cat-api/pull/296) CAT-541 When we try to update MotivationActor Criteria with empty list we get error.
- [#298](https://github.com/FC4E-CAT/fc4e-cat-api/pull/298) CAT-539 Updating Criteria for specific MotivationActor items fails with 500.
- [#299](https://github.com/FC4E-CAT/fc4e-cat-api/pull/299) CAT-548 API: Updating MotivationActor with existing criteria (but changed imperatives) doesn't proceed.
- [#300](https://github.com/FC4E-CAT/fc4e-cat-api/pull/300) CAT-547 API: Updating Criteria for specific MotivationActor item doesn't change the imperative.
- [#314](https://github.com/FC4E-CAT/fc4e-cat-api/pull/314) CAT-576 API: Pagination issue in Motivation Principles call.
- [#315](https://github.com/FC4E-CAT/fc4e-cat-api/pull/315) CAT-577: Duplicate Principle Entries in Response on GET /motivation/{id}/criteria.


### Changed

- [#307](https://github.com/FC4E-CAT/fc4e-cat-api/pull/307) CAT-565 Template Response to Use Object Metric Instead of Array of Metrics.




## 1.7.0 - 2024-09-10

---
### Added

- [#222](https://github.com/FC4E-CAT/fc4e-cat-api/pull/222) CAT-425 Register a New Assessment Type in CAT Database.
- [#223](https://github.com/FC4E-CAT/fc4e-cat-api/pull/223) CAT-426 Register a new template for PID Manager for PID NL National Policy.
- [#225](https://github.com/FC4E-CAT/fc4e-cat-api/pull/225) CAT-429 Update Manager Template for National PID policy.
- [#228](https://github.com/FC4E-CAT/fc4e-cat-api/pull/228) CAT-439 Enable Sharing of Assessments Among Users via Keycloak Attributes.
- [#230](https://github.com/FC4E-CAT/fc4e-cat-api/pull/230) CAT-441 Guidance in db.
- [#231](https://github.com/FC4E-CAT/fc4e-cat-api/pull/231) CAT-443 CRUD actions on Principles.
- [#232](https://github.com/FC4E-CAT/fc4e-cat-api/pull/232) CAT-444 Create Endpoint for Sharing Assessments Among Users.
- [#235](https://github.com/FC4E-CAT/fc4e-cat-api/pull/235) CAT-442 Create CRUD Operations for Criteria under Admin User.
- [#236](https://github.com/FC4E-CAT/fc4e-cat-api/pull/236) CAT-386 wrong question -update text in templates.
- [#239](https://github.com/FC4E-CAT/fc4e-cat-api/pull/239) CAT-370 Admin - View User.
- [#241](https://github.com/FC4E-CAT/fc4e-cat-api/pull/241) CAT-448 Reason for rejection of a validation.
- [#242](https://github.com/FC4E-CAT/fc4e-cat-api/pull/242) CAT-450 Input type in the evidence.
- [#243](https://github.com/FC4E-CAT/fc4e-cat-api/pull/243) CAT-460 Share an assessment.
- [#244](https://github.com/FC4E-CAT/fc4e-cat-api/pull/244) CAT-458 As a user i want to know to whom i shared an assessment.
- [#245](https://github.com/FC4E-CAT/fc4e-cat-api/pull/245) CAT-452 Comments in an assessment.
- [#250](https://github.com/FC4E-CAT/fc4e-cat-api/pull/250) CAT-465 Add more user info on each comment.
- [#252](https://github.com/FC4E-CAT/fc4e-cat-api/pull/252) CAT-459 As a user i want to see to whom i shared an assessment.
- [#257](https://github.com/FC4E-CAT/fc4e-cat-api/pull/257) CAT-475 Motivations.
- [#258](https://github.com/FC4E-CAT/fc4e-cat-api/pull/258) CAT-484 Codelist - Tables sync with Vocabulary service (CRUD Type Criterion).
- [#259](https://github.com/FC4E-CAT/fc4e-cat-api/pull/259) CAT-484 Codelist - Tables sync with Vocabulary service (GET Imperative).
- [#260](https://github.com/FC4E-CAT/fc4e-cat-api/pull/260) CAT-477 Motivations & Principles.
- [#261](https://github.com/FC4E-CAT/fc4e-cat-api/pull/261) CAT-484 Codelist - Tables sync with Vocabulary service (GET Benchmark Type).
- [#263](https://github.com/FC4E-CAT/fc4e-cat-api/pull/263) CAT-476 Motivations and actors - (GET Actor).
- [#265](https://github.com/FC4E-CAT/fc4e-cat-api/pull/265) CAT-481 Criterion & Metrics.
- [#276](https://github.com/FC4E-CAT/fc4e-cat-api/pull/276) CAT-499 Metric-Test Relations Table

### Changed

- [#249](https://github.com/FC4E-CAT/fc4e-cat-api/pull/249) CAT-464 Issue when updating the user details in profile.


## 1.6.0 - 2024-07-08

---
### Added

- [#218](https://github.com/FC4E-CAT/fc4e-cat-api/pull/218) CAT-421 Create a new endpoint allowing an admin to update an existing assessment.
- [#219](https://github.com/FC4E-CAT/fc4e-cat-api/pull/219) CAT-422 Create endpoint to retrieve all generated assessments.


## 1.5.2 - 2024-07-05

---

## Changed
- [#212](https://github.com/FC4E-CAT/fc4e-cat-api/pull/212) CAT-402 Duplicate test in assessment template.
- [#213](https://github.com/FC4E-CAT/fc4e-cat-api/pull/213) CAT-418 Fix Service Provider template to support thresholds in tests.
- [#214](https://github.com/FC4E-CAT/fc4e-cat-api/pull/214) CAT-419 Flag thresholds as locked when having a required default value.



## 1.5.1 - 2024-06-18

---

### Changed
- [#208](https://github.com/FC4E-CAT/fc4e-cat-api/pull/208) CAT-395 Public assessment not updated.
- [#209](https://github.com/FC4E-CAT/fc4e-cat-api/pull/209) CAT-398 Update PID Service Provider template to remove default thresholds.

## 1.5.0 - 2024-06-17

---

### Added

- [#206](https://github.com/FC4E-CAT/fc4e-cat-api/pull/206) Update the JSON schema, which validates new assessments.


## 1.4.0 - 2024-06-10

---

### Changed

- [#198](https://github.com/FC4E-CAT/fc4e-cat-api/pull/198) CAT-376 Swagger correct example.
- [#200](https://github.com/FC4E-CAT/fc4e-cat-api/pull/200) CAT-384 Enable Utf-8 character set in cat db.


## 1.3.0 - 2024-04-29

---

### Added

- [#188](https://github.com/FC4E-CAT/fc4e-cat-api/pull/188) CAT-350 Search Users as an admin.
- [#189](https://github.com/FC4E-CAT/fc4e-cat-api/pull/189) CAT-351 Search Validations as an admin.
- [#190](https://github.com/FC4E-CAT/fc4e-cat-api/pull/190) CAT-362 Wrong Results return when searching Users combining actor value and search box value.
- [#190](https://github.com/FC4E-CAT/fc4e-cat-api/pull/190) CAT-363 Handle REJECT status in search of validations.

### Changed

- [#175](https://github.com/FC4E-CAT/fc4e-cat-api/pull/175) CAT-331 Naming convention to Keycloak.
- [#187](https://github.com/FC4E-CAT/fc4e-cat-api/pull/187) CAT-340 Update Environment Variable Naming Convention for CAT API.
- [#196](https://github.com/FC4E-CAT/fc4e-cat-api/pull/187) CAT-378 Change Subject Fields length.


## 1.2.5 - 2024-02-19

---

### Changed

- Update Manager template to include best practices in guidelines.


## 1.2.4 - 2024-02-16

---

### Added

- [#139](https://github.com/FC4E-CAT/fc4e-cat-api/pull/139) - CAT-320 Implement Custom Organization Source Feature.

## 1.2.3 - 2024-02-13

---

### Added

- [#136](https://github.com/FC4E-CAT/fc4e-cat-api/pull/136) - Fill in Validation with User Details.

## 1.2.2 - 2024-02-12

---

### Fixed

- Assign validated role to user upon auto-approve validation.

## 1.2.1 - 2024-02-12

---

### Added

- [#130](https://github.com/FC4E-CAT/fc4e-cat-api/pull/130) - CAT-299 Enable automatic approval of validation requests on cat-api demo.

## 1.2.0 - 2024-01-23

---

### Added

-   [#76](https://github.com/FC4E-CAT/fc4e-cat-api/pull/76)    -  CAT-178 Delete private assessment.
-   [#81](https://github.com/FC4E-CAT/fc4e-cat-api/pull/81)    -  CAT-183 Generate JSON Schema for PID Owner Template.
-   [#81](https://github.com/FC4E-CAT/fc4e-cat-api/pull/81)    -  CAT-183 Generate JSON Schema for PID Owner Template.
-   [#92](https://github.com/FC4E-CAT/fc4e-cat-api/pull/92)    -  CAT-210 List of Objects.
-   [#104](https://github.com/FC4E-CAT/fc4e-cat-api/pull/104)  -  CAT-261 Create a new Subject.
-   [#105](https://github.com/FC4E-CAT/fc4e-cat-api/pull/105)  -  CAT-263 Delete a Subject.
-   [#106](https://github.com/FC4E-CAT/fc4e-cat-api/pull/106)  -  CAT-274 Fetch the created Subjects.
-   [#107](https://github.com/FC4E-CAT/fc4e-cat-api/pull/107)  -  CAT-262 Update a Subject.
-   [#109](https://github.com/FC4E-CAT/fc4e-cat-api/pull/109)  -  CAT-276 Implement Flexible Logic for Saving Assessments in the Database.
-   [#112](https://github.com/FC4E-CAT/fc4e-cat-api/pull/112)  -  CAT-264 Update api calls to objects used for filters.
-   [#113](https://github.com/FC4E-CAT/fc4e-cat-api/pull/113)  -  CAT-279 API Endpoint for assigning deny_access role.
-   [#114](https://github.com/FC4E-CAT/fc4e-cat-api/pull/114)  -  CAT-280 API Endpoint for removing deny_access role.
-   [#116](https://github.com/FC4E-CAT/fc4e-cat-api/pull/116)  -  CAT-289 As an admin i want to see some statistics in CAT
-   [#121](https://github.com/FC4E-CAT/fc4e-cat-api/pull/121)  -  CAT-295 Assign identified role to user during registration through Keycloak API.
### Changed

-   [#73](https://github.com/FC4E-CAT/fc4e-cat-api/pull/73)    -  CAT-159 Change assessment id from bigint to uuid.
-   [#79](https://github.com/FC4E-CAT/fc4e-cat-api/pull/79)    -  CAT-169 Create a template for the actor: Scheme.
-   [#80](https://github.com/FC4E-CAT/fc4e-cat-api/pull/80)    -  CAT-167 Create a template for the actor: Manager.
-   [#81](https://github.com/FC4E-CAT/fc4e-cat-api/pull/81)    -  CAT-181 Generate JSON Schema for PID Owner Template.
-   [#82](https://github.com/FC4E-CAT/fc4e-cat-api/pull/82)    -  fix json of scheme template.
-   [#83](https://github.com/FC4E-CAT/fc4e-cat-api/pull/83)    -  CAT-168 Create a template for the actor: Service.
-   [#95](https://github.com/FC4E-CAT/fc4e-cat-api/pull/95)    -  CAT-218 API move /users list under /admin path.
-   [#96](https://github.com/FC4E-CAT/fc4e-cat-api/pull/96)    -  CAT-220 Remove unnecessary properties from the assessment creation request.
-   [#99](https://github.com/FC4E-CAT/fc4e-cat-api/pull/99)    -  CAT-231 Sort the validation requests by creation date.
-   [#100](https://github.com/FC4E-CAT/fc4e-cat-api/pull/100)  -  CAT-229 Required fields on assessment metadata.
-   [#108](https://github.com/FC4E-CAT/fc4e-cat-api/pull/108)  -  CAT-275 Refactor the template json to handle subject db_id property .


## 1.1.0 - 2023-09-01

---

### Added

-   [#46](https://github.com/FC4E-CAT/fc4e-cat-api/pull/46)    -  Create API endpoint to serve assessment template json by type for actor.
-   [#48](https://github.com/FC4E-CAT/fc4e-cat-api/pull/48)    -  Create Endpoint for Admin Users to Create Assessment Templates.
-   [#49](https://github.com/FC4E-CAT/fc4e-cat-api/pull/49)    -  Create API endpoint to allow user to store a new assessment.
-   [#52](https://github.com/FC4E-CAT/fc4e-cat-api/pull/52)    -  Create API endpoint to view an assessment.
-   [#53](https://github.com/FC4E-CAT/fc4e-cat-api/pull/53)    -  Create an endpoint that retrieves the available assessment types.
-   [#50](https://github.com/FC4E-CAT/fc4e-cat-api/pull/50)    -  CAT-90 Create API endpoint to allow user to update an existing assessment (PUT).
-   [#54](https://github.com/FC4E-CAT/fc4e-cat-api/pull/54)    -  CAT-111 Create API endpoint to get the list of templates for an actor.
-   [#55](https://github.com/FC4E-CAT/fc4e-cat-api/pull/55)    -  CAT-91 Create an API endpoint that allows to view list of assessments.
-   [#59](https://github.com/FC4E-CAT/fc4e-cat-api/pull/59)    -  CAT-146 Add a new field named published (true or false) to Assessment JSON Template.
-   [#62](https://github.com/FC4E-CAT/fc4e-cat-api/pull/62)    -  CAT-156 Add imperative = should in owner.template.
-   [#64](https://github.com/FC4E-CAT/fc4e-cat-api/pull/64)    -  CAT-158 When an assessment is created, update it's id field in JSON.
-   [#67](https://github.com/FC4E-CAT/fc4e-cat-api/pull/67)    -  CAT-165 Create template for actor: PID Authority.

### Changed

-   [#51](https://github.com/FC4E-CAT/fc4e-cat-api/pull/51)    -  CAT-75 Investigate to enrich ROR search organisation on acronym.
-   [#56](https://github.com/FC4E-CAT/fc4e-cat-api/pull/56)    -  CAT-122 Update "status" column data type from varchar to int in the Validation table for improved table ordering.
-   [#57](https://github.com/FC4E-CAT/fc4e-cat-api/pull/57)    -  CAT-120 Global way to search for ror orgs.
-   [#60](https://github.com/FC4E-CAT/fc4e-cat-api/pull/60)    -  CAT-151 Update JSON Template in Database to Include Actor ID.
-   [#63](https://github.com/FC4E-CAT/fc4e-cat-api/pull/63)    -  CAT-157 Support non-filled values for values/results in tests and metrics of template.
-   [#66](https://github.com/FC4E-CAT/fc4e-cat-api/pull/66)    -  CAT-163 Small update in PID owner template - add an extra field.
-   [#68](https://github.com/FC4E-CAT/fc4e-cat-api/pull/68)    -  CAT-160 Partial Assessment Information.


## 1.0.0 - 2023-07-18

---

### Added

-   [#18](https://github.com/FC4E-CAT/fc4e-cat-api/pull/18)    - Multi-Module Structure and User Registration.
-   [#19](https://github.com/FC4E-CAT/fc4e-cat-api/pull/19)    - Add User Profile Information.
-   [#21](https://github.com/FC4E-CAT/fc4e-cat-api/pull/21)    - Add User List Endpoint.
-   [#22](https://github.com/FC4E-CAT/fc4e-cat-api/pull/22)    - Update User Profile Metadata Endpoint.
-   [#23](https://github.com/FC4E-CAT/fc4e-cat-api/pull/23)    - List of organisation integration sources.
-   [#24](https://github.com/FC4E-CAT/fc4e-cat-api/pull/24)    - User Validation Endpoint.
-   [#25](https://github.com/FC4E-CAT/fc4e-cat-api/pull/25)    - Retrieve the promotion requests submitted by a specific user.
-   [#26](https://github.com/FC4E-CAT/fc4e-cat-api/pull/26)    - Retrieve Organisation by id.
-   [#28](https://github.com/FC4E-CAT/fc4e-cat-api/pull/28)    - Implement Admin API endpoint for managing Validation Requests.
-   [#29](https://github.com/FC4E-CAT/fc4e-cat-api/pull/29)    - Enable CORS in API.
-   [#30](https://github.com/FC4E-CAT/fc4e-cat-api/pull/30)    - Enable Keycloak Policy Enforcer.
-   [#35](https://github.com/FC4E-CAT/fc4e-cat-api/pull/35)    - Implement API communication with Keycloak to change user role upon validation approval.
-   [#36](https://github.com/FC4E-CAT/fc4e-cat-api/pull/36)    - Implement API Endpoints for Validation Requests.
-   [#38](https://github.com/FC4E-CAT/fc4e-cat-api/pull/38)    - Add extended user and actor info on validation response.
-   [#39](https://github.com/FC4E-CAT/fc4e-cat-api/pull/39)    - Implement API Endpoint for Assigning User Roles.
-   [#41](https://github.com/FC4E-CAT/fc4e-cat-api/pull/41)    - Update Validation endpoint to return requests based on status.
-   [#42](https://github.com/FC4E-CAT/fc4e-cat-api/pull/42)    - Enhance User entity with orcid id.
