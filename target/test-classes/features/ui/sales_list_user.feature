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

  @UI_SALESLISTPAGE_USER_03 @regression
  Scenario: Verify default sorting is set to "sold date" when the sales page is loaded
    Then the default sorting option should be "sold date"

  @UI_SALESLISTPAGE_USER_04 @regression
  Scenario: Verify other sorting options are available on the sales page
    When the user clicks the sorting option
    Then other sorting options should be visible

  @UI_SALESLISTPAGE_USER_05 @regression
  Scenario: Verify delete button is not visible to regular users
    Then the delete button should not be visible to the user