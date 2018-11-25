package display;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagment.Database;
import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import utilities.Utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

class SignInPanel extends JPanel {

    private Window display;
    private Database database;

    private JTextField idField;
    private JComboBox<String> reasonField;
    private JComboBox<String> subjectField;
    private JComboBox<String> courseMissedField;

    private JButton submitButton;
    private JButton backButton;
    
    private int maxX;
    private int maxY;

    SignInPanel(Window display, Database database) {
        this.display = display;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        
        this.database = database;

        
        idField = new JTextField(10);
        idField.setFont(idField.getFont().deriveFont(35f));
        add(idField);


        reasonField = new JComboBox<>();
        reasonField.addItem("Select Reason");
        for (String reason : database.getReasons()) {
            reasonField.addItem(reason);
        }
        add(reasonField);


        subjectField = new JComboBox<>();
        subjectField.addItem("Select Subject");
        for (String subject : database.getCourses()) {
            subjectField.addItem(subject);
        }
        add(subjectField);

        courseMissedField = new JComboBox<>();
        courseMissedField.addItem("Select Missing Course");
        for (String courseMissed : database.getCourses()) {
            courseMissedField.addItem(courseMissed);
        }
        add(courseMissedField);


        submitButton = new JButton("Sign In");
        submitButton.addActionListener(e -> submit());
        add(submitButton);


        backButton = new JButton("Back");
        backButton.addActionListener(e -> display.changeState(1));
        add(backButton);

        setVisible(true);
    }

    private void submit() {
        String id = idField.getText();
        String subject = (String)subjectField.getSelectedItem();
        String reason = (String)reasonField.getSelectedItem();
        String courseMissed = (String)courseMissedField.getSelectedItem();


        try {
            database.signIn(id, subject, reason, courseMissed);
            idField.setText(null);

        } catch (InvalidIdException | AlreadyLoggedInException e) {
            e.printStackTrace();
        }
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e){
        }
        public void mouseClicked(MouseEvent e) {
        }
        public void mousePressed(MouseEvent e){
        }
        public void mouseExited(MouseEvent e){
        }
        public void mouseReleased(MouseEvent e){
        }
    }
}
