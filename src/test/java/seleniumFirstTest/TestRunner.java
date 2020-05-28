package seleniumFirstTest;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions (monochrome = true, 
					features = "src/test/java/features/registerUserInFrontPage.feature", 
					glue = "src/test/java/seleniumFirstTest/",
					dryRun = false)

public class TestRunner {
	
}
