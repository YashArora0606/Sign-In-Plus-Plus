package display;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * An Error Window that
 *  1) Displays a message
 *  2) Can be closed
 *  3) Will close the whole program upon being closed
 */
public class ErrorWindow {

    public ErrorWindow(String message) {
        JLabel label = new JLabel(message);
        JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

}
