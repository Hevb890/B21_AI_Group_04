@ui @dashboard @user
Feature: Dashboard - User UI

  Background:
    Given the user is logged in successfully

  @UI_DASHBOARD_USER_004 @smoke
  Scenario: Verify dashboard summary information is displayed for regular user
    Given the user is on the dashboard page
    Then the dashboard should load successfully for the user
    And the category summary card should be visible for the user
    And the plants summary card should be visible for the user
    And the sales summary card should be visible for the user

  @UI_DASHBOARD_USER_005 @regression
  Scenario: Verify active navigation menu is highlighted on Dashboard page for regular user
    Given the user is on the dashboard page
    Then the dashboard menu item should be highlighted as active for the user
