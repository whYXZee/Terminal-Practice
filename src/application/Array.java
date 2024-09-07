package application;

import scenarios.algebra.*;
import scenarios.physics.*;
import flashcards.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Array {
    // public static final String[] scenarioSubjects = { "algebra" };
    // public static final String[] flashcardSubjects = { "english", "history of the
    // americas" };

    public static final LinkedHashMap<String, Scenario> algebraScenarios = new LinkedHashMap<String, Scenario>() {
        {
            put("algebraic memory", new AlgebraMemory());
            put("simplify equations", new SimplifyEquation());
            put("solve for x (linear)", new SolveForXLinear());
            put("quadratic factors", new QuadFactors());
        }
    };

    public static final LinkedHashMap<String, Scenario> physicsScenarios = new LinkedHashMap<String, Scenario>() {
        {
            put("projectile motion", new ProjectileMotion());
        }
    };

    public static final LinkedHashMap<String, Flashcard> englishFlashcards = new LinkedHashMap<String, Flashcard>() {
        {
            put("roots", new Roots());
        }
    };

    public static final LinkedHashMap<String, HashMap<String, String>> hotaFlashcards = new LinkedHashMap<String, HashMap<String, String>>() {
        {
            put("reconstruction dates",
                    JSONTools.getCustomHashMap("history of the americas", "reconstructionDate",
                            "./src/flashcards/hota/"));
            put("reconstruction definitions",
                    JSONTools.getCustomHashMap("history of the americas", "reconstructionDef",
                            "./src/flashcards/hota/"));
            put("u.s. expansionism definitions",
                    JSONTools.getCustomHashMap("history of the americas", "u.s. expansionist definitions",
                            "./src/flashcards/hota"));
            put("u.s. expansionism dates", JSONTools.getCustomHashMap("history of the americas",
                    "u.s. expansionist dates", "./src/flashcards/hota"));
        }
    };

    public static final LinkedHashMap<String, HashMap<String, String>> spanishFlashcards = new LinkedHashMap<String, HashMap<String, String>>() {
        {
            put("animales", JSONTools.getCustomHashMap("spanish", "animales", "./src/flashcards/spanish"));
        }
    };

    public static final LinkedHashMap<String, HashMap<String, Scenario>> scenarioHashMap = new LinkedHashMap<String, HashMap<String, Scenario>>() {
        {
            put("algebra", algebraScenarios);
            put("physics", physicsScenarios);
        }
    };
    public static final LinkedHashMap<String, HashMap<String, HashMap<String, String>>> flashcardHashMap = new LinkedHashMap<String, HashMap<String, HashMap<String, String>>>() {
        {
            // put("english", englishFlashcards);
            put("history of the americas", hotaFlashcards);
            put("spanish", spanishFlashcards);
        }
    };

    public static void printArrays(ArrayList<String> array) {
        for (String i : array) {
            System.out.println("* " + RunApplication.capitalize(i));
        }
    }

    public static void printArrays(HashMap<String, ?> hashMap) {
        for (String i : hashMap.keySet()) {
            System.out.println("* " + RunApplication.capitalize(i));
        }
    }

    public static boolean inArray(HashMap<String, ?> array, String value) {
        for (String i : array.keySet()) {
            if (value.equals(i)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inArray(ArrayList<String> array, String value) {
        for (String i : array) {
            if (value.equals(i)) {
                return true;
            }
        }
        return false;
    }
}
