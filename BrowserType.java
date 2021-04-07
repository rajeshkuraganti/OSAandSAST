package com.cx.automation.adk.selenium;

/**
 * Created by: iland
 * Date: 5/6/2015
 */
public enum BrowserType {

    CHROME("chrome"),
    HTML_UNIT_DRIVER("HTML Unit Driver"),
    FIREFOX("firefox"),
    IE("internet explorer"),
    EDGE("edge"),
    UNKNOWN("Unknown");

    private String name;

    BrowserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
