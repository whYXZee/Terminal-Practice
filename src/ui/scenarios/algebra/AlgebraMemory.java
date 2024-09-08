package ui.scenarios.algebra;

import ui.scenarios.ScenarioUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import application.RunApplication;
import application.Scenario;
import resources.Equation;

import java.awt.event.KeyEvent;
import java.awt.Dimension;

public class AlgebraMemory extends ScenarioUI implements ActionListener {
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    // public String answer = "";
    Semaphore internalSemaphore = new Semaphore(0);
    public Semaphore externalSemaphore = new Semaphore(0);
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

        for (int i = 0; i < RunApplication.goal; i++) {
            // Showing the equation
            Equation eq = randomize();
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + RunApplication.goal);
            JLabel question = new JLabel("Remember: " + eq);
            this.add(questionTracker, grid);
            grid.gridy++;
            this.add(question, grid);
            display();
            Thread.sleep(1250);

            // Ask question
            this.remove(question);
            textField = new JTextField();
            textField.setColumns(10);
            textField.addActionListener(this);
            this.add(textField, grid);
            grid.gridy++;

            JButton backButton = new JButton("End practice");
            backButton.setActionCommand("end");
            backButton.setHorizontalTextPosition(AbstractButton.CENTER);
            backButton.setVerticalTextPosition(AbstractButton.CENTER);
            backButton.setPreferredSize(new Dimension(150, 25));
            backButton.setToolTipText("End the drill early.");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.addActionListener(this);

            this.add(Box.createVerticalGlue(), grid);
            display();

            internalSemaphore.acquire();

            if (eq.toString().equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + eq.toString());
            }
            this.remove(Box.createVerticalGlue());
            this.add(correctIncorrect, grid);
            this.add(Box.createVerticalGlue());
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
        if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
        } else {
            this.response = textField.getText();
        }
        internalSemaphore.release();
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
            term = term + Integer.toString(Scenario.rng.nextInt(25) + 1);
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
