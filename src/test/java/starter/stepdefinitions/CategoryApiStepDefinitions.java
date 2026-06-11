package starter.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import starter.steps.CategoryAdminApiSteps;
import starter.steps.CategoryUserApiSteps;

public class CategoryApiStepDefinitions {

    @Steps
    CategoryAdminApiSteps categoryAdminApiSteps;

    @Steps
    CategoryUserApiSteps categoryUserApiSteps;

    // ─── Background - Admin ───────────────────────

    // ─── Background - User ────────────────────────

    // ─── Setup Steps - Admin ──────────────────────
    @Given("multiple categories exist in the database")
    public void multipleCategoriesExistInDatabase() {
        categoryAdminApiSteps.ensureMultipleCategoriesExist();
    }

    @Given("a category already exists with name {string}")
    public void aCategoryAlreadyExistsWithName(String categoryName) {
        categoryAdminApiSteps.createCategoryIfNotExists(categoryName);
    }

    @Given("a valid category exists with ID {long}")
    public void aValidCategoryExistsWithID(Long categoryId) {
        categoryAdminApiSteps.verifyCategoryExists(categoryId);
    }

    // ─── API_POST_CATEGORY_01 ─────────────────────
    @When("the admin sends a POST request to create a category with valid data")
    public void theAdminSendsPostRequestToCreateCategory(io.cucumber.datatable.DataTable dataTable) {
        String categoryName = dataTable.asMap(String.class, String.class).get("name");
        categoryAdminApiSteps.createCategory(categoryName);
    }



    @Then("the response should contain the category details")
    public void theResponseShouldContainCategoryDetails() {
        categoryAdminApiSteps.verifyResponseContainsCategoryDetails();
    }

    // ─── API_POST_CATEGORY_02 ─────────────────────
    @When("the admin sends a POST request to create a category with the same name")
    public void theAdminSendsPostRequestWithSameName(io.cucumber.datatable.DataTable dataTable) {
        String categoryName = dataTable.asMap(String.class, String.class).get("name");
        categoryAdminApiSteps.createCategory(categoryName);
    }

    @Then("the response should contain a validation error message")
    public void theResponseShouldContainValidationError() {
        categoryAdminApiSteps.verifyResponseContainsError();
    }

    // ─── API_PUT_CATEGORY_03 ──────────────────────
    @When("the admin sends a PUT request to update the category")
    public void theAdminSendsPutRequestToUpdateCategory(io.cucumber.datatable.DataTable dataTable) {
        String categoryName = dataTable.asMap(String.class, String.class).get("name");
        categoryAdminApiSteps.updateCategory(1L, categoryName);
    }

    @Then("the response should contain the updated category details")
    public void theResponseShouldContainUpdatedDetails() {
        categoryAdminApiSteps.verifyResponseContainsCategoryDetails();
    }

    // ─── API_DELETE_CATEGORY_04 ───────────────────
    @When("the admin sends a DELETE request to delete the category")
    public void theAdminSendsDeleteRequestToDeleteCategory() {
        categoryAdminApiSteps.deleteCategory(1L);
    }



    @Then("the category should be deleted successfully")
    public void theCategoryShouldBeDeletedSuccessfully() {
        categoryAdminApiSteps.verifyCategoryDeleted();
    }

    // ─── API_GET_CATEGORYPAGE_05 ──────────────────
    @When("the admin sends a GET request to retrieve paginated categories with page={int} and size={int}")
    public void theAdminSendsGetRequestForPaginatedCategories(int page, int size) {
        categoryAdminApiSteps.getPaginatedCategories(page, size);
    }

    @Then("the response should contain paginated category records")
    public void theResponseShouldContainPaginatedRecords() {
        categoryAdminApiSteps.verifyResponseContainsPaginatedRecords();
    }

    // ─── User API Tests ───────────────────────────
    @When("the user sends a GET request to retrieve all categories")
    public void theUserSendsGetRequestToRetrieveAllCategories() {
        categoryUserApiSteps.getAllCategories();
    }

    @Then("the response should contain a valid category list")
    public void theResponseShouldContainValidCategoryList() {
        categoryUserApiSteps.verifyResponseContainsCategoryList();
    }

    @When("the user sends a GET request to retrieve category with ID {long}")
    public void theUserSendsGetRequestToRetrieveCategoryWithID(Long categoryId) {
        categoryUserApiSteps.getCategoryById(categoryId);
    }

    @Then("the response should contain the correct category details")
    public void theResponseShouldContainCorrectDetails() {
        categoryUserApiSteps.verifyResponseContainsCategoryDetails();
    }

    @When("the user sends a POST request to create a category with valid data")
    public void theUserSendsPostRequestToCreateCategory(io.cucumber.datatable.DataTable dataTable) {
        String categoryName = dataTable.asMap(String.class, String.class).get("name");
        categoryUserApiSteps.createCategory(categoryName);
    }

    @Then("the response should contain an {string} message")
    public void theResponseShouldContainMessage(String message) {
        categoryUserApiSteps.verifyResponseContainsMessage(message);
    }

    @When("the user sends a GET request to retrieve category with invalid ID {int}")
    public void theUserSendsGetRequestWithInvalidID(int categoryId) {
        categoryUserApiSteps.getCategoryByInvalidId(categoryId);
    }

    @Then("the response should contain a {string} error message")
    public void theResponseShouldContainErrorMessage(String errorType) {
        categoryUserApiSteps.verifyResponseContainsErrorMessage(errorType);
    }

    // ─── Setup - User ─────────────────────────────
    @Given("main categories exist in the database")
    public void mainCategoriesExistInDatabase() {
        categoryUserApiSteps.ensureMainCategoriesExist();
    }

    // ─── API_GET_MAIN_CATEGORIES_05 ───────────────
    @When("the user sends a GET request to retrieve main categories")
    public void theUserSendsGetRequestToRetrieveMainCategories() {
        categoryUserApiSteps.getMainCategories();
    }

    @Then("the response should contain only main categories")
    public void theResponseShouldContainOnlyMainCategories() {
        categoryUserApiSteps.verifyResponseContainsOnlyMainCategories();
    }
}
