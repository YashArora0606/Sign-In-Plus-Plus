package utilities;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Static utility class (used for scaling rn)
 */
public class Utils {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static double scale = screenSize.width/1920.0;
    
    private static String[] reasons = new String[] {
            "Test", "Chill Zone", "Quiet Work", "Academic Support", "Group Work"
    };

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
    
    public static String[] getReasons() {
    	return reasons;
    }
    


}
