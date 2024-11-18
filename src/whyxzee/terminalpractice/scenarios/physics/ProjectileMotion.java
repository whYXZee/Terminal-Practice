package whyxzee.terminalpractice.scenarios.physics;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Trigonometry;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import java.util.ArrayList;

public class ProjectileMotion extends ScenarioUI {
    //
    // Scenario-specific variables
    //

    // enums to determine problem type
    private static ProblemType problemType = ProblemType.INIT_VH;
    private static GivenVal givenVal = GivenVal.INIT_SIDES;
    private static ArrayList<Integer> givenSides = new ArrayList<Integer>();

    // What part of the motion needs to be found?
    private enum ProblemType {
        INIT_VH, // initial velocity of hypotenuse
        INIT_VX, // initial velocity of X position
        INIT_VY, // initial velocity of Y position
        INIT_THETA_Y, // initial angle Y of the force
        // INIT_THETA_X,
        F_VH, // final velocity of hypotenuse
        F_VX, // final velocity of X position
        F_VY, // final velocity of Y position
        F_THETA, // final angle of the force
        MAX_HEIGHT, // max height of the projectile
        TIME_MAX_HEIGHT, // time of the max height
        TOTAL_TIME, // time of the total x displacement
        TOTAL_X, // total x displacement
    }

    // What values from the motion are given?
    private enum GivenVal {
        INIT_SIDES, // two sides from initial triangle
        INIT_THETAY, // angle of initial triangle + random side
        // F_SIDES, // two sides from final triangle
        // F_THETA // angle of final triangle + random side
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

    public ProjectileMotion() throws InterruptedException {

    }

    @Override
    public void randomize() {
        // Randomizes what is given
        switch (ScenarioConstants.rng.nextInt(2)) {
            case 0:
                givenVal = GivenVal.INIT_THETAY;
                break;
            case 1:
                givenVal = GivenVal.INIT_SIDES;
                break;
        }

        // Randomizes the values based on the problem
        int randomSide;
        do {
            // Resetting the equation
            triangle.updateFromList(nullList);
            givenSides.removeAll(givenSides);

            switch (givenVal) {
                case INIT_THETAY:
                    triangle.thetaY = (float) (ScenarioConstants.rng.nextInt(80) + 1);
                    rngSides(ScenarioConstants.rng.nextInt(3));
                    break;
                case INIT_SIDES:
                    rngSides(ScenarioConstants.rng.nextInt(3));

                    // Avoid randomizing the same side
                    randomSide = ScenarioConstants.rng.nextInt(3);
                    while (triangle.sidePresent(randomSide)) {
                        randomSide = ScenarioConstants.rng.nextInt(3);
                    }
                    rngSides(randomSide);

                    // Rearrange the triangle to follow legal triangle rules
                    triangle.rearrange();
                    break;
                default:
                    break;
            }
            triangle.updateLists();
        } while (!triangle.isLegal90Deg());

        // What the problem wants you to find
        do {
            switch (ScenarioConstants.rng.nextInt(8)) {
                case 0:
                    problemType = ProblemType.INIT_THETA_Y;
                    break;
                case 1:
                    problemType = ProblemType.INIT_VH;
                    break;
                case 2:
                    problemType = ProblemType.INIT_VX;
                    break;
                case 3:
                    problemType = ProblemType.INIT_VY;
                    break;
                case 4:
                    problemType = ProblemType.MAX_HEIGHT;
                    break;
                case 5:
                    problemType = ProblemType.TIME_MAX_HEIGHT;
                    break;
                case 6:
                    problemType = ProblemType.TOTAL_TIME;
                    break;
                case 7:
                    problemType = ProblemType.TOTAL_X;
                    break;
            }
        } while (questionOverlap());
    }

