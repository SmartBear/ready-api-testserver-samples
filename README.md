## Samples for Ready! API TestServer usage

This project currently contains simple projects showing how to run API tests with 
Ready! API TestServer.

## Configuration

All the samples in this project run against the public TestServer instance available at 
http://testserver.readyapi.io:8080, if you want to run against your own installation you
can provide system properties for testserver.host, testserver.user and testserver.password 
containing the hostname, user and password of your TestServer installation. 

### JUnit Client Sample

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

### Cucumber Samples

The Cucumber sample contains a simple feature file for testing the SwaggerHub API ()
and corresponding Step definitions to build and execute the required recipe.

### Looking for more samples?

Please don't hesitate to raise issues in this repo to ask for more!
