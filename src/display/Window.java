package display;

import datamanagement.SignInManager;
import display.panels.AddStudentPanel;
import display.panels.ChangePasswordPanel;
import display.panels.ChangeSertsPanel;
import display.panels.GenerateSheetPanel;
import display.panels.HomePanel;
import display.panels.MenuPanel;
import display.panels.PasswordPanel;
import display.panels.RemoveStudentPanel;
import display.panels.SignInPanel;
import display.panels.SignOutPanel;
import display.panels.TeacherDashboardPanel;
import utilities.Utils;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main display hub for the program
 * All panels stem from this one
 */
public class Window extends JFrame {
    public final int maxX;
    public final int maxY;
    private SignInManager signInManager;

    private JPanel homePanel;
    private JPanel menuPanel;
    private JPanel signInPanel;
    private JPanel signOutPanel;
    private JPanel passwordPanel;
    private JPanel teacherDashboardPanel;
    private JPanel changePasswordPanel;
    private JPanel addStudentPanel;
    private JPanel removeStudentPanel;
    private JPanel generateSheetPanel;
    private JPanel changeSertsPanel;


    public Window(SignInManager signInManager) {
        super("SignIn++");
        this.signInManager = signInManager;

        //generate frame
        this.maxX = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        this.maxY = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation(Utils.scale(0), Utils.scale(0));
        this.setSize(new Dimension(maxX, maxY));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //generate panels
        this.homePanel = new HomePanel(this);
        this.menuPanel = new MenuPanel(this);
        this.signInPanel = new SignInPanel(this, signInManager);
        this.signOutPanel = new SignOutPanel(this, signInManager);
        this.passwordPanel = new PasswordPanel(this);
        this.teacherDashboardPanel = new TeacherDashboardPanel(this);
        this.changePasswordPanel = new ChangePasswordPanel(this);
        this.addStudentPanel = new AddStudentPanel(this, signInManager);
        this.removeStudentPanel = new RemoveStudentPanel(this,signInManager);
        this.generateSheetPanel = new GenerateSheetPanel(this, signInManager);
        this.changeSertsPanel = new ChangeSertsPanel(this, signInManager);

        ImageIcon icon = new ImageIcon("assets/bolt.png");
        this.setIconImage(icon.getImage());

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
     * changeState
     * Displays a panel based on state (0 = home, 1 = menu, 2 = sign in, 3 = sign out, 4 = password,
     * 5 = teacher dashboard, 6 = change password, 7 = add student, 8 = remove student, 9 = generate, 10 = change serts other = error)
     * @param state
     */
    public void changeState(int state) {
        switch (state) {
            case 0:
                switchPanel(homePanel);
                return;

            case 1:
                switchPanel(menuPanel);
                return;

            case 2:
                switchPanel(signInPanel);
                return;

            case 3:
                switchPanel(signOutPanel);
                return;

            case 4:
                switchPanel(passwordPanel);
                return;

            case 5:
                switchPanel(teacherDashboardPanel);
                return;

            case 6:
                switchPanel(changePasswordPanel);
                return;

            case 7:
                switchPanel(addStudentPanel);
//                ((AddStudentPanel) addStudentPanel).initialize();
                return;

            case 8:
                switchPanel(removeStudentPanel);
                return;

            case 9:
                switchPanel(generateSheetPanel);
                return;
                
            case 10:
            	switchPanel(changeSertsPanel);

//            default:
//                throw new IndexOutOfBoundsException();
        }
    }

    /**
     * closeWindow
     * Closes the window, we should really have a listener to detect this too
     * Make sure to do the necessary modifications to resolve the signInManager
     */
    public void closeWindow() {
        signInManager.close();
        dispose();
    }

    private void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().add(newPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }
}
