# recipe-services
Recipe is designed to add new recipe details in the database and filter the recipes based on the filter criteria

### System Design
Restaurant Menu Service is microservice based layered RESTful Web Service.
- Top layer, which is main interface available for integration and interaction with front-end or consumers
- Service Layer
  - this layer mainly focuses on the business logic, and transformation from entity to model
  - it get the input from the top layer and validates the data. if the input is valid then it transforms to the input to data layer. similar way response from data layer is validates and transform the data and return to top layer
- Data Access Layer
  - it mainly focuses ORM, we use default JPA repository.
- Persistence Layer
  - it is responsible for the the persisting the data to DB
  - H2 database used in Dev environment & mysql for prod environment.

### Tech stacks
Feature | Framework used
------------ | -------------
ReSTful API | [Springboot](https://spring.io/projects/spring-boot)
Object Relationship Mapping | [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
Logging | [SLF4J](http://www.slf4j.org/manual.html) Logger
Unit Tests | Junit 5 [junit5](https://junit.org/junit5/docs/current/user-guide/)
Integration Tests | Rest Assured [RestAssured](https://rest-assured.io/)


### Prerequisites
* [JDK 11](https://docs.oracle.com/en/java/javase/11/)
* [Apache Maven](https://maven.apache.org/)

### Steps to build Web Service
* clone the repository from github or download code zip
* move to the project root folder and then `mvn clean package` or import the cloned Project to IDE and do the same process

### Steps to execute Web Service
* **Execution on Development profile with Embedded H2 Database**
  - In Development Mode, by default web service uses [Embedded H2 database](https://spring.io/guides/gs/accessing-data-jpa/) for persisting and retrieving recipes details.
  - Command to execute:
    --> Run main application RestaurantMenuServiceApplication.java
  - On successfull start, one should notice log message on console `Tomcat started on port(s): 9000 (http)` and have web service listening for web requests at port 9000
  - it also creates `RECIPES` table and load default data for dev environment

  - you can access [h2-db console](http://localhost:9000/h2-console/)

### Web Service Rest API End Points
Restaurant Recipe Menu Webservice comes with ReST API Ends points for creating a new recipe, filters recipes based on the filter criteria.

API End Point | Method | Purpose | Request | Response
------------ | ------------- | ------------- | ------------ | -------------
/recipe | POST   | Create a new recipe                                              | RecipeVariant Model       | Recipe Model with 201 Created on Success, 400 Bad request on failure
/recipe/filter | POST   | filter recipes based on search criteria          | RecipeFilterCriteria | List of RecipeVariant Model with 200 on Success, 204 if no results found with search
/recipe/{id} | GET    | get the single recipe based on the recipe id     | Integer Id           | RecipeVariant Model with 200 on Success, 204 if No Recipe found for the ID               
/recipes | GET    | get all recipes from the database                       |            | get all recipes with 200 on Success, 204 if no results found in database 
/recipe/{id} | DELETE | delete recipe based on recipe id                     |  Integer Id                     | informational message with 200 on Success, 204 if mentioned recipe not found      
/recipe | PUT    | modify the existing recipe based on information provide   | RecipeVariant Mode        | modified RecipeVariant Model with 201 on Success, 400 for failure       


### Future improvements
- object transformations can be done through [mapStruct](https://mapstruct.org/)
- improvements with swagger documentation 

