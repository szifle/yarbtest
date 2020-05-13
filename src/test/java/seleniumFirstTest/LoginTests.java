package seleniumFirstTest;

import java.io.IOException;
import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginTests extends TestBase {

	public LoginTests() throws IOException {
		super();
	}
	
	@Test
	public void testLoginSuccess() throws Throwable {
		try {
			loginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			waitForBoardOverview();
		} catch (Throwable t) {
			super.handleThrowable(t);
		}
	}
	
	@Test
	public void testLoginWithInvalidCredentials() throws Throwable {
		try {
			login(testUsername, testPassword);
			String errorText = waitForText(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("password-helper-text")));
			Assert.assertEquals(errorText, "Username or password is wrong");
		} catch (Throwable t) {
			super.handleThrowable(t);
		}
	}
	
	public void login(String username, String password) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.typeUsername(username);
		loginPage.typePassword(password);
		loginPage.submitForm();
	}

}
