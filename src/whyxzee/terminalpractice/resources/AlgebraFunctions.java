package whyxzee.terminalpractice.resources;

import java.util.ArrayList;
// import java.util.HashMap;

/**
 * String-based algebraic functions. String-based as in strings that want to
 * accomplish arithmetics, with the inclusion of variables.
 * 
 * Syntax:
 * <ul>
 * <li>(): denotes the start/end of an exponent,
 * used for fractional exponents
 * <li>[]: denotes the start/end of a parenthesis
 * <li>{d[var]/d<[var]}: denotes that the variable is a derivative with respect
 * to the bottom derivative.
 * <li>^: denotes the power/exponent
 * <li>-: denotes a negative
 * <li>+: marks the left/right sides of an addition problem
 * <li>*: marks the left/right sides of a multiplication problem
 * <li>/: marks the left/right sides of a division problem
 * <li>|: marks the numerator/denominator of a fraction
 */
public class AlgebraFunctions {
    /**
     * Gets the coefficient of the input.
     * 
     * @param input
     * @return
     */
    public static String getCoefficient(String input) {
        // Setting the vars
        boolean isPower = false;
        String output = "";

        // Number construction loop
        for (Character i : input.toCharArray()) {
            if (i == '^' || i == '(') {
                isPower = true;
            } else if (i == ')') {
                isPower = false;
            } else if ((Character.isDigit(i) || i == '-' || i == '|') && !isPower) {
                output += i;
            }
        }

        // In the chance there is no coefficient.
        if (output.equals("")) {
            output = "1";
        } else if (output.equals("-")) {
            output = "-1";
        }
        return output;
    }

    /**
     * Parses the variables (non-digit characters) from an input.
     */
    public static String getVars(String input) {
        // Setting vars
        boolean isPower = false;
        String output = "";

        // Var construction loop
        for (Character i : input.toCharArray()) {
            // Toggling if it's a power to bypass the "char != digit" condition
            if (i == '^' || i == '(') {
                // So it doesn't check exponent bool on something w/o a var
                if (!output.equals("")) {
                    isPower = true;
                    output += i;
                }
            } else if (i == '|') {
                if (!output.equals("")) {
                    output += i;
                }
            } else if (i == ')') {
                if (!output.equals("")) {
                    isPower = false;
                    output += i;
                }
            } else if ((!Character.isDigit(i) && i != '-') || isPower) {
                output += i;
            }
        }
        return output;
    }

    /**
     * Removes double negatives from strings to avoid number parsing errors.
     * 
     * @param input
     * @return
     */
    public static String parseDoubleNegative(String input) {
        // Settings vars
        boolean skip = false;
        String output = "";

        // Term construction loop
        for (int i = 0; i < input.toCharArray().length; i++) {
            char character = input.toCharArray()[i];
            if (skip) { // would only skip one char, that being the 2nd '-'.
                skip = false;
            } else if (character != '-' && !skip) {
                output = output + character;
            } else if (i != input.toCharArray().length && character == '-' && input.toCharArray()[i + 1] == '-') {
                // if the current char and the next char is a '-'
                skip = true;
            } else { // why would i do this???
                output = output + "-";
            }
        }
        return output;
    }

    /**
     * Does String arithmetics for addition.
     * 
     * @param input
     * @return
     */
    public static String addition(String input) {
        boolean rightSide, power;
        rightSide = power = false;

        String output, lSide, lVar, rSide, rVar;
        output = "error";
        lSide = lVar = rSide = rVar = "";

        for (Character i : input.toCharArray()) {
            if (i == '+') {
                rightSide = true;
                power = false;
            } else if (i == '^' || Character.isLetter(i) || power) {
                power = true;
                if (!rightSide) {
                    lVar = lVar + i;
                } else {
                    rVar = rVar + i;
                }
            } else {
                if (!rightSide) {
                    lSide = lSide + i;
                } else {
                    rSide = rSide + i;
                }
            }
        }

        if (lVar.equals(rVar)) {
            return Integer.toString(Integer.valueOf(lSide) + Integer.valueOf(rSide)) + lVar;
        } else {
            return output; // temporary, make it an equation later.
        }
    }

