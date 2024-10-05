package whyxzee.terminalpractice.flashcards;

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
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import whyxzee.terminalpractice.application.AppConstants;

public class RunDrillsUI extends JPanel implements ActionListener {
    DrillsDaemon daemon;

    // UI constants
    int correct = 0;
    int termsCompleted = 0;
    AnswerSet answers;
    String response;
    Semaphore drillSemaphore = new Semaphore(0);
    boolean shouldBreak = false;

    // UI components
    JLabel questionTracker = new JLabel("");
    JLabel[] questions = {};
    JButton endButton = new JButton("End practice");
    JTextField textField = new JTextField();
    public JLabel correctIncorrect = new JLabel();
    GridBagConstraints grid = new GridBagConstraints();

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
        if (AppConstants.goal > totalAnswers) {
            AppConstants.goal = totalAnswers;
        }

        daemon = new DrillsDaemon(this);
        daemon.setDaemon(true);
        daemon.start();

        //
        // Setting component data
        //
        questionTracker.setFont(AppConstants.biggerFont);

        textField.addActionListener(this);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setColumns(AppConstants.answerColumns);
        textField.setFont(AppConstants.bigFont);

        endButton.addActionListener(this);
        endButton.setActionCommand("end");
        endButton.setPreferredSize(AppConstants.smallButtonDimension);
        endButton.setFont(AppConstants.medFont);
        endButton.setToolTipText("End the drill early.");
        endButton.setMnemonic(KeyEvent.VK_E);

        for (String i : shuffled) {
            this.answers = new AnswerSet(terms.get(i));
            if (answers.answerIsAllowed(bannedLetters, (int) beginCharIndex)) {
                // Question tracker
                questionTracker.setText("Question " + (termsCompleted + 1) + "/" + AppConstants.goal + ":");

                // Questions
                questions = AppConstants.divideLabel(i);

                textField.setText("");

                // Adding the components
                this.add(questionTracker, grid);
                grid.gridy++;
                for (JLabel label : questions) {
                    label.setFont(AppConstants.bigFont);
                    this.add(label, grid);
                    grid.gridy++;
                }
                grid.gridy++;
                this.add(textField, grid);
                grid.gridy++;
                this.add(endButton, grid);
                grid.gridy++;

                display();
                drillSemaphore.acquire();
                if (shouldBreak) {
                    termsCompleted++;
                    break;
                }

                try {
                    if (answers.inSet(response)) {
                        if (answers.sizeAnswers() == 0) {
                            correctIncorrect = new JLabel("Correct!");
                            correctIncorrect.setFont(AppConstants.medFont);
                            this.add(correctIncorrect, grid);
                            display();
                            Thread.sleep(2000);
                        } else {
                            JOptionPane.showMessageDialog(AppConstants.frame,
                                    "Other answers include: \"" + answers + " \".",
                                    "Correct!",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        correct++;
                    } else {
                        JOptionPane.showMessageDialog(AppConstants.frame, "Answers include: \"" + answers + "\".",
                                "Incorrect",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    termsCompleted++;

                    if (termsCompleted == AppConstants.goal) {
                        break;
                    }
                } catch (InterruptedException f) {

                }
                this.removeAll();
            }
        }

        if (termsCompleted == 0) {
            JOptionPane.showMessageDialog(AppConstants.frame, "No available terms, due to restriction issues.",
                    "Drill Completion",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(AppConstants.frame, "You got " + correct + " correct!", "Drill Completion",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        AppConstants.semaphore.release();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
            drillSemaphore.release();
        } else if (drillSemaphore.hasQueuedThreads()) {
            try {
                this.response = textField.getText();
            } catch (NullPointerException error) {
                this.response = "";
            }
            drillSemaphore.release();
        }
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
        textField.requestFocusInWindow();
    }

    public void resize() {
        questionTracker.setFont(AppConstants.biggerFont);
        for (JLabel label : questions) {
            label.setFont(AppConstants.bigFont);
        }

        textField.setColumns(AppConstants.answerColumns);
        textField.setFont(AppConstants.bigFont);

        endButton.setPreferredSize(AppConstants.smallButtonDimension);
        endButton.setFont(AppConstants.medFont);
    }
}

class DrillsDaemon extends Thread {
    private RunDrillsUI ui;

    public DrillsDaemon(RunDrillsUI ui) {
        super("DrillsFrameDaemon");
        this.ui = ui;
    }

    public void run() {
        boolean shouldRun = true;
        while (shouldRun) {
            switch (AppConstants.gameEnum) {
                case DRILLS:
                    ui.resize();
                    break;
                case CUSTOM_DRILLS:
                    ui.resize();
                    break;
                default:
                    shouldRun = false;
                    break;
            }
        }
    }
}
