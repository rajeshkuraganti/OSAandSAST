package com.cx.automation.adk.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This implementation is by no way fool proof
 * It is only a quick hack designed to serve a couple of required fixes.
 * <p>
 * This solution does not work for columns that have no clear UI text (chkSelect, scan scope, scan logs)
 *
 * @Author: giladm
 */
public class TablesHelper {

    /**
     * Returns a map of a table's Header names to their correlating Index
     * <br> <b>Requires locating the table's main Grid object.</b>
     *
     * @param tableGridLocator The Table's main Grid object locator
     * @return Map of header name as String to column index as Integer
     */
    public static Map<String, Integer> getGridTableHeadersToIndexMap(WebDriver driver, By tableGridLocator) {
        // Focus search on specific grid
        WebElement gridContext = WebDriverHelper.stableFindElement(driver, tableGridLocator, 300);
        // Find all grid header elements
        List<WebElement> webElements = gridContext.findElements(By.xpath("//div[contains(@id,'GridHeader')]//th[@scope='col']"));
        // Map all header elements name(text) to index
        return IntStream.range(0, webElements.size())
                .boxed()
                .collect(Collectors.toMap(
                        // key Mapping
                        i -> webElements.get(i).getText().trim(), // When sorted values return with a trailing space
                        // value Mapping
                        i -> i + 1, // XPath elements are indexed starting at 1
                        // merge (some elements have an empty name, these cause a collision by having the same map key)
                        // this simple implementation will throw one of the keys away.
                        // columns with duplicate names are mostly columns with no ui names (chkSelect, scan cope, scan logs)
                        (v1, v2) -> v1));
    }

    /**
     * Returns a map of a table's Header names to their correlating Index
     * <br> <b>Requires locating the hover table's main object.</b>
     *
     * @param hoverTableLocator The hover Table's main object locator
     * @return Map of header name as String to column index as Integer
     */
    public static Map<String, Integer> getHoverTableHeadersToIndexMap(WebDriver driver, By hoverTableLocator) {
        // Focus search on specific grid
        WebElement hoverContext = WebDriverHelper.stableFindElement(driver, hoverTableLocator, 300);
        // Find all hover header elements
        List<WebElement> webElements = hoverContext.findElements(By.xpath("//thead//th[contains(@class,'col')]"));
        // Map all header elements name(text) to index
        return IntStream.range(0, webElements.size())
                .boxed()
                .collect(Collectors.toMap(
                        // key Mapping
                        i -> webElements.get(i).getText().trim(), // When sorted values return with a trailing space
                        // value Mapping
                        i -> i + 1, // XPath elements are indexed starting at 1
                        // merge (some elements have an empty name, these cause a collision by having the same map key)
                        (v1, v2) -> v1));
    }
}
