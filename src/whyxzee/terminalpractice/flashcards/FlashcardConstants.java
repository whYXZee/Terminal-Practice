package whyxzee.terminalpractice.flashcards;

import java.io.File;

import java.util.HashMap;

public class FlashcardConstants {
    public static final HashMap<String, File> spanishFlashcards = new HashMap<String, File>() {
        {
            put("animals", new File("./src/whyxzee/terminalpractice/flashcards/spanish/Animals.json"));
            // put("spanish phonetics", new
            // File("./src/whyxzee/terminalpractice/flashcards/spanish/EsPhonetics.json"));
        }
    };

    public static final HashMap<String, HashMap<String, File>> flashcardHashMap = new HashMap<String, HashMap<String, File>>() {
        {
            put("spanish", spanishFlashcards);
        }
    };
}
