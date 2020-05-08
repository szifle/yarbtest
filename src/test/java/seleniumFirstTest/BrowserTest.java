package seleniumFirstTest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.zifle.meintestprojekt.db.DbCommands;
import de.zifle.meintestprojekt.model.Board;
import de.zifle.meintestprojekt.model.BoardColumn;

public class BrowserTest {

	private WebDriver driver;
	private YarbApi api = new YarbApi();
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSSS");
	private Properties properties = new Properties();
	private static final String specificboardurl = "specificboardurl";
	private static final String testUsername = "susisusi";
	private static final String testPassword = "huchhuch";
	private static final String registerUsernameHelperText = "registerUsername-helper-text";

	public BrowserTest() throws IOException {
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("textfiles\\props.txt"));
		properties.load(stream);
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(false);
		System.setProperty("webdriver.gecko.driver", properties.getProperty("driverpath"));
		driver = new FirefoxDriver(options);
	}

	@Before
	public void before() throws SQLException, IOException {
		DbCommands.dropAndCreateYarbDB();
		driver.get(properties.getProperty("defaulturl"));
	}

	@After
	public void after() {
		driver.close();
	}

	@Test
	public void testLoginSuccess() throws Throwable {
		try {
			loginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			waitForBoardOverview();
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void testLoginWithInvalidCredentials() throws Throwable {
		try {
			login(testUsername, testPassword);
			String errorText = waitForText(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("password-helper-text")));
			Assert.assertEquals(errorText, "Username or password is wrong");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void logoutUser() throws Throwable {
		try {
			loginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			BoardPage boardPage = new BoardPage(driver);
			boardPage.logout();
			
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			
			String currentUrl = driver.getCurrentUrl();
			
			Assert.assertEquals(properties.getProperty("loginurl"), currentUrl);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void testRegisterWithNewUser() throws Throwable {
		try {
			LoginPage loginPage = new LoginPage(driver);
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
			
			LoginPage loginPage = new LoginPage(driver);
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
			LoginPage loginPage = new LoginPage(driver);
			loginPage.switchToRegister();
			
			RegisterPage registerPage = new RegisterPage(driver);
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
			LoginPage loginPage = new LoginPage(driver);
			loginPage.switchToRegister();
			
			RegisterPage registerPage = new RegisterPage(driver);
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
			RegisterPage registerPage = new RegisterPage(driver);
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
			RegisterPage registerPage = new RegisterPage(driver);
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
	
	@Test
	public void boardTitleLessThanThreeCharacters() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			String boardTitle = "D";
		
			BoardPage boardPage = new BoardPage(driver);
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
			
			BoardPage boardPage = new BoardPage(driver);
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
			
			BoardPage boardPage = new BoardPage(driver);
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
			
			BoardPage boardPage = new BoardPage(driver);
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
			
			BoardPage boardPage = new BoardPage(driver);
			boardPage.openNewBoardFormular();
			boardPage.cancelBoardCreation();
			
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			
			int sizeBoardFormulars = driver.findElements(By.xpath("//div[@class='MuiPaper-root MuiPaper-elevation24 MuiDialog-paper MuiDialog-paperScrollPaper MuiDialog-paperWidthSm MuiDialog-paperFullWidth MuiPaper-rounded']")).size();
			
			Assert.assertEquals(0, sizeBoardFormulars);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void createMultipleBoardsInApi() {
		
	}

	@Test
	public void createMultipleBoards() throws Throwable {
		
		try {
			api.createUser(testUsername, testPassword);
			api.login(testUsername, testPassword);
			createMultipleBoardsInApi();
			
			login(testUsername, testPassword);
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'boardCard')]")));
			
			int sizeOfBoards = driver.findElements(By.xpath("//*[contains(@id, 'boardCard')]")).size();
			
			Assert.assertEquals(10, sizeOfBoards);
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
			
			BoardPage boardPage = new BoardPage(driver);
			createNewBoard(boardPage, "Der heutige tag");
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'boardCard')]")));
			
			int sizeOfBoards = driver.findElements(By.xpath("//*[contains(@id, 'boardCard')]")).size();
			
			Assert.assertEquals((1), sizeOfBoards);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void openAboutPage() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			BoardPage boardPage = new BoardPage(driver);
			boardPage.openAboutPage();
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.urlMatches(properties.getProperty("abouturl")));
			
			String url = driver.getCurrentUrl();
			
			Assert.assertEquals(properties.getProperty("abouturl"), url);
		} catch (Throwable t) {
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
		BoardPage boardPage = new BoardPage(driver);
		prepareBoardByApi();
		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'boardCard')]")));
		boardPage.openBoardOne();
		EditBoardPage editBoardPage = new EditBoardPage(driver);
				
		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("addButton-1")));
		editBoardPage.clickBtnNewNote();
		editBoardPage.typeInTextArea("neu");
		editBoardPage.submitTextAreaContent();
		
		waitFor(Duration.ofSeconds(2), ExpectedConditions.visibilityOfElementLocated(By.id("card-1")));
	}
	
	public void createNewBoard(BoardPage boardPage, String boardTitle) {
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
			
			BoardPage boardPage = new BoardPage(driver);
			createNewBoard(boardPage, "Der heutige Taaaag");
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'openBoardButton')]")));
			boardPage.openBoardOne();
			
			EditBoardPage editBoardPage = new EditBoardPage(driver);
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
	
	public EditBoardPage organisateBoardAndLikesTilWaiting() throws Throwable {
		loginWithApi(testUsername, testPassword);
		login(testUsername, testPassword);
		organisateVotesInApi();
		driver.get(properties.getProperty(specificboardurl) + prepareBoardByApi().getId());
		
		EditBoardPage editBoardPage = new EditBoardPage(driver);
		waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.id("likeButton-1")));
		waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div[2]/div[1]/div/div/div/div[2]/span/span")));
				
		return editBoardPage;
	}
	
	public void organisateVotesInApi() {
		BoardColumn column1 = prepareBoardByApi().getColumns().get(0);
		Integer noteId = api.createNote(column1, "hallo");
		set10VotesForNoteInApi(noteId);
	}
	
	public Board prepareBoardByApi() {
		Integer boardId = api.createBoard("name", "eins", "zwei", "drei");
		return api.getBoard(boardId);
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
			
			EditBoardPage editBoardPage = new EditBoardPage(driver);
			
			waitFor(Duration.ofSeconds(10),(ExpectedConditions.visibilityOfElementLocated(By.id("likeButton-1"))));
			
			editBoardPage.likeButton1.click();
			
			String text = waitForText(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiBadge-badge .MuiBadge-anchorOriginTopRightRectangle .jss1154 MuiBadge-colorPrimary")));
			
			Assert.assertEquals("2", text);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void likeNoteWith10Like() throws Throwable {
		try {
			EditBoardPage editBoardPage = organisateBoardAndLikesTilWaiting();
			int likeNumberBefore = getTextLikeNumberInInt(editBoardPage);
			editBoardPage.likeButton1.click();
			int likeNumberAfter = getTextLikeNumberInInt(editBoardPage);
			Assert.assertTrue(likeNumberBefore < likeNumberAfter);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public int getTextLikeNumberInInt(EditBoardPage editBoardPage) {
		return Integer.parseInt(editBoardPage.getLikeNumberFirstNote().getText());
	}
	
	@Test
	public void deleteLikeFromNote() throws Throwable {
		try {
			EditBoardPage editBoardPage = organisateBoardAndLikesTilWaiting();
			editBoardPage.likeButton1.click();
			int likeNumberLiked = getTextLikeNumberInInt(editBoardPage);
			editBoardPage.likeButton1.click();
			int likeNumberAfter = getTextLikeNumberInInt(editBoardPage);
			Assert.assertTrue(likeNumberLiked > likeNumberAfter);
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
			
			EditBoardPage editBoardPage = new EditBoardPage(driver);
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.id("deleteButton-1")));
			editBoardPage.deleteFirstNote();
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.id("confirmCardDeletion")));
			editBoardPage.confirmCardDeletion();
			editBoardPage.getFirstNote();
			
			Assert.assertTrue(driver.findElements(By.xpath("//*[@id='root']/div/div/div[2]/div[1]/div/div")).isEmpty());
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	
	
	@Test
	public void editNote() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			api.login(testUsername, testPassword);
			takeNewNoteInFirstColumn();
			
			EditBoardPage editBoardPage = new EditBoardPage(driver);
			
			By container = By.cssSelector("MuiDialog-container MuiDialog-scrollPaper");
			WebDriverWait waitForInsivibility = new WebDriverWait(driver, Duration.ofSeconds(10));
			waitForInsivibility.until(ExpectedConditions.invisibilityOfElementLocated(container));
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.elementToBeClickable(By.id("editButton-1")));
			editBoardPage.triggerEditNote();
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[3]/div")));
			
			String newContent = "esfsgeog";
			editBoardPage.typeInTextArea(newContent);
			editBoardPage.submitTextAreaContent();
			
			Assert.assertEquals(newContent, editBoardPage.getTextFromFirstNote());
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void loginWithApi(String username, String password) throws Throwable {
		api.createUser(username, password);
		api.login(username, password);
	}
	
	

	public void waitForBoardOverview() {
		waitFor(Duration.ofSeconds(2), ExpectedConditions.urlContains("/user/boards"));
	}

	public void login(String username, String password) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.typeUsername(username);
		loginPage.typePassword(password);
		loginPage.submitForm();
	}

	public void fillRegisterForm(String username, String password) {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.typeUserName(username);
		registerPage.typePassword(password);
		registerPage.typePasswordRepetition(password);
		registerPage.submitForm();
	}
	
	private void handleThrowable(Throwable t) throws Throwable {
		takeScreenshot();
		throw t;
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
	
	public void waitFor(Duration dur, ExpectedCondition<?> conditions) {
		WebDriverWait wait = new WebDriverWait(driver, dur);
		wait.until(conditions);
	}
	
	public String waitForText(Duration dur, ExpectedCondition<?> conditions) {
		WebDriverWait wait = new WebDriverWait(driver, dur);
		return ((WebElement) wait.until(conditions)).getText();
	}

	public void takeScreenshot() throws IOException {
		Date d = new Date();
		String fileName = dateFormatter.format(d) + ".png";
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File("screenshots/" + fileName));
	}
	
	
}
