package whyxzee.terminalpractice.scenarios.algebra;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.HashMap;

public class Factoring extends ScenarioUI implements ActionListener {
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
    private static ProblemType problemType = ProblemType.SFFT;
    private static Equation eq = new Equation(new ArrayList<String>());
    private static HashMap<Character, Integer> varsAndVals = new HashMap<Character, Integer>();
    private static String x = "";
    private static String y = "";
    private static String xTerm = "";
    private static String yTerm = "";
    // private static ArrayList<String> equationList = new ArrayList<String>();

    // What combinatoric needs to be found?
    private enum ProblemType {
        SFFT // Simon's Favorite Factoring Trick
    }

    public Factoring() throws InterruptedException {
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
                if (solve().equals(response)) {
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

    private void printQuestion() {
        switch (problemType) {
            case SFFT:
                questions = AppConstants.divideLabel(
                        "Given the equation: " + eq
                                + ", what are the factors? [answer in the form of (x + #)(y + #) = #]");
        }

        for (JLabel j : questions) {
            j.setFont(AppConstants.medFont);
            this.add(j, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }
    }

    private static void randomize() {
        // Resetting vars in between questions
        varsAndVals = new HashMap<Character, Integer>();
        // equationList = new ArrayList<String>();

        switch (0) {
            case 0:
                problemType = ProblemType.SFFT;

                // Randomize values
                if (Math.random() > .5) {
                    varsAndVals.put('x', -(ScenarioConstants.rng.nextInt(10) + 1));
                } else {
                    varsAndVals.put('x', ScenarioConstants.rng.nextInt(10) + 1);
                }

                if (Math.random() > .5) {
                    varsAndVals.put('y', -(ScenarioConstants.rng.nextInt(10) + 1));
                } else {
                    varsAndVals.put('y', ScenarioConstants.rng.nextInt(10) + 1);
                }

                if (Math.random() > .5) {
                    varsAndVals.put('=', -(ScenarioConstants.rng.nextInt(10) + 1));
                } else {
                    varsAndVals.put('=', ScenarioConstants.rng.nextInt(10) + 1);
                }

                // Formatting
                x = varsAndVals.get('x') + "x";
                y = varsAndVals.get('y') + "y";

                if (Math.abs(varsAndVals.get('x')) == 1) {
                    x = "x";
                }
                if (Math.abs(varsAndVals.get('y')) == 1) {
                    y = "y";
                }

                eq = new Equation(new ArrayList<String>() {
                    {
                        add("xy");
                        add(x);
                        add(y);
                        add("=" + varsAndVals.get('='));
                    }
                });
                break;
        }

    }

    private static String solve() {
        switch (problemType) {
            case SFFT:
                // Var Declaration
                xTerm = "";
                yTerm = "";

                // Building the var
                if (varsAndVals.get('y') > 0) {
                    xTerm = "(x + " + varsAndVals.get('y') + ")";
                } else {
                    xTerm = "(x - " + varsAndVals.get('y') + ")";
                }
                if (varsAndVals.get('x') > 0) {
                    yTerm = "(y + " + varsAndVals.get('x') + ")";
                } else {
                    yTerm = "(y + " + varsAndVals.get('x') + ")";
                }
                return xTerm + yTerm + " = " + (varsAndVals.get('=') + (varsAndVals.get('x') * varsAndVals.get('y')));
            default:
                return "";
        }
    }

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
            case SFFT:
                howToLabels = AppConstants.divideLabel("First, factor out the x: " + xTerm
                        + ". Second, add the number that is the product of the x coefficient and the y coefficient to both sides: "
                        + varsAndVals.get('x') + " * " + varsAndVals.get('y') + " = "
                        + (varsAndVals.get('y') * varsAndVals.get('x')) + ". Finally, factor out the y term: " + yTerm
                        + ". The result should be: " + xTerm + yTerm + " = " + varsAndVals.get('=') + " + "
                        + (varsAndVals
                                .get('y') * varsAndVals.get('x')));
                break;
        }

        for (JLabel label : howToLabels) {
            label.setFont(AppConstants.medFont);
            this.add(label, ScenarioConstants.grid);
            ScenarioConstants.grid.gridy++;
        }

        // Button
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(this);
        continueButton.setActionCommand("move on");
        continueButton.setMnemonic(KeyEvent.VK_C);
        continueButton.setFont(AppConstants.smallFont);
        continueButton.setPreferredSize(AppConstants.smallButtonDimension);
        continueButton.setToolTipText("Continue to the next problem.");
        this.add(continueButton, ScenarioConstants.grid);
        ScenarioConstants.grid.gridy++;

        display();
    }

}
