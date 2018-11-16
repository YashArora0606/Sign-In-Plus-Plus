import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class Display extends JFrame {

	static JFrame window;
	int timesSubmitted = 0;
	String FILE_NAME = "MasterList.xlsx";

	public static void main(String[] args) throws IOException {
		window = new Display();
	}

	Display() throws IOException {

		ExcelManager masterList = new ExcelManager(FILE_NAME);

		JButton signIn = new JButton("Sign-In");
		JButton signOut = new JButton("Sign-Out");
		JButton closeProgram = new JButton("Close Program");
		JButton submitId = new JButton("Sign In");
		JButton submitIdOut = new JButton("Sign Out");

		JButton back = new JButton("Back");
		JButton backOut = new JButton("Back");

		JPanel panel = new JPanel();
		JPanel signInPanel = new JPanel();
		JPanel signOutPanel = new JPanel();

		JTextField idField = new JTextField(10);

		JComboBox<String> reasons = new JComboBox<String>();
		reasons.addItem("Select Reason");
		reasons.addItem("Test");
		reasons.addItem("Chill Zone");
		reasons.addItem("Quiet Work");
		reasons.addItem("Academic Support");
		reasons.addItem("Group Work");

		JComboBox<String> subjects = new JComboBox<String>();
		subjects.addItem("Select Subject");
		subjects.addItem("Art");
		subjects.addItem("Math");
		subjects.addItem("Music");
		subjects.addItem("Science");
		subjects.addItem("History");
		subjects.addItem("Geography");
		subjects.addItem("Business");
		subjects.addItem("Family Studies");
		subjects.addItem("Physical Ed.");
		subjects.addItem("Technology Studies");
		subjects.addItem("Social Sciences");
		subjects.addItem("Lunch / Spare");

		JTextField idFieldOut = new JTextField(10);
		// idFieldOut.setText("073689440");

		idField.setFont(idField.getFont().deriveFont(35f));
		idFieldOut.setFont(idField.getFont().deriveFont(35f));

		panel.add(signIn);
		panel.add(signOut);
		panel.add(closeProgram);

		signInPanel.add(idField);
		signInPanel.add(reasons);
		signInPanel.add(subjects);
		signInPanel.add(submitId);
		signInPanel.add(back);

		signOutPanel.add(idFieldOut);
		signOutPanel.add(submitIdOut);
		signOutPanel.add(backOut);

		signIn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(false);
				signInPanel.setVisible(true);
				getContentPane().add(signInPanel);
			}

		});

		signOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(false);
				signOutPanel.setVisible(true);
				getContentPane().add(signOutPanel);
			}

		});

		back.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(true);
				signInPanel.setVisible(false);
			}

		});

		backOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(true);
				signOutPanel.setVisible(false);
			}

		});

		submitId.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				String text = idField.getText();

				if (isStudentNumber(text) && reasons.getSelectedIndex() != 0
						&& subjects.getSelectedIndex() != 0) {

					masterList.signStudentIn(text, (String) subjects.getSelectedItem(), (String) reasons.getSelectedItem());

					idField.setText(null);
				}

			}

		});

		submitIdOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				String text = idFieldOut.getText();

				if (text.length() == 9 && isStudentNumber(text)) {

					if (masterList.signStudentOut(text)) {
						idFieldOut.setText(null);
					}
				}

			}

		});

		closeProgram.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				try {
					masterList.closeExcelFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				window.dispose();
			}

		});

		this.getContentPane().add(panel);
		this.setTitle("SignIn++");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(200, 100);
		this.setSize(1000, 600);
		this.setVisible(true);

	}

	public static boolean isStudentNumber(String string) {
		try {
			double d = Double.parseDouble(string);
		} catch (NumberFormatException nfe) {
			return false;
		}
		
		if (string.length() == 9) {
			return true;
		}
		return true;
	}







}