import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    private Display display;

    private JButton signInButton;
    private JButton signOutButton;
    private JButton closeButton;

    public MenuPanel(Display display) {
        this.display = display;

        signInButton = new JButton("Sign-In");
        signInButton.addActionListener(e -> display.changeState(1));
        this.add(signInButton);


        signOutButton = new JButton("Sign-Out");
        signOutButton.addActionListener( e -> display.changeState(2));
        this.add(signOutButton);


        closeButton = new JButton("Close Program");
        closeButton.addActionListener(e -> display.closeWindow());
        this.add(closeButton);
    }

}
