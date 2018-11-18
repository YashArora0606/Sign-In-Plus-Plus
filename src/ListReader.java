import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ListReader {


    String fileName;
    XSSFWorkbook workbook;
    XSSFSheet spreadsheet;
    private ArrayList<Student> studentList = new ArrayList<>();
    FileInputStream inputStream;

    ListReader (String fileName) throws Exception {
        File file = new File(fileName);
        inputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(inputStream);
        spreadsheet = workbook.getSheet("Sheet1");
    }

    public void createStudents () {
        String id;
        String firstName;
        String lastName;
        String grade;
           for (int row = 1; row < spreadsheet.getLastRowNum() + 1; row++) {
                    id = spreadsheet.getRow(row).getCell(0).toString();
                    firstName = spreadsheet.getRow(row).getCell(1).toString();
                    lastName = spreadsheet.getRow(row).getCell(2).toString();
                    grade = spreadsheet.getRow(row).getCell(3).toString();
                    studentList.add(new Student(firstName, lastName, id, grade));
            }
            System.out.println(studentList.get(0).compareTo(studentList.get(1)));
            Collections.sort(studentList);
            System.out.println(studentList.get(0).compareTo(studentList.get(1)));
    }

    public ArrayList<Student> getStudentList () {
        return studentList;
    }
}
