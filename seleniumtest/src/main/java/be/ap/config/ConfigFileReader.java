package be.ap.config;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigFileReader {

    private Properties properties,secret;
    private final String propertyFilePath = "configs//Configuration.properties";
    private String secretsPath;
    private static final Pattern ENCRYPTED_PROPERTY_PATTERN = Pattern.compile("^AP-ENC\\((.*)\\)$");


    public ConfigFileReader() {
        secretsPath = System.getenv("secretPath");
        
        if(secretsPath == null){
            secretsPath = "C:/Dev/devops/secret.properties";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(propertyFilePath))) {
            System.out.print("path : " + propertyFilePath);
            properties = new Properties();
            properties.load(reader);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(secretsPath))) {
            System.out.print("path : " + secretsPath);
            secret = new Properties();
            secret.load(reader);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("secret.properties not found at " + secretsPath);
        }
    }

    public String getTargetURL (){

        String url = System.getProperty("TARGET_URL");
        if(Strings.isNullOrEmpty(url)){
            url = getLOCAL_URL();
        }

       return url;
    }

    public String getTargetBrowser(){

        String browser = System.getProperty("TARGET_BROWSER");
        if(Strings.isNullOrEmpty(browser)){
            browser = getDefaultBrowser();
        }
        return browser;
    }

    public String getChromeDriverPath() {
        String driverPath = properties.getProperty("chromeDriverPath");
        if (driverPath != null) return driverPath;
        else throw new RuntimeException("chromeDriverPath not specified in the Configuration.properties file.");
    }

    
    public String getGeckoDriverPath() {
        String driverPath = properties.getProperty("geckoDriverPath");
        if (driverPath != null) return driverPath;
        else throw new RuntimeException("geckoDriverPath not specified in the Configuration.properties file.");
    }

    public String getLOCAL_URL() {
        String driverPath = properties.getProperty("URL_LOCAL");
        if (driverPath != null) return driverPath;
        else throw new RuntimeException("URL local not specified in the Configuration.properties file.");
    }

    public String getDefaultBrowser() {
        String driverPath = properties.getProperty("DEFAULT_BROWSER");
        if (driverPath != null) return driverPath;
        else throw new RuntimeException("default browser not specified in the Configuration.properties file.");
    }

    public String getDEV_URL() {
        String driverPath = properties.getProperty("URL_DEV");
        if (driverPath != null) return driverPath;
        else throw new RuntimeException("URL DEV not specified in the Configuration.properties file.");
    }

    public String getQUAL_URL() {
        String driverPath = properties.getProperty("QUAL_UAT");
        if (driverPath != null) return driverPath;
        else throw new RuntimeException("URL UAT not specified in the Configuration.properties file.");
    }

    public String getUAT_APLICATION() {
        String driverPath = properties.getProperty("URL_APLICATION");
        if (driverPath != null) return driverPath;
        else throw new RuntimeException("application UAT not specified in the Configuration.properties file.");
    }

    public Boolean getHEADLESS(){
        Boolean headless = new Boolean(properties.getProperty("HEADLESS"));
        return headless;
    }
    public long getImplicitlyWait() {
        String implicitlyWait = properties.getProperty("implicitlyWait");
        if (implicitlyWait != null) return Long.parseLong(implicitlyWait);
        else throw new RuntimeException("implicitlyWait not specified in the Configuration.properties file.");
    }
    
    public String getGestionairePassword() throws SecurityException {
    	   String driverPath = secret.getProperty("selenium_gestionaire_password");
           if (driverPath != null) return matchAndDecryptPassword(driverPath);
           else throw new RuntimeException("gestionaire password not specified in the Configuration.properties file.");
    }

    public String getResourcesPath() {
        String resourcesPath = properties.getProperty("resourcesPath");
        if (resourcesPath != null) return resourcesPath;
        else throw new RuntimeException("resourcesPath not specified in the Configuration.properties file.");
    }


    public String getGestionnairePassword() throws SecurityException {
    	   System.out.println("It will try to get the password decrypt");
    	   String driverPath = secret.getProperty("selenium_gestionnaire_password");
           if (driverPath != null) return matchAndDecryptPassword(driverPath);
           else throw new RuntimeException("Gestionnaire password not specified in the Configuration.properties file.");
    }

	private String matchAndDecryptPassword(String originalValue) throws SecurityException{
		Matcher matcher = ENCRYPTED_PROPERTY_PATTERN.matcher(originalValue);
           String propertyValue;
           if (matcher.matches()) {
              propertyValue = SecurityUtils.decrypt(matcher.group(1));
           } else {
               propertyValue = originalValue;
           }
           return propertyValue;
	}

}