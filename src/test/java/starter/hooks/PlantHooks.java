package starter.hooks;

import io.cucumber.java.Before;
import starter.steps.PlantAdminApiSteps;

public class PlantHooks {

    @Before("@UI_PLANTATIONPAGE_ADMIN_05")
    public void seedEditTargetPlantForAdmin05() {
        PlantAdminApiSteps apiSteps = new PlantAdminApiSteps();
        apiSteps.prepareEditTargetPlantForUiEditTest();
        System.out.println("[PlantHooks] Seeded edit target plant '"
                + PlantAdminApiSteps.SEARCH_PLANT_NAME + "' for ADMIN_05");
    }
}
