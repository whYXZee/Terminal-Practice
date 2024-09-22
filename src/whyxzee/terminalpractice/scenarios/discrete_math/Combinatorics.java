package whyxzee.terminalpractice.scenarios.discrete_math;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.DiscreteMath;
import whyxzee.terminalpractice.resources.English;
import whyxzee.terminalpractice.scenarios.ExampleObjects;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.HashMap;

public class Combinatorics extends ScenarioUI implements ActionListener {
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
    private static BigInteger denominator = BigInteger.valueOf(1);
    private static ExampleObjects object = new ExampleObjects();
    private static ProblemType problemType = ProblemType.PERMUTATION_ALLOCATION;
    private static boolean isWord = false; // for generalized
    private static int n_1, n_2, n_3, n_4, r, grouped, totalNumbers;
    private static int[] groups = {}; // for generalized
    private static String randomWord = "";

    // What combinatoric needs to be found?
    private enum ProblemType {
        PERMUTATION_ALLOCATION,
        PERMUTATION_GROUPED,
        PERMUTATION_CIRCULAR,
        PERMUTATION_CLOCK_COUNTER_CLOCK,
        PERMUTATION_GENERALIZED_TOTAL,

        COMBINATION_ALLOCATION,
        COMBINATION_GENERALIZATION_TOTAL,

        PARTITION_ALLOCATION,
    }

