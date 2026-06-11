@api @category @admin
Feature: Category API - Admin

  Background:
    Given the admin authentication token is available

  @API_POST_CATEGORY_01 @smoke
  Scenario: Verify admin can create category successfully
    When the admin sends a POST request to create a category with valid data
      | name | TestCat |
    Then the response status code should be 201
    And the response should contain the category details

  @API_POST_CATEGORY_02 @regression
  Scenario: Verify duplicate category creation is restricted
    Given a category already exists with name "ExistCat"
    When the admin sends a POST request to create a category with the same name
      | name | ExistCat |
    Then the response status code should be 400
    And the response should contain a validation error message

  @API_PUT_CATEGORY_03 @regression
  Scenario: Verify admin can update category details
    Given a valid category exists with ID 1
    When the admin sends a PUT request to update the category
      | name | UpdCat |
    Then the response status code should be 200
    And the response should contain the updated category details

  @API_DELETE_CATEGORY_04 @smoke
  Scenario: Verify admin can delete category
    Given a valid category exists with ID 1
    When the admin sends a DELETE request to delete the category
    Then the response status code should be 200 or 204
    And the category should be deleted successfully

  @API_GET_CATEGORYPAGE_05 @regression
  Scenario: Verify admin can retrieve paginated category list
    Given multiple categories exist in the database
    When the admin sends a GET request to retrieve paginated categories with page=0 and size=10
    Then the response status code should be 200
    And the response should contain paginated category records
