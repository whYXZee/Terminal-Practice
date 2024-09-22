package whyxzee.terminalpractice.scenarios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;

import whyxzee.terminalpractice.application.AppConstants;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;

import java.util.concurrent.Semaphore;

public class RunScenarioUI extends JPanel implements ActionListener {
    public int correct;
    public String response = "";
    // public String answer = "";
    Semaphore internalSemaphore = new Semaphore(0);
    public Semaphore externalSemaphore = new Semaphore(0);
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    public RunScenarioUI(JLabel questionTracker, JLabel question, String answer) throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.WEST;
        this.add(questionTracker, grid);
        grid.gridy++;
        this.add(question, grid);
        grid.gridy++;

        textField = new JTextField();
        textField.setColumns(10);
        textField.addActionListener(this);
        this.add(textField, grid);
        grid.gridy++;
        this.add(Box.createVerticalGlue(), grid);
        display();

        internalSemaphore.acquire();

        if (answer.equals(response)) {
            correctIncorrect = new JLabel("Correct!");
            correct++;
        } else {
            correctIncorrect = new JLabel("Incorrect, the answer was: " + answer);
        }
        this.remove(Box.createVerticalGlue());
        this.add(correctIncorrect, grid);
        this.add(Box.createVerticalGlue());
        display();
        Thread.sleep(2000);
        this.removeAll();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.response = textField.getText();
        internalSemaphore.release();
        externalSemaphore.release();
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
    }
}
