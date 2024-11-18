package whyxzee.terminalpractice.scenarios.physics;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.ImageCreator;
import whyxzee.terminalpractice.scenarios.ExampleObjects;
import whyxzee.terminalpractice.scenarios.ScenarioUI;

import java.math.BigDecimal;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Forces extends ScenarioUI {

    //
    // Scenario-specific variables
    //
    private static ProblemType problemType = ProblemType.BOUYANCY;
    private static ExampleObjects object = new ExampleObjects();

    // Misc
    private static int mass = 0;

    // Bouyancy
    private static double density = 0;
    private static double volume = 0;
    private static double gravity = -9.8;

    // Momentum
    private static int acceleration = 0;

    // Drag
    private static int velocity = 0;
    private static double radius = 0; // small objects
    private static double viscosity = 0;
    private static String medium = "";
    private static int area = 0;
    private static double dragCoefficient = 0;

    // Spring
    private static int displacement = 0;
    private static double springCoefficient = 0;

    // Celestial gravity
    private static final double bigGConstant = 6.67 * Math.pow(10, -11);
    private static int massG_1 = 0;
    private static int massG_2 = 0;
    private static int celestialDistance = 0;

    // Inclined Slope
    private static double fGravity = 0;
    private static double fPerpendicular = 0;
    private static double fParallel = 0;
    private static int slopeTheta = 0;
    private static double frictionCoefficient = 0;

    private enum ProblemType {
        BOUYANCY,
        MOMENTUM,
        DRAG_SMALL,
        DRAG,
        SPRING,
        CELESTIAL_GRAVITY,
        // TENSION,

        INCLINED_SLOPE,
        // PULLEYS
        // PULLEY_INCLINED_SLOPE
    }

    public Forces() throws InterruptedException {

    }

    @Override
    public void randomize() {
        object.rngObjectForce();

        switch (/* rng.nextInt(8) */ 7) {
            case 0:
                problemType = ProblemType.BOUYANCY;
                if (Math.random() > .5) {
                    density = 0.001225; // in L, 1.225 g = 1 L
                    medium = "air";
                } else {
                    density = 1; // in L, 1 kg = 1 L = 1000 cm^3
                    medium = "water";
                }
                volume = (rng.nextInt(10) + 1) * 1000; // in cm^(3)
                break;
            case 1:
                problemType = ProblemType.MOMENTUM;
                mass = rng.nextInt(25) + 1;
                acceleration = rng.nextInt(25) + 1;
                break;
            case 2:
                problemType = ProblemType.DRAG_SMALL;
                velocity = rng.nextInt(100) + 1;

                do {
                    radius = (int) (Math.random() * 200) / 100.0; // in .# mm
                } while (radius == 0);

                if (Math.random() > .5) {
                    medium = "air";
                    viscosity = 0.0000181; // kg / ms
                } else {
                    medium = "water";
                    viscosity = 0.001; // kg / ms
                }
                break;
            case 3:
                problemType = ProblemType.DRAG;
                area = rng.nextInt(25) + 1;
                velocity = rng.nextInt(25) + 1;
                do {
                    dragCoefficient = (int) (Math.random() * 100) / 100.0;
                } while (dragCoefficient == 0);

                if (Math.random() > .5) {
                    medium = "air";
                    density = 0.001225; // in L, 1.225 g = 1 L
                } else {
                    medium = "water";
                    density = 1; // in L, 1 kg = 1 L = 1000 cm^3
                }
                break;
            case 4:
                problemType = ProblemType.SPRING;
                displacement = rng.nextInt(25) + 1;
                do {
                    springCoefficient = (int) (Math.random() * 100) / 100.0;
                } while (springCoefficient == 0);
                break;
            case 5:
                problemType = ProblemType.CELESTIAL_GRAVITY;
                celestialDistance = rng.nextInt(100) + 1;
                massG_1 = rng.nextInt(100) + 1;
                massG_2 = rng.nextInt(100) + 1;
                break;
            case 6:
                // problemType = ProblemType.TENSION;
                // mass = rng.nextInt(25) + 1;
                // break;
            case 7:
                problemType = ProblemType.INCLINED_SLOPE;
                frictionCoefficient = (int) (Math.random() * 100) / 100.0;
                slopeTheta = rng.nextInt(80) + 1;
                mass = rng.nextInt(25) + 1;

                fGravity = (int) (mass * -gravity * 100) / 100.0;
                fPerpendicular = fGravity * Math.cos(Math.toRadians(slopeTheta));
                fParallel = fGravity * Math.sin(Math.toRadians(slopeTheta));
                break;
        }
    }

    @Override
    public String solve() {
        switch (problemType) {
            case BOUYANCY: // 1000 cm^(3) = 1 L, 1000 g = 1 kg
                return new BigDecimal(density * (volume / 1000) * -gravity, round).toString();
            case MOMENTUM:
                return Integer.toString(mass * acceleration);
            case DRAG_SMALL:
                return new BigDecimal(6 * Math.PI * radius * viscosity * velocity, round).toString();
            case DRAG:
                return new BigDecimal(.5 * area * density * dragCoefficient * Math.pow(velocity, 2), round).toString();
            case SPRING:
                return new BigDecimal(springCoefficient * displacement, round).toString();
            case CELESTIAL_GRAVITY:
                return new BigDecimal((bigGConstant * massG_1 * massG_2) / Math.pow(celestialDistance, 2), round)
                        .toString();
            case INCLINED_SLOPE:
                return new BigDecimal(fParallel - (fPerpendicular * frictionCoefficient), round).toString();
            default:
                return "";
        }
    }

    @Override
    public void getQuestion() {
        String friction = "with a friction coefficient of " + frictionCoefficient;
        if (frictionCoefficient == 0) {
            friction = "on a frictionless plane";
        }

        switch (problemType) {
            case BOUYANCY:
                questions = AppConstants.divideLabel("A" + object.singular + " has a volume of " + volume
                        + " cm^(3) and is floating in the " + medium
                        + ". What is the force of bouyancy on the object?");
                break;
            case MOMENTUM:
                questions = AppConstants.divideLabel("What is the force of a" + object.singular + " that has "
                        + "a mass of " + mass + " kg going at an acceleration of " + acceleration + " m/s^(2)?");
                break;
            case DRAG_SMALL:
                questions = AppConstants.divideLabel("What is the drag of a sphere going through the "
                        + medium + " with a radius of " + radius + " mm going at " + velocity + " m/s?");
                break;
            case DRAG:
                questions = AppConstants.divideLabel("What is the drag of a" + object.singular + " that has "
                        + "an area of " + area + " m, going at " + velocity + " m/s in the " + medium + "?");
                break;
            case SPRING:
                questions = AppConstants.divideLabel("Given the spring coefficient " + springCoefficient
                        + ", how much force is used to pull a spring by " + displacement + " m?");
                break;
            case CELESTIAL_GRAVITY:
                questions = AppConstants.divideLabel("Given two celestial bodies with the masses of "
                        + massG_1 + " kg, " + massG_2 + " kg, and distances of " + celestialDistance
                        + " m, what is the force of gravity acting on the bodies?");
                break;
            case INCLINED_SLOPE:
                questions = AppConstants.divideLabel("Given a inclined slope of " + slopeTheta + "\u00b0 with a"
                        + object.singular + ", " + friction + " and a mass of " + mass + " kg, what is the net force?");
                break;
        }
    }

    @Override
    public JLabel getQuestionImg() {
        ImageCreator image = new ImageCreator(AppConstants.imageDimension.width,
                AppConstants.imageDimension.height + 100);

        int horizontalGrid = image.width / 50;
        int verticalGrid = image.height / 50;

        // System.out.println("horizontal grid: " + horizontalGrid);
        // System.out.println("vertical grid: " + verticalGrid);

        switch (problemType) {
            case INCLINED_SLOPE:
                int triangleHeight = verticalGrid * 48;
                int triangleWidth = horizontalGrid * 48;
                image.setColor(175, 175, 175);

                image.filled90Triangle(horizontalGrid, verticalGrid, triangleHeight, triangleWidth);

                // Box
                image.setColor(125, 125, 125);
                double gridRotation = Math.atan(triangleHeight / (double) triangleWidth);
                image.filledRotatedRectangle(horizontalGrid * 20, verticalGrid * 20, verticalGrid * 10,
                        horizontalGrid * 10, gridRotation);

                // Text
                image.setColor(0, 0, 0);
                image.text(slopeTheta + "\u00b0", horizontalGrid * 39, verticalGrid * 47, AppConstants.smallFont);

                return new JLabel(new ImageIcon(image.img));
            default:
                return null;
        }
    }

    @Override
    public void getHowTo() {
        String conversion = "";
        if (medium.equals("air")) {
            conversion = "Finally, the density of air is " + (density * 100)
                    + " g/L. Because the units are in kg, a unit conversion from grams to kilograms is necessary: "
                    + "1000 g = 1 kg, so the density in kg/L is " + density + " kg/L.";
        } else if (medium.equals("water")) {
            conversion = "Finally, the density of water is 1 kg/L, so the density is 1 kg/L.";
        }

        switch (problemType) {
            case BOUYANCY:
                howToLabels = AppConstants.divideLabel("The formula to calculate bouyancy is: "
                        + "Force = density of medium * volume of object * -gravity. The gravity is "
                        + "-9.8 m/s^(2); the volume is in cm^(3) and the density is in L. Therefore " +
                        "a unit conversion from cm^(3) to L is necessary: 1000 cm^(3) = 1 L, so the volume in L is "
                        + (volume / 1000) + " L. " + conversion + " When plugging everything in: F = " + density + " * "
                        + (volume / 1000) + " * "
                        + -gravity + ".");
                break;
            case MOMENTUM:
                howToLabels = AppConstants.divideLabel("The formula to calculate momentum is: "
                        + "Force = mass * acceleration. When plugging everything in, then "
                        + "F = " + mass + " * " + acceleration + ".");
                break;
            case DRAG_SMALL:
                howToLabels = AppConstants.divideLabel("The formula to calcuate drag on a small "
                        + " sphere is: Force = 6 * \u03C0 * radius * viscosity of medium * velocity "
                        + "(also known as Stokes' Law). First, the radius is in mm and must be in meters. Therefore, a unit conversion "
                        + " from mm to m is necessary: 1000 mm = 1 m, so the radius in m is " + (radius / 1000)
                        + ". Because the medium is " + medium + ", the viscosity is " + viscosity
                        + ". When plugging everything in: F = 6 * \u03C0 * " + (radius / 1000) + " * " + viscosity
                        + " * " + velocity + ".");
                break;
            case DRAG:
                howToLabels = AppConstants.divideLabel("The formula to calcuate drag of an object is: "
                        + "Force = .5 * area of the front * density of medium * drag coefficient * velocity^(2). "
                        + ". " + conversion + " When plugging everything in: F = .5 * " + area + " * " + density + " * "
                        + dragCoefficient + " * " + Math.pow(velocity, 2) + ".");
                break;
            case SPRING:
                howToLabels = AppConstants.divideLabel("The formula used to calculate the force of a spring is: "
                        + "Force = spring coefficient * displacement of the spring (also called Hooke's Law). The spring coefficient, "
                        + springCoefficient + ", is the property of a spring to resist deformation. Meanwhile, "
                        + "displacement is the length that a spring is pulled. When plugging everything in: F = "
                        + springCoefficient + " * " + displacement + ".");
                break;
            case CELESTIAL_GRAVITY:
                howToLabels = AppConstants.divideLabel("The formula used to calculate gravity between celestial bodies "
                        + "is: Force = (G * mass of body 1 * mass of body 2) / (distance of two centers of mass)^(2). G is a gravity constant which is equal "
                        + "to 6.67 * 10^(-11). When plugging everything in: F = (" + bigGConstant + " * " + massG_1
                        + " * " + massG_2 + ") / " + Math.pow(celestialDistance, 2) + ".");
                break;
            case INCLINED_SLOPE:
                if (frictionCoefficient == 0) {
                    howToLabels = AppConstants.divideLabel("To find the net force, you add the forces together. "
                            + "However, because the slope is frictionless, there is no need to find friction force. "
                            + "So, all that is needed is to find the force parallel to the slope. Use the formula: Force parallel to the slope = "
                            + "Force of gravity * cos(\u03b8), so F parallel = " + fGravity + " * cos(" + slopeTheta
                            + ") = " + fParallel + ". F Net = " + fParallel + ".");
                } else {
                    howToLabels = AppConstants.divideLabel("To find the net force, you add the forces together. "
                            + "First, find the frictional force. Force of friction is found using the formula: Force of friction = friction coefficient * normal force. "
                            + "Normal force is the force perpendicular to the plane, so use the formula: Force perpendicular to slope = Force of gravity * sin(\u03b8)"
                            + ", so F perpendicular = " + fGravity + " * sin(" + slopeTheta + ") = " + fPerpendicular
                            + ". Then, plug into the friction formula for: F friction = "
                            + frictionCoefficient + " * " + fPerpendicular
                            + ". Finally, find the force parallel to the slope, using the formula: Force parallel to the slope = "
                            + "Force of gravity * cos(\u03b8), so F parallel = " + fGravity + " * cos(" + slopeTheta
                            + ") = " + fParallel + ". Finally, add the forces together with "
                            + "respect to where each force is going (friction opposes the applied force, so it would be negative.): "
                            + "F Net = " + fParallel + " - " + (fPerpendicular
                                    * frictionCoefficient)
                            + ".");
                }
                break;
        }

    }

    @Override
    public void printInfo() {
        String units = "When getting answers:\n - Mass is in kg\n - Distance in m";
        String conversions = "\nRemember conversions:\n - 1000 cm^3 = 1 L\n - 1000 <unit> = 1 kilo<unit>\n 1000 mili<unit> = 1 <unit>";
        String constants = "\nRemember constants:\n - Chosen gravity constant: " + gravity
                + "m/s^(2)\n - Density of air: 1.225 g/L\n - Density of water: 1 kg/L\n - Viscosity of air: 0.0000181 kg / ms\n - Viscosity of water: 0.001 kg / ms";

        JOptionPane.showMessageDialog(AppConstants.frame,
                "Solve the given forces problem." + units + conversions + constants,
                "Scenario Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}
