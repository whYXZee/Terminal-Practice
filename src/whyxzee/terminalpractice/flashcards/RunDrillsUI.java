package whyxzee.terminalpractice.flashcards;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import whyxzee.terminalpractice.application.RunApplication;

public class RunDrillsUI extends JPanel implements ActionListener {
    // UI constants
    int correct = 0;
    int termsCompleted = 0;
    AnswerSet answers;
    String response;

    Semaphore drillSemaphore = new Semaphore(0);
    JLabel questionTracker;
    JLabel[] questions;
    JTextField textField = new JTextField();
    public JLabel correctIncorrect = new JLabel();
    GridBagConstraints grid = new GridBagConstraints();
    boolean shouldBreak = false;
    boolean buffer = false;

    public RunDrillsUI(HashMap<String, String> terms, ArrayList<String> bannedLetters, long beginCharIndex)
            throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        // Shuffling terms & removing banned letters
        ArrayList<String> shuffled = new ArrayList<String>() {
            {
                addAll(terms.keySet());
            }
        };
        Collections.shuffle(shuffled);
        int totalAnswers = AnswerSet.totalAnswers(terms, bannedLetters, (int) beginCharIndex);
        if (RunApplication.goal > totalAnswers) {
            RunApplication.goal = totalAnswers;
        }
        for (String i : shuffled) {
            RunApplication.getFontSize();
            this.answers = new AnswerSet(terms.get(i));
            if (answers.answerIsAllowed(bannedLetters, (int) beginCharIndex)) {
                questionTracker = new JLabel("Question " + (termsCompleted + 1) + "/" + RunApplication.goal + ":");
                questions = RunApplication.divideLabel(i);

                // Changing size of the labels
                questionTracker.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize));

                textField = new JTextField();
                textField.addActionListener(this);
                textField.setColumns(RunApplication.getColumns());
                textField.setHorizontalAlignment(JTextField.CENTER);
                textField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));

                JButton backButton = new JButton("End practice");
                backButton.addActionListener(this);
                backButton.setActionCommand("end");
                backButton.setPreferredSize(new Dimension(150, 25));
                backButton.setToolTipText("End the drill early.");
                backButton.setMnemonic(KeyEvent.VK_E);

                // Adding the components
                this.add(questionTracker, grid);
                grid.gridy++;
                for (JLabel label : questions) {
                    label.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));
                    this.add(label, grid);
                    grid.gridy++;
                }
                grid.gridy++;
                this.add(textField, grid);
                grid.gridy++;
                this.add(backButton, grid);
                grid.gridy++;

                display();
                buffer = false;
                drillSemaphore.acquire();
                buffer = true;
                if (shouldBreak) {
                    break;
                }

                try {
                    if (answers.inSet(response)) {
                        if (answers.sizeAnswers() == 0) {
                            correctIncorrect = new JLabel("Correct!");
                        } else {
                            correctIncorrect = new JLabel("Correct! Other answers include: \"" + answers + "\".");
                        }
                        correct++;
                    } else {
                        correctIncorrect = new JLabel("Incorrect, answers include: \"" + answers + "\".");
                    }
                    termsCompleted++;
                    correctIncorrect.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
                    this.add(correctIncorrect, grid);
                    display();
                    Thread.sleep(2000);
                    if (termsCompleted == RunApplication.goal) {
                        this.remove(correctIncorrect);
                        break;
                    }
                } catch (InterruptedException f) {

                }
                this.removeAll();
            }
        }
        correctIncorrect = new JLabel("Congratulations, you got " + correct + " correct!");
        this.add(correctIncorrect, grid);
        display();
        Thread.sleep(3000);
        if (termsCompleted == 0 || shouldBreak || termsCompleted != shuffled.size()
                || termsCompleted == RunApplication.goal) {
            // to release the semaphore in case all words were restricted, but not when its
            // already released.
            RunApplication.semaphore.release();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
            drillSemaphore.release();
        } else if (!buffer) {
            try {
                this.response = textField.getText();
            } catch (NullPointerException error) {
                this.response = "";
            }
            drillSemaphore.release();
        }
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
        textField.requestFocusInWindow();
    }
}
