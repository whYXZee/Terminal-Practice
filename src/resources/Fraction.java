package resources;

/**
 * The {@code Fraction} class represents fractions as a String. The
 * numerator/denominator can include variables.
 */
public class Fraction {
    private String numerator, denominator, term;

    /**
     * Creates a fraction.
     * 
     * @param num   the String of the numerator
     * @param denom the String of the denominator
     */
    public Fraction(String num, String denom) {
        this.numerator = num;
        this.denominator = denom;
        this.term = num + "/" + denom;
        this.simplify();
    }

    /**
     * Changes the fraction post-declaration.
     * 
     * @param num   the String of the numerator
     * @param denom the String of the denominator
     */
    public void changeFraction(String num, String denom) {
        this.numerator = num;
        this.denominator = denom;
        this.term = num + "/" + denom;
    }

    /**
     * Converts the string input into a fraction
     * 
     * @param input as the String of "numerator/denominator"
     */
    public static Fraction toFraction(String input) {
        boolean isDenom, fracPower, avoid;
        isDenom = fracPower = avoid = false;
        String num = "";
        String denom = "";
        String fractionPower = "^";

        for (Character i : input.toCharArray()) {
            if (fracPower) { // if there's a fractional power present, then use different conditions
                if (i == ')') { // Removes the fractional power as its the final part of the fraction
                    avoid = false;
                    fractionPower = fractionPower + ")";
                } else if (avoid) { // prevents the numbers and backslash from reaching the else statement
                    fractionPower = fractionPower + i;
                } else if (i == '^') { // marks the start of the exponent, starting the avoidance
                    avoid = true;
                } else if (i == '/') { // only triggers if avoid is false
                    isDenom = true;
                } else {
                    if (!isDenom) {
                        num = num + i;
                    } else {
                        denom = denom + i;
                    }
                }
            } else {
                if (i == '/') {
                    isDenom = true;
                } else if (!isDenom) {
                    num = num + i;
                } else {
                    denom = denom + i;
                }
            }
        }
        if (fracPower) { // adds the fractional power to the numerator
            num = num + fractionPower;
        }
        if (denom.equals("")) { // so that way theres no null value
            denom = "1";
        }
        return new Fraction(num, denom);
    }

    /**
     * Returns true if the String input is a function.
     * 
     * @param input String to check if its a fraction
     * @return {@code true} when there's a "/" in the input
     *         <li>{@code false} if there's no "/" in the input
     */
    public static boolean isFraction(String input) {
        boolean fracPow = false;
        for (Character i : input.toCharArray()) {
            // Voiding a fractional power
            if (i == '(') {
                fracPow = true;
            } else if (i == ')') {
                fracPow = false;
            } else if (i == '/' && !fracPow) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reduces the fraction so it uses the smallest numbers possible.
     */
    public void simplify() {
        float divisor;

        // Simplifies Coefficients
        float numNum = MathFunctions.getCoefficient(numerator);
        float denomNum = MathFunctions.getCoefficient(denominator);
        if (denomNum % numNum == 0) { // If the denominator is divisible by the numerator, then divide the
                                      // numerator
            denomNum = denomNum / numNum;
            numNum = numNum / numNum;
        } else {
            if (MathFunctions.greatestCommonDivisor(numNum, denomNum, 2) != 1) {
                // Checks if the GCD is 2, then divides by the GCD based on multiples of 2.
                divisor = MathFunctions.greatestCommonDivisor(numNum, denomNum, 2);
                denomNum = denomNum / divisor;
                numNum = numNum / divisor;
            } else if (MathFunctions.greatestCommonDivisor(numNum, denomNum, 3) != 1) {
                // Checks if the GCD is 3, then divides by the GCD based on multiples of 3.
                divisor = MathFunctions.greatestCommonDivisor(numNum, denomNum, 3);
                denomNum = denomNum / divisor;
                numNum = numNum / divisor;
            } else {
                // Checks if the GCD is a prime number.
                divisor = MathFunctions.greatestCommonDivisor(numNum, denomNum, 0);
                denomNum = denomNum / divisor;
                numNum = numNum / divisor;
            }
        }

        // Vars + exponents
        String numVar = MathFunctions.getVars(numerator);
        String denomVar = MathFunctions.getVars(denominator);

        // Clean up
        String num = Integer.toString((int) numNum);
        String denom = Integer.toString((int) denomNum);
        if (denom.charAt(0) == '-') { // move the negative to the numerator and/or clean up -/-
            num = MathFunctions.multiplication("-1*" + num);
            denom = MathFunctions.multiplication("-1*" + denom);
        }
        changeFraction(num + numVar, denom + denomVar);
    }

    /**
     * Divides two fractions inputted as Strings in case there are whole numbers.
     * 
     * @param dividend : fraction or number as a String, what is being divided.
     * @param divisor  : fraction or number as a String, what its dividing by.
     */
    public static Fraction divideFraction(String dividend, String divisor) {
        // Converts the inputs to fractions
        // (if they're already fractions, it doesn't change anything)
        Fraction dividendFrac = toFraction(dividend);
        Fraction divisorFrac = toFraction(divisor);

        // Does the cross multiplication trick and uses the string multiplication method
        String numerator = MathFunctions.multiplication(dividendFrac.numerator + "*" + divisorFrac.denominator);
        String denominator = MathFunctions.multiplication(dividendFrac.denominator + "*" + divisorFrac.numerator);
        return new Fraction(numerator, denominator);
    }

    public static Fraction subtractFraction(String a, String b) {
        Fraction fracA = toFraction(a);
        Fraction fracB = toFraction(b);
        if (!fracA.denominator.equals(fracB.denominator)) { // ensures that the denominators are the same before doing
                                                            // illegal fraction arithmetics
            fracA.changeFraction(MathFunctions.multiplication(fracA.numerator + "*" + fracB.denominator),
                    MathFunctions.multiplication(fracA.denominator + "*" + fracB.denominator));
            fracB.changeFraction(MathFunctions.multiplication(fracB.numerator + "*" + fracA.denominator),
                    MathFunctions.multiplication(fracB.denominator + "*" + fracA.denominator));
        }
        return new Fraction(MathFunctions.subtraction(fracA.numerator + "_" + fracB.numerator),
                MathFunctions.subtraction(fracA.denominator + "_" + fracB.denominator));
    }

    @Override
    public String toString() {
        if (denominator.equals("1")) { // if the denom is over one, then there shouldn't be a fraction
            return numerator;
        }
        return term;
    }
}
