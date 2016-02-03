## Samples projects for Ready! API TestServer usage

This project currently contains simple projects showing how to run API tests with 
Ready! API TestServer.

In both cases you will have to provide system properties for testserver.host, testserver.user and 
 testserver.password containing the hostname, user and password of your TestServer installation. 

### JUnit Client Sample

This contains a single unit test that uses the TestServer Java Client to run a single request
against the SwaggerHub API and assert the response. You can run this either from within your
IDE or with maven:

```
mvn surefire:test -Dtestserver.host=... -Dtestserver.user=... -Dtestserver.password=...
```

### Maven Plugin Sample

This contains a single recipe that tests the SwaggerHub API and asserts the response. The 
testserver-maven-plugin is configured to run as part of your integration tests when
performing a mvn:install build - but you can also trigger them directly with 

```
mvn testserver:run -Dtestserver.host=... -Dtestserver.user=... -Dtestserver.password=...
```

### Looking for more samples?

Please don't hesitate to raise issues in this repo to ask for more!
