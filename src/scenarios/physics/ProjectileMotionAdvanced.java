package scenarios.physics;

import java.math.BigDecimal;
import java.util.ArrayList;

import application.Scenario;
import whyxzee.terminalpractice.application.RunApplication;
import whyxzee.terminalpractice.resources.Trigonometry;

public class ProjectileMotionAdvanced extends Scenario {
    // enums to determine problem type
    private static ProblemType problemType = ProblemType.NONE;
    private static GivenVal givenVal = GivenVal.NONE;
    private static int ledgeHeight = 0;

    private enum ProblemType { // What needs to be found?
        NONE,
        F_VH, // final velocity of hypotenuse
        F_VX, // final velocity of X position
        F_VY, // final velocity of Y position
        F_THETA, // final angle of the force
        MAX_HEIGHT, // max height of the projectile
        TIME_MAX_HEIGHT, // time of the max height
        TIME_TOTAL_X, // time of the total x displacement
        TOTAL_X, // total x displacement
    }

    private enum GivenVal { // What values are given?
        NONE,
        INIT_SIDES, // two sides from initial triangle
        INIT_THETA, // angle of initial triangle + random side
        F_SIDES, // two sides from final triangle
        F_THETA // angle of final triangle + random side
    }

    // To track between problems
    private static Trigonometry triangle = new Trigonometry(new Float[] { null, null, null, null, null, null });
    private static ArrayList<Float> nullList = new ArrayList<Float>() {
        {
            add(null);
            add(null);
            add(null);
            add(null);
            add(null);
            add(null);
        }
    };

    @Override
    public void runScenario(int goal, boolean buffer) throws InterruptedException {
        int correct = 0;
        String input;
        for (int i = 0; i < goal; i++) {
            System.out.println("Question " + (i + 1) + "/" + goal);
            randomize();
            if (buffer) {
                buffer = false;
                input = RunApplication.IO.nextLine();
            }

            // Printing the problem
            System.out.println(triangle);
            printQuestion();
            BigDecimal answer = solve();
            System.out.println(answer);
            input = RunApplication.IO.nextLine();
            try {
                BigDecimal response = new BigDecimal(Float.valueOf(input), Trigonometry.trigRound);
                if (answer.equals(response)) {
                    System.out.println("Correct! Moving on:");
                    correct++;
                } else {
                    System.out.println("Incorrect! The correct answer was: " + answer);
                }
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Error translating to float. The answer was: " + answer);
            }
            if (input.equals("stop")) {
                break;
            }

            Thread.sleep(2000);
            // Resets the problem
            givenVal = GivenVal.NONE;
            problemType = ProblemType.NONE;
            triangle.updateFromList(nullList);
        }
        System.out.println("Congratuations, you got " + correct + " correct!");

    }

    private static void randomize() {
        // What is given
        int rand = Scenario.rng.nextInt(2);
        switch (rand) {
            case 0:
                givenVal = GivenVal.INIT_THETA;
                break;
            case 1:
                givenVal = GivenVal.INIT_SIDES;
                break;
        }
        // randomizes values based on the problem
        int randomSide;
        switch (givenVal) {
            case INIT_THETA:
                triangle.thetaY = (float) (Scenario.rng.nextInt(80) + 1);
                rngSides(Scenario.rng.nextInt(3) + 1);
                break;
            case INIT_SIDES:
                rngSides(Scenario.rng.nextInt(3) + 1);
                randomSide = Scenario.rng.nextInt(3) + 1;
                while (triangle.sidePresent(randomSide)) {
                    randomSide = Scenario.rng.nextInt(3) + 1;
                }
                rngSides(randomSide);
                triangle.rearrange();
                break;
            default:
                break;
        }

        // What to find
        do {
            rand = Scenario.rng.nextInt(8);
            switch (rand) {
                case 4:
                    problemType = ProblemType.MAX_HEIGHT;
                    break;
                case 5:
                    problemType = ProblemType.TIME_MAX_HEIGHT;
                    break;
                case 6:
                    problemType = ProblemType.TIME_TOTAL_X;
                    break;
                case 7:
                    problemType = ProblemType.TOTAL_X;
                    break;
            }
        } while (questionOverlap());
    }

    private static void printQuestion() {
        if (ledgeHeight > 0) {
            System.out.println("Height of the ledge: " + ledgeHeight);
        } else if (ledgeHeight < 0) {
            System.out.println("Height of the ridge: " + -ledgeHeight);
        }
        switch (problemType) {
            case F_THETA:
                System.out.println("What is the final theta?");
                break;
            case F_VH:
                System.out.println("What is the final velocity?");
                break;
            case F_VX:
                System.out.println("What is the final velocity of the X position?");
                break;
            case F_VY:
                System.out.println("What is the final velocity of the Y position?");
                break;
            case MAX_HEIGHT:
                System.out.println("What is the max height of the projectile?");
                break;
            case TIME_MAX_HEIGHT:
                System.out.println("What is the time that the projectile is at its max height?");
                break;
            case TIME_TOTAL_X:
                System.out.println("What time does the projectile land?");
                break;
            case TOTAL_X:
                System.out.println("What is the total X displacement?");
                break;
            default:
                break;
        }
    }

    // Checks that what is given isn't what is being asked of
    private static boolean questionOverlap() {
        switch (givenVal) {
            case INIT_THETA: // Checks that the theta isn't being asked when given
                switch (problemType) {
                    case F_THETA:
                        return true;
                    default:
                        break;
                }
            case INIT_SIDES: // Checks that the sides aren't present
                switch (problemType) {
                    case F_VH:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case F_VX:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case F_VY:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            default:
                break;
        }
        return false;
    }

    private static BigDecimal solve() {
        triangle.solve90DegTrig();
        switch (problemType) {
            case F_THETA:
                return new BigDecimal(triangle.thetaY, Trigonometry.trigRound);
            case F_VH:
                return new BigDecimal(triangle.hypotenuse, Trigonometry.trigRound);
            case F_VX:
                return new BigDecimal(triangle.x, Trigonometry.trigRound);
            case F_VY:
                return new BigDecimal(-triangle.y, Trigonometry.trigRound);
            case MAX_HEIGHT: // derived from suvat equation v^2 = u^2 + 2as
                return new BigDecimal((Math.pow(triangle.y, 2) / 19.6), Trigonometry.trigRound);
            case TIME_MAX_HEIGHT:
                return new BigDecimal((triangle.y / 9.8), Trigonometry.trigRound);
            case TIME_TOTAL_X:
                return new BigDecimal(((2 * triangle.y) / 9.8), Trigonometry.trigRound);
            case TOTAL_X:
                return new BigDecimal((triangle.x * ((2 * triangle.y) / 9.8)), Trigonometry.trigRound);
            default:
                return new BigDecimal(0);
        }
    }

    // Randomizes the sides
    private static void rngSides(int side) {
        if (side == 1) {
            triangle.x = (float) (Scenario.rng.nextInt(50) + 1);
        } else if (side == 2) {
            triangle.y = (float) (Scenario.rng.nextInt(50) + 1);
        } else {
            triangle.hypotenuse = (float) (Scenario.rng.nextInt(50) + 1);
        }
    }
}