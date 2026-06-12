@ui @auth @admin
Feature: Authentication - Admin UI

  @UI_AUTH_ADMIN_001 @smoke
  Scenario: Verify that admin user can successfully login with valid credentials
    Given the admin is on the login page
    When the admin enters valid admin credentials
    And the admin clicks the login button
    Then the login should be successful
    And the admin should be redirected to the dashboard page

  @UI_AUTH_ADMIN_002 @regression
  Scenario: Verify validation message is displayed when admin submits empty login form
    Given the admin is on the login page
    When the admin submits the login form without entering any credentials
    Then a validation message "Username is required" should be displayed below the username field
    And a validation message "Password is required" should be displayed below the password field

  @UI_AUTH_ADMIN_003 @smoke
  Scenario: Verify logout success message is displayed for admin user
    Given the admin is logged in successfully
    When the admin clicks the logout button
    Then the admin should be logged out successfully
    And a success logout message should be displayed
    And the admin should be redirected to the login page