    public static String subtraction(String input) {
        boolean rightSide, power;
        rightSide = power = false;

        String output, lSide, lVar, rSide, rVar;
        output = "error";
        lSide = lVar = rSide = rVar = "";

        for (Character i : input.toCharArray()) {
            if (i == '_') {
                rightSide = true;
                power = false;
            } else if (i == '^' || Character.isLetter(i) || power) {
                power = true;
                if (!rightSide) {
                    lVar = lVar + i;
                } else {
                    rVar = rVar + i;
                }
            } else {
                if (!rightSide) {
                    lSide = lSide + i;
                } else {
                    rSide = rSide + i;
                }
            }
        }

        if (lVar.equals(rVar)) {
            return Integer.toString(Integer.valueOf(lSide) - Integer.valueOf(rSide)) + lVar;
        } else {
            return output; // temporary, make it an equation later.
        }
    }

    public static String multiplication(String input) {
        // Declaring variables
        boolean rightSide, isVar, isImplicit;
        String lSide, rSide, vars, implicitVar;
        rightSide = isVar = isImplicit = false;
        lSide = rSide = vars = implicitVar = "";

        // Getting the numbers and variables
        input = parseDoubleNegative(input);
        for (Character i : input.toCharArray()) {
            if (i == '*') {
                rightSide = true;
                isVar = false;
            } else if (i == '{' || i == '}') {
                implicitVar += i;
                isImplicit = !isImplicit;
            } else if (isImplicit) {
                implicitVar += i;
            } else if (i == '^' || Character.isLetter(i) || isVar) {
                isVar = true;
                vars += i;
            } else {
                if (!rightSide) {
                    lSide += i;
                } else {
                    rSide += i;
                }
            }
        }

        // Avoid errors with empty strings.
        if (rSide.equals("")) {
            rSide = "1";
        }
        return Integer.toString(Integer.valueOf(lSide) * Integer.valueOf(rSide)) + vars + implicitVar;
    }

    public static String division(String input) {
        boolean rightSide, power;
        String lSide, rSide, vars;
        rightSide = power = false;
        lSide = rSide = vars = "";

        input = parseDoubleNegative(input);
        for (Character i : input.toCharArray()) {
            if (i == '/' && !power) {
                rightSide = true;
            } else if (i == ')') {
                power = false;
                vars = vars + i;
            } else if (i == '^' || Character.isLetter(i) || power) {
                power = true;
                vars = vars + i;
            } else {
                if (!rightSide) {
                    lSide = lSide + i;
                } else {
                    rSide = rSide + i;
                }
            }
        }
        if ((Integer.valueOf(lSide) % Integer.valueOf(rSide) == 0)) {
            return Integer.toString(Integer.valueOf(lSide) / Integer.valueOf(rSide)) + vars;
        } else {
            return new Fraction(lSide + vars, rSide).toString();
        }
    }

    /**
     * Finds the divisors given a number and a divisor.
     * 
     * @param input   : the number that needs to be checked.
     * @param divisor : the number that is divided
     */
    public static ArrayList<Float> findDivisors(float input, int divisor) {
        ArrayList<Float> divisors = new ArrayList<Float>();
        for (float i = divisor; i < input; i += divisor) { // checks for the divisor and multiples
            if (input % i == 0) {
                divisors.add(i); // adds what its divisble by
                divisors.add(input / i); // adds the result
            }
        }
        // for (float i = divisor; i < input; i += divisor) {
        // if (input % i == 0) { // adds what the
        // divisors.add(input / i);
        // }
        // }
        // In the chance there are no divisors, add itself and one.
        if (divisors.size() == 0) {
            divisors.add(1f);
            divisors.add((float) input);
        }
        return divisors;
    }

    /**
     * Finds the prime divisors of the input.
     * 
     * @param input : the number that needs divisors to be found
     */
    public static ArrayList<Float> findPrimeDivisors(float input) {
        ArrayList<Float> divisors = new ArrayList<Float>();
        for (float i = 1; i < input; i++) {
            // Checking to make sure its divisible by the number, but not 2, or 3
            if ((input % i == 0) && ((input % 2 != 0) && (input % 3 != 0))) {
                divisors.add(i);
            }
        }
        // If the divisors is empty, add 1 and itself so the method doesn't return an
        // empty ArrayList.
        if (divisors.size() == 0) {
            divisors.add(1f);
            divisors.add(input);
        }
        return divisors;
    }

