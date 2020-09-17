package be.ap.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.everis.selenium.CommonRegression;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.List;

public class Utils {


    public static void captureScreenShot(WebDriver driver, String screenShotName) {

        try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			FileHandler.copy(source, new File("./ScreenShots/" + screenShotName+"_"+System.currentTimeMillis()+".png"));
			System.out.println("Taking a Screen Shot");

        } catch (Exception	 e) {
            System.out.println("Exception while taking a Screen Shot" + e.getMessage());
        }
    }

    public static void chooseElementOnRadioList(WebDriver driver, String radioElementStr,int order){
        System.out.println("chooseElementOnRadio : " + radioElementStr+ "-boxLabelEl");
        List<WebElement> radioElements = driver.findElements(By.name(radioElementStr));
		System.out.println("click on element number: " + radioElements.size());
		Actions actions = new Actions(driver);
		actions.moveToElement(radioElements.get(order)).click().perform();
    }

    public static void chooseElementOnComboBox (WebDriver driver, WebDriverWait wait, String element, String component) throws InterruptedException {
        System.out.println("i_choose_"+ element +"_libataire_on_" +component);

        WebElement comboCmp = driver.findElement(By.name(component));
        String comboId = comboCmp.getAttribute("id");

        comboId = comboId.substring(0, comboId.lastIndexOf("-"));
        WebElement picker = driver.findElement(By.id(comboId.concat("-trigger-picker")));
        picker.click();

        Thread.sleep(1000);
        String optionBoundList = comboCmp.getAttribute("aria-owns");
        optionBoundList = optionBoundList.substring(0, optionBoundList.lastIndexOf("-"));

        By boundList = By.xpath("//div[@id='" + optionBoundList + "-listWrap']/ul/li");
        wait.until(ExpectedConditions.visibilityOfElementLocated(boundList));
        List<WebElement> allElements = driver.findElements(boundList);

        Integer integerTarget = Integer.parseInt(element.replaceAll("\"", ""));

        for (int i=0; i<allElements.size(); i++){
            System.out.println(allElements.get(i).getText());
        }


        allElements.get(integerTarget).click();

        Thread.sleep(1000);
    }

}
