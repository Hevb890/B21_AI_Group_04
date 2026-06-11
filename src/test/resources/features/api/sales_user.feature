@api @sales @user
Feature: Sales API - User

  Background:
    Given the user authentication token is available

  @API_GET_PAGINATEDSALES_01 @smoke
  Scenario: Verify successful retrieval of paginated sales records when records are available
    When the user sends a GET request to retrieve paginated sales
    Then the response status code should be 200
    And the response should contain paginated sales data

  @API_POST_SELLPLANT_02 @smoke
  Scenario: Verify unauthorized access to plant selling with user token
    Given a valid plant id exists
    When the user sends a POST request to sell a plant
    Then the response status code should be 401

  @API_DELETE_SALE_03 @smoke
  Scenario: Verify unauthorized access to delete sales with user token
    Given a valid sale id exists
    When the user sends a DELETE request to delete a sale
    Then the response status code should be 401

  @API_GET_SALES_04 @smoke
  Scenario: Verify successful retrieval of sale with valid sale id
    Given a valid sale id exists
    When the user sends a GET request to retrieve a sale by id
    Then the response status code should be 200
    And the response should contain valid sale details

  @API_GET_PAGINATEDSALES_02 @regression
  Scenario Outline: Verify paginated sales retrieval with different sorting options
    When the user sends a GET request to retrieve paginated sales sorted by "<sortField>" in "<sortDir>" order
    Then the response status code should be 200
    And the response should contain paginated sales data

    Examples:
      | sortField  | sortDir |
      | soldAt     | asc     |
      | soldAt     | desc    |
      | plant.name | asc     |
      | plant.name | desc    |