package whyxzee.terminalpractice.scenarios.number_sense;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.DiscreteMath;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Factorial extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static int number = 0;

    public Factorial() throws InterruptedException {
    }

    /**
     * Randomizes the number to be factorialized.
     */
    @Override
    public void randomize() {
        number = ScenarioConstants.rng.nextInt(11);
    }

    /**
     * Solves the factorial
     * 
     * @return a factorial as a {@code String}, as the numbers can get large.
     */
    @Override
    public String solve() {
        return DiscreteMath.factorial(number).toString();
    }

    @Override
    public void getQuestion() {
        questions = new JLabel[] { new JLabel("What is the factorial of " + number + "?") };
    }

    @Override
    public void getHowTo() {
        if (number > 2) {
            howToLabels = AppConstants.divideLabel("A factorial, denoted by a #!, is the product of all numbers from "
                    + number + " to 1: (" + number + ")(" + number + " - 1) ... (1)");
        } else if (number == 1) {
            howToLabels = AppConstants.divideLabel("A factorial, denoted by a #!, is the product of all numbers from "
                    + "a number to 1. Because the given number is one, then the 1! is 1.");
        } else if (number == 2) {
            howToLabels = AppConstants.divideLabel("A factorial, denoted by a #!, is the product of all numbers from "
                    + "a number to 1. (2)(1) = 2");
        } else {
            howToLabels = AppConstants.divideLabel("A factorial, denoted by a #!, is the product of all numbers from "
                    + "a number to 1. 0! = 1."); // add explanation later?
        }

    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame, "Get the factorial of the given number.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
