package display;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagment.Database;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;

class SignOutPanel extends JPanel {

    private Window display;
    private Database database;

    private JTextField idField;
    private JButton submitButton;
    private JButton backButton;

    SignOutPanel(Window display, Database database) {
        this.display = display;
        this.database = database;


        idField = new JTextField(10);
        idField.setFont(idField.getFont().deriveFont(35f));
        add(idField);


        submitButton = new JButton("Sign Out");
        submitButton.addActionListener(e -> submit());
        add(submitButton);


        backButton = new JButton("Back");
        backButton.addActionListener(e -> display.changeState(1));
        add(backButton);

        setVisible(true);
    }

    private void submit() {
        String id = idField.getText();

        try {
            database.signOut(id);
            idField.setText(null);

        } catch (InvalidIdException | NotLoggedInException e) {
            e.printStackTrace();
        }
    }

}
