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

public class SolveForXLinear extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private Equation eq;

    public SolveForXLinear() throws InterruptedException {
    }

    @Override
    public void randomize() {
        ArrayList<String> output = new ArrayList<String>();
        String sign, x;
        double aRandom = Math.random(); // randomize to see if the "a" value is a fraction, one, or another number
        if (aRandom > .6) {
            x = Integer.toString(ScenarioConstants.rng.nextInt(9) + 1) + "x/" + Integer
                    .toString(ScenarioConstants.rng.nextInt(9) + 1);
        } else if (aRandom > .3) {
            x = "x";
        } else {
            x = Integer.toString(ScenarioConstants.rng.nextInt(5) + 1) + "x";
        }
        if (Math.random() > .5) { // randomze if it's positive or negative
            sign = "-";
        } else {
            sign = "";
        }

        output.add(sign + x);
        x = Integer.toString(ScenarioConstants.rng.nextInt(10) + 1);
        if (Math.random() > .5) { // randomize if it's positive or negative
            sign = "-";
        } else {
            sign = "";
        }
        output.add(sign + x);

        if (Math.random() > 0.5) {
            output.add("=0");
        } else {
            x = Integer.toString(ScenarioConstants.rng.nextInt(10) + 1);
            if (Math.random() > .5) { // randomize if it's positive or negative
                sign = "-";
            } else {
                sign = "";
            }
            output.add("=" + sign + x);
        }

        eq = new Equation(output);
    }

    @Override
    public String solve() {
        int equalTerm = Integer.valueOf(Equation.parseEqual(eq.termArray[2]));
        int term_1 = Integer.valueOf(AlgebraFunctions.getCoefficient(eq.termArray[0]));
        int term_2 = Integer.valueOf(eq.termArray[1]);
        System.out.println("=: " + eq.termArray[2] + ", 1: " + eq.termArray[1] + ", 0: " + eq.termArray[0]);

        // add or subtract the constant, based on the inner function.
        boolean shouldSubtract = !AlgebraFunctions.isNegative(Integer.toString(term_2));
        String output = AlgebraFunctions.parseDoubleNegativeEQ(equalTerm + "-" + term_2);
        if (shouldSubtract) {
            output = AlgebraFunctions.subtraction(output);
        } else {
            output = AlgebraFunctions.addition(output);
        }

        // String output =
        // AlgebraFunctions.subtraction(AlgebraFunctions.parseDoubleNegativeEQ(equalTerm
        // + "-" + term_2));
        // System.out.println(output);
        if (Fraction.isFraction(eq.termArray[0])) {
            output = Fraction.divideFraction(output, Integer.toString(term_1)).toString();
        } else {
            output = AlgebraFunctions.division(output + "/" + term_1);
        }
        return output;
    }

    @Override
    public void customActions(String action) {
        if (action.equals("\u2044")) {
            response = textField.getText();
            textField.setText(response + "[] \u2044 []");
        }
    }

    @Override
    public void getQuestion() {
        questions = AppConstants.divideLabel("Solve for x: " + eq);
    }

    @Override
    public void getExtraButtons() {
        extraButtonsArray = new JButton[] { new JButton("\u2044") };
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Solve for x.\nClick the \"\u2044\" button to declare fractions.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
