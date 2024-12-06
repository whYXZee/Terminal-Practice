package whyxzee.terminalpractice.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class RecMath {
    /**
     * Checks if a number is a vampire number.
     * 
     * @param vampire the number to check
     * @param fang_1  the first 'fang' number
     * @param fang_2  the second 'fang' number
     * @return {@code true} if the number is a vampire, {@code false} if otherwise
     */
    public static boolean validVampire(int vampire, int fang_1, int fang_2) {
        char[] f_1 = Integer.toString(fang_1).toCharArray();
        char[] f_2 = Integer.toString(fang_2).toCharArray();
        char[] v = Integer.toString(vampire).toCharArray();

        //
        // Check for combo
        //
        boolean passing = true;
        for (char i : v) {
            boolean present = false;

            // Check through the first fang
            for (char j : f_1) {
                if (i == j) {
                    present = true;
                    break;
                }
            }

            // If not in the first fang, check the second
            if (!present) {
                for (char j : f_2) {
                    if (i == j) {
                        present = true;
                        break;
                    }
                }
            }

            // if present is false, then passing will be false too.
            passing = present;
            if (!passing) {
                // so the bool does not go from false to true
                break;
            }
        }

        //
        // Check that the fangs are the same digit size
        //
        passing = (f_1.length == f_2.length);

        return passing;
    }
}
