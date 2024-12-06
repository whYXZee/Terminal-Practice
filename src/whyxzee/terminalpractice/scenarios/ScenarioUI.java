package whyxzee.terminalpractice.scenarios;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.application.Timer;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ScenarioUI extends JPanel implements ActionListener {
    //
    // UI-related Variables
    //
    protected ScenarioDaemon daemon;

    // Labels
    protected JLabel questionTracker = new JLabel();
    protected JLabel[] questions = {};
    protected JLabel[] correctIncorrect = {};
    protected JLabel[] howToLabels = {};
    protected JLabel timerLabel = new JLabel();

    // Inputs
    protected JTextField textField = new JTextField();
    protected JButton backButton = new JButton("End practice");
    protected JButton continueButton = new JButton("Continue");
    protected JButton submitButton = new JButton("Submit Answers");
    protected JButton[] extraButtonsArray = {};

    // Display
    protected GridBagConstraints grid = new GridBagConstraints();
    protected GridBagConstraints innerGrid = new GridBagConstraints();
    protected JPanel extraButtons = new JPanel();

    //
    // General variables
    //
    protected boolean shouldBreak = false;
    protected int numCorrect = 0;
    protected int questionNum = 0;
    protected String response = "";
    protected String action = "";

    protected Random rng = new Random();
    protected Semaphore scenarioSemaphore = new Semaphore(0);

    // Scenario Constants
    protected int maxNum = ScenarioTools.maxNum;
    protected int maxChoices = ScenarioTools.maxChoices;
    protected boolean timerPresent = ScenarioTools.timerEnabled;
    protected int timerMS = ScenarioTools.timerInMS;
    protected int roundTo = ScenarioTools.roundingDigits;

    protected Timer timer = new Timer();

    //
    // Answers
    //
    protected String answer = "";

    // Open-Ended
    protected MathContext round = new MathContext(ScenarioTools.roundingDigits, RoundingMode.HALF_UP);
    protected int openRNG = 0;

    // Multiple-Choice
    protected ArrayList<String> choices = new ArrayList<String>();
    protected ArrayList<String> correctChoices = new ArrayList<String>();
    protected ArrayList<String> chosenChoices = new ArrayList<String>();
    protected ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
    protected int multiRNG = 0;

    //
    // Question
    //
    protected enum QuestionType {
        OPEN,
        MULTI_CHOICE
    }

    protected QuestionType questionType = QuestionType.OPEN;

    public ScenarioUI() {

    }

    /**
     * Displays the scenario, can be overwritten for custom scenarios.
     * 
     * @param questionNum the number of the question, starting at 0.
     * @throws InterruptedException due to semaphore
     */
    public void runProblem(int questionNum) throws InterruptedException {
        resetGrid();

        // Timer
        if (timerPresent) {
            this.add(timerLabel, grid);
            grid.gridy++;
            timerPresent = true;
        }

        // Question
        questionTracker(questionNum);
        switch (rng.nextInt(2)) {
            case 0:
                questionType = QuestionType.OPEN;

                randomizeOpen();
                printQuestion();

                printExtraButtons();

                textField();
                break;
            case 1:
                questionType = QuestionType.MULTI_CHOICE;

                randomizeMulti();
                printQuestion();

                multiChoices();
                break;
        }

        // End Button
        backButton();

        // Display
        display();
        scenarioSemaphore.acquire();
    }

    //
    // Scenario-related methods
    //

    /**
     * Randomizes the scenario, if the question is open-ended.
     */
    public void randomizeOpen() {

    }

    /**
     * Solves the scenario, if the question is open-ended.
     */
    public String solveOpen() {
        return "";
    }

    /**
     * Randomizes the scenario, if the question is multiple-choice.
     */
    public void randomizeMulti() {

    }

    // /**
    // * Solves the scenario, if the question is a multiple-choice question.
    // */
    // public void solveMulti() {

    // }

    /**
     * Checks the answer.
     */
    public final void checkAnswer(String response) throws InterruptedException {
        // Timer
        if (timerPresent) {
            timer.reset();
        }

        // Checking the answer
        switch (questionType) {
            case OPEN:
                if (answer.equals(response)) {
                    correctIncorrect = new JLabel[] { new JLabel("Correct!") };
                    printCorrectIncorrect();
                    numCorrect++;

                    display();
                    textField.setEnabled(false);
                    Thread.sleep(2000);

                } else if (shouldBreak) {

                } else {
                    correctIncorrect = AppConstants.divideLabel("Incorrect, the answer was: " + answer);
                    printCorrectIncorrect();
                    printHowTo();
                    scenarioSemaphore.acquire();
                }
                break;

            case MULTI_CHOICE:
                // Check if the correct answers are selected.
                boolean answerCheck = true;
                for (String i : correctChoices) {
                    if (!chosenChoices.contains(i)) {
                        answerCheck = false;
                    }
                }

                // Check if the number of correct answers are selected.
                if (chosenChoices.size() != correctChoices.size()) {
                    answerCheck = false;
                }

                if (answerCheck) {
                    correctIncorrect = new JLabel[] { new JLabel("Correct!") };
                    printCorrectIncorrect();
                    numCorrect++;

                    display();
                    textField.setEnabled(false);
                    Thread.sleep(2000);

                } else if (shouldBreak) {

                } else {

                    correctIncorrect = AppConstants.divideLabel("Incorrect, the answer(s) are bolded.");
                    for (String i : correctChoices) {
                        for (JCheckBox j : checkBoxes) {
                            if (j.getText().equals(i)) {

                            }
                        }
                    }

                    printCorrectIncorrect();
                    printHowTo();
                    scenarioSemaphore.acquire();
                }

                break;
        }
    }

    //
    // UI-related methods
    //

    @Override
    public final void actionPerformed(ActionEvent e) {
        // System.out.println("detected: " + e.getActionCommand()); // for debugging
        // purposes
        // if (action.equals(e.getActionCommand())) {
        // // so there are no duplicate inputs

        // } else {
        action = e.getActionCommand();
        // System.out.println("confirmed: " + action);
        if (scenarioSemaphore.hasQueuedThreads()) {
            if (action.equals("end")) {
                shouldBreak = true;
                switch (AppConstants.gameEnum) {
                    case OMEGA_SCENARIO:
                        AppConstants.isOmega = false;
                        break;
                    default:
                        break;
                }
                scenarioSemaphore.release();
            } else if (action.equals("move on")) {
                scenarioSemaphore.release();
            } else if (action.equals("answer") || action.equals("submit")) {
                action = ""; // so if the answer is correct it won't intefere w/ the next one
                switch (questionType) {
                    case OPEN:
                        response = textField.getText();
                        break;

                    case MULTI_CHOICE:
                        for (JCheckBox b : checkBoxes) {
                            if (b.isSelected()) {
                                chosenChoices.add(b.getText());
                            }
                        }
                        break;
                }
                scenarioSemaphore.release();
            }

            customActions(action);
        }
        // }
    }

    /**
     * Custom actions for scenarios.
     * 
     * @param action the action command as a string
     */
    public void customActions(String action) {

    }

    /**
     * Runs the scenario as a normal scenario.
     * 
     * @throws InterruptedException
     */
    public final void normalRun() throws InterruptedException {
        // Set up the scenario UI.
        printInfo();
        initDisplay();

        // Question loop
        for (int i = 0; i < AppConstants.goal; i++) {
            runProblem(i);

            // Checking the input
            checkAnswer(response);
            if (shouldBreak) {
                break;
            }
        }
        closingMessage();

        disableListeners();
    }

    /**
     * Runs the scenario as an Omega scenario.
     */
    public final void omega(int currentNum) throws InterruptedException {
        initDisplay();
        runProblem(currentNum);

        checkAnswer(response);
        disableListeners();
    }

    /**
     * Sets up the UI of the scenario.
     */
    public final void initDisplay() {
        // Setting the Grid
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;
        grid.gridx = 0;
        grid.gridy = 0;

        innerGrid.insets = new Insets(8, 8, 8, 8);
        innerGrid.anchor = GridBagConstraints.CENTER;
        innerGrid.gridx = 0;
        innerGrid.gridy = 0;

        textField.setEnabled(false);
        // Layout
        this.setLayout(new GridBagLayout());

        // Resize and loop function
        daemon = new ScenarioDaemon(this);
        daemon.setDaemon(true);
        daemon.start();

        // Timer
        if (ScenarioTools.timerEnabled) {
            timer.start(timerMS * 1000);
        }
        timerLabel.setFont(AppConstants.medFont);

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

        submitButton.setActionCommand("submit");
        submitButton.setPreferredSize(AppConstants.smallButtonDimension);
        submitButton.setFont(AppConstants.medFont);
        submitButton.setToolTipText("Submit your current answers.");
        submitButton.setMnemonic(KeyEvent.VK_S);
        submitButton.addActionListener(this);
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
        grid.gridx = 0;
        grid.gridy = 0;
        this.removeAll();

        // Clearing arraylists
        choices = new ArrayList<String>();
        correctChoices = new ArrayList<String>();
        chosenChoices = new ArrayList<String>();
        checkBoxes = new ArrayList<JCheckBox>();

        // Removing all extra buttons so they don't duplicate
        for (JButton i : extraButtonsArray) {
            extraButtons.remove(i);
        }
    }

    /**
     * Creates the question tracker for the scenario.
     * 
     * @param questionNum the question number
     */
    public final void questionTracker(int questionNum) {
        questionTracker = new JLabel("Question " + (questionNum + 1) + "/" + AppConstants.goal);
        questionTracker.setFont(AppConstants.biggerFont);

        this.add(questionTracker, grid);
        grid.gridy++;
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
            this.add(j, grid);
            grid.gridy++;
        }

    }

    /**
     * Creates the text field for the scenario.
     */
    public final void textField() {
        textField.setText("");
        this.add(textField, grid);
        grid.gridy++;
    }

    /**
     * Creates the answer choices for the scenario.
     */
    public final void multiChoices() {
        // randomizing answer order
        Collections.shuffle(choices);

        // Creating the checkboxes
        for (String i : choices) {
            checkBoxes.add(new JCheckBox(i));
        }

        // Creating the listeners and whatnot
        for (JCheckBox i : checkBoxes) {
            i.addActionListener(this);
            i.setActionCommand(i.getText());
            i.setFont(AppConstants.medFont);

            this.add(i, grid);
            grid.gridy++;
        }

        this.add(submitButton, grid);
        grid.gridy++;

    }

    /**
     * Creates the back button for the scenario.
     */
    public final void backButton() {
        this.add(backButton, grid);
        grid.gridy++;
    }

    /**
     * Prints the correct/incorrect label
     */
    public final void printCorrectIncorrect() {
        for (JLabel label : correctIncorrect) {
            label.setFont(AppConstants.smallFont);
            this.add(label, grid);
            grid.gridy++;
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
        switch (questionType) {
            case MULTI_CHOICE:
                this.remove(submitButton);
                break;
            default:
                break;
        }

        printCorrectIncorrect();

        getHowTo();

        for (JLabel label : howToLabels) {
            label.setFont(AppConstants.medFont);
            this.add(label, grid);
            grid.gridy++;
        }

        // Button
        this.add(continueButton, grid);
        grid.gridy++;

        display();

        // Disabling inputs
        textField.setEnabled(false);
        for (JCheckBox i : checkBoxes) {
            i.setEnabled(false);

            // Also to show the correct answers.
            if (correctChoices.contains(i.getText())) {
                i.setFont(i.getFont().deriveFont(Font.BOLD));
            }
        }
    }

    /**
     * Override this method in a scenario to add extra buttons to the scenario.
     */
    public void getExtraButtons() {

    }

    public void printExtraButtons() {
        getExtraButtons();

        innerGrid.gridx = 0;
        innerGrid.gridy = 0;

        for (JButton i : extraButtonsArray) {
            i.setActionCommand(i.getText());
            i.addActionListener(this);
            i.setPreferredSize(AppConstants.extraButtonDimension);
            i.setFont(AppConstants.smallFont);

            extraButtons.add(i, innerGrid);
            innerGrid.gridx++;
        }

        this.add(extraButtons, grid);
        grid.gridy++;
    }

    public void closingMessage() {
        JOptionPane.showMessageDialog(AppConstants.frame, "You got " + numCorrect + " correct!", "Scenario Completion",
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
    public final void loop() {
        timerLabel.setFont(AppConstants.medFont);
        if (timerPresent) {
            timer.update();
            timerLabel.setText(timer.toString());
            if (timer.finished) {
                timer.reset();
                switch (questionType) {
                    case OPEN:
                        for (ActionListener a : textField.getActionListeners()) {
                            a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "answer"));
                        }
                        break;
                    case MULTI_CHOICE:
                        for (ActionListener a : submitButton.getActionListeners()) {
                            a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "submit"));
                        }
                        break;
                }
            }
        }

        questionTracker.setFont(AppConstants.biggerFont);

        for (JLabel j : questions) {
            j.setFont(AppConstants.medFont);

        }

        textField.setColumns(AppConstants.answerColumns);
        textField.setFont(AppConstants.smallFont);

        // for (JCheckBox i : checkBoxes) {
        // i.setFont(AppConstants.medFont);
        // }

        submitButton.setPreferredSize(AppConstants.smallButtonDimension);
        submitButton.setFont(AppConstants.medFont);

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

    public final void disableListeners() {
        continueButton.removeActionListener(this);
        backButton.removeActionListener(this);
        textField.removeActionListener(this);
        submitButton.removeActionListener(this);

        for (JButton i : extraButtonsArray) {
            i.removeActionListener(this);
        }

        for (JCheckBox i : checkBoxes) {
            i.removeActionListener(this);
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
                    ui.loop();
                    break;
                case OMEGA_SCENARIO:
                    ui.loop();
                    break;

                default:
                    shouldRun = false;
                    break;
            }
        }
    }
}
