package seleniumFirstTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SingleBoardPage {
	
	@FindBy (id="addButton-1")
	WebElement buttonAddToFirstColumn;
	
	@FindBy (id="cardContentInput")
	WebElement textAreaForCardContent;
	
	@FindBy (id="confirmCardEditing")
	WebElement buttonConfirmCardEditing;
	
	@FindBy (id="likeButton-1")
	WebElement likeButton1;
	
	@FindBy (id="likeButton-2")
	WebElement likeButton2;
	
	@FindBy (id="deleteButton-1")
	WebElement deleteButton1;
	
	@FindBy (id="editButton-1")
	WebElement editButton1;
	
	@FindBy (xpath="//*[@id=\"likeBadge-1\"]/span")
	WebElement likeNumberNote1;
	
	@FindBy (xpath="//*[@id='root']/div/div/div[2]/div[1]/div/div")
	WebElement firstNote;
	
	@FindBy (xpath="/html/body/div/div/div/div[2]/div[1]/div/div/div/div[2]/span/span")
	WebElement likeNumberFirstNote;
	
	@FindBy (id="confirmCardDeletion")
	WebElement confirmCardDeletion;
	
	@FindBy (xpath="//*[@id=\'card-1\']/div[1]/p")
	WebElement textFirstNote;
	
	@FindBy (id="card-1")
	WebElement card1;
	
	@FindBy (xpath="//*[contains(@id, 'boardCard')]")
	WebElement boardCard;
	
	@FindBy (css=".MuiDialog-container .MuiDialog-scrollPaper")
	WebElement muiDialogContainerScrollpaper;
	
	WebDriver driver;
	
	public SingleBoardPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public WebElement getBoardTitle() {
		return driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div[1]/h5"));
	}
	
	public WebElement getFirstNote() {
		return firstNote;
	}
	
	public void clickBtnNewNote() {
		buttonAddToFirstColumn.click();
	}
	
	public void typeInTextArea(String newText) {
		textAreaForCardContent.clear();
		textAreaForCardContent.sendKeys(newText);
	}
	
	public void submitTextAreaContent() {
		buttonConfirmCardEditing.click();
	}
	
	public void likeFirstNote() {
		likeButton1.click();
	}
	
	public void likeSecondNote() {
		likeButton2.click();
	}
	
	public void deleteFirstNote() {
		deleteButton1.click();
	}
	
	public void triggerEditNote() {
		editButton1.click();
	}
	
	public String getlikeNumber() {
		return likeNumberNote1.getText();
	}
	
	public void confirmCardDeletion() {
		confirmCardDeletion.click();
	}
	
	public String getTextFromFirstNote() {
		return textFirstNote.getText();
	}
	
	public void openCertainBoard(String url) {
		driver.get(url);
	}
	
	public WebElement getLikeNumberFirstNote() {
		return likeNumberFirstNote;
	}
}
