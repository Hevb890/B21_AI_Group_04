@api @auth @admin
Feature: Authentication & Dashboard API - Admin

  @API_POST_ADMIN_001 @smoke
  Scenario: Verify admin can login with valid credentials
    When the admin sends a POST request to login with valid admin credentials
    Then the response status code should be 200
    And the response should contain a valid JWT token

  @API_POST_ADMIN_002 @regression
  Scenario: Verify admin login fails with invalid password
    When the admin sends a POST request to login with valid username and invalid password
    Then the response status code should be 400

  @API_GET_ADMIN_003 @smoke
  Scenario: Verify admin can retrieve category summary
    Given the admin authentication token is available
    When the admin sends a GET request to retrieve category summary
    Then the response status code should be 200
    And the response should contain category summary data

  @API_GET_ADMIN_004 @smoke
  Scenario: Verify admin can retrieve plant summary
    Given the admin authentication token is available
    When the admin sends a GET request to retrieve plant summary
    Then the response status code should be 200
    And the response should contain plant summary data

  @API_GET_ADMIN_005 @smoke
  Scenario: Verify admin can retrieve sales records
    Given the admin authentication token is available
    When the admin sends a GET request to retrieve all sales records
    Then the response status code should be 200
    And the response should contain sales records
