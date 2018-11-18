

/**
 * Basic launcher for the program - mainly for aesthetics
 */
public class Launcher {
    public static void main(String[] args) {
        run();
    }


    public static void run() {
        Database database = new Database();
        Display frame = new Display(database);
    }
}
