package com.framework.extentreports;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.framework.comm.Helper;

public class ExtentReportsFactory {
	
	private static ExtentReports extentReports = null;
	
	protected ExtentReportsFactory() {
		//defeat instantiation
	}
	
	public static ExtentReports getInstance() {
		String reportPath = addDayFolder();
		if(reportPath == null) {
			reportPath = "target/extentReports";
		}
		reportPath = reportPath + "/TestAutomationReport_"+Helper.getDateTime("yyMMddHHmmssSSS")+".html";
		if(extentReports == null) {
			extentReports = new ExtentReports();
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			
			spark.config().setDocumentTitle("Automation Report");
			extentReports.attachReporter(spark);
		}
		
		return extentReports;
		
	}
	
	
	public static String addDayFolder() {
		String path = "target/extentreports/"+Helper.getDateTime("yyyyMMdd");
		try {
			Files.createDirectories(Paths.get(path));
			return path;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	

}
