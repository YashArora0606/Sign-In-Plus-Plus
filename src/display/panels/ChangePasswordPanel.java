package display.panels;

import javax.swing.JPanel;
import javax.swing.JTextField;

import display.Window;
import display.customcomponents.CustomButton;
import utilities.Utils;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FontMetrics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ChangePasswordPanel extends JPanel {
    JTextField passwordField;
    private JPanel panel;
    private int maxX;
    private int maxY;
    private Window display;
    private int attemptSuccess = 0;
    private Font mainFont;
    private CustomButton back;
    private CustomButton submit;

    public ChangePasswordPanel(Window display){
        this.panel = this;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.display = display;

        this.setLayout(null);
        passwordField = new JTextField(20);
        mainFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45.0));
        passwordField.setFont(mainFont);
        Dimension size = passwordField.getPreferredSize();
        this.add(passwordField);
        passwordField.setBounds(maxX/2-Utils.scale(size.width/2), maxY/2-2*Utils.scale(size.height), Utils.scale(size.width), Utils.scale(size.height));

        setBackground(Utils.colours[1]);
        
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        back = new CustomButton("Back",0,0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
        back.draw(g, panel);


        submit = new CustomButton("Submit",maxX/2-Utils.scale(100), maxY/2, Utils.scale(200), Utils.scale(80), Utils.colours[2]);
        submit.draw(g, panel);

        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30));
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);
        Font titleFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45));
        FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
        g.setColor(Utils.colours[0]);
        g.setFont(titleFont);

        CustomButton passwordLabel = new CustomButton("Change password", display.maxX/2 - Utils.scale(250)/2,
        		passwordField.getY() - (int)(Utils.scale(50)*(1.2)), Utils.scale(250), Utils.scale(50), Utils.colours[4]);
        passwordLabel.setSelectable(false);
        passwordLabel.draw(g, panel);
        
        g.setFont(errorFont);
        if (attemptSuccess == 1) {
            g.drawString("Invalid Password. Please enter a password of 8+ characters",
                    maxX / 2 - errorFontMetrics.stringWidth("Invalid Password. Please enter a password of 8+ characters")/2,
                    maxY/4);
        } else if (attemptSuccess == 2){
            g.drawString("Successfully changed password!",
                    maxX / 2 - errorFontMetrics.stringWidth("Successfully changed password!")/2,
                    maxY/4);
        }

        repaint();
    }

    private boolean isValidPassword(String string){
        return (string.length()>=8);
    }

    private void writeToFile(String string){
        try{
            File passwordFile = new File("assets/password.txt");
            PrintWriter out = new PrintWriter(passwordFile);
            out.println(string);
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean submit(){
        String enteredPass = passwordField.getText();
        if (isValidPassword(enteredPass)){
            writeToFile(Utils.encode(enteredPass));
            return true;
        }
        return false;
    }

    public void leaveScreen(int state){
        attemptSuccess = 0;
        passwordField.setText("");
        display.changeState(state);
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e){

        }
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)){
                leaveScreen(5);
            } else if (submit.isMouseOnButton(panel)) {
                if (submit()) {
                    attemptSuccess = 2;
                    passwordField.setText("");
                } else {
                    attemptSuccess = 1;
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
