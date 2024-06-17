package com.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentTest;
import com.framework.extentreports.ExtentTestManager;


public class AssertWrapper {
	
	protected final static Logger logger = LoggerFactory.getLogger(AssertWrapper.class);
	public static ExtentTest extentTest;
	
	public static void setLogMsg(String msg) {
		extentTest = ExtentTestManager.initExtentTest(extentTest);
		extentTest.info(msg);
	}
	
	public static void setPassMsg(String msg) {
		extentTest = ExtentTestManager.initExtentTest(extentTest);
		extentTest.pass(msg);
	}
	
	public static void setWarningMsg(String msg) {
		extentTest = ExtentTestManager.initExtentTest(extentTest);
		extentTest.warning(msg);
	}
	

}
