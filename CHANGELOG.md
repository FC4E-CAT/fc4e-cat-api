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

- [#269](https://github.com/FC4E-CAT/fc4e-cat-api/pull/269) CAT-476 Motivations and actors (motivation-actor relation).
- [#273](https://github.com/FC4E-CAT/fc4e-cat-api/pull/273) CAT-498 GET endpoints for motivation types.
- [#274](https://github.com/FC4E-CAT/fc4e-cat-api/pull/274) CAT-500 API: Get list of relations (codelist).
- [#278](https://github.com/FC4E-CAT/fc4e-cat-api/pull/278) CAT-479:Criteria & actors.
- [#283](https://github.com/FC4E-CAT/fc4e-cat-api/pull/283) CAT-504 Create API Endpoint to Assign Principles to a Motivation.
- [#284](https://github.com/FC4E-CAT/fc4e-cat-api/pull/284) CAT-508 Filters on Motivation.
- [#287](https://github.com/FC4E-CAT/fc4e-cat-api/pull/287) CAT-512 CAT-515 GET list of Criteria by Motivation, Pagination issue in Motivations.
- [#291](https://github.com/FC4E-CAT/fc4e-cat-api/pull/291) CAT-519 API provide a call to update the criteria of a motivation actor (assessment type).
### Fix

- [#275](https://github.com/FC4E-CAT/fc4e-cat-api/pull/275) CAT-501 Unwrap description column values that are wrapped with double quotes in the database.
- [#282](https://github.com/FC4E-CAT/fc4e-cat-api/pull/282) CAT-506 lodMTV missing column from t_Actor.
- [#286](https://github.com/FC4E-CAT/fc4e-cat-api/pull/286) CAT-509 Weird Principles local pagination issue when page size=5.
- [#288](https://github.com/FC4E-CAT/fc4e-cat-api/pull/288) CAT-518 Pagination issues in Motivation > Actor > Criteria.
- [#289](https://github.com/FC4E-CAT/fc4e-cat-api/pull/289) CAT-516 Criteria items under motivation are represented with a bit different schema under two different GET calls - Missing criterion id


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
- [#213](https://github.com/FC4E-CAT/fc4e-cat-api/pull/213) CAT-418 Fix Service Provider template to support thresholds in tests
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

- Assign validated role to user upon auto-approve validation

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
