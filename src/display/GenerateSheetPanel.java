package display;

import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import datamanagement.Database;
import datamanagement.SignInManager;
import utilities.Utils;

public class GenerateSheetPanel extends JPanel {
	private Window display;

	private JPanel panel;
	private int x;
	private int y;
	private CustomButton back;
	private CustomButton generate;

	private SelectMultipleMenu reasonSelect;
	private SelectMultipleMenu sertSelect;
	private SelectMultipleMenu courseMissingSelect;
	
	private static final int PADDING_CONSTANT = 20;

	GenerateSheetPanel(Window display) {
		this.display = display;
		this.panel = this;
		this.x = display.maxX;
		this.y = display.maxY;
		this.setLayout(null);
		setBackground(Utils.colours[4]);

		setLayout(new BorderLayout());

		reasonSelect = new SelectMultipleMenu(SignInManager.getReasons(), "Reason");
		sertSelect = new SelectMultipleMenu(SignInManager.getSerts(), "SERT");
		courseMissingSelect = new SelectMultipleMenu(SignInManager.getCourses(), "Course Missing");

//		JPanel centerPanel = new JPanel();
//		
//		//centerPanel.setPreferredSize(new Dimension(display.maxX, 620));
//		
//		centerPanel.add(reasonSelect);
//		centerPanel.add(sertSelect);
//		centerPanel.add(courseMissingSelect);
//		
//		add(centerPanel, BorderLayout.CENTER);
//		

		JLayeredPane pane = new JLayeredPane();
		pane.setPreferredSize(new Dimension(x, y));

		reasonSelect.setBounds(x / 2 - reasonSelect.getPreferredSize().width - sertSelect.getPreferredSize().width / 2 - PADDING_CONSTANT,
				30, reasonSelect.getPreferredSize().width, reasonSelect.getPreferredSize().height);
		
		sertSelect.setBounds(x / 2 - sertSelect.getPreferredSize().width / 2,
				30, sertSelect.getPreferredSize().width, sertSelect.getPreferredSize().height);
		
		courseMissingSelect.setBounds(x / 2 + sertSelect.getPreferredSize().width / 2 + PADDING_CONSTANT,
				30, courseMissingSelect.getPreferredSize().width, courseMissingSelect.getPreferredSize().height);

		pane.add(reasonSelect);
		pane.add(sertSelect);
		pane.add(courseMissingSelect);

		add(pane);

		this.addMouseListener(new MyMouseListener());

		this.setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, panel);

		generate = new CustomButton("Generate", x / 2 - Utils.scale(200) / 2, (int) (y * 0.8) - Utils.scale(80) / 2,
				Utils.scale(200), Utils.scale(80), Utils.colours[2]);
		generate.draw(g, panel);

		repaint();
	}

	public void generateSheet() {

	}

	public class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {

		}

		public void mouseClicked(MouseEvent e) {
			if (back.isMouseOnButton(panel)) {
				display.changeState(5);
			} else if (generate.isMouseOnButton(panel)) {

			}
		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}
	}
}
