@ui @category @admin
Feature: Category Page - Admin

  Background:
    Given the admin is logged in
    And the admin navigates to the category page

  @UI_CATEGORYPAGE_ADMIN_01 @smoke
  Scenario: Verify "Add Category" button is visible to admin users
    Then the "Add Category" button should be visible on the page

  @UI_CATEGORYPAGE_ADMIN_02 @smoke
  Scenario: Verify admin can search category by category name
    Given multiple categories exist in the system
    When the admin enters "Anthoorium" in the search field
    And the admin clicks the search button
    Then the category list should display only matching categories

  @UI_CATEGORYPAGE_ADMIN_03 @regression
  Scenario: Verify sorting by category name works correctly
    Given multiple categories exist in the system
    When the admin clicks the category name column header to sort
    Then the categories should be displayed in alphabetical order by category name

  @UI_ADDCATEGORYPAGE_ADMIN_04 @regression
  Scenario: Verify validation message appears when category name is empty
    When the admin clicks the "Add Category" button
    And the admin leaves the category name empty
    And the admin clicks the save button
    Then a validation message "Category name is required" should appear below the field

  @UI_ADDCATEGORYPAGE_ADMIN_05 @smoke
  Scenario: Verify cancel button navigates back to category list page
    When the admin clicks the "Add Category" button
    And the admin clicks the "Cancel" button on the add category page
    Then the admin should be redirected back to the category list page
