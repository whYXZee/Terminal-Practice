package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.AppConstants;
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

public class AlgebraMemory extends ScenarioUI implements ActionListener {
    //
    // General variables
    //
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    public AlgebraMemory() throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        for (int i = 0; i < AppConstants.goal; i++) {
            // Showing the equation
            Equation eq = randomize();
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + AppConstants.goal);
            JLabel question = new JLabel("Remember: " + eq);
            questionTracker.setFont(AppConstants.biggerFont);
            question.setFont(AppConstants.medFont);
            this.add(questionTracker, grid);
            grid.gridy++;
            this.add(question, grid);
            display();
            Thread.sleep(1500);

            // Ask question
            this.remove(question);
            textField = new JTextField();
            textField.setColumns(AppConstants.answerColumns);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(AppConstants.smallFont);
            textField.addActionListener(this);
            this.add(textField, grid);
            grid.gridy++;

            JButton backButton = new JButton("End practice");
            backButton.addActionListener(this);
            backButton.setActionCommand("end");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setFont(AppConstants.medFont);
            backButton.setToolTipText("End the drill early.");
            this.add(backButton, grid);

            // Display
            display();
            textField.requestFocusInWindow();
            ScenarioConstants.scenarioSemaphore.acquire();

            // Checking the input
            if (eq.toString().equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correctIncorrect.setFont(AppConstants.bigFont);
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + eq.toString());
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
        return new Equation(output);
    }
}
