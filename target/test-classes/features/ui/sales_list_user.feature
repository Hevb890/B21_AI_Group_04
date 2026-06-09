@ui @sales @user
Feature: Sales List Page - User

  Background:
    Given the user is logged in
    And the user navigates to the sales list page

  @UI_SALESLISTPAGE_USER_01 @smoke
  Scenario: Verify regular user can view their sales records as a paginated list
    Then the sales records should be displayed as a paginated list

  @UI_SALESLISTPAGE_USER_02 @smoke
  Scenario: Verify "No Sales Found" message is displayed when no sales records exist
    Then the "No Sales Found" message should be displayed on the page