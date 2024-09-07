package resources;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Trigonometry {
    public Float thetaY;
    public Float thetaX;
    public Float thetaHyp;
    public Float x;
    public Float y;
    public Float hypotenuse;
    private ArrayList<Float> sides;
    private ArrayList<Float> angles;
    private ArrayList<Float> everything;
    private String[] text = { "X (base) value: ", "Y value: ", "Hypotenuse: ", "\u03b8X: ", "\u03b8Y: ",
            "\u03b8Hypotenuse: " };

    public static MathContext trigRound = new MathContext(3, RoundingMode.HALF_UP);

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
    private void updateLists() {
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
     * Attempts to solve the triangle as if it was a 90 degree triangle.
     */
    public void solve90DegTrig() {
        ArrayList<Float> save = everything; // incase that the triangle isn't 90 degrees, we want to preserve the old
                                            // values
        if (thetaY != null) { // If theta Y isn't missing, then check for sides to solve the rest.
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
            if (isPythagorean()) { // Checks for three sides in case some of the sides are still null.
                thetaHyp = (float) 90.0;
                thetaX = (float) 90 - thetaY;
            } else {
                updateFromList(save);
            }
        } else if (thetaX != null) { // If theta X isn't missing, then check for sides to solve the rest.
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
            if (isPythagorean()) { // Checks for three sides in case some of the sides are still null.
                thetaHyp = (float) 90.0;
                thetaY = (float) 90 - thetaX;
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
            if (isPythagorean()) { // Checks for three sides in case some of the sides are still null.
                thetaHyp = (float) 90.0;
                thetaY = (float) Math.toDegrees(Math.atan(y / x));
                thetaX = (float) Math.toDegrees(Math.atan(x / y));
            } else {
                updateFromList(save);
            }
        }
        System.out.println("Theta Hyp: " + thetaHyp);
        System.out.println("Theta Y: " + thetaY);
        System.out.println("Theta X: " + thetaX);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("Hyp: " + hypotenuse);
    }

    // Checks if a triangle has two sides defined
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
     * 
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
     * Checks if the Triangle Sum Theorem is followed or true.
     * The theorem states that a hypotenuse must be larger than either side of the
     * triangle, but lesser than the sum of both sides.
     * 
     * @return
     */
    public boolean isTriSum() {
        if ((hypotenuse > x) && (hypotenuse > y) && (hypotenuse < x + y)) {
            return true;
        }
        return false;
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

    public boolean sidePresent(int input) {
        if ((input == 1 && x != null) || (input == 2 && y != null) || (input == 3 && hypotenuse != null)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String output = "";
        if (x != null) {
            output = output + text[0] + x + "\n";
        }
        if (y != null) {
            output = output + text[1] + y + "\n";
        }
        if (hypotenuse != null) {
            output = output + text[2] + hypotenuse + "\n";
        }
        if (thetaX != null) {
            output = output + text[3] + thetaX + "\n";
        }
        if (thetaY != null) {
            output = output + text[4] + thetaY + "\n";
        }
        if (thetaHyp != null) {
            output = output + text[5] + thetaHyp;
        }
        return output;
    }
}
