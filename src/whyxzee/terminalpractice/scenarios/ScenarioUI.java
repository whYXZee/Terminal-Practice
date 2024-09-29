package whyxzee.terminalpractice.scenarios;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import whyxzee.terminalpractice.application.AppConstants;

public class ScenarioUI extends JPanel implements ActionListener {
    protected ScenarioDaemon daemon;

    //
    // General variables
    //
    protected boolean shouldBreak = false;
    protected int correct = 0;
    protected int question = 0;
    protected String response = "";
    protected String action = "";

    protected JLabel questionTracker = new JLabel();
    protected JLabel[] questions = {};
    protected JTextField textField = new JTextField();
    protected JButton backButton = new JButton("End practice");
    // protected JLabel correctIncorrect = new JLabel();
    protected JLabel[] correctIncorrect = {};
    protected JLabel[] howToLabels = {};
    protected JButton continueButton = new JButton("Continue");

    public ScenarioUI() throws InterruptedException {
        textField.setEnabled(false);
        // Layout
        this.setLayout(new GridBagLayout());
        printInfo();

        daemon = new ScenarioDaemon(this);
        daemon.setDaemon(true);
        daemon.start();

        // Setting data
        textField.addActionListener(this);
        textField.setActionCommand("answer");
        textField.setColumns(AppConstants.answerColumns);
        textField.setFont(AppConstants.smallFont);
        textField.setHorizontalAlignment(JTextField.CENTER);

        backButton.addActionListener(this);
        backButton.setActionCommand("end");
        backButton.setMnemonic(KeyEvent.VK_E);
        backButton.setPreferredSize(AppConstants.smallButtonDimension);
        backButton.setFont(AppConstants.medFont);
        backButton.setToolTipText("End the scenario early.");

        continueButton.setActionCommand("move on");
        continueButton.setPreferredSize(AppConstants.smallButtonDimension);
        continueButton.setFont(AppConstants.medFont);
        continueButton.setToolTipText("Continue to the next problem.");
        continueButton.setMnemonic(KeyEvent.VK_C);
        continueButton.addActionListener(this);

        for (int i = 0; i < AppConstants.goal; i++) {
            runProblem(i);

            // Checking the input
            checkAnswer(response);
            if (shouldBreak) {
                break;
            }
        }
        closingMessage();
    }

    public void runProblem(int questionNum) throws InterruptedException {
        // System.out.println(i);
        resetGrid();
        questionTracker(questionNum);

        // Question
        randomize();
        printQuestion();

        // Answer box
        textField();

        // End button
        backButton();

        // Display
        display();
        ScenarioConstants.scenarioSemaphore.acquire();
    }

    //
    // Scenario-related methods
    //

    /**
     * Randomizes the scenario.
     */
    public void randomize() {

    }

    /**
     * Solves the scenario.
     */
    public String solve() {
        return "";
    }

    /**
     * Checks the answer.
     */
    public final void checkAnswer(String response) throws InterruptedException {
        if (solve().equals(response)) {
            correctIncorrect = new JLabel[] { new JLabel("Correct!") };
            printCorrectIncorrect();
            correct++;

            display();
            textField.setEnabled(false);
            Thread.sleep(2000);
        } else if (shouldBreak) {

        } else {
            correctIncorrect = AppConstants.divideLabel("Incorrect, the answer was: " + solve());
            printCorrectIncorrect();
            printHowTo();
            ScenarioConstants.scenarioSemaphore.acquire();
        }
    }

    //
    // UI-related methods
    //

    @Override
    public final void actionPerformed(ActionEvent e) {
        // System.out.println("detected: " + e.getActionCommand()); // for debugging
        // purposes
        if (action.equals(e.getActionCommand())) {
            // so there are no duplicate inputs

        } else {
            action = e.getActionCommand();
            // System.out.println("confirmed: " + action);
            if (ScenarioConstants.scenarioSemaphore.hasQueuedThreads()) {
                if (action.equals("end")) {
                    shouldBreak = true;
                } else if (action.equals("move on")) {

                } else if (action.equals("answer")) {
                    action = ""; // so if the answer is correct it won't intefere w/ the next one
                    response = textField.getText();
                }
                ScenarioConstants.scenarioSemaphore.release();
            }
        }
    }

