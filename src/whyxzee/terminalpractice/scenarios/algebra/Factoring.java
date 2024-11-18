package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.resources.Fraction;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.HashMap;

public class Factoring extends ScenarioUI {
    //
    // Scenario-specific variables
    //

    // UI
    public JButton plusMinus = new JButton("\u00b1");

    // General
    private static ProblemType problemType = ProblemType.SFFT;
    private static Equation eq = new Equation(new ArrayList<String>());

    // SFFT
    private static HashMap<Character, Integer> varsAndVals = new HashMap<Character, Integer>();
    private static String x = "";
    private static String y = "";
    private static String xTerm = "";
    private static String yTerm = "";

    // Completing the Square
    private static int a = 0;
    private static int b = 0;
    private static String cSquare = "";
    private static int c = 0;
    private static String cString = "";
    private static int equal = 0;
    private static String zero = "";

    // What combinatoric needs to be found?
    private enum ProblemType {
        SFFT, // Simon's Favorite Factoring Trick
        COMPLETING_SQUARE,
    }

    public Factoring() throws InterruptedException {
    }

    @Override
    public void randomize() {
        // Resetting vars in between questions
        varsAndVals = new HashMap<Character, Integer>();
        // equationList = new ArrayList<String>();

        switch (1) {
            case 0:
                problemType = ProblemType.SFFT;

                // Randomize values
                if (Math.random() > .5) {
                    varsAndVals.put('x', -(ScenarioConstants.rng.nextInt(10) + 1));
                } else {
                    varsAndVals.put('x', ScenarioConstants.rng.nextInt(10) + 1);
                }

                if (Math.random() > .5) {
                    varsAndVals.put('y', -(ScenarioConstants.rng.nextInt(10) + 1));
                } else {
                    varsAndVals.put('y', ScenarioConstants.rng.nextInt(10) + 1);
                }

                if (Math.random() > .5) {
                    varsAndVals.put('=', -(ScenarioConstants.rng.nextInt(10) + 1));
                } else {
                    varsAndVals.put('=', ScenarioConstants.rng.nextInt(10) + 1);
                }

                // Formatting
                x = varsAndVals.get('x') + "x";
                y = varsAndVals.get('y') + "y";

                if (Math.abs(varsAndVals.get('x')) == 1) {
                    x = "x";
                }
                if (Math.abs(varsAndVals.get('y')) == 1) {
                    y = "y";
                }

                eq = new Equation(new ArrayList<String>() {
                    {
                        add("xy");
                        add(x);
                        add(y);
                        add("=" + varsAndVals.get('='));
                    }
                });
                break;
            case 1:
                problemType = ProblemType.COMPLETING_SQUARE;

                // A value
                int a;
                if (false) {
                    a = rng.nextInt(5) + 1;
                } else {
                    a = 1;
                }

                eq = new Equation(new ArrayList<String>() {
                    {
                        add(a + "x^(2)");
                        add(rng.nextInt(25) + 1 + "x");
                        add(Integer.toString(rng.nextInt(25) + 1));
                        add("=0");
                    }
                });
                break;
        }
    }

