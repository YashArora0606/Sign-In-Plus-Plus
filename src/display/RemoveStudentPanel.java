package display;

import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagement.SignInManager;
import exceptions.StudentDoesNotExistException;
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
    
    private CustomButton studentId;
    
    private int attemptValidation = 0;

    private Dimension idSize;


    private int maxX;
    private int maxY;

    RemoveStudentPanel(Window display, SignInManager signInManager) {
        this.panel = this;
        this.display = display;
        this.signInManager = signInManager;
        this.addMouseListener(new MyMouseListener());
        this.setLayout(null);
        this.setBackground(Utils.colours[2]);

        this.maxX = display.maxX;
        this.maxY = display.maxY;

        idField = new JTextField(7);
        idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
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

        submit = new CustomButton("Submit", maxX / 2 - Utils.scale(250) / 2, (int) Math.round(0.45*maxY), Utils.scale(250),
                Utils.scale(80), Utils.colours[1]);
        submit.draw(g, this);

        Font mainFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(40));
        FontMetrics mainFontMetrics = g.getFontMetrics(mainFont);
        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30));
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);

        g.setFont(mainFont);
        g.setColor(Utils.colours[0]);
        //g.drawString("Student Number", maxX/2-mainFontMetrics.stringWidth("Student Number")/2, maxY/3);
        
		studentId = new CustomButton("Student Id", display.maxX / 2 - Utils.scale(220)/2,
				display.maxY / 2 - idSize.height - Utils.scale(165), Utils.scale(220), Utils.scale(50), Utils.colours[4]);
		studentId.setSelectable(false);
		studentId.draw(g, this);

        g.setFont(errorFont);
        if (attemptValidation == 1){
            g.drawString("That is not an existing student number. Please try again.",
                    maxX / 2 - errorFontMetrics.stringWidth("That is not an existing student Number. Please try again.") / 2,
                    3*maxY/5);
        }else if (attemptValidation == 2){
            g.drawString("Successfully removed student",
                    maxX / 2 - errorFontMetrics.stringWidth("Successfully removed student") / 2,
                    3*maxY/5);
        }

        repaint();
    }

    private boolean removeStudent() throws StudentDoesNotExistException {
        if (idField.getText().length()!=9){
            return false;
        }
        return signInManager.removeStudent(Integer.parseInt(idField.getText()));
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                attemptValidation = 0;
                idField.setText("");
                display.changeState(5);
            }
            if (submit.isMouseOnButton(panel)) {
                try {
                    if (removeStudent()) {
                        attemptValidation = 2;
                    }
                } catch (StudentDoesNotExistException error) {
                    attemptValidation = 1;
                    error.printStackTrace();
                }
                idField.setText("");
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
