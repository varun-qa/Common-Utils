package com.tests.excel;

import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.framework.utils.ExcelUtil;

public class TC01_excelTest {
	
	ExcelUtil excel = new ExcelUtil();
	
	@DataProvider	
	public Object[][] getData(){
		String workBookName = "Book1";
		String testDataSheetName = "Sheet1";
		String testDataExcelPath = TC01_excelTest.class.getResource("/testdata/"+workBookName+".xlsx").getPath();
		
		List<Map<String, String>> excelData = excel.readExcelDataToListOfMaps(testDataExcelPath, testDataSheetName);
		Object[][] testData = new Object[excelData.size()][1];
		for(int i = 0; i<excelData.size(); i++) {
			testData[i][0] = excelData.get(i);
		}
		
		//can be useed to run single test case in excel
//		Object[][] testdata2 = new Object[1][1];
//		testdata2[0][0] = excelData.get(0);
		
		return testData;
	}
	
	@Test(dataProvider = "getData")
	public void excelDemoTest(Map<String, String> requestData) {
		
		System.out.println("Excel Data : " + requestData);
		System.out.println("accountName From Excel Data : " + requestData.get("accountName"));
	}
	
}
