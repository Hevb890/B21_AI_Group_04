@api @plant @user
Feature: Plantation API - User

  Background:
    Given the user authentication token is valid

  @API_POST_PLANTATIONPAGE_USER_01 @smoke
  Scenario: Verify User-role cannot add a plants data via API
    Given a valid sub-category ID exists
    When the user sends a POST request to add a plant for the category
    Then the API response status code should be 403 Forbidden

  @API_PUT_PLANTATIONPAGE_USER_02 @smoke
  Scenario: Verify User-role cannot update plant data via API
    Given a plant record exists in the database
    When the user sends a PUT request to update the plant data
    Then the API response status code should be 403 Forbidden

  @API_DELETE_PLANTATIONPAGE_USER_03 @smoke
  Scenario: Verify User-role cannot delete plant data via API
    Given a plant record exists in the database
    When the user sends a DELETE request for the plant ID
    Then the API response status code should be 403 Forbidden

  @API_GET_PLANTATIONPAGE_USER_04 @smoke
  Scenario: Verify user-role can get access plant summary data
    When the user sends a GET request to retrieve plant summary data
    Then the API response status code should be 200 OK

  @API_GET_PLANTATIONPAGE_USER_05 @regression
  Scenario: Verify API boundary constraints for pagination query parameters
    When the user sends a GET request for plants with page "-1"
    Then the system gracefully processes or rejects via HTTP 400
    When the user sends a GET request for plants with size "0"
    Then the system gracefully processes or rejects via HTTP 400
    When the user sends a GET request for plants with size "1000"
    Then the API response status code should be 200 OK
