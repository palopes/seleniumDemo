package be.ap.selenium.stepdefinitions.common;

import be.ap.config.ConfigFileReader;
import be.ap.utils.Utils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.everis.selenium.CommonRegression;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.support.ui.*;
import java.nio.charset.Charset;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommonStepDefinitions {
    WebDriver driver = CommonRegression.getInstanceDriver();
    WebDriverWait wait = CommonRegression.getInstanceWait();
    ExtentReports extent;
    ExtentTest logger;
    Scenario scenario;


    private static final ConfigFileReader configFileReader = new ConfigFileReader();


    @Before
    public void setScenario(Scenario scenario){
        System.out.println(scenario.getSourceTagNames());
        this.scenario = scenario;
    }

    @BeforeMethod
    public void reportSetup (){
        System.out.println(scenario.getName());
        ExtentHtmlReporter reporter = new ExtentHtmlReporter("./Reports/ReportRun-");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        logger = extent.createTest("report");
    }


    @AfterMethod
    public void screenShootListener (ITestResult result){
        if(ITestResult.FAILURE==result.getStatus()){
            //captureScreenShot(result.getName());
        }
        driver.quit();
    }

    public void captureScreenShot(String stepName) throws IOException {
        try {
            Collection<String> tags = scenario.getSourceTagNames();
            String toFind = "ardprev";
            String tagName = "";
            for(String tag: tags){
                if(tag.toLowerCase().contains(toFind)){
                    tagName = tag.substring(1);
                }

                if(tagName.isEmpty() && tags.iterator().next() != null){
                    tagName = tag.substring(1);
                }
            }

            File source = ((TakesScreenshot) CommonRegression.getInstanceDriver()).getScreenshotAs(OutputType.FILE);

            String systemPath = System.getProperty("user.dir");
            String filename = (stepName.isEmpty()) ? systemPath + "\\screenshots\\" + tagName + ".png" : systemPath + "\\screenshots\\" + tagName + "_" + stepName + ".png" ;
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

    @Then("^take screenshot \"([^\"]*)\"$")
    public void take_screeshot(String stepName) throws IOException {
        captureScreenShot(stepName);
    }
    private void reload() {
        if (CommonRegression.isClosed()) {
            CommonRegression.reloadInstances();
        }
        driver = CommonRegression.getInstanceDriver();
        wait = CommonRegression.getInstanceWait();
    }


    /////////BPM automated tests /////////
    @When("^I enter bpm portal the application$")
    public void i_enter_bpm_portal_the_application() throws Throwable {
        try {
            driver.navigate().to(configFileReader.getLOCAL_URL());
        } catch (Exception e) {
            reload();
            driver.get(configFileReader.getLOCAL_URL());
        }
    }

    @Then("^I enter my bpm credentials$")
    public void i_enter_my_bpm_credentials() throws Throwable {
        driver.switchTo().frame(driver.findElement(By.id("login-iframe")));
        System.out.println("i_enter_the_credentials_to_login_with_user_EVE");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement username = driver.findElement(By.name("username"));
        username.click();
        username.sendKeys("EVE");
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.sendKeys(configFileReader.getGestionairePassword());
        WebElement submit = driver.findElement(By.name("login"));
        submit.click();
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/main/div/iframe")));


    }

    @Then("^I press on the \"([^\"]*)\" button$")
    public void i_press_on_the_button(String arg1) throws Throwable {
        List<WebElement> menuList;
        System.out.println("i_press_on_the_button");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("x-treelist-item-wrap")));
        Thread.sleep(3000);
        if(arg1.equals("Auto")) {
            menuList = driver.findElements(By.xpath("//div[contains(text(),'" + arg1 + "')]"));
            new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + arg1 + "')]")));
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", menuList.get(1));


        } else{
            WebElement option = driver.findElement(By.xpath("//div[contains(text(),'" + arg1 + "')]"));
            new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'" + arg1 + "')]")));
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", option);

        }
        System.out.println("PASSEI!!!!!!!!");
        if(!(driver.findElement(By.xpath("//*[contains(@class,'x-tab') and .//*[text()='"+ arg1 +"']]")) instanceof WebElement)){
            throw new Exception("Could not open tab");
        };

    }



    @Then("^I press on the \"([^\"]*)\" on the Branche Combo$")
    public void i_press_on_the_on_the_Branche_Combo(String arg1) throws Throwable {
        System.out.println("i_press_on_the_branche_combo");
        WebElement branche = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("branchCd")));
        switch (arg1) {
            case "Individuelle":
                branche.sendKeys("02 - Individuelle");
                break;
            case "Incendie":
                branche.sendKeys("03 - Incendie");
                break;
            case "RC":
                branche.sendKeys("04 - RC");
                break;
            case "Auto":
                branche.sendKeys("05 - Auto");
                break;
            case "Accident travail":
                branche.sendKeys("06 - Accident travail");
                break;
            case "Incendie R.I.":
                branche.sendKeys("07 - Incendie R.I.");
                break;
            case "Assistance":
                branche.sendKeys("09 - Assistance");
                break;
            case "Globalia":
                branche.sendKeys("80 - Globalia");
                break;
            case "Globalia Pro":
                branche.sendKeys("85 - Globalia Pro");
                break;
            default:
                break;
        }
    }

    @Then("^I click on Rechercher un dossier$")
    public void i_click_on_Rechercher_un_dossier() throws Throwable {
        System.out.println("i_click_on_rechercher_un_dossier");
        new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("dossier")));
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.partialLinkText("dossier")));
        WebElement dossier = driver.findElement(By.partialLinkText("dossier"));
        Thread.sleep(3000);
        Actions actions = new Actions(driver);
        actions.moveToElement(dossier).click().perform();
    }

    @Then("^I click on first result$")
    public void i_click_on_first_result() throws Throwable {
        System.out.println("i_click_on_first_result");
        WebElement firstResult = wait.until(ExpectedConditions.elementToBeClickable(By.className("x-grid-cell-inner")));
        String firstResultText = firstResult.getAttribute("innerHTML");
        Actions actions = new Actions(driver);
        actions.moveToElement(firstResult).click().perform();
        // This part is only evaluating if the tab name and the entry that was clicked have the same name
        List<WebElement> tabs = driver.findElements(By.className("x-tab-inner"));
        wait.until(ExpectedConditions.attributeContains(tabs.get(1), "innerHTML", firstResultText));
        if(!(tabs.get(1).getAttribute("innerHTML").trim().equals(firstResultText.trim()))){
            throw new Exception ("Tab name is not the same as entry");
        }
    }

    @Then("^I insert the value \"([^\"]*)\" in reference$")
    public void i_insert_the_value_in_reference(String arg1) throws Throwable {
        System.out.println("i_insert the value in reference");
        WebElement reference = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("reference")));
        Actions actions = new Actions(driver);
        actions.moveToElement(reference).click().perform();
        reference.sendKeys(arg1);
    }

    @Then("^I validate the results number$")
    public void i_validate_the_results_number() throws Throwable {
        System.out.println("i_validate_the_results_number");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("x-grid-cell-inner")));
        if (!(driver.findElement(By.className("x-grid-cell-inner")) instanceof WebElement)) {
            throw new Exception("No results");
        }
    }



    @Then("^I validate that the tab \"([^\"]*)\" opened$")
    public void i_validate_that_the_tab_opened(String arg1) throws Throwable {
        System.out.println("i_validate_that_the_tab_opened ");
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.className("x-tab")));
        if(!(driver.findElement(By.xpath("//*[contains(@class,'x-tab') and .//*[text()='"+ arg1 +"']]")) instanceof WebElement)){
            throw new Exception("Tab did not open");
        }
    }

    @Then("^I enter \"([^\"]*)\" on Gestionnaire field$")
    public void i_enter_on_Gestionnaire_field(String arg1) throws Throwable{
        System.out.println("i_enter_on_gestionaire_field " +arg1);
        WebElement gestionnaire = driver.findElement(By.name("initials"));
        Actions actions = new Actions(driver);
        actions.moveToElement(gestionnaire).click().perform();
        gestionnaire.sendKeys(arg1);
    }

    @Then("^I choose \"([^\"]*)\" on gestionnaire combo$")
    public void i_choose_on_gestionnaire_combo(String arg1) throws Throwable {
        System.out.println("i_choose_on_gestionnaire_combo " +arg1);
        WebElement gestionnaire = driver.findElement(By.name("initials"));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", gestionnaire);
    }



    @Then("^I upload the file \"([^\"]*)\"$")
    public void i_upload_the_file(String arg1) throws Throwable {
        System.out.println("i_upload_the_file " +arg1);
        String path = configFileReader.getResourcesPath();
        driver.findElement(By.name("fileUploadBtn")).sendKeys(path + arg1);

    }

    @Then("^I validate \"([^\"]*)\" upload$")
    public void i_validate_upload(String arg1) throws Throwable {
        System.out.println("i_validate_upload " +arg1);
        Assert.assertTrue(driver.getPageSource().contains(arg1));

    }



    @Then("^I enter \"([^\"]*)\" on Statut field$")
    public void i_enter_on_Statut_field(String arg1) throws Throwable {
        System.out.println("i_enter_on_Statut_field " +arg1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("taskStatusCd")));
        wait.until(ExpectedConditions.elementToBeClickable(By.name("taskStatusCd")));
        driver.findElement(By.name("taskStatusCd"));
        WebElement statut = driver.findElement(By.name("taskStatusCd"));
        Actions actions = new Actions(driver);
        actions.moveToElement(statut).click().perform();
        statut.sendKeys(arg1);
    }



    @Then("^I click on Sauver$")
    public void i_click_on_Sauver() throws Throwable {
        System.out.println("i_click_on_Sauver");
        WebElement sauver = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText("Sauver"))));
        sauver.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("success-toaster")));
    }

    @Then("^I click on Dupliquer$")
    public void i_click_on_Dupliquer() throws Throwable {
        System.out.println("i_click_on_Dupliquer");
        WebElement dupliquer = driver.findElement(By.partialLinkText("Dupliquer"));
        dupliquer.click();
        if(driver.findElement(By.className("x-window-header")).isDisplayed()){
           System.out.println("ENTROU");
           List<WebElement> radioButtons = driver.findElements(By.name("op"));
           System.out.println("O TAMANHO É   : " + radioButtons.size());
           radioButtons.get(1).click();
           wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("OK"))).click();
        }
    }

    @Then("^I click on Creer$")
    public void i_click_on_Creer() throws Throwable {
        System.out.println("I click on Creer");
        final Charset UTF_8 = Charset.forName("UTF-8");
        final Charset ISO = Charset.forName("ISO-8859-1");
        List<WebElement> tabs = driver.findElements(By.className("x-tab-inner-mainTabPanel"));
        String aux = "Créer";
        String text = new String(aux.getBytes(ISO), UTF_8);
        WebElement creer = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText(text))));
        creer.click();
        List<WebElement> newTabs = driver.findElements(By.className("x-tab-inner-mainTabPanel"));
        Thread.sleep(3000); /* This one is needed for the portal to be able to open another tab */
        if(newTabs.size() <= tabs.size()){
            throw new Exception ("New tab did not open");
        }
    }


    @Then("^I validate that another tab opened$")
    public void i_validate_that_another_tab_opened() throws Throwable {
        System.out.println("i_validate_that_another_tab_opened");
        List<WebElement> tabs = driver.findElements(By.className("x-tab-inner-mainTabPanel"));
        if(!(tabs.get(1).getAttribute("innerHTML").equals(tabs.get(2).getAttribute("innerHTML")))){
            throw new Exception("The duplicated tab is not equal to the one that was already open");
        }
    }

    @Then("^I insert on PoliceNr the value \"([^\"]*)\"$")
    public void i_insert_on_PoliceNr_the_value(String arg1) throws Throwable {
        System.out.println("i_insert_on_PoliceNr_the_value " +arg1);
        WebElement nrPolice = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[3]/div/div/div/div[3]/div[2]/div[2]/div/div/div[1]/div/div/div[1]/div/div/div[1]/div[2]/div/div/div/div[2]/div/div/div[1]/input"))));
        nrPolice.click();
        nrPolice.sendKeys(arg1);
    }

    @Then("^I press Verifier$")
    public void i_press_Verifier() throws Throwable {
        WebElement branche = driver.findElement(By.name("branchCd"));
        final Charset UTF_8 = Charset.forName("UTF-8");
        final Charset ISO = Charset.forName("ISO-8859-1");
        String aux = "Vérifier";
        String text = new String(aux.getBytes(ISO), UTF_8);
        WebElement verifier = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText(text))));
        verifier.click();
    }

    @Then("^I validate the Branche field has \"([^\"]*)\"$")
    public void i_validate_the_Branche_field_has(String arg1) throws Throwable {
        System.out.println("i_validate_the_Branche_field_has " + arg1);
        WebElement aux = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[3]/div/div/div/div[3]/div[2]/div[2]/div/div/div[1]/div/div/div[1]/div/div/div[1]/div[2]/div/div/div/div[6]/div/div/div[1]/input"));
        aux.sendKeys(Keys.DOWN);
        WebElement comboItem = driver.findElement(By.className("x-boundlist-selected"));
        System.out.println(comboItem.getAttribute("innerHTML"));
        if (!comboItem.getAttribute("innerHTML").equals(arg1)) {
            throw new Exception("Branche field does not contain " + arg1);
        }
    }


    @Then("^i press on the bpm portal the button Logout$")
    public void i_press_on_the_bpm_portal_the_button_Logout() throws Throwable {
        final Charset UTF_8 = Charset.forName("UTF-8");
        final Charset ISO = Charset.forName("ISO-8859-1");
        System.out.println("Logging out");
        String aux = "connecter";
        String text = new String(aux.getBytes(ISO), UTF_8);
        WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText(text))));
        logout.click();
        System.out.println("Clicked on logout button");
        WebElement confirm = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.partialLinkText("Oui"))));
        confirm.click();
        Thread.sleep(10000);
        driver.navigate().refresh();
    }

    public static void waitForTextToAppear(WebDriver newDriver, String textToAppear, WebElement element) {

        WebDriverWait wait = new WebDriverWait(newDriver,30);

        wait.until(ExpectedConditions.textToBePresentInElement(element, textToAppear));

    }

    /****/

    @When("^open SeleniumEasy portal$")
    public void open_SeleniumEasy_portal() throws Throwable {
        try {
            driver.navigate().to(configFileReader.getLOCAL_URL());
        } catch (Exception e) {
            reload();
            driver.get(configFileReader.getLOCAL_URL());
        }
    }

    @Then("^i click on the top menu \"([^\"]*)\"$")
    public void i_click_on_the_top_menu(String arg1) throws Throwable {
        WebElement inputFormMenu = driver.findElement(By.xpath("//a[contains(text(),'Input Forms')]"));
        inputFormMenu.click();
    }

    @Then("^select on sub menu \"([^\"]*)\"$")
    public void select_on_sub_menu(String arg1) throws Throwable {
        WebElement simpleFormDemoMenu = driver.findElement(By.xpath("//a[contains(text(),'Simple Form Demo')]"));
        simpleFormDemoMenu.click();
    }

    @Then("^i wait for \"([^\"]*)\" page to load$")
    public void i_wait_for_page_to_load(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^i insert \"([^\"]*)\" on single input field example$")
    public void i_insert_on_single_input_field_example(String arg1) throws Throwable {
        WebElement userMessageTextField = driver.findElement(By.id("user-message"));
        userMessageTextField.sendKeys(arg1);
    }

    @Then("^i insert (\\d+) on the \"([^\"]*)\" field$")
    public void i_insert_on_the_field(int arg1, String arg2) throws Throwable {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sum1")));

        switch(arg2) {
            case "a":
                WebElement fieldA = driver.findElement(By.id("sum1"));
                fieldA.sendKeys("" + arg1);
                break;
            case "b":
                WebElement fieldB = driver.findElement(By.id("sum2"));
                fieldB.sendKeys("" + arg1);
                break;
            default:
                // code block
        }
    }

    @Then("^i validate if the sum of values is numberic$")
    public void i_validate_if_the_sum_of_values_is_numberic() throws Throwable {
        WebElement closeButton = driver.findElement(By.id("displayvalue"));
        Assert.assertEquals("42", closeButton.getText());
    }

    @Then("^i close publicity popup$")
    public void i_close_publicity_popup() throws Throwable {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("at-cv-lightbox-close")));
        WebElement closeButton = driver.findElement(By.id("at-cv-lightbox-close"));
        closeButton.click();
    }

    @Then("^i press the button \"([^\"]*)\"$")
    public void i_press_the_button(String arg1) throws Throwable {
        switch(arg1) {
            case "Get Total":
                WebElement getTotalButton = driver.findElement(By.xpath("(//button[@type='button'])[3]"));
                getTotalButton.click();
                break;
            case "Show Message":
                WebElement showMessageButton = driver.findElement(By.xpath("(//button[@type='button'])[2]"));
                showMessageButton.click();
                break;
            default:
                // code block
        }
    }

    @Then("^press the button \"([^\"]*)\"$")
    public void press_the_button(String arg1) throws Throwable {
        switch(arg1) {
            case "Get Total":
                WebElement getTotalButton = driver.findElement(By.xpath("(//button[@type='button'])[3]"));
                getTotalButton.click();
                break;
            case "Show Message":
                WebElement showMessageButton = driver.findElement(By.xpath("(//button[@type='button'])[2]"));
                showMessageButton.click();
                break;
            default:
                // code block
        }
    }


}
