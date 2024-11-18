package whyxzee.terminalpractice.scenarios.geometry;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.Trigonometry;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

public class TriangleCenters extends ScenarioUI {
    //
    // Scenario-specific variables
    //

    // General
    private static ProblemType problemType = ProblemType.CIRCUMCIRCLE_RADIUS_SIDES;
    private static Trigonometry triangle = new Trigonometry(Trigonometry.nullArray);

    // Circumcircle and Incircle
    private static float circumcircleRadius = 0;
    private static float incircleRadius = 0;

    // Centroid
    private static String side = "";
    private static String point = "";
    private static int areaSections = 0;
    private static float sectionArea = 0;

    // Coordinate System
    private static float xOffset = 0;
    private static float yOffset = 0;
    private static Float[] answerCoords;

    // What needs to be found?
    private enum ProblemType {
        CIRCUMCIRCLE_RADIUS_SIDES,
        CIRCUMCIRCLE_RADIUS_EQUILATERAL,
        CIRCUMCENTER_RADIUS_ANGLE,

        INRADIUS_SIDES, // inradius is the radius of the incircle
        INCENTER_COORDS,
        INCENTER_VERTEX_DISTANCE,

        CENTROID_VERTEX_DISTANCE,
        CENTROID_SIDE_DISTANCE,
        CENTROID_COORDS,
        CENTROID_AREA,

        EXCIRCLE_RADIUS,

        // ORTHOCENTROIDAL_CIRCLE_DIAMETER,
        // CIRCUMCIRCLE_INCENTER,
    }

    public TriangleCenters() throws InterruptedException {

    }

    @Override
    public void randomize() {
        triangle.updateFromList(Trigonometry.nullArrayList);
        point = "";
        side = "";
        switch (/* rng.nextInt(11) */ 0) {
            case 0: // Radius of the circumcircle given the sides
                problemType = ProblemType.CIRCUMCIRCLE_RADIUS_SIDES;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());
                break;
            case 1: // Radius of the circumcircle given an equilateral triangle
                problemType = ProblemType.CIRCUMCIRCLE_RADIUS_EQUILATERAL;
                triangle.hypotenuse = triangle.x = triangle.y = (float) rng.nextInt(25) + 1;
                break;
            case 2: // Radius of the circumcircle given an angle and an opposite side
                problemType = ProblemType.CIRCUMCENTER_RADIUS_ANGLE;
                randomSideAngle(rng.nextInt(3));
                break;
            case 3: // Inradius given sides
                problemType = ProblemType.INRADIUS_SIDES;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());
                break;
            case 4: // Coordinates of the incenter
                problemType = ProblemType.INCENTER_COORDS;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());

                xOffset = (float) rng.nextInt(6) + 1;
                yOffset = (float) rng.nextInt(6) + 1;
                incircleRadius = (triangle.x * triangle.y * triangle.hypotenuse) / (4 * triangle.heronsTheorem());

                triangle.yCoords = new Float[] { xOffset, yOffset };
                triangle.hypCoords = new Float[] { xOffset + triangle.x, yOffset };
                triangle.thetaX = (float) Math.toDegrees(Math.acos(triangle.x / triangle.hypotenuse));

                triangle.xCoords = new Float[] {
                        xOffset + triangle.x + (triangle.y * (float) Math.cos(Math.toRadians(triangle.thetaX))),
                        yOffset + (triangle.y * (float) Math.sin(Math.toRadians(triangle.thetaX))) };

                break;
            case 5: // Distance between the vertex and the incenter
                problemType = ProblemType.INCENTER_VERTEX_DISTANCE;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());

                triangle.cosineLawAngles(0);
                triangle.cosineLawAngles(1);
                triangle.thetaHyp = 180 - triangle.thetaX - triangle.thetaY;

                switch (rng.nextInt(3)) {
                    case 0:
                        point = "X";
                        break;
                    case 1:
                        point = "Y";
                        break;
                    case 2:
                        point = "Hypotenuse";
                        break;
                }

                break;
            case 6: // Distance between the vertex and the centroid
                problemType = ProblemType.CENTROID_VERTEX_DISTANCE;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());

                switch (rng.nextInt(3)) {
                    case 0:
                        point = "X";
                        break;
                    case 1:
                        point = "Y";
                        break;
                    case 2:
                        point = "Hypotenuse";
                        break;
                }
                break;
            case 7: // Distance between the side and the centroid
                problemType = ProblemType.CENTROID_SIDE_DISTANCE;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());

                switch (rng.nextInt(3)) {
                    case 0:
                        side = "x";
                        break;
                    case 1:
                        side = "y";
                        break;
                    case 2:
                        side = "hypotenuse";
                        break;
                }
                break;
            case 8: // Coordinates of the centroid
                problemType = ProblemType.CENTROID_COORDS;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());

                xOffset = (float) rng.nextInt(6) + 1;
                yOffset = (float) rng.nextInt(6) + 1;
                circumcircleRadius = (triangle.x * triangle.y * triangle.hypotenuse) / (4 * triangle.heronsTheorem());

                triangle.yCoords = new Float[] { xOffset, yOffset };
                triangle.hypCoords = new Float[] { xOffset + triangle.x, yOffset };
                triangle.thetaX = (float) Math.toDegrees(Math.acos(triangle.x / triangle.hypotenuse));

                triangle.xCoords = new Float[] {
                        xOffset + triangle.x + (triangle.y * (float) Math.cos(Math.toRadians(triangle.thetaX))),
                        yOffset + (triangle.y * (float) Math.sin(Math.toRadians(triangle.thetaX))) };

                break;
            case 9: // Area of 1-5 sections from the medians
                problemType = ProblemType.CENTROID_AREA;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());

                // Randomize number of sections
                areaSections = rng.nextInt(5) + 1;
                break;
            case 10: // Radius of the excircle
                problemType = ProblemType.EXCIRCLE_RADIUS;
                do {
                    triangle.hypotenuse = (float) rng.nextInt(25) + 1;
                    triangle.x = (float) rng.nextInt(25) + 1;
                    triangle.y = (float) rng.nextInt(25) + 1;
                    triangle.updateLists();
                } while (!triangle.isTriInequal());

                triangle.heronsTheorem();

                switch (rng.nextInt(3)) {
                    case 0:
                        side = "x";
                        break;
                    case 1:
                        side = "y";
                        break;
                    case 2:
                        side = "hypotenuse";
                        break;
                }
                break;
        }
    }

    @Override
    public String solve() {
        if (response.equals("")) {
            response = "0";
        }
        switch (problemType) {
            case CIRCUMCIRCLE_RADIUS_SIDES:
                circumcircleRadius = (triangle.x * triangle.y * triangle.hypotenuse) / (4 * triangle.heronsTheorem());
                return new BigDecimal(circumcircleRadius, round).toString();
            case CIRCUMCIRCLE_RADIUS_EQUILATERAL:
                circumcircleRadius = triangle.hypotenuse / (float) Math.sqrt(3);
                return new BigDecimal(circumcircleRadius, round).toString();
            case CIRCUMCENTER_RADIUS_ANGLE:
                return new BigDecimal(triangle.sineLaw() / 2, round).toString();
            case INRADIUS_SIDES:
                incircleRadius = (2 * triangle.heronsTheorem()) / (triangle.x + triangle.y + triangle.hypotenuse);
                return new BigDecimal(incircleRadius, round).toString();
            case INCENTER_COORDS:
                answerCoords = incenterCoords();
                return "(" + answerCoords[0] + "," + answerCoords[1] + ")";
            case INCENTER_VERTEX_DISTANCE:
                return new BigDecimal(getIncenterDistance(), round).toString();
            case CENTROID_VERTEX_DISTANCE:
                return new BigDecimal((getMedian() * 2) / 3, round).toString();
            case CENTROID_SIDE_DISTANCE:
                return new BigDecimal(getMedian() / 3, round).toString();
            case CENTROID_COORDS:
                answerCoords = centroidCoords();
                return "(" + answerCoords[0] + "," + answerCoords[1] + ")";
            case CENTROID_AREA:
                sectionArea = triangle.heronsTheorem() / 6;
                return new BigDecimal(sectionArea * areaSections, round).toString();
            case EXCIRCLE_RADIUS:
                return new BigDecimal(triangle.area / (triangle.semiPerimeter - getSideLength()), round).toString();
            default:
                return "";
        }
    }

    @Override
    public void getQuestion() {
        switch (problemType) {
            case CIRCUMCIRCLE_RADIUS_SIDES:
                questions = AppConstants.divideLabel("Given a triangle with side lengths of"
                        + triangle.printSides() + ", what is the radius of the circumcircle?");
                break;
            case CIRCUMCIRCLE_RADIUS_EQUILATERAL:
                questions = AppConstants.divideLabel("Given a triangle with side lengths of"
                        + triangle.printSides() + ", what is the radius of the circumcircle?");
                break;
            case CIRCUMCENTER_RADIUS_ANGLE:
                questions = AppConstants.divideLabel("Given a triangle with side lengths of "
                        + triangle.printSides() + ", what is the radius of the circumcircle?");
                break;
            case INRADIUS_SIDES:
                questions = AppConstants.divideLabel("Given a triangle with side lengths " + triangle.printSides()
                        + ", what is the inradius (radius of the incircle)?");
                break;
            case INCENTER_COORDS:
                questions = AppConstants.divideLabel("Given the triangle with points " + triangle.printCartesianCoords()
                        + " and side lengths" + triangle.printSides()
                        + ", what are the coordinates of the incenter? [answer as (x,y) in decimals]");
                break;
            case INCENTER_VERTEX_DISTANCE:
                questions = AppConstants.divideLabel("Given the triangle" + triangle + ", what is the "
                        + "distance between the incenter and point " + point + "?");
                break;
            case CENTROID_VERTEX_DISTANCE:
                questions = AppConstants.divideLabel("Given the triangle with side lengths" + triangle.printSides()
                        + ", what is the distance between the centroid and point " + point + "?");
                break;
            case CENTROID_SIDE_DISTANCE:
                questions = AppConstants.divideLabel("Given the triangle with side lengths" + triangle.printSides()
                        + ", what is the distance between the centroid and side " + side + "?");
                break;
            case CENTROID_COORDS:
                questions = AppConstants.divideLabel("Given the triangle with points " + triangle.printCartesianCoords()
                        + ", what are the coordinates of the incenter? [answer as (x,y) in decimals]");
                break;
            case CENTROID_AREA:
                questions = AppConstants.divideLabel("Given the triangle with side lengths" + triangle.printSides()
                        + ", what is the area of " + areaSections + " section(s) formed from the medians?");
                break;
            case EXCIRCLE_RADIUS:
                questions = AppConstants.divideLabel("Given the triangle with side lengths" + triangle.printSides()
                        + ", what is the radius of the excircle tangent to side " + side + "?");
                break;
        }
    }

    @Override
    public void getHowTo() {
        switch (problemType) {
            case CIRCUMCIRCLE_RADIUS_SIDES:
                howToLabels = AppConstants.divideLabel("First, find the area of the triangle. To do this, "
                        + "find the semiperimeter (s) and use Heron's Formula. The formula for the semiperimeter of a triangle is: "
                        + ".5(a + b + c). Therefore, .5(" + triangle.x + " + " + triangle.y + " + "
                        + triangle.hypotenuse + "), which equals " + triangle.semiPerimeter
                        + ". Then, Heron's Formula is: sqrt(s * (s - a) * (s - b) * (s - c)). Plugging everything in: sqrt("
                        + triangle.semiPerimeter + " * " + (triangle.semiPerimeter - triangle.x) + " * "
                        + (triangle.semiPerimeter - triangle.y) + " * " + (triangle.semiPerimeter - triangle.hypotenuse)
                        + "). Finally, use the formula for the radius of a circumscribed circle, radius = (a * b * c) / (4 * area). "
                        + "Plugging everything in: r = " + (triangle.x * triangle.y * triangle.hypotenuse) + " / "
                        + (4 * triangle.area) + ".");
                break;
            case CIRCUMCIRCLE_RADIUS_EQUILATERAL:
                howToLabels = AppConstants.divideLabel("Because the triangle is equilateral, we "
                        + "use a different formula: radius = side length / sqrt(3).");
                break;
            case CIRCUMCENTER_RADIUS_ANGLE:
                howToLabels = AppConstants.divideLabel("Becuase we are only given a side and an opposite angle, "
                        + "we use the Law of Sines: 2 * radius = side / sin(opposite angle).");
                break;
            case INRADIUS_SIDES:
                howToLabels = AppConstants.divideLabel("There are two ways to solve this problem. "
                        + "The first way, is to use the formula r = (2 * area) / (a + b + c). The area is found using the semiperimeter [s = .5(a + b + c).] and Heron's "
                        + "Formula [sqrt(s * (s - a) * (s - b) * (s - c))]. The second way, is to use the simplified formula using Heron's Theorem: sqrt(["
                        + "(s - a) * (s - b) * (s - c)] / s), where s is the semiperimeter.");
                break;
            case INCENTER_COORDS:
                howToLabels = AppConstants.divideLabel("The x-coordinate of the incenter is: "
                        + "([a * x-coord of A] + [b * x-coord of B] + [c * x-coord of C]) / (a + b + c). The same premise applies with the y-coordinates.");
                break;
            case INCENTER_VERTEX_DISTANCE:
                howToLabels = AppConstants.divideLabel("The formula to find the distance between a point A "
                        + " and the incenter is: c * (sin(B / 2) / cos (C / 2)) or b * (sin(C / 2) / cos (B / 2)).");
                break;
            case CENTROID_VERTEX_DISTANCE:
                howToLabels = AppConstants.divideLabel("First, find the distance of the median using "
                        + "the formula: median of a = .5 * sqrt(2c^(2) + 2b^(2) - a^(2)), where a is the side that "
                        + "connects to the median. Plugging in the numbers gets the length of " + getMedian()
                        + ". Finally, the distance between the vertex and the centroid is "
                        + "two-thirds of the length of the median.");
                break;
            case CENTROID_SIDE_DISTANCE:
                howToLabels = AppConstants.divideLabel("First, find the distance of the median using "
                        + "the formula: median of a = .5 * sqrt(2c^(2) + 2b^(2) - a^(2)), where a is the side that "
                        + "connects to the median. Plugging in the numbers gets the length of " + getMedian()
                        + ". Finally, the distance between the vertex and the centroid is "
                        + "one-third of the length of the median.");
                break;
            case CENTROID_COORDS:
                howToLabels = AppConstants.divideLabel("The x-coordinate of the centroid is "
                        + "the mean of the x-coordinates of the vertexes: (" + triangle.xCoords[0] + " + "
                        + triangle.yCoords[0] + " + " + triangle.hypCoords[0] + ") / 3."
                        + "The same premise applies with y-coordinates.");
                break;
            case CENTROID_AREA:
                howToLabels = AppConstants.divideLabel("First, find the area of the triangle. "
                        + "The easiest way would be through Heron's Formula, sqrt(s * (s - a) * (s - b) * (s - c)), with semiperimeter (s) = .5(a + b + c). "
                        + "Because the medians cut the area into six equal sections, you find the area of each section ("
                        + (sectionArea * 6) + " / 6) and multiply by the number of sections you want to find ("
                        + sectionArea + " * " + areaSections + ").");
                break;
            case EXCIRCLE_RADIUS:
                howToLabels = AppConstants.divideLabel("First, find the area of the triangle."
                        + "The easiest way would be through Heron's Formula, sqrt(s * (s - a) * (s - b) * (s - c)), with semiperimeter (s) = .5(a + b + c). "
                        + "Then, use the formula radius = area / (semiperimeter - side length of tangent side to triangle). Plugging everything in: r = "
                        + triangle.area + " / (" + triangle.semiPerimeter + " - " + getSideLength() + ").");
                break;
        }
    }

    @Override
    public void printInfo() {
        JOptionPane.showMessageDialog(AppConstants.frame,
                "Find what is being asked.\nRemember:\n - s = .5(a + b + c)\n - A = sqrt(s * (s - a) * (s - b) * (s - c))",
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    //
    // Scenario-specific methods
    //
    /**
     * Randomizes the side and corresponding angle
     * 
     * @param side number of the side, with the following key:
     *             <ul>
     *             <li>0: side X
     *             <li>1: side Y
     *             <li>2: Hypotenuse
     */
    private void randomSideAngle(int side) {
        if (side == 0) {
            triangle.x = (float) rng.nextInt(25) + 1;
            triangle.thetaX = (float) rng.nextInt(80) + 1;
        } else if (side == 1) {
            triangle.y = (float) rng.nextInt(25) + 1;
            triangle.thetaY = (float) rng.nextInt(80) + 1;
        } else if (side == 2) {
            triangle.hypotenuse = (float) rng.nextInt(25) + 1;
            triangle.thetaHyp = (float) rng.nextInt(80) + 1;
        }
        triangle.updateLists();
    }

    /**
     * Solves the coordinates of the incenter.
     * 
     * @return
     */
    private Float[] incenterCoords() {
        float denom = triangle.x + triangle.y + triangle.hypotenuse;

        float xNum = (triangle.x * triangle.xCoords[0]) + (triangle.y * triangle.yCoords[0]) + (triangle.hypotenuse
                * triangle.hypCoords[0]);
        float yNum = (triangle.x * triangle.xCoords[1]) + (triangle.y * triangle.yCoords[1]) + (triangle.hypotenuse
                * triangle.hypCoords[1]);

        return new Float[] { new BigDecimal(xNum / denom, round).floatValue(),
                new BigDecimal(yNum / denom, round).floatValue() };
    }

    /**
     * Gets the coordinates of the centroid.
     * 
     * @return
     */
    private Float[] centroidCoords() {
        float xCoord = new BigDecimal((triangle.xCoords[0] + triangle.yCoords[0] + triangle.hypCoords[0]) / 3, round)
                .floatValue();
        float yCoord = new BigDecimal((triangle.xCoords[1] + triangle.yCoords[1] + triangle.hypCoords[1]) / 3, round)
                .floatValue();

        return new Float[] { xCoord, yCoord };
    }

    /**
     * Gets the distance between the incenter and a point.
     * 
     * @return
     */
    private Float getIncenterDistance() {
        // Get numerator
        float numerator = 0;
        if (point.equals("X")) {
            numerator = (float) Math.sin(Math.toRadians(triangle.thetaHyp / 2));
        } else if (point.equals("Y")) {
            numerator = (float) Math.sin(Math.toRadians(triangle.thetaX / 2));
        } else if (point.equals("Hypotenuse")) {
            numerator = (float) Math.sin(Math.toRadians(triangle.thetaY / 2));
        }

        // Get denominator
        float denom = 0;
        float side = 0;
        if (point.equals("X")) {
            denom = (float) Math.cos(Math.toRadians(triangle.thetaY / 2));
            side = triangle.y;
        } else if (point.equals("Y")) {
            denom = (float) Math.cos(Math.toRadians(triangle.thetaHyp / 2));
            side = triangle.hypotenuse;
        } else if (point.equals("Hypotenuse")) {
            denom = (float) Math.cos(Math.toRadians(triangle.thetaX / 2));
            side = triangle.x;
        }

        return side * (numerator / denom);

    }

    /**
     * Gets the length of the side.
     * 
     * @return {@code null} if "side" variable is not "x", "y", or "hypotenuse"
     */
    private Float getSideLength() {
        if (side.equals("x")) {
            return triangle.x;
        } else if (side.equals("y")) {
            return triangle.y;
        } else if (side.equals("hypotenuse")) {
            return triangle.hypotenuse;
        }
        return null;
    }

    /**
     * Gets the length of the median (used for finding the centroid).
     * 
     * @return
     */
    private Float getMedian() {
        if (point.equals("X") || side.equals("x")) {
            return new BigDecimal(.5 * Math.sqrt(
                    (2 * Math.pow(triangle.y, 2)) + (2 * Math.pow(triangle.hypotenuse, 2)) - Math.pow(triangle.x, 2)),
                    round).floatValue();
        } else if (point.equals("Y") || side.equals("y")) {
            return new BigDecimal(.5 * Math.sqrt(
                    (2 * Math.pow(triangle.x, 2)) + (2 * Math.pow(triangle.hypotenuse, 2)) - Math.pow(triangle.y, 2)),
                    round).floatValue();
        } else if (point.equals("Hypotenuse") || side.equals("hypotenuse")) {
            return new BigDecimal(.5f * Math.sqrt(
                    (2 * Math.pow(triangle.y, 2)) + (2 * Math.pow(triangle.x, 2)) - Math.pow(triangle.hypotenuse, 2)),
                    round).floatValue();
        }
        return null;
    }
}