    @Override
    public String solve() {
        switch (problemType) {
            case SFFT:
                // Var Declaration
                xTerm = "";
                yTerm = "";

                // Building the var
                if (varsAndVals.get('y') > 0) {
                    xTerm = "(x + " + varsAndVals.get('y') + ")";
                } else {
                    xTerm = "(x - " + Math.abs(varsAndVals.get('y')) + ")";
                }
                if (varsAndVals.get('x') > 0) {
                    yTerm = "(y + " + varsAndVals.get('x') + ")";
                } else {
                    yTerm = "(y - " + Math.abs(varsAndVals.get('x')) + ")";
                }
                return xTerm + yTerm + " = " + (varsAndVals.get('=') + (varsAndVals.get('x') * varsAndVals.get('y')));
            case COMPLETING_SQUARE:
                a = Integer.valueOf(AlgebraFunctions.getCoefficient(eq.termArray[0]));
                b = Integer.valueOf(AlgebraFunctions.getCoefficient(eq.termArray[1]));
                c = Integer.valueOf(AlgebraFunctions.getCoefficient(eq.termArray[2]));
                equal = -c;

                zero = Fraction.toFraction(b + "|2").toString();
                cSquare = Fraction.toFraction((int) Math.pow(b, 2) + "|4").toString();

                // check if it's negative
                if (AlgebraFunctions.isNegative(Integer.toString(b))) {

                    // Check if c is a fraction
                    if (Fraction.isFraction(cSquare)) {
                        cString = Fraction.addFractions(cSquare, Integer.toString(equal)).toString();

                        return "-" + zero + " \u00b1 \u221a(" + cString + ")";
                    } else {

                        equal += Integer.valueOf(cSquare);

                        // Check if the equals side is an integer
                        if (Math.sqrt(equal) % 1 == 0) {
                            equal = (int) Math.sqrt(equal);

                            return zero + " \u00b1 " + equal;
                        } else {
                            return zero + " \u00b1 \u221a(" + equal + ")";
                        }
                    }
                } else {

                    // Check if c is a fraction
                    if (Fraction.isFraction(cSquare)) {
                        cString = Fraction.addFractions(cSquare, Integer.toString(equal)).toString();

                        return "-" + zero + " \u00b1 \u221a(" + cString + ")";
                    } else {

                        equal += Integer.valueOf(cSquare);

                        // Check if the equals side is an integer
                        if (Math.sqrt(equal) % 1 == 0) {
                            equal = (int) Math.sqrt(equal);

                            return "-" + zero + " \u00b1 " + equal;
                        } else {
                            return "-" + zero + " \u00b1 \u221a(" + equal + ")";
                        }
                    }
                }

            default:
                return "";
        }
    }

    @Override
    public void customActions(String action) {
        if (action.equals("\u00b1")) {
            response = textField.getText();
            textField.setText(response + "\u00b1");
        } else if (action.equals("\u221a")) {
            response = textField.getText();
            textField.setText(response + "\u221a()");
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case SFFT:
                questions = AppConstants.divideLabel("Given the equation: " + eq
                        + ", what are the factors? [answer in the form of (x + #)(y + #) = #]");
                break;
            case COMPLETING_SQUARE:
                questions = AppConstants.divideLabel("What are the factors of " + eq
                        + "? [answer in the form of # +- \u221a(#), and simplify when necessary]");
                break;
        }
    }

    @Override
    public void getHowTo() {
        switch (problemType) {
            case SFFT:
                howToLabels = AppConstants.divideLabel("First, factor out the x: x" + yTerm
                        + ". Second, add the number that is the product of the x coefficient and the y coefficient to both sides: "
                        + varsAndVals.get('x') + " * " + varsAndVals.get('y') + " = "
                        + (varsAndVals.get('y') * varsAndVals.get('x')) + ". Finally, factor out the y term: " + yTerm
                        + ". The result should be: " + xTerm + yTerm + " = " + varsAndVals.get('=') + " + "
                        + (varsAndVals.get('y') * varsAndVals.get('x')));
                break;
            case COMPLETING_SQUARE:
                howToLabels = AppConstants.divideLabel("First, move the constant to the equal side. Then, "
                        + "use the formula (b/2) to find what was squared: " + zero
                        + ". Afterwards, complete the square by squaring "
                        + "what was just found and add it to both sides: " + cSquare + ". Then, use the " + zero
                        + " to get the binomial square: (x + " + zero + "). Next, square root both sides. "
                        + "Because a positive or negative value can be used to get a square, we add the \u00b1 "
                        + "to cover both the positive and negative value. Then, move the " + zero
                        + "to the other side. This technique is known as \"Completing the Square\"");
                break;
        }
    }

    @Override
    public void getExtraButtons() {
        switch (problemType) {
            case COMPLETING_SQUARE:
                extraButtonsArray = new JButton[] { new JButton("\u00b1"), new JButton("\u221a") };
                break;
            default:
                extraButtonsArray = new JButton[] {};
                break;
        }
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame, "Factor the given equation.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
