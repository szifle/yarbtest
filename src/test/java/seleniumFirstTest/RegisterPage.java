package seleniumFirstTest;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
	WebDriver driver;
	
	@FindBy (id="confirmLoginRegister")
	private WebElement buttonConfirm;
	
	@FindBy (id="switchLoginRegister")
	private WebElement switchLoginRegister;
	
	@FindBy (id="registerUsername")
	private WebElement inputUsername;
	
	@FindBy (id="registerPassword")
	private WebElement inputPw;
	
	@FindBy (id="registerPasswordRepetition")
	private WebElement inputPwRepetition;
	
	@FindBy (id="root")
	private WebElement rooot;
	
	public RegisterPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void registerUser(String userName, String password) throws Throwable {
		YarbApi api = new YarbApi();
		api.createUser(userName, password);
	}
	
	public void submitForm() {
		buttonConfirm.click();
	}
	
	public void clickOnRoot() {
		rooot.click();
	}
	
	public void switchToRegister() {
		switchLoginRegister.click();
	}
	
	public void typeUserName(String nickname) {
		inputUsername.sendKeys(nickname);
	}
	
	public void typeTABAfterUsername() {
		inputUsername.sendKeys(Keys.TAB);
	}
	
	public WebElement getInputUsername(){
		return inputUsername;
	}
	
	public void typePassword(String password) {
		inputPw.sendKeys(password);
	}
	
	public void typeTABAfterPW() {
		inputPw.sendKeys(Keys.TAB);
	}
	
	public void typePasswordRepetition(String passwordRepetition) {
		inputPwRepetition.sendKeys(passwordRepetition);
	}
	
	public void typeTABAfterPasswordRepetition() {
		inputPwRepetition.sendKeys(Keys.TAB);
	}
}
