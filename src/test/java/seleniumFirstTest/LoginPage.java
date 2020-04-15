package seleniumFirstTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
	@FindBy (id="username")
	private WebElement usernameInput;
	
	@FindBy (id="password")
	private WebElement passwordInput;
	
	@FindBy (id="confirmLoginRegister")
	private WebElement buttonConfirm;
	
	@FindBy (id="switchLoginRegister")
	private WebElement switchLoginRegister;
	
	public LoginPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	public void typeUsername(String nickname) {
		usernameInput.sendKeys(nickname);
	}
	
	public void typePassword(String password) {
		passwordInput.sendKeys(password);
	}
	
	public void submitForm() {
		buttonConfirm.click();
	}
	
	public void switchToRegister() {
		switchLoginRegister.click();
	}
}
