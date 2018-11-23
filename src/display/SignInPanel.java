package display;
import javax.swing.JButton; 
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagment.Database;
import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import utilities.Utils;

import java.io.IOException;

public class SignInPanel extends JPanel {

    private Window display;
    private Database database;

    private JTextField idField;     
    private JComboBox<String> reasonField;
    private JComboBox<String> subjectField;
    private JComboBox<String> courseMissedField;

    
    
    private JButton submitButton;
    private JButton backButton;

    public SignInPanel(Window display, Database database) {
        this.display = display;
        this.database = database;

        idField = new JTextField(10);
        idField.setFont(idField.getFont().deriveFont(35f));
        add(idField);


        reasonField = new JComboBox<>();
        reasonField.addItem("Select Reason");
        for (String reason : Utils.getReasons()) {
            reasonField.addItem(reason);
        }
        add(reasonField);


        subjectField = new JComboBox<>();
        subjectField.addItem("Select Subject");
        for (String subject : Utils.getSubjects()) {
            subjectField.addItem(subject);
        }
        add(subjectField);
        
        courseMissedField = new JComboBox<>();
        courseMissedField.addItem("Select Subject");
        for (String courseMissed : Utils.getCoursesMissed()) {
            courseMissedField.addItem(courseMissed);
        }
        add(courseMissedField);


        submitButton = new JButton("Sign In");
        submitButton.addActionListener(e -> submit());
        add(submitButton);


        backButton = new JButton("Back");
        backButton.addActionListener(e -> display.changeState(0));
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
}
