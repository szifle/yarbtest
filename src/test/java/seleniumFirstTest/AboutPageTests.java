package seleniumFirstTest;

import java.io.IOException;
import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AboutPageTests extends TestBase {
	
	public AboutPageTests() throws IOException {
		super();
	}

	@Test
	public void openAboutPage() throws Throwable {
		try {
			api.createUser(testUsername, testPassword);
			login(testUsername, testPassword);
			
			waitForBoardOverview();
			
			OverviewOfBoards boardPage = new OverviewOfBoards(driver);
			boardPage.openAboutPage();
			
			waitFor(Duration.ofSeconds(10), ExpectedConditions.urlMatches(properties.getProperty("abouturl")));
			
			String url = driver.getCurrentUrl();
			
			Assert.assertEquals(properties.getProperty("abouturl"), url);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

}