    /**
     * Finds the greatest common divisor of two numbers, given a divisor.
     * 
     * @param a       : the first number as a float
     * @param b       : the second number as a float
     * @param divisor : what number should try to divide the two
     *                <ul>
     *                <li>if the divisor is {@code 0}, then the method checks prime
     *                numbers
     * 
     * @return If {@code 1}, then the greatest common divisor is itself, meaning
     *         it's prime and the inputed divisor isn't a divisor.
     *         <li>Any other number, meaning that the output is the GCD.
     */
    public static int greatestCommonDivisor(float a, float b, int divisor) {
        int g_c_d = 1;
        if (divisor == 0) {
            // "key" to check prime numbers, also prevents division by 0 errors
            ArrayList<Float> aDivisors = findPrimeDivisors(a);
            ArrayList<Float> bDivisors = findPrimeDivisors(b);
            for (float i : aDivisors) {
                for (float j : bDivisors) {
                    if ((a % j == 0) && (b % j == 0) && (i == 1 || i == j)) {
                        g_c_d = (int) j;
                    } else if ((a % i == 0) && (b % i == 0) && (j == 1 || i == j)) {
                        g_c_d = (int) i;
                    }
                }
            }
        } else {
            ArrayList<Float> aDivisors = findDivisors(a, divisor);
            ArrayList<Float> bDivisors = findDivisors(b, divisor);
            for (float i : aDivisors) {
                for (float j : bDivisors) {
                    if ((a % j == 0) && (b % j == 0) && (i == 1 || i == j)) {
                        g_c_d = (int) j;
                    } else if ((a % i == 0) && (b % i == 0) && (j == 1 || i == j)) {
                        g_c_d = (int) i;
                    }
                }
            }
        }
        return g_c_d;
    }

    public static ArrayList<Float> findAllDivisors(float a, float b) {
        ArrayList<Float> output = new ArrayList<Float>();
        ArrayList<Float> aDivisors = findPrimeDivisors(a);
        ArrayList<Float> bDivisors = findPrimeDivisors(b);
        for (float i : aDivisors) {
            for (float j : bDivisors) {
                if ((a % j == 0) && (b % j == 0) && (i == 1 || i == j)) {
                    if (!output.contains(j)) {
                        output.add(j);
                    }
                } else if ((a % i == 0) && (b % i == 0) && (j == 1 || i == j)) {
                    if (!output.contains(i)) {
                        output.add(i);
                    }
                }
            }
        }
        aDivisors = findDivisors(a, 2);
        bDivisors = findDivisors(b, 2);
        for (float i : aDivisors) {
            for (float j : bDivisors) {
                if ((a % j == 0) && (b % j == 0) && (i == 1 || i == j)) {
                    if (!output.contains(j)) {
                        output.add(j);
                    }
                } else if ((a % i == 0) && (b % i == 0) && (j == 1 || i == j)) {
                    if (!output.contains(i)) {
                        output.add(i);
                    }
                }
            }
        }
        aDivisors = findDivisors(a, 3);
        bDivisors = findDivisors(b, 3);
        for (float i : aDivisors) {
            for (float j : bDivisors) {
                if ((a % j == 0) && (b % j == 0) && (i == 1 || i == j)) {
                    if (!output.contains(j)) {
                        output.add(j);
                    }
                } else if ((a % i == 0) && (b % i == 0) && (j == 1 || i == j)) {
                    if (!output.contains(i)) {
                        output.add(i);
                    }
                }
            }
        }
        return output;
    }

    /**
     * Checks if the first character is a '-'.
     * 
     * @param input
     * @return
     */
    public static boolean isNegative(String input) {
        if (input.toCharArray()[0] == '-') {
            return true;
        }
        return false;
    }

    /**
     * Checks if the input is made of digits.
     * 
     * @param input
     * @return {@code true} if the input is only digits, {@code false} if there is a
     *         symbol or var.
     */
    public static boolean isPureNumber(String input) {
        for (Character i : input.toCharArray()) {
            if (!Character.isDigit(i)) {
                return false;
            }
        }
        return true;
    }
}
