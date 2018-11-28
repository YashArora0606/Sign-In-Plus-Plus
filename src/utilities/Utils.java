package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Static utility class (used for scaling rn)
 */
public class Utils {
    public static Color[] colours = {new Color(100,100,100), new Color(249, 236, 236), new Color(240,217,218), new Color(200,217,235), new Color(236,242,249)};
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static double scale = screenSize.width/1920.0;

    public static int scale(double x) {
        return (int)Math.round(scale * x);
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

    public static String encode(String string){
        String newString = "";
        for (int i = 0; i < string.length(); i++){
            int unicode = (int) string.charAt(i)+5;
            newString += (char) unicode;
        }
        return newString;
    }

    public static String decode(String string){
        String newString = "";
        for (int i = 0; i < string.length(); i++){
            int unicode = (int) string.charAt(i)-5;
            newString += (char) unicode;
        }
        return newString;
    }

    public static Timestamp getNow() {
        return new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public static String idToString(int id) {
        String stringId = String.valueOf(id);
        for (int i = stringId.length(); i < 9; i++) {
            stringId = "0" + stringId;
        }
        return stringId;
    }
}
