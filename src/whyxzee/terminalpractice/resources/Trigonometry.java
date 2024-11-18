package whyxzee.terminalpractice.resources;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Trigonometry {
    // Sides & Angles
    public Float thetaY;
    public Float thetaX;
    public Float thetaHyp;
    public Float x;
    public Float y;
    public Float hypotenuse;
    private ArrayList<Float> sides;
    private ArrayList<Float> angles;
    private ArrayList<Float> everything;

    // Cartesian-coordinate system (0 is x, 1 is y)
    public Float[] xCoords;
    public Float[] yCoords;
    public Float[] hypCoords;
    public Float[][] triangleCoords; // [0] is X, [1] is Y, [2] is Hypotenuse

    public Float semiPerimeter;
    public Float area;

    private String[] text = { "X value: ", "Y value: ", "Hypotenuse: ", "\u03b8X: ", "\u03b8Y: ",
            "\u03b8Hypotenuse: " };
    public static Float[] nullArray = { null, null, null, null, null, null };
    public static ArrayList<Float> nullArrayList = new ArrayList<Float>() {
        {
            add(null);
            add(null);
            add(null);
            add(null);
            add(null);
            add(null);
        }
    };

    public static MathContext trigRound = new MathContext(3, RoundingMode.HALF_UP);

    //
    // Creating the triangle (sides and angles)
    //

    /**
     * Creating a triangle using an array of primitive floats. The rules of the
     * array are as follows:
     * <ul>
     * <li>[0]: length of side x (the base)
     * <li>[1]: length of side y (or the side closest to the y axis)
     * <li>[2]: length of the hypotenuse
     * <li>[3]: measure of angle X
     * <li>[4]: measure of angle Y
     * <li>[5]: measure of angle hypotenuse
     */
    public Trigonometry(Float[] input) {
        this.x = input[0];
        this.y = input[1];
        this.hypotenuse = input[2];
        this.thetaX = input[3];
        this.thetaY = input[4];
        this.thetaHyp = input[5];

        updateLists();
    }

    /**
     * Updates the triangle from an ArrayList of floats
     */
    public void updateFromList(ArrayList<Float> input) {
        this.x = input.get(0);
        this.y = input.get(1);
        this.hypotenuse = input.get(2);
        this.thetaX = input.get(3);
        this.thetaY = input.get(4);
        this.thetaHyp = input.get(5);
        updateLists();
    }

    /**
     * Updates the list of sides and angles of a triangle. Needed for:
     * <ul>
     * <li>Boolean methods (such as hasTwoSides(), hasThreeSides())
     */
    public void updateLists() {
        this.sides = new ArrayList<Float>() {
            {
                add(x);
                add(y);
                add(hypotenuse);
            }
        };
        this.angles = new ArrayList<Float>() {
            {
                add(thetaX);
                add(thetaY);
                add(thetaHyp);
            }
        };
        this.everything = new ArrayList<Float>() {
            {
                addAll(sides);
                addAll(angles);
            }
        };
    }

    /**
     * In case the length of the hypotenuse is smaller than the other sides, it
     * should rearrange it (only with two sides)
     */
    public void rearrange() {
        float middleman;
        if (y != null && hypotenuse != null) { // we don't want to do arithmetics w/ null i think
            if (y > hypotenuse) {
                middleman = y;
                y = hypotenuse;
                hypotenuse = middleman;
            }
        } else if (x != null && hypotenuse != null) {
            if (x > hypotenuse) {
                middleman = x;
                x = hypotenuse;
                hypotenuse = middleman;
            }
        }
        updateLists();
    }

    //
    // 90-degree triangles
    //

    /**
     * Attempts to find missing values of a triangle as if it was a 90 degree
     * triangle.
     */
    public void solve90DegTrig() {
        ArrayList<Float> save = everything; // incase that the triangle isn't 90 degrees, we want to preserve the old
                                            // values

        // if Theta Y isn't missing, then check for the sides to solve the rest
        if (thetaY != null) {
            if (hypotenuse != null) {
                x = hypotenuse * (float) Math.cos(Math.toRadians(thetaY));
                y = hypotenuse * (float) Math.sin(Math.toRadians(thetaY));
            } else if (x != null) {
                y = x * (float) Math.tan(Math.toRadians(thetaY));
                hypotenuse = x / (float) Math.cos(Math.toRadians(thetaY));
            } else if (y != null) {
                x = y / (float) Math.tan(Math.toRadians(thetaY));
                hypotenuse = y / (float) Math.sin(Math.toRadians(thetaY));
            }
            updateLists();

            // Checks for three sides in case some are still null
            if (isPythagorean()) {
                thetaHyp = (float) 90.0;
                thetaX = (float) 90 - thetaY;
                updateLists();
            } else {
                updateFromList(save);
            }

            // If theta X isn't missing, then check for sides to solve the rest
        } else if (thetaX != null) {
            if (hypotenuse != null) {
                x = hypotenuse * (float) Math.cos(Math.toRadians(thetaX));
                y = hypotenuse * (float) Math.sin(Math.toRadians(thetaX));
            } else if (x != null) {
                y = x / (float) Math.tan(Math.toRadians(thetaX));
                hypotenuse = x * (float) Math.cos(Math.toRadians(thetaX));
            } else if (y != null) {
                x = y * (float) Math.tan(Math.toRadians(thetaX));
                hypotenuse = y / (float) Math.sin(Math.toRadians(thetaX));
            }
            updateLists();

            // Checks for three sides in case some are still null
            if (isPythagorean()) {
                thetaHyp = (float) 90.0;
                thetaY = (float) 90 - thetaX;
                updateLists();
            } else {
                updateFromList(save);
            }
        } else if (hasTwoSides()) {
            if (hypotenuse == null) {
                hypotenuse = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            } else if (x == null) {
                x = (float) Math.sqrt(Math.pow(hypotenuse, 2) - Math.pow(y, 2));
            } else if (y == null) {
                y = (float) Math.sqrt(Math.pow(hypotenuse, 2) - Math.pow(x, 2));
            }
            updateLists();

            // Checks for three sides in case some are still null
            if (isPythagorean()) {
                thetaHyp = (float) 90.0;
                thetaY = (float) Math.toDegrees(Math.atan(y / x));
                thetaX = (float) Math.toDegrees(Math.atan(x / y));
                updateLists();
            } else {
                updateFromList(save);
            }
        }
    }

    //
    // Non 90-degree triangles
    //

    /**
     * Finds the area of a non 90-degree triangle using Heron's Theorem (SSS
     * triangles).
     * 
     * @return float if all sides are given, {@code null} if not.
     */
    public Float heronsTheorem() {
        if (x != null && y != null && hypotenuse != null) {
            semiPerimeter = (x + y + hypotenuse) * .5f;
            area = (float) Math.sqrt(semiPerimeter * (semiPerimeter - x) *
                    (semiPerimeter - y) * (semiPerimeter - hypotenuse));

            return area;
        } else {
            return null;
        }
    }

    /**
     * Uses the normal Law of Sines (a / sin(A) = b / sin(B) = c / sin(C))
     * 
     * @return
     */
    public Float sineLaw() {
        if (x != null && thetaX != null) {
            return x / (float) Math.sin(Math.toRadians(thetaX));
        } else if (y != null && thetaY != null) {
            return y / (float) Math.sin(Math.toRadians(thetaY));
        } else if (hypotenuse != null && thetaHyp != null) {
            return hypotenuse / (float) Math.sin(Math.toRadians(thetaHyp));
        } else {
            return null;
        }
    }

    /**
     * Uses the reciprocal of the Law of Sines (sin(A) / a = sin(B) / b = sin(C) /
     * c)
     * 
     * @return
     */
    public Float sineLawReciprocal() {
        if (x != null && thetaX != null) {
            return (float) Math.sin(Math.toRadians(thetaX)) / x;
        } else if (y != null && thetaY != null) {
            return (float) Math.sin(Math.toRadians(thetaY)) / y;
        } else if (hypotenuse != null && thetaHyp != null) {
            return (float) Math.sin(Math.toRadians(thetaHyp)) / hypotenuse;
        } else {
            return null;
        }
    }

    /**
     * Uses the Law of Cosines to find the missing side
     * 
     * @return
     */
    public Float cosineLawSides() {
        if (x == null) {
            System.out.println(Math.sqrt(Math.pow(y, 2) + Math.pow(hypotenuse, 2)
                    - (2 * y * hypotenuse * Math.cos(Math.toRadians(thetaX)))));
            return (float) Math.sqrt(Math.pow(y, 2) + Math.pow(hypotenuse, 2)
                    - (2 * y * hypotenuse * Math.cos(Math.toRadians(thetaX))));
        } else if (y == null) {
            System.out.println(Math.sqrt(Math.pow(x, 2) + Math.pow(hypotenuse, 2)
                    - (2 * x * hypotenuse * Math.cos(Math.toRadians(thetaY)))));
            return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(hypotenuse, 2)
                    - (2 * x * hypotenuse * Math.cos(Math.toRadians(thetaY))));
        } else if (hypotenuse == null) {
            System.out.println((float) Math.sqrt(Math.pow(y, 2) + Math.pow(x, 2)
                    - (2 * y * x * Math.cos(Math.toRadians(thetaHyp)))));
            return (float) Math.sqrt(Math.pow(y, 2) + Math.pow(x, 2)
                    - (2 * y * x * Math.cos(Math.toRadians(thetaHyp))));
        } else {
            return null;
        }
    }

    /**
     * Uses the Law of Cosines to find the missing angle
     * <ul>
     * <li>[0]: theta X
     * <li>[1]: theta Y
     * <li>[2]: theta Hypotenuse
     * 
     * @return
     */
    public void cosineLawAngles(int angle) {
        if (thetaX == null && angle == 0) {
            try {
                thetaX = (float) Math.toDegrees(
                        Math.acos((Math.pow(x, 2) - Math.pow(y, 2) - Math.pow(hypotenuse, 2)) / (-2 * y * hypotenuse)));
                updateLists();
            } catch (NullPointerException e) {

            }
        }
        if (thetaY == null && angle == 1) {
            try {
                thetaY = (float) Math.toDegrees(
                        Math.acos((Math.pow(y, 2) - Math.pow(x, 2) - Math.pow(hypotenuse, 2)) / (-2 * x * hypotenuse)));
                updateLists();
            } catch (NullPointerException e) {

            }
        }
        if (thetaHyp == null && angle == 2) {
            try {
                thetaHyp = (float) Math.toDegrees(
                        Math.acos((Math.pow(hypotenuse, 2) - Math.pow(x, 2) - Math.pow(y, 2)) / (-2 * x * y)));
                updateLists();
            } catch (NullPointerException e) {

            }
        }
    }

    //
    // Side-based Conditionals
    //

    /**
     * Checks if the side is present;
     * 
     * @param input with numbers of:
     *              <ul>
     *              <li>[1]: X
     *              <li>[2]: Y
     *              <li>[3]: Hypotenuse
     * @return {@code true} if the given side is not null, {@code false} otherwise.
     */
    public boolean sidePresent(int input) {
        if ((input == 1 && x != null) || (input == 2 && y != null) || (input == 3 && hypotenuse != null)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a triangle has two defined sides
     */
    public boolean hasTwoSides() {
        boolean oneSide = false;
        for (Float i : sides) {
            if (i != null) {
                if (oneSide) { // If one of the sides has already been found
                    return true;
                } else { // One of the sides has been found, so toggling that it is found
                    oneSide = true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if all sides of the triangle are defined.
     */
    public boolean hasThreeSides() {
        for (Float i : sides) {
            if (i == null) {
                return false; // if any are null then it can't be true
            }
        }
        return true;
    }

    /**
     * Checks if the triangle is a 90 degree triangle.
     * 
     * @return the boolean of whether the triangle is a 90 degree triangle
     */
    public boolean isPythagorean() {
        if (hasThreeSides()) { // To prevent arithmetics with null
            if (Math.round(Math.pow(hypotenuse, 2)) == Math.round(Math.pow(x, 2) + Math.pow(y, 2))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the triangle has null values, to prevent arithmetics with null.
     */
    public boolean hasNull() {
        // Saving and solving
        ArrayList<Float> save = everything;
        this.solve90DegTrig();

        // Checking
        for (Float i : everything) {
            if (i == null) {
                updateFromList(save);
                return true;
            }
        }
        updateFromList(save);
        return false;
    }

    //
    // Postulate, Conjecture, and Theorem based Conditionals
    //

    /**
     * Checks if the Triangle Inequality Theorem is followed.
     * The theorem states that a hypotenuse must be larger than either side of the
     * triangle, but lesser than the sum of both sides.
     * 
     * @return
     */
    public boolean isTriInequal() {
        if ((hypotenuse >= x) && (hypotenuse >= y) && (hypotenuse <= x + y)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the Triangle Angle Sum Theorem is followed.
     * 
     * @return
     */
    public boolean isAngleSum() {
        if (180 == thetaHyp + thetaX + thetaY) {
            return true;
        }
        return false;
    }

    /**
     * Makes sure that the hypotenuse is the largest side, with the largest angle.
     * 
     * @return
     */
    public boolean isHypotenuseLargest() {
        if (Math.max(thetaHyp, Math.max(thetaX, thetaY)) == thetaHyp
                && Math.max(hypotenuse, Math.max(x, y)) == hypotenuse) {
            return true;
        }
        return false;
    }

    /**
     * An hodgepodge of all postulate, conjecture, and theorem based conditionals.
     * 
     * @return
     */
    public boolean isLegal90Deg() {
        // Setting vars
        ArrayList<Float> save = everything; // to save the triangle for questions
        // System.out.println(save);
        boolean output = true;

        // Solving
        solve90DegTrig();

        try {
            output = isTriInequal();
        } catch (NullPointerException e) {
            output = false;
        }

        updateFromList(save);
        return output;
    }

    //
    // Printing the triangle
    //

    @Override
    public String toString() {
        String output = "";
        if (x != null) {
            output = output + text[0] + x + " \n";
        }
        if (y != null) {
            output = output + text[1] + y + " \n";
        }
        if (hypotenuse != null) {
            output = output + text[2] + hypotenuse + " \n";
        }
        if (thetaX != null) {
            output = output + text[3] + thetaX + " \n";
        }
        if (thetaY != null) {
            output = output + text[4] + thetaY + " \n";
        }
        if (thetaHyp != null) {
            output = output + text[5] + thetaHyp;
        }
        return output;
    }

    public String printSides() {
        String output = "";
        if (x != null) {
            output += " " + text[0] + x;
        }
        if (y != null) {
            output += " " + text[1] + y;
        }
        if (hypotenuse != null) {
            output += " " + text[2] + hypotenuse;
        }

        return output;
    }

    /**
     * Prints the Cartesian coordinate system of the triangle
     */
    public String printCartesianCoords() {
        String output = "";
        if (xCoords != null) {
            output += "X: (" + xCoords[0] + "," + xCoords[1] + ") ";
        }
        if (yCoords != null) {
            output += "Y: (" + yCoords[0] + "," + yCoords[1] + ") ";
        }
        if (hypCoords != null) {
            output += "Hypotenuse: (" + hypCoords[0] + "," + hypCoords[1] + ") ";
        }
        return output;
    }
}
