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

        // Multiply the coefficients
        if (Fraction.isFraction(coefficient) || Fraction.isFraction(exponent)) {
            coefficient = Fraction.multiplyFractions(coefficient, exponent).toString();
        } else {
            coefficient = Integer.toString(Integer.valueOf(coefficient) * Integer.valueOf(exponent));
        }

        // Change the power
        if (Fraction.isFraction(exponent)) {
            exponent = Fraction.subtractFraction(exponent, "1").toString();
        } else {
            exponent = Integer.toString(Integer.valueOf(exponent) - 1);
        }

        // Implicit Differentiation
        if (implicit && var.equals(derivativeOf)) {
            implicitVar = "{d" + var + "/d" + respectTo + "}";
        }

        // Adding to the var
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
}
