package seleniumFirstTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import de.zifle.meintestprojekt.model.Board;
import de.zifle.meintestprojekt.model.BoardColumn;
import de.zifle.meintestprojekt.model.BoardNote;
import de.zifle.meintestprojekt.model.UpdateBoardNote;

public class SingleBoardPageTests extends TestBase {

	private static final String specificboardurl = "specificboardurl";
	private static OverviewOfBoards boardPage;
	private static SingleBoardPage singleBoardPage;

	public SingleBoardPageTests() throws IOException {
		super();
	}
	
	@Override
	public void before() throws SQLException, IOException {
		super.before();
		boardPage = new OverviewOfBoards(driver);
		singleBoardPage = new SingleBoardPage(driver);
	}

	@Test
	public void boardTitleLessThanThreeCharacters() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);

			waitForBoardOverview();

			String boardTitle = "D";

			boardPage.openNewBoardFormular();
			boardPage.setBoardTitle(boardTitle);
			boardPage.typeTabAfterBoardTitle();

			String errorMessage = waitForText(Duration.ofSeconds(2),
					ExpectedConditions.visibilityOfElementLocated(By.id("boardTitleInput-helper-text")));

			Assert.assertEquals("Must contain at least 3 characters", errorMessage);

		} catch (Throwable t) {
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
			boardPage.openNewBoardFormular();
			boardPage.setBoardTitle(boardTitle);
			boardPage.setBoardColumnInput0("");

			String errorMessage = waitForText(Duration.ofSeconds(2),
					ExpectedConditions.visibilityOfElementLocated(By.id("boardColumnInput0-helper-text")));

			Assert.assertEquals("Must not be empty", errorMessage);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void deleteColumn0() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);

			waitForBoardOverview();
			boardPage.openNewBoardFormular();
			boardPage.deleteColumn0();

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			NoSuchElementException noSuchElementException = null;
			
			try {
				boardPage.boardColumnInput3Label.isDisplayed();
			} catch (NoSuchElementException e) {
				noSuchElementException = e;
			}

			Assert.assertNotNull(noSuchElementException);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void deleteOneColumnAndAddTwoColumns() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);

			waitForBoardOverview();

			boardPage.openNewBoardFormular();
			boardPage.deleteColumn0();
			boardPage.deleteColumn0();
			boardPage.addBoardColumn();
			boardPage.addBoardColumn();
			
			Assert.assertTrue(boardPage.boardColumnInput3Label.isDisplayed());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void cancelBoardCreation() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);

			waitForBoardOverview();

			boardPage.openNewBoardFormular();
			boardPage.cancelBoardCreation();

			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			
			NoSuchElementException noSuchElementException = null;
			
			try {
				boardPage.boardFormular.isDisplayed();
			} catch (NoSuchElementException e) {
				noSuchElementException = e;
			}

			Assert.assertNotNull(noSuchElementException);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void createNewBoardTest() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);

			waitForBoardOverview();

			createNewBoard(boardPage, "Der heutige tag");

			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOf(boardPage.boardCard));
			
			Assert.assertTrue(boardPage.boardCard.isDisplayed());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void takeNewNoteInFirstColumnIsValid() throws Throwable {
		try {
			api.createUserAndloginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			takeNewNoteInFirstColumn();
		} catch (Throwable t) {
			handleThrowable(t);
		}
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
	public void seeLikeFromOtherUser() throws Throwable {
		try {
			api.createUserAndloginWithApi("sfdsgrdshr", "sgdhzrdh");
			Board board1 = prepareBoardByApi();
			BoardColumn column1 = board1.getColumns().get(0);
			Integer noteId = api.createNote(column1, "hallo");

			driver.get(properties.getProperty(specificboardurl) + board1.getId());
	
			waitFor(Duration.ofSeconds(10), (ExpectedConditions.visibilityOf(singleBoardPage.likeButton1)));

			Assert.assertEquals("0", singleBoardPage.likeNumberNote1.getText());

			api.setVote(noteId);

			waitFor(Duration.ofSeconds(10),
					ExpectedConditions.textToBePresentInElement(singleBoardPage.likeNumberNote1, "1"));
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void likeNoteWithNoLikes() throws Throwable {
		try {

			api.createUserAndloginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);

			createNewBoard(boardPage, "Der heutige Taaaag");
			waitFor(Duration.ofSeconds(10),
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'openBoardButton')]")));
			boardPage.openBoardOne();

			createNewNoteInFormular();

			waitFor(Duration.ofSeconds(10), (ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"likeButton-1\"]/span"))));

			singleBoardPage.likeFirstNote();

			waitFor(Duration.ofSeconds(10),
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"likeButton-1\"]/span")));

			String numberOfLikes = singleBoardPage.getlikeNumber();

			Assert.assertEquals("1", numberOfLikes);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void createNewNoteInFormular() {
		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("addButton-1")));

		singleBoardPage.clickBtnNewNote();
		singleBoardPage.typeInTextArea("neu");
		singleBoardPage.submitTextAreaContent();

		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOf(singleBoardPage.card1));
	}
	
	public void takeNewNoteInFirstColumn() throws Throwable {
		Board board1 = prepareBoardByApi();

		driver.get(properties.getProperty(specificboardurl) + board1.getId());
		
		createNewNoteInFormular();
	}

	@Test
	public void likeNoteWithOneLike() throws Throwable {
		try {
			api.createUserAndloginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);

			Board board1 = prepareBoardByApi();
			BoardColumn column1 = board1.getColumns().get(0);
			Integer noteId = api.createNote(column1, "hallo");
			api.setVote(noteId);

			driver.get(properties.getProperty(specificboardurl) + board1.getId());

			waitFor(Duration.ofSeconds(10), (ExpectedConditions.visibilityOf(singleBoardPage.likeButton1)));

			singleBoardPage.likeButton1.click();

			String text = waitForText(Duration.ofSeconds(10),
					ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
							".MuiBadge-badge .MuiBadge-anchorOriginTopRightRectangle .jss1154 MuiBadge-colorPrimary")));

			Assert.assertEquals("2", text);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void likeNoteWith10Likes() throws Throwable {
		try {
			SingleBoardPage singleBoardPage = organisateBoardAndLikesTilWaiting();
			int likeNumberBefore = getTextLikeNumberInInt(singleBoardPage);
			singleBoardPage.likeButton1.click();
			int likeNumberAfter = getTextLikeNumberInInt(singleBoardPage);
			Assert.assertTrue(likeNumberBefore < likeNumberAfter);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void deleteNote() throws Throwable {
		try {
			api.createUserAndloginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);

			takeNewNoteInFirstColumn();

			waitFor(Duration.ofSeconds(60), ExpectedConditions.visibilityOfElementLocated(By.id("deleteButton-1")));
			singleBoardPage.deleteFirstNote();

			waitFor(Duration.ofSeconds(60),
					ExpectedConditions.visibilityOfElementLocated(By.id("confirmCardDeletion")));
			singleBoardPage.confirmCardDeletion();
			singleBoardPage.getFirstNote();
			
			// check if grid has no notices
			Assert.assertTrue(driver.findElements(By.xpath("//*[@id='root']/div/div/div[2]/div[1]/div/div")).isEmpty());
		} catch (Throwable t) {
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

	public int getTextLikeNumberInInt(SingleBoardPage singleBoardPage) {
		return Integer.parseInt(singleBoardPage.getLikeNumberFirstNote().getText());
	}

	@Test
	public void deleteLikeFromNote() throws Throwable {
		try {
			SingleBoardPage singleBoardPage = organisateBoardAndLikesTilWaiting();
			singleBoardPage.likeButton1.click();
			int likeNumberLiked = getTextLikeNumberInInt(singleBoardPage);
			singleBoardPage.likeButton1.click();
			int likeNumberAfter = getTextLikeNumberInInt(singleBoardPage);
			Assert.assertTrue(likeNumberLiked > likeNumberAfter);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public Boolean isNoteDeleted(BoardColumn column1, Integer noteID) {
		List<BoardNote> notes = column1.getNotes();
		
		for (BoardNote note : notes) {
			if(note.getId().equals(noteID)) {
				return false;
			}
		}
		return true;
	}
	
	@Test
	public void seeHowUserCreatesNote() throws Throwable {
		try {
			api.createUserAndloginWithApi("sfdsgrdshr", "sgdhzrdh");
			Board board1 = prepareBoardByApi();							// board
			BoardColumn column1 = board1.getColumns().get(0);			// spalte
			
			driver.get(properties.getProperty(specificboardurl) + board1.getId());
			
			Integer noteId = api.createNote(column1, "hallo");			// noteID
			
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(
					By.xpath("/html/body/div/div/div/div[2]/div[1]/div/div/div/div[2]/span/span")));
			
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void seeHowOtherUserDeletesNote() throws Throwable {
		try {
			api.createUserAndloginWithApi("sfdsgrdshr", "sgdhzrdh");
			Board board1 = prepareBoardByApi();							// board
			BoardColumn column1 = board1.getColumns().get(0);			// spalte
			Integer noteId = api.createNote(column1, "hallo");			// noteID

			driver.get(properties.getProperty(specificboardurl) + board1.getId());
			api.deleteNote(noteId);
			
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			Boolean noteIsDeleted = isNoteDeleted(column1, noteId);
			
			Assert.assertTrue(noteIsDeleted == true);
			
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void seeHowOtherUserEditesNote() throws Throwable {
		try {
			api.createUserAndloginWithApi("sfdsgrdshr", "sgdhzrdh");
			Board board1 = prepareBoardByApi();							// board
			BoardColumn column1 = board1.getColumns().get(0);			// spalte
			Integer noteId = api.createNote(column1, "hallo");			// noteID

			driver.get(properties.getProperty(specificboardurl) + board1.getId());

			UpdateBoardNote updateBoardNote = new UpdateBoardNote();
			UpdateBoardNote content = updateBoardNote.content("hallihallo");
			api.updateNote(noteId, content);
			
			Assert.assertEquals("hallihallo", updateBoardNote.getContent());
			
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void organisateVotesInApi(Board board) {
		BoardColumn column1 = board.getColumns().get(0);
		Integer noteId = api.createNote(column1, "hallo");
		set10VotesForNoteInApi(noteId);
	}

	public SingleBoardPage organisateBoardAndLikesTilWaiting() throws Throwable {
		api.createUserAndloginWithApi(testUsername, testPassword);
		login(testUsername, testPassword);
		Board board = prepareBoardByApi();
		organisateVotesInApi(board);
		driver.get(properties.getProperty(specificboardurl) + board.getId());
		
		waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(
				By.xpath("/html/body/div/div/div/div[2]/div[1]/div/div/div/div[2]/span/span")));

		return singleBoardPage;
	}
}
