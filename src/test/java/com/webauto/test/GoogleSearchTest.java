package com.webauto.test;

import com.webauto.pages.GoogleHomePage;
import com.webauto.pages.GoogleSearchResultsPage;
import com.utils.ExcelUtils;
import com.webauto.reports.ExtentReportManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class GoogleSearchTest {

    private WebDriver driver;
    private GoogleHomePage homePage;
    private GoogleSearchResultsPage resultsPage;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", System.getenv("DRIVER"));
        driver = new ChromeDriver();
        homePage = new GoogleHomePage(driver);
        resultsPage = new GoogleSearchResultsPage(driver);
        extent = ExtentReportManager.createInstance("extent-report.html");
    }

    @Test(dataProvider = "testData")
    public void testGoogleSearch(String username, String password, String searchText) {
        test = extent.createTest("Google Search Test - " + searchText);

        driver.get("https://www.google.com");
        //homePage.login(username, password); // Implement login if required.
        homePage.search(searchText);
        resultsPage.clickFirstLink();

        String expectedTitle = "Intelligent Automation and process Intelligence |Automation Anywhere"; // Set the expected title of the opened page.
        Assert.assertTrue(driver.getTitle().contains(expectedTitle));

        test.log(Status.PASS, "Google search for '" + searchText + "' successful.");
    }

    @DataProvider(name = "testData")
    public Object[][] testData() {
        return ExcelUtils.readTestData("/WebappAutoDemo/src/test/java/resources/testData.xlsx", "Sheet1");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        extent.flush();
    }
}

