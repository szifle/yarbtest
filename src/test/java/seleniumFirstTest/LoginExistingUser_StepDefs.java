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

public class LoginExistingUser_StepDefs extends TestBase{
	public LoginExistingUser_StepDefs() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	LoginPage loginPage = new LoginPage(driver);
	YarbApi yarbApi = YarbApi.getInstance();
	
	@Given("I open the loginPage")
	public void i_open_the_loginPage() throws SQLException, IOException {
		super.before();
	}
	
	@And("User is created in Restapi")
	public void user_is_created_in_Restapi() throws Throwable {
		yarbApi.createUserAndloginWithApi(super.testUsername, super.testPassword);
	}
	
	@When("I type Username")
	public void i_type_Username() {
		loginPage.typeUsername(super.testUsername);
	}
	
	@And("I type password")
	public void i_type_password() {
		loginPage.typePassword(super.testPassword);
	}
	
	@And("I click on button to confirm")
	public void i_click_on_button_to_confirm() {
		loginPage.submitForm();
	}
	
	@Then("I am on the board overview")
	public void i_am_on_the_boardoverview() {
		super.waitForBoardOverview();
	}

}