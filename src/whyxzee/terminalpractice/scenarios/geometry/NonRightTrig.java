package whyxzee.terminalpractice.scenarios.geometry;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Trigonometry;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;

public class NonRightTrig extends ScenarioUI {
    //
    // Scenario-specific variables
    //
    private static Trigonometry triangle = new Trigonometry(Trigonometry.nullArray);

    // enums to determine the problem type
    private static ProblemType problemType = ProblemType.LAW_OF_COSINES;
    private static SolveFor solveFor = SolveFor.X;

    // Law of Sines
    private static int keep = 0;

    private enum ProblemType {
        LAW_OF_SINES,
        LAW_OF_COSINES
    }

    private enum SolveFor {
        X,
        Y,
        HYPOTENUSE,
        THETA_X,
        THETA_Y,
        THETA_HYP
    }

    public NonRightTrig() throws InterruptedException {

    }

    @Override
    public void randomize() {
        // Randomize all values first
        do {
            triangle.updateFromList(Trigonometry.nullArrayList);

            triangle.x = (float) rng.nextInt(25);
            triangle.y = (float) rng.nextInt(25);
            triangle.thetaX = (float) (rng.nextInt(80) + 1);
            triangle.thetaY = (float) (rng.nextInt(80) + 1);

            triangle.thetaHyp = 180 - triangle.thetaX - triangle.thetaY;
            triangle.hypotenuse = triangle.cosineLawSides();
        } while (triangle.isTriInequal() && triangle.isAngleSum() && triangle.isHypotenuseLargest());

        // Then remove the values based on what needs to be found
        switch (rng.nextInt(2)) {
            case 0:
                problemType = ProblemType.LAW_OF_COSINES;
                switch (rng.nextInt(6)) {
                    case 0:
                        solveFor = SolveFor.X;
                        triangle.x = null;
                        triangle.thetaHyp = null;
                        triangle.thetaY = null;
                        break;
                    case 1:
                        solveFor = SolveFor.Y;
                        triangle.y = null;
                        triangle.thetaX = null;
                        triangle.thetaHyp = null;
                        break;
                    case 2:
                        solveFor = SolveFor.HYPOTENUSE;
                        triangle.hypotenuse = null;
                        triangle.thetaX = null;
                        triangle.thetaY = null;
                        break;
                    case 3:
                        solveFor = SolveFor.THETA_X;
                        triangle.thetaX = null;
                        triangle.thetaY = null;
                        triangle.thetaHyp = null;
                        break;
                    case 4:
                        solveFor = SolveFor.THETA_Y;
                        triangle.thetaX = null;
                        triangle.thetaY = null;
                        triangle.thetaHyp = null;
                        break;
                    case 5:
                        solveFor = SolveFor.THETA_HYP;
                        triangle.thetaX = null;
                        triangle.thetaY = null;
                        triangle.thetaHyp = null;
                        break;
                }
                triangle.updateLists();
                break;
            case 1:
                problemType = ProblemType.LAW_OF_SINES;
                switch (rng.nextInt(6)) {
                    case 0:
                        solveFor = SolveFor.X;
                        keep = 0;
                        triangle.x = null;
                        break;
                    case 1:
                        solveFor = SolveFor.Y;
                        keep = 1;
                        triangle.y = null;
                        break;
                    case 2:
                        solveFor = SolveFor.HYPOTENUSE;
                        keep = 2;
                        triangle.hypotenuse = null;
                        break;
                    case 3:
                        solveFor = SolveFor.THETA_X;
                        keep = 0;
                        triangle.thetaX = null;
                        break;
                    case 4:
                        solveFor = SolveFor.THETA_Y;
                        keep = 1;
                        triangle.thetaY = null;
                        break;
                    case 5:
                        solveFor = SolveFor.THETA_HYP;
                        keep = 2;
                        triangle.thetaHyp = null;
                        break;
                }

                // Remove a pair, to force user to use Law of Sines
                int pairToRemove;
                do {
                    pairToRemove = rng.nextInt(3);
                } while (pairToRemove == keep);
                removePair(pairToRemove);

                triangle.updateLists();
                break;
        }
    }

    @Override
    public String solve() {
        switch (problemType) {
            case LAW_OF_COSINES:
                switch (solveFor) {
                    case X:
                        return new BigDecimal(triangle.cosineLawSides(), Trigonometry.trigRound).toString();
                    case Y:
                        return new BigDecimal(triangle.cosineLawSides(), Trigonometry.trigRound).toString();
                    case HYPOTENUSE:
                        return new BigDecimal(triangle.cosineLawSides(), Trigonometry.trigRound).toString();
                    case THETA_X:
                        triangle.cosineLawAngles(0);
                        return new BigDecimal(triangle.thetaX, Trigonometry.trigRound).toString();
                    case THETA_Y:
                        triangle.cosineLawAngles(1);
                        return new BigDecimal(triangle.thetaY, Trigonometry.trigRound).toString();
                    case THETA_HYP:
                        triangle.cosineLawAngles(2);
                        return new BigDecimal(triangle.thetaHyp, Trigonometry.trigRound).toString();
                }
            case LAW_OF_SINES:
                switch (solveFor) {
                    case X:
                        return new BigDecimal(triangle.sineLaw() * (float) Math.sin(Math.toRadians(triangle.thetaX)),
                                Trigonometry.trigRound).toString();
                    case Y:
                        return new BigDecimal(triangle.sineLaw() * (float) Math.sin(Math.toRadians(triangle.thetaY)),
                                Trigonometry.trigRound).toString();
                    case HYPOTENUSE:
                        return new BigDecimal(triangle.sineLaw() * (float) Math.sin(Math.toRadians(triangle.thetaHyp)),
                                Trigonometry.trigRound).toString();
                    case THETA_X:
                        return new BigDecimal(triangle.sineLawReciprocal() * triangle.x, Trigonometry.trigRound)
                                .toString();
                    case THETA_Y:
                        return new BigDecimal(triangle.sineLawReciprocal() * triangle.y, Trigonometry.trigRound)
                                .toString();
                    case THETA_HYP:
                        return new BigDecimal(triangle.sineLawReciprocal() * triangle.hypotenuse,
                                Trigonometry.trigRound).toString();
                }
            default:
                return "";
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case LAW_OF_COSINES:
                questions = AppConstants.divideLabel("Given " + triangle +
                        ", what is " + getMissing() + "?");
                break;
            case LAW_OF_SINES:
                questions = AppConstants.divideLabel("Given " + triangle +
                        ", what is " + getMissing() + "?");
                break;
        }
    }

    /**
     * Gets what is missing based on the {@code SolveFor} enum.
     * 
     * @return
     */
    private static String getMissing() {
        switch (solveFor) {
            case X:
                return "side X";
            case Y:
                return "side Y";
            case HYPOTENUSE:
                return "side Hypotenuse";
            case THETA_X:
                return "\u03b8X";
            case THETA_Y:
                return "\u03b8Y";
            case THETA_HYP:
                return "\u03b8Hypotenuse";
            default:
                return "";
        }
    }

    /**
     * Removes a pair of information from the triangle.
     * <ul>
     * <li>[0]: X and ΘX
     * <li>[1]: Y and ΘY
     * <li>[2]: Hypotenuse and ΘHypotenuse
     */
    private static void removePair(int remove) {
        if (remove == 0) {
            triangle.x = null;
            triangle.thetaX = null;
        } else if (remove == 1) {
            triangle.y = null;
            triangle.thetaY = null;
        } else if (remove == 2) {
            triangle.hypotenuse = null;
            triangle.thetaHyp = null;
        }
    }

}
