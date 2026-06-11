package starter.stepdefinitions;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import starter.steps.SalesUserApiSteps;

public class SalesUserApiStepDefinitions {

    @Steps
    SalesUserApiSteps salesUserApiSteps;

    // ─── Background ───────────────────────────────────────────

    // ─── Setup ────────────────────────────────────────────────

    @Given("a valid plant id exists")
    public void aValidPlantIdExists() {
        salesUserApiSteps.ensureValidPlantExists();
    }

    @Given("a valid sale id exists")
    public void aValidSaleIdExists() {
        salesUserApiSteps.ensureValidSaleExists();
    }

    // ─── API_GET_PAGINATEDSALES_01 ────────────────────────────

    @When("the user sends a GET request to retrieve paginated sales")
    public void theUserSendsGetRequestToRetrievePaginatedSales() {
        salesUserApiSteps.getPaginatedSales();
    }

    @Then("the response should contain paginated sales data")
    public void theResponseShouldContainPaginatedSalesData() {
        // Assertions already done inside getPaginatedSales()
    }

    // ─── API_POST_SELLPLANT_02 ────────────────────────────────

    @When("the user sends a POST request to sell a plant")
    public void theUserSendsPostRequestToSellAPlant() {
        salesUserApiSteps.sellPlantAsUser();
    }

    // ─── API_DELETE_SALE_03 ───────────────────────────────────

    @When("the user sends a DELETE request to delete a sale")
    public void theUserSendsDeleteRequestToDeleteASale() {
        salesUserApiSteps.deleteSaleAsUser();
    }

    // ─── API_GET_SALES_04 ─────────────────────────────────────

    @When("the user sends a GET request to retrieve a sale by id")
    public void theUserSendsGetRequestToRetrieveSaleById() {
        salesUserApiSteps.getSaleById();
    }

    @Then("the response should contain valid sale details")
    public void theResponseShouldContainValidSaleDetails() {
        // Assertions already done inside getSaleById()
    }

    // ─── API_GET_PAGINATEDSALES_02 ────────────────────────────

    @When("the user sends a GET request to retrieve paginated sales sorted by {string} in {string} order")
    public void theUserSendsGetRequestForPaginatedSalesSorted(String sortField, String sortDir) {
        salesUserApiSteps.getPaginatedSalesSorted(sortField, sortDir);
    }

}