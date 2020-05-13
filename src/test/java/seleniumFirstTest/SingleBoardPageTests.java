package seleniumFirstTest;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import de.zifle.meintestprojekt.model.Board;
import de.zifle.meintestprojekt.model.BoardColumn;

public class SingleBoardPageTests extends TestBase {
	
	private static final String specificboardurl = "specificboardurl";

	public SingleBoardPageTests() throws IOException {
		super();
	}

	
	@Test
	public void boardTitleLessThanThreeCharacters() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			String boardTitle = "D";
		
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			boardPage.openNewBoardFormular();
			boardPage.setBoardTitle(boardTitle);
			boardPage.typeTabAfterBoardTitle();
			
			String errorMessage = waitForText(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("boardTitleInput-helper-text")));
			
			Assert.assertEquals("Must contain at least 3 characters", errorMessage);
			
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void column0IsEmpty() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			String boardTitle = "Ddddddd";
			
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			boardPage.openNewBoardFormular();
			boardPage.setBoardTitle(boardTitle);
			boardPage.setBoardColumnInput0("");
			
			String errorMessage = waitForText(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("boardColumnInput0-helper-text")));
			
			Assert.assertEquals("Must not be empty", errorMessage);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void deleteColumn0() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			boardPage.openNewBoardFormular();
			boardPage.deleteColumn0();
			
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			int lastLabel = driver.findElements(By.id("boardColumnInput3-label")).size();

			Assert.assertEquals(0, lastLabel);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void deleteOneColumnAndAddTwoColumns() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			boardPage.openNewBoardFormular();
			boardPage.deleteColumn0();
			boardPage.deleteColumn0();
			boardPage.addBoardColumn();
			boardPage.addBoardColumn();
			
			int lastLabelSize = driver.findElements(By.id("boardColumnInput3-label")).size();
			
			Assert.assertEquals(1, lastLabelSize);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void cancelBoardCreation() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			boardPage.openNewBoardFormular();
			boardPage.cancelBoardCreation();
			
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			
			int sizeBoardFormulars = driver.findElements(By.xpath("//div[@class='MuiPaper-root MuiPaper-elevation24 MuiDialog-paper MuiDialog-paperScrollPaper MuiDialog-paperWidthSm MuiDialog-paperFullWidth MuiPaper-rounded']")).size();
			
			Assert.assertEquals(0, sizeBoardFormulars);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	
	@Test
	public void createNewBoardTest() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			createNewBoard(boardPage, "Der heutige tag");
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'boardCard')]")));
			
			int sizeOfBoards = driver.findElements(By.xpath("//*[contains(@id, 'boardCard')]")).size();
			
			Assert.assertEquals((1), sizeOfBoards);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void takeNewNoteInFirstColumnIsValid() throws Throwable {
		try {
			loginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			takeNewNoteInFirstColumn();
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void takeNewNoteInFirstColumn() throws Throwable {
		Board board1 = prepareBoardByApi();
		
		driver.get(properties.getProperty(specificboardurl) + board1.getId());
		
		SingleBoardPage editBoardPage = new SingleBoardPage(driver);
				
		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("addButton-1")));
		
		editBoardPage.clickBtnNewNote();
		editBoardPage.typeInTextArea("neu");
		editBoardPage.submitTextAreaContent();
		
		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("card-1")));
	}
	
	public void createNewBoard(OverviewOfBoards boardPage, String boardTitle) {
		boardPage.openNewBoardFormular();
		boardPage.setBoardTitle(boardTitle);
		boardPage.setBoardColumnInput0("bunt");
		boardPage.setBoardColumnInput1("xy");
		boardPage.setBoardColumnInput2("xwwwwy");
		boardPage.setBoardColumnInput3("xfdsfsdgy");
		boardPage.confirmBoardCreation();
	}
	
	@Test
	public void likeNoteWithNoLikes() throws Throwable {
		try {
			
			loginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			createNewBoard(boardPage, "Der heutige Taaaag");
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'openBoardButton')]")));
			boardPage.openBoardOne();
			
			SingleBoardPage editBoardPage = new SingleBoardPage(driver);
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'addButton')]")));
			editBoardPage.clickBtnNewNote();
			editBoardPage.typeInTextArea("neu");
			editBoardPage.submitTextAreaContent();
			
			waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("card-1")));
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".MuiDialog-container .MuiDialog-scrollPaper")));
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.id("likeButton-1")));
			
			editBoardPage.likeFirstNote();
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"likeButton-1\"]/span")));
			
			String numberOfLikes = editBoardPage.getlikeNumber();
			
			Assert.assertEquals("1", numberOfLikes);
		} catch(Throwable t) {
			handleThrowable(t);
		}
		Thread.sleep(3000);
	}
	
	@Test
	public void likeNoteWithOneLike() throws Throwable {
		try {
			loginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			
			Board board1 = prepareBoardByApi();
			BoardColumn column1 = board1.getColumns().get(0);
			Integer noteId = api.createNote(column1, "hallo");
			api.setVote(noteId);
			
			driver.get(properties.getProperty(specificboardurl) + board1.getId());
			
			SingleBoardPage editBoardPage = new SingleBoardPage(driver);
			
			waitFor(Duration.ofSeconds(10),(ExpectedConditions.visibilityOfElementLocated(By.id("likeButton-1"))));
			
			editBoardPage.likeButton1.click();
			
			String text = waitForText(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiBadge-badge .MuiBadge-anchorOriginTopRightRectangle .jss1154 MuiBadge-colorPrimary")));
			
			Assert.assertEquals("2", text);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void likeNoteWith10Likes() throws Throwable {
		try {
			SingleBoardPage editBoardPage = organisateBoardAndLikesTilWaiting();
			int likeNumberBefore = getTextLikeNumberInInt(editBoardPage);
			editBoardPage.likeButton1.click();
			int likeNumberAfter = getTextLikeNumberInInt(editBoardPage);
			Assert.assertTrue(likeNumberBefore < likeNumberAfter);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void deleteNote() throws Throwable {
		try {
			loginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			
			takeNewNoteInFirstColumn();
			
			SingleBoardPage editBoardPage = new SingleBoardPage(driver);
			waitFor(Duration.ofSeconds(60), ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".MuiDialog-container .MuiDialog-scrollPaper")));
			waitFor(Duration.ofSeconds(60), ExpectedConditions.visibilityOfElementLocated(By.id("deleteButton-1")));
			editBoardPage.deleteFirstNote();
			
			waitFor(Duration.ofSeconds(60), ExpectedConditions.visibilityOfElementLocated(By.id("confirmCardDeletion")));
			editBoardPage.confirmCardDeletion();
			editBoardPage.getFirstNote();
			
			Assert.assertTrue(driver.findElements(By.xpath("//*[@id='root']/div/div/div[2]/div[1]/div/div")).isEmpty());
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	
	
	
	public void set10VotesForNoteInApi(Integer noteId) {
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
		api.setVote(noteId);
	}
	
	public Board prepareBoardByApi() {
		Integer boardId = api.createBoard("name", "eins", "zwei", "drei");
		return api.getBoard(boardId);
	}
	
	public int getTextLikeNumberInInt(SingleBoardPage editBoardPage) {
		return Integer.parseInt(editBoardPage.getLikeNumberFirstNote().getText());
	}
	
	@Test
	public void deleteLikeFromNote() throws Throwable {
		try {
			SingleBoardPage editBoardPage = organisateBoardAndLikesTilWaiting();
			editBoardPage.likeButton1.click();
			int likeNumberLiked = getTextLikeNumberInInt(editBoardPage);
			editBoardPage.likeButton1.click();
			int likeNumberAfter = getTextLikeNumberInInt(editBoardPage);
			Assert.assertTrue(likeNumberLiked > likeNumberAfter);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void organisateVotesInApi(Board board) {
		BoardColumn column1 = board.getColumns().get(0);
		Integer noteId = api.createNote(column1, "hallo");
		set10VotesForNoteInApi(noteId);
	}
	
	public SingleBoardPage organisateBoardAndLikesTilWaiting() throws Throwable {
		loginWithApi(testUsername, testPassword);
		login(testUsername, testPassword);
		Board board = prepareBoardByApi();
		organisateVotesInApi(board);
		driver.get(properties.getProperty(specificboardurl) + board.getId());
		
		SingleBoardPage editBoardPage = new SingleBoardPage(driver);
		waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.id("likeButton-1")));
		waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div[2]/div[1]/div/div/div/div[2]/span/span")));
				
		return editBoardPage;
	}
}
