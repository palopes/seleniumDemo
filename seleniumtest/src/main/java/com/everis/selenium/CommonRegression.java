package com.everis.selenium;

import be.ap.config.ConfigFileReader;
import jdk.nashorn.internal.runtime.options.Options;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.SSL;
import static org.openqa.selenium.remote.CapabilityType.ACCEPT_SSL_CERTS;

public class CommonRegression {



	private static WebDriver driver = null;
	private static WebDriverWait wait = null;
	private static boolean closed=false;

	private static final ConfigFileReader configFileReader = new ConfigFileReader();

	static {
		System.setProperty("webdriver.chrome.driver", configFileReader.getChromeDriverPath());
		System.setProperty("webdriver.gecko.driver", configFileReader.getGeckoDriverPath());
	}

	public static WebDriver getInstanceDriver() {
		String browser = configFileReader.getTargetBrowser();
		if (browser.equals("firefox")){
			//to change
			return getInstanceChromeDriver();
		}else{
			return getInstanceChromeDriver();
		}
	}

	public static WebDriver getInstanceChromeDriver() {
		if (driver == null) {
			synchronized (CommonRegression.class) {
				if (driver == null) {

					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.addArguments("--window-size=1920,1080");
					chromeOptions.addArguments("--no-sandbox");
					chromeOptions.addArguments("--disable-dev-shm-usage");
					chromeOptions.addArguments("--ignore-certificate-errors");
					chromeOptions.addArguments("--disable-extensions");
					//chromeOptions.addArguments("--disable-gpu");
					if(configFileReader.getHEADLESS()){
						chromeOptions.addArguments("--headless");
					}
					driver = new ChromeDriver(chromeOptions);
					//driver = new InternetExplorerDriver();
					driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
					driver.manage().window().maximize();
				}
			}
		}
		return driver;
	}




	public static WebDriverWait getInstanceWait() {
		if (wait == null) {
			synchronized (CommonRegression.class) {
				if (wait == null) {
				}
				wait = new WebDriverWait(driver, 60);
			}
		}
		return wait;
	}

	public static void reloadInstances() {
		System.out.println("reloading instances...");
		synchronized (CommonRegression.class) {
			if(closed) {
			//driver = new ChromeDriver();
			driver = new InternetExplorerDriver();
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
			driver.manage().window().maximize();
			wait = new WebDriverWait(driver, 60);
			closed=false;}
		}
	}

	public static String getTestFailureDate(){
		/*
		LocalDate localDate = LocalDate.now();
		String finalDate = DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate);
		return finalDate.replace("/","");

		 */
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
		return modifiedDate;

	}

	public static synchronized void close() {
		closed=true;
	}
	public static synchronized boolean isClosed() {
		return closed;
	}
}
