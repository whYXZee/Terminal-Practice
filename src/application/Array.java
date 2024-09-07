package application;

import scenarios.algebra.*;
import scenarios.physics.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

public final class Array {
    public static final HashMap<String, Scenario> algebraScenarios = new HashMap<String, Scenario>() {
        {
            put("algebraic memory", new AlgebraMemory());
            put("simplify equations", new SimplifyEquation());
            put("solve for x (linear)", new SolveForXLinear());
            put("quadratic factors", new QuadFactors());
        }
    };

    public static final HashMap<String, Scenario> physicsScenarios = new HashMap<String, Scenario>() {
        {
            put("projectile motion", new ProjectileMotion());
        }
    };

    public static final HashMap<String, File> hotaFlashcards = new HashMap<String, File>() {
        {
            put("reconstruction dates",
                    JSONTools.getJSONPath("history of the americas", "reconstructionDate", "./src/flashcards/hota/"));
            put("reconstruction definitions",
                    JSONTools.getJSONPath("history of the americas", "reconstructionDef", "./src/flashcards/hota/"));
            put("u.s. expansionism definitions",
                    JSONTools.getJSONPath("history of the americas", "u.s. expansionist definitions",
                            "./src/flashcards/hota/"));
            put("u.s. expansionism dates",
                    JSONTools.getJSONPath("history of the americas", "u.s. expansionist dates",
                            "./src/flashcards/hota/"));
        }
    };

    public static final HashMap<String, File> spanishFlashcards = new HashMap<String, File>() {
        {
            put("animales",
                    JSONTools.getJSONPath("spanish", "animales", "./src/flashcards/spanish/"));
        }
    };

    public static final HashMap<String, HashMap<String, Scenario>> scenarioHashMap = new HashMap<String, HashMap<String, Scenario>>() {
        {
            put("algebra", algebraScenarios);
            put("physics", physicsScenarios);
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
