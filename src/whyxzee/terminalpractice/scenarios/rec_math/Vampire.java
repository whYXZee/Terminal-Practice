package whyxzee.terminalpractice.scenarios.rec_math;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.RecMath;
import whyxzee.terminalpractice.scenarios.ScenarioTools;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Scenarios of 'vampire' and 'fang' numbers. Questions include:
 * <ul>
 * <li>Fang Numbers to Vampire numbers
 * <li>Vampire Numbers to Fang Numbers
 * 
 */
public class Vampire extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private int fang_1 = 0;
    private int fang_2 = 0;
    private int vampire = 0;

    private static ArrayList<Set<Character>> combos = new ArrayList<Set<Character>>();

    //
    // Question-related variables
    //
    ProblemMulti problemMulti = ProblemMulti.FANG_TO_VAMPIRE;

    boolean atLeastOneAnswer = false;

    // What combinatoric needs to be found?
    // private enum ProblemOpen {
    // }

    private enum ProblemMulti {
        FANG_TO_VAMPIRE,
        VAMPIRE_TO_FANG
    }

    public Vampire() {
    }

    @Override
    public void runProblem(int questionNum) throws InterruptedException {
        // System.out.println(i);
        resetGrid();
        questionTracker(questionNum);

        // Question
        questionType = QuestionType.MULTI_CHOICE;

        randomizeMulti();
        printQuestion();

        multiChoices();

        // End button
        backButton();

        // Display
        display();
        scenarioSemaphore.acquire();
    }

    @Override
    public void randomizeMulti() {
        switch (0/* rng.nextInt(2) */) {
            case 0:
                problemMulti = ProblemMulti.FANG_TO_VAMPIRE;

                // Creating the answers
                boolean hasAnswer = false;
                do {
                    // Clear so there is no bleed between answer choices/questions
                    choices.clear();
                    correctChoices.clear();
                    combos.clear();

                    for (int i = 0; i < ScenarioTools.maxChoices; i++) {
                        fang_1 = rng.nextInt(ScenarioTools.maxNum * 100);
                        fang_2 = rng.nextInt(ScenarioTools.maxNum * 100);

                        // Checking if the answer choice is correct
                        if (areFangs(fang_1, fang_2)) {
                            correctChoices.add("{" + fang_1 + ", " + fang_2 + "}");
                            hasAnswer = true;
                        }
                        choices.add("{" + fang_1 + ", " + fang_2 + "}");
                    }
                } while (!hasAnswer);
                break;

            case 1:
                problemMulti = ProblemMulti.VAMPIRE_TO_FANG;
                fang_1 = rng.nextInt(ScenarioTools.maxNum * 100);
                fang_2 = rng.nextInt(ScenarioTools.maxNum * 100);

                for (int i = 0; i < ScenarioTools.maxChoices; i++) {
                    vampire = rng.nextInt(ScenarioTools.maxNum * 100);

                    // Checking if the answer choice is correct
                    if (RecMath.validVampire(vampire, fang_1, fang_2)) {
                        correctChoices.add(Integer.toString(vampire));
                    }
                    choices.add(Integer.toString(vampire));
                }
                break;
        }
    }

    @Override
    public void getQuestion() {
        switch (problemMulti) {
            case FANG_TO_VAMPIRE:
                questions = AppConstants.divideLabel("Which of the following numbers make up a vampire number?");
                break;
            case VAMPIRE_TO_FANG:
                questions = new JLabel[] { new JLabel("Which of the following numbers have these fangs?"),
                        new JLabel("{" + fang_1 + ", " + fang_2 + "}") };
                break;
        }

    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Find the fangs or vampire numbers.\nIf there are no answers, then hit 'submit' with no answers selected.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    //
    // Scenario-specific function
    //
    public static void setCombination(char[] set, Set<Character> combination, int currentIndex, int finalIndex,
            ArrayList<Integer> usedIndexes) {
        for (int i = 0; i < set.length; i++) {
            //
            // First Iteration
            //
            if (combination == null) {
                combination = new HashSet<Character>();
            }
            if (usedIndexes == null) {
                usedIndexes = new ArrayList<Integer>();
            }

            //
            // Every Iteration
            //
            if (!usedIndexes.contains(i)) {
                // to not repeat positions
                usedIndexes.add(i);

                // to add position to the set
                combination.add(set[i]);

                if (currentIndex + 1 == finalIndex) {
                    // Final iteration
                    combos.add(combination);
                } else {
                    setCombination(set, combination, currentIndex + 1, finalIndex, usedIndexes);
                }
            }
        }
    }

    /**
     * Checks if two numbers are valid fang numbers.
     * 
     * @param fang_1 the first 'fang' number
     * @param fang_2 the second 'fang' number
     * @return {@code true} if the numbers are fangs, {@code false} if otherwise.
     */
    public static boolean areFangs(int fang_1, int fang_2) {
        // doesn't work, cuz numbers can't repeat
        int product = fang_1 * fang_2;

        Set<Character> f_1 = intToSet(fang_1);
        Set<Character> f_2 = intToSet(fang_2);
        char[] vampire = Integer.toString(product).toCharArray();

        boolean passing = true;
        //
        // Check that the fangs are the same digit size
        //
        passing = (f_1.size() == f_2.size());

        // check that they aren't the same
        if (passing) {
            passing = (fang_1 != fang_2);
        }

        //
        // Check for combo
        //
        if (passing) {
            // all combinations
            setCombination(vampire, null, 0, vampire.length / 2, null);

            if (combos.contains(f_1)) {
                combos.remove(f_1);
            } else {
                return false;
            }

            if (combos.contains(f_2)) {
                combos.remove(f_2);
            } else {
                return false;
            }
            System.out.println(combos);
        }

        return passing;
    }

    private static Set<Character> intToSet(int input) {
        Set<Character> output = new HashSet<Character>();

        for (Character i : Integer.toString(input).toCharArray()) {
            output.add(i);
        }

        return output;
    }

}