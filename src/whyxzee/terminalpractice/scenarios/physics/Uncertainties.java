package whyxzee.terminalpractice.scenarios.physics;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import java.util.ArrayList;

public class Uncertainties extends ScenarioUI implements ActionListener {

    GridBagConstraints grid = new GridBagConstraints();
    // Scenairo Constants
    boolean shouldBreak = false;
    public int correct = 0;
    public String response = "";
    JTextField textField;
    public JLabel correctIncorrect = new JLabel();

    // Scenario-specific Constants
    static MathContext round = new MathContext(4, RoundingMode.HALF_UP);
    static ArrayList<BigDecimal> measurements = new ArrayList<BigDecimal>();
    static ArrayList<String> uncertainties = new ArrayList<String>();
    static int power = 0;

    enum ProblemType {
        NONE,
        ABS_UNCERTAINTY,
        FRAC_UNCERTAINTY,
        PER_UNCERTAINTY,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        POWER
    }

    ProblemType problemType = ProblemType.NONE;

    public Uncertainties() throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        for (int i = 0; i < AppConstants.goal; i++) {
            // Showing the equation
            randomize();
            JLabel questionTracker = new JLabel("Question " + (i + 1) + "/" + AppConstants.goal);
            questionTracker.setFont(AppConstants.biggerFont);
            this.add(questionTracker, grid);
            grid.gridy++;
            printQuestion();

            textField = new JTextField();
            textField.setColumns(AppConstants.answerColumns);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setFont(AppConstants.smallFont);
            textField.addActionListener(this);
            this.add(textField, grid);
            grid.gridy++;

            JButton backButton = new JButton("End practice");
            backButton.addActionListener(this);
            backButton.setActionCommand("end");
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setFont(AppConstants.medFont);
            backButton.setToolTipText("End the drill early.");
            backButton.setMnemonic(KeyEvent.VK_E);
            this.add(backButton, grid);
            grid.gridy++;

            // Display
            display();
            textField.requestFocusInWindow();
            ScenarioConstants.scenarioSemaphore.acquire();

            // Checking the input
            correctIncorrect.setFont(AppConstants.medFont);
            if (solve().equals(response)) {
                correctIncorrect = new JLabel("Correct!");
                correctIncorrect.setFont(AppConstants.bigFont);
                correct++;
            } else if (shouldBreak) {
                break;
            } else {
                correctIncorrect = new JLabel("Incorrect, the answer was: " + solve());
                correctIncorrect.setFont(AppConstants.bigFont);
            }
            this.add(correctIncorrect, grid);
            display();
            Thread.sleep(2000);
            this.removeAll();
        }
        JOptionPane.showMessageDialog(AppConstants.frame, "You got " + correct + " correct!", "Scenario Completion",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // joyce was here! :) tehe
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("timer")) {
            this.response = textField.getText();
        } else if (e.getActionCommand().equals("end")) {
            shouldBreak = true;
        } else {
            this.response = textField.getText();
        }

