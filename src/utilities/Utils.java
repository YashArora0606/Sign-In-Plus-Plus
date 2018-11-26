package utilities;
import java.awt.*;

import java.io.File;
import java.io.IOException;

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
    
    public static boolean isAnInteger(String x) {
        try {
            Integer.parseInt(x);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }


    /**
     * Checks if a String is a valid representation of a student ID
     * A student ID must be an integer that is 9 numbers long (leading zeroes allowed)
     * @param id
     * @return true, if the string is a valid representation of a student ID
     */
    public static boolean isValidId(String id) {
        return (id.length() == 9 && Utils.isAnInteger(id));
    }

    /**
     * Checks if a String is a valid representation of a name
     * A name must be at least 1 character, contain no digits, and have its first character capitalized
     * @param name
     * @return true, if the string is a valid representation of a name
     */
    public static boolean isValidName(String name) {
        return ((name.length() >= 1) &&
                (name.matches("^[^0-9]+$")) &&
                (name.charAt(0) == Character.toUpperCase(name.charAt(0))));
    }

    /**
     * Checks if a String validly represents a grade
     * A grade must be an integer from 9 - 13
     * @param grade
     * @return true, if the String is a valid representation of a grade
     */
    public static boolean isValidGrade(String grade) {
        if (Utils.isAnInteger(grade)) {
            int gradeNum = Integer.parseInt(grade);
            return (gradeNum >= 9 && gradeNum <= 13);
        }
        return false;
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
}
