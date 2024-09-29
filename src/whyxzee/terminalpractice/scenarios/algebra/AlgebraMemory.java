package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.ArrayList;

// add daemon
public class AlgebraMemory extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static Equation eq;

    public AlgebraMemory() throws InterruptedException {
    }

    @Override
    public void runProblem(int questionNum) throws InterruptedException {
        resetGrid();
        questionTracker(questionNum);

        randomize();
        printQuestion();

        // Displaying question then removing it
        display();
        textField.setEnabled(false);
        Thread.sleep(1500);
        for (JLabel label : questions) {
            this.remove(label);
        }
        // this.remove(question);

        textField();
        backButton();

        display();
        ScenarioConstants.scenarioSemaphore.acquire();
    }

    @Override
    public void randomize() {
        int termNumber = 2;
        if (Math.random() > .6) {
            termNumber = 3;
        }
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < termNumber; i++) {
            String term = "";
            if (Math.random() > .5) {
                term = "-";
            }
            term = term + Integer.toString(ScenarioConstants.rng.nextInt(25) + 1);
            if (i == 0 && termNumber == 3) {
                term = term + "x^2";
            } else if ((i == 0 && termNumber == 2) || (i == 1 && termNumber == 3)) {
                term = term + "x";
            }
            output.add(term);
        }
        eq = new Equation(output);
    }

    @Override
    public String solve() {
        return eq.toString();
    }

    @Override
    public void getQuestion() {
        questions = new JLabel[] { new JLabel("Remember: " + eq) };
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "A random equation will appear, then disappear after some time.\nRemember the equation and answer it as you see it.\n(add space in between signs)",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
