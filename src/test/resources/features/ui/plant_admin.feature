@ui @plant @admin
Feature: Plantation Page - Admin

  Background:
    Given the plant admin is logged in
    And the admin navigates to the plant page

  @UI_PLANTATIONPAGE_ADMIN_01 @smoke
  Scenario: Verify add a plant button appear to the admin role
    Then the "Add a Plant" button should be visible on the plant page

  @UI_PLANTATIONPAGE_ADMIN_02 @smoke
  Scenario: Verify admin can add a new plant with valid data
    Given the plant "Red Anthoorium" is removed if it already exists
    When the admin clicks the add plant button on the plant page
    And the admin enters "Red Anthoorium" in the Plant Name field
    And the admin selects "Anthoorium" from the Sub category dropdown
    And the admin enters "200" in the Price field
    And the admin enters "20" in the Quantity field
    And the admin clicks the save plant button
    Then a plant success message should be displayed
    And the system navigates back to the Plants page

  @UI_PLANTATIONPAGE_ADMIN_03 @regression
  Scenario: Verify Plant Name, Sub-category, Price, and Quantity are required fields
    When the admin clicks the add plant button on the plant page
    And the admin leaves Plant Name, Sub category, Price, Quantity empty
    And the admin clicks the save plant button
    Then a validation message "Plant name must be between 3 and 25 characters" should appear below the plant Name field
    And a validation message "Plant name is required" should appear below the plant Name field
    And a validation message "Category is required" should appear below the category field
    And a validation message "Price is required" should appear below the price field
    And a validation message "Quantity is required" should appear below the quantity field

  @UI_PLANTATIONPAGE_ADMIN_04 @smoke
  Scenario: Verify the system successfully searches for plants with multi-word names
    Given the searchable plant record is prepared for UI tests
    When the admin enters "red anthoorium" in the Search Plant field
    And the admin chooses the relevant option from the All Categories dropdown
    And the admin clicks the plant search button
    Then the plant list should accurately display the "Red Anthurium" record

  @UI_PLANTATIONPAGE_ADMIN_05 @regression
  Scenario: Verify admin can successfully edit existing plant data with valid plant data
    Given the edit target plant "Red Anthurium" is prepared for UI tests
    When the admin clicks the Edit button on a particular plant data row "Red Anthurium"
    And the admin enters "White Orkind" in the Plant Name field
    And the admin selects the updated sub-category from the dropdown
    And the admin enters the updated price in the Price field
    And the admin enters the updated quantity in the Quantity field
    And the admin clicks the save plant button
    Then a plant success message "Plant updated successfully" should be displayed
    And the system navigates back to the Plants page
