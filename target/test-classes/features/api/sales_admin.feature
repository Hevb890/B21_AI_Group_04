@api @sales @admin
Feature: Sales API - Admin

  Background:
    Given the admin authentication token is available

  @API_POST_SELLPLANT_01 @smoke
  Scenario: Verify successful sell plant request with valid plant id and quantity
    Given a valid plant exists with sufficient stock
    When the admin sends a POST request to sell the plant with quantity 1
    Then the response status code should be 201
    And the plant stock should be reduced

  @API_POST_SELLPLANT_02 @regression
  Scenario: Verify validation of exceeded plant quantity
    Given a valid plant exists with sufficient stock
    When the admin sends a POST request to sell the plant with exceeded quantity
    Then the response status code should be 400

  @API_DELETE_SALE_03 @smoke
  Scenario: Verify successful deletion request with valid sale id
    Given a sale record exists
    When the admin sends a DELETE request to delete the sale
    Then the response status code should be 204

  @API_POST_SELLPLANT_04 @regression
  Scenario: Verify validation of quantity less than 1
    Given a valid plant exists with sufficient stock
    When the admin sends a POST request to sell the plant with quantity 0
    Then the response status code should be 400

  @API_GET_SALES_05 @smoke
  Scenario: Verify retrieval of all available sales records
    When the admin sends a GET request to retrieve all sales
    Then the response status code should be 200
    And the response should contain a valid sales list