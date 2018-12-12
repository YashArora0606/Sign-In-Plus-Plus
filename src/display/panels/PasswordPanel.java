/**
 * [PasswordPanel.java]
 * the panel which prompts the user for the password to enter the teacher dashboard
 * checks the password with the password text file and then rewrites the password to the text file
 * December 2 2018
 */

package display.panels;

import display.Window;
import display.customcomponents.CustomButton;
import utilities.Utils;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PasswordPanel extends JPanel{
    private Window display;
    private JPanel panel;
    private JTextField passwordField;
    private int maxX;
    private int maxY;
    private CustomButton back;
    private CustomButton submit;
    private boolean attempted = false;

    /**
     * Constructor
     * @param display the Window object to which this panel belongs
     */
    public PasswordPanel(Window display) {
        this.display = display;
        this.panel = this;
        this.maxX = display.maxX;
        this.maxY = display.maxY;

        this.setLayout(null); // allows for absolute positioning
        passwordField = new JTextField(20); //initializing the length of the password field
        Font mainFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45.0)); //retrieving the font
        passwordField.setFont(mainFont);

        Dimension size = passwordField.getPreferredSize();
        this.add(passwordField);
        passwordField.setBounds(maxX/2-Utils.scale(size.width/2),  //sets position on the screen
                maxY/2-2*Utils.scale(size.height), Utils.scale(size.width), Utils.scale(size.height));

        this.addMouseListener(new MyMouseListener());
        
        setBackground(Utils.colours[1]);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //back button leads to the home panel
        back = new CustomButton("Back",0,0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
        back.draw(g, panel);

        //submit button submits the password - if successful will advance to the teacher dashboard
        submit = new CustomButton("Submit",maxX/2-Utils.scale(100), maxY/2, Utils.scale(200), Utils.scale(80), Utils.colours[2]);
        submit.draw(g, panel);

        //font for the error message
        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30));
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);

        //font for the title
        Font titleFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45));

        g.setColor(Utils.colours[0]);
        g.setFont(titleFont);

        //Making a label using the custom button (for ease of text positioning)
        CustomButton passwordLabel = new CustomButton("Password", display.maxX/2 - Utils.scale(200)/2,
        		passwordField.getY() - (int)(Utils.scale(50)*(1.2)), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        passwordLabel.setSelectable(false); //setting the label to unclickable
        passwordLabel.draw(g, panel);
        
        g.setFont(errorFont);
        if (attempted) { //if an unsuccessful attempt has been made, will print error message
            g.drawString("Wrong password, please try again.",
                    maxX / 2 - errorFontMetrics.stringWidth("Wrong password, please try again.")/2,
                    maxY/4);
        }

        repaint();
    }

    /**
     * Reads the password file and calls on method to rewrite it to the file
     * @return the encrypted password
     */
    private String retrievePassword(){
        try{
            File myFile = new File("assets/password.txt"); //loading the file
            Scanner input = new Scanner(myFile);
            String password = input.nextLine();
            input.close(); //closing the file scanner
            return password;
        } catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    /**
     * Checks if it is the correct password
     * @return true if it is, false if it isn't
     */
    private boolean validPassword(){
        if (retrievePassword()!=null){
            return (passwordField.getText().equals(Utils.decode(retrievePassword())));
        }
        return false;
    }

    /**
     * The method to reinitialize validation variables when someone leaves the screen
     * Reinitializes textfields as well
     * @param state indicates which state to switch to
     */
    private void leaveScreen(int state){
        attempted = false;
        passwordField.setText("");
        display.changeState(state);
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e){

        }

        /**
         * Checks which button was clicked and responds accordingly
         * @param e the mouse event which occurred
         */
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)){ //back button leads to home panel
                leaveScreen(0);
            } else if (submit.isMouseOnButton(panel)){ //if submit button is clicked, will check password
                if (validPassword()){ //if it is correct, will change display panel to the dashboard
                    leaveScreen(5);
                } else { // if it is incorrect, will empty the text field
                    passwordField.setText("");
                    attempted = true; //sets the boolean variable to display the error message
                }
            }
        }
        public void mousePressed(MouseEvent e){

        }
        public void mouseExited(MouseEvent e){

        }
        public void mouseReleased(MouseEvent e){

        }
    }
}
