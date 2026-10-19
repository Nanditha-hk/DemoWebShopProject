package Utilities;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {

    public static String getData(String sheetName, int row, int column) throws IOException {

    	FileInputStream fis = new FileInputStream("./src/test/resources/DemoWebShop_TestDataa.xlsx");
        Workbook wb = WorkbookFactory.create(fis);
        
        
        String data = wb.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
        return data;
    }
}
