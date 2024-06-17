package com.framework.extentreports;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;



import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {
	final static Logger logger=LoggerFactory.getLogger(ExtentTestManager.class);
	
	private static Map<Integer , ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	private static ExtentReports extentReports = ExtentReportsFactory.getInstance();
	
	public static synchronized ExtentTest getTest() {
		ExtentTest test;
		
		try {
			test = ((ExtentTest) extentTestMap.get((int)(long)Thread.currentThread().getId()));
		} catch (java.lang.NullPointerException ex) {
			test = null;
			logger.info("Null Pointer Exception Occured in ExtentTestManager.getTest().");
		}
		return test;
	}
	
	public static synchronized void endTest() {
		ExtentTest test = (ExtentTest) extentTestMap.get((int)(long)Thread.currentThread().getId());
		extentReports.flush();
		logger.info("Extent Test Flush Completed - " + test.getModel().getName());
		
	}
	
	public static synchronized ExtentTest startTest(String testName) {
		logger.info("Extent Test Start Test Name - "+ testName);
		ExtentTest test = extentReports.createTest(testName, "");
		extentTestMap.put((int)(long)(Thread.currentThread().getId()), test);
		return test;
	}
	
	public static synchronized ExtentTest startTest(ExtentReports extentReports, String testName, String desc) {
		logger.info("Extent Test Start Test Name - " + testName);
		ExtentTest test = extentReports.createTest(testName, desc);
		extentTestMap.put((int)(long)(Thread.currentThread().getId()), test);
		return test;
	}
	
	public static synchronized ExtentTest startTest() {
		ExtentTest extentTest = getTest();
		ITestResult result = Reporter.getCurrentTestResult();
		
		String testMethodName = result.getMethod().getMethodName().toString()+"_"+System.currentTimeMillis();
		String testName = (String)result.getAttribute("testName");
		if(testName != null) {
			testMethodName = testName;
		}
		if(result.getMethod().getDescription()!=null&& !result.getMethod().getDescription().toString().isEmpty()) {
			testMethodName = result.getMethod().getDescription().toString();
		}
		
		try {
			if(extentTest == null) {
				logger.info("First Test Comfiguration is about to begin " + testMethodName + " Thread" + Thread.currentThread().getId());
				extentTest = startTest(testMethodName);
			} else if(!extentTest.getModel().getName().equals(testMethodName)) {
				logger.info("New Test Configuration is about to begin " + testMethodName + " Thread" + Thread.currentThread().getId());
				extentTest = startTest(testMethodName);
			} else {
				logger.info("Extent TestManager Found Test : " + extentTest.getModel().getName());
				return extentTest;
			}

		} catch (java.lang.NullPointerException ex) {
			logger.info("Start Report NPE Condition - Starting Extent Test : "+testMethodName + "Thread" + Thread.currentThread().getId());
			extentTest = startTest(testMethodName);
		}
		return extentTest;		
	}
	
	public static synchronized ExtentTest initExtentTest(ExtentTest extentTest) {
		
		ITestResult result = Reporter.getCurrentTestResult();
		String testName = (String)result.getAttribute("testName");
		if(testName !=null) {
			if(extentTest == null) {
				return ExtentTestManager.startTest();
			}
			if(!extentTest.getModel().getName().equals(testName)) {
				extentTest = ExtentTestManager.startTest();
			}
		} else {
			try {
				ExtentTest currentTest = extentTestMap.get((int)(long)(Thread.currentThread().getId()));
				if(currentTest == null) {
					extentTest = ExtentTestManager.startTest();
				} else {
					return currentTest;
				}
			} catch (Exception e) {
				extentTest = ExtentTestManager.startTest();
			}
		}
		return extentTest;
	}

}
