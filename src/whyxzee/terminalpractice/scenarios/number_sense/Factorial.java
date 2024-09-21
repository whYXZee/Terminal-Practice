package whyxzee.terminalpractice.scenarios.number_sense;

import whyxzee.terminalpractice.application.RunApplication;
import whyxzee.terminalpractice.resources.DiscreteMath;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

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

public class Factorial extends ScenarioUI implements ActionListener {
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    Timer timer = new Timer(4500, this);
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    // Scenario-specific variables
    private static int number = 0;

    public Factorial() throws InterruptedException {
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
            randomize();
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + RunApplication.goal);
            JLabel question = new JLabel("What is the factorial of " + number + "?");
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

            // timer = ;
            // timer.restart();
            // timer.setActionCommand("timer");
            // timer.start();

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

            if (solve().equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
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
        // timer.stop();
        ScenarioConstants.scenarioSemaphore.release();
    }

    private void randomize() {
        number = ScenarioConstants.rng.nextInt(11);
    }

    private String solve() {
        return DiscreteMath.factorial(number).toString();
    }
}