        ScenarioConstants.scenarioSemaphore.release();
    }

    private void printQuestion() {
        JLabel question = new JLabel();
        switch (problemType) {
            case ABS_UNCERTAINTY:
                question = new JLabel("Find the absolute uncertainty: " + measurements);
                break;
            case FRAC_UNCERTAINTY:
                question = new JLabel("Find the fractional uncertainty: " + measurements);
                break;
            case PER_UNCERTAINTY:
                question = new JLabel("Find the percent uncertainty: " + measurements);
                break;
            case ADD:
                question = new JLabel("Find the sum of the uncertainties (absolute): " + uncertainties);
                break;
            case SUBTRACT:
                question = new JLabel(
                        "Find the difference of the uncertainties (absolute): " + uncertainties);
                break;
            case MULTIPLY:
                question = new JLabel("Find the product of the uncertainties (percent): " + uncertainties);
                break;
            case DIVIDE:
                question = new JLabel("Find the quotient of the uncertainties (percent): " + uncertainties);
                break;
            case POWER:
                question = new JLabel("Find the uncertainty w/ the power of " + power + " (percent): " + uncertainties);
                break;
            default:
        }
        question.setFont(AppConstants.medFont);
        this.add(question, grid);
        grid.gridy++;
    }

    private void randomize() {
        switch (ScenarioConstants.rng.nextInt(8) + 1) {
            case 1:
                problemType = ProblemType.ABS_UNCERTAINTY;
                measurements = randomizeMeasurements();
                break;
            case 2:
                problemType = ProblemType.FRAC_UNCERTAINTY;
                measurements = randomizeMeasurements();
                break;
            case 3:
                problemType = ProblemType.PER_UNCERTAINTY;
                measurements = randomizeMeasurements();
                break;
            case 4:
                problemType = ProblemType.ADD;
                uncertainties = randomizeUncertainties();
                break;
            case 5:
                problemType = ProblemType.SUBTRACT;
                uncertainties = randomizeUncertainties();
                break;
            case 6:
                problemType = ProblemType.MULTIPLY;
                uncertainties = randomizeUncertainties();
                break;
            case 7:
                problemType = ProblemType.DIVIDE;
                uncertainties = randomizeUncertainties();
                break;
            case 8:
                problemType = ProblemType.POWER;
                uncertainties = new ArrayList<String>() {
                    {
                        add(getUncertainty(randomizeMeasurements()));
                    }
                };
                power = ScenarioConstants.rng.nextInt(4) + 2;
                break;
            default:
        }

    }

    /**
     * Randomizes the given set of measurements.
     * 
     * @return
     */
    private ArrayList<BigDecimal> randomizeMeasurements() {
        ArrayList<BigDecimal> output = new ArrayList<BigDecimal>();
        double x = ScenarioConstants.rng.nextInt(12) + 1;
        for (int i = 0; i < ScenarioConstants.rng.nextInt(6) + 2; i++) {
            output.add(new BigDecimal(x + Math.random(), round));
        }
        return output;
    }

    private ArrayList<String> randomizeUncertainties() {
        return new ArrayList<String>() {
            {
                add(getUncertainty(randomizeMeasurements()));
                add(getUncertainty(randomizeMeasurements()));
            }
        };
    }

    /**
     * Parses the number from uncertainties (<number> +- <error>).
     * 
     * @param input
     * @return
     */
    private BigDecimal parseNumberUncertainty(String input) {
        String stringedNum = "";
        for (Character i : input.toCharArray()) {
            if (!i.equals(' ')) { // we never need to go to the other side, so if it is then we break.
                stringedNum = stringedNum + i;
            } else {
                break;
            }
        }
        return new BigDecimal(Double.valueOf(stringedNum), round);
    }

    /**
     * Parses the error from uncertainties (<number> +- <error>).
     * 
     * @param input
     * @return
     */
    private BigDecimal parseErrorUncertainty(String input) {
        String stringedNum = "";
        boolean isError = false;
        for (Character i : input.toCharArray()) {
            if (i.equals('+')) {
                isError = true;
            } else if (isError && (Character.isDigit(i) || i.equals('.'))) {
                stringedNum += i;
            }
        }
        return new BigDecimal(Double.valueOf(stringedNum), round);
    }

    private String solve() {
        String finalNum = "";
        String finalError = "";
        switch (problemType) {
            case ABS_UNCERTAINTY:
                return getUncertainty(measurements);
            case FRAC_UNCERTAINTY:
                return getUncertainty(measurements);
            case PER_UNCERTAINTY:
                return getUncertainty(measurements);
            case ADD:
                finalNum = new BigDecimal(parseNumberUncertainty(uncertainties.get(0)).doubleValue()
                        + parseNumberUncertainty(uncertainties.get(1)).doubleValue(), round).toString();
                finalError = new BigDecimal(parseErrorUncertainty(uncertainties.get(0)).doubleValue()
                        + parseErrorUncertainty(uncertainties.get(1)).doubleValue(), round).toString();
                return finalNum + " +- " + finalError;
            case SUBTRACT:
                finalNum = new BigDecimal(parseNumberUncertainty(uncertainties.get(0)).doubleValue()
                        - parseNumberUncertainty(uncertainties.get(1)).doubleValue(), round).toString();
                finalError = new BigDecimal(parseErrorUncertainty(uncertainties.get(0)).doubleValue()
                        - parseErrorUncertainty(uncertainties.get(1)).doubleValue(), round).toString();

                // No negative values cuz idk.
                if (AlgebraFunctions.isNegative(finalNum)) {
                    finalNum = new BigDecimal(-1 * Double.valueOf(finalNum), round).toString();
                }
                if (AlgebraFunctions.isNegative(finalError)) {
                    finalError = new BigDecimal(-1 * Double.valueOf(finalError), round).toString();
                }
                return finalNum + " +- " + finalError;
            case MULTIPLY:
                finalNum = new BigDecimal(parseNumberUncertainty(uncertainties.get(0)).doubleValue()
                        * parseNumberUncertainty(uncertainties.get(1)).doubleValue(), round).toString();
                finalError = new BigDecimal(
                        (parseErrorUncertainty(uncertainties.get(0)).doubleValue() / parseNumberUncertainty(
                                uncertainties.get(0))
                                .doubleValue()) * 100
                                + (parseErrorUncertainty(uncertainties.get(1)).doubleValue() / parseNumberUncertainty(
                                        uncertainties.get(1)).doubleValue()) * 100,
                        round).toString();
                // Error has to be fraction or percentage
                return finalNum + " +- " + finalError + "%";
            case DIVIDE:
                finalNum = new BigDecimal(parseNumberUncertainty(uncertainties.get(0)).doubleValue()
                        / parseNumberUncertainty(uncertainties.get(1)).doubleValue(), round).toString();
                finalError = new BigDecimal(
                        (parseErrorUncertainty(uncertainties.get(0)).doubleValue() / parseNumberUncertainty(
                                uncertainties.get(0))
                                .doubleValue()) * 100
                                + (parseErrorUncertainty(uncertainties.get(1)).doubleValue() / parseNumberUncertainty(
                                        uncertainties.get(1)).doubleValue()) * 100,
                        round).toString();
                // Error has to be fraction or percentage
                return finalNum + " +- " + finalError + "%";
            case POWER:
                finalNum = new BigDecimal(Math.pow(parseNumberUncertainty(uncertainties.get(0)).doubleValue(), power),
                        round).toString();
                finalError = new BigDecimal(
                        (parseErrorUncertainty(uncertainties.get(0)).doubleValue() / parseNumberUncertainty(
                                uncertainties.get(0))
                                .doubleValue() * 100 * power),
                        round).toString();
                return finalNum + " +- " + finalError + "%";
            default:
                return "";
        }
    }

    private String getUncertainty(ArrayList<BigDecimal> arrayInput) {
        BigDecimal total = new BigDecimal(0);
        BigDecimal error;

        double max = 0;
        double min = 0;
        for (int i = 0; i < arrayInput.size(); i++) {
            double index = arrayInput.get(i).doubleValue();
            if (max < index) {
                max = index;
            }
            if (i == 0) { // allows the min to be a number and not 0, as it would never change
                min = index;
            } else if (min > index) {
                min = index;
            }
            total = new BigDecimal(total.doubleValue() + index, round);
        }

        total = new BigDecimal(total.doubleValue() / arrayInput.size(), round);
        error = new BigDecimal((max - min) / 2, round);
        switch (problemType) {
            case ABS_UNCERTAINTY:
                return total.doubleValue() + " +- " + error.doubleValue();
            case FRAC_UNCERTAINTY:
                error = new BigDecimal(error.doubleValue() / total.doubleValue(), round);
                return total.doubleValue() + " +- " + error.doubleValue();
            case PER_UNCERTAINTY:
                error = new BigDecimal((error.doubleValue() / total.doubleValue()) * 100, round);
                return total.doubleValue() + " +- " + error.doubleValue() + "%";
            default:
                return total.doubleValue() + " +- " + error.doubleValue();
        }

    }
}
