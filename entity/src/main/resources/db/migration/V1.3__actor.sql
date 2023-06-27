-- ------------------------------------------------
-- Version: v1.3
--
-- Description: Migration that introduces the Actor table
-- -------------------------------------------------

-- actor table
CREATE TABLE Actor (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name varchar(255) NOT NULL,
   description varchar(455) NOT NULL
 );

INSERT INTO
      Actor(name, description)
VALUES
      ('PID Standards Body','A PID Standardisation organisation (IETF, IANA, The DONA Foundation, ISO) which appoints the PID Authority and is responsible for the PID Scheme.'),
      ('PID Authority','A controller responsible for maintaining the rules for defining the integrity of PIDs within a PID Scheme. These rules may include setting standards for lexical formats, algorithms and protocols to ensure global uniqueness, together with setting quality of service conditions to enforce compliance to the rules. PID Authorities may be organisations (e.g. DOI.org), which enforce control over a PID infrastructure.'),
      ('PID Service Provider','An organisation which provides PID services in conformance to a PID Scheme, subject to its PID Authority. PID Service Providers have responsibility for the provision, integrity, reliability and scalability of PID Services, in particular the issuing and resolution of PIDs, but also lookup and search services, and interoperability with a generic resolution system.'),
      ('Multi-Primary Administrator','Each credentialed MPA operates its own GHR Services in accordance with the DONA Foundation Policies & Procedures for the GHR, and coordinates its GHR Services with other MPAs and DONA in the distributed operation of the GHR on a multi-primary basis.'),
      ('PID Manager','PID Managers have responsibilities to maintain the integrity of the relationship between entities and their PIDs, in conformance to a PID Scheme defined by a PID Authority. A PID Manager will typically subscribe to PID services to offer functionality to PID Owners within the PID Manager’s services.'),
      ('PID Owner','An actor (an organisation or individual) who has the authority to create a PID, assign PID to an entity, provide and maintain accurate Kernel Information for the PID. A new PID Owner must be identified and these responsibilities transferred, if the current PID Owner is no longer able to carry them out.'),
      ('End User','The end user of PID Services, for example researchers, or software, or services produced to support researchers.'),
      ('Compliance Monitoring','On completion, the work will support an additional role and associated component for the EOSC PID Policy, as follows: Compliance Monitoring (Role) - One or more organisations that provide services to monitor and/ or enforce compliance (with PID Policy), resulting in interoperable and aggregatable compliance metrics for the roles and components foreseen in the policy.');




