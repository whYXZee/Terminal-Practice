package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.resources.Fraction;
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

import java.util.ArrayList;

public class SolveForXLinear extends ScenarioUI implements ActionListener {
    //
    // General variables
    //
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    public SolveForXLinear() throws InterruptedException {
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
            Equation eq = randomize();
            JLabel question = new JLabel("Solve for x: " + eq);
            question.setFont(AppConstants.medFont);
            this.add(question, grid);
            grid.gridy++;

            // Answer box
            textField = new JTextField();
            textField.setColumns(AppConstants.answerColumns);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(AppConstants.smallFont);
            textField.addActionListener(this);
            this.add(textField, grid);
            grid.gridy++;

            // Back button
            JButton backButton = new JButton("End practice");
            backButton.setActionCommand("end");
            backButton.addActionListener(this);
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setFont(AppConstants.medFont);
            backButton.setToolTipText("End the drill early.");
            backButton.setMnemonic(KeyEvent.VK_E);
            this.add(backButton, grid);
            grid.gridy++;

            // Display
            display();
            textField.requestFocusInWindow();
            ScenarioConstants.scenarioSemaphore.acquire();

            // Checking the input
            if (solve(eq).equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correctIncorrect.setFont(AppConstants.bigFont);
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + solve(eq));
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

    private Equation randomize() {
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

        return new Equation(output);
    }

    private String solve(Equation eq) {
        String output = "";
        if (Fraction.isFraction(eq.termArray[0])) {
            output = Fraction.divideFraction("-" + eq.termArray[1],
                    AlgebraFunctions.getCoefficient(eq.termArray[0])).toString();
        } else {
            output = AlgebraFunctions
                    .division("-" + eq.termArray[1] + "/" + AlgebraFunctions.getCoefficient(eq.termArray[0]));
        }
        return output;
    }
}
