/**
 * [CustomTextField.java]

 * Custom text field that looks good
 * @author Yash Arora
 * December 2 2018
 */

package display.customcomponents;

import java.awt.BorderLayout;   
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import display.panels.Palette;
import utilities.Utils;
 
/**
 * Custom text field class that looks good
 * @author Yash Arora
 */
public class CustomTextField extends JPanel {
	
	// Imporant class variables
	private String title;
	private CustomButton titleButton;
	private JTextField textField;
	private boolean hasTitle;
	
	/**
	 * Constructor
	 * @param title name of the text field label
	 */
	public CustomTextField(String title) {
		this.title = title;
		this.setPreferredSize(new Dimension(Utils.scale(270), Utils.scale(70)));
		setBackground(null);
		setLayout(new BorderLayout());
		textField = new JTextField(7);
		textField.setBorder(BorderFactory.createDashedBorder(Palette.colours[0]));
		textField.setOpaque(false);
		textField.setFont(Utils.getFont("assets/Kollektif.ttf", Utils.scale(30)));
		textField.setText("");
		this.hasTitle = true;
		add(textField, BorderLayout.SOUTH);

        setVisible(true);
	}
	
	/**
	 * getText
	 * @return the text in the field
	 */
	public String getText() {
		return textField.getText();
	}

	/**
	 * setText
	 * @param string to set the field to
	 */
	public void setText(String string){
		textField.setText(string);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Draw the field's title
		if (this.hasTitle) {
			titleButton = new CustomButton(title, getPreferredSize().width/2 - Utils.scale(230)/2, 0, Utils.scale(230), Utils.scale(30), Palette.colours[3]);
			titleButton.setSelectable(false);
			titleButton.draw(g, this);
		}

		
		repaint();
	}

	// Reset the text in the field
	public void reset() {
		this.setText("");
	}

}

