package display;

import javax.swing.JButton;
import javax.swing.JPanel;

class HomePanel extends JPanel {

    private Window display;

    private JButton studentButton;
    private JButton teacherButton;

    HomePanel(Window display) {
        this.display = display;

        studentButton = new JButton("Student");
        studentButton.addActionListener(e -> display.changeState(1));
        this.add(studentButton);


        teacherButton = new JButton("Teacher");
        teacherButton.addActionListener(e -> display.changeState(5));
        this.add(teacherButton);
    }


}
