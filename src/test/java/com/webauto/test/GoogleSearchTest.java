package com.webauto.test;

import com.webauto.pages.GoogleHomePage;
import com.webauto.pages.GoogleSearchResultsPage;
import com.utils.ExcelUtils;
import com.utils.FmDBUtils;
import com.utils.FmExcelUtils;
import com.utils.JiraDefectCreation;
import com.webauto.reports.ExtentReportManager;

import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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

	private Map<String, String> datamap;
	public JiraDefectCreation jira = new JiraDefectCreation();

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

	public GoogleSearchTest() {

		FmExcelUtils Xslutil = new FmExcelUtils();

		String xlspath = "src/test/resources/testData_FM.xlsx";
		String searchvalue;

		datamap = Xslutil.getTestMethodData(xlspath, "Search_1");
		searchvalue = datamap.get("searchtext");
		System.out.println("Value from search text: " + searchvalue);

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

		} catch (AssertionError e) {
			/*
			 * String defectId = jira.createJiraDefect(searchText, e.getMessage());
			 * test.assignCategory("Bugs"); test.log(Status.FAIL, "Defect ID: " + defectId);
			 * test.log(Status.FAIL, "Defect: " + e.getMessage());
			 */

			String defectId = jira.createJiraDefect(searchText, e.getMessage());
			ExtentTest defectNode = test.createNode("Defect Details");
			defectNode.log(Status.FAIL, "JIRA Defect ID: " + defectId);
			defectNode.log(Status.FAIL, "Defect description : " + e.getMessage());
			defectNode.assignCategory("Bugs");
			test.log(Status.FAIL, "Test failed. Defect created in JIRA - Please find the details in the below tab");

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

		} catch (AssertionError e) {
			String defectId = jira.createJiraDefect(searchText, e.getMessage());
			ExtentTest defectNode = test.createNode("Defect Details");
			defectNode.log(Status.FAIL, "JIRA Defect ID: " + defectId);
			defectNode.log(Status.FAIL, "Defect description: " + e.getMessage());
			defectNode.assignCategory("Bugs");
			test.log(Status.FAIL, "Test failed. Defect created in JIRA - Please find the details in the below tab");

		}

	}

	@DataProvider(name = "testData")
	public Object[][] testData() {

		String[][] testData = ExcelUtils.readTestData("src/test/resources/testData.xlsx", "Sheet1");
		return testData;
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
		extent.flush();
	}

}
