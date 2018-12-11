package display;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class StartFrame extends JFrame {
    public StartFrame() {
        super("Welcome!");
        ImageIcon icon = new ImageIcon("assets/bolt.png");
        this.setIconImage(icon.getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
    }

    /**
     * Draws the background image
     * @param g The graphic object to draw images
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Image panel = Toolkit.getDefaultToolkit().getImage("assets/background.png");
        g.drawImage(panel, 0, 0, this);

    }
}
