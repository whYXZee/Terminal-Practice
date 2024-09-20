package whyxzee.terminalpractice.resources;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * String-based algebraic functions. String-based as in strings that want to
 * accomplish arithmetics, with the inclusion of variables.
 * 
 * Syntax:
 * <ul>
 * <li>(): denotes the start/end of an exponent, used for fractional exponents,
 * terms with multiple vars & exponents, or when vars are in the exponent.
 * <li>[]: denotes the start/end of a ___
 * <li>{d[var]/d[var]}: denotes that the variable is a derivative with respect
 * to the bottom derivative.
 * <li>^: denotes the power/exponent
 * <li>-: denotes a negative
 * <li>+: marks the left/right sides of an addition problem
 * <li>*: marks the left/right sides of a multiplication problem
 * <li>/: marks the left/right sides of a division problem
 * <li>|: marks the numerator/denominator of a fraction
 */
public class CalculusFunctions {
    /**
     * Using the derivative power rule, the derivative of a String term is found.
     * 
     * @param input        what the function should derive
     * @param derivativeOf what variable should the derivitave be
     * @param respectTo    with respect to the variable.
     * @return derivative of the input.
     */
    public static String derivPowerRule(String input, boolean implicit, String derivativeOf, String respectTo) {
        // Setting vars
        boolean isExponent = false;
        boolean pureNumber;
        String exponent, var, implicitVar;
        exponent = var = implicitVar = "";
        String coefficient = AlgebraFunctions.getCoefficient(input);

        // Finds the exponent & the var
        for (Character i : input.toCharArray()) {
            if (i == '^') {
                isExponent = true;
            } else if (isExponent && i != '(' && i != ')') {
                exponent += i;
            } else if (i == ')') {
                isExponent = false;
            } else if (Character.isLetter(i)) {
                var += i;
            }
        }

        // in case there is no closing parenthesis or '^'.
        if (!var.equals("") && exponent.equals("")) {
            exponent = "1";
        } else if (var.equals("") && exponent.equals("")) {
            return "0";
        }

        //
        // Apply the derivative power rule.
        //
        pureNumber = AlgebraFunctions.isPureNumber(exponent);

        // Multiply the coefficients
        if (Fraction.isFraction(coefficient) || Fraction.isFraction(exponent)) {
            coefficient = Fraction.multiplyFractions(coefficient, exponent).toString();
        } else if (!pureNumber) { // var in the exponent
            coefficient = AlgebraFunctions.multiplication(coefficient + "*" + exponent);
        } else {
            coefficient = Integer.toString(Integer.valueOf(coefficient) * Integer.valueOf(exponent));
        }

        // Change the power
        if (Fraction.isFraction(exponent)) {
            exponent = Fraction.subtractFraction(exponent, "1").toString();
        } else if (!pureNumber) {
            exponent += "-1";
        } else {
            exponent = Integer.toString(Integer.valueOf(exponent) - 1);
        }

        // Implicit Differentiation
        if (implicit && var.equals(derivativeOf)) {
            implicitVar = "{d" + var + "/d" + respectTo + "}";
        }

        // Putting it all together
        if (exponent.equals("1")) {
            return coefficient + var + implicitVar;
        } else if (exponent.equals("0")) {
            return coefficient + implicitVar;
        } else if (Fraction.isFraction(exponent) || Fraction.isFraction(var)) {
            // So a null-ish thing isn't multiplied
            if (var.equals("")) {
                var = "1";
            }

            return Fraction.multiplyFractions(coefficient, var + "^(" + exponent + ")" + implicitVar).toString();
        } else {
            return coefficient + var + "^(" + exponent + ")" + implicitVar;
        }
    }

    /**
     * Using the derivative power rule, the derivative of a String term is found.
     * 
     * @param input        what the function should derive
     * @param implicit     whether or not an implicit differentiation should be
     *                     applied
     * @param derivativeOf what variable should the derivative be
     * @param respectTo    with respect to the variable
     * @return derivative of the input
     */
    public static Equation derivProductRule(String input, boolean implicit, String derivativeOf, String respectTo) {
        // Setting vars
        boolean isExponent = false;
        String exponent, var;
        exponent = var = "";
        String coefficient = AlgebraFunctions.getCoefficient(input);
        HashMap<String, String> termAndExponents = new HashMap<String, String>();
        ArrayList<String> derivedTerms = new ArrayList<String>();

        // Derivative of a constant, do later

        // Finds the exponent & the var
        for (Character i : input.toCharArray()) {
            if (i == '^') {
                isExponent = true;
            } else if (isExponent && i != '(' && i != ')') {
                exponent += i;
            } else if (i == ')') {
                isExponent = false;
                termAndExponents.put(var, exponent);
                var = "";
                exponent = "";
            } else if (Character.isLetter(i)) {
                if (var.equals("")) { // is var is empty
                    var = Character.toString(i);
                } else { // if there is already a var, make it exponent of 1
                    termAndExponents.put(var, "1");
                    var = Character.toString(i);
                }
            }
        }

        // in case there is no closing parenthesis or '^'.
        if (!var.equals("") && exponent.equals("")) {
            termAndExponents.put(var, "1");
            var = "";
        } // do derivative of a constant later

        //
        // Apply the derivative product rule via equation.
        //

        // Making the overall equation of the product rule, i will be the current
        // derived var
        for (String i : termAndExponents.keySet()) {
            // Setting vars
            String currentExponent = termAndExponents.get(i);
            String currentCoefficient = "";
            boolean pureNumber = AlgebraFunctions.isPureNumber(input);

            //
            // Getting the derivative
            //

            // Multiply the coefficients
            if (Fraction.isFraction(coefficient) || Fraction.isFraction(currentExponent)) {
                currentCoefficient = Fraction.multiplyFractions(coefficient, currentExponent).toString();
            } else if (!pureNumber) {
                currentCoefficient = AlgebraFunctions.multiplication(coefficient + "*" + currentExponent);
            } else {
                currentCoefficient = Integer.toString(Integer.valueOf(coefficient) * Integer.valueOf(currentExponent));
            }

            // Change the power
            if (Fraction.isFraction(currentExponent)) {
                currentExponent = Fraction.subtractFraction(currentExponent, "1").toString();
            } else if (!pureNumber) {
                currentExponent = currentExponent + "-1";
            } else {
                currentExponent = Integer.toString(Integer.valueOf(currentExponent) - 1);
            }

            // Piecing the term together.
            String term = currentCoefficient;
            for (String j : termAndExponents.keySet()) {
                // if i & j aren't the same, add the var to the output
                if (!j.equals(i)) {
                    // For printing purposes, we don't want var^(1)
                    if (termAndExponents.get(j).equals("1")) {
                        term += j;
                    } else {
                        term += j + "^(" + termAndExponents.get(j) + ")";
                    }
                } else { // the derivative of the current var
                    if (currentExponent.equals("1")) {
                        term += i;
                    } else if (currentExponent.equals("0")) { // cuz it would be 0 now.

                    } else {
                        term += i + "^(" + currentExponent + ")";
                    }
                }
            }

            // Implicit Differentiation
            if (implicit && i.equals(derivativeOf)) {
                term += "{d" + derivativeOf + "/d" + respectTo + "}";
            }
            derivedTerms.add(term);
        }

        // Implicit Differentiation
        // if (implicit && var.equals(derivativeOf)) {
        // implicitVar = "{d" + var + "/d" + respectTo + "}";
        // }

        return new Equation(derivedTerms);
    }
}