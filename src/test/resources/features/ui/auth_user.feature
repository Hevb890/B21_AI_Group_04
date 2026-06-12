@ui @auth @user
Feature: Authentication - User UI

  @UI_AUTH_USER_001 @smoke
  Scenario: Verify regular user can successfully login with valid credentials
    Given the user is on the login page
    When the user enters valid user credentials
    And the user clicks the login button
    Then the login should be successful for the user
    And the user should be redirected to the dashboard page

  @UI_AUTH_USER_002 @regression
  Scenario: Verify invalid login error message is displayed for regular user
    Given the user is on the login page
    When the user enters invalid username and password
    And the user clicks the login button
    Then the login should fail
    And a global error message "Invalid username or password." should be displayed

  @UI_AUTH_USER_003 @regression
  Scenario: Verify unauthorized user is redirected to login page when accessing dashboard without authentication
    Given the user is not logged into the system
    When the user directly accesses the dashboard URL
    Then the user should be redirected to the login page
    And the dashboard should not be accessible without login
