import datamanagement.Database;
import datamanagement.DerbyDatabase;
import datamanagement.SignInManager;
import display.Window;
import exceptions.StudentAlreadyExistsException;
import exceptions.StudentDoesNotExistException;

import java.io.IOException;

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
        Window frame = new Window(signInManager);
    }
}
