package display;

import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagment.SignInManager;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import utilities.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class RemoveStudentPanel extends JPanel {
    private JPanel panel;
    private Window display;
    private SignInManager signInManager;

    private JTextField idField;
    private CustomButton submit;
    private CustomButton back;
    private int attemptSuccessful = 0;
    private CustomButton studentId;

    private Dimension idSize;


    private int x;
    private int y;

    RemoveStudentPanel(Window display) {
        this.panel = this;
        this.display = display;
        this.addMouseListener(new MyMouseListener());
        this.setLayout(null);
        this.setBackground(Utils.colours[2]);

        this.x = display.maxX;
        this.y = display.maxY;

        idField = new JTextField(7);
        idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
        idField.setText("");
        idSize = idField.getPreferredSize();
        this.add(idField);
        this.addMouseListener(new MyMouseListener());

        idField.setBounds(display.maxX / 2 - idSize.width / 2, display.maxY / 2 - idSize.height - Utils.scale(100), idSize.width,
                idSize.height);

        idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
        idField.setBackground(null);

        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
        back.draw(g, this);

        submit = new CustomButton("Submit", x / 2 - Utils.scale(250) / 2, (int) (y * 0.5), Utils.scale(250),
                Utils.scale(80), Utils.colours[1]);
        submit.draw(g, this);

        studentId = new CustomButton("Student Id", display.maxX / 2 - Utils.scale(220)/2,
                display.maxY / 2 - idSize.height - Utils.scale(165), Utils.scale(220), Utils.scale(50), Utils.colours[4]);
        studentId.setSelectable(false);
        studentId.draw(g, this);

        g.setColor(Utils.colours[0]);

        if (attemptSuccessful == 1){

        }

        repaint();
    }

    private boolean submit() {
        return true;
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                display.changeState(5);
            }

            if (submit.isMouseOnButton(panel)) {
                if (submit()){
                    attemptSuccessful = 2;
                } else {
                    attemptSuccessful = 1;
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
