package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.awt.Font;
import javax.swing.JButton;

import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;

import application.RunApplication;
import resources.AnswerSet;

public class RunDrillsUI extends JPanel implements ActionListener {
    int correct = 0;
    int termsCompleted = 0;
    AnswerSet answers;
    String response;
    Semaphore semaphore = new Semaphore(0);
    JLabel questionTracker;
    JLabel question;
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();
    GridBagConstraints grid = new GridBagConstraints();
    boolean shouldBreak = false;

    public RunDrillsUI(HashMap<String, String> terms, ArrayList<String> bannedLetters) throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        // Shuffling terms
        ArrayList<String> shuffled = new ArrayList<String>(terms.keySet());
        Collections.shuffle(shuffled);
        for (String i : shuffled) {
            RunApplication.getFontSize();
            this.answers = new AnswerSet(terms.get(i));
            if (Character.toString(i.charAt(0)).equals("-") || !bannedLetters
                    .contains(Character.toString(i.charAt(0)))) {
                if (!bannedLetters.contains(Character.toString(i.charAt(1)))) {
                    questionTracker = new JLabel("Question " + (termsCompleted + 1) + "/" + RunApplication.goal + ":");
                    question = new JLabel(i);

                    // Changing size of the labels
                    questionTracker.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize));
                    question.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));

                    textField = new JTextField();
                    textField.setColumns(RunApplication.getColumns());
                    textField.setHorizontalAlignment(JTextField.CENTER);
                    textField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));
                    textField.addActionListener(this);

                    JButton backButton = new JButton("End practice");
                    backButton.setActionCommand("end");
                    backButton.setHorizontalTextPosition(AbstractButton.CENTER);
                    backButton.setVerticalTextPosition(AbstractButton.CENTER);
                    backButton.setPreferredSize(new Dimension(150, 25));
                    backButton.setToolTipText("End the drill early.");
                    backButton.setMnemonic(KeyEvent.VK_E);
                    backButton.addActionListener(this);

                    // Adding the components
                    this.add(questionTracker, grid);
                    grid.gridy++;
                    this.add(question, grid);
                    grid.gridy++;
                    this.add(textField, grid);
                    grid.gridy++;
                    this.add(backButton, grid);
                    grid.gridy++;

                    display();
                    semaphore.acquire();
                    if (shouldBreak) {
                        System.out.println("test 1");
                        RunApplication.semaphore.release();
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
                            RunApplication.semaphore.release();
                            correctIncorrect = new JLabel("Congratulations, you got " + correct + " correct!");
                            break;
                        }
                    } catch (InterruptedException f) {

                    }
                    this.removeAll();
                }
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("end")) {
            System.out.println("ended");
            shouldBreak = true;
        } else {
            this.response = textField.getText();
        }
        semaphore.release();
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
