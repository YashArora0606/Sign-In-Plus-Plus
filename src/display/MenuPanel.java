package display;
import javax.swing.JButton;
import javax.swing.JPanel;

class MenuPanel extends JPanel {

    private Window display; // remember to remove unnecessary variables later (note to self)

    private JButton signInButton;
    private JButton signOutButton;
    private JButton backButton;

    MenuPanel(Window display) {
        this.display = display;

        signInButton = new JButton("Sign-In");
        signInButton.addActionListener(e -> display.changeState(2));
        this.add(signInButton);


        signOutButton = new JButton("Sign-Out");
        signOutButton.addActionListener(e -> display.changeState(3));
        this.add(signOutButton);


        backButton = new JButton("Back");
        backButton.addActionListener(e -> display.changeState(0));
        this.add(backButton);
    }

}
