package application;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public final class Constants {
    // static HashMap<String, ScenarioUI> algebraScenarios = new HashMap<String,
    // ScenarioUI>();
    static final ArrayList<String> algebraScenarios = new ArrayList<String>() {
        {
            add("algebraic memory");
            add("simplify equations");
            add("solve for x (linear)");
        }
    };

    static final ArrayList<String> physicsScenarios = new ArrayList<String>() {
        {
            add("projectile motion");
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
            put("physics", physicsScenarios);
        }
    };
    public static final HashMap<String, HashMap<String, File>> flashcardHashMap = new HashMap<String, HashMap<String, File>>() {
        {
            // put("english", englishFlashcards);
            put("spanish", spanishFlashcards);
        }
    };

    // @SuppressWarnings("unchecked")
    // public static void printArrays(Set<String> set) {
    // // To sort the array in alphabetical order
    // @SuppressWarnings("rawtypes")
    // List<String> list = new ArrayList(set);
    // Collections.sort(list);
    // for (String i : list) {
    // System.out.println("* " + RunApplication.capitalize(i));
    // }
    // }

    // public static boolean inArray(Set<String> array, String value) {
    // for (String i : array) {
    // if (value.equals(i)) {
    // return true;
    // }
    // }
    // return false;
    // }
}
