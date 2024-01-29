-- ------------------------------------------------
-- Version: v1.22
--
-- Description: Migration that insert a new Actor Scheme into the Actor table
-- -------------------------------------------------


INSERT INTO
      Actor(name, description)
VALUES
      ('PID Scheme','A set of rules and standards defining the nature of a PID. This would include a set of lexical formatting rules for PIDs within a namespace. It could also define for example: associated PID Type; definition of associated metadata; quality assurance conditions; usage rights, terms and conditions, and algorithmic methods for generating PID names and enforcing PID properties.');



