package starter;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        plugin = {"pretty", "net.serenitybdd.cucumber.core.plugin.SerenityReporterParallel", "timeline:build/test-results/timeline"},
        features = "src/test/resources/features",
        glue = "starter"
)
public class CucumberTestSuite extends AbstractTestNGCucumberTests {
}
