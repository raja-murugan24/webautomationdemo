package com.webauto.test;

import com.webauto.pages.GoogleHomePage;
import com.webauto.pages.GoogleSearchResultsPage;
import com.utils.ExcelUtils;
import com.webauto.reports.ExtentReportManager;

import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class GoogleSearchTest {

	private WebDriver driver;
	private GoogleHomePage homePage;
	private GoogleSearchResultsPage resultsPage;
	private ExtentReports extent;
	private ExtentTest test;
	private int totalTests = 0;
	private int passedTests = 0;
	private int failedTests = 0;

	@BeforeClass
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		driver = new ChromeDriver();
		homePage = new GoogleHomePage(driver);
		resultsPage = new GoogleSearchResultsPage(driver);
		extent = ExtentReportManager.createInstance("extent-report.html");

	}

	public WebDriver getDriver() {
		return driver;
	}

	@Test(priority = 1, dataProvider = "testData")
	@Description("Negative Scenario - Verify Google Search and first link navigation.")
	@Severity(SeverityLevel.NORMAL)
	public void testGoogleSearchNeg(String username, String password, String searchText) {
		test = extent.createTest("Google Search Results - " + searchText).assignCategory("Smoke");
		driver.get("https://www.google.com");
		// homePage.login(username, password); // Implement login if required.
		homePage.search(searchText);
		resultsPage.clickFirstLink();
		try {
			String expectedTitle = "Katalon Studio | Best Codeless Test Automation Tools";
			System.out.println(driver.getTitle().toString());
			Assert.assertTrue(driver.getTitle().contains(expectedTitle), "Actual title '" + driver.getTitle()
					+ "' does not contain the expected title '" + expectedTitle + "'.");

			test.log(Status.PASS, "Google search for '" + searchText + "' successful.");

			totalTests++;
			passedTests++;
		} catch (AssertionError e) {
			test.log(Status.FAIL, e.getMessage());
			totalTests++;
			failedTests++;
		}

	}

	@Test(priority = 2, dataProvider = "testData")
	@Description("Positive Scenario - Verify Google Search and first link navigation.")
	@Severity(SeverityLevel.NORMAL)
	public void testGoogleSearchPos(String username, String password, String searchText) {
		test = extent.createTest("Google Search Test - " + searchText).assignCategory("Smoke");
		driver.get("https://www.google.com");
		// homePage.login(username, password); // Implement login if required.
		homePage.search(searchText);
		resultsPage.clickFirstLink();
		try {
			String expectedTitle = "Katalon Studio - Google Search"; // Set the expected title of the opened page.
			System.out.println(driver.getTitle().toString());
			Assert.assertTrue(driver.getTitle().contains(expectedTitle), "Actual title '" + driver.getTitle()
					+ "' does not contain the expected title '" + expectedTitle + "'.");

			test.log(Status.PASS, "Google search for '" + searchText + "' successful.");
			totalTests++;
			passedTests++;
		} catch (AssertionError e) {
			test.log(Status.FAIL, e.getMessage());
			totalTests++;
			failedTests++;
		}

	}

	@DataProvider(name = "testData")
	public Object[][] testData() {
		return ExcelUtils.readTestData("src/test/resources/testData.xlsx", "Sheet1");
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
		extent.flush();
	}

}
