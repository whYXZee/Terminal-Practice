package whyxzee.terminalpractice.scenarios.number_sense;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.resources.Equation;
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

public class SimplifyEquation extends ScenarioUI implements ActionListener {
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

    // What will be asked?
    private enum ProblemType {
        ADDITION_SUBTRACTION,
        MULTIPLICATION
    }

    private Equation eq = new Equation(new ArrayList<String>());
    private ProblemType problemType = ProblemType.ADDITION_SUBTRACTION;

    public SimplifyEquation() throws InterruptedException {
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
            eq = randomize();
            JLabel question = new JLabel("Simplify: " + printEQ());
            question.setFont(AppConstants.bigFont);
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
            // Displaying if it's correct or incorrect.
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
     * Randomizes the given problem.
     */
    private Equation randomize() {
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
        return new Equation(output);

    }

    /**
     * Solves the randomized equation.
     */
    private String solve() {
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

    /**
     * Prints the randomized equation.
     */
    private String printEQ() {
        // String output = "";
        switch (problemType) {
            case ADDITION_SUBTRACTION:
                return eq.toString();
            case MULTIPLICATION:
                return eq.termArray[0] + " * " + eq.termArray[1];
            default:
                return "";
        }
    }
}
