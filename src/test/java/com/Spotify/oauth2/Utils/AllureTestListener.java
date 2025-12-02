package com.Spotify.oauth2.Utils;

import io.qameta.allure.Allure;
import org.testng.*;

public class AllureTestListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        Allure.addAttachment("Suite Started", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        Allure.addAttachment("Suite Finished", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        Allure.addAttachment("Test Started", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.addAttachment("Test Passed", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.addAttachment("Test Failed", result.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.addAttachment("Test Skipped", result.getMethod().getMethodName());
    }
}
