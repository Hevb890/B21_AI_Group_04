@api @auth @user
Feature: Authentication & Dashboard API - User

  @API_POST_USER_001 @smoke
  Scenario: Verify user can login with valid credentials
    When the user sends a POST request to login with valid user credentials
    Then the response status code should be 200
    And the response should contain a valid JWT token for the user

  @API_POST_USER_002 @regression
  Scenario: Verify user login fails with empty credentials
    When the user sends a POST request to login with empty username and password
    Then the response status code should be 400

  @API_GET_USER_003 @regression
  Scenario: Verify unauthorized user cannot access sales details without valid token
    When the user sends a GET request to retrieve sales details without a valid token
    Then the response status code should be 401 or 403

  @API_GET_USER_004 @smoke
  Scenario: Verify user can retrieve category summary
    Given the user authentication token is available
    When the user sends a GET request to retrieve the category summary
    Then the response status code should be 200
    And the response should contain category summary data for the user

  @API_GET_USER_005 @smoke
  Scenario: Verify user can retrieve plant summary
    Given the user authentication token is available
    When the user sends a GET request to retrieve the plant summary
    Then the response status code should be 200
    And the response should contain plant summary data for the user
