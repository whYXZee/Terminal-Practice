package whyxzee.terminalpractice.scenarios;

import whyxzee.terminalpractice.scenarios.discrete_math.*;
import whyxzee.terminalpractice.scenarios.physics.*;
import whyxzee.terminalpractice.scenarios.rec_math.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ScenarioTools {
    //
    // Scenario Constants
    //

    // Timer
    public static boolean timerEnabled = false; // If the timer should be used or not.
    public static int timerInMS = 3000; // Timer of scenario in seconds.

    // Answers
    public static int roundingDigits = 4;
    public static int maxChoices = 5;
    public static int maxNum = 25; // for randomization

    //
    // HashMaps
    //
    static final ArrayList<String> discreteMathScenarios = new ArrayList<String>() {
        {
            add("Combinatorics");
        }
    };

    static final ArrayList<String> recMathScenarios = new ArrayList<String>() {
        {
            add("Vampires");
        }
    };

    static final ArrayList<String> physicsScenarios = new ArrayList<String>() {
        {
            add("Work, Energy, and Power");
            add("Circuitry");
            add("Thermal Energy Transfer");
        }
    };

    public static final HashMap<String, ArrayList<String>> scenarioHashMap = new HashMap<String, ArrayList<String>>() {
        {
            put("Discrete Mathematics", discreteMathScenarios);
            put("Recreational Mathematics", recMathScenarios);
            put("Physics", physicsScenarios);
        }
    };

    //
    // Methods
    //

    /**
     * Gets the scenario based off the String.
     * 
     * @param scenario
     * @return
     */
    public static ScenarioUI getScenario(String scenario) {
        // grid.insets = new Insets(8, 8, 8, 8);
        // grid.anchor = GridBagConstraints.CENTER;
        // grid.gridx = 0;
        // grid.gridy = 0;

        switch (scenario) {
            // Discrete Mathematics
            case "Combinatorics":
                return new Combinatorics();

            // Physics
            case "Circuitry":
                return new Circuitry();
            case "Thermal Energy Transfer":
                return new ThermalEnergyTransfer();
            case "Work, Energy, and Power":
                return new WorkEnergyPower();

            // Rec Math
            case "Vampires":
                return new Vampire();

            default:
                return null;
        }
    }
}
