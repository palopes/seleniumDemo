package be.ap.test;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(features = {"src/test/resources/features"},
        glue = {"be.ap.selenium.stepdefinitions.common"},
        plugin = {"pretty", "html:target/cucumber", "json:target/cucumberResult.json"})

public class DemoSeleniumTest extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}