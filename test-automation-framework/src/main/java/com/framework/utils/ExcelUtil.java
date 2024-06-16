package com.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

//	readFirstRow
//	@param fileName
//	@param sheetName
//	@return ListString

	public List<String> readFirstRow(String fileflame, String sheetName) {

		List<String> fieldsArrayList = new ArrayList<String>();

		try {
			FileInputStream myInput = new FileInputStream(new File(fileflame));

			Workbook workBook = new XSSFWorkbook(myInput);
			Sheet sheet = workBook.getSheet(sheetName);
			Row firstRow = sheet.getRow(0);
			int length = firstRow.getLastCellNum();
			Cell cell = null;
			for (int i = 0; i < length; i++) {
				cell = firstRow.getCell(i);
				fieldsArrayList.add(cell.toString());
			}
			workBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldsArrayList;
	}

//	readExcelDataToListOfMaps
//	@param fileName
//	@param sheetName
//	@return List<Map<String, String>>

	// mainly used this for all test
	public List<Map<String, String>> readExcelDataToListOfMaps(String fileName, String sheetName) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<String> headers = new ArrayList<String>();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			// create an excel workwook feom filesystem
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			// get the first sheet on the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			XSSFRow firstRow = sheet.getRow(0);
			int length = firstRow.getLastCellNum();
			for (int i = 0; i < length; i++) {
				XSSFCell cell = firstRow.getCell(i);
				headers.add(cell.toString());
			}
			System.out.println("headers: " + headers);
			int lastRowNum = sheet.getLastRowNum();

			for (int j = 1; j <= lastRowNum; j++) {
				Map<String, String> rowData = new HashMap<String, String>();
				XSSFRow row = sheet.getRow(j);
				String cellValue = "";
				for (int k = 0; k < length; k++) {
					try {
						XSSFCell c = row.getCell(k);
						if (c == null || c.getCellType() == CellType.BLANK) {
							// this cell is empty
							cellValue = "";
						} else {
							cellValue = row.getCell(k).toString();
						}
					} catch (Exception e) {

					}
					rowData.put(headers.get(k), cellValue);
				}
				data.add(rowData);
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

//	readFirstRow
//	@param fileName
//	@param sheetName
//	@return ListString

	public Map<Integer, Map<String, String>> readExcelDataToMapOfMaps(String fileName, String sheetName) {
		Map<Integer, Map<String, String>> data = new HashMap<Integer, Map<String, String>>();
		List<String> headers = new ArrayList<String>();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			// create an excel workwook feom filesystem
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			// get the first sheet on the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			XSSFRow firstRow = sheet.getRow(0);
			int length = firstRow.getLastCellNum();
			for (int i = 0; i < length; i++) {
				XSSFCell cell = firstRow.getCell(i);
				headers.add(cell.toString());
			}
			System.out.println("headers: " + headers);
			int lastRowNum = sheet.getLastRowNum();

			for (int j = 1; j <= lastRowNum; j++) {
				Map<String, String> rowData = new HashMap<String, String>();
				XSSFRow row = sheet.getRow(j);
				String cellValue = "";
				for (int k = 0; k < length; k++) {
					try {
						XSSFCell c = row.getCell(k);
						if (c == null || c.getCellType() == CellType.BLANK) {
							// this cell is empty
							cellValue = "";
						} else {
							cellValue = row.getCell(k).toString();
						}
					} catch (Exception e) {

					}
					rowData.put(headers.get(k), cellValue);
				}
				data.put(j, rowData);
			}
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return data;
	}

//	writeBulkDataToExcel
//	@param fileName
//	@param sheetName
//	@param data
//	@throws Exception

	public void writeBulkDataToExcel(String fileName, String sheetName, Map<Integer, Map<String, String>> data)
			throws Exception {
		List<String> cols = readFirstRow(fileName, sheetName);
		FileInputStream inputStream = new FileInputStream(fileName);
		XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
		inputStream.close();

		SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
		wb.setCompressTempFiles(true);

		wb.createSheet("OutPut");
		SXSSFSheet sh = (SXSSFSheet) wb.getSheet("OutPut");
		sh.setRandomAccessWindowSize(100); // keep 100 rows in memory, exceeding rows will be flushed to disk

		// sheet headers
		Row row0 = sh.createRow(0);
		for (int cnum = 0; cnum < cols.size(); cnum++) {
			Cell c = row0.createCell(cnum);
			c.setCellValue(cols.get(cnum));
		}

		// set data

		for (int rownum = 1; rownum <= data.size(); rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < cols.size(); cellnum++) {
				Cell cell = row.createCell(cellnum);
				cell.setCellValue(data.get(rownum).get(cols.get(cellnum)));
			}
		}

		FileOutputStream out = new FileOutputStream(fileName);
		wb.write(out);
		out.close();
		wb.close();

	}

//	setCellData
//	@param path
//	@param sheetName
//	@param colName
//	@param rowName
//	@param data
//	@throws true if data is set succesfully elze false

	public boolean setCellData(String path, String sheetName, String colName, int rowNum, String data) {

		try {
			FileInputStream fis = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0) {
				workbook.close();
				return false;
			}

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1) {
				workbook.close();
				return false;
			}

			Sheet sheet = workbook.getSheetAt(index);

			Row row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
					colNum = i;
				}
			}
			if (colNum == -1) {
				workbook.close();
				return false;
			}

			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			Cell cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			// cell style
			CellStyle cs = workbook.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(data);

			FileOutputStream fileOut = new FileOutputStream(path);

			workbook.write(fileOut);
			workbook.close();
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
//	readExcelDataAsGroupOfRows
//	@param fileName
//	@param sheetName
//	@return
	
	public Map<String, List<Map<String, String>>> readExcelDataAsGroupOfRows(String fileName, String sheetName) {
		List<Map<String, String>> data = readExcelDataToListOfMaps(fileName, sheetName);
		
		String testCaseId = "";
		String priviouTextCaseId;
		List<Map<String, String>> testCaseRow = null;
		Map<String, List<Map<String, String>>> testData = new HashMap<String, List<Map<String,String>>>();
		
		for(int i=0; i<data.size(); i++) {
			
			Map<String, String > row = data.get(i);
			testCaseId = row.get("TestCaseId");
			if(i==0) {
				priviouTextCaseId = testCaseId;
				testCaseRow = new ArrayList<Map<String,String>>();						
			} else {
				priviouTextCaseId = data.get(i-1).get("TestCaseId");
			}
			
			if(!testCaseId.equals(priviouTextCaseId)) {
				testCaseRow = new ArrayList<Map<String,String>>();
			}
			
			testCaseRow.add(row);
			testData.put(testCaseId, testCaseRow);
		}
		return testData;
	}
	
	
	
	
	
	

	

}
