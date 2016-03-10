## Samples for Ready! API TestServer usage

This project currently contains simple projects showing how to run API tests with 
Ready! API TestServer.

Clone this project and run 'mvn test' in the root folder; this will run each of the included modules;
- The JUnit sample runs API tests using the java client library for TestServer
- The Maven plugin runs API test recipes and projects provided as resources
- The Cucumber sample "executes" a feature file via the java client for the TestServer 

### JUnit Sample

In the java/junit-client-sample folder/module; this contains a single unit test that uses the 
[TestServer Java Client](https://github.com/SmartBear/ready-api-testserver-client) to run a single request
against the SwaggerHub API and assert the response. You can run this either from within your
IDE or with maven:

```
mvn surefire:test -Dtestserver.host=... -Dtestserver.user=... -Dtestserver.password=...
```

### Maven Plugin Sample

In the java/maven-plugin-sample folder/module; this contains a single recipe that tests the SwaggerHub API 
and asserts the response. The [testserver-maven-plugin](https://github.com/olensmar/readyapi-testserver-maven-plugin) 
is configured to run as part of your integration tests when performing a mvn:install build - but you can also 
trigger them directly with 

```
mvn testserver:run -Dtestserver.host=... -Dtestserver.user=... -Dtestserver.password=...
```

### Cucumber Sample

The Declarative cucumber sample contains a simple [feature file](https://github.com/SmartBear/ready-api-testserver-samples/blob/master/java/cucumber-sample/src/test/resources/cucumber/swaggerhub.feature) for testing the SwaggerHub API 
and corresponding [step definitions](https://github.com/SmartBear/ready-api-testserver-samples/blob/master/java/cucumber-sample/src/test/java/com/smartbear/readyapi/testserver/SwaggerHubStepDefs.java) to build and execute the required recipe. 

For a generic imperative approach to API Testing with Cucumber and TestServer please have a look at 
the [testserver-cucumber](https://github.com/olensmar/testserver-cucumber) project.

Running them from the command-line with "mvn test" will result in the following:

```
...
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.smartbear.readyapi.testserver.RunCucumberTest
Feature: SwaggerHub REST API

  Scenario: Default API Listing                         # swaggerhub.feature:2
    When a request to the API listing is made           # SwaggerHubStepDefs.aRequestToTheAPIListing()
    Then a list of APIs should be returned within 500ms # SwaggerHubStepDefs.aListOfAPIsShouldBeReturned(int)

  Scenario: Owner API Listing                           # swaggerhub.feature:6
    Given an owner named swagger-hub                    # SwaggerHubStepDefs.anOwnerNamed(String)
    When a request to the API listing is made           # SwaggerHubStepDefs.aRequestToTheAPIListing()
    Then a list of APIs should be returned within 500ms # SwaggerHubStepDefs.aListOfAPIsShouldBeReturned(int)

  Scenario: API Version Listing                         # swaggerhub.feature:11
    Given an owner named swagger-hub                    # SwaggerHubStepDefs.anOwnerNamed(String)
    And an api named registry-api                       # SwaggerHubStepDefs.anApiNamed(String)
    When a request to the API listing is made           # SwaggerHubStepDefs.aRequestToTheAPIListing()
    Then a list of APIs should be returned within 500ms # SwaggerHubStepDefs.aListOfAPIsShouldBeReturned(int)

  Scenario: API Retrieval                                  # swaggerhub.feature:17
    Given an owner named swagger-hub                       # SwaggerHubStepDefs.anOwnerNamed(String)
    And an api named registry-api                          # SwaggerHubStepDefs.anApiNamed(String)
    And a version named 1.0.0                              # SwaggerHubStepDefs.aVersionNamed(String)
    When a request to the API listing is made              # SwaggerHubStepDefs.aRequestToTheAPIListing()
    Then an API definition should be returned within 500ms # SwaggerHubStepDefs.anApiDefinitionShouldBeReturned(int)

4 Scenarios (4 passed)
14 Steps (14 passed)
0m2.136s

Tests run: 18, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.617 sec
...
```

## Configuration

All the samples in this project run against the public TestServer instance available at 
http://testserver.readyapi.io:8080, if you want to run against your own installation you
can provide system properties for testserver.host, testserver.user and testserver.password 
containing the hostname, user and password of your TestServer installation. 


### Looking for more samples?

Please don't hesitate to raise issues in this repo to ask for more!
