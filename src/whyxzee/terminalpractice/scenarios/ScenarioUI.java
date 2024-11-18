package whyxzee.terminalpractice.scenarios;

import whyxzee.terminalpractice.application.AppConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.util.Random;

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

    protected Random rng = new Random();
    protected MathContext round = new MathContext(4, RoundingMode.HALF_UP);

    // Labels
    protected JLabel questionTracker = new JLabel();
    protected JLabel imageLabel = null;
    protected JLabel[] questions = {};
    protected JLabel[] correctIncorrect = {};
    protected JLabel[] howToLabels = {};

    // Inputs
    protected JTextField textField = new JTextField();
    protected JButton backButton = new JButton("End practice");
    protected JButton continueButton = new JButton("Continue");
    protected JButton[] extraButtonsArray = {};

    // UI
    protected GridBagConstraints grid = new GridBagConstraints();
    protected GridBagConstraints innerGrid = new GridBagConstraints();

    protected JPanel extraButtons = new JPanel();

    public ScenarioUI() throws InterruptedException {
        textField.setEnabled(false);
        // Layout
        this.setLayout(new GridBagLayout());
        extraButtons.setLayout(new GridBagLayout());

        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;
        grid.gridx = 0;
        grid.gridy = 0;

        innerGrid.insets = new Insets(8, 8, 8, 8);
        innerGrid.anchor = GridBagConstraints.CENTER;
        innerGrid.gridx = 0;
        innerGrid.gridy = 0;

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

        printInfo();
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

    /**
     * Displays the scenario, can be overwritten for custom scenarios.
     * 
     * @param questionNum the number of the question, starting at 0.
     * @throws InterruptedException due to semaphore
     */
    public void runProblem(int questionNum) throws InterruptedException {
        // System.out.println(i);
        resetGrid();
        questionTracker(questionNum);

        // Question
        randomize();
        printQuestion();

        printExtraButtons();

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
        response = AppConstants.parseCustom(response);
        textField.setText(response);

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
        // if (action.equals(e.getActionCommand())) {
        // so there are no duplicate inputs

        // } else {
        action = e.getActionCommand();
        // System.out.println("confirmed: " + action);
        if (ScenarioConstants.scenarioSemaphore.hasQueuedThreads()) {
            if (action.equals("end")) {
                shouldBreak = true;
                response = textField.getText();
                ScenarioConstants.scenarioSemaphore.release();
            } else if (action.equals("move on")) {
                ScenarioConstants.scenarioSemaphore.release();
            } else if (action.equals("answer")) {
                action = ""; // so if the answer is correct it won't intefere w/ the next one
                response = textField.getText();
                ScenarioConstants.scenarioSemaphore.release();
            }

            customActions(action);

        }
        // }
    }

    /**
     * Custom actions for scenarios.
     * 
     * @param action action command as a string
     */
    public void customActions(String action) {

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
        extraButtons.removeAll();
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
     * Gets the question image for the problem.
     * 
     * @return {@code null} if no image, a JLabel if there is one.
     */
    public JLabel getQuestionImg() {
        return null;
    }

    /**
     * Gets the latex of the question.
     * 
     * @return {@code null} if there is no latex available.
     */
    public JScrollPane getLatexQuestion() {
        return null;
    }

    /**
     * Gets the latex of the how to labels.
     * 
     * @return {@code null} if there is no latex available.
     */
    public String getLatexHowTo() {
        return "";
    }

    /**
     * Prints the question labels for the scenario.
     */
    public final void printQuestion() {
        getQuestion();

        // Getting the image
        imageLabel = getQuestionImg();
        if (imageLabel != null) {
            this.add(imageLabel, grid);
            grid.gridy++;
        }

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
        textField.setEnabled(false);
    }

    /**
     * Adds extra scenario buttons.
     */
    public void getExtraButtons() {

    }

    /**
     * Prints the extra buttons for the scenario.
     */
    public final void printExtraButtons() {
        getExtraButtons();

        innerGrid.gridx = 0;
        innerGrid.gridy = 0;

        for (JButton button : extraButtonsArray) {
            // if (button != null) {
            button.setActionCommand(button.getText());
            button.addActionListener(this);
            button.setPreferredSize(AppConstants.extraButtonDimension);
            button.setFont(AppConstants.smallFont);

            extraButtons.add(button, innerGrid);
            innerGrid.gridx++;
            // }
        }
        this.add(extraButtons, grid);
        grid.gridy++;
    }

    public final void closingMessage() {
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

        if (imageLabel != null) {
            // imageLabel. // resize it
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
