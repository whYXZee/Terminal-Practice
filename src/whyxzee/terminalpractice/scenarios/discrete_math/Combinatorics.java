package whyxzee.terminalpractice.scenarios.discrete_math;

import whyxzee.terminalpractice.application.RunApplication;
import whyxzee.terminalpractice.resources.DiscreteMath;
import whyxzee.terminalpractice.scenarios.ExampleObjects;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Combinatorics extends ScenarioUI implements ActionListener {
    // General variables
    boolean shouldBreak = false;
    int correct = 0;
    public String response = "";
    JLabel questionTracker;
    static JLabel[] questions;
    JTextField textField;
    JLabel correctIncorrect = new JLabel();

    // static JLabel lookingFor = new JLabel();

    // Scenario-specific variables
    private static ProblemType problemType = ProblemType.PERMUTATION_ALLOCATION;
    private static int n_1, n_2, n_3, n_4, r, grouped;
    private static int[] groups = {};
    private static ExampleObjects object = new ExampleObjects();

    /**
     * What part of the motion needs to be found?
     */
    private enum ProblemType {
        PERMUTATION_ALLOCATION,
        PERMUTATION_GROUPED,
        PERMUTATION_CIRCULAR,
        PERMUTATION_CLOCK_COUNTER_CLOCK,
        PERMUTATION_GENERALIZED,

        COMBINATION_ALLOCATION,
    }

    public Combinatorics() throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());

        for (int i = 0; i < RunApplication.goal; i++) {
            RunApplication.getFontSize();

            // Question tracker
            questionTracker = new JLabel("Question " + (i + 1) + "/" + RunApplication.goal);
            questionTracker.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));
            this.add(questionTracker, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;

            // Question
            randomize();
            printQuestion();

            // Answer box
            textField = new JTextField();
            textField.setColumns(RunApplication.getColumns());
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
            textField.addActionListener(this);
            this.add(textField, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;

            // End button
            JButton backButton = new JButton("End practice");
            backButton.setActionCommand("end");
            backButton.setPreferredSize(new Dimension(150, 25));
            backButton.setToolTipText("End the drill early.");
            backButton.setMnemonic(KeyEvent.VK_E);
            backButton.addActionListener(this);
            this.add(backButton, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;

            // Display
            display();
            textField.requestFocusInWindow();
            ScenarioConstants.scenarioSemaphore.acquire();

            // Answering the question
            try {
                if (solve().equals(new BigInteger(response))) {
                    correctIncorrect = new JLabel("Correct!");
                    this.add(correctIncorrect, ScenarioConstants.grid);
                    correct++;
                    display();
                    Thread.sleep(2000);
                } else if (shouldBreak) {
                    break;
                } else {
                    correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
                    printHowTo();
                    ScenarioConstants.scenarioSemaphore.acquire();
                }
            } catch (NumberFormatException error) {
                if (shouldBreak) {
                    break;
                } else {
                    correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
                    printHowTo();
                    ScenarioConstants.scenarioSemaphore.acquire();
                }
            }
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
        } else if (e.getActionCommand().equals("move on")) {

        } else {
            this.response = textField.getText();
        }
        ScenarioConstants.scenarioSemaphore.release();
    }

    /**
     * Randomizes the projectile motion problem.
     */
    private static void randomize() {
        object.randomizeObject();
        switch (ScenarioConstants.rng.nextInt(6)) {
            case 0:
                problemType = ProblemType.PERMUTATION_ALLOCATION;
                do {
                    n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                    r = ScenarioConstants.rng.nextInt(3) + 2;
                } while (r >= n_1);
                break;
            case 1:
                problemType = ProblemType.PERMUTATION_GROUPED;
                do {
                    n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                    grouped = ScenarioConstants.rng.nextInt(3) + 2;
                } while (grouped >= n_1);
                break;
            case 2:
                problemType = ProblemType.PERMUTATION_CIRCULAR;
                n_1 = ScenarioConstants.rng.nextInt(10) + 3;
                break;
            case 3:
                problemType = ProblemType.PERMUTATION_CLOCK_COUNTER_CLOCK;
                n_1 = ScenarioConstants.rng.nextInt(10) + 3;
                break;
            case 4:
                // problemType = ProblemType.PERMUTATION_GENERALIZED;
                // n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                // n_2 = ScenarioConstants.rng.nextInt(10) + 1;
                // n_3 = ScenarioConstants.rng.nextInt(10) + 1;
                // if (Math.random() > .5) {
                // n_4 = ScenarioConstants.rng.nextInt(10) + 1;
                // } else {
                // n_4 = 1;
                // }
                // groups = new int[] { n_1, n_2, n_3, n_4 };
                // break;

            case 5:
                problemType = ProblemType.COMBINATION_ALLOCATION;
                do {
                    n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                    r = ScenarioConstants.rng.nextInt(3) + 2;
                } while (r >= n_1);
                break;
        }
    }

    private static BigInteger solve() {
        switch (problemType) {
            case PERMUTATION_ALLOCATION:
                return DiscreteMath.permutation(n_1, r);
            case PERMUTATION_GROUPED:
                return DiscreteMath.factorial(n_1 - grouped + 1).multiply(DiscreteMath.factorial(grouped));
            case PERMUTATION_CIRCULAR:
                return DiscreteMath.factorial(n_1 - 1);
            case PERMUTATION_CLOCK_COUNTER_CLOCK:
                return DiscreteMath.factorial(n_1 - 1).divide(BigInteger.valueOf(2));
            case PERMUTATION_GENERALIZED:
                BigInteger denominator = BigInteger.valueOf(1);
                int totalNumbers = n_1 + n_2 + n_3 + n_4;
                for (int i : groups) {
                    denominator = denominator.multiply(DiscreteMath.factorial(i));
                }
                return DiscreteMath.factorial(totalNumbers).divide(denominator);

            case COMBINATION_ALLOCATION:
                return DiscreteMath.combination(n_1, r);
        }
        return new BigInteger("1");
    }

    private void printQuestion() {
        switch (problemType) {
            case PERMUTATION_ALLOCATION:
                switch (ScenarioConstants.rng.nextInt(2)) {
                    case 0:
                        questions = RunApplication.divideLabel("How many ways can " + r + " unique " + object.plural
                                + " be chosen out of " + n_1 + " " + object.corresponding + "?");
                    case 1:
                        questions = RunApplication.divideLabel("How many permutations are available with "
                                + n_1 + " " + object.plural + " and " + r + " " + object.corresponding + "?");
                }
                break;
            case PERMUTATION_GROUPED:
                questions = RunApplication.divideLabel("How many ways can " + n_1 + " " + object.plural
                        + " be sorted if " + grouped + " " + object.plural + " need to be together?");
                break;
            case PERMUTATION_CIRCULAR:
                questions = RunApplication.divideLabel("How many ways can " + n_1 + " " + object.plural +
                        " be arranged around a circle of " + object.corresponding + "?");
                break;
            case PERMUTATION_CLOCK_COUNTER_CLOCK:
                questions = RunApplication.divideLabel("How many ways can " + n_1 + " " + object.plural +
                        " be arranged around a circle of " + object.corresponding + "?");
                break;

            case COMBINATION_ALLOCATION:
                switch (ScenarioConstants.rng.nextInt(2)) {
                    case 0:
                        questions = RunApplication.divideLabel("How many ways can " + r + object.plural
                                + " be chosen out of " + n_1 + " " + object.corresponding + "?");
                    case 1:
                        questions = RunApplication.divideLabel("How many combinations are available with "
                                + n_1 + " " + object.plural + " and " + r + " " + object.corresponding + "?");
                }
                break;
        }
        for (JLabel j : questions) {
            j.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 3));
            this.add(j, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }
    }

    private void printHowTo() {
        // Removing everything to re-do window:
        this.removeAll();
        ScenarioConstants.grid.gridx = 0;
        ScenarioConstants.grid.gridy = 0;

        // Adding everything
        JLabel[] howToLabels = {};
        this.add(correctIncorrect, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;
        switch (problemType) {
            case PERMUTATION_ALLOCATION:
                howToLabels = RunApplication.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + ", and each " + object.singular + " and "
                        + object.correspondingSingle + " is distinct. Therefore, a permutation of "
                        + n_1 + " P " + r + " must occur. (" + n_1 + "!/[" + n_1 + "-" + r + "]!)");
                break;
            case PERMUTATION_GROUPED:
                howToLabels = RunApplication.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + ", and each " + object.singular + " and " +
                        object.correspondingSingle + " is distinct. In addition, " + grouped + " " + object.plural +
                        " need to be grouped together. As the two events are independent of each other, you " +
                        "apply the Rule of Sequential Counting (aka the Multiplication Rule). You multiply " +
                        "the factorial of the total amount of " + object.plural + " minus the " + object.plural +
                        " in the group, addding one to treat the group as a single " + object.singular +
                        ", then multiply it by the permutations (factorial) of the " + object.plural + " within the " +
                        "group. ([" + n_1 + "-" + grouped + "+1]!*" + grouped + "!)");
                break;
            case PERMUTATION_CIRCULAR:
                howToLabels = RunApplication.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + ", and each " + object.singular + " and "
                        + object.correspondingSingle + " is distinct. Becuase a set of " + object.plural
                        + " and a shifted set of " + object.plural + " are the same (example: 1234 = 4123), "
                        + "you fix on one " + object.singular + " to start with then take the " +
                        "factorial of the remaining terms. (" + n_1 + "-1)!");
                break;
            case PERMUTATION_CLOCK_COUNTER_CLOCK:
                howToLabels = RunApplication.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + ", and each " + object.singular + " and "
                        + object.correspondingSingle + " is distinct. Becuase a set of " + object.plural
                        + " and a shifted set of " + object.plural + " are the same (example: 1234 = 4123), "
                        + "you fix on one " + object.singular + " to start with then take the " +
                        "factorial of the remaining terms. In addition, the orientations are treated the same " +
                        "(example: 1234 = 1432), so each permutation counts as 2. ([" + n_1 + "-1]!/2)");
                break;

            case COMBINATION_ALLOCATION:
                howToLabels = RunApplication.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + "; each " + object.correspondingSingle + " is distinct, " +
                        "but each " + object.singular + " is not distinct. Therefore, a combination of "
                        + n_1 + " C " + r + " must occur. (" + n_1 + "!/[" + n_1 + "-" + r + "]!)");
                break;

        }
        for (JLabel label : howToLabels) {
            label.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
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
}