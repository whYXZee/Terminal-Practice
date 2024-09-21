package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.RunApplication;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.resources.Fraction;
import whyxzee.terminalpractice.scenarios.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.util.ArrayList;

public class QuadFactors extends ScenarioUI implements ActionListener {
    // General variables
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    Timer timer = new Timer(4500, this);
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    // Scenario-specific variables

    public QuadFactors() throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        for (int i = 0; i < RunApplication.goal; i++) {
            RunApplication.getFontSize();
            // Showing the equation
            Equation eq = randomize();
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + RunApplication.goal);
            JLabel question = new JLabel("Solve for the factors of x: " + eq);
            questionTracker.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));
            question.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 3));
            this.add(questionTracker, grid);
            grid.gridy++;
            this.add(question, grid);
            grid.gridy++;

            textField = new JTextField();
            textField.setColumns(RunApplication.getColumns());
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
            textField.addActionListener(this);
            this.add(textField, grid);
            grid.gridy++;

            // Timer stuff
            if (ScenarioConstants.timerEnabled) {
                timer.restart();
                timer.setActionCommand("timer");
                timer.start();
            }

            JButton backButton = new JButton("End practice");
            backButton.setActionCommand("end");
            backButton.setPreferredSize(new Dimension(150, 25));
            backButton.setToolTipText("End the drill early.");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.addActionListener(this);
            this.add(backButton, grid);
            grid.gridy++;

            display();
            textField.requestFocusInWindow();

            ScenarioConstants.scenarioSemaphore.acquire();

            if (solve(eq).equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + solve(eq));
            }
            this.add(correctIncorrect, grid);
            display();
            Thread.sleep(2000);
            this.removeAll();
        }
        correctIncorrect = new JLabel("Congratuations, you got " + correct + " correct!");
        display();
        Thread.sleep(2000);
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
        if (ScenarioConstants.timerEnabled) {
            timer.stop();
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
        howToLabels = RunApplication.divideLabel("");

        for (JLabel label : howToLabels) {
            label.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
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
