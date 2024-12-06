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

import whyxzee.terminalpractice.application.Timer;

import whyxzee.terminalpractice.application.AppConstants;

/**
 * Bland drills have no goal amount, and have a timer.
 */
public class RunBlandDrillsUI extends JPanel implements ActionListener {
    BlandDrillsDaemon daemon;

    // UI constants
    int correct = 0;
    int termsCompleted = 0;
    AnswerSet answers;
    String response;
    Semaphore drillSemaphore = new Semaphore(0);
    boolean shouldBreak = false;
    boolean timerPresent = false;
    int timerSec = 0;

    // UI components
    JLabel questionTracker = new JLabel("");
    JLabel timerLabel = new JLabel("");
    JLabel[] questions = {};
    JButton endButton = new JButton("End practice");
    JTextField textField = new JTextField();
    public JLabel correctIncorrect = new JLabel();
    GridBagConstraints grid = new GridBagConstraints();

    // Technical components
    Timer timer = new Timer();

    public RunBlandDrillsUI(HashMap<String, String> terms)
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

        AppConstants.goal = shuffled.size();

        daemon = new BlandDrillsDaemon(this);
        daemon.setDaemon(true);
        daemon.start();

        // Timer
        boolean repeatTimerQuestion = true;
        while (repeatTimerQuestion) {
            String timerString = JOptionPane.showInputDialog(
                    "How many seconds do you want on each question?\n(leave blank or X out if you don't want a timer)",
                    0);
            if (timerString == null || timerString.equals("0")) {
                repeatTimerQuestion = false;
                timerPresent = false;
            } else {
                try {
                    timerSec = Integer.valueOf(timerString);
                    timerPresent = true;
                    repeatTimerQuestion = false;

                    // to avoid going over an hour
                    if (timerSec > 3600) {
                        timerSec = 3600;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(AppConstants.frame, "Invalid input, please put in a number.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        //
        // Setting component data
        //
        timerLabel.setFont(AppConstants.medFont);
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

        //
        // Running each question.
        //
        for (String i : shuffled) {
            this.answers = new AnswerSet(terms.get(i));
            // Timer
            if (timerPresent) {
                timer.start(timerSec * 1000);
                this.add(timerLabel, grid);
                grid.gridy++;
            }

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
            timer.reset();

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

                // if (termsCompleted == AppConstants.goal) {
                // break;
                // }
            } catch (InterruptedException f) {

            }
            this.removeAll();
        }

        JOptionPane.showMessageDialog(AppConstants.frame, "You got " + correct + " correct!", "Drill Completion",
                JOptionPane.INFORMATION_MESSAGE);

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

    public void loop() {
        timerLabel.setFont(AppConstants.medFont);
        if (timerPresent) {
            timer.update();
            timerLabel.setText(timer.toString());
            if (timer.finished) {
                timer.reset();
                for (ActionListener a : textField.getActionListeners()) {
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "null"));
                }
            }
        }

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

class BlandDrillsDaemon extends Thread {
    private RunBlandDrillsUI ui;

    public BlandDrillsDaemon(RunBlandDrillsUI ui) {
        super("Bland Drills Daemon");
        this.ui = ui;
    }

    public void run() {
        boolean shouldRun = true;
        while (shouldRun) {
            switch (AppConstants.gameEnum) {
                case OMEGA_DRILL:
                    ui.loop();
                    break;
                default:
                    shouldRun = false;
                    break;
            }
        }
    }
}
