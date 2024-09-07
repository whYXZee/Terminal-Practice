package resources;

import java.util.ArrayList;
// import java.util.HashMap;

public class MathFunctions {
    public static int getCoefficient(String input) {
        // Setting vars
        boolean exponent = false;
        String output = "";
        // Number construction loop
        for (Character i : input.toCharArray()) {
            if (Character.isDigit(i) || i == '-' || (i == '/' && !exponent)) {
                output = output + i;
            }
        }
        // Make it one if it's only vars
        if (output.equals("")) {
            output = "1";
        } else if (output.equals("-")) {
            output = "-1";
        }
        return Integer.valueOf(parseDoubleNegative(output));
    }

    public static String getFracCoefficient(String input) {
        String output = "";
        for (Character i : input.toCharArray()) {
            if (!Character.isDigit(i) && i != '-' && i != '/') {
            } else {
                output = output + i;
            }
        }
        // Making it one if it's only vars
        if (output.equals("")) {
            output = "1";
        } else if (output.equals("-")) {
            output = "-1";
        }
        return output;
    }

    public static String getVars(String input) {
        String output = "";
        boolean power = false;
        // Var construction loop
        for (Character i : input.toCharArray()) {
            // Toggling if it's a power to bypass the "char not digit" condition
            if (i == '^') {
                power = true;
            } else if (i == ')') {
                power = false;
            }
            if ((!Character.isDigit(i) && i != '-') || power) {
                output = output + i;
            }
        }
        return output;
    }

    public static String parseDoubleNegative(String input) {
        String output = "";
        boolean skip = false;
        for (int i = 0; i < input.toCharArray().length; i++) {
            char character = input.toCharArray()[i];
            if (skip) {
                skip = false;
            } else if (character != '-' && !skip) {
                output = output + character;
            } else if (i != input.toCharArray().length && character == '-' && input.toCharArray()[i + 1] == '-') {
                skip = true;
            } else {
                output = output + "-";
            }
        }
        return output;
    }

    public static String multiplication(String input) {
        boolean rightSide, power;
        String lSide, rSide, vars;
        rightSide = power = false;
        lSide = rSide = vars = "";

        input = parseDoubleNegative(input);
        for (Character i : input.toCharArray()) {
            if (i == '*') {
                rightSide = true;
                power = false;
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
        return Integer.toString(Integer.valueOf(lSide) * Integer.valueOf(rSide)) + vars;
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
        if (divisors.size() == 0) { // incase there are no divisors, add itself and one.
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

    // Finds the derivative of a term via the power rule
    // public static String derivPower(String input) {
    // String coefficient, var;

    // if (Fraction.isFraction(input)) {
    // coefficient = getFracCoefficient(input);
    // } else {
    // coefficient = Integer.toString(getCoefficient(input));
    // }
    // var = getVars(input);

    // return
    // }
}
