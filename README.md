# DisciplineApp - application for habits, goals and communication beetween people, who creates their habits and goals.

- Java 8
- MySql (or in-memory H2)
- Tomcat
- Maven
- Spring
- FlyWay
- Hibernate

To start application locally you need to install:
1. Java 8 
2. Maven
3. Tomcat.

Start your tomcat localy, after starting - choose profile 'dev' (for creating in-memory DB) and command 'package' (maven lifecycle).
Or create in MySql only scheme and command 'pakcage'. Flyway plugin creates tables and data.