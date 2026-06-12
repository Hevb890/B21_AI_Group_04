@ui @plant @user
Feature: Plantation Page - User

  Background:
    Given the plant user is logged in
    And the user navigates to the plant page

  @UI_PLANTATIONPAGE_USER_01 @smoke
  Scenario: Verify the system successfully searches for plants with multi-word names
    Given the searchable plant record is prepared for UI tests
    When the user enters "red anthoorium" in the Search Plant field
    And the user chooses the relevant option from the All Categories dropdown
    And the user clicks the plant search button
    Then the plant list should accurately display the "Red Anthurium" record

  @UI_PLANTATIONPAGE_USER_02 @smoke
  Scenario: Verify clicking the Reset button clears all search filters and restores the full plant records display
    Given the searchable plant record is prepared for UI tests
    When the user enters text into the Search Plant field
    And the user chooses the relevant option from the All Categories dropdown
    And the user clicks the plant search button
    And the user clicks the Reset button
    Then the text in the Search Plant field should be cleared
    And the All Categories dropdown should revert to its default state
    And the plant list should update dynamically to show all available records

  @UI_PLANTATIONPAGE_USER_04 @smoke
  Scenario: Verify visual indicator logic for low-stock plant products
    Given plants with low and high stock levels are prepared for UI tests
    When the user locates a plant row where the quantity is under 5
    Then a visible "Low" indicator tag is attached to the plant entry
    When the user locates a plant row where the quantity is 5 or more
    Then no indicator tag or warning badge is applied to the entry

  @UI_PLANTATIONPAGE_USER_05 @regression
  Scenario: Verify multi-column sorting functionality for the plant data table
    Given multiple plant records are prepared for sorting tests
    When the user clicks the column header to toggle sorting for "Plant Name"
    Then the list dynamically rearranges alphabetically by Plant name
    When the user clicks the column header to toggle sorting for "Price"
    Then the list dynamically rearranges numerically by Price values
    When the user clicks the column header to toggle sorting for "Quantity"
    Then the list dynamically rearranges numerically by remaining stock levels

  @UI_PLANTATIONPAGE_USER_03 @regression @destructive
  Scenario: Verify the No plants found empty state behavior on the plants list dashboard
    Given no plants exist in the system
    Then the user should see the message "No plants found" on the plant page