    public Combinatorics() throws InterruptedException {
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
                if (solve().equals(new BigInteger(response))) {
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
     * Randomizes the combinatorics problem.
     */
    private static void randomize() {
        object.randomizeObject();
        switch (6) {
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
                problemType = ProblemType.PERMUTATION_GENERALIZED_TOTAL;
                if (Math.random() > .5) {
                    // Variables
                    isWord = true;
                    randomWord = English.words.get(ScenarioConstants.rng.nextInt(English.words.size())).toLowerCase();
                    HashMap<Character, Integer> letters = new HashMap<Character, Integer>();
                    ArrayList<Integer> letterNums = new ArrayList<Integer>();

                    // Getting the number of letters
                    for (Character i : randomWord.toCharArray()) {
                        if (letters.containsKey(i)) {
                            letters.replace(i, letters.get(i) + 1);
                        } else {
                            letters.put(i, 1);
                        }
                    }
                    for (Character i : letters.keySet()) {
                        letterNums.add(letters.get(i));
                    }
                    groups = letterNums.stream().mapToInt(i -> i).toArray();
                } else {
                    isWord = false;
                    n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                    n_2 = ScenarioConstants.rng.nextInt(10) + 1;
                    n_3 = ScenarioConstants.rng.nextInt(10) + 1;
                    if (Math.random() > .5) {
                        n_4 = ScenarioConstants.rng.nextInt(10) + 1;
                    } else {
                        n_4 = 1;
                    }
                    groups = new int[] { n_1, n_2, n_3, n_4 };
                }
                break;

            case 5:
                problemType = ProblemType.COMBINATION_ALLOCATION;
                do {
                    n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                    r = ScenarioConstants.rng.nextInt(3) + 2;
                } while (r >= n_1);
                break;
            case 6:
                problemType = ProblemType.COMBINATION_GENERALIZATION_TOTAL;
                // if (Math.random() > .5) {
                // isWord = true;
                // } else {
                n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                n_2 = ScenarioConstants.rng.nextInt(10) + 1;
                n_3 = ScenarioConstants.rng.nextInt(10) + 1;
                if (Math.random() > .5) {
                    n_4 = ScenarioConstants.rng.nextInt(10) + 1;
                } else {
                    n_4 = 1;
                }
                groups = new int[] { n_1, n_2, n_3, n_4 };
                // }
                break;

            case 7:
                problemType = ProblemType.PARTITION_ALLOCATION;
                n_1 = ScenarioConstants.rng.nextInt(10) + 1;
                n_2 = ScenarioConstants.rng.nextInt(10) + 1;
                n_3 = ScenarioConstants.rng.nextInt(10) + 1;
                if (Math.random() > .5) {
                    n_4 = ScenarioConstants.rng.nextInt(10) + 1;
                } else {
                    n_4 = 1;
                }
                groups = new int[] { n_1, n_2, n_3, n_4 };
                break;
        }
    }

    /**
     * Solves the combinatoric problem.
     * 
     * @return the combinatoric as a {@code BigInteger}, as the problems can get
     *         big.
     */
    private static BigInteger solve() {
        BigInteger combination = BigInteger.valueOf(1);
        switch (problemType) {
            case PERMUTATION_ALLOCATION:
                return DiscreteMath.permutation(n_1, r);
            case PERMUTATION_GROUPED:
                return DiscreteMath.factorial(n_1 - grouped + 1).multiply(DiscreteMath.factorial(grouped));
            case PERMUTATION_CIRCULAR:
                return DiscreteMath.factorial(n_1 - 1);
            case PERMUTATION_CLOCK_COUNTER_CLOCK:
                return DiscreteMath.factorial(n_1 - 1).divide(BigInteger.valueOf(2));
            case PERMUTATION_GENERALIZED_TOTAL:
                denominator = BigInteger.valueOf(1);
                totalNumbers = 0;
                for (int i : groups) {
                    totalNumbers += i;
                    denominator = denominator.multiply(DiscreteMath.factorial(i));
                }
                return DiscreteMath.factorial(totalNumbers).divide(denominator);

            case COMBINATION_ALLOCATION:
                return DiscreteMath.combination(n_1, r);
            case COMBINATION_GENERALIZATION_TOTAL:
                totalNumbers = 0;
                for (int i : groups) {
                    totalNumbers += i;
                }
                combination = DiscreteMath.combination(totalNumbers, groups[0]);
                for (int i = 1; i < groups.length; i++) {
                    combination = combination
                            .multiply(DiscreteMath.combination(totalNumbers - groups[i - 1], groups[i]));
                    totalNumbers -= groups[i - 1];
                    System.out.println(i);
                }
                return DiscreteMath.factorial(totalNumbers).divide(denominator);

            case PARTITION_ALLOCATION:

        }
        return new BigInteger("1");
    }

    /**
     * Prints the combinatoric question.
     */
    private void printQuestion() {
        switch (problemType) {
            case PERMUTATION_ALLOCATION:
                switch (ScenarioConstants.rng.nextInt(2)) {
                    case 0:
                        questions = AppConstants.divideLabel("How many ways can " + r + " unique " + object.plural
                                + " be chosen out of " + n_1 + " " + object.corresponding + "?");
                    case 1:
                        questions = AppConstants.divideLabel("How many permutations are available with "
                                + n_1 + " " + object.plural + " and " + r + " " + object.corresponding + "?");
                }
                break;
            case PERMUTATION_GROUPED:
                questions = AppConstants.divideLabel("How many ways can " + n_1 + " " + object.plural
                        + " be sorted if " + grouped + " " + object.plural + " need to be together?");
                break;
            case PERMUTATION_CIRCULAR:
                questions = AppConstants.divideLabel("How many ways can " + n_1 + " " + object.plural +
                        " be arranged around a circle of " + object.corresponding + "?");
                break;
            case PERMUTATION_CLOCK_COUNTER_CLOCK:
                questions = AppConstants.divideLabel("How many ways can " + n_1 + " " + object.plural +
                        " be arranged around a circle of " + object.corresponding + "?");
                break;
            case PERMUTATION_GENERALIZED_TOTAL:
                if (!isWord) {
                    if (n_4 == 1) {
                        questions = AppConstants
                                .divideLabel("Given groups of " + n_1 + ", " + n_2 + ", and " + n_3 + " "
                                        + object.plural + ", how many ways can they be uniquely ordered?");
                    } else {
                        questions = AppConstants
                                .divideLabel("Given groups of " + n_1 + ", " + n_2 + ", " + n_3 + ", and "
                                        + n_4 + " " + object.plural + ", how many ways can they be uniquely ordered?");
                    }
                } else {
                    questions = AppConstants
                            .divideLabel("Givent the word \"" + randomWord + "\", how many permutations can be made?");
                }
                break;

            case COMBINATION_ALLOCATION:
                switch (ScenarioConstants.rng.nextInt(2)) {
                    case 0:
                        questions = AppConstants.divideLabel("How many ways can " + r + " identical " + object.plural
                                + " be chosen out of " + n_1 + " " + object.corresponding + "?");
                    case 1:
                        questions = AppConstants.divideLabel("How many combinations are available with "
                                + n_1 + " " + object.plural + " and " + r + " " + object.corresponding + "?");
                }
                break;
            case COMBINATION_GENERALIZATION_TOTAL:
                if (!isWord) {
                    if (n_4 == 1) {
                        questions = AppConstants
                                .divideLabel("Given groups of " + n_1 + ", " + n_2 + ", and " + n_3 + " "
                                        + object.plural + ", how many ways can they be uniquely ordered?");
                    } else {
                        questions = AppConstants
                                .divideLabel("Given groups of " + n_1 + ", " + n_2 + ", " + n_3 + ", and "
                                        + n_4 + " " + object.plural + ", how many ways can they be uniquely ordered?");
                    }
                } else {
                    questions = AppConstants
                            .divideLabel("Given the word \"" + randomWord + "\", how many combinations can be made");
                }
                break;

            case PARTITION_ALLOCATION:

                break;
        }
        for (JLabel j : questions) {
            j.setFont(AppConstants.medFont);
            this.add(j, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }
    }

    /**
     * Prints how to solve the combinatoric problem.
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
        switch (problemType) {
            case PERMUTATION_ALLOCATION:
                howToLabels = AppConstants.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + ", and each " + object.singular + " and "
                        + object.correspondingSingle + " is distinct. Therefore, a permutation of "
                        + n_1 + " P " + r + " must occur. (" + n_1 + "!/[" + n_1 + "-" + r + "]!)");
                break;
            case PERMUTATION_GROUPED:
                howToLabels = AppConstants.divideLabel("No two " + object.plural + " can occupy the same "
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
                howToLabels = AppConstants.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + ", and each " + object.singular + " and "
                        + object.correspondingSingle + " is distinct. Becuase a set of " + object.plural
                        + " and a shifted set of " + object.plural + " are the same (example: 1234 = 4123), "
                        + "you fix on one " + object.singular + " to start with then take the " +
                        "factorial of the remaining terms. (" + n_1 + "-1)!");
                break;
            case PERMUTATION_CLOCK_COUNTER_CLOCK:
                howToLabels = AppConstants.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + ", and each " + object.singular + " and "
                        + object.correspondingSingle + " is distinct. Becuase a set of " + object.plural
                        + " and a shifted set of " + object.plural + " are the same (example: 1234 = 4123), "
                        + "you fix on one " + object.singular + " to start with then take the " +
                        "factorial of the remaining terms. In addition, the orientations are treated the same " +
                        "(example: 1234 = 1432), so each permutation counts as 2. ([" + n_1 + "-1]!/2)");
                break;
            case PERMUTATION_GENERALIZED_TOTAL:
                if (!isWord) {
                    howToLabels = AppConstants.divideLabel("Because certain " + object.plural + " are not distinct, "
                            + "the objects must be grouped.");
                } else {
                    howToLabels = AppConstants.divideLabel("In the word \"" + randomWord + "\", certain letters " +
                            "repeat. Therefore, there are non-distinct objects in a permutation, thus a generalized permutation "
                            + "must occur.");
                }
                break;

            case COMBINATION_ALLOCATION:
                howToLabels = AppConstants.divideLabel("No two " + object.plural + " can occupy the same "
                        + object.correspondingSingle + "; each " + object.correspondingSingle + " is distinct, " +
                        "but each " + object.singular + " is not distinct. Therefore, a combination of "
                        + n_1 + " C " + r + " must occur. (" + n_1 + "!/[" + n_1 + "-" + r + "]!)");
                break;
            case COMBINATION_GENERALIZATION_TOTAL:
                if (!isWord) {
                    howToLabels = AppConstants.divideLabel("Because certain " + object.plural + " are not distinct, "
                            + "the objects must be grouped.");
                } else {
                    howToLabels = AppConstants.divideLabel("In the word \"" + randomWord + "\", certain letters " +
                            "repeat. Therefore, there are non-distinct objects in a combination, thus a generalized permutation "
                            + "must occur.");
                }
                break;

        }
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
}