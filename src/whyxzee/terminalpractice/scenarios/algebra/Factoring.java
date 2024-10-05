package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Factoring extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static ProblemType problemType = ProblemType.SFFT;
    private static Equation eq = new Equation(new ArrayList<String>());
    private static HashMap<Character, Integer> varsAndVals = new HashMap<Character, Integer>();
    private static String x = "";
    private static String y = "";
    private static String xTerm = "";
    private static String yTerm = "";
    // private static ArrayList<String> equationList = new ArrayList<String>();

    // What combinatoric needs to be found?
    private enum ProblemType {
        SFFT // Simon's Favorite Factoring Trick
    }

    public Factoring() throws InterruptedException {
    }

    @Override
    public void randomize() {
        // Resetting vars in between questions
        varsAndVals = new HashMap<Character, Integer>();
        // equationList = new ArrayList<String>();

        switch (0) {
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
            default:
                return "";
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case SFFT:
                questions = AppConstants.divideLabel(
                        "Given the equation: " + eq
                                + ", what are the factors? [answer in the form of (x + #)(y + #) = #]");
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
        }
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame, "Factor the given equation.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
