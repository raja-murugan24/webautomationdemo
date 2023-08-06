package com.webauto.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GoogleHomePage {

    private WebDriver driver;

    @FindBy(name = "q")
    private WebElement searchBox;

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        // Implement login if required (e.g., if it's a test scenario for a logged-in user).
    }

    public void search(String searchText) {
        searchBox.sendKeys(searchText);
        searchBox.submit();
    }
}
