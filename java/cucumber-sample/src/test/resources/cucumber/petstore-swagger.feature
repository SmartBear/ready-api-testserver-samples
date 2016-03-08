Feature: Petstore API

  Scenario: Find pet by status
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json
    When a request to findPetsByStatus is made
    And status equals test
    And the client accepts json
    Then a 200 response is returned within 500ms

  Scenario: Find pet by tags
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json
    When a request to findPetsByTags is made
    And tags equals test
    And the client accepts json
    Then a 200 response is returned within 500ms

  Scenario: Create pet with parameters
    Given the Swagger definition at http://petstore.swagger.io/v2/swagger.json
    When a request to addPet is made
    And name equals doggies
    And status equals available
    Then a 200 response is returned within 500ms

