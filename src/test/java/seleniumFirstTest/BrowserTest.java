package seleniumFirstTest;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.zifle.meintestprojekt.db.DbCommands;

public class BrowserTest {

	private WebDriver driver;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSSS");
	

	public BrowserTest() {
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\susicharlson\\eclipse-workspace\\geckodriver.exe");
		driver = new FirefoxDriver();
	}

	@Before
	public void before() throws SQLException, IOException {
		DbCommands.dropAndCreateYarbDB();
		driver.get("https://192.168.178.32:8443/");
	}

	@After
	public void after() {
		driver.close();
	}

	@Test
	public void testLoginSuccess() throws Throwable {
		YarbApi api = new YarbApi();
		api.createUser("susisusi", "huchhuch");
		try {
			login("susisusi", "huchhuch");
			waitForBoardOverview();
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void testLoginWithInvalidCredentials() throws Throwable {
		try {
			login("susisusi", "huchhuch");
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			String errorText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password-helper-text"))).getText();
			Assert.assertEquals(errorText, "Username or password is wrong");
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void logoutUser() throws Throwable {
		YarbApi api = new YarbApi();
		api.createUser("susisusi", "huchhuch");
		try {
			login("susisusi", "huchhuch");
			BoardPage boardPage = new BoardPage(driver);
			boardPage.logout();
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			String currentUrl = driver.getCurrentUrl();
			Assert.assertEquals("https://192.168.178.32:8443/login", currentUrl);
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
			YarbApi api = new YarbApi();
			api.createUser("susisusi", "huchhuch");
			LoginPage loginPage = new LoginPage(driver);
			loginPage.switchToRegister();
			fillRegisterForm("schmetterling", "huchhuch");

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerUsername-helper-text"))).getText();
		
			Assert.assertEquals("Username is already taken", errorMessage);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	private void handleThrowable(Throwable t) throws Throwable {
		takeScreenshot();
		throw t;
	}
	
	@Test 
	public void testInvalidRegisterNameMoreThan20Characters() throws Throwable {
		try {
			LoginPage loginPage = new LoginPage(driver);
			loginPage.switchToRegister();
			RegisterPage registerPage = new RegisterPage(driver);
			registerPage.typeUserName("aladdinunddiewunderlampe");
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerUsername-helper-text")));
			WebElement error = driver.findElement(By.id("registerUsername-helper-text"));
			wait.until(ExpectedConditions.visibilityOf(error));
			
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
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerUsername-helper-text"))).getText();
			
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
			registerPage.typeUserName("al");
			registerPage.typePassword("123");
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerPassword-helper-text"))).getText();
			
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
			registerPage.typeUserName("al");
			registerPage.typePassword("123");
			registerPage.typePasswordRepetition("12333");
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			String errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerPasswordRepetition-helper-text"))).getText();
			
			Assert.assertEquals("Must be the same as password", errorMessage);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void createMultipleBoards() throws Throwable {
		
		try {
			YarbApi api = new YarbApi();
			api.createUser("susisusi", "huchhuch");
			api.login("susisusi", "huchhuch");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			api.createBoard("name", "eins", "zwei", "drei");
			
			login("susisusi", "huchhuch");
			int sizeOfBoards = driver.findElements(By.xpath("//*[contains(@id, 'boardCard')]")).size();
			
			Assert.assertEquals(10, sizeOfBoards);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}

	@Test
	public void createNewBoard() throws Throwable {
		try {
			YarbApi api = new YarbApi();
			api.createUser("susisusi", "huchhuch");
			login("susisusi", "huchhuch");
			String boardTitle = "Der heutige Taaaaaag";
	
			BoardPage boardPage = new BoardPage(driver);
			
			boardPage.openNewBoardFormular();
			boardPage.setBoardTitle(boardTitle);
			boardPage.setBoardColumnInput0("bunt");
			boardPage.setBoardColumnInput1("xy");
			boardPage.setBoardColumnInput2("xwwwwy");
			boardPage.setBoardColumnInput3("xfdsfsdgy");
			boardPage.confirmBoardCreation();
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/main")));
			
			Integer sizeOfBoards = driver.findElements(By.cssSelector("MuiGrid-root MuiGrid-item MuiGrid-grid-xs-3")).size();
			
			Assert.assertEquals(Integer.valueOf(0), sizeOfBoards);
		} catch(Throwable t) {
			handleThrowable(t);
		}

		Thread.sleep(3000);
	}
	
	@Test
	public void openAboutPage() throws Throwable {
		YarbApi api = new YarbApi();
		api.createUser("susisusi", "huchhuch");
		try {
			login("susisusi", "huchhuch");
			BoardPage boardPage = new BoardPage(driver);
			boardPage.openAboutPage();
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			wait.until(ExpectedConditions.urlMatches("https://192.168.178.32:8443/user/about"));
			
			String url = driver.getCurrentUrl();
			
			Assert.assertEquals("https://192.168.178.32:8443/user/about", url);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void editBoardWorks() throws Throwable {
		YarbApi api = new YarbApi();
		api.createUser("susisusi", "huchhuch");
		try {
			login("susisusi", "huchhuch");
			BoardPage boardPage = new BoardPage(driver);
			boardPage.openBoard();
			boardPage.setBoardTitle("boardTitle");
			boardPage.setBoardColumnInput0("bunt");
			boardPage.setBoardColumnInput1("xy");
			boardPage.setBoardColumnInput2("xwwwwy");
			boardPage.setBoardColumnInput3("xfdsfsdgy");
			boardPage.confirmBoardCreation();
			
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/main")));
			
			EditBoardPage editBoardPage = new EditBoardPage(driver);
			editBoardPage.addNoteToFirstSheet();
			editBoardPage.typeInTextArea("neu");
			editBoardPage.submitTextAreaContent();
			
//			String boardTitle = editBoardPage.getBoardTitle().getText();
//			WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(3));
//			String headingOfSingleBoard = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("MuiTypography-root jss1729 MuiTypography-h5 MuiTypography-colorTextSecondary MuiTypography-alignCenter"))).getText();
//			
//			Assert.assertEquals(boardTitle, headingOfSingleBoard);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void takeNewNote() throws Throwable {
		YarbApi api = new YarbApi();
		api.createUser("susisusi", "huchhuch");
		login("susi","huchhuch");
		
		String boardTitle = "Der heutige Taaaaaag";
		
		BoardPage boardPage = new BoardPage(driver);
		boardPage.openNewBoardFormular();
		boardPage.setBoardTitle(boardTitle);
		boardPage.setBoardColumnInput0("bunt");
		boardPage.setBoardColumnInput1("xy");
		boardPage.setBoardColumnInput2("xwwwwy");
		boardPage.setBoardColumnInput3("xfdsfsdgy");
		boardPage.confirmBoardCreation();
		boardPage.openBoard();
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiGrid-root MuiGrid-container MuiGrid-spacing-xs-2'")));
		
//		EditBoardPage editBoardPage = new EditBoardPage(driver);
//		editBoardPage.
	}

	public void waitForBoardOverview() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		wait.until(ExpectedConditions.urlContains("/user/boards"));
	}

	public void login(String username, String password) {
		LoginPage loginPage = new LoginPage(driver);
		loginPage.typeUsername(username);
		loginPage.typePassword(password);
		loginPage.submitForm();
	}

	public void fillRegisterForm(String username, String password) {
		RegisterPage registerPage = new RegisterPage(driver);
		//registerPage.switchToRegister();
		registerPage.typeUserName(username);
		registerPage.typePassword(password);
		registerPage.typePasswordRepetition(password);
		registerPage.submitForm();
	}

	public void takeScreenshot() throws IOException {
		Date d = new Date();
		String fileName = dateFormatter.format(d) + ".png";
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File("screenshots/" + fileName));
	}
}
