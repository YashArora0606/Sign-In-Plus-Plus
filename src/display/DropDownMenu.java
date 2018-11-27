package display;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


import utilities.Utils;




public class DropDownMenu extends JPanel {
	
    Timer tm1,tm2,tm3;
    // the panels default height = 60
    Integer pl1 = 30,pl2 = 30,pl3 = 30;
	String selectedText = "Select";
	String title;

    String[] items;
	
	DropDownMenu(String[] items, String title) {
		this.items = items;
		this.title = title;
		
		
		//this.setBounds(100, 100, 550, 200);
		this.setPreferredSize(new Dimension(180, 30));
//		//this.setLayout(null);
//		btnNewButton.setBackground(Color.ORANGE);
//		//btnNewButton.setBounds(0, 0, 148, 60);
//		
//
//		JButton button = new JButton("New button");
//		button.setBackground(Color.ORANGE);
//		//button.setBounds(0, 61, 148, 60);
//		add(button);
//
//		JButton button_1 = new JButton("New button");
//		button_1.setBackground(Color.ORANGE);
//		//button_1.setBounds(0, 122, 148, 60);
//		add(button_1);
//
//		JButton button_2 = new JButton("New button");
//		button_2.setBackground(Color.ORANGE);
//		//button_2.setBounds(0, 183, 148, 60);
//		add(button_2);
//
//		JButton button_3 = new JButton("New button");
//		button_3.setBackground(Color.ORANGE);
//		//button_3.setBounds(0, 243, 148, 60);
//		add(button_3);
//
		tm1 = new Timer(20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 300 the maximum height
				if (pl1 > 303) {
					tm1.stop();
				} else {
					setSize(getWidth(), pl1);
					pl1 += 20;
				}

			}
		});

		
        setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		CustomButton select = new CustomButton(selectedText, 0, 0, 180, 30);
		
		if (isMouseOnPanel(this)) {
			setSize(180, 30 * (items.length + 1));
		} else {
			setSize(this.preferredSize());
		}
		
		select.draw(g, this);
		
		for (int i = 1; i <= items.length; i++) {
			CustomButton b = new CustomButton(items[i-1], 0, i * 30, 180, 30);
			if (b.isMouseOnButton(this) && isMouseOnPanel(this)) {
				selectedText = items[i-1];
			}
			b.draw(g, this);
		}
		
		//g.drawRect(0, 0, getWidth(), getHeight());
		
		repaint();
	}

	private boolean isMouseOnPanel(JPanel panel) {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = panel.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

        return ((x >= 0) && (x <= panel.getWidth()) && (y >= 0) && (y <= panel.getHeight()));
        //return ((x >= panel.getX()) && (x <= panel.getX() + panel.getWidth()) && (y >= panel.getY()) && (y <= panel.getY() + panel.getHeight()));
	}

}

