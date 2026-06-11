package starter.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.rest.SerenityRest;
import starter.steps.PlantAdminApiSteps;
import starter.steps.PlantUserApiSteps;

import static org.assertj.core.api.Assertions.assertThat;

public class PlantApiStepDefinitions {

    @Steps
    PlantAdminApiSteps plantAdminApiSteps;

    @Steps
    PlantUserApiSteps plantUserApiSteps;

    // Admin API Steps
    @Given("the admin authentication token is valid")
    public void adminAuthenticationTokenIsValid() {
        plantAdminApiSteps.getAdminToken();
    }

    @Given("a valid sub-category ID exists")
    public void validSubCategoryIdExists() {
        plantAdminApiSteps.ensureSubCategoryExists();
    }

    @When("the admin sends a POST request to add a plant for the category")
    public void adminSendsPostRequestToAddPlant() {
        plantAdminApiSteps.sendPostRequestToAddPlant();
    }

    @Then("the API response status code should be {int} Created")
    public void apiResponseStatusCodeShouldBeCreated(int statusCode) {
        assertStatusCode(statusCode);
    }

    @Then("the API response status code should be {int} OK")
    public void apiResponseStatusCodeShouldBeOK(int statusCode) {
        assertStatusCode(statusCode);
    }

    @Then("the API response status code should be {int} No Content")
    public void apiResponseStatusCodeShouldBeNoContent(int statusCode) {
        assertStatusCode(statusCode);
    }

    @Then("the API response status code should be {int} Not Found")
    public void apiResponseStatusCodeShouldBeNotFound(int statusCode) {
        assertStatusCode(statusCode);
    }

    @Then("the API response status code should be {int} Forbidden")
    public void apiResponseStatusCodeShouldBeForbidden(int statusCode) {
        assertStatusCode(statusCode);
    }

    @Then("the plant record should be created successfully in the response")
    public void plantRecordShouldBeCreatedSuccessfully() {
        plantAdminApiSteps.verifyPlantRecordCreated();
    }

    @Given("a plant record exists in the database")
    public void plantRecordExistsInDatabase() {
        plantAdminApiSteps.ensurePlantRecordExists();
    }

    @When("the admin sends a GET request to retrieve the plant by ID")
    public void adminSendsGetRequestToRetrievePlant() {
        plantAdminApiSteps.sendGetRequestToRetrievePlant();
    }

    @Then("the response should contain the plant details")
    public void responseShouldContainPlantDetails() {
        plantAdminApiSteps.verifyResponseContainsPlantDetails();
    }

    @When("the admin sends a DELETE request for the plant ID")
    public void adminSendsDeleteRequestForPlantId() {
        plantAdminApiSteps.sendDeleteRequestForPlant();
    }

    @Then("the plant record should be completely removed from the database")
    public void plantRecordShouldBeCompletelyRemoved() {
        plantAdminApiSteps.verifyPlantRecordRemoved();
    }

    @Given("plant records exist under a specific category ID")
    public void plantRecordsExistUnderSpecificCategory() {
        plantAdminApiSteps.ensurePlantRecordsExistUnderCategory();
    }

    @When("the admin sends a GET request to filter plants by the category ID")
    public void adminSendsGetRequestToFilterPlantsByCategoryId() {
        plantAdminApiSteps.sendGetRequestToFilterPlantsByCategory();
    }

    @Then("the API response status code should be {int} or {int}")
    public void apiResponseStatusCodeShouldBeOr(int status1, int status2) {
        plantAdminApiSteps.verifyResponseStatusCodeIsOr(status1, status2);
    }

    @Given("target plant ID {int} does not exist in the database")
    public void targetPlantIdDoesNotExist(int id) {
        plantAdminApiSteps.ensureTargetPlantDoesNotExist();
    }

    @When("the admin sends a DELETE request for the plant ID {int}")
    public void adminSendsDeleteRequestForPlantId(int id) {
        plantAdminApiSteps.sendDeleteRequestForNonExistentPlant();
    }

    @Then("the non-existent plant record should remain absent from the database")
    public void nonExistentPlantShouldRemainAbsent() {
        plantAdminApiSteps.verifyNonExistentPlantRemainsAbsent();
    }

    @Then("the error payload should contain standard keys")
    public void errorPayloadShouldContainStandardKeys() {
        plantAdminApiSteps.verifyErrorPayloadContainsStandardKeys();
    }

    // User API Steps
    @Given("the user authentication token is valid")
    public void userAuthenticationTokenIsValid() {
        plantUserApiSteps.getUserToken();
    }

    @When("the user sends a POST request to add a plant for the category")
    public void userSendsPostRequestToAddPlant() {
        plantUserApiSteps.sendPostRequestToAddPlant();
    }

    @When("the user sends a PUT request to update the plant data")
    public void userSendsPutRequestToUpdatePlant() {
        plantUserApiSteps.sendPutRequestToUpdatePlant();
    }

    @When("the user sends a DELETE request for the plant ID")
    public void userSendsDeleteRequestForPlant() {
        plantUserApiSteps.sendDeleteRequestForPlant();
    }

    @When("the user sends a GET request to retrieve plant summary data")
    public void userSendsGetRequestToRetrievePlantSummary() {
        plantUserApiSteps.sendGetRequestToRetrievePlantSummary();
    }

    @When("the user sends a GET request for plants with page {string}")
    public void userSendsGetRequestWithPage(String page) {
        plantUserApiSteps.sendGetRequestWithPage(page);
    }

    @When("the user sends a GET request for plants with size {string}")
    public void userSendsGetRequestWithSize(String size) {
        plantUserApiSteps.sendGetRequestWithSize(size);
    }

    @Then("the system gracefully processes or rejects via HTTP 400")
    public void systemGracefullyProcessesOrRejects() {
        plantUserApiSteps.verifySystemGracefullyProcessesBoundaryConstraint();
    }

    private void assertStatusCode(int expectedStatusCode) {
        assertThat(SerenityRest.lastResponse().statusCode())
                .as("Response status code should be " + expectedStatusCode)
                .isEqualTo(expectedStatusCode);
    }
}
