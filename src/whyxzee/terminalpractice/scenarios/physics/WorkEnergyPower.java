package whyxzee.terminalpractice.scenarios.physics;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.scenarios.ScenarioTools;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

/**
 * Scenarios over work, energy, and power. Available question types
 * include:
 * <ul>
 * <li>Finding kinetic energy
 * <li>Finding gravitational potential energy
 * <li>Finding work
 * <li>Finding angled work
 * <li>Finding work along a curve
 * <li>Finding efficiency
 * <li>Finding power
 * <li>Converting change in kinetic energy to work
 * <li>Finding the gain of kinetic energy from the loss of potential energy
 * <li>{@code Open-only} Vocabulary
 * 
 */
public class WorkEnergyPower extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static double mass = 0;
    private static int velocity = 0;
    private static double gravity = 9.8; // ib does 9.8
    private static int displacement = 0;
    private static double springConstant = 0;

    // Work
    private static int force = 0;
    private static int angle = 0;

    // Efficiency
    private static int usefulOutput = 0;
    private static int totalInput = 0;

    // Power
    private static int work = 0;
    private static int seconds = 0;

    private static int startKinetic = 0;
    private static int endKinetic = 0;

    // Delta Potential to Delta Kinetic
    private static int xI = 0;
    private static int xF = 0;
    private static double initPotential = 0;
    private static double finalPotential = 0;

    // Unit Conversion
    private static HashMap<String, String> units = new HashMap<String, String>() {
        {
            put("1Nm", "1J");
            put("1W", "1J");
            put("1kWh", "3600000J");
        }
    };
    private static String question = "";

    // Vocab
    private static String[] vocab = { "Work", "Energy", "Power", "Potential Energy", "Kinetic Energy" };

    //
    // Question-related variables
    //
    private ProblemType problemType = ProblemType.KINETIC;

    // private enum ProblemOpen {
    // KINETIC,
    // }

    // private enum ProblemMulti {
    // KINETIC
    // }

    private enum ProblemType {
        // Multi and open
        KINETIC,
        POTENTIAL_GRAV,
        POTENTIAL_ELASTIC,
        WORK,
        ANGLED_WORK,
        CURVED_WORK,
        EFFICIENCY,
        POWER,

        DELTA_KINETIC_TO_WORK,
        POTENTIAL_TO_KINETIC,
        // FORCE_DISTANCE_TO_WORK // graph

        // Open-only
        UNIT_CONVERSIONS,

        // Multi-only
        VOCAB
    }

    public WorkEnergyPower() {
        openRNG = multiRNG = ProblemType.values().length - 1;
        // multiRNG = openRNG;
        System.out.println(openRNG);
    }

    @Override
    public void randomizeOpen() {
        switch (rng.nextInt(openRNG)) {
            case 0: // KINETIC
                problemType = ProblemType.KINETIC;

                // Randomize values
                mass = rng.nextInt(maxNum * 2) + 1;
                velocity = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(.5 * mass * Math.pow(velocity, 2), round).toPlainString() + "J";
                break;
            case 1: // POTENTIAL GRAV
                problemType = ProblemType.POTENTIAL_GRAV;

                // Randomize values
                mass = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(mass * gravity * displacement, round).toPlainString() + "J";
                break;
            case 2: // POTENTIAL ELASTIC
                problemType = ProblemType.POTENTIAL_ELASTIC;

                // Randomize values
                springConstant = (int) (Math.random() * Math.pow(10, ScenarioTools.roundingDigits))
                        / Math.pow(10, ScenarioTools.roundingDigits);
                displacement = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(.5 * springConstant * Math.pow(displacement, 2), round).toPlainString() + "J";
                break;
            case 3: // WORK
                problemType = ProblemType.WORK;

                // Randomize values
                force = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(force * displacement, round).toPlainString() + "J";
                break;
            case 4: // ANGLED WORK
                problemType = ProblemType.ANGLED_WORK;

                // Randomize values
                force = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 2) + 1;
                angle = rng.nextInt(181); // 0 - 180
                answer = new BigDecimal(force * Math.cos(Math.toRadians(angle)) * displacement, round).toPlainString()
                        + "J";
                break;
            case 5: // CURVED WORK
                problemType = ProblemType.CURVED_WORK;

                // Randomize values
                force = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 4) + 1;
                answer = new BigDecimal(force * displacement, round).toPlainString() + "J";
                break;
            case 6: // EFFICIENCY
                problemType = ProblemType.EFFICIENCY;

                // Randomize values
                do {
                    totalInput = rng.nextInt(maxNum * 10) + 1;
                    usefulOutput = rng.nextInt(maxNum * 10) + 1;
                } while (totalInput < usefulOutput);
                answer = new BigDecimal(((double) usefulOutput / totalInput) * 100, round).toPlainString() + "%";
                break;
            case 7: // POWER
                problemType = ProblemType.POWER;

                // Randomize values
                work = rng.nextInt(maxNum * 2) + 1;
                seconds = rng.nextInt(60) + 1;
                answer = new BigDecimal(work / (double) seconds, round).toPlainString() + "J/s";
                break;
            case 8: // DELTA KINETIC TO WORK
                problemType = ProblemType.DELTA_KINETIC_TO_WORK;

                // Randomize values
                startKinetic = rng.nextInt(maxNum * 4) + 1;
                endKinetic = rng.nextInt(maxNum * 4) + 1;
                answer = Integer.toString(endKinetic - startKinetic) + "J";
                break;
            case 9: // POTENTIAL TO KINETIC
                problemType = ProblemType.POTENTIAL_TO_KINETIC;

                // Randomize values
                do {
                    xI = rng.nextInt(maxNum * 4) + 1;
                    xF = rng.nextInt(maxNum * 4) + 1;
                } while (xI <= xF);
                initPotential = rng.nextInt(maxNum * 10) + 1;

                // Find missing values
                mass = initPotential / (xI * gravity);
                finalPotential = mass * gravity * xF;
                answer = new BigDecimal(-(finalPotential - initPotential), round).toPlainString() + "J";
                break;
            case 10:
                problemType = ProblemType.UNIT_CONVERSIONS;

                // Randomize values
                question = new ArrayList<String>(units.keySet()).get(rng.nextInt(units.size()));
                answer = units.get(question);
                break;
        }
    }

    @Override
    public void randomizeMulti() {
        switch (rng.nextInt(multiRNG)) {
            case 0: // KINETIC
                problemType = ProblemType.KINETIC;

                // Randomize values
                mass = rng.nextInt(maxNum * 2) + 1;
                velocity = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(.5 * mass * Math.pow(velocity, 2), round).toPlainString() + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempVelocity = 0;
                    do {
                        tempVelocity = rng.nextInt(maxNum * 2) + 1;
                    } while (tempVelocity == velocity); // no repeating answers

                    choices.add(new BigDecimal(.5 * mass * Math.pow(tempVelocity, 2), round).toPlainString() + "J");
                }
                break;

            case 1: // POTENTIAL GRAV
                problemType = ProblemType.POTENTIAL_GRAV;

                // Randomize values
                mass = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(mass * gravity * displacement, round).toPlainString() + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempDisplacement = 0;
                    do {
                        tempDisplacement = rng.nextInt(maxNum * 2) + 1;
                    } while (tempDisplacement == displacement); // no repeating answers

                    choices.add(new BigDecimal(mass * gravity * tempDisplacement, round).toPlainString() + "J");
                }
                break;
            case 2: // POTENTIAL ELASTIC
                problemType = ProblemType.POTENTIAL_ELASTIC;

                // Randomize values
                springConstant = (int) (Math.random() * Math.pow(10, ScenarioTools.roundingDigits))
                        / Math.pow(10, ScenarioTools.roundingDigits);
                displacement = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(.5 * springConstant * Math.pow(displacement, 2), round).toPlainString() + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    double tempSpringConstant = 0;
                    do {
                        tempSpringConstant = (int) (Math.random() * Math.pow(10, ScenarioTools.roundingDigits))
                                / Math.pow(10, ScenarioTools.roundingDigits);
                    } while (tempSpringConstant == springConstant);

                    choices.add(new BigDecimal(.5 * tempSpringConstant * Math.pow(displacement, 2), round)
                            .toPlainString() + "J");
                }
                break;
            case 3: // WORK
                problemType = ProblemType.WORK;

                // Randomize values
                force = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 2) + 1;
                answer = new BigDecimal(force * displacement, round).toPlainString() + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    double tempForce = rng.nextInt(maxNum * 2) + 1;
                    int tempDisplacement = rng.nextInt(maxNum * 2) + 1;

                    choices.add(new BigDecimal(tempForce * tempDisplacement, round).toPlainString() + "J");
                }
                break;
            case 4: // ANGLED WORK
                problemType = ProblemType.ANGLED_WORK;

                // Randomize values
                force = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 2) + 1;
                angle = rng.nextInt(181); // 0 - 180
                answer = new BigDecimal(force * Math.cos(Math.toRadians(angle)) * displacement, round).toPlainString()
                        + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    double tempForce = rng.nextInt(maxNum * 2) + 1;
                    int tempDisplacement = rng.nextInt(maxNum * 2) + 1;

                    choices.add(new BigDecimal(tempForce * Math.cos(Math.toRadians(angle)) * tempDisplacement, round)
                            .toPlainString() + "J");
                }
                break;
            case 5: // CURVED WORK
                problemType = ProblemType.CURVED_WORK;

                // Randomize values
                force = rng.nextInt(maxNum * 2) + 1;
                displacement = rng.nextInt(maxNum * 4) + 1;
                answer = new BigDecimal(force * displacement, round).toPlainString() + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    double tempForce = rng.nextInt(maxNum * 2) + 1;
                    int tempDisplacement = rng.nextInt(maxNum * 4) + 1;

                    choices.add(new BigDecimal(tempForce * tempDisplacement, round).toPlainString() + "J");
                }
                break;
            case 6: // EFFICIENCY
                problemType = ProblemType.EFFICIENCY;

                // Randomize values
                do {
                    totalInput = rng.nextInt(maxNum * 10) + 1;
                    usefulOutput = rng.nextInt(maxNum * 10) + 1;
                } while (totalInput < usefulOutput);
                answer = new BigDecimal(((double) usefulOutput / totalInput) * 100, round).toPlainString() + "%";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                choices.add(new BigDecimal(((double) totalInput / usefulOutput) * 100, round).toPlainString() + "%");
                choices.add(new BigDecimal(totalInput * usefulOutput, round).toPlainString() + "%");
                for (int i = 0; i < maxChoices - 3; i++) {
                    double tempOut = 0;
                    do {
                        tempOut = rng.nextInt(maxNum * 10) + 1;
                    } while (totalInput < tempOut || tempOut == usefulOutput);
                    choices.add(new BigDecimal(((double) tempOut / totalInput) * 100, round).toPlainString() + "%");
                }
                break;
            case 7: // POWER
                problemType = ProblemType.POWER;

                // Randomize values
                work = rng.nextInt(maxNum * 2) + 1;
                seconds = rng.nextInt(60) + 1;
                answer = new BigDecimal(work / (double) seconds, round).toPlainString() + "J/s";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempWork = rng.nextInt(maxNum * 2) + 1;
                    int tempSeconds = rng.nextInt(60) + 1;

                    choices.add(new BigDecimal(tempWork / (double) tempSeconds, round).toPlainString() + "J/s");
                }
                break;
            case 8: // DELTA KINETIC TO WORK
                problemType = ProblemType.DELTA_KINETIC_TO_WORK;

                // Randomize values
                startKinetic = rng.nextInt(maxNum * 4) + 1;
                endKinetic = rng.nextInt(maxNum * 4) + 1;
                answer = Integer.toString(endKinetic - startKinetic) + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempStartKinetic = rng.nextInt(maxNum * 4) + 1;
                    int tempEndKinetic = rng.nextInt(maxNum * 4) + 1;

                    choices.add(Integer.toString(tempEndKinetic - tempStartKinetic) + "J");
                }
                break;
            case 9: // POTENTIAL TO KINETIC
                problemType = ProblemType.POTENTIAL_TO_KINETIC;

                // Randomize values
                do {
                    xI = rng.nextInt(maxNum * 4) + 1;
                    xF = rng.nextInt(maxNum * 4) + 1;
                } while (xI <= xF);
                initPotential = rng.nextInt(maxNum * 10) + 1;

                // Find missing values
                mass = initPotential / (xI * gravity);
                finalPotential = mass * gravity * xF;
                answer = new BigDecimal(-(finalPotential - initPotential), round).toPlainString() + "J";

                // Answers
                choices.add(answer);
                correctChoices.add(answer);
                for (int i = 0; i < maxChoices - 1; i++) {
                    int tempXI = 0;
                    int tempXF = 0;
                    do {
                        tempXI = rng.nextInt(maxNum * 4) + 1;
                        tempXF = rng.nextInt(maxNum * 4) + 1;
                    } while (tempXI <= tempXF);
                    int tempEip = rng.nextInt(maxNum * 10) + 1;

                    // Find missing values
                    double tempMass = tempEip / (tempXI * gravity);
                    double tempEfp = tempMass * gravity * tempXF;

                    choices.add(new BigDecimal(-(tempEfp - tempEip), round).toPlainString() + "J");
                }
                break;

            case 10: // VOCAB (EXCLUSIVE)
                problemType = ProblemType.VOCAB;

                answer = vocab[rng.nextInt(vocab.length)];
                correctChoices.add(answer);
                for (String i : vocab) {
                    choices.add(i);
                }
                break;
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case KINETIC:
                questions = AppConstants.divideLabel("What is the kinetic energy of an object with a mass of " + mass
                        + "g at a velocity of " + velocity + "m/s?");
                break;
            case POTENTIAL_GRAV:
                questions = AppConstants.divideLabel("What is the potential energy of an object with a mass of " + mass
                        + "g with a height of " + displacement + "m?");
                break;
            case POTENTIAL_ELASTIC:
                questions = AppConstants.divideLabel(
                        "What is the potential energy of a spring with a spring constant of " + springConstant
                                + " with a displacement of " + displacement + "m?");
                break;
            case WORK:
                questions = AppConstants.divideLabel("What is the amount of work done on a body if a " + force
                        + "N force is applied, with a displacement of " + displacement + "m?");
                break;
            case ANGLED_WORK:
                questions = AppConstants.divideLabel("What is the amount of work done on a body if a " + force
                        + "N force is applied at a " + angle + "\u00b0 angle, with a displacement of " + displacement
                        + "m?");
                break;
            case CURVED_WORK:
                questions = AppConstants.divideLabel("What is the amount of work done on a body if a " + force
                        + "N force is applied on a curved path, with a displacement of " + displacement + "m?");
                break;
            case EFFICIENCY:
                questions = AppConstants
                        .divideLabel("What is the efficiency of a system if it has an input of " + totalInput
                                + "J and a useful output of " + usefulOutput + "J?");
                break;
            case POWER:
                questions = AppConstants.divideLabel(
                        "What is the power of an object doing " + work + "J of work after " + seconds + "s?");
                break;
            case DELTA_KINETIC_TO_WORK:
                questions = AppConstants.divideLabel(
                        "Given the starting kinetic energy of " + startKinetic + "J and an end kinetic energy of "
                                + endKinetic + "J, what is the amount of work of the object?");
                break;
            case POTENTIAL_TO_KINETIC:
                questions = AppConstants.divideLabel("An object is " + xI + "m away from the ground, and has "
                        + initPotential
                        + "J of potential energy there. How much kinetic energy is gained if the ball moves to be " + xF
                        + "m away from the ground?");
                // can do as well: (xI-xF)/xI * initPotential
                break;

            case UNIT_CONVERSIONS:
                questions = AppConstants.divideLabel("What is " + question + " equivalent to?");
                break;
            case VOCAB:
                switch (answer) {
                    case "Work":
                        questions = AppConstants.divideLabel("What is the transfer of energy between two bodies?");
                        break;
                    case "Energy":
                        questions = AppConstants.divideLabel("What is the ability to do work?");
                        break;
                    case "Power":
                        questions = AppConstants.divideLabel("What is the rate at which work is done?");
                        break;
                    case "Potential Energy":
                        questions = AppConstants.divideLabel("What is the energy a body gains from its position?");
                        break;
                    case "Kinetic Energy":
                        questions = AppConstants.divideLabel("What is the energy a body has due to its motion?");
                        break;
                }

        }
    }

    @Override
    public void getHowTo() {
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Solve the given integral.\nConstants:\n- Gravity Constant: " + gravity
                        + " m/s^2\nUnits:\n- Work and energy is in J (joules)\n- Efficiency is a percentage\n- Power is in J/s (joules per second)",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}