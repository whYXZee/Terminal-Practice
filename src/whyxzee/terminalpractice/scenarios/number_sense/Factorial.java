package whyxzee.terminalpractice.scenarios.number_sense;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.DiscreteMath;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class Factorial extends ScenarioUI implements ActionListener {
    //
    // General variables
    //
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    //
    // Scenario-specific variables
    //
    private static int number = 0;

    public Factorial() throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        for (int i = 0; i < AppConstants.goal; i++) {

            // Question tracker
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + AppConstants.goal);
            questionTracker.setFont(AppConstants.biggerFont);
            this.add(questionTracker, grid);
            grid.gridy++;

            // Question
            randomize();
            JLabel question = new JLabel("What is the factorial of " + number + "?");
            question.setFont(AppConstants.medFont);
            this.add(question, grid);
            grid.gridy++;

            // Answer box
            textField = new JTextField();
            textField.addActionListener(this);
            textField.setColumns(AppConstants.answerColumns);
            textField.setFont(AppConstants.smallFont);
            textField.setHorizontalAlignment(JTextField.CENTER);
            this.add(textField, grid);
            grid.gridy++;

            // End button
            JButton backButton = new JButton("End practice");
            backButton.addActionListener(this);
            backButton.setActionCommand("end");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setFont(AppConstants.medFont);
            backButton.setToolTipText("End the drill early.");
            this.add(backButton, grid);
            grid.gridy++;

            // Display
            display();
            textField.requestFocusInWindow();
            ScenarioConstants.scenarioSemaphore.acquire();

            // Checking the input
            if (solve().equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correctIncorrect.setFont(AppConstants.bigFont);
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
                correctIncorrect.setFont(AppConstants.bigFont);
            }
            this.add(correctIncorrect, grid);
            display();
            Thread.sleep(2000);
            this.removeAll();
        }
        JOptionPane.showMessageDialog(AppConstants.frame, "You got " + correct + " correct!", "Scenario Completion",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("timer")) {
            this.response = textField.getText();
        } else if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
        } else {
            this.response = textField.getText();
        }
        ScenarioConstants.scenarioSemaphore.release();
    }

    /**
     * Randomizes the number to be factorialized.
     */
    private void randomize() {
        number = ScenarioConstants.rng.nextInt(11);
    }

    /**
     * Solves the factorial
     * 
     * @return a factorial as a {@code String}, as the numbers can get large.
     */
    private String solve() {
        return DiscreteMath.factorial(number).toString();
    }
}
