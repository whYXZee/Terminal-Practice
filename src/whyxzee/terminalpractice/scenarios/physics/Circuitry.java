package whyxzee.terminalpractice.scenarios.physics;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

// Add more scenarios regarding actual circuits
// Add

/**
 * Scenarios over circuitry. Available question types
 * include:
 * <ul>
 * <li>Voltage of a series circuit
 * <li>Resistance of a series circuit
 * <li>Current of a series circuit
 * <li>Voltage of a parallel circuit
 * <li>Resistance of a parallel circuit
 * <li>Current of a parallel circuit
 * <li>Resistivity
 * <li>Drift velocity
 * <li>Electromotive force
 * <li>{@code Fix} add units
 * <li>{@code Fix} repeating answers
 * <li>{@code Fix} incorrect random choices for parallel resistance
 * <li>{@code Fix} given values are not rounded to the proper rounding?
 * <li>{@code Fix} use diagrams instead of labels for the circuits.
 * <li>{@code Fix} use better questions
 * 
 */
public class Circuitry extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private ArrayList<Double> values = new ArrayList<Double>(); // convert to string later
    private double baseVal = 0;
    private double numAnswer = 0;
    private double error = 0;

    private int rE = 0; // external resistance
    private int rI = 0; // internal resistance
    private int current = 0;
    private double area = 0;
    private int length = 0;
    private int time = 0;

    //
    // Question-related variables
    //
    private ProblemType problemType = ProblemType.SERIES_VOLTAGE;

    private enum ProblemType {
        // Multi and open
        SERIES_VOLTAGE,
        SERIES_RESISTANCE,
        SERIES_CURRENT,

        PARALLEL_VOLTAGE,
        PARALLEL_RESISTANCE,
        PARALLEL_CURRENT,

        RESISTIVITY,
        DRIFT_VELOCITY,
        ELECTROMOTIVE,

        // OHM_LAW,
        // POWER_DISSIPATED,
        // POTENTIAL_DIFF,
        // DC_CURRENT // i = delta q / delta t

        // Open-only

        // Multi-only
    }

    public Circuitry() {
        openRNG = multiRNG = ProblemType.values().length;
    }

    @Override
    public void randomizeOpen() {
        // Clear values to remove bleed to other questions
        values.clear();
        numAnswer = 0;

        switch (rng.nextInt(openRNG)) {
            case 0: // SERIES VOLTAGE
                problemType = ProblemType.SERIES_VOLTAGE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += baseVal + error;
                }
                answer = new BigDecimal(numAnswer, round).toPlainString() + "V";
                break;
            case 1: // SERIES RESISTANCE
                problemType = ProblemType.SERIES_RESISTANCE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += baseVal + error;
                }
                answer = new BigDecimal(numAnswer, round).toPlainString() + "\u03a9";
                break;
            case 2: // SERIES CURRENT
                problemType = ProblemType.SERIES_CURRENT;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                error = new BigDecimal(Math.random(), round).doubleValue();
                if (Math.random() > .5) {
                    error *= -1;
                }
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // to trick the person
                    values.add(baseVal + error);
                }
                answer = new BigDecimal(baseVal + error, round).toPlainString() + "A";
                break;
            case 3: // PARALLEL VOLTAGE
                problemType = ProblemType.PARALLEL_VOLTAGE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                error = new BigDecimal(Math.random(), round).doubleValue();
                if (Math.random() > .5) {
                    error *= -1;
                }
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // To trick the person
                    values.add(baseVal + error);
                }
                answer = new BigDecimal(baseVal + error, round).toPlainString() + "V";
                break;
            case 4: // PARALLEL RESISTANCE
                problemType = ProblemType.PARALLEL_RESISTANCE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += Math.pow(baseVal + error, -1);
                }
                answer = new BigDecimal(Math.pow(numAnswer, -1), round).toPlainString() + "\u03a9";
                break;
            case 5: // PARALLEL CURRENT
                problemType = ProblemType.PARALLEL_CURRENT;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += baseVal + error;
                }
                answer = new BigDecimal(numAnswer, round).toPlainString() + "A";
                break;
            case 6: // RESISTIVITY
                problemType = ProblemType.RESISTIVITY;

                // Randomize values
                rE = rng.nextInt(maxNum) + 1;
                area = Math.pow(rng.nextInt(maxNum) + 1, 2);
                length = rng.nextInt(maxNum) + 1;
                answer = new BigDecimal((rE * area) / length, round).toPlainString();
                break;
            case 7: // DRIFT VELOCITY
                problemType = ProblemType.DRIFT_VELOCITY;

                // Randomize values
                length = rng.nextInt(maxNum * 2) + 1;
                time = rng.nextInt(60) + 1;
                answer = new BigDecimal(length / (double) (time * 100), round).toPlainString() + "m/s"; // * 100 for cm
                                                                                                        // -> m
                break;
            case 8: // ELECTROMOTIVE FORCE
                problemType = ProblemType.ELECTROMOTIVE;

                // Randomize values
                rI = rng.nextInt(maxNum) + 1;
                rE = rng.nextInt(maxNum) + 1;
                current = rng.nextInt(maxNum) + 1;
                answer = new BigDecimal(current * (rI + rE), round).toPlainString() + "V";
                break;
        }
    }

    @Override
    public void randomizeMulti() {
        // Clear values to remove bleed between questions
        values.clear();
        numAnswer = 0;

        switch (rng.nextInt(multiRNG)) {
            case 0: // SERIES VOLTAGE
                problemType = ProblemType.SERIES_VOLTAGE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += baseVal + error;
                }
                answer = new BigDecimal(numAnswer, round).toPlainString() + "V";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    numAnswer -= baseVal + error;

                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }
                    numAnswer += baseVal + error;

                    choices.add(new BigDecimal(numAnswer, round).toPlainString() + "V");
                }
                break;
            case 1: // SERIES RESISTANCE
                problemType = ProblemType.SERIES_RESISTANCE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += baseVal + error;
                }
                answer = new BigDecimal(numAnswer, round).toPlainString() + "\u03a9";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    numAnswer -= baseVal + error;

                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }
                    numAnswer += baseVal + error;

                    choices.add(new BigDecimal(numAnswer, round).toPlainString() + "\u03a9");
                }
                break;
            case 2: // SERIES CURRENT
                problemType = ProblemType.SERIES_CURRENT;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                error = new BigDecimal(Math.random(), round).doubleValue();
                if (Math.random() > .5) {
                    error *= -1;
                }
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // to trick the person
                    values.add(baseVal + error);
                }
                answer = new BigDecimal(baseVal + error, round).toPlainString() + "A";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);

                choices.add(new BigDecimal((baseVal + error) * values.size(), round).toPlainString() + "A");
                for (int i = 0; i < maxChoices - 2; i++) {
                    double tempError = 0;
                    do {
                        tempError = new BigDecimal(Math.random(), round).doubleValue();
                        if (Math.random() > .5) {
                            tempError *= -1;
                        }
                    } while (tempError == error); // no repeating answers

                    choices.add(new BigDecimal(baseVal + tempError).toPlainString() + "A");
                }
                break;
            case 3: // PARALLEL VOLTAGE
                problemType = ProblemType.PARALLEL_VOLTAGE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                error = new BigDecimal(Math.random(), round).doubleValue();
                if (Math.random() > .5) {
                    error *= -1;
                }
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // To trick the person
                    values.add(baseVal + error);
                }
                answer = new BigDecimal(baseVal + error, round).toPlainString() + "V";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);

                choices.add(new BigDecimal((baseVal + error) * values.size(), round).toPlainString() + "V");
                for (int i = 0; i < maxChoices - 2; i++) {
                    double tempError = 0;
                    do {
                        tempError = new BigDecimal(Math.random(), round).doubleValue();
                        if (Math.random() > .5) {
                            tempError *= -1;
                        }
                    } while (tempError == error); // no repeating answers

                    choices.add(new BigDecimal(baseVal + tempError, round).toPlainString() + "v");
                }
                break;
            case 4: // PARALLEL RESISTANCE
                problemType = ProblemType.PARALLEL_RESISTANCE;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += Math.pow(baseVal + error, -1);
                }
                answer = new BigDecimal(Math.pow(numAnswer, -1), round).toPlainString() + "\u03a9";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    numAnswer = Math.pow(numAnswer, -1) - Math.pow(baseVal + error, -1);

                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }
                    numAnswer += Math.pow(baseVal + error, -1);
                    numAnswer = Math.pow(numAnswer, -1);

                    choices.add(new BigDecimal(numAnswer, round).toPlainString() + "\u03a9");
                }
                break;
            case 5: // PARALLEL CURRENT
                problemType = ProblemType.PARALLEL_CURRENT;

                // Randomize values
                baseVal = rng.nextInt(maxNum) + 3;
                for (int i = 0; i < rng.nextInt(5) + 2; i++) {
                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }

                    values.add(baseVal + error);
                    numAnswer += baseVal + error;
                }
                answer = new BigDecimal(numAnswer, round).toPlainString() + "A";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    numAnswer -= baseVal + error;

                    // Adding a margin for the values
                    error = new BigDecimal(Math.random(), round).doubleValue();
                    if (Math.random() > .5) {
                        error *= -1;
                    }
                    numAnswer += baseVal + error;

                    choices.add(new BigDecimal(numAnswer, round).toPlainString() + "A");
                }
                break;
            case 6: // RESISTIVITY
                problemType = ProblemType.RESISTIVITY;

                // Randomize values
                rE = rng.nextInt(maxNum) + 1;
                area = Math.pow(rng.nextInt(maxNum) + 1, 2);
                length = rng.nextInt(maxNum) + 1;
                answer = new BigDecimal((rE * area) / length, round).toPlainString();

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempLength = 0;
                    do {
                        tempLength = rng.nextInt(maxNum) + 1;
                    } while (tempLength == length);

                    choices.add(new BigDecimal((rE * area) / tempLength, round).toPlainString());
                }
                break;
            case 7: // DRIFT VELOCITY
                problemType = ProblemType.DRIFT_VELOCITY;

                // Randomize values
                length = (rng.nextInt(maxNum * 2) + 1);
                time = rng.nextInt(60) + 1;
                answer = new BigDecimal(length / (double) (time * 100), round).toPlainString() + "m/s"; // * 100 for cm
                                                                                                        // -> m

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempTime = 0;
                    do {
                        tempTime = rng.nextInt(60) + 1;
                    } while (tempTime == time);

                    choices.add(new BigDecimal(length / (double) (tempTime * 100), round).toPlainString() + "m/s"); // *
                                                                                                                    // 100
                                                                                                                    // for
                    // cm -> m
                }
                break;
            case 8: // ELECTROMOTIVE FORCE
                problemType = ProblemType.ELECTROMOTIVE;

                // Randomize values
                rI = rng.nextInt(maxNum) + 1;
                rE = rng.nextInt(maxNum) + 1;
                current = rng.nextInt(maxNum) + 1;
                answer = new BigDecimal(current * (rI + rE), round).toPlainString() + "V";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    double tempAnswer = 0;
                    do {
                        int tempRI = rng.nextInt(maxNum) + 1;
                        int tempRE = rng.nextInt(maxNum) + 1;
                        int tempCurrent = rng.nextInt(maxNum) + 1;
                        tempAnswer = tempCurrent * (tempRI + tempRE);
                    } while ((current * (rI + rE)) == tempAnswer);
                    choices.add(new BigDecimal(tempAnswer, round).toPlainString() + "V");
                }
                break;
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case SERIES_VOLTAGE:
                questions = AppConstants.divideLabel("What is the total voltage of the following series circuit? "
                        + values);
                break;
            case SERIES_RESISTANCE:
                questions = AppConstants.divideLabel("What is the total resistance of the following series circuit? "
                        + values);
                break;
            case SERIES_CURRENT:
                questions = AppConstants.divideLabel("What is the total current of the following series circuit? "
                        + values);
                break;
            case PARALLEL_VOLTAGE:
                questions = AppConstants.divideLabel("What is the total voltage of the following parallel circuit? "
                        + values);
                break;
            case PARALLEL_RESISTANCE:
                questions = AppConstants.divideLabel("What is the total resistance of the following parallel circuit? "
                        + values);
                break;
            case PARALLEL_CURRENT:
                questions = AppConstants.divideLabel("What is the total current of the following parallel circuit? "
                        + values);
                break;

            case RESISTIVITY:
                questions = AppConstants
                        .divideLabel("What is the resistivity of a conductor, which has a resistance of " + rE
                                + "\u03a9, a cross sectional area of " + area + ", and  a length of " + length + "?");
                break;
            case DRIFT_VELOCITY:
                questions = AppConstants.divideLabel(
                        "What is the velocity of an electron if it travels " + length + "cm in " + time + " seconds?");
                break;
            case ELECTROMOTIVE:
                questions = AppConstants
                        .divideLabel("What is the electromotive force of a cell if its internal resistance is "
                                + rI + "\u03a9, its external resistance is " + rE
                                + "\u03a9, and the circuit has a current of " + current + "A?");
                break;
        }
    }

    @Override
    public void getHowTo() {
    }

    @Override
    public void customActions(String action) {
        if (action.equals("\u03a9")) {
            response = textField.getText();
            textField.setText(response + "\u03a9");
            textField.requestFocusInWindow();
        }
    }

    @Override
    public void getExtraButtons() {
        extraButtonsArray = new JButton[] { new JButton("\u03a9") };
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Solve the given problem.", "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}