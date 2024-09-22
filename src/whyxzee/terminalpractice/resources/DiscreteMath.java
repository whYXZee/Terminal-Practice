package whyxzee.terminalpractice.resources;

import java.math.BigInteger;

public class DiscreteMath {
    /**
     * Gets the factorial of the input.
     * 
     * @param input
     * @return the factorial of the input (input!) as a BigInteger, as the numbers
     *         increase rapidly
     */
    public static BigInteger factorial(int input) {
        BigInteger output = BigInteger.valueOf(1);
        for (int i = input; i > 0; i--) {
            output = output.multiply(BigInteger.valueOf(i));
        }
        return output;
    }

    /**
     * Gets the permutation n of r (nPr).
     * 
     * A permutation is a set where order <b>does</b> matter. It is the number of
     * possible orders within chosen objects, and not the number of possible
     * selections. For example, if given the set {1, 2, 3, 4}, and we need three of
     * those digits, then 123 is not the same as 132 as the order changed. In
     * addition, 123 and 124 would be different.
     * 
     * <p>
     * This method uses the Rule of Sequential Counting (multiplication) instead
     * of n!/(n-r)! for time purposes.
     * 
     * @param n
     * @param r
     */
    public static BigInteger permutation(int n, int r) {
        // Declaring vars
        BigInteger output = BigInteger.valueOf(1);

        if (r > n) { // because r has to be less than or equal to n
            r = n;
        } else if (r < 0) { // r cannot be negative, assumes it should be 0
            return BigInteger.valueOf(1);
        }

        for (int i = n; i > (n - r); i--) {
            output = output.multiply(BigInteger.valueOf(i));
        }
        return output;

        // return factorial(n).divide(factorial(n - r));
    }

    /**
     * Gets the combination n of r (nCr).
     * 
     * A combination is a set where order <b>does not</b> matter. It is the number
     * of possible selections within a set, and not the order of the chosen
     * objects. For example, given set {1, 2, 3, 4}, 123 is the same as 132 as
     * the chosen numbers didn't change. However, 123 and 124 would be different.
     * 
     * <p>
     * This method uses the formula nPr/r! instead of n!/[(n-r)!*r!] for time
     * purposes.
     * 
     * @param n
     * @param r
     * @return
     */
    public static BigInteger combination(int n, int r) {
        // Preventing illegal combinations
        if (r > n) { // because r has to be less than or equal to n
            r = n;
        }
        if (r < 0 || r == 0 || r == n) {
            return BigInteger.valueOf(1);
        }

        return permutation(n, r).divide(factorial(r));

        // return factorial(n).divide((factorial(n - r).multiply(factorial(r))));
    }
}
