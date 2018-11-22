package display;
import javax.swing.JFrame; 
import javax.swing.JPanel;

import datamanagment.Database;
import utilities.Utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main display hub for the program
 * All panels stem from this one
 */
public class Window extends JFrame {

    private Database database;

    private JPanel menuPanel;
    private JPanel signInPanel;
    private JPanel signOutPanel;

    public Window(Database database) {

        this.database = database;

        //generate frame
        this.setTitle("SignIn++");
        this.setLocation(Utils.scale(200), Utils.scale(100));
        this.setSize(Utils.scale(1000), Utils.scale(600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //generate panels
        this.menuPanel = new MenuPanel(this);
        this.signInPanel = new SignInPanel(this, database);
        this.signOutPanel = new SignOutPanel(this, database);

        //set displayed panel to menu
        changeState(0);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }
        });

        this.setVisible(true);
    }

    /**
     * Displays a panel based on state (0 = menu, 1 = sign in, 2 = sign out, other = error)
     * @param state
     */
    public void changeState(int state) {
        switch (state) {
            case 0:
                getContentPane().removeAll();
                getContentPane().add(menuPanel);
                getContentPane().revalidate();
                getContentPane().repaint();
                return;

            case 1:
                getContentPane().removeAll();
                getContentPane().add(signInPanel);
                getContentPane().revalidate();
                getContentPane().repaint();
                return;

            case 2:
                getContentPane().removeAll();
                getContentPane().add(signOutPanel);
                getContentPane().revalidate();
                getContentPane().repaint();
                return;

            default:
                System.out.print(state);
                throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Closes the window, we should really have a listener to detect this too
     * Make sure to do the necessary modifications to resolve the database
     */
    public void closeWindow() {
        database.close();
        dispose();
    }
}