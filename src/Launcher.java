import datamanagment.Database;
import display.Window;

/**
 * Basic launcher for the program - mainly for aesthetics
 */
public class Launcher {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        Database database = new Database();
        Window frame = new Window(database);
    }
}
