package com.webauto.reports;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.webauto.test.GoogleSearchTest;
import com.webauto.test.GoogleSearchTestAllure;

public class AllureReport extends TestListenerAdapter {

	@Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = ((GoogleSearchTestAllure) testClass).getDriver();

        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            saveScreenshot(screenshot);
        }
    }

    public void saveScreenshot(byte[] screenshot) {
    	Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", screenshot);;
    }
}