    @Override
    public String solve() {
        triangle.solve90DegTrig();

        switch (problemType) {
            case INIT_THETA_Y:
                return new BigDecimal(triangle.thetaY, Trigonometry.trigRound).toString();
            case INIT_VH:
                return new BigDecimal(triangle.hypotenuse, Trigonometry.trigRound).toString();
            case INIT_VX:
                return new BigDecimal(triangle.x, Trigonometry.trigRound).toString();
            case INIT_VY:
                return new BigDecimal(triangle.y, Trigonometry.trigRound).toString();
            case F_THETA:
                return new BigDecimal(triangle.thetaY, Trigonometry.trigRound).toString();
            case F_VH:
                return new BigDecimal(triangle.hypotenuse, Trigonometry.trigRound).toString();
            case F_VX:
                return new BigDecimal(triangle.x, Trigonometry.trigRound).toString();
            case F_VY:
                return new BigDecimal(-triangle.y, Trigonometry.trigRound).toString();
            case MAX_HEIGHT: // derived from suvat equation v^2 = u^2 + 2as
                return new BigDecimal((Math.pow(triangle.y, 2) / 19.6), Trigonometry.trigRound).toString();
            case TIME_MAX_HEIGHT:
                return new BigDecimal((triangle.y / 9.8), Trigonometry.trigRound).toString();
            case TOTAL_TIME:
                return new BigDecimal(((2 * triangle.y) / 9.8), Trigonometry.trigRound).toString();
            case TOTAL_X:
                return new BigDecimal((triangle.x * ((2 * triangle.y) / 9.8)), Trigonometry.trigRound).toString();
            default:
                return "0";
        }
    }

    /**
     * Gets the projectile motion question.
     */
    @Override
    public void getQuestion() {
        switch (problemType) {
            case INIT_THETA_Y:
                questions = AppConstants.divideLabel(triangle + "; What is the initial theta?");
                break;
            case INIT_VH:
                questions = AppConstants.divideLabel(triangle + "; What is the initial velocity?");
                break;
            case INIT_VX:
                questions = AppConstants.divideLabel(triangle + "; What is the initial velocity of the X position?");
                break;
            case INIT_VY:
                questions = AppConstants.divideLabel(triangle + "; What is the initial velocity of the Y position?");
                break;
            case F_THETA:
                questions = AppConstants.divideLabel(triangle + "; What is the final theta?");
                break;
            case F_VH:
                questions = AppConstants.divideLabel(triangle + "; What is the final velocity?");
                break;
            case F_VX:
                questions = AppConstants.divideLabel(triangle + "; What is the final velocity of the X position?");
                break;
            case F_VY:
                questions = AppConstants.divideLabel(triangle + "; What is the final velocity of the Y position?");
                break;
            case MAX_HEIGHT:
                questions = AppConstants.divideLabel(triangle + "; What is the max height of the projectile?");
                break;
            case TIME_MAX_HEIGHT:
                questions = AppConstants
                        .divideLabel(triangle + "; What is the time that the projectile is at its max height?");
                break;
            case TOTAL_TIME:
                questions = AppConstants.divideLabel(triangle + "; What time does the projectile land?");
                break;
            case TOTAL_X:
                questions = AppConstants.divideLabel(triangle + "; What is the total X displacement?");
                break;
            default:
                break;
        }
    }

