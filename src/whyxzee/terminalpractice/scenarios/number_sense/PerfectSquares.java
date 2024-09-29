package whyxzee.terminalpractice.scenarios.number_sense;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class PerfectSquares extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static int number = 0;

    public PerfectSquares() throws InterruptedException {
    }

    @Override
    public void randomize() {
        number = ScenarioConstants.rng.nextInt(25) + 1;
    }

    /**
     * @return the square of the number as a {@code String}.
     */
    @Override
    public String solve() {
        return Integer.toString((int) Math.pow(number, 2));
    }

    @Override
    public void getQuestion() {
        questions = new JLabel[] { new JLabel("What is the square of " + number + "?") };
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame, "Get the square of the given number.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

}
