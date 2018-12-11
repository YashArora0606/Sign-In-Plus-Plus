package display;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * The window which pops up upon launching before the program starts up
 * Displays our logo
 *
 * @author Katelyn Wang
 * last updated 2018/12/11
 */
public class StartFrame extends JFrame {
    /**
     * Constructor
     */
    public StartFrame() {
        super("Welcome!");
        ImageIcon icon = new ImageIcon("assets/bolt.png"); //sets the icon at the top to our logo
        this.setIconImage(icon.getImage());
        this.setResizable(false); //doesn't allow the screen to be resized
        this.setAlwaysOnTop(true); //sets it above all the other panels
        this.setSize(500, 500); //hardcoded to our assets (our image we use is 500x500 so it won't ever need to be changed)
        this.setLocationRelativeTo(null); //centers it

        this.setVisible(true);
    }

    /**
     * Draws the background image
     *
     * @param g The graphic object to draw images
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Image panel = Toolkit.getDefaultToolkit().getImage("assets/background.png"); //retrieves the image
        g.drawImage(panel, 0, 0, this); //draws it

    }
}
