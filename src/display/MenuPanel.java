package display;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    private Window display; // remember to remove unnecessary variables later (note to self)

    private JButton signInButton;
    private JButton signOutButton;

    public MenuPanel(Window display) {
        this.display = display;

        signInButton = new JButton("Sign-In");
        signInButton.addActionListener(e -> display.changeState(1));
        this.add(signInButton);


        signOutButton = new JButton("Sign-Out");
        signOutButton.addActionListener(e -> display.changeState(2));
        this.add(signOutButton);
    }

}
