package seleniumFirstTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditBoardPage {
	
	WebDriver driver;
	
	@FindBy (id="addButton-5")
	WebElement buttonAddToFirstColumn;
	
	@FindBy (id="addButton-6")
	WebElement buttonAddToSecondColumn;
	
	@FindBy (id="addButton-7")
	WebElement buttonAddToThirdColumn;
	
	@FindBy (id="addButton-8")
	WebElement buttonAddToFourthColumn;
	
	@FindBy (id="cardContentInput")
	WebElement textAreaForCardContent;
	
	@FindBy (id="confirmCardEditing")
	WebElement buttonConfirmCardEditing;
	
	@FindBy (className="MuiTypography-root jss1729 MuiTypography-h5 MuiTypography-colorTextSecondary MuiTypography-alignCenter")
	WebElement boardTitle;
	
	@FindBy (id="likeButton-1")
	WebElement likeButton1;
	
	public EditBoardPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement getBoardTitle() {
		return boardTitle;
	}
	
	public void addNoteToFirstSheet() {
		buttonAddToFirstColumn.click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[3]")));
	}
	
	public void addNoteToSecondSheet() {
		buttonAddToSecondColumn.click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[3]")));
	}
	
	public void typeInTextArea(String newText) {
		textAreaForCardContent.sendKeys(newText);
	}
	
	public void submitTextAreaContent() {
		buttonConfirmCardEditing.click();
	}
	
	public void likeNote() {
		likeButton1.click();
	}
}
