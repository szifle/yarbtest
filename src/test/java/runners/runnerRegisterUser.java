package runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)

@CucumberOptions(  features = "src/test/java/features/registerUserInFrontPage.feature",
                         glue = "src/test/java/seleniumFirstTest/")

public class runnerRegisterUser {
  //Run this from Maven or as JUnit
}

