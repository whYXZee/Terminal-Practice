package whyxzee.terminalpractice.scenarios.geometry;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Trigonometry;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.util.ArrayList;

public class RightTrig extends ScenarioUI implements ActionListener {
    //
    // General variables
    //
    boolean shouldBreak = false;
    int correct = 0;
    public String response = "";
    static JLabel[] questions;
    JTextField textField;
    JLabel correctIncorrect = new JLabel();

    //
    // Scenario-specific variables
    //
    private static Trigonometry triangle = new Trigonometry(new Float[] { null, null, null, null, null, null });
    private static ArrayList<Float> nullList = new ArrayList<Float>() {
        {
            add(null);
            add(null);
            add(null);
            add(null);
            add(null);
            add(null);
        }
    };

    // enums to determine problem type
    private static ProblemType problemType = ProblemType.HYPOTENUSE;
    private static GivenVal givenVal = GivenVal.SIDES;

    // What part of the motion needs to be found?
    private enum ProblemType {
        HYPOTENUSE,
        X_SIDE,
        Y_SIDE,
        THETA_X,
        THETA_Y
    }

    // What values from the motion are given?
    private enum GivenVal {
        SIDES, // two sides from initial triangle
        THETA_Y, // angle of initial triangle + random side
        THETA_X
    }

    public RightTrig() throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        correctIncorrect.setFont(AppConstants.biggerFont);

