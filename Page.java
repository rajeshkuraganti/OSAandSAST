package com.cx.automation.adk.selenium;

import org.openqa.selenium.WebDriver;

/**
 * Created by: iland
 * Date: 5/6/2015
 */
public interface Page<T extends Page<T>> {

    /**
     * Load page after navigation if needed.
     *
     * @return the specific page after navigation.
     */
    T loadPage(WebDriver driver);

    /**
     * @return true if on correct page and false otherwise.
     */
    boolean verifyPage(WebDriver driver);

}
