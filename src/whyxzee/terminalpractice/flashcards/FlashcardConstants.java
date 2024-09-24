package whyxzee.terminalpractice.flashcards;

import java.io.File;

import java.util.HashMap;

public class FlashcardConstants {
    public static final HashMap<String, File> spanishFlashcards = new HashMap<String, File>() {
        {
            put("animals", new File("./src/whyxzee/terminalpractice/flashcards/spanish/Animals.json"));
            put("plants", new File("./src/whyxzee/terminalpractice/flashcards/spanish/Plants.json"));
            put("nationalities, ethnicities, & religions",
                    new File("./src/whyxzee/terminalpractice/flashcards/spanish/NationsEthnicities.json"));
            // put("spanish phonetics", new
            // File("./src/whyxzee/terminalpractice/flashcards/spanish/EsPhonetics.json"));
        }
    };

    static final HashMap<String, File> usHistoryFlashcards = new HashMap<String, File>() {
        {
            put("presidential parties",
                    new File("./src/whyxzee/terminalpractice/flashcards/us_history/PresidentialParty.json"));
        }
    };

    static final HashMap<String, File> englishFlashcards = new HashMap<String, File>() {
        {
            put("roots", new File("./src/whyxzee/terminalpractice/flashcards/english/Roots.json"));
        }
    };

    public static final HashMap<String, HashMap<String, File>> flashcardHashMap = new HashMap<String, HashMap<String, File>>() {
        {
            put("spanish", spanishFlashcards);
            put("u.s. history", usHistoryFlashcards);
            put("english", englishFlashcards);
        }
    };
}
