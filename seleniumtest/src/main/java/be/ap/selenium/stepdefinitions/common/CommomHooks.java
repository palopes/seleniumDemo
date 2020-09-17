package be.ap.selenium.stepdefinitions.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import com.everis.selenium.CommonRegression;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;

public class CommomHooks {

	private static final CommonStepDefinitions commonStepDefinitions = new CommonStepDefinitions();



	@After
	public static void failureTestProcedures(Scenario scenario) throws Throwable {
		System.out.println("This will run after the Scenario");
		if (scenario.isFailed()) {
			System.out.println("in scenario fail");
				commonStepDefinitions.setScenario(scenario);
			commonStepDefinitions.captureScreenShot("fail");
			Thread.sleep(4000);
			commonStepDefinitions.i_press_on_the_bpm_portal_the_button_Logout();
		}
		}
/*
	private static void captureScreenShot(Scenario scenario) throws IOException {
		try {

			Collection<String> tags = scenario.getSourceTagNames();
			String toFind = "ardprev";
			String tagName = "";
			for(String tag: tags){
				if(tag.toLowerCase().contains(toFind)){
					tagName = tag.substring(1);
				}
			}

			String finalDate = CommonRegression.getTestFailureDate();
			String testName = scenario.getName();
			File source = ((TakesScreenshot) CommonRegression.getInstanceDriver()).getScreenshotAs(OutputType.FILE);


			String systemPath = System.getProperty("user.dir");
			testName = testName.replaceAll("\\s", "");
			testName = testName.replaceAll("\"", "");
			String filename = systemPath + "\\screenshots\\" + tagName + ".png";
			//String filename = systemPath + "\\screenshots\\" + testName + "_" + finalDate + ".png";
			FileUtils.copyFile(source, new File(filename));

            byte[] screenshot = ((TakesScreenshot) CommonRegression.getInstanceDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
			System.out.println("Taking a Screen Shot");


		} catch (WebDriverException wde) {
			System.err.println(wde.getMessage());
		} catch (ClassCastException cce) {
			cce.printStackTrace();
		}
	}
 */


	private static void logout() throws InterruptedException {
		WebDriver driver = CommonRegression.getInstanceDriver();
		WebDriverWait wait = CommonRegression.getInstanceWait();
		wait.until(ExpectedConditions.elementToBeClickable(By.name("logout")));
		WebElement logoutButton = driver.findElement(By.name("logout"));
		Thread.sleep(2000);
		logoutButton.click();
		Thread.sleep(2000);
		WebElement btnReconnection = driver.findElement(By.id("button-1006-btnInnerEl"));
		btnReconnection.click();
		System.out.println("finish logout");
	}

}
