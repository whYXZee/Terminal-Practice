package whyxzee.terminalpractice.scenarios;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.scenarios.algebra.*;
import whyxzee.terminalpractice.scenarios.discrete_math.*;
import whyxzee.terminalpractice.scenarios.number_sense.*;
import whyxzee.terminalpractice.scenarios.physics.*;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ScenarioConstants {
    // Universal classes
    public static final Random rng = new Random();
    public static final Semaphore scenarioSemaphore = new Semaphore(0);
    public static final GridBagConstraints grid = new GridBagConstraints();

    // HashMaps
    static final ArrayList<String> algebraScenarios = new ArrayList<String>() {
        {
            add("algebraic memory");
            add("solve for x (linear)");
            // add("quadratic factors");
        }
    };

    static final ArrayList<String> discreteMathScenarios = new ArrayList<String>() {
        {
            add("combinatorics");
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
            add("projectile motion");
            add("uncertainties");
        }
    };

    public static final HashMap<String, ArrayList<String>> scenarioHashMap = new HashMap<String, ArrayList<String>>() {
        {
            put("algebra", algebraScenarios);
            put("physics", physicsScenarios);
            put("number sense", numberSenseScenarios);
            put("discrete mathematics", discreteMathScenarios);
        }
    };

    /**
     * Runs the scenario depending on what the set in RunApplication is.
     * 
     * @throws InterruptedException because of the semaphores in the UI.
     */
    public static void runScenario() throws InterruptedException {
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;
        grid.gridx = 0;
        grid.gridy = 0;
        switch (AppConstants.set) {
            // Algebra
            case "algebraic memory":
                new AlgebraMemory().display();
                break;
            case "solve for x (linear)":
                new SolveForXLinear().display();
                break;

            // Discrete Mathematics
            case "combinatorics":
                new Combinatorics().display();
                break;

            // Number Sense
            case "factorials":
                new Factorial().display();
                break;
            case "simplify equations":
                new SimplifyEquation().display();
                break;
            case "perfect squares":
                new PerfectSquares().display();
                break;

            // Physics
            case "projectile motion":
                new ProjectileMotion().display();
                break;
            case "uncertainties":
                new Uncertainties().display();
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
