package whyxzee.terminalpractice.scenarios;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.scenarios.algebra.*;
import whyxzee.terminalpractice.scenarios.discrete_math.*;
import whyxzee.terminalpractice.scenarios.geometry.*;
import whyxzee.terminalpractice.scenarios.number_sense.*;
import whyxzee.terminalpractice.scenarios.physics.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ScenarioConstants {
    // Universal classes
    public static final Random rng = new Random();
    public static final Semaphore scenarioSemaphore = new Semaphore(0);

    // HashMaps
    static final ArrayList<String> algebraScenarios = new ArrayList<String>() {
        {
            add("algebraic memory");
            add("solve for x (linear)");
            add("factoring");
            // add("quadratic factors"); // need to actually finish it lmao
        }
    };

    static final ArrayList<String> discreteMathScenarios = new ArrayList<String>() {
        {
            add("combinatorics");
        }
    };

    static final ArrayList<String> geometryScenarios = new ArrayList<String>() {
        {
            add("right triangle trigonometry"); // needs howToLabels
            add("triangle centers");
            add("non-right triangle trigonometry");
            // structure
        }
    };

    static final ArrayList<String> numberSenseScenarios = new ArrayList<String>() {
        {
            add("simplify equations");
            add("perfect squares");
            add("factorials");
        }
    };

    static final ArrayList<String> physicsScenarios = new ArrayList<String>() {
        {
            // add("projectile motion"); // needs "howToLabels"
            add("uncertainties");
            add("forces");
            add("work, energy, & power");
        }
    };

    public static final HashMap<String, ArrayList<String>> scenarioHashMap = new HashMap<String, ArrayList<String>>() {
        {
            put("algebra", algebraScenarios); // needs scenario structure
            put("physics", physicsScenarios);
            put("number sense", numberSenseScenarios); // needs new structure
            // put("discrete mathematics", discreteMathScenarios); // needs new structure
            put("geometry", geometryScenarios);
        }
    };

    /**
     * Runs the scenario depending on what the set in RunApplication is.
     * 
     * @throws InterruptedException because of the semaphores in the UI.
     */
    public static void runScenario() throws InterruptedException {

        switch (AppConstants.set) {
            // Algebra
            case "algebraic memory":
                new AlgebraMemory();
                break;
            case "solve for x (linear)":
                new SolveForXLinear();
                break;
            case "factoring":
                new Factoring();
                break;

            // Discrete Mathematics
            case "combinatorics":
                new Combinatorics();
                break;

            // Geometry
            case "non-right triangle trigonometry":
                new NonRightTrig();
                break;
            case "right triangle trigonometry":
                new RightTrig();
                break;
            case "triangle centers":
                new TriangleCenters();
                break;

            // Number Sense
            case "factorials":
                new Factorial();
                break;
            case "simplify equations":
                new SimplifyEquation();
                break;
            case "perfect squares":
                new PerfectSquares();
                break;

            // Physics
            case "forces":
                new Forces();
                break;
            case "projectile motion":
                new ProjectileMotion();
                break;
            case "uncertainties":
                new Uncertainties();
                break;
            case "work, energy, & power":
                new WorkEnergyPower();
                break;
        }
    }

    // Timer
    public static boolean timerEnabled = false; // If the timer should be used or not.
    public static int timerInMS = 4500; // Timer of scenario in milliseconds.

    // /**
    // * Terms in list as:
    // * <ul>
    // * <li>[0]: absolute uncertainty
    // * <li>[1]: fractional uncertainty
    // * <li>[2]: percentage uncertainty
    // * <li>[3]: sum of uncertainties
    // * <li>[4]: difference of uncertainties
    // * <li>[5]: multiplication of uncertainties
    // * <li>[6]: division of uncertainties
    // * <li>[7]: power of uncertainties
    // */
    // public static ArrayList<Boolean> uncertainBool = new ArrayList<Boolean>() {
    // {
    // add(true);
    // add(true);
    // add(true);
    // add(true);
    // add(true);
    // add(true);
    // add(true);
    // add(true);
    // }
    // };
}
