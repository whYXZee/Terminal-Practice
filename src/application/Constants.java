package application;

import scenarios.physics.*;
import ui.scenarios.algebra.*;
import ui.scenarios.ScenarioUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

public final class Constants {
    // static HashMap<String, ScenarioUI> algebraScenarios = new HashMap<String,
    // ScenarioUI>();
    static final ArrayList<String> algebraScenarios = new ArrayList<String>() {
        {
            add("algebraic memory");
        }
    };

    public static final HashMap<String, Scenario> physicsScenarios = new HashMap<String, Scenario>() {
        {
            put("projectile motion", new ProjectileMotion());
        }
    };

    public static final HashMap<String, File> hotaFlashcards = new HashMap<String, File>() {
        {
            put("reconstruction dates", new File("./src/flashcards/hota/ReconstructionDates.json"));
            put("reconstruction definitions", new File("./src/flashcards/hota/ReconstructionDef.json"));
            put("u.s. expansionism definitions", new File("./src/flashcards/hota/USExpansionismDef.json"));
            put("u.s. expansionism dates", new File("./src/flashcards/hota/USExpansionismDates.json"));
        }
    };

    public static final HashMap<String, File> spanishFlashcards = new HashMap<String, File>() {
        {
            put("animales", new File("./src/flashcards/spanish/Animales.json"));
        }
    };

    public static final HashMap<String, ArrayList<String>> scenarioHashMap = new HashMap<String, ArrayList<String>>() {
        {
            put("algebra", algebraScenarios);
            // put("physics", physicsScenarios);
        }
    };
    public static final HashMap<String, HashMap<String, File>> flashcardHashMap = new HashMap<String, HashMap<String, File>>() {
        {
            // put("english", englishFlashcards);
            put("history of the americas", hotaFlashcards);
            put("spanish", spanishFlashcards);
        }
    };

    @SuppressWarnings("unchecked")
    public static void printArrays(Set<String> set) {
        // To sort the array in alphabetical order
        @SuppressWarnings("rawtypes")
        List<String> list = new ArrayList(set);
        Collections.sort(list);
        for (String i : list) {
            System.out.println("* " + RunApplication.capitalize(i));
        }
    }

    public static boolean inArray(Set<String> array, String value) {
        for (String i : array) {
            if (value.equals(i)) {
                return true;
            }
        }
        return false;
    }
}
