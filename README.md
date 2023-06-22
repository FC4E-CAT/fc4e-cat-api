# FAIRCORE4EOSC - CAT: Compliance Assessment Toolkit
The FAIRCORE4EOSC project focuses on the development and realisation of core components for the European Open Science Cloud (EOSC). Supporting a FAIR EOSC and addressing gaps identified in the Strategic Research and Innovation Agenda (SRIA). Leveraging existing technologies and services, the project will develop nine new EOSC-Core components aimed to improve the discoverability and interoperability of an increased amount of research outputs.

The Compliance Assessment Toolkit will support the EOSC PID policy with services to encode, record, and query compliance with the policy. To do so, a wide range of compliance requirements ( TRUST, FAIR, PID Policy, Reproducibility, GDPR, Licences) will be evaluated as use cases for definition of a conceptual model. At the same time, vocabularies, concepts, and designs are intended to be re-usable for other compliance needs: TRUST, FAIR, POSI, CARE, Data Commons, etc. This will be followed by a supporting service specification (the framework), accompanied  by development and testing of operational services for PID Policy Compliance monitoring.

## FAIRCORE4EOSC - CAT: Compliance Assessment Toolkit - API

This is the repository of the CAT API that allows for the encoding, recording, and querying of compliance assessments. Operational examples will support EOSC PID Policy compliance, while FAIR and TRUST will be available as beta versions.

More Information : FAIRCORE4EOSC Website: https://faircore4eosc.eu/

Participants: GRNET, DANS

## CAT: Compliance Assessment Toolkit - API Structure

This project is a REST API built using Maven. It follows the service-repository pattern and is divided into several Maven modules.

### Modules

The project consists of the following modules:

1) entity

The `entity` module contains the database ORM (Object-Relational Mapping) entities. These entities represent the data model of the application 
and are used for interacting with the database.

2) repository

The `repository` module contains the repositories responsible for communicating with the database. These repositories provide an abstraction layer for accessing and manipulating data in the database. 
They utilize the ORM entities from the `entity` module.

3) service

The `service` module contains the business logic of the application. It provides various services and operations that manipulate the data and implement the core functionality of the API. 
The services in this module utilize the repositories from the `repository` module to interact with the database.

4) api

The `api` module contains the REST endpoints of the API. These endpoints serve as the interface for clients to interact with the application. 
The `api` module depends on the services from the `service` module to handle the requests and process the data.

5) data-transfer-object

The `data-transfer-object` module contains the DTOs (Data Transfer Objects). These DTOs are used to transfer data between the API layer and the clients. 
They provide a convenient way to encapsulate and transport data without exposing the internal structure of the entities.

6) handler

The `handler` module is responsible for handling different events, such as exceptions. 
It provides error handling and other event-related functionalities to ensure robustness and proper execution of the API.

7) exception

The `exception` module contains the project-specific exceptions. These exceptions are used to handle exceptional situations and provide meaningful error messages or behavior. 
The module defines custom exception classes that extend the standard exception classes or implement specific interfaces.

8) mapper

The `mapper` module is responsible for mapping entities with DTOs using MapStruct.

9) enum

The `enum` module contains the available API enumerations.

10) validator

The `validator` module contains the API Validators. They allow us to express and validate the API constraints.

## Access the protected resources

Since the API's endpoints must only be obtainable to verified clients, every client who wants access to the API resources should be authenticated.
The communication with protected API endpoints is performed using the Bearer Authentication. Bearer Authentication is a type of token-based authentication commonly used in HTTP-based APIs.

Once the client receives the access token, they should construct their HTTP request to access the protected API endpoint. They should include the access token in the HTTP request by adding an "Authorization" header to the HTTP request.
The header's value starts with the word "Bearer" followed by a space and the actual access token.

`Authorization: Bearer {{token}}`

### Access Token Retrieval

This [web page](https://api.cat.argo.grnet.gr/) allows users to obtain an access token for authentication purposes. By following these instructions, users can retrieve an access token to authenticate API requests.

### Instructions

1. Open the Web Page

   Open your preferred web browser and navigate to the URL of the [web page](https://api.cat.argo.grnet.gr/) where the access token can be obtained.

2. Locate the Access Token Button

   Once the web page loads, locate the button that triggers the access token retrieval process. The button should be visible on the web page.

3. Click the Obtain an Access Token button

   Click the access token button to initiate the process of retrieving an access token. This will trigger the necessary steps to obtain the token from the authentication server.

4. Provide Required Information
   
   Choose your preferred identity provider from the available options. After selecting the identity provider, enter your credentials.

5. Retrieve the Access Token

   After providing the necessary information, the web page will communicate with the authentication server to retrieve the access token. This process may take a few moments. Once the retrieval is successful, the access token will be displayed on the web page.

6. Use the Access Token

   Once you have obtained the access token, you can use it for authenticating API requests. Follow the documentation or guidelines provided by the API to understand how to include the access token in the appropriate HTTP requests using the Bearer Authentication scheme.

## Instructions for Developers

### Prerequisites

Before you can use this project, make sure you have the following software installed on your development machine:

- Java Development Kit (JDK) 11 or later
- Apache Maven  3.8.4 or later
- Docker

### Install the necessary dependencies

When you clone the project, please switch to the root directory of the project and execute the following command to install the necessary dependencies in your local maven repository:

`mvn clean install -DskipTests=true -U`

### Start the application with Quarkus Dev Services

Please switch to the root directory of the project and execute the following command:

`mvn clean quarkus:dev`

This command will start the application in development mode and automatically launch the necessary services, such as the database and Keycloak server, using Quarkus Dev Services.

### Access the Dev Service Database

To access the database, please execute the following command:

`mysql -h localhost -P 3306 -u cat -p cat --protocol=tcp`

The development database name is `cat`.

### Obtain an access token from Dev Service Keycloak

To obtain an access token, please follow the instructions provided by [Quarkus](https://quarkus.io/guides/security-openid-connect-dev-services#dev-services-for-keycloak).

Use the following credentials to log into Dev Service Keycloak:

- username : `alice`
- password : `alice`




