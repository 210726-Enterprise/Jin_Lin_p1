# Project 1 WebApp

## Description
Simple web application to demonstrate the use of custom ORM (Object-Relational Mapping) deployed on AWS
EC2. Demonstration will perform CRUD operations through Postman and present persistence in the database.

## Functional Implementations
- [x] CRUD operations are supported for one or more domain objects via the web application's exposed endpoints
- [x] JDBC logic is abstracted away by the custom ORM
- [x] Programmatic persistence of entities (basic CRUD support) using custom ORM
- [ ] File-based or programmatic configuration of entities

## Additional Implementations
- [ ] 80% line coverage of all service layer classes
- [ ] Generated Jacoco reports that display coverage metrics
- [x] Usage of the java.util.Stream API within your project
- [x] Custom ORM source code should be included within the web application as a Maven dependency

## Tech Stack
- [x] Java 8
- [x] JUnit
- [ ] Mockito
- [x] Apache Maven
- [x] Jackson library (for JSON marshalling/unmarshalling)
- [x] Java EE Servlet API (v4.0+)
- [x] PostGreSQL deployed on AWS RDS
- [x] Git SCM (on GitHub)
- [x] Tomcat 9.0.52
- [ ] Jacoco
- [x] Postman
- [ ] AWS EC2