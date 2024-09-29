package whyxzee.terminalpractice.scenarios.number_sense;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import javax.swing.JOptionPane;

import java.util.ArrayList;

public class SimplifyEquation extends ScenarioUI {
    //
    // Scenario-specific variables
    //

    // What will be asked?
    private enum ProblemType {
        ADDITION_SUBTRACTION,
        MULTIPLICATION
    }

    private Equation eq = new Equation(new ArrayList<String>());
    private ProblemType problemType = ProblemType.ADDITION_SUBTRACTION;

    public SimplifyEquation() throws InterruptedException {
    }

    @Override
    public void randomize() {
        // Declaring vars
        ArrayList<String> output = new ArrayList<String>();
        int termNumber = 2;

        // Randomizing the equation type
        switch (ScenarioConstants.rng.nextInt(2)) {
            case 0:
                problemType = ProblemType.ADDITION_SUBTRACTION;
                if (Math.random() > .6) {
                    termNumber = 3;
                }
                break;
            case 1:
                problemType = ProblemType.MULTIPLICATION;
                break;
        }

        // Randomize terms
        for (int i = 0; i < termNumber; i++) {
            String term = "";
            if (Math.random() > .5) {
                term = "-";
            }
            term = term + Integer.toString(ScenarioConstants.rng.nextInt(25) + 1);
            output.add(term);
        }
        eq = new Equation(output);

    }

    @Override
    public String solve() {
        String output = "";
        switch (problemType) {
            case ADDITION_SUBTRACTION:
                output = AlgebraFunctions.addition(eq.termArray[0] + "+" + eq.termArray[1]);
                if (eq.termArray.length == 3) {
                    output = AlgebraFunctions.addition(output + "+" + eq.termArray[2]);
                }
                break;
            case MULTIPLICATION:
                output = AlgebraFunctions.multiplication(eq.termArray[0] + "*" + eq.termArray[1]);
                break;
        }
        return output;
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case ADDITION_SUBTRACTION:
                questions = AppConstants.divideLabel("Simplify: " + eq.toString());
                break;
            case MULTIPLICATION:
                questions = AppConstants.divideLabel("Simplify: " + eq.termArray[0] + " * " + eq.termArray[1]);
                break;
        }
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame, "Simplify the given equation.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
