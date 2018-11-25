package display;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PasswordPanel extends JPanel{
    private Window display;

    private JTextField idField;
    private JButton backButton; //remove buttons when making it look pretty and add a mouse listener
    private JButton enterButton; //for debugging purposes to reach dashboard panel
    PasswordPanel(Window display) {
        this.display = display;

        /*backButton = new JButton("Back");
        backButton.addActionListener(e -> display.changeState(0));
        this.add(backButton);

        enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> display.changeState(5));
        this.add(enterButton);*/
        this.addMouseListener(new MyMouseListener());
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e){

        }
        public void mouseClicked(MouseEvent e) {
            display.changeState(5);
        }
        public void mousePressed(MouseEvent e){

        }
        public void mouseExited(MouseEvent e){

        }
        public void mouseReleased(MouseEvent e){

        }
    }
}
