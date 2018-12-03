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
		this.setPreferredSize(new Dimension(Utils.scale(270), Utils.scale(70)));
		setBackground(null);
		setLayout(new BorderLayout());
        
		textField = new JTextField(7);
		textField.setBorder(BorderFactory.createDashedBorder(Utils.colours[0]));
		textField.setBackground(null);
		textField.setFont(Utils.getFont("assets/Kollektif.ttf", Utils.scale(30)));
		textField.setText("");
		//textField.setBounds(0, 30, 250, 60);
		
		add(textField, BorderLayout.SOUTH);
		
        setVisible(true);

	}
	
	public String getText() {
		return textField.getText();
	}

	public void setText(String string){
		textField.setText(string);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		titleButton = new CustomButton(title, getPreferredSize().width/2 - Utils.scale(230)/2, 0, Utils.scale(230), Utils.scale(30), Utils.colours[3]);

		titleButton.setSelectable(false);
		titleButton.draw(g, this);
		

		
		repaint();
	}

}

