package starter.stepdefinitions;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import starter.steps.SalesAdminApiSteps;

public class SalesAdminApiStepDefinitions {

    @Steps
    SalesAdminApiSteps salesAdminApiSteps;

    // ─── Background ───────────────────────────────────────────
    // "the admin authentication token is available"
    // handled by CommonApiStepDefinitions

    // ─── Setup ────────────────────────────────────────────────

    @Given("a valid plant exists with sufficient stock")
    public void aValidPlantExistsWithSufficientStock() {
        salesAdminApiSteps.createValidPlantWithSufficientStock();
    }

    @Given("a sale record exists")
    public void aSaleRecordExists() {
        salesAdminApiSteps.createSaleRecord();
    }

    // ─── API_POST_SELLPLANT_01 ────────────────────────────────

    @When("the admin sends a POST request to sell the plant with quantity 1")
    public void theAdminSendsPostRequestToSellPlantWithQuantity1() {
        salesAdminApiSteps.sellPlantWithValidQuantity();
    }

    @Then("the plant stock should be reduced")
    public void thePlantStockShouldBeReduced() {
        salesAdminApiSteps.verifyStockReduced();
    }

    // ─── API_POST_SELLPLANT_02 ────────────────────────────────

    @When("the admin sends a POST request to sell the plant with exceeded quantity")
    public void theAdminSendsPostRequestToSellPlantWithExceededQuantity() {
        salesAdminApiSteps.sellPlantWithExceededQuantity();
    }

    // ─── API_DELETE_SALE_03 ───────────────────────────────────

    @When("the admin sends a DELETE request to delete the sale")
    public void theAdminSendsDeleteRequestToDeleteTheSale() {
        salesAdminApiSteps.deleteSale();
    }

    // ─── API_POST_SELLPLANT_04 ────────────────────────────────

    @When("the admin sends a POST request to sell the plant with quantity 0")
    public void theAdminSendsPostRequestToSellPlantWithQuantity0() {
        salesAdminApiSteps.sellPlantWithZeroQuantity();
    }

    // ─── API_GET_SALES_05 ─────────────────────────────────────

    @When("the admin sends a GET request to retrieve all sales")
    public void theAdminSendsGetRequestToRetrieveAllSales() {
        salesAdminApiSteps.getAllSales();
    }

    @Then("the response should contain a valid sales list")
    public void theResponseShouldContainAValidSalesList() {
        // Assertion already done inside getAllSales()
    }
}