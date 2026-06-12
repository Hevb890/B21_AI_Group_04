@api @plant @admin
Feature: Plantation API - Admin

  Background:
    Given the admin authentication token is valid

  @API_POST_PLANTATIONPAGE_ADMIN_01 @smoke
  Scenario: Verify admin can add a new plant successfully
    Given a valid sub-category ID exists
    When the admin sends a POST request to add a plant for the category
    Then the API response status code should be 201 Created
    And the plant record should be created successfully in the response

  @API_GET_PLANTATIONPAGE_ADMIN_02 @smoke
  Scenario: Verify successful retrieval of plant record by ID
    Given a plant record exists in the database
    When the admin sends a GET request to retrieve the plant by ID
    Then the API response status code should be 200 OK
    And the response should contain the plant details

  @API_DELETE_PLANTATIONPAGE_ADMIN_03 @smoke
  Scenario: Verify successful deletion of a plant record by ID
    Given a plant record exists in the database
    When the admin sends a DELETE request for the plant ID
    Then the API response status code should be 204 No Content
    And the plant record should be completely removed from the database

  @API_GET_PLANTATIONPAGE_ADMIN_04 @smoke
  Scenario: Verify filtering plants by category ID
    Given plant records exist under a specific category ID
    When the admin sends a GET request to filter plants by the category ID
    Then the API response status code should be 200 or 204

  @API_DELETE_PLANTATIONPAGE_ADMIN_05 @regression
  Scenario: Validate handling of non-existent plant deletion
    Given target plant ID 99999 does not exist in the database
    When the admin sends a DELETE request for the plant ID 99999
    Then the API response status code should be 204 No Content
    And the non-existent plant record should remain absent from the database
