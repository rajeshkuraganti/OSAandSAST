package com.cx.automation.adk.selenium.utils;

import com.cx.automation.adk.selenium.BrowserType;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by: iland
 * Date: 5/18/2014
 */
public class WebDriverHelper {

    private static final Logger log = LoggerFactory.getLogger(WebDriverHelper.class);

    private static int WAIT_TIME = 10;

    public static void closeAllButThis(WebDriver driver, String winTitle) {
        Set<String> handlers = driver.getWindowHandles();
        String tabToReturn;

        if (handlers.size() < 2) {
            return;
        }
        tabToReturn = handlers.iterator().next();

        for (String handler : handlers) {
            driver.switchTo().window(handler);
            if (driver.getWindowHandles().size() == 1) {
                return;
            }
            if (driver.getTitle().contains(winTitle)) {
                tabToReturn = handler;
                continue;
            }
            driver.close();
        }
        driver.switchTo().window(tabToReturn);
    }

    public static void switchToByTitle(WebDriver driver, String winTitle) {
        Set<String> handlers = driver.getWindowHandles();

        for (String handler : handlers) {
            driver.switchTo().window(handler);
            if (driver.getTitle().contains(winTitle)) {
                break;
            }
        }
    }

    public static WebElement stableFindElement(SearchContext driver, By by, int refreshTimeInMilliSec) throws WebDriverException {
        WebElement element = null;
        WebDriverException webDrvExp = null;
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                driver.findElement(by).isDisplayed();
                if (attempts == 0) {
                    sleep(refreshTimeInMilliSec);
                } else {
                    element = driver.findElement(by);
                    sleep(refreshTimeInMilliSec);
                    element.isDisplayed();
                    break;
                }
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.", e);
                element = null;
                webDrvExp = e;
                sleep(refreshTimeInMilliSec);
            } catch (ElementNotVisibleException ex) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.", ex);
                element = null;
                webDrvExp = ex;
                sleep(refreshTimeInMilliSec);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.", expn);
                element = null;
                webDrvExp = expn;
                sleep(refreshTimeInMilliSec);
            } catch (WebDriverException exp) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.", exp);
                element = null;
                webDrvExp = exp;
                sleep(refreshTimeInMilliSec);
            }
        }
        if (element == null && webDrvExp != null) {
            log.debug("Couldn't find element for the given 'By' that was given: " + by.toString());
            throw webDrvExp;
        }
        return element;
    }

    public static List<WebElement> stableFindElements(SearchContext driver, By by, int refreshTimeInMilliSec) throws WebDriverException {
        List<WebElement> elements = null;
        WebDriverException webDrvExp = null;
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                driver.findElements(by);
                if (attempts == 0) {
                    sleep(refreshTimeInMilliSec);
                } else {
                    elements = driver.findElements(by);
                    sleep(refreshTimeInMilliSec);
                    for (WebElement elm : elements) {
                        elm.getText();
                    }
                    break;
                }
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.", e);
                elements = null;
                webDrvExp = e;
                sleep(refreshTimeInMilliSec);
            } catch (ElementNotVisibleException ex) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.", ex);
                elements = null;
                webDrvExp = ex;
                sleep(refreshTimeInMilliSec);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.", expn);
                elements = null;
                webDrvExp = expn;
                sleep(refreshTimeInMilliSec);
            } catch (WebDriverException exp) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.", exp);
                elements = null;
                webDrvExp = exp;
                sleep(refreshTimeInMilliSec);
            }
        }
        if (elements == null && webDrvExp != null) {
            log.debug("Couldn't find elements for the given 'By' that was given: " + by.toString());
            throw webDrvExp;
        }
        return elements;
    }

    public static boolean stableFindClick(WebDriver driver, By by, int timeoutInSec) throws WebDriverException {
        driver.manage().timeouts().implicitlyWait(timeoutInSec, TimeUnit.SECONDS);
        boolean result = false;
        try {
            result = stableFindClick(driver, by);
        } finally {
            driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        }

        return result;
    }

    public static boolean stableFindClick(WebDriver driver, By by) throws WebDriverException {
        boolean result = false;
        WebDriverException webDrvExp = null;
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                clickWhenReady(driver, by);
                result = true;
                break;
            } catch (WebDriverException e) {
                webDrvExp = e;
                log.info("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                sleep(1500);
            }
        }
        if (webDrvExp != null && !result) {
            throw webDrvExp;
        }
        return true;
    }

    public static boolean delayedStableFindClick(WebDriver driver, By by, int dealyInMs) throws WebDriverException {
        sleep(dealyInMs);
        return stableFindClick(driver, by);
    }

    public static WebElement delayedStableFindElement(SearchContext driver, By by, int pollingTime, int dealyInMs) throws WebDriverException {
        sleep(dealyInMs);
        return stableFindElement(driver, by, pollingTime);
    }

    public static void waitUntilElementIsNotVisible(WebDriver driver, By locator, int timeOutInSeconds) {
        int counter = 0;
        boolean isFound = false;

        while (!isFound && counter++ < timeOutInSeconds) {
            try {
                driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS); // max time to search the element
                driver.findElement(locator);
                sleep(1000);
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return;
            }
        }
    }

    public static void waitForElement(WebDriver driver, By locatorToWait, int timeOutInSeconds) {
        new WebDriverWait(driver, timeOutInSeconds).
                until(ExpectedConditions.visibilityOfElementLocated(locatorToWait));
    }

    public static void waitForElementAndWait(WebDriver driver, By locatorToWait, int timeOutInSeconds, int waitInMillis) {
        new WebDriverWait(driver, timeOutInSeconds).
                until(ExpectedConditions.visibilityOfElementLocated(locatorToWait));
        sleep(waitInMillis);
    }

    public static boolean waitUntilElementContainsAttribute(SearchContext driver, By locatorToWait, String attributeName, String attributeContent, int timeOutInMills) {
        int counter = 0;
        boolean isFound = false;

        while (!isFound || counter < timeOutInMills) {
            try {
                if (driver.findElement(locatorToWait).getAttribute(attributeName).contains(attributeContent)) {
                    sleep(200);
                    return true;
                } else {
                    counter += 100;
                    sleep(100);
                }
            } catch (NoSuchElementException e) {
                counter += 100;
                sleep(100);
            } catch (NullPointerException e) {
                log.warn("Attribute name: " + attributeName + " doesn't related with element " + locatorToWait);
                return false;
            }
        }
        return false;
    }

    public static boolean waitUntilElementText(SearchContext driver, By locatorToWait, String text, long timeOutInMills) {
        int counter = 0;
        boolean isFoundOrTimeEnded = false;

        while (!isFoundOrTimeEnded) {
            if (driver.findElement(locatorToWait).getText().equals(text)) {
                return true;
            } else if (counter < timeOutInMills) {
                counter += 500;
                sleep(500);
            } else {
                return true;
            }
        }
        return false;
    }

    public static void clickAndWaitForElement(WebDriver driver, WebElement elementToClick, By locatorToWait, int timeOutInSeconds) {
        elementToClick.click();

        new WebDriverWait(driver, timeOutInSeconds).
                until(ExpectedConditions.visibilityOfElementLocated(locatorToWait));
    }

    public static void clickAndWaitForWebElement(WebDriver driver, WebElement elementToClick, By locatorToWait, int timeOutInSeconds) {
        elementToClick.click();

        new WebDriverWait(driver, timeOutInSeconds).
                until(ExpectedConditions.visibilityOf(elementToClick.findElement(locatorToWait)));
    }

    public static boolean stableFindRightClick(WebDriver driver, By by) throws WebDriverException {
        boolean result = false;
        WebDriverException webDrvExp = null;
        Actions action = new Actions(driver);
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                action.contextClick(driver.findElement(by)).build().perform();
                result = true;
                break;
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.");
                webDrvExp = e;
                sleep(555);
            } catch (ElementNotVisibleException exp) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.");
                webDrvExp = exp;
                sleep(555);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.");
                webDrvExp = expn;
                sleep(555);
            } catch (WebDriverException ex) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                webDrvExp = ex;
                sleep(555);
            }
        }
        if (webDrvExp != null && !result) {
            throw webDrvExp;
        }
        return true;
    }

    public static boolean stableMoveToElement(WebDriver driver, By by) throws WebDriverException {
        boolean result = false;
        WebDriverException webDrvExp = null;
        Actions action = new Actions(driver);
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                action.moveToElement(driver.findElement(by)).build().perform();
                result = true;
                break;
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.");
                webDrvExp = e;
                sleep(555);
            } catch (ElementNotVisibleException exp) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.");
                webDrvExp = exp;
                sleep(555);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.");
                webDrvExp = expn;
                sleep(555);
            } catch (WebDriverException ex) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                webDrvExp = ex;
                sleep(555);
            }
        }
        if (webDrvExp != null && !result) {
            throw webDrvExp;
        }
        return true;
    }

    public static String stableFindGetText(SearchContext driver, By by) throws WebDriverException {
        String elmText = null;
        WebDriverException webDrvExp = null;
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                elmText = driver.findElement(by).getText();
                break;
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.");
                webDrvExp = e;
                sleep(555);
            } catch (ElementNotVisibleException exp) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.");
                webDrvExp = exp;
                sleep(555);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.");
                webDrvExp = expn;
                sleep(555);
            } catch (WebDriverException ex) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                webDrvExp = ex;
                sleep(555);
            }
        }
        if (elmText == null && webDrvExp != null) {
            throw webDrvExp;
        }
        return elmText;
    }

    public static String stableFindGetText(SearchContext driver, By by, int attemptsPerElementFound, int textRefreshTimeInMilliSec) {

        String elmText = null;
        WebDriverException webDrvExp = null;

        for (int attempts = 0; attempts < attemptsPerElementFound && elmText == null; attempts++) {
            try {
                elmText = driver.findElement(by).getText();
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.", e);
                webDrvExp = e;
                sleep(textRefreshTimeInMilliSec);
            } catch (ElementNotVisibleException ex) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.", ex);
                webDrvExp = ex;
                sleep(textRefreshTimeInMilliSec);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.", expn);
                webDrvExp = expn;
                sleep(textRefreshTimeInMilliSec);
            } catch (WebDriverException exp) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.", exp);
                webDrvExp = exp;
                sleep(textRefreshTimeInMilliSec);
            }
        }

        if (elmText == null && webDrvExp != null) {
            log.debug("Couldn't find text for the given 'By' that was given: " + by.toString());
            throw webDrvExp;
        }
        return elmText;
    }

    public static String stableFindGetAttribute(SearchContext driver, By by, String attrName) throws WebDriverException {
        String attrValue = null;
        WebDriverException webDrvExp = null;
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                attrValue = driver.findElement(by).getAttribute(attrName);
                break;
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.");
                webDrvExp = e;
                sleep(555);
            } catch (ElementNotVisibleException exp) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.");
                webDrvExp = exp;
                sleep(555);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.");
                webDrvExp = expn;
                sleep(555);
            } catch (WebDriverException ex) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                webDrvExp = ex;
                sleep(555);
            }
        }

        if (attrValue == null && webDrvExp != null) {
            throw webDrvExp;
        }
        return attrValue;
    }

    public static void safeSendKeys(SearchContext driver, By by, String keysToSend) {
        sleep(500);
        driver.findElement(by).sendKeys(keysToSend);
    }

    public static void stableClearSendKeys(SearchContext driver, By by, String keysToSend) throws WebDriverException {
        WebDriverException webDrvExp = null;
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                driver.findElement(by).clear();
                Thread.sleep(200);
                driver.findElement(by).sendKeys(keysToSend);
                webDrvExp = null;
                break;
            } catch (NoSuchElementException e) {
                log.debug("Caught 'NoSuchElementException' in: '" + attempts + "' attempt.");
                webDrvExp = e;
                sleep(555);
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.");
                webDrvExp = e;
                sleep(555);
            } catch (ElementNotVisibleException exp) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.");
                webDrvExp = exp;
                sleep(555);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.");
                webDrvExp = expn;
                sleep(555);
            } catch (WebDriverException ex) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                webDrvExp = ex;
                sleep(555);
            } catch (InterruptedException ignored) {
            }
        }

        if (webDrvExp != null) {
            throw webDrvExp;
        }
    }

    public static void stableSwitchToFrame(String xPath, WebDriver driver) throws WebDriverException {
        stableSwitchToFrame(xPath, driver, 555); // backward compatability, temporary
    }

    public static void stableSwitchToFrame(String xPath, WebDriver driver, int refreshTimeInMilliSec) throws WebDriverException {
        WebElement frame = null;
        WebDriverException webDrvExp = null;
        By by = By.xpath(xPath);
        for (int attempts = 0; attempts < 3; ++attempts) {
            try {
                frame = driver.findElement(by);
                driver.switchTo().frame(frame);
                break;
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.", e);
                frame = null;
                webDrvExp = e;
                sleep(refreshTimeInMilliSec);
            } catch (ElementNotVisibleException ex) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.", ex);
                frame = null;
                webDrvExp = ex;
                sleep(refreshTimeInMilliSec);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.", expn);
                frame = null;
                webDrvExp = expn;
                sleep(refreshTimeInMilliSec);
            } catch (WebDriverException exp) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.", exp);
                frame = null;
                webDrvExp = exp;
                sleep(refreshTimeInMilliSec);
            }
        }
        if (frame == null) {
            throw webDrvExp;
        }
    }

    public static WebElement findElementWithTimeLimit(WebDriver driver, By by, int timeoutInSec) throws WebDriverException {
        driver.manage().timeouts().implicitlyWait(timeoutInSec, TimeUnit.SECONDS);

        WebElement expectedElm = null;
        WebDriverException ex = null;
        try {
            expectedElm = driver.findElement(by);
        } catch (WebDriverException e) {
            log.debug("Fail to find element. Exception: '" + e.getClass() + "', Message: '" + e.getMessage() + "'.");
            ex = e;
        } finally {
            driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        }

        if (ex != null) {
            throw ex;
        } else {
            return expectedElm;
        }
    }

    public static List<WebElement> findElementsWithTimeLimit(WebDriver driver, By by, int timeoutInSec) throws WebDriverException {
        driver.manage().timeouts().implicitlyWait(timeoutInSec, TimeUnit.SECONDS);

        List<WebElement> expectedElms = null;
        WebDriverException ex = null;
        try {
            expectedElms = driver.findElements(by);
        } catch (WebDriverException e) {
            log.debug("Fail to find element. Exception: '" + e.getClass() + "', Message: '" + e.getMessage() + "'.");
            ex = e;
        } finally {
            driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        }

        if (ex != null) {
            throw ex;
        } else {
            return expectedElms;
        }
    }

    public static boolean isElementExist(SearchContext driver, By by) {
        if (driver.findElements(by).isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isElementExists(WebDriver driver, By by) {
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isElementExistWithTimeLimit(WebDriver driver, By by, int timeoutInSec) {
        driver.manage().timeouts().implicitlyWait(timeoutInSec, TimeUnit.SECONDS);

        try {
            driver.findElement(by);
        } catch (WebDriverException e) {
            log.debug("Caught 'WebDriverException', element doe's not exist.");
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        }

        return true;
    }

    public static void releaseActiveFocus(SearchContext driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("document.activeElement.blur();");

        log.debug("Released focus from active element.");
    }

    private static void sleep(int milliSec) {
        try {
            Thread.sleep(milliSec);
        } catch (InterruptedException e) {
            log.error("InterruptedException: ", e);
        }
    }

    public static boolean isClickableWaitInSec(WebDriver driver, By by, int timeoutInSec) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (TimeoutException e) {
            return false;
        }

        return WebDriverHelper.stableFindElement(driver, by, timeoutInSec).isEnabled();
    }

    public static void scrollToTop(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
        sleep(555);
    }

    public static void clickElementWithJSInCaseOfFailure(WebDriver driver, WebElement elementToClick, int timeout) {
        try {
            clickWhenReady(driver, elementToClick, timeout);
        } catch (WebDriverException e) {
            JavascriptExecutor executor = (JavascriptExecutor)driver;
            executor.executeScript("arguments[0].click();", elementToClick);
        }
    }

    public static void scrollToElement(SearchContext driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        sleep(555);
    }

    public static void stableScrollToElement(SearchContext driver, By by) {
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(by));
            } catch (WebDriverException e) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                sleep(555);
            }
        }
    }

    public static boolean stableDragAndDrop(WebDriver driver, WebElement fromElement, WebElement toElement) {
        boolean result = false;
        WebDriverException webDrvExp = null;
        Actions action = new Actions(driver);
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                action.dragAndDrop(toElement, fromElement).build().perform();
                result = true;
                break;
            } catch (StaleElementReferenceException e) {
                log.debug("Caught 'StaleElementReferenceException' in: '" + attempts + "' attempt.");
                webDrvExp = e;
                sleep(555);
            } catch (ElementNotVisibleException exp) {
                log.debug("Caught 'ElementNotVisibleException' in: '" + attempts + "' attempt.");
                webDrvExp = exp;
                sleep(555);
            } catch (InvalidElementStateException expn) {
                log.debug("Caught 'InvalidElementStateException' in: '" + attempts + "' attempt.");
                webDrvExp = expn;
                sleep(555);
            } catch (WebDriverException ex) {
                log.debug("Caught 'WebDriverException' in: '" + attempts + "' attempt.");
                webDrvExp = ex;
                sleep(555);
            }
        }
        if (webDrvExp != null && !result) {
            throw webDrvExp;
        }
        return true;
    }

    public static boolean isCheckBoxChecked(SearchContext driver, By byCheckBoxPath) {
        String attr = driver.findElement(byCheckBoxPath).getAttribute("checked");
        return "true".equals(attr);
    }

    public static void clickWhenReady(WebDriver driver, By buttonLocator) {
        clickWhenReady(driver, buttonLocator, 2);
    }


    public static void delayedClickWhenReady(WebDriver driver, By buttonLocator, int delayInMs) {
        try {
            Thread.sleep(delayInMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clickWhenReady(driver, buttonLocator);
    }

    public static void delayedClickWhenReady(WebDriver driver, WebElement webElement, int delayInMs) {
        try {
            Thread.sleep(delayInMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clickWhenReady(driver, webElement);
    }

    public static void clickWhenReady(WebDriver driver, By buttonLocator, int timeoutSec) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSec);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
        element.click();

        sleep(500);
    }

    public static void clickWhenReady(WebDriver driver, WebElement clickableElement) {
        clickWhenReady(driver, clickableElement, 5);
    }

    public static void clickWhenReady(WebDriver driver, WebElement clickableElement, int timeoutSec) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSec);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(clickableElement));
        element.click();
    }

    public static BrowserType getBrowserType(WebDriver driver) {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName();
        for (BrowserType val : BrowserType.values()) {
            if (val.getName().equals(browserName)) {
                return val;
            }
        }
        return BrowserType.UNKNOWN;
    }

    public static WebElement fluentWait(WebDriver driver, By locator, int timeoutInSec, int pollingInSec) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(timeoutInSec, TimeUnit.SECONDS)
                .pollingEvery(pollingInSec, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        return fluentWait.until(webDriver -> webDriver.findElement(locator));
    }

    public static String getAllTextInPage(SearchContext driver) {
        WebElement body = driver.findElement(By.tagName("body"));
        return body.getText();
    }

    public static void refreshBrowser(WebDriver driver) {
        driver.navigate().refresh();
    }
}
