package ui.scenarios.algebra;

import ui.scenarios.ScenarioUI;
import application.RunApplication;
import application.Scenario;
import resources.Equation;
import resources.MathFunctions;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SimplifyEquation extends ScenarioUI implements ActionListener {
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    Timer timer = new Timer(4500, this);
    Semaphore internalSemaphore = new Semaphore(0);
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    public SimplifyEquation() throws InterruptedException {
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
            JLabel question = new JLabel("Simplify: " + eq);
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
            timer.restart();
            timer.setActionCommand("timer");
            timer.start();

            JButton backButton = new JButton("End practice");
            backButton.setActionCommand("end");
            // backButton.setHorizontalTextPosition(AbstractButton.CENTER);
            // backButton.setVerticalTextPosition(AbstractButton.CENTER);
            backButton.setPreferredSize(new Dimension(150, 25));
            backButton.setToolTipText("End the drill early.");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.addActionListener(this);
            this.add(backButton, grid);
            grid.gridy++;

            display();
            textField.requestFocusInWindow();

            internalSemaphore.acquire();

            if (solve(eq).equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + solve(eq));
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
        if (e.getActionCommand().equals("timer")) {
            this.response = textField.getText();
        } else if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
        } else {
            this.response = textField.getText();
        }
        timer.stop();
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
            System.out.println(term);
            output.add(term);
        }
        return new Equation(output);
    }

    private String solve(Equation eq) {
        String output = MathFunctions.addition(eq.termArray[0] + "+" + eq.termArray[1]);
        // for (String i : eq.termArray) {
        // System.out.println(i);
        // }
        if (eq.termArray.length == 3) {
            // System.out.println("yip " + output);
            output = MathFunctions.addition(output + "+" + eq.termArray[2]);
            // System.out.println("yip2 " + output);
        }
        return output;
    }
}
