package display;

import javax.swing.JPanel;
import javax.swing.JTextField;
import utilities.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ChangePasswordPanel extends JPanel {
    private static final int BUTTON_WIDTH = Utils.scale(150);
    private static final int BUTTON_HEIGHT = Utils.scale(80);
    JTextField passwordField;
    private int maxX;
    private int maxY;
    private Window display;
    private int attemptSuccess = 0;
    private Font mainFont;
    private boolean inWindow = false;
    private int highlight = -1;

    ChangePasswordPanel(Window display){
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.display = display;
        this.setLayout(null);
        passwordField = new JTextField(20);
        mainFont = Utils.getFont("assets/Hind-Light.ttf", 45.0f);
        passwordField.setFont(mainFont);
        passwordField.setText("New Password");
        Dimension size = passwordField.getPreferredSize();
        this.add(passwordField);
        passwordField.setBounds(maxX/2-size.width/2, maxY/2-size.height, size.width, size.height);
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font buttonFont = Utils.getFont("assets/Hind-Light.ttf", 38.0f);
        FontMetrics buttonFontMetrics = g.getFontMetrics(buttonFont);
        FontMetrics mainFontMetrics = g.getFontMetrics(mainFont);

        if (highlight == 0) {
            g.setColor(new Color(40, 40, 40));
        } else {
            g.setColor(new Color(133, 175, 255));
        }
        g.fillRect(40,40, BUTTON_WIDTH, BUTTON_HEIGHT);
        g.setColor(new Color(255, 255, 255));
        g.setFont(buttonFont);
        g.drawString("Back", BUTTON_WIDTH/2+40-buttonFontMetrics.stringWidth("Back")/2, BUTTON_HEIGHT /2+40+buttonFontMetrics.getMaxAscent()/4);

        if (highlight == 1) {
            g.setColor(new Color(40, 40, 40));
        } else {
            g.setColor(new Color(91, 166, 255));
        }
        g.fillRect(maxX/2-BUTTON_WIDTH/2, Utils.scale(420), BUTTON_WIDTH, BUTTON_HEIGHT);
        g.drawOval(500,420, 10,10);
        g.setColor(new Color(255,255,255));
        g.drawString("Submit", maxX/2-buttonFontMetrics.stringWidth("Submit")/2, BUTTON_HEIGHT/2+Utils.scale(420)+buttonFontMetrics.getMaxAscent()/4);

        g.setColor(new Color(245, 51, 84));
        if(attemptSuccess == 1) {
            g.drawString("Invalid Password. Please enter a password of 8+ characters",
                    maxX / 2 - buttonFontMetrics.stringWidth("Invalid Password. Please enter a password of 8+ characters")/2,
                    Utils.scale(360));
        } else if (attemptSuccess == 2){
            g.drawString("Successfully changed password!",
                    maxX / 2 - buttonFontMetrics.stringWidth("Successfully changed password!")/2,
                    Utils.scale(360));
        }
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = this.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX()-relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY()-relScreenLocation.getY());
        if (inWindow) {
            if ((x >= 40) && (x <= 40 + BUTTON_WIDTH) && (y >= 40) && (y <= 40 + BUTTON_HEIGHT)) {
                highlight = 0;
            } else if ((x >= maxX/2-BUTTON_WIDTH/2) && (x <= maxX/2-BUTTON_WIDTH/2+BUTTON_WIDTH)
                    && (y >= Utils.scale(420)) && (y <= Utils.scale(420)+BUTTON_HEIGHT)) {
                highlight = 1;
            } else {
                highlight = -1;
            }
        } else {
            highlight = -1;
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

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e){
            inWindow = true;
        }
        public void mouseClicked(MouseEvent e) {
            if ((e.getX() >= 40) && (e.getX() <= 40 + BUTTON_WIDTH) && (e.getY() >= 40) && (e.getY() <= 40 + BUTTON_HEIGHT)){
                display.changeState(5);
            } else if ((e.getX() >= maxX/2-BUTTON_WIDTH/2) && (e.getX() <= maxX/2-BUTTON_WIDTH/2+BUTTON_WIDTH)
                    && (e.getY() >= Utils.scale(420)) && (e.getY() <= Utils.scale(420)+BUTTON_HEIGHT)) {
                if (submit()) {
                    attemptSuccess = 2;
                } else {
                    attemptSuccess = 1;
                }
            }
        }
        public void mousePressed(MouseEvent e){
        }
        public void mouseExited(MouseEvent e){
            inWindow = false;
        }
        public void mouseReleased(MouseEvent e){
        }
    }
}
