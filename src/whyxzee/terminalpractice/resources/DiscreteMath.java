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
     * A permutation is a ___ where order <b>does</b> matter. It is the number of
     * possible orders within chosen objects, and not the number of possible
     * selections. For example, if given the set {1, 2, 3, 4}, and we need three of
     * those digits, then 123 is not the same as 132 as the order changed. In
     * addition, 123 and 124 would be different.
     * 
     * @param n
     * @param r
     */
    public static BigInteger permutation(int n, int r) {
        return factorial(n).divide(factorial(n - r));
    }

    /**
     * Gets the combination n of r (nCr).
     * 
     * A combination is a ___ where order <b>does not</b> matter. It is the number
     * of possible selections within a set, and not the order of the chosen
     * objects. For example, given set {1, 2, 3, 4}, 123 is the same as 132 as
     * the chosen numbers didn't change. However, 123 and 124 would be different.
     * 
     * @param n
     * @param r
     * @return
     */
    public static BigInteger combination(int n, int r) {
        return factorial(n).divide((factorial(n - r).multiply(factorial(r))));
    }
}
