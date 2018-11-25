package display;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import utilities.Utils;

public class PasswordPanel extends JPanel{
    private Window display;

    private JTextField passwordField;
    private JButton backButton; //remove buttons when making it look pretty and add a mouse listener
    private JButton enterButton; //for debugging purposes to reach dashboard panel
    PasswordPanel(Window display) {
        this.display = display;

        /*backButton = new JButton("Back");
        backButton.addActionListener(e -> display.changeState(0));
        this.add(backButton);
*/
        passwordField = new JTextField(20);
        this.add(passwordField);

        enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> checkPassword());
        this.add(enterButton);
        this.addMouseListener(new MyMouseListener());
    }

    private String retrievePassword(){
        try{
            File myFile = new File("assets/password.txt");
            Scanner input = new Scanner(myFile);
            String password = input.nextLine();
            rewritePassword(password);
            input.close();
            return password;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void rewritePassword(String string){
        try{
            File myFile = new File("assets/password.txt");
            PrintWriter out = new PrintWriter(myFile);
            out.println(string);
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void checkPassword(){
        if (retrievePassword()!=null){
            if (passwordField.getText().equals(Utils.decode(retrievePassword()))) {
                display.changeState(5);
            }
            passwordField.setText("");
        }
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
