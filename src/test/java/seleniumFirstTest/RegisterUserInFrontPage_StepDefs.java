package seleniumFirstTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import de.zifle.meintestprojekt.db.DbCommands;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import seleniumFirstTest.RegisterTests;

public class RegisterUserInFrontPage_StepDefs extends TestBase{
	public RegisterUserInFrontPage_StepDefs() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	RegisterPage registerPage = new RegisterPage(driver);
	
	@Given("User opens loginPage")
	public void user_opens_loginPage() throws SQLException, IOException {
		super.before();
	}
	
	@When("User clicks on button to register")
	public void user_clicks_on_button_to_register(){
		registerPage.switchToRegister();
	}
	
	@And("User types username")
	public void user_types_username() {
		registerPage.typeUserName(super.testUsername);
	}
	
	@And("User types password")
	public void user_types_password() {
		registerPage.typePassword(super.testPassword);
	}
	
	@And("User types passwordRepetition")
	public void user_types_passwordRepetition() {
		registerPage.typePasswordRepetition(super.testPassword);
	}
	
	@And("User confirms registration")
	public void user_confirms_registration() {
		registerPage.submitForm();
	}
	
	@Then("^User is on boardpage$")
	public void user_is_on_boardpage() {
		super.waitForBoardOverview();
	}

}