package whyxzee.terminalpractice.resources;

import java.util.ArrayList;

/**
 * The {@code Equation} class consists of an array of terms, stored as
 * Strings. Terms can contain variables, fractions, and numbers.
 * 
 * Valid terms are:
 * <ul>
 * <li>+#
 * <li>-#
 * <li>=#
 */
public class Equation {
    public String[] termArray;

    // public enum EquationType {

    // }

    //
    // Equation construction
    //

    public Equation(ArrayList<String> termList) {
        this.termArray = parseZero(termList).toArray(new String[parseZero(termList).size()]);
    }

    /**
     * Changes the equation post-declaration.
     * 
     * @param termList : list of terms.
     */
    public void changeEq(ArrayList<String> termList) {
        this.termArray = parseZero(termList).toArray(new String[parseZero(termList).size()]);
    }

    /**
     * Removes any zero terms from the equation.
     * 
     * @param list : list of terms.
     */
    public static ArrayList<String> parseZero(ArrayList<String> list) {
        // Setting vars
        ArrayList<String> outputList = new ArrayList<String>();

        // Going through the input term list
        for (int i = 0; i < list.size(); i++) {
            if (Fraction.isFraction(list.get(i))) {
                // Check if fraction

                Fraction frac = Fraction.toFraction(list.get(i));
                if (!AlgebraFunctions.getCoefficient(frac.toString()).equals("0")) {
                    // If the coefficient is not 0, add to the output

                    outputList.add(frac.toString());
                }

            } else if (list.get(i).contains("=")) {
                // If it's the right of the equal sign

                outputList.add(list.get(i));
            } else {
                // Nothing special with it

                if (Integer.valueOf(AlgebraFunctions.getCoefficient(list.get(i))) != 0) {
                    // if not equal to zero, add to output list.

                    outputList.add(list.get(i));
                }
            }

        }
        return outputList;
    }

    /** */
    public static String parseEqual(String input) {
        String output = "";
        for (Character i : input.toCharArray()) {
            if (i != '=') {
                output += i;
            }
        }
        return output;
    }

    /**
     * Turns a String input into an Equation.
     * 
     * @param input : terms must be differentiated by a "+" or a "-" with spaces
     *              surrounding the signs.
     */
    public static Equation toEquation(String input) {
        String term = "";
        ArrayList<String> termList = new ArrayList<String>();
        boolean termEnded = false;
        for (int i = 0; i < input.toCharArray().length; i++) {
            char character = input.toCharArray()[i];
            if (character == ' ' && !termEnded) {
                termEnded = true;
                termList.add(term);
                term = "";
            } else if (character == ' ' && termEnded) {
                termEnded = false;
            } else if (character != '+') {
                term = term + character;
            }
        }
        return new Equation(termList);
    }

    //
    // Conditionals
    //

    @Override
    public String toString() {
        // Setting vars
        String output = "";

        // Go through the terms
        for (int i = 0; i < termArray.length; i++) {
            String sign = "+";
            String term = "";
            if (termArray[i].toCharArray()[0] == '-' && i != 0) {
                // Minus

                sign = "-";
                for (Character j : termArray[i].toCharArray()) {
                    if (j != '-') {
                        term += j;
                    }
                }

            } else if (termArray[i].toCharArray()[0] == '=') {
                // Right of equal sign

                sign = "=";
                for (Character j : termArray[i].toCharArray()) {
                    if (j != '=') {
                        term += j;
                    }
                }

            } else {
                // Plus

                term = termArray[i];
            }

            if (i == 0) {
                output = term;
            } else {
                output = output + " " + sign + " " + term;
            }
        }
        return output;
    }
}
