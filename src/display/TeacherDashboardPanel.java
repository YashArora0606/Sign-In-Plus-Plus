package display;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import utilities.Utils;

public class TeacherDashboardPanel extends JPanel{
    private JPanel panel;
    private Window display;
    private final int maxX;
    private final int maxY;
    private CustomButton addStudent;
    private CustomButton removeStudent;
    private CustomButton changePassword;
    private CustomButton generateExcel;
    private CustomButton back;

    TeacherDashboardPanel(Window display){
        this.panel = this;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.display = display;
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        back = new CustomButton("Back",0,0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);

        addStudent = new CustomButton("Add Student", Utils.scale(maxX/2-325), Utils.scale(80), Utils.scale(300), Utils.scale(80), Utils.colours[1]);
        removeStudent = new CustomButton("Remove Student", Utils.scale(maxX/2+25), Utils.scale(80), Utils.scale(350), Utils.scale(80), Utils.colours[2]);
        changePassword = new CustomButton("Change Password", Utils.scale(maxX/2-400), Utils.scale(200), Utils.scale(375), Utils.scale(80), Utils.colours[3]);
        generateExcel = new CustomButton("Generate Excel Sheet", Utils.scale(maxX/2+25), Utils.scale(200), Utils.scale(400), Utils.scale(80), Utils.colours[4]);

        addStudent.draw(g, panel);
        removeStudent.draw(g, panel);
        changePassword.draw(g, panel);
        generateExcel.draw(g, panel);
        back.draw(g, panel);

        repaint();
    }

    private class MyMouseListener implements MouseListener{
        public void mouseEntered(MouseEvent e){

        }
        public void mouseClicked(MouseEvent e) {
            if (addStudent.isMouseOnButton(panel)){
                // add student panel
            } else if (removeStudent.isMouseOnButton(panel)){
                //remove student panel
            } else if (changePassword.isMouseOnButton(panel)) {
                display.changeState(6);
            } else if (generateExcel.isMouseOnButton(panel)) {
                //generate excel panel
            } else if (back.isMouseOnButton(panel)){
                display.changeState(1);
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
