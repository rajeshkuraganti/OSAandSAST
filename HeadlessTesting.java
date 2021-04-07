package com.cx.automation.adk;

import com.cx.automation.adk.selenium.BrowserType;
import com.cx.automation.adk.selenium.WebDriverFactoryImpl;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aviat on 12/11/2018.
 */
@Ignore
public class HeadlessTesting {

    @Test
    public void testHeadless() throws IOException {
//        System.setProperty("headless",String.valueOf(true));
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", "C:\\downloads");
        prefs.put("plugins.always_open_pdf_externally", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-infobars", "--start-maximized");
        options.addArguments("headless");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.get("http://seleniumhq.org");
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot.png"));
        // a guarantee that the test was really executed
        Assert.assertTrue(driver.findElement(By.id("q")).isDisplayed());
        driver.quit();
    }
}
