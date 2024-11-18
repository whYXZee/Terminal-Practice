package whyxzee.terminalpractice.scenarios.physics;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.util.ArrayList;

public class Uncertainties extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    String finalNum = "";
    String finalError = "";
    double max = 0;
    double min = 0;

    static MathContext round = new MathContext(4, RoundingMode.HALF_UP);
    static ArrayList<BigDecimal> measurements = new ArrayList<BigDecimal>();
    static ArrayList<String> uncertainties = new ArrayList<String>();
    static int power = 0;

    enum ProblemType {
        ABS_UNCERTAINTY,
        FRAC_UNCERTAINTY,
        PER_UNCERTAINTY,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        POWER,
        GRADIENT
    }

    ProblemType problemType = ProblemType.ABS_UNCERTAINTY;

    public Uncertainties() throws InterruptedException {

    }

    // joyce was here! :) tehe

    /**
     * Randomizes the uncertainties problem
     */
    @Override
    public void randomize() {
        switch (ScenarioConstants.rng.nextInt(8)) {
            case 0:
                problemType = ProblemType.ABS_UNCERTAINTY;
                measurements = randomizeMeasurements();
                break;
            case 1:
                problemType = ProblemType.FRAC_UNCERTAINTY;
                measurements = randomizeMeasurements();
                break;
            case 2:
                problemType = ProblemType.PER_UNCERTAINTY;
                measurements = randomizeMeasurements();
                break;
            case 3:
                problemType = ProblemType.ADD;
                uncertainties = randomizeUncertainties();
                break;
            case 4:
                problemType = ProblemType.SUBTRACT;
                uncertainties = randomizeUncertainties();
                break;
            case 5:
                problemType = ProblemType.MULTIPLY;
                uncertainties = randomizeUncertainties();
                break;
            case 6:
                problemType = ProblemType.DIVIDE;
                uncertainties = randomizeUncertainties();
                break;
            case 7:
                problemType = ProblemType.POWER;
                uncertainties = new ArrayList<String>() {
                    {
                        add(getUncertainty(randomizeMeasurements()));
                    }
                };
                power = ScenarioConstants.rng.nextInt(4) + 2;
                break;
            case 8:
                problemType = ProblemType.GRADIENT;
                break;
        }
    }

    @Override
    public String solve() {
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
                return finalNum + " \u00b1 " + finalError;
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
                return finalNum + " \u00b1 " + finalError;
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
                return finalNum + " \u00b1 " + finalError + "%";
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
                return finalNum + " \u00b1 " + finalError + "%";
            case POWER:
                finalNum = new BigDecimal(Math.pow(parseNumberUncertainty(uncertainties.get(0)).doubleValue(), power),
                        round).toString();
                finalError = new BigDecimal(
                        (parseErrorUncertainty(uncertainties.get(0)).doubleValue() / parseNumberUncertainty(
                                uncertainties.get(0))
                                .doubleValue() * 100 * power),
                        round).toString();
                return finalNum + " \u00b1 " + finalError + "%";
            case GRADIENT:

            default:
                return "";
        }
    }

    @Override
    public void customActions(String action) {
        if (action.equals("\u00b1")) {
            response = textField.getText();
            textField.setText(response + "\u00b1");
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case ABS_UNCERTAINTY:
                questions = AppConstants
                        .divideLabel("Find the absolute uncertainty: " + measurements + " (answer as # \u00b1 #)");
                break;
            case FRAC_UNCERTAINTY:
                questions = AppConstants
                        .divideLabel("Find the fractional uncertainty: " + measurements + " (answer as # \u00b1 #)");
                break;
            case PER_UNCERTAINTY:
                questions = AppConstants
                        .divideLabel("Find the percent uncertainty: " + measurements + " (answer as # \u00b1 #%)");
                break;
            case ADD:
                questions = AppConstants.divideLabel(
                        "Find the sum of the uncertainties (absolute): " + uncertainties + " (answer as # \u00b1 #)");
                break;
            case SUBTRACT:
                questions = AppConstants.divideLabel(
                        "Find the difference of the uncertainties (absolute): " + uncertainties
                                + " (answer as # \u00b1 #)");
                break;
            case MULTIPLY:
                questions = AppConstants
                        .divideLabel("Find the product of the uncertainties (percent): " + uncertainties
                                + " (answer as # \u00b1 #%)");
                break;
            case DIVIDE:
                questions = AppConstants
                        .divideLabel("Find the quotient of the uncertainties (percent): " + uncertainties
                                + " (answer as # \u00b1 #%)");
                break;
            case POWER:
                questions = AppConstants
                        .divideLabel("Find the uncertainty w/ the power of " + power + " (percent): " + uncertainties
                                + " (answer as # \u00b1 #%)");
                break;
            default:
        }
    }

    @Override
    public void getHowTo() {
        double error1 = 0;
        double error2 = 0;

        switch (problemType) {
            case ABS_UNCERTAINTY:
                howToLabels = AppConstants.divideLabel("To find the value, average all the values (add" +
                        " them all, the divide them by the number of values). Then, to find the uncertainty, subtract "
                        + "the max value from the minimum value and divide by 2: (" + max + " - " + min + ") / 2.");
                break;
            case FRAC_UNCERTAINTY:
                howToLabels = AppConstants.divideLabel("To find the value, average all the values (add" +
                        " them all, the divide them by the number of values). Then, to find the uncertainty you "
                        + "begin by, subtracting the max value from the minimum value and divide by 2: [(" + max + " - "
                        + min + ") / 2] = " + finalError + ". Afterwards, divide the error by the value: " + finalError
                        + "/" + finalNum + ".");
                break;
            case PER_UNCERTAINTY:
                howToLabels = AppConstants.divideLabel("To find the value, average all the values (add" +
                        " them all, the divide them by the number of values). Then, to find the uncertainty you "
                        + "begin by, subtracting the max value from the minimum value and divide by 2: [(" + max + " - "
                        + min + ") / 2] = " + finalError + ". Afterwards, divide the error by the value (" + finalError
                        + "/ " + finalNum + ") and change it to a percentage.");
                break;
            case ADD:
                howToLabels = AppConstants.divideLabel("To find the sum of the value, add both values: "
                        + parseNumberUncertainty(uncertainties.get(0)) + " + "
                        + parseNumberUncertainty(uncertainties.get(1)) + " = " + finalNum
                        + ". Then, to find the uncertainty you add both uncertainties: "
                        + parseErrorUncertainty(uncertainties.get(0)) + " + "
                        + parseErrorUncertainty(uncertainties.get(1)) + " = "
                        + finalError + ".");
                break;
            case SUBTRACT:
                howToLabels = AppConstants.divideLabel("To find the difference of the value, subtract both values: "
                        + parseNumberUncertainty(uncertainties.get(0)) + " - "
                        + parseNumberUncertainty(uncertainties.get(1)) + " = " + finalNum
                        + ". Then, to find the uncertainty you subtract both uncertainties: "
                        + parseErrorUncertainty(uncertainties.get(0)) + " - "
                        + parseErrorUncertainty(uncertainties.get(1)) + " = "
                        + finalError + ".");
                break;
            case MULTIPLY:
                error1 = parseErrorUncertainty(uncertainties.get(0)).doubleValue()
                        / parseNumberUncertainty(uncertainties.get(0)).doubleValue() * 100;
                error2 = parseErrorUncertainty(uncertainties.get(1)).doubleValue()
                        / parseNumberUncertainty(uncertainties.get(1)).doubleValue() * 100;
                howToLabels = AppConstants.divideLabel("To find the product of the value, multiply both values: "
                        + parseNumberUncertainty(uncertainties.get(0)) + " * "
                        + parseNumberUncertainty(uncertainties.get(1)) + " = " + finalNum
                        + ". Then, to find the uncertainty you add both percent uncertainties: "
                        + error1 + " + " + error2 + " = " + finalError + "%.");
                break;
            case DIVIDE:
                error1 = parseErrorUncertainty(uncertainties.get(0)).doubleValue()
                        / parseNumberUncertainty(uncertainties.get(0)).doubleValue() * 100;
                error2 = parseErrorUncertainty(uncertainties.get(1)).doubleValue()
                        / parseNumberUncertainty(uncertainties.get(1)).doubleValue() * 100;
                howToLabels = AppConstants.divideLabel("To find the quotient of the value, divide both values: "
                        + parseNumberUncertainty(uncertainties.get(0)) + " / "
                        + parseNumberUncertainty(uncertainties.get(1)) + " = " + finalNum
                        + ". Then, to find the uncertainty you add both percent uncertainties: "
                        + error1 + " + " + error2 + " = " + finalError + "%.");
                break;
            case POWER:
                error1 = parseErrorUncertainty(uncertainties.get(0)).doubleValue()
                        / parseNumberUncertainty(uncertainties.get(0)).doubleValue() * 100;
                howToLabels = AppConstants.divideLabel("First, get the power of the value: "
                        + parseNumberUncertainty(uncertainties.get(0)) + "^( " + power
                        + ") = " + finalNum + ". Then, to find the uncertainty you add both percent uncertainties: "
                        + error1 + " * " + power + " = " + finalError + "%.");
                break;
            case GRADIENT:
                howToLabels = AppConstants.divideLabel("");
                break;
        }
    }

    @Override
    public void getExtraButtons() {
        extraButtonsArray = new JButton[] { new JButton("\u00b1") };
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Solve the given uncertainties problem.\nA '+-' in the answer will be turned into a \u00b1.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    //
    // Scenario-specific methods
    //

    /**
     * Randomizes the given set of measurements.
     * 
     * @return ArrayList of measurements
     */
    private ArrayList<BigDecimal> randomizeMeasurements() {
        ArrayList<BigDecimal> output = new ArrayList<BigDecimal>();
        double x = ScenarioConstants.rng.nextInt(12) + 1;
        for (int i = 0; i < ScenarioConstants.rng.nextInt(6) + 2; i++) {
            output.add(new BigDecimal(x + Math.random(), round));
        }
        return output;
    }

    /**
     * Randomizes two uncertainties for arithmetics (addition, subtraction,
     * multiplication, and division)
     * 
     * @return two uncertainties (value and the error)
     */
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
            if (i.equals('\u00b1')) {
                isError = true;
            } else if (isError && (Character.isDigit(i) || i.equals('.'))) {
                stringedNum += i;
            }
        }
        return new BigDecimal(Double.valueOf(stringedNum), round);
    }

    /**
     * Gets the uncertainty from the range of data.
     * 
     * @param arrayInput
     * @return
     */
    private String getUncertainty(ArrayList<BigDecimal> arrayInput) {
        BigDecimal total = new BigDecimal(0);
        BigDecimal error;

        max = 0;
        min = 0;

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

        finalNum = total.toString();
        finalError = error.toString();

        switch (problemType) {
            case ABS_UNCERTAINTY:
                return total.doubleValue() + " \u00b1 " + error.doubleValue();
            case FRAC_UNCERTAINTY:
                error = new BigDecimal(error.doubleValue() / total.doubleValue(), round);
                return total.doubleValue() + " \u00b1 " + error.doubleValue();
            case PER_UNCERTAINTY:
                error = new BigDecimal((error.doubleValue() / total.doubleValue()) * 100, round);
                return total.doubleValue() + " \u00b1 " + error.doubleValue() + "%";
            default:
                return total.doubleValue() + " \u00b1 " + error.doubleValue();
        }

    }
}
