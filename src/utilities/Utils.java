package utilities;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GraphicsEnvironment;
import java.awt.FontFormatException;

import java.io.File;
import java.io.IOException;

/**
 * Static utility class (used for scaling rn)
 */
public class Utils {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static double scale = screenSize.width/1920.0;

    public static int scale(double x) {
        return (int)Math.round(scale * x);
    }
    
    public static boolean isAnInteger(String x) {
        try {
            Integer.parseInt(x);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves a font and creates it
     * @param fileName the name of the font file
     * @param size the desired size of the font to be made
     * @return the created Font
     */
    public static Font getFont(String fileName, float size){
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(fileName)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.PLAIN,new File(fileName)));
        } catch (IOException | FontFormatException e){
            font = new Font(Font.SANS_SERIF, Font.PLAIN, Math.round(size));
        }
        return font;
    }
}
