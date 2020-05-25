package seleniumFirstTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OverviewOfBoardsTests extends TestBase {
	
	private static OverviewOfBoards boardPage;

	public OverviewOfBoardsTests() throws IOException {
		super();
	}
	
	@Override
	public void before() throws SQLException, IOException {
		super.before();
		boardPage = new OverviewOfBoards(driver);
	}
	
	@Test
	public void logoutUser() throws Throwable {
		try {
			api.createUserAndloginWithApi(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			boardPage.logout();
			
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			
			String currentUrl = driver.getCurrentUrl();
			
			Assert.assertEquals(properties.getProperty("loginurl"), currentUrl);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	@Test
	public void createMultipleBoards() throws Throwable {
		
		try {
			api.createUserAndloginWithApi(testUsername, testPassword);
			createMultipleBoardsInApi();
			
			login(testUsername, testPassword);
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.visibilityOf(boardPage.boardCard));
			
			int sizeOfBoards = driver.findElements(By.xpath("//*[contains(@id, 'boardCard')]")).size();
			
			Assert.assertEquals(10, sizeOfBoards);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void createMultipleBoardsInApi() {
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
	}
	
	

}
