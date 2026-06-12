@ui @dashboard @admin
Feature: Dashboard - Admin UI

  Background:
    Given the admin is logged in successfully

  @UI_DASHBOARD_ADMIN_004 @smoke
  Scenario: Verify dashboard summary information is displayed for admin user
    Given the admin is on the dashboard page
    Then the dashboard should load successfully
    And the category summary card should be visible
    And the plants summary card should be visible
    And the sales summary card should be visible

  @UI_DASHBOARD_ADMIN_005 @regression
  Scenario: Verify active navigation menu is highlighted on Dashboard page for admin user
    Given the admin is on the dashboard page
    Then the dashboard menu item should be highlighted as active
