package ui.scenarios.physics;

import application.RunApplication;
import application.Scenario;
import ui.scenarios.ScenarioUI;

import resources.Trigonometry;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.math.BigDecimal;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ProjectileMotion extends ScenarioUI implements ActionListener {
    // enums to determine problem type
    private static ProblemType problemType = ProblemType.NONE;
    private static GivenVal givenVal = GivenVal.NONE;

    /**
     * What part of the motion needs to be found?
     */
    private enum ProblemType {
        NONE,
        INIT_VH, // initial velocity of hypotenuse
        INIT_VX, // initial velocity of X position
        INIT_VY, // initial velocity of Y position
        INIT_THETA, // initial angle of the force
        F_VH, // final velocity of hypotenuse
        F_VX, // final velocity of X position
        F_VY, // final velocity of Y position
        F_THETA, // final angle of the force
        MAX_HEIGHT, // max height of the projectile
        TIME_MAX_HEIGHT, // time of the max height
        TIME_TOTAL_X, // time of the total x displacement
        TOTAL_X, // total x displacement
    }

    /**
     * What values from the motion are given?
     */
    private enum GivenVal {
        NONE,
        INIT_SIDES, // two sides from initial triangle
        INIT_THETA, // angle of initial triangle + random side
        F_SIDES, // two sides from final triangle
        F_THETA // angle of final triangle + random side
    }

    // To track between problems
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

    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    Semaphore internalSemaphore = new Semaphore(0);
    public Semaphore externalSemaphore = new Semaphore(0);
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();
    static JLabel lookingFor = new JLabel();

    public ProjectileMotion() throws InterruptedException {
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
            randomize();
            printQuestion();
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + RunApplication.goal);
            JLabel question = new JLabel(triangle.toString());

            questionTracker.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));
            question.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 3));
            lookingFor.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 3));

            this.add(questionTracker, grid);
            grid.gridy++;
            this.add(question, grid);
            grid.gridy++;
            this.add(lookingFor, grid);
            grid.gridy++;

            textField = new JTextField();
            textField.setColumns(RunApplication.getColumns());
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
            textField.addActionListener(this);
            this.add(textField, grid);
            grid.gridy++;

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

            if (solve().toString().equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
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
        if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
        } else {
            this.response = textField.getText();
        }
        internalSemaphore.release();
    }

    private static void randomize() {
        // What is given
        int rand = Scenario.rng.nextInt(2);
        switch (rand) {
            case 0:
                givenVal = GivenVal.INIT_THETA;
                break;
            case 1:
                givenVal = GivenVal.INIT_SIDES;
                break;
        }
        // randomizes values based on the problem
        int randomSide;
        switch (givenVal) {
            case INIT_THETA:
                triangle.thetaY = (float) (Scenario.rng.nextInt(80) + 1);
                rngSides(Scenario.rng.nextInt(3) + 1);
                break;
            case INIT_SIDES:
                rngSides(Scenario.rng.nextInt(3) + 1);
                randomSide = Scenario.rng.nextInt(3) + 1;
                while (triangle.sidePresent(randomSide)) {
                    randomSide = Scenario.rng.nextInt(3) + 1;
                }
                rngSides(randomSide);
                triangle.rearrange();
                break;
            default:
                break;
        }

        // What to find
        do {
            rand = Scenario.rng.nextInt(8);
            switch (rand) {
                case 0:
                    problemType = ProblemType.INIT_THETA;
                    break;
                case 1:
                    problemType = ProblemType.INIT_VH;
                    break;
                case 2:
                    problemType = ProblemType.INIT_VX;
                    break;
                case 3:
                    problemType = ProblemType.INIT_VY;
                    break;
                case 4:
                    problemType = ProblemType.MAX_HEIGHT;
                    break;
                case 5:
                    problemType = ProblemType.TIME_MAX_HEIGHT;
                    break;
                case 6:
                    problemType = ProblemType.TIME_TOTAL_X;
                    break;
                case 7:
                    problemType = ProblemType.TOTAL_X;
                    break;
            }
        } while (questionOverlap());
    }

    private static BigDecimal solve() {
        triangle.solve90DegTrig();
        switch (problemType) {
            case INIT_THETA:
                return new BigDecimal(triangle.thetaY, Trigonometry.trigRound);
            case INIT_VH:
                return new BigDecimal(triangle.hypotenuse, Trigonometry.trigRound);
            case INIT_VX:
                return new BigDecimal(triangle.x, Trigonometry.trigRound);
            case INIT_VY:
                return new BigDecimal(triangle.y, Trigonometry.trigRound);
            case F_THETA:
                return new BigDecimal(triangle.thetaY, Trigonometry.trigRound);
            case F_VH:
                return new BigDecimal(triangle.hypotenuse, Trigonometry.trigRound);
            case F_VX:
                return new BigDecimal(triangle.x, Trigonometry.trigRound);
            case F_VY:
                return new BigDecimal(-triangle.y, Trigonometry.trigRound);
            case MAX_HEIGHT: // derived from suvat equation v^2 = u^2 + 2as
                return new BigDecimal((Math.pow(triangle.y, 2) / 19.6), Trigonometry.trigRound);
            case TIME_MAX_HEIGHT:
                return new BigDecimal((triangle.y / 9.8), Trigonometry.trigRound);
            case TIME_TOTAL_X:
                return new BigDecimal(((2 * triangle.y) / 9.8), Trigonometry.trigRound);
            case TOTAL_X:
                return new BigDecimal((triangle.x * ((2 * triangle.y) / 9.8)), Trigonometry.trigRound);
            default:
                return new BigDecimal(0);
        }
    }

    /**
     * Makes sure that what is given isn't what is being asked of.
     */
    private static boolean questionOverlap() {
        switch (givenVal) {
            case INIT_THETA: // Checks that the theta isn't being asked when given
                switch (problemType) {
                    case INIT_THETA:
                        return true;
                    case F_THETA:
                        return true;
                    default:
                        break;
                }
            case INIT_SIDES: // Checks that the sides aren't present
                switch (problemType) {
                    case INIT_VH:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case INIT_VX:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case INIT_VY:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    case F_VH:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case F_VX:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case F_VY:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            default:
                break;
        }
        return false;
    }

    // Randomizes the sides
    private static void rngSides(int side) {
        if (side == 1) {
            triangle.x = (float) (Scenario.rng.nextInt(50) + 1);
        } else if (side == 2) {
            triangle.y = (float) (Scenario.rng.nextInt(50) + 1);
        } else {
            triangle.hypotenuse = (float) (Scenario.rng.nextInt(50) + 1);
        }
    }

    private static void printQuestion() {
        switch (problemType) {
            case INIT_THETA:
                lookingFor = new JLabel("What is the initial theta?");
                break;
            case INIT_VH:
                lookingFor = new JLabel("What is the initial velocity?");
                break;
            case INIT_VX:
                lookingFor = new JLabel("What is the initial velocity of the X position?");
                break;
            case INIT_VY:
                lookingFor = new JLabel("What is the initial velocity of the Y position?");
                break;
            case F_THETA:
                lookingFor = new JLabel("What is the final theta?");
                break;
            case F_VH:
                lookingFor = new JLabel("What is the final velocity?");
                break;
            case F_VX:
                lookingFor = new JLabel("What is the final velocity of the X position?");
                break;
            case F_VY:
                lookingFor = new JLabel("What is the final velocity of the Y position?");
                break;
            case MAX_HEIGHT:
                lookingFor = new JLabel("What is the max height of the projectile?");
                break;
            case TIME_MAX_HEIGHT:
                lookingFor = new JLabel("What is the time that the projectile is at its max height?");
                break;
            case TIME_TOTAL_X:
                lookingFor = new JLabel("What time does the projectile land?");
                break;
            case TOTAL_X:
                lookingFor = new JLabel("What is the total X displacement?");
                break;
            default:
                break;
        }
    }
}