    /**
     * Displays the scenario.
     */
    public final void display() {
        AppConstants.frame.setContentPane(new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        AppConstants.frame.setVisible(true);

        textField.setEnabled(true);
        textField.requestFocusInWindow();
    }

    /**
     * Resets the question
     */
    public final void resetGrid() {
        ScenarioConstants.grid.gridx = 0;
        ScenarioConstants.grid.gridy = 0;
        this.removeAll();
    }

    /**
     * Creates the question tracker for the scenario.
     * 
     * @param questionNum the question number
     */
    public final void questionTracker(int questionNum) {
        questionTracker = new JLabel("Question " + (questionNum + 1) + "/" + AppConstants.goal);
        questionTracker.setFont(AppConstants.biggerFont);

        this.add(questionTracker, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;
    }

    /**
     * Gets the question labels for the scenario.
     */
    public void getQuestion() {

    }

    /**
     * Prints the question labels for the scenario.
     */
    public final void printQuestion() {
        getQuestion();

        for (JLabel j : questions) {
            j.setFont(AppConstants.medFont);
            this.add(j, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }

    }

    /**
     * Creates the text field for the scenario.
     */
    public final void textField() {
        textField.setText("");
        this.add(textField, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;
    }

    /**
     * Creates the back button for the scenario.
     */
    public final void backButton() {
        this.add(backButton, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;
    }

    /**
     * Prints the correct/incorrect label
     */
    public final void printCorrectIncorrect() {
        for (JLabel label : correctIncorrect) {
            label.setFont(AppConstants.smallFont);
            this.add(label, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }
    }

    /**
     * Gets the "how to" labels
     */
    public void getHowTo() {

    }

    /**
     * Prints how to solve the scenario.
     */
    public final void printHowTo() {
        this.remove(backButton);

        printCorrectIncorrect();

        getHowTo();

        for (JLabel label : howToLabels) {
            label.setFont(AppConstants.medFont);
            this.add(label, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }

        // Button
        this.add(continueButton, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;

        display();
        textField.setEnabled(false);
    }

    public void closingMessage() {
        JOptionPane.showMessageDialog(AppConstants.frame, "You got " + correct + " correct!", "Scenario Completion",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Prints instructions for the scenario.
     */
    public void printInfo() {
    }

    /**
     * Resizes the components for the scenario.
     */
    public final void resize() {
        questionTracker.setFont(AppConstants.biggerFont);

        for (JLabel j : questions) {
            j.setFont(AppConstants.medFont);

        }

        textField.setColumns(AppConstants.answerColumns);
        textField.setFont(AppConstants.smallFont);

        backButton.setPreferredSize(AppConstants.smallButtonDimension);
        backButton.setFont(AppConstants.medFont);

        for (JLabel label : howToLabels) {
            label.setFont(AppConstants.medFont);
        }

        continueButton.setPreferredSize(AppConstants.smallButtonDimension);
        continueButton.setFont(AppConstants.medFont);

        for (JLabel label : correctIncorrect) {
            label.setFont(AppConstants.smallFont);
        }
    }
}

/**
 * Daemon thread for resizing scenarios.
 */
class ScenarioDaemon extends Thread {
    ScenarioUI ui;

    public ScenarioDaemon(ScenarioUI ui) {
        super("ScenarioDaemon");
        this.ui = ui;
    }

    public void run() {
        boolean shouldRun = true;
        while (shouldRun) {
            switch (AppConstants.gameEnum) {
                case SCENARIOS:
                    ui.resize();
                    break;
                default:
                    shouldRun = false;
                    break;
            }
        }
    }
}
