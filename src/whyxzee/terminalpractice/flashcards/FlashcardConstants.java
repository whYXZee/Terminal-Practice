package whyxzee.terminalpractice.flashcards;

import java.io.File;

import java.util.HashMap;

public class FlashcardConstants {
    public static final HashMap<String, File> spanishFlashcards = new HashMap<String, File>() {
        {
            put("Animals", new File("./src/whyxzee/terminalpractice/flashcards/spanish/Animals.json"));
            put("Plants", new File("./src/whyxzee/terminalpractice/flashcards/spanish/Plants.json"));
            put("Nationalities, Ethnicities, & Religions",
                    new File("./src/whyxzee/terminalpractice/flashcards/spanish/NationsEthnicities.json"));
            put("Food (Plants)", new File("./rc/whyxzee/terminalpractice/flashcards/spanish/FoodPlants.json"));
        }
    };

    static final HashMap<String, File> englishFlashcards = new HashMap<String, File>() {
        {
            put("Greek Roots", new File("./src/whyxzee/terminalpractice/flashcards/english/GreekRoots.json"));
            put("Latin Roots", new File("./src/whyxzee/terminalpractice/flashcards/english/LatinRoots.json"));
            put("Moods and Tones", new File("./src/whyxzee/terminalpractice/flashcards/english/MoodTones.json"));
        }
    };

    static final HashMap<String, File> ibhotaFlashcards = new HashMap<String, File>() {
        {
            put("WW2 Terms (Set 1)", new File("./src/whyxzee/terminalpractice/flashcards/ibhota/WW2_Set1.json"));
            put("WW2 Terms (Set 2)", new File("./src/whyxzee/terminalpractice/flashcards/ibhota/WW2_Set2.json"));
            put("WW2 Dates (Set 2)", new File("./src/whyxzee/terminalpractice/flashcards/ibhota/WW2_Dates_Set2.json"));
        }
    };

    public static final HashMap<String, HashMap<String, File>> flashcardHashMap = new HashMap<String, HashMap<String, File>>() {
        {
            put("Spanish", spanishFlashcards);
            put("English", englishFlashcards);
            put("IB History of the Americas", ibhotaFlashcards);
        }
    };

    public static File getFileGeneric(String input) {
        for (String i : flashcardHashMap.keySet()) {
            if (flashcardHashMap.get(i).keySet().contains(input)) {
                return flashcardHashMap.get(i).get(input);
            }
        }
        return null;
    }
}