        for (int i = 0; i < AppConstants.goal; i++) {
            // Question tracker
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + AppConstants.goal);
            questionTracker.setFont(AppConstants.biggerFont);
            this.add(questionTracker, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;

            // Question
            randomize();
            JLabel triangleLabel = new JLabel(triangle.toString());
            triangleLabel.setFont(AppConstants.medFont);
            this.add(triangleLabel, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;

            printQuestion();

            // Answer box
            textField = new JTextField();
            textField.addActionListener(this);
            textField.setColumns(AppConstants.answerColumns);
            textField.setFont(AppConstants.smallFont);
            textField.setHorizontalAlignment(JTextField.CENTER);
            this.add(textField, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;

            // End button
            JButton backButton = new JButton("End practice");
            backButton.addActionListener(this);
            backButton.setActionCommand("end");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setFont(AppConstants.medFont);
            backButton.setToolTipText("End the drill early.");
            this.add(backButton, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;

            // Display
            display();
            textField.requestFocusInWindow();
            ScenarioConstants.scenarioSemaphore.acquire();

            // Checking the input
            try {
                if (solve().toString().equals(response)) {
                    correctIncorrect = new JLabel("Correct!");
                    correctIncorrect.setFont(AppConstants.bigFont);
                    this.add(correctIncorrect, ScenarioConstants.grid);
                    correct++;

                    display();
                    Thread.sleep(2000);
                } else if (shouldBreak) {
                    break;
                } else {
                    correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
                    correctIncorrect.setFont(AppConstants.bigFont);
                    printHowTo();
                    ScenarioConstants.scenarioSemaphore.acquire();
                }
            } catch (NumberFormatException error) {
                if (shouldBreak) {
                    break;
                } else {
                    correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
                    correctIncorrect.setFont(AppConstants.bigFont);
                    printHowTo();
                    ScenarioConstants.scenarioSemaphore.acquire();
                }
            }
            this.removeAll();
        }
        JOptionPane.showMessageDialog(AppConstants.frame, "You got " + correct + " correct!", "Scenario Completion",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
        } else if (e.getActionCommand().equals("move on")) {

        } else {
            this.response = textField.getText();
        }
        ScenarioConstants.scenarioSemaphore.release();
    }

    /**
     * Randomizes the right triangle trigonometry problem.
     */
    private static void randomize() {
        // Randomiozes what is given
        switch (ScenarioConstants.rng.nextInt(3)) {
            case 0:
                givenVal = GivenVal.THETA_X;
                break;
            case 1:
                givenVal = GivenVal.THETA_Y;
                break;
            case 2:
                givenVal = GivenVal.SIDES;
                break;
        }

        // Randomizes the values based on the problem
        int randomSide;
        do {
            // Resetting the equation
            triangle.updateFromList(nullList);

            switch (givenVal) {
                case THETA_Y:
                    triangle.thetaY = (float) (ScenarioConstants.rng.nextInt(80) + 1);
                    rngSides(ScenarioConstants.rng.nextInt(3) + 1);
                    break;
                case THETA_X:
                    triangle.thetaX = (float) (ScenarioConstants.rng.nextInt(80) + 1);
                    rngSides(ScenarioConstants.rng.nextInt(3) + 1);
                    break;
                case SIDES:
                    rngSides(ScenarioConstants.rng.nextInt(3) + 1);
                    randomSide = ScenarioConstants.rng.nextInt(3) + 1;
                    while (triangle.sidePresent(randomSide)) {
                        randomSide = ScenarioConstants.rng.nextInt(3) + 1;
                    }
                    rngSides(randomSide);
                    triangle.rearrange();
                    break;
            }
            triangle.updateLists();
        } while (!triangle.isLegal90Deg());

        // What the problem wants you to find
        do {
            switch (ScenarioConstants.rng.nextInt(5)) {
                case 0:
                    problemType = ProblemType.THETA_X;
                    break;
                case 1:
                    problemType = ProblemType.THETA_Y;
                    break;
                case 2:
                    problemType = ProblemType.HYPOTENUSE;
                    break;
                case 3:
                    problemType = ProblemType.X_SIDE;
                    break;
                case 4:
                    problemType = ProblemType.Y_SIDE;
                    break;
            }
        } while (questionOverlap());
    }

    /**
     * Solves the right triangle trigonometry problem.
     */
    private static BigDecimal solve() {
        triangle.solve90DegTrig();
        switch (problemType) {
            case HYPOTENUSE:
                return new BigDecimal(triangle.hypotenuse, Trigonometry.trigRound);
            case X_SIDE:
                return new BigDecimal(triangle.x, Trigonometry.trigRound);
            case Y_SIDE:
                return new BigDecimal(triangle.y, Trigonometry.trigRound);
            case THETA_X:
                return new BigDecimal(triangle.thetaX, Trigonometry.trigRound);
            case THETA_Y:
                return new BigDecimal(triangle.thetaY, Trigonometry.trigRound);
            default:
                return new BigDecimal(0);
        }

    }

    /**
     * Prints the right triangle trigonometric question.
     */
    private void printQuestion() {
        switch (problemType) {
            case HYPOTENUSE:
                questions = AppConstants.divideLabel("What is the hypotenuse of the triangle, given the values?");
                break;
            case X_SIDE:
                questions = AppConstants.divideLabel("What is the base of the triangle, given the values?");
                break;
            case Y_SIDE:
                questions = AppConstants.divideLabel("What is the height of the triangle, given the values?");
                break;
            case THETA_X:
                questions = AppConstants.divideLabel("What is the measure of the base angle? [in degrees]");
                break;
            case THETA_Y:
                questions = AppConstants.divideLabel("What is the measure of the height angle? [in degrees]");
        }
        for (JLabel j : questions) {
            j.setFont(AppConstants.medFont);
            this.add(j, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }
    }

    /**
     * Prints how to solve the right triangle trinonometry problem.
     */
    private void printHowTo() {
        // Resetting the frame:
        this.removeAll();
        ScenarioConstants.grid.gridx = 0;
        ScenarioConstants.grid.gridy = 0;

        // Adding everything
        JLabel[] howToLabels = {};
        this.add(correctIncorrect, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;

        for (JLabel label : howToLabels) {
            label.setFont(AppConstants.medFont);
            this.add(label, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }

        // Button
        JButton continueButton = new JButton("Continue");
        continueButton.setActionCommand("move on");
        continueButton.setPreferredSize(new Dimension(150, 25));
        continueButton.setToolTipText("Continue to the next problem.");
        continueButton.setMnemonic(KeyEvent.VK_C);
        continueButton.addActionListener(this);
        this.add(continueButton, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;

        display();
    }

    /**
     * Checks if the question overlaps with what is being asked.
     * 
     * @return
     */
    private static boolean questionOverlap() {
        switch (givenVal) {
            case SIDES:
                switch (problemType) {
                    case HYPOTENUSE:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case X_SIDE:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case Y_SIDE:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case THETA_X:
                switch (problemType) {
                    case HYPOTENUSE:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case X_SIDE:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case Y_SIDE:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    case THETA_X:
                        return true;
                    default:
                        break;
                }
                break;
            case THETA_Y:
                switch (problemType) {
                    case HYPOTENUSE:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case X_SIDE:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case Y_SIDE:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    case THETA_Y:
                        return true;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Randomizes the sides
     * 
     * @param side number of the side, with the following key:
     *             <ul>
     *             <li>1: side X
     *             <li>2: side Y
     *             <li>3: Hypotenuse
     */
    private static void rngSides(int side) {
        if (side == 1) {
            triangle.x = (float) (ScenarioConstants.rng.nextInt(50) + 1);
        } else if (side == 2) {
            triangle.y = (float) (ScenarioConstants.rng.nextInt(50) + 1);
        } else {
            triangle.hypotenuse = (float) (ScenarioConstants.rng.nextInt(50) + 1);
        }
    }
}