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

### Added

-   [#46](https://github.com/FC4E-CAT/fc4e-cat-api/pull/46)    -  Create API endpoint to serve assessment template json by type for actor.
-   [#48](https://github.com/FC4E-CAT/fc4e-cat-api/pull/48)    -  Create Endpoint for Admin Users to Create Assessment Templates.
-   [#49](https://github.com/FC4E-CAT/fc4e-cat-api/pull/49)    -  Create API endpoint to allow user to store a new assessment.
-   [#52](https://github.com/FC4E-CAT/fc4e-cat-api/pull/52)    -  Create API endpoint to view an assessment.
-   [#53](https://github.com/FC4E-CAT/fc4e-cat-api/pull/53)    -  Create an endpoint that retrieves the available assessment types.
-   [#50](https://github.com/FC4E-CAT/fc4e-cat-api/pull/50)    -  CAT-90 Create API endpoint to allow user to update an existing assessment (PUT).
-   [#51](https://github.com/FC4E-CAT/fc4e-cat-api/pull/51)    -  CAT-75 Investigate to enrich ROR search organisation on acronym.
-   [#54](https://github.com/FC4E-CAT/fc4e-cat-api/pull/54)    -  CAT-111 Create API endpoint to get the list of templates for an actor.
-   [#55](https://github.com/FC4E-CAT/fc4e-cat-api/pull/55)    -  CAT-91 Create an API endpoint that allows to view list of assessments.
-   [#56](https://github.com/FC4E-CAT/fc4e-cat-api/pull/56)    -  CAT-122 Update "status" column data type from varchar to int in the Validation table for improved table ordering.
-   [#57](https://github.com/FC4E-CAT/fc4e-cat-api/pull/57)    -  CAT-120 Global way to search for ror orgs.
-   [#59](https://github.com/FC4E-CAT/fc4e-cat-api/pull/59)    -  CAT-146 Add a new field named published (true or false) to Assessment JSON Template.
-   [#60](https://github.com/FC4E-CAT/fc4e-cat-api/pull/60)    -  CAT-151 Update JSON Template in Database to Include Actor ID.
-   [#62](https://github.com/FC4E-CAT/fc4e-cat-api/pull/62)    -  CAT-156 Add imperative = should in owner.template.
-   [#63](https://github.com/FC4E-CAT/fc4e-cat-api/pull/63)    -  CAT-157 Support non-filled values for values/results in tests and metrics of template. 
-   [#64](https://github.com/FC4E-CAT/fc4e-cat-api/pull/64)    -  CAT-158 When an assessment is created, update it's id field in JSON.
-   [#66](https://github.com/FC4E-CAT/fc4e-cat-api/pull/66)    -  CAT-163 Small update in PID owner template - add an extra field.
-   [#67](https://github.com/FC4E-CAT/fc4e-cat-api/pull/67)    -  CAT-165 Create template for actor: PID Authority.
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
