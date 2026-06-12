@api @category @user
Feature: Category API - User

  Background:
    Given the user authentication token is available

  @API_GET_CATEGORIES_01 @smoke
  Scenario: Verify regular user can view category list
    When the user sends a GET request to retrieve all categories
    Then the response status code should be 200
    And the response should contain a valid category list

  @API_GET_CATEGORYBYID_02 @smoke
  Scenario: Verify user can retrieve category by valid ID
    Given a valid category exists with ID 1
    When the user sends a GET request to retrieve category with ID 1
    Then the response status code should be 200
    And the response should contain the correct category details

  @API_POST_CATEGORY_03 @regression
  Scenario: Verify regular user cannot create category
    When the user sends a POST request to create a category with valid data
      | name | UserCat |
    Then the response status code should be 403
    And the response should contain an "Forbidden" message

  @API_GET_CATEGORY_INVALIDID_04 @regression
  Scenario: Verify user receives error for invalid category ID
    When the user sends a GET request to retrieve category with invalid ID 99999
    Then the response status code should be 404
    And the response should contain a "Not Found" error message

  @API_GET_MAIN_CATEGORIES_05 @smoke
  Scenario: Verify user can retrieve main categories only
    Given main categories exist in the database
    When the user sends a GET request to retrieve main categories
    Then the response status code should be 200
    And the response should contain only main categories
