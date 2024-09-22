package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.resources.Fraction;
import whyxzee.terminalpractice.scenarios.*;

import java.awt.Dimension;
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

public class QuadFactors extends ScenarioUI implements ActionListener {
    //
    // General variables
    //
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    public QuadFactors() throws InterruptedException {
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
            JLabel question = new JLabel("Solve for the factors of x: " + eq);
            question.setFont(AppConstants.medFont);
            this.add(question, grid);
            grid.gridy++;

            // Answer box
            textField = new JTextField();
            textField.addActionListener(this);
            textField.setColumns(AppConstants.answerColumns);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(AppConstants.smallFont);
            this.add(textField, grid);
            grid.gridy++;

            // Back button
            JButton backButton = new JButton("End practice");
            backButton.addActionListener(this);
            backButton.setActionCommand("end");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setToolTipText("End the drill early.");
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
        String a, c;

        // A term
        a = Integer.toString(ScenarioConstants.rng.nextInt(4) + 1);
        if (Math.random() > .5) {
            a = "-" + a;
        }

        // C term
        c = Integer.toString(ScenarioConstants.rng.nextInt(40) + 1);
        if (Math.random() > .5) { // randomze if it's positive or negative
            c = "-" + c;
        }

        output.add(a + "x^2");
        // output.add(Math.multiplication + "x");

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

    private void printHowTo() {
        // Removing everything to re-do window:
        this.removeAll();
        ScenarioConstants.grid.gridx = 0;
        ScenarioConstants.grid.gridy = 0;

        // Adding everything
        JLabel[] howToLabels = {};
        this.add(correctIncorrect, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;
        howToLabels = AppConstants.divideLabel("");

        for (JLabel label : howToLabels) {
            label.setFont(AppConstants.medFont);
            this.add(label, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }

        // Button
        JButton continueButton = new JButton("Continue");
        continueButton.setActionCommand("move on");
        continueButton.setPreferredSize(new Dimension(150, 25));
        continueButton.setToolTipText("Continue to the next problem.");
        continueButton.setMnemonic(KeyEvent.VK_C);
        continueButton.addActionListener(this);

        this.add(continueButton, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;

        display();
    }
}
