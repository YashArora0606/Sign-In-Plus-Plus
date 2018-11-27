import datamanagment.SignInManager;
import display.Window;

/**
 * Basic launcher for the program - mainly for aesthetics
 */
public class Launcher {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        SignInManager signInManager = new SignInManager();
        Window frame = new Window(signInManager);
    }
}
