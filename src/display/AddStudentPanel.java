package display;

import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FontMetrics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;

import utilities.Utils;

public class AddStudentPanel extends JPanel {
    private Window display;

    private JTextField passwordField;
    private JPanel panel;
    private int maxX;
    private int maxY;
    private CustomButton back;
    private CustomButton submit;
    private boolean attempted = false;

    AddStudentPanel(Window display) {
        this.display = display;
        this.panel = this;
        this.maxX = display.maxX;
        this.maxY = display.maxY;

        this.setLayout(null);
        passwordField = new JTextField(20);
        Font mainFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45.0));
        passwordField.setFont(mainFont);
        Dimension size = passwordField.getPreferredSize();
        this.add(passwordField);
        passwordField.setBounds(maxX / 2 - Utils.scale(size.width / 2), maxY / 2 - 2 * Utils.scale(size.height), Utils.scale(size.width), Utils.scale(size.height));

        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
        back.draw(g, panel);


        submit = new CustomButton("Submit", maxX / 2 - Utils.scale(100), Utils.scale(350), Utils.scale(200), Utils.scale(80), Utils.colours[2]);
        submit.draw(g, panel);

        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30));
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);
        g.setFont(errorFont);
        g.setColor(Utils.colours[0]);

        if (attempted) {
            g.drawString("Wrong password, please try again.",
                    maxX / 2 - errorFontMetrics.stringWidth("Wrong password, please try again.") / 2,
                    Utils.scale(300));
        }

        repaint();
    }

    private String retrievePassword() {
        try {
            File myFile = new File("assets/password.txt");
            Scanner input = new Scanner(myFile);
            String password = input.nextLine();
            input.close();
            rewritePassword(password);
            return password;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void rewritePassword(String string) {
        try {
            File myFile = new File("assets/password.txt");
            PrintWriter out = new PrintWriter(myFile);
            out.println(string);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validPassword() {
        if (retrievePassword() != null) {
            return (passwordField.getText().equals(Utils.decode(retrievePassword())));
        }
        return false;
    }

    public void leaveScreen(int state) {
        attempted = false;
        passwordField.setText("");
        display.changeState(state);
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e) {

        }

        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                leaveScreen(0);
            } else if (submit.isMouseOnButton(panel)) {
                if (validPassword()) {
                    leaveScreen(5);
                } else {
                    passwordField.setText("");
                    attempted = true;
                }
            }
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {

        }
    }
}
