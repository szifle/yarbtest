package seleniumFirstTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BoardPage {
	@FindBy (id="createBoard")
	WebElement createBoardBTN;
	
	@FindBy (id="boardTitleInput")
	WebElement inputForTitle;
	
	@FindBy (id="boardColumnInput0")
	WebElement inputRow0;
	
	@FindBy (id="boardColumnInput1")
	WebElement inputRow1;
	
	@FindBy (id="boardColumnInput2")
	WebElement inputRow2;
	
	@FindBy (id="boardColumnInput3")
	WebElement inputRow3;
	
	@FindBy (id="confirmBoardCreation")
	WebElement confirmBoardCreationBTN;
	
	@FindBy (xpath="//*[@id=\"root\"]/div/div/header/div/button")
	WebElement logoutIcon;
	
	@FindBy (id="openBoardButton-1")
	WebElement openBoardButton1;
	
	@FindBy (id="openBoardButton-2")
	WebElement openBoardButton2;
	
	@FindBy (xpath="/html/body/div/div/div/div/div/div/ul[2]/div")
	WebElement navPointAbout;
	
	@FindBy (xpath="/html/body/div[2]/div[3]/div/div[2]/div[2]/div/div/button")
	WebElement deleteColumn0Button;
	
	@FindBy (id="addBoardColumn")
	WebElement addBoardColumnButton;
	
	@FindBy (id="cancelBoardCreation")
	WebElement buttonCancelBoardCreation;
	
	WebDriver driver;
	
	public BoardPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void logout() {
		logoutIcon.click();
	}
	
	public void openBoardOne() {
		Actions actions = new Actions(driver);
		actions.moveToElement(openBoardButton1).click().perform();
	}
	
	public void openBoardTwo() {
		openBoardButton2.click();
	}
	
	public void openAboutPage() {
		navPointAbout.click();
	}
	
	public void openNewBoardFormular() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		wait.until(ExpectedConditions.urlContains("/user/boards"));
		createBoardBTN.click();
	}
	
	public void waitUntilFormularIsOpen() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2)); 
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[@class='MuiPaper-root MuiPaper-elevation24 MuiDialog-paper MuiDialog-paperScrollPaper MuiDialog-paperWidthSm MuiDialog-paperFullWidth MuiPaper-rounded']")));
	}
	
	public void setBoardTitle(String title) {
		inputForTitle.clear();
		inputForTitle.sendKeys(title);
	}
	
	public void setBoardColumnInput0(String input) {
		inputRow0.clear();
		inputRow0.sendKeys(input);
	}
	
	public void setBoardColumnInput1(String input) {
		inputRow1.clear();
		inputRow1.sendKeys(input);
	}
	
	public void setBoardColumnInput2(String input) {
		inputRow2.clear();
		inputRow2.sendKeys(input);
	}
	
	public void setBoardColumnInput3(String input) {
		inputRow3.clear();
		inputRow3.sendKeys(input);
	}
	
	public void confirmBoardCreation() {
		confirmBoardCreationBTN.click();
	}
	
	public void typeTabAfterBoardTitle() {
		inputForTitle.sendKeys(Keys.TAB);
	}
	
	public void deleteColumn0() {
		deleteColumn0Button.click();
	}
	
	public WebElement getDeleteColumnButton0 () {
		return deleteColumn0Button;
	}
	
	public void addBoardColumn() {
		addBoardColumnButton.click();
	}
	
	public void cancelBoardCreation() {
		buttonCancelBoardCreation.click();
	}
}
