package display;

import javax.swing.*;

public class PasswordPanel extends JPanel{
    private Window display;

    private JTextField idField;
    private JButton backButton;
    PasswordPanel(Window display) {
        this.display = display;

        backButton = new JButton("Back");
        backButton.addActionListener(e -> display.changeState(0));
        this.add(backButton);
    }
}
