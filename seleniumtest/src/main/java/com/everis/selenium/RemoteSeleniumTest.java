package com.everis.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RemoteSeleniumTest {

	public static void main(String[] args) throws MalformedURLException {
//		// TODO Auto-generated method stub
//		DesiredCapabilities capabilities=DesiredCapabilities.chrome();
//		capabilities.setJavascriptEnabled(true);
//		WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444"),  capabilities);
//		driver.manage().timeouts().implicitlyWait(100, TimeUnit.MINUTES);
//		driver.get("http://localhost/ap3client/");
//		WebElement username = driver.findElement(By.name("name"));
//		username.click();
//		username.sendKeys("eve");
		// We could use any driver for our tests...
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		
		// ... but only if it supports javascript
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("ignoreZoomSetting", true);
		capabilities.setVersion("2.40");
		// Get a handle to the driver. This will throw an exception
		// if a matching driver cannot be located
		WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		
		// Query the driver to find out more information
		Capabilities actualCapabilities = ((RemoteWebDriver) driver).getCapabilities();

		// And now use it
		driver.get("http://www.google.com");
	}

}
