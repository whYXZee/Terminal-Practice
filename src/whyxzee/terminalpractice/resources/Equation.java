package whyxzee.terminalpractice.resources;

import java.util.ArrayList;

/**
 * The {@code Equation} class consists of an array of terms, stored as
 * Strings. Terms can contain variables, fractions, and numbers.
 */
public class Equation {
    public String[] termArray;

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
        ArrayList<String> outputList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) { // iterate throught the list.
            if (Fraction.isFraction(list.get(i))) {
                Fraction frac = Fraction.toFraction(list.get(i));
                if (!AlgebraFunctions.getCoefficient(frac.toString()).equals("0")) {
                    outputList.add(frac.toString());
                }
            } else {
                if (Integer.valueOf(AlgebraFunctions.getCoefficient(list.get(i))) != 0) {
                    // if not equal to zero, add to output list.
                    outputList.add(list.get(i));
                }
            }

        }
        return outputList;
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

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < termArray.length; i++) {
            String sign = "+";
            String term = "";
            if (termArray[i].toCharArray()[0] == '-' && i != 0) {
                sign = "-";
                for (Character j : termArray[i].toCharArray()) {
                    if (j != '-') {
                        term = term + j;
                    }
                }
            } else {
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
