package seleniumFirstTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterTests extends TestBase {
	
	private static LoginPage loginPage;
	private static RegisterPage registerPage;
	
	public RegisterTests() throws IOException {
		super();
	}
	
	@Override
	public void before() throws SQLException, IOException {
		super.before();
		loginPage = new LoginPage(driver);
		registerPage = new RegisterPage(driver);
	}

	@Test
	public void testRegisterWithNewUser() throws Throwable {
		try {
			loginPage.switchToRegister();
			fillRegisterForm("susiiii", "password12");
			waitForBoardOverview();
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void testRegisterWithExistingUser() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			loginPage.switchToRegister();
			fillRegisterForm(testUsername, testPassword);
			String errorMessage = waitForText(Duration.ofSeconds(2),ExpectedConditions.visibilityOfElementLocated(By.id(registerUsernameHelperText)));
		
			Assert.assertEquals("Username is already taken", errorMessage);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test 
	public void testInvalidRegisterNameMoreThan20Characters() throws Throwable {
		try {
			loginPage.switchToRegister();
			
			registerPage.typeUserName("aladdinunddiewunderlampe");
			registerPage.typeTABAfterUsername();
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.id(registerUsernameHelperText)));
			
			WebElement error = driver.findElement(By.id(registerUsernameHelperText));
			waitFor(Duration.ofSeconds(10), (ExpectedConditions.visibilityOf(error)));
			
			Assert.assertEquals("Must contain at most 20 characters", error.getText());
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test 
	public void testRegisterNameLessThan4Characters() throws Throwable {
		try {
			loginPage.switchToRegister();
			
			registerPage.typeUserName("al");
			registerPage.typeTABAfterUsername();
	
			String errorMessage = waitForText(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id(registerUsernameHelperText)));
			
			Assert.assertEquals("Must contain at least 4 characters", errorMessage);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test 
	public void testRegisterPasswordLessThan6Characters() throws Throwable {
		try {
			registerPage.switchToRegister();
			registerPage.typeUserName("alaaaaaaa");
			registerPage.typePassword("123");
			registerPage.typeTABAfterPW();
			
			String errorMessage = waitForText(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("registerPassword-helper-text")));
			
			Assert.assertEquals("Must contain at least 6 characters", errorMessage);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test 
	public void testRegisterPasswordsNotSame() throws Throwable {
		try {
			registerPage.switchToRegister();
			registerPage.typeUserName("alaaaaaa");
			registerPage.typePassword("123");
			registerPage.typePasswordRepetition("12333");
			registerPage.typeTABAfterPasswordRepetition();
			
			String errorMessage = waitForText(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("registerPasswordRepetition-helper-text")));
			
			Assert.assertEquals("Must be the same as password", errorMessage);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void fillRegisterForm(String username, String password) {
		registerPage.typeUserName(username);
		registerPage.typePassword(password);
		registerPage.typePasswordRepetition(password);
		registerPage.submitForm();
	}

}
