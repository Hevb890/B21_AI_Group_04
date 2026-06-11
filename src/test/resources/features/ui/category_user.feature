@ui @category @user
Feature: Category Page - User

  Background:
    Given the user is logged in as a regular user
    And the user navigates to the category page

  @UI_CATEGORYPAGE_USER_01 @smoke
  Scenario: Verify regular user can view category list as paginated list
    Then the user should see a paginated list of categories

  @UI_CATEGORYPAGE_USER_02 @regression
  Scenario: Verify "No category found" message is displayed when no records exist
    Given no categories exist in the system
    When the user navigates to the category page
    Then the user should see the message "No category found" on the page

  @UI_CATEGORYPAGE_USER_03 @regression
  Scenario: Verify regular user can filter categories by parent category
    Given categories with parent category records exist
    When the user selects a parent category from the filter dropdown
    Then the user should see only categories related to the selected parent category

  @UI_CATEGORYPAGE_USER_04 @smoke
  Scenario: Verify "Add Category" button is not visible to regular users
    Then the "Add Category" button should not be visible on the page

  @UI_CATEGORYPAGE_USER_05 @smoke
  Scenario: Verify Edit and Delete actions are hidden for regular users
    Given category records exist
    Then the "Edit" and "Delete" action buttons should be hidden or disabled for the user
