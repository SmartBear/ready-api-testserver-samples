Feature: SwaggerHub REST API
  Scenario: Default API Listing
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10
    When a request to searchApis is made
    Then the response is a list of APIs in APIs.json format

  Scenario: Owner API Listing
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10
    When a request to getOwnerApis is made
    And owner is swagger-hub
    Then the response is a list of APIs in APIs.json format

  Scenario: API Version Listing
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10
    When a request to getApiVersions is made
    And owner is swagger-hub
    And api is registry-api
    Then the response is a list of API versions in APIs.json format
    And the response body contains
      """
      "url":"/apis/swagger-hub/registry-api"
      """

  Scenario: API Retrieval
    Given the Swagger definition at https://api.swaggerhub.com/apis/swagger-hub/registry-api/1.0.10
    When a request to getDefinition is made
    And owner is swagger-hub
    And api is registry-api
    And version is 1.0.10
    Then the response is the Swagger API in requested format