    @Override
    public void getHowTo() {
        switch (problemType) {
            case INIT_THETA_Y:
                switch (givenVal) {
                    case INIT_SIDES:
                        if (!givenSides.contains(0)) {
                            howToLabels = AppConstants
                                    .divideLabel("The initial theta can be found using right triangle trigonometry " +
                                            "(SOH CAH TOA). In this case, we would use SOH (sine = opposite/hypotenuse) as "
                                            + "the x-side is missing: sin(\u03b8) = (" + triangle.y + "/"
                                            + triangle.hypotenuse +
                                            "). Then, solve for theta by taking the inverse of sine: \u03b8 = "
                                            + "sine inverse (" + triangle.y + "/" + triangle.hypotenuse + ").");
                        } else if (!givenSides.contains(1)) {
                            howToLabels = AppConstants
                                    .divideLabel("The initial theta can be found using right triangle trigonometry " +
                                            "(SOH CAH TOA). In this case, we would use CAH (cosine = adjacent/hypotenuse) as "
                                            + "the y-side is missing: cos(\u03b8) = (" + triangle.x + "/"
                                            + triangle.hypotenuse
                                            + "). Then, solve for theta by taking the inverse of cosine: \u03b8 = "
                                            + "cosine inverse (" + triangle.x + "/" + triangle.hypotenuse + ").");
                        } else {
                            howToLabels = AppConstants
                                    .divideLabel("The initial theta can be found using right triangle trigonometry " +
                                            "(SOH CAH TOA). In this case, we would use TOA (tangent = opposite/adjacent) as "
                                            + "the hypotenuse is missing: tan(\u03b8) = (" + triangle.x + "/"
                                            + triangle.y
                                            + "). Then, solve for theta by taking the inverse of tangent: \u03b8 = "
                                            + "tangent " + "inverse (" + triangle.x + "/" + triangle.y + ").");
                        }
                    default:
                        break;
                }
                break;
            case INIT_VH:
                switch (givenVal) {
                    case INIT_SIDES:
                        howToLabels = AppConstants.divideLabel("The initial velocity of a projectile is the hypotenuse "
                                + "of the initial triangle. In this case, we would use pythagorean theorem [a^(2) + b^(2) = c^(2)] "
                                + "to find the hypotenuse. " + triangle.x + "^(2) + " + triangle.y + "^(2) = "
                                + triangle.hypotenuse + "^(2).");
                        break;
                    case INIT_THETAY:
                        if (!givenSides.contains(0)) {
                            howToLabels = AppConstants.divideLabel("The initial velocity of a projectile is " +
                                    "the hypotenuse of the initial triangle. Due to the given values, we "
                                    + "would use right triangle trigonometry (SOH CAH TOA) to find the "
                                    + "hypotenuse. In this case, we would use SOH (sine = opposite/"
                                    + "hypotenuse) as the y-side is present: sin(" + triangle.thetaY +
                                    ") = (" + triangle.y + "/ hypotenuse). Then, solve for the "
                                    + "missing side: hypotenuse = " + triangle.y + "/sin(" + triangle.thetaY + ")");
                        } else {
                            howToLabels = AppConstants.divideLabel("The initial velocity of a projectile is " +
                                    "the hypotenuse of the initial triangle. Due to the given values, we "
                                    + "would use right triangle trigonometry (SOH CAH TOA) to find the "
                                    + "hypotenuse. In this case, we would use CAH (cosine = opposite/"
                                    + "hypotenuse) as the y-side is present: cos(" + triangle.thetaY +
                                    ") = (" + triangle.x + "/ hypotenuse). Then, solve for the "
                                    + "missing side: hypotenuse = " + triangle.x + "/cos(" + triangle.thetaY + ")");
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame, "Solve the given projectile motion problem.",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Makes sure that what information is given isn't the information being asked.
     */
    private static boolean questionOverlap() {
        switch (givenVal) {
            case INIT_THETAY: // Checks that the theta isn't being asked when given
                switch (problemType) {
                    case INIT_THETA_Y:
                        return true;
                    case F_THETA:
                        return true;
                    default:
                        break;
                }
            case INIT_SIDES: // Checks that the sides aren't present
                switch (problemType) {
                    case INIT_VH:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case INIT_VX:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case INIT_VY:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
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

    /**
     * Randomizes the sides
     * 
     * @param side number of the side, with the following key:
     *             <ul>
     *             <li>0: side X
     *             <li>1: side Y
     *             <li>2: Hypotenuse
     */
    private static void rngSides(int side) {
        givenSides.add(side);
        if (side == 0) {
            triangle.x = (float) (ScenarioConstants.rng.nextInt(50) + 1);
        } else if (side == 1) {
            triangle.y = (float) (ScenarioConstants.rng.nextInt(50) + 1);
        } else {
            triangle.hypotenuse = (float) (ScenarioConstants.rng.nextInt(50) + 1);
        }
    }
}
