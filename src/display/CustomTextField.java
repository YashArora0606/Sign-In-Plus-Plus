package display;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;


import utilities.Utils;


public class CustomTextField extends JPanel {
	
	String title;
	CustomButton titleButton;
	private JTextField textField;
	
	CustomTextField(String title) {
		this.title = title;
		this.setPreferredSize(new Dimension(270, 70));
		setBackground(null);
		setLayout(new BorderLayout());
        
		textField = new JTextField(7);
		textField.setBorder(BorderFactory.createDashedBorder(Utils.colours[0]));
		textField.setBackground(null);
		textField.setFont(Utils.getFont("assets/Kollektif.ttf", 30f));
		textField.setText("");
		//textField.setBounds(0, 30, 250, 60);
		
		add(textField, BorderLayout.SOUTH);
		
        setVisible(true);

	}
	
	public String getText() {
		return textField.getText();
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		titleButton = new CustomButton(title, getPreferredSize().width/2 - 220/2, 0, 220, 30, Utils.colours[3]);
		titleButton.setSelectable(false);
		titleButton.draw(g, this);
		

		
		repaint();
	}

}

