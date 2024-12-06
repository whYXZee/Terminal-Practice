package whyxzee.terminalpractice.scenarios.physics;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.scenarios.ScenarioTools;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Scenarios over work, energy, and power. Available question types
 * include:
 * <ul>
 * <li>Average kinetic energy of particles
 * <li>Stefan-Boltsmann Law
 * <li>Luminosity
 * <li>Appeared brightness
 * <li>Latent heat
 * <li>{@code Coming soon!} Heat capacity
 * <li>Albedo
 * <li>Emissivity
 * <li>{@code Open-only} Constants
 * </ul>
 */
public class ThermalEnergyTransfer extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static final BigDecimal boltzmann = new BigDecimal(1.38 * Math.pow(10, -23));
    private static final double stefanBoltsmann = 5.67 * Math.pow(10, -8);
    // private static final int solar = 1361;

    private static int kelvin = 0;
    private static double emissivity = 0;
    private static double area = 0;
    private static int luminosity = 0;
    private static int distance = 0;
    private static double latentHeat = 0;
    private static int mass = 0;
    private static int radI = 0; // incoming radiation
    private static int radO = 0; // outputed radiation
    private static int power = 0;

    private static HashMap<String, String> constants = new HashMap<String, String>() {
        {
            put("Stefan-Boltsmann Constant", "5.67E-8 Wm^(-2)K^(-4)");
            put("Boltzmann Constant", "1.38E-23 JK^(-1)molecules^(-1)");
            put("solar constant", "1361 Wm^(-2)S^(-1)");
        }
    };

    //
    // Question-related variables
    //
    private ProblemType problemType = ProblemType.AVG_EK;
    private static String question = "";

    private enum ProblemType {
        // Multi and Open
        AVG_EK,
        STEFAN_BOLTSMANN_LAW,
        LUMINOSITY,
        APPEARED_BRIGHTNESS,
        LATENT_HEAT, // for phase changes
        // HEAT_CAPACITY,

        ALBEDO,
        EMISSIVITY,

        // Open-only
        CONSTANTS

        // Multi-only
    }

    public ThermalEnergyTransfer() {
        openRNG = ProblemType.values().length;
        multiRNG = openRNG - 1;
    }

    @Override
    public void randomizeOpen() {
        switch (rng.nextInt(openRNG)) {
            case 0: // AVERAGE KINETIC ENERGY
                problemType = ProblemType.AVG_EK;

                // Randomize values
                kelvin = rng.nextInt(maxNum * 100) + 1;
                answer = new BigDecimal(1.5 * kelvin, round).multiply(boltzmann).round(round) + "J";
                break;
            case 1: // STEFAN BOLTSMANN LAW
                problemType = ProblemType.STEFAN_BOLTSMANN_LAW;

                // Randomize values
                area = Math.pow(rng.nextInt(maxNum * 2) + 1, 2);
                kelvin = rng.nextInt(maxNum * 100) + 1;
                emissivity = (int) (Math.random() * Math.pow(10, ScenarioTools.roundingDigits))
                        / Math.pow(10, ScenarioTools.roundingDigits);
                answer = new BigDecimal(emissivity * stefanBoltsmann * area * Math.pow(kelvin, 4), round)
                        .toPlainString()
                        + "W";
                break;
            case 2: // LUMINOSITY
                problemType = ProblemType.LUMINOSITY;

                // Randomize values
                area = Math.pow(rng.nextInt(maxNum * 2) + 1, 2);
                kelvin = rng.nextInt(maxNum * 100) + 1;
                answer = new BigDecimal(stefanBoltsmann * area * Math.pow(kelvin, 4), round).toPlainString() + "W";
                break;
            case 3: // APPEARED BRIGHTNESS
                problemType = ProblemType.APPEARED_BRIGHTNESS;

                // Randomize values
                luminosity = rng.nextInt(maxNum * 2) + 1;
                distance = rng.nextInt(maxNum * 1000) + 1;
                answer = new BigDecimal(luminosity / (4 * Math.PI * Math.pow(distance, 2)), round).toString();
                break;
            case 4: // LATENT HEAT (phase changes)
                problemType = ProblemType.LATENT_HEAT;

                // Randomize values
                latentHeat = (int) (Math.random() * Math.pow(10, rng.nextInt(3)) * Math.pow(10, roundTo))
                        / Math.pow(10, roundTo);
                if (Math.random() > .5) {
                    latentHeat *= -1;
                }
                mass = rng.nextInt(maxNum) + 1;
                answer = new BigDecimal(mass * latentHeat, round).toPlainString() + "J";
                break;

            case 5: // ALBEDO
                problemType = ProblemType.ALBEDO;

                // Randomize values
                do {
                    radI = rng.nextInt(maxNum * 4) + 1;
                    radO = rng.nextInt(maxNum * 4);
                } while (radO > radI);

                answer = new BigDecimal((radO / (double) radI) * 100, round).toPlainString() + "%";
                break;
            case 6: // EMISSIVITY
                problemType = ProblemType.EMISSIVITY;

                // Randomize values
                power = rng.nextInt(maxNum * 10) + 1;
                kelvin = rng.nextInt(maxNum * 100) + 1;
                answer = new BigDecimal(power / (stefanBoltsmann * kelvin), round).toPlainString();
                break;
            case 7: // CONSTANTS
                problemType = ProblemType.CONSTANTS;

                // Randomize values
                question = new ArrayList<String>(constants.keySet()).get(rng.nextInt(constants.size()));
                answer = constants.get(question);
                break;

        }
    }

    @Override
    public void randomizeMulti() {
        switch (rng.nextInt(multiRNG)) {
            case 0: // AVERAGE KINETIC ENERGY
                problemType = ProblemType.AVG_EK;

                // Randomize values
                kelvin = rng.nextInt(maxNum * 100) + 1;
                answer = new BigDecimal(1.5 * kelvin, round).multiply(boltzmann).round(round) + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempK = 0;
                    do {
                        tempK = rng.nextInt(maxNum * 100) + 1;
                    } while (tempK == kelvin); // no repeating answers
                    choices.add(new BigDecimal(1.5 * tempK, round).multiply(boltzmann).round(round) + "J");
                }
                break;
            case 1: // STEFAN BOLTSMANN LAW
                problemType = ProblemType.STEFAN_BOLTSMANN_LAW;

                // Randomize values
                area = Math.pow(rng.nextInt(maxNum * 2) + 1, 2);
                kelvin = rng.nextInt(maxNum * 100) + 1;
                emissivity = (int) (Math.random() * Math.pow(10, ScenarioTools.roundingDigits))
                        / Math.pow(10, ScenarioTools.roundingDigits);
                answer = new BigDecimal(emissivity * stefanBoltsmann * area * Math.pow(kelvin, 4), round)
                        .toPlainString()
                        + "W";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    double tempE = 0;
                    do {
                        tempE = (int) (Math.random() * Math.pow(10, ScenarioTools.roundingDigits))
                                / Math.pow(10, ScenarioTools.roundingDigits);
                    } while (tempE == emissivity); // no repeat answers

                    choices.add(new BigDecimal(tempE * stefanBoltsmann * area * Math.pow(kelvin, 4), round)
                            .toPlainString() + "W");
                }
                break;
            case 2: // LUMINOSITY
                problemType = ProblemType.LUMINOSITY;

                // Randomize values
                area = Math.pow(rng.nextInt(maxNum * 2) + 1, 2);
                kelvin = rng.nextInt(maxNum * 100) + 1;
                answer = new BigDecimal(stefanBoltsmann * area * Math.pow(kelvin, 4), round).toPlainString() + "W";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    double tempArea = 0;
                    do {
                        tempArea = Math.pow(rng.nextInt(maxNum * 2) + 1, 2);
                    } while (area == tempArea); // no repeats

                    choices.add(new BigDecimal(stefanBoltsmann * tempArea * Math.pow(kelvin, 4), round)
                            .toPlainString() + "W");
                }
                break;
            case 3: // APPEARED BRIGHTNESS
                problemType = ProblemType.APPEARED_BRIGHTNESS;

                // Randomize values
                luminosity = rng.nextInt(maxNum * 2) + 1;
                distance = rng.nextInt(maxNum * 1000) + 1;
                answer = new BigDecimal(luminosity / (4 * Math.PI * Math.pow(distance, 2)), round).toString();

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempL = 0;
                    do {
                        tempL = rng.nextInt(maxNum * 2) + 1;
                    } while (tempL == luminosity); // no repeating answers

                    choices.add(
                            new BigDecimal(tempL / (4 * Math.PI * Math.pow(distance, 2)), round).toString());
                }
                break;
            case 4: // LATENT HEAT (phase changes)
                problemType = ProblemType.LATENT_HEAT;

                // Randomize values
                latentHeat = (int) (Math.random() * Math.pow(10, rng.nextInt(3)) * Math.pow(10, roundTo))
                        / Math.pow(10, roundTo);
                if (Math.random() > .5) {
                    latentHeat *= -1;
                }
                mass = rng.nextInt(maxNum) + 1;
                answer = new BigDecimal(mass * latentHeat, round).toPlainString() + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                choices.add(new BigDecimal(mass / latentHeat, round).toPlainString() + "J");
                for (int i = 0; i < maxChoices - 2; i++) {
                    double tempLH = 0;
                    do {
                        tempLH = (int) (Math.random() * Math.pow(10, rng.nextInt(3)) * Math.pow(10, roundTo))
                                / Math.pow(10, roundTo);
                        if (Math.random() > .5) {
                            latentHeat *= -1;
                        }
                    } while (tempLH == latentHeat); // no repeating answers

                    choices.add(new BigDecimal(mass * tempLH, round).toPlainString() + "J");
                }
                break;
            case 5: // ALBEDO
                problemType = ProblemType.ALBEDO;

                // Randomize values
                do {
                    radI = rng.nextInt(maxNum * 4) + 1;
                    radO = rng.nextInt(maxNum * 4);
                } while (radO > radI);
                answer = new BigDecimal((radO / (double) radI) * 100, round).toPlainString() + "%";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                choices.add(new BigDecimal((radI / (double) radO) * 100, round).toPlainString() + "%");
                for (int i = 0; i < maxChoices - 2; i++) {
                    int tempRadO = 0;
                    do {
                        tempRadO = rng.nextInt(maxNum * 4);
                    } while (tempRadO == radO || tempRadO > radI); // no repeating answeres
                    choices.add(new BigDecimal((tempRadO / (double) radI) * 100, round).toPlainString() + "%");
                }
                break;
            case 6: // EMISSIVITY
                problemType = ProblemType.EMISSIVITY;

                // Randomize values
                power = rng.nextInt(maxNum * 10) + 1;
                kelvin = rng.nextInt(maxNum * 100) + 1;
                answer = new BigDecimal(power / (stefanBoltsmann * kelvin), round).toPlainString();

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempP = 0;
                    do {
                        tempP = rng.nextInt(maxNum * 100) + 1;
                    } while (tempP == power);
                    choices.add(new BigDecimal(tempP / (stefanBoltsmann * kelvin), round).toPlainString());
                }
                break;
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case AVG_EK:
                questions = AppConstants.divideLabel(
                        "What is the average kinetic energy of a set of particles if they have a temperature of "
                                + kelvin + "K? [answer in scientific notation]");
                break;
            case STEFAN_BOLTSMANN_LAW:
                questions = new JLabel[] { new JLabel("What is the energy radiated by a blackbody, if:"),
                        new JLabel("- emissivity is " + emissivity),
                        new JLabel("- the surface area is " + area + "m^(2)"),
                        new JLabel("- its temperature is " + kelvin + "K") };
                break;
            case LUMINOSITY:
                questions = new JLabel[] { new JLabel("What is the luminosity of a blackbody, if:"),
                        new JLabel("- the surface area is " + area + "m^(2)"),
                        new JLabel("- its temperature is " + kelvin + "K") };
                break;
            case APPEARED_BRIGHTNESS:
                questions = AppConstants
                        .divideLabel("What is the appeared brightness of a blackbody if its luminosity is " + luminosity
                                + "W and you are " + distance + "m away? [answer in scientific notation]");
                break;
            case LATENT_HEAT:
                if (latentHeat > 0) {
                    questions = AppConstants
                            .divideLabel("What is the amount of heat absorbed by an object if its mass is " + mass
                                    + "g with a latent heat of " + latentHeat + "J/g?");
                } else {
                    questions = AppConstants
                            .divideLabel("What is the amount of heat released by an object if its mass is " + mass
                                    + "g with a latent heat of " + latentHeat + "J/g?");
                }
                break;
            case ALBEDO:
                questions = AppConstants.divideLabel("What is the albedo of a blackbody who reflects " + radO
                        + "W, when it recieves " + radI + "W?");
                break;
            case EMISSIVITY:
                questions = new JLabel[] { new JLabel("What is the emissivity of a blackbody, if:"),
                        new JLabel("- it emits " + power + "W/m^(2)"),
                        new JLabel("- its temperature is " + kelvin + "K") };
                break;

            case CONSTANTS:
                switch (question) {
                    case "Stefan-Boltsmann Constant":
                        questions = new JLabel[] {
                                new JLabel("In scientific notation, what is the Stefan-Boltsmann Constant?") };
                        break;
                    case "Boltzmann Constant":
                        questions = new JLabel[] {
                                new JLabel("In scientific notation, What is the Boltzmann Constant?") };
                        break;
                    case "solar constant":
                        questions = new JLabel[] { new JLabel("What is the solar constant?") };
                        break;
                }
                break;
        }
    }

    @Override
    public void getHowTo() {
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Solve the given problem over Thermal Energy Transfer.\nConstants:\n- Stefan Boltsmann Constant: "
                        + stefanBoltsmann + " Wm^(-2)K^(-4)\n- Solar Constant: 1361 Wm^(-2)S^(-1)"
                        + "\nUnits:\n- Luminosity and energy are in W (watts)\n- Albedo is a percentage"
                        + "\nRounding:\n- Answers have " + ScenarioTools.roundingDigits + " significant figures",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}