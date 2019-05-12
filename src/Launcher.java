import datamanagement.Database; 
import datamanagement.DerbyDatabase;
import datamanagement.SignInManager;
import display.StartFrame;
import display.Window;

import javax.swing.SwingUtilities;

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

        SwingUtilities.invokeLater(() -> {
            StartFrame startFrame = new StartFrame();
            SwingUtilities.invokeLater(() -> new Window(signInManager));
            startFrame.dispose();
        });
    }
}
