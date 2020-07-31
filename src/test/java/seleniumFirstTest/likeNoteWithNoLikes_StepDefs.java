package seleniumFirstTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;

import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import de.zifle.meintestprojekt.db.DbCommands;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import seleniumFirstTest.RegisterTests;

public class likeNoteWithNoLikes_StepDefs extends TestBase{
	public likeNoteWithNoLikes_StepDefs() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	LoginPage loginPage = new LoginPage(driver);
	LoginTests loginTests = new LoginTests();
	OverviewOfBoards overviewOfBoards = new OverviewOfBoards(driver);
	SingleBoardPage singleBoardPage = new SingleBoardPage(driver);
	SingleBoardPageTests singleBoardPageTests = new SingleBoardPageTests();
	LoginExistingUser_StepDefs loginExistingUserStepDefs = new LoginExistingUser_StepDefs();
	YarbApi yarbApi = YarbApi.getInstance();
	
	@Given("I open the loginPage_likesNotes")
	public void i_open_the_loginPage() throws SQLException, IOException {
		super.before();
	}
	
	@And("User is created in Restapi_likesNotes")
	public void user_is_created_in_Restapi() throws Throwable {
		yarbApi.createUserAndloginWithApi(super.testUsername, super.testPassword);
	}
	
	@When("I type Username_likesNotes")
	public void i_type_Username() {
		loginPage.typeUsername(super.testUsername);
	}
	
	@And("I type password_likesNotes")
	public void i_type_password() {
		loginPage.typePassword(super.testPassword);
	}
	
	@And("I click on button to confirm_likesNotes")
	public void i_click_on_button_to_confirm() {
		loginPage.submitForm();
	}
	
	@Then("I am on the board overview_likesNotes")
	public void i_am_on_the_boardoverview() {
		super.waitForBoardOverview();
	}
	
	@When("I open new board formular")
	public void i_open_new_board_formular(){
		overviewOfBoards.openNewBoardFormular();
	}
	
	@And("I set board title")
	public void i_set_boardtitle() {
		overviewOfBoards.setBoardTitle("hallo");
		overviewOfBoards.typeTabAfterBoardTitle();
	}
	
	@And("I set board column input0")
	public void i_set_board_column_input0() {
		overviewOfBoards.setBoardColumnInput0("hallo");
	}
	
	@And("I set board column input1")
	public void i_set_board_column_iput1() {
		overviewOfBoards.setBoardColumnInput1("hallo");
	}
	
	@And("I set board column input2")
	public void i_set_board_column_iput2() {
		overviewOfBoards.setBoardColumnInput2("hallo");
	}
	
	@And("I confirm board creation")
	public void i_confirm_board_creation() {
		overviewOfBoards.confirmBoardCreation();
	}
	
	@Then("There should be a new board")
	public void there_should_be_a_new_board() {
		waitFor(Duration.ofSeconds(10), (ExpectedConditions.visibilityOf(singleBoardPage.likeButton1)));
	}
	
	@Given("I open Board One")
	public void openBoardOne() {
		overviewOfBoards.openBoardOne();
	}
	
	@When("I click on button new note")
	public void i_click_on_button_new_note() {
		singleBoardPageTests.createNewNoteInFormular();
	}
	
	@And("I type in text area neu")
	public void typeintextarea() {
		singleBoardPage.typeInTextArea("neu");
	}
	
	@Then("Note should be visible")
	public void noteShouldBeVisible() {
		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOf(singleBoardPage.card1));
	}
	
	@When("I like first note")
	public void i_like_first_note1() {
		singleBoardPage.likeFirstNote();
	}
	
	@Then("like should be 1")
	public void like_should_be_1() {
		waitFor(Duration.ofSeconds(10),
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"likeButton-1\"]/span")));

		String numberOfLikes = singleBoardPage.getlikeNumber();
		Assert.assertEquals("1", numberOfLikes);
	}
	
	
}