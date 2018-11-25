package utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {

    private static DataFormatter formatter = new DataFormatter();

    public static XSSFWorkbook loadFile(String pathName) throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream(pathName);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();
        return workbook;
    }

    public static void saveFile( XSSFWorkbook workbook, String pathName) throws FileNotFoundException, IOException{
        File file = new File(pathName);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
    }

    public static String getValueFromCell(Cell cell) {
        return formatter.formatCellValue(cell);
    }

}
