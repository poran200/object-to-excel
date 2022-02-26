package org.data.exproter;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.data.exproter.exceptions.InvalidSheetException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SheetExporter {
    private Properties properties;

    public SheetExporter() {
        try {
            readProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readProperties() throws IOException {
        properties = new Properties();
        String propFileName = "application.properties";
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        if (resourceAsStream != null){
            properties.load(resourceAsStream);
        }else {
            throw  new FileNotFoundException("Property file ' "+propFileName +"' not found in the classpath ");
        }
    }
    public void writeExcel(String sheetName, List<Map<String, Object>> maps,List<String> columnNames) throws IOException {
        Workbook workbook = null;
        String fileName = properties.getProperty("ote.fileName");
        if (fileName.endsWith("xlsx")){
            workbook = new XSSFWorkbook();
        }else if (fileName.endsWith("xls")){
            workbook = new HSSFWorkbook();
        }else {
            throw new InvalidSheetException("Invalid file, should be xls or xlsx");
        }

        Sheet sheet = workbook.createSheet(sheetName);
        int rowIndex = 0;
        CellStyle style = workbook.createCellStyle();//Create style
        Font font = workbook.createFont();//Create font
        font.setBold(true);//Make font bold
        style.setFont(font);//set it to bold
        Row row = sheet.createRow(rowIndex++);
        int columnNameCounter = 0;
        for (String columnName : columnNames) {

            Cell cell = row.createCell(columnNameCounter++);
            cell.setCellValue(columnName);
            cell.setCellStyle(style);

        }
        int columnCount = columnNames.size();
        for (Map<String, Object> map : maps) {
            row = sheet.createRow(rowIndex++);
            int columnCounter = 0;
            while (columnCounter <columnCount){
                Cell cell = row.createCell(columnCounter);
                Object object = map.get(columnNames.get(columnCounter++));
                if (object instanceof String s){
                    cell.setCellValue(s);
                }else if (object instanceof Integer integer){
                    cell.setCellValue(integer);
                } else if (object instanceof Long l){
                    cell.setCellValue(l);
                }

            }
        }
        writeWorkbookAsFile(workbook, fileName);
    }

    private void writeWorkbookAsFile(Workbook workbook, String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

}
