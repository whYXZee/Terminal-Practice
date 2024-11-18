package whyxzee.terminalpractice.scenarios.geometry;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Trigonometry;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import java.util.ArrayList;

public class RightTrig extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static Trigonometry triangle = new Trigonometry(Trigonometry.nullArray);
    private static ArrayList<Integer> givenSides = new ArrayList<Integer>();

    // enums to determine problem type
    private static ProblemType problemType = ProblemType.HYPOTENUSE;
    private static GivenVal givenVal = GivenVal.SIDES;

    // What part of the triangle needs to be found?
    private enum ProblemType {
        HYPOTENUSE,
        X_SIDE,
        Y_SIDE,
        THETA_X,
        THETA_Y
    }

    // What values from the triangle are given?
    private enum GivenVal {
        SIDES,
        THETA_Y,
        THETA_X
    }

    public RightTrig() throws InterruptedException {
    }

    @Override
    public void randomize() {
        // Randomiozes what is given
        switch (ScenarioConstants.rng.nextInt(3)) {
            case 0:
                givenVal = GivenVal.THETA_X;
                break;
            case 1:
                givenVal = GivenVal.THETA_Y;
                break;
            case 2:
                givenVal = GivenVal.SIDES;
                break;
        }

        // Randomizes the values based on the problem
        int randomSide;
        do {
            // Resetting the equation
            triangle.updateFromList(Trigonometry.nullArrayList);
            givenSides.removeAll(givenSides);

            switch (givenVal) {
                case THETA_Y:
                    triangle.thetaY = (float) (ScenarioConstants.rng.nextInt(80) + 1);
                    rngSides(ScenarioConstants.rng.nextInt(3) + 1);
                    break;
                case THETA_X:
                    triangle.thetaX = (float) (ScenarioConstants.rng.nextInt(80) + 1);
                    rngSides(ScenarioConstants.rng.nextInt(3) + 1);
                    break;
                case SIDES:
                    rngSides(ScenarioConstants.rng.nextInt(3));
                    randomSide = ScenarioConstants.rng.nextInt(3);
                    while (triangle.sidePresent(randomSide)) {
                        randomSide = ScenarioConstants.rng.nextInt(3);
                    }
                    rngSides(randomSide);
                    triangle.rearrange();
                    break;
            }
            triangle.updateLists();
        } while (!triangle.isLegal90Deg());

        // What the problem wants you to find
        do {
            switch (ScenarioConstants.rng.nextInt(5)) {
                case 0:
                    problemType = ProblemType.THETA_X;
                    break;
                case 1:
                    problemType = ProblemType.THETA_Y;
                    break;
                case 2:
                    problemType = ProblemType.HYPOTENUSE;
                    break;
                case 3:
                    problemType = ProblemType.X_SIDE;
                    break;
                case 4:
                    problemType = ProblemType.Y_SIDE;
                    break;
            }
        } while (questionOverlap());
    }

    @Override
    public String solve() {
        triangle.solve90DegTrig();
        switch (problemType) {
            case HYPOTENUSE:
                return new BigDecimal(triangle.hypotenuse, Trigonometry.trigRound).toString();
            case X_SIDE:
                return new BigDecimal(triangle.x, Trigonometry.trigRound).toString();
            case Y_SIDE:
                return new BigDecimal(triangle.y, Trigonometry.trigRound).toString();
            case THETA_X:
                return new BigDecimal(triangle.thetaX, Trigonometry.trigRound).toString();
            case THETA_Y:
                return new BigDecimal(triangle.thetaY, Trigonometry.trigRound).toString();
            default:
                return "0";
        }

    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case HYPOTENUSE:
                questions = AppConstants
                        .divideLabel(triangle + " What is the hypotenuse of the triangle, given the values?");
                break;
            case X_SIDE:
                questions = AppConstants.divideLabel(triangle + "What is the base of the triangle, given the values?");
                break;
            case Y_SIDE:
                questions = AppConstants.divideLabel(
                        triangle + "What is the height of the triangle, given the values?");
                break;
            case THETA_X:
                questions = AppConstants.divideLabel(triangle + "What is the measure of the base angle? [in degrees]");
                break;
            case THETA_Y:
                questions = AppConstants.divideLabel(
                        triangle + "What is the measure of the height angle? [in degrees]");
        }
    }

    // @Override
    // public void getHowTo() {
    // }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Solve the right triangle given the values.\n- X is the base of the triangle\nY is the height of the triangle",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Checks if the question overlaps with what is being asked.
     * 
     * @return
     */
    private static boolean questionOverlap() {
        switch (givenVal) {
            case SIDES:
                switch (problemType) {
                    case HYPOTENUSE:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case X_SIDE:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case Y_SIDE:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case THETA_X:
                switch (problemType) {
                    case HYPOTENUSE:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case X_SIDE:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case Y_SIDE:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    case THETA_X:
                        return true;
                    default:
                        break;
                }
                break;
            case THETA_Y:
                switch (problemType) {
                    case HYPOTENUSE:
                        if (triangle.hypotenuse != null) {
                            return true;
                        }
                        break;
                    case X_SIDE:
                        if (triangle.x != null) {
                            return true;
                        }
                        break;
                    case Y_SIDE:
                        if (triangle.y != null) {
                            return true;
                        }
                        break;
                    case THETA_Y:
                        return true;
                    default:
                        break;
                }
                break;
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