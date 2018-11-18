/**
 * Database (very underdeveloped)
 */
public class Database {

    private final String[] reasons; //different reasons for sign in
    private final String[] subjects; //different subjects for sign in

    private ExcelManager master;

    public Database() {

        master = new ExcelManager("MasterList.xlsx");

        //instead of hardcoding reasons or subjects, we could read from a .txt file to make it more dynamic
        reasons = new String[] {
                "Test", "Chill Zone", "Quiet Work", "Academic Support", "Group Work"
        };

        subjects = new String[] {
                "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
                "Physical Ed.", "Technology Studies", "Social Sciences", "Lunch / Spare"
        };
    }
    

    public String[] getReasons() {
        return reasons;
    }

    public String[] getSubjects() {
        return subjects;
    }

    
    public boolean signIn(String id, String course, String reason) throws InvalidIdException {
        if (!validateId(id)) { //validate id
            throw new InvalidIdException(id);
        }

        master.signStudentIn(id, course, reason);
        return true;
    }
    
    public boolean signOut(String id) throws InvalidIdException {
        if (!validateId(id)) { //validate id
            throw new InvalidIdException(id);
        }

        return master.signStudentOut(id);
    }

    public void close() {
        master.closeExcelFile();
    }

    private boolean validateId(String id) {
        if (id == null || id.length() != 9  || !Utils.isAnInteger(id)) {
            return false;
        }
        return true;
    }
}
