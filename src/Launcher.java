import datamanagement.Database;
import datamanagement.DerbyDatabase;
import datamanagement.SignInManager;
import display.Window;
import exceptions.StudentAlreadyExistsException;

/**
 * Basic launcher for the program - mainly for aesthetics
 */
public class Launcher {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        Database database = new DerbyDatabase();
        SignInManager signInManager = new SignInManager(database);


        try {
            signInManager.addStudent(74264672, "Alston", "Lo", 12);
            signInManager.addStudent(74264672, "Alston", "Lo", 12);
        } catch (StudentAlreadyExistsException e) {
            e.printStackTrace();
        }

        Window frame = new Window(signInManager);
    }
}
