package whyxzee.terminalpractice.resources;

/**
 * The {@code Fraction} class represents fractions as a String. The
 * numerator/denominator can include variables.
 */
public class Fraction {
    private String numerator, denominator, term;

    //
    // Fraction Construction
    //

    /**
     * Creates a fraction.
     * 
     * @param num   the String of the numerator
     * @param denom the String of the denominator
     */
    public Fraction(String num, String denom) {
        this.numerator = num;
        this.denominator = denom;
        this.term = "[" + num + "] \u2044 [" + denom + "]";
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
        this.term = "[" + num + "] \u2044 [" + denom + "]";
    }

    /**
     * Converts the string input into a fraction
     * 
     * @param input as the String of "numerator/denominator"
     */
    public static Fraction toFraction(String input) {
        // Declaring vars
        boolean isDenom, fracPower, avoid;
        isDenom = avoid = false;
        fracPower = Fraction.isFracPower(input);

        String num = "";
        String denom = "";
        String fractionPower = "^";

        for (Character i : input.toCharArray()) {
            // If there's a fractional power present, then use different conditions
            if (fracPower) {
                if (i == ')') { // Removes the fractional power as its the final part of the fraction
                    avoid = false;
                    fractionPower += ")";
                } else if (avoid) { // prevents the numbers and backslash from reaching the else statement
                    fractionPower += i;
                } else if (i == '^') { // marks the start of the exponent, starting the avoidance
                    avoid = true;
                } else if (i == '\u2044') { // only triggers if avoid is false
                    isDenom = true;
                } else {
                    if (!isDenom) {
                        num += i;
                    } else {
                        denom += i;
                    }
                }
            } else {
                if (i == '\u2044') {
                    isDenom = true;
                } else if (!isDenom) {
                    num += i;
                } else {
                    denom += i;
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
     * Reduces the fraction so it uses the smallest numbers possible.
     */
    public void simplify() {
        float divisor;

        // Simplifies Coefficients
        float numNum = Float.valueOf(AlgebraFunctions.getCoefficient(numerator));
        float denomNum = Float.valueOf(AlgebraFunctions.getCoefficient(denominator));
        if (denomNum % numNum == 0) { // If the denominator is divisible by the numerator, then divide the
                                      // numerator
            denomNum = denomNum / numNum;
            numNum = numNum / numNum;
        } else {
            if (AlgebraFunctions.greatestCommonDivisor(numNum, denomNum, 2) != 1) {
                // Checks if the GCD is 2, then divides by the GCD based on multiples of 2.
                divisor = AlgebraFunctions.greatestCommonDivisor(numNum, denomNum, 2);
                denomNum = denomNum / divisor;
                numNum = numNum / divisor;
            } else if (AlgebraFunctions.greatestCommonDivisor(numNum, denomNum, 3) != 1) {
                // Checks if the GCD is 3, then divides by the GCD based on multiples of 3.
                divisor = AlgebraFunctions.greatestCommonDivisor(numNum, denomNum, 3);
                denomNum = denomNum / divisor;
                numNum = numNum / divisor;
            } else {
                // Checks if the GCD is a prime number.
                divisor = AlgebraFunctions.greatestCommonDivisor(numNum, denomNum, 0);
                denomNum = denomNum / divisor;
                numNum = numNum / divisor;
            }
        }

        // Vars + exponents
        String numVar = AlgebraFunctions.getVars(numerator);
        String denomVar = AlgebraFunctions.getVars(denominator);

        // Clean up
        String num = Integer.toString((int) numNum);
        String denom = Integer.toString((int) denomNum);
        if (denom.charAt(0) == '-') { // move the negative to the numerator and/or clean up -/-
            num = AlgebraFunctions.multiplication("-1*" + num);
            denom = AlgebraFunctions.multiplication("-1*" + denom);
        }
        changeFraction(num + numVar, denom + denomVar);
    }

    //
    // Arithmetics
    //

    /**
     * Divides two fractions inputted as Strings in case there are whole numbers.
     * 
     * @param dividend fraction or number as a String, what is being divided.
     * @param divisor  fraction or number as a String, what its dividing by.
     */
    public static Fraction divideFraction(String dividend, String divisor) {
        // Converts the inputs to fractions
        // (if they're already fractions, it doesn't change anything)
        Fraction dividendFrac = toFraction(dividend);
        Fraction divisorFrac = toFraction(divisor);

        // Does the cross multiplication trick and uses the string multiplication method
        String numerator = AlgebraFunctions.multiplication(dividendFrac.numerator + "*" + divisorFrac.denominator);
        String denominator = AlgebraFunctions.multiplication(dividendFrac.denominator + "*" + divisorFrac.numerator);
        return new Fraction(numerator, denominator);
    }

    /**
     * Subtracts two fractions inputted as Strings in case there are whole numbers.
     * 
     * @param a fraction or number as a String, what is being subtracted from
     * @param b fraction or number as a String, what is being subtracted
     */
    public static Fraction subtractFraction(String a, String b) {
        Fraction fracA = toFraction(a);
        Fraction fracB = toFraction(b);
        if (!fracA.denominator.equals(fracB.denominator)) { // ensures that the denominators are the same before doing
                                                            // illegal fraction arithmetics
            fracA.changeFraction(AlgebraFunctions.multiplication(fracA.numerator + "*" + fracB.denominator),
                    AlgebraFunctions.multiplication(fracA.denominator + "*" + fracB.denominator));
            fracB.changeFraction(AlgebraFunctions.multiplication(fracB.numerator + "*" + fracA.denominator),
                    AlgebraFunctions.multiplication(fracB.denominator + "*" + fracA.denominator));
        }
        return new Fraction(AlgebraFunctions.subtraction(fracA.numerator + "-" + fracB.numerator), fracA.denominator);
    }

    /**
     * Multiplies two fractions inputted as Strings in case there are whole numbers.
     * 
     * @param a fraction or number as a String
     * @param b fraction or number as a String
     */
    public static Fraction multiplyFractions(String a, String b) {
        Fraction fracA = toFraction(a);
        Fraction fracB = toFraction(b);

        return new Fraction(
                AlgebraFunctions.multiplication(fracA.numerator + "*" + fracB.numerator),
                AlgebraFunctions.multiplication(fracA.denominator + "*" + fracB.denominator));
    }

    /**
     * 
     * @param a
     * @param b
     * @return
     */
    public static Fraction addFractions(String a, String b) {
        Fraction fracA = toFraction(a);
        Fraction fracB = toFraction(b);
        if (!fracA.denominator.equals(fracB.denominator)) { // ensures that the denominators are the same before doing
                                                            // illegal fraction arithmetics
            fracA.changeFraction(AlgebraFunctions.multiplication(fracA.numerator + "*" + fracB.denominator),
                    AlgebraFunctions.multiplication(fracA.denominator + "*" + fracB.denominator));
            fracB.changeFraction(AlgebraFunctions.multiplication(fracB.numerator + "*" + fracA.denominator),
                    AlgebraFunctions.multiplication(fracB.denominator + "*" + fracA.denominator));
        }

        return new Fraction(AlgebraFunctions.addition(fracA.numerator + "+" + fracB.numerator), fracA.denominator);
    }

    //
    // Conditionals
    //

    private static boolean isFracPower(String input) {
        boolean isPower = false;
        for (Character i : input.toCharArray()) {
            if (isPower && i == '\u2044') {
                return true;
            } else if (i == '(') {
                isPower = true;
            } else if (i == ')') {
                isPower = false;
            }
        }
        return false;
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
            } else if (i == '\u2044' && !fracPow) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if (denominator.equals("1")) { // if the denom is over one, then there shouldn't be a fraction
            return numerator;
        }
        return term;
    }
}
