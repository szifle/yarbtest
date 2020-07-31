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

public class TestBase {

	protected WebDriver driver;
	protected YarbApi api = YarbApi.getInstance();
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSSS");
	protected Properties properties = new Properties();
	protected static final String registerUsernameHelperText = "registerUsername-helper-text";
	protected static final String testUsername = "susisusi";
	protected static final String testPassword = "huchhuch";
	protected static final String loginErrorText = "Username or password is wrong";

	public TestBase() throws IOException {
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("test.properties"));
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
		waitFor(Duration.ofSeconds(2), ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".MuiDialog-container .MuiDialog-scrollPaper"))); 
	}

	@After
	public void after() {
		driver.close();
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
	
	protected void handleThrowable(Throwable t) throws Throwable {
		takeScreenshot();
		throw t;
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
