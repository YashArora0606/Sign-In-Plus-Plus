import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.IOException;

public class SignInPanel extends JPanel {

    private Display display;
    private Database database;

    private JTextField idField;
    private JComboBox<String> reasonField;
    private JComboBox<String> subjectField;
    private JButton submitButton;
    private JButton backButton;

    public SignInPanel(Display display, Database database) {
        this.display = display;
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
        for (String subject : database.getSubjects()) {
            subjectField.addItem(subject);
        }
        add(subjectField);


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

        try {
            database.signIn(id, subject, reason);
            idField.setText(null);
        } catch (InvalidIdException e) {

        }
    }
}
