import datamanagment.Database;
import display.Window;

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

    private static void run() {
        Database database = new Database();
        Window window = new Window(database);
    }
}