@ui @sales @admin
Feature: Sales List Page - Admin

  Background:
    Given the admin is logged in
    And the admin navigates to the sales list page

  @UI_SALESLISTPAGE_ADMIN_01 @smoke
  Scenario: Verify that "Sell Plant" button is visible to the admin
    Then the "Sell Plant" button should be visible on the page

  @UI_SALESLISTPAGE_ADMIN_02 @smoke
  Scenario: Verify that confirmation prompt appears when admin performs delete action
    When the admin clicks the "Delete" button on a sales record
    Then a confirmation prompt should appear

  @UI_SALESLISTPAGE_ADMIN_03 @regression
  Scenario: Verify plant dropdown contains available plants and current stock
    When the admin clicks the "Sell Plant" button
    And the admin opens the plant dropdown
    Then the plant dropdown should contain available plants with stock information

  @UI_SALESLISTPAGE_ADMIN_04 @regression
  Scenario: Verify error message is displayed when quantity exceeds current stock
    When the admin clicks the "Sell Plant" button
    And the admin selects a plant from the dropdown
    And the admin enters a quantity that exceeds the current stock
    And the admin submits the sell plant form
    Then an error message should be displayed for exceeding stock

  @UI_SALESLISTPAGE_ADMIN_05 @regression
  Scenario: Verify navigation back to sales list page when cancel button is clicked
    When the admin clicks the "Sell Plant" button
    And the admin clicks the "Cancel" button on the sell plant page
    Then the admin should be navigated back to the sales list page
