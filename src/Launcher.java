import datamanagment.Database;
import datamanagment.DerbyDatabase;
import datamanagment.SignInManager;
import datamanagment.Student;
import display.Window;
import exceptions.StudentAlreadyExistsException;
import exceptions.StudentDoesNotExistException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

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
