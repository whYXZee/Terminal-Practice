package whyxzee.terminalpractice.scenarios.physics;

import java.math.BigDecimal;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

public class WorkEnergyPower extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static ProblemType problemType = ProblemType.KINETIC;

    // Kinetic Energy
    private static int mass = 0;
    private static int velocity = 0;

    // Potential Energy
    private static double gravity = 9.8;
    private static int displacement = 0;
    private static double springCoefficient = 0;

    // Work
    private static int force = 0;
    private static int angle = 0;
    private static int pathLength = 0;

    // Efficiency
    private static int eInput = 0;
    private static int usefulOutput = 0;

    // Power
    private static int work = 0;
    private static int seconds = 0;

    private enum ProblemType {
        KINETIC,
        POTENTIAL_GRAV,
        POTENTIAL_ELASTIC,
        // CONSERVATION_OF_ENERGY,

        WORK,
        ANGLED_WORK,
        CURVED_WORK,

        EFFICIENCY,
        POWER
    }

    public WorkEnergyPower() throws InterruptedException {
    }

    @Override
    public void randomize() {
        displacement = rng.nextInt(25) + 1;

        switch (rng.nextInt(7)) {
            case 0: // Kinetic Energy
                problemType = ProblemType.KINETIC;
                mass = rng.nextInt(50) + 1;
                velocity = rng.nextInt(50) + 1;
                break;
            case 1: // Gravitational Potential Energy
                problemType = ProblemType.POTENTIAL_GRAV;
                mass = rng.nextInt(50) + 1;
                velocity = rng.nextInt(50) + 1;
                break;
            case 2: // Elastic Potential Energy
                problemType = ProblemType.POTENTIAL_ELASTIC;
                do {
                    springCoefficient = (int) (Math.random() * 100) / 100.0;
                } while (springCoefficient == 0);
                break;
            case 3: // Work
                problemType = ProblemType.WORK;
                force = rng.nextInt(25) + 1;
                break;
            case 4: // Work w/ angles
                problemType = ProblemType.ANGLED_WORK;
                force = rng.nextInt(25) + 1;
                angle = rng.nextInt(180) + 1;
                break;
            case 5: // Work along a curve
                problemType = ProblemType.CURVED_WORK;
                force = rng.nextInt(25) + 1;
                pathLength = rng.nextInt(100) + 1;
                break;
            case 6: // Efficiency
                problemType = ProblemType.EFFICIENCY;
                eInput = rng.nextInt(50) + 25;
                do {
                    // prevents 100% efficiency
                    usefulOutput = rng.nextInt(eInput) + 1;
                } while (usefulOutput == rng.nextInt(eInput));
                break;
            case 7:
                problemType = ProblemType.POWER;
                work = rng.nextInt(200) + 1;
                seconds = rng.nextInt(99) + 2;
                break;

        }

    }

    @Override
    public String solve() {
        switch (problemType) {
            case KINETIC:
                return new BigDecimal(.5 * mass * Math.pow(velocity, 2), round).toString();
            case POTENTIAL_GRAV:
                return new BigDecimal(mass * gravity * displacement, round).toString();
            case POTENTIAL_ELASTIC:
                return new BigDecimal(.5 * springCoefficient * Math.pow(displacement, 2), round).toString();
            case WORK:
                return new BigDecimal(force * displacement, round).toString();
            case ANGLED_WORK:
                return new BigDecimal(force * Math.cos(angle) * displacement, round).toString();
            case CURVED_WORK:
                return new BigDecimal(force * pathLength).toString();
            case EFFICIENCY:
                return new BigDecimal((float) (usefulOutput / eInput) * 100, round).toString() + "%";
            case POWER:
                return new BigDecimal((float) work / seconds, round).toString();
            default:
                return "";
        }

    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case KINETIC:
                questions = AppConstants.divideLabel("What is the kinetic energy of an object "
                        + "with the mass of " + mass + " kg if it moves " + velocity + " m/s?");
                break;
            case POTENTIAL_GRAV:
                questions = AppConstants.divideLabel("What is the potential energy of an object "
                        + "with the mass of " + mass + " kg if it is " + displacement + " m above the ground?");
                break;
            case POTENTIAL_ELASTIC:
                questions = AppConstants.divideLabel("What is the potential energy of an object "
                        + "on a string with a spring coefficient of " + springCoefficient + " stretched " + displacement
                        + " m?");
                break;
            case WORK:
                questions = AppConstants.divideLabel("What is the amount of work of an object if it moves "
                        + displacement + " m at a force of " + force + " N?");
                break;
            case ANGLED_WORK:
                questions = AppConstants.divideLabel("What is the amount of work of an object if it moves "
                        + displacement + " m at a force of " + force + " N at a " + angle + "\u00b0 angle?");
                break;
            case CURVED_WORK:
                questions = AppConstants.divideLabel("What is the amount of work of an object if it moves along a "
                        + pathLength + " m path at a force of " + force + " N?");
                break;
            case EFFICIENCY:
                questions = AppConstants.divideLabel("What is the efficiency of a system if it has "
                        + "an energy input of " + eInput + " J and a useful output of " + usefulOutput + " J? "
                        + "[answer in terms of #%]");
                break;
            case POWER:
                questions = AppConstants.divideLabel("What is the power of a system if it generates "
                        + work + " J every " + seconds + "seconds?");
                break;
        }

    }

    @Override
    public void getHowTo() {

    }

    @Override
    public void printInfo() {

    }

}
