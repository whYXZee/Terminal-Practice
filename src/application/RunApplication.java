package application;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;

public class RunApplication {
    public static final Scanner IO = new Scanner(new InputStreamReader(System.in, Charset.forName("ISO-8859-1")));

    /**
     * Capitalizes each word of the input.
     */
    public static String capitalize(String input) {
        String string = "";
        for (int j = 0; j < input.toCharArray().length; j++) {
            if (j == 0 || input.toCharArray()[j - 1] == ' ') {
                string = string + Character.toString(input.toCharArray()[j]).toUpperCase();
            } else {
                string = string + input.toCharArray()[j];
            }
        }
        return string;
    }

    private static String chooseSubject(ArrayList<String> map) {
        while (true) {
            System.out.println("\nWhich subject would you like to choose?");
            try {
                Array.printArrays(new HashSet<>(map));
                System.out.println("[type one of the above]");
                String subject = IO.nextLine().toLowerCase();
                if (Array.inArray(new HashSet<>(map), subject)) {
                    return subject;
                } else if (subject.equals("back")) {
                    return "exit code";
                }
            } catch (java.lang.NullPointerException e) {
                System.out.println("Try again.");
            }
        }
    }

    private static String chooseSet(ArrayList<String> map) {
        while (true) {
            System.out.println("\nWhich scenario would you like to choose?");
            Array.printArrays(new HashSet<>(map));
            System.out.println("[type one of the above]");
            try {
                String set = IO.nextLine().toLowerCase();
                if (Array.inArray(new HashSet<>(map), set)) {
                    return set;
                }
            } catch (java.lang.NullPointerException e) {
                System.out.println("Try again.");
            }
        }
    }

    enum Game {
        FLASHCARDS,
        SCENARIOS,
        CUSTOM_FLASHCARDS,
        JSON_EDITOR,
        JSON_CREATOR,
        NONE,
        EXIT
    }

    public static void runApp() throws InterruptedException, FileNotFoundException {
        // Declaring variables
        boolean run, buffer, numberChosen;
        run = buffer = true;
        numberChosen = false;
        int practiceNumber = 10;
        String subject = "";
        String set = "";
        Game gameEnum = Game.NONE;
        File json;
        while (run) {
            switch (gameEnum) {
                case FLASHCARDS:
                    // Getting the flashcard
                    subject = chooseSubject(new ArrayList<>(Array.flashcardHashMap.keySet()));
                    set = chooseSet(new ArrayList<>(Array.flashcardHashMap.get(subject).keySet()));
                    json = Array.flashcardHashMap.get(subject).get(set);

                    // Playing the flashcard
                    Flashcard.runFlashcard(buffer, JSONTools.getCustomHashMap(json), JSONTools.getRestriction(json));
                    System.out.println("Would you like to go again? [n to exit]");
                    if (IO.nextLine().equals("n")) {
                        // isFlashcard = subjectChosen = numberChosen = scenarioChosen = gameChosen =
                        // false;
                        gameEnum = Game.EXIT;
                    } else {
                        buffer = false;
                    }
                    break;

                case SCENARIOS:
                    subject = chooseSubject(new ArrayList<>(Array.scenarioHashMap.keySet()));
                    set = chooseSet(new ArrayList<>(Array.scenarioHashMap.get(subject).keySet()));

                    // Choosing the # of times to play
                    System.out.println("\nHow many questions you like to practice? ");
                    while (!numberChosen) {
                        try {
                            practiceNumber = IO.nextInt();
                            if (practiceNumber >= 1) {
                                numberChosen = buffer = true;
                            } else {
                                practiceNumber = 10;
                                numberChosen = buffer = true;
                            }
                        } catch (java.lang.NullPointerException e) {
                            System.out.println("Try again.");
                        }
                    }

                    // Playing the scenario.
                    Array.scenarioHashMap.get(subject).get(set).runScenario(practiceNumber, buffer);
                    System.out.println("Would you like to go again? [n to exit]");
                    if (IO.nextLine().equals("n")) {
                        gameEnum = Game.EXIT;
                        break;
                    } else {
                        buffer = false;
                    }
                case JSON_CREATOR:
                    JSONTools.create();
                    gameEnum = Game.EXIT;
                    break;

                case JSON_EDITOR:
                    subject = chooseSubject(JSONTools.getCustomSubjects());
                    set = chooseSet(JSONTools.getCustomSets(subject));

                    if (!subject.equals("null")) {
                        JSONTools.edit(JSONTools.getJSONPath(subject, set, "./src/customjson/"));
                        gameEnum = Game.EXIT;
                    }
                    break;

                case CUSTOM_FLASHCARDS:
                    subject = chooseSubject(JSONTools.getCustomSubjects());
                    set = chooseSet(JSONTools.getCustomSets(subject));
                    json = JSONTools.getJSONPath(subject, set, "./src/customjson");

                    // Playing the flashcard.
                    if (!subject.equals("null")) {
                        Flashcard.runFlashcard(buffer, JSONTools.getCustomHashMap(json),
                                JSONTools.getRestriction(json));
                        System.out.println("Would you like to go again? [n to exit]");
                        if (IO.nextLine().equals("n")) {
                            gameEnum = Game.EXIT;
                            break;
                        } else {
                            buffer = false;
                        }
                    }
                case NONE:
                    System.out.println("* Flashcards\n* Scenarios\n* Flashcard Creator");
                    File directory = new File("./src/customjson/");
                    File[] jsons = JSONTools.removeGitKeep(directory.listFiles());
                    try {
                        if (jsons.length != 0) {
                            System.out.println("* Custom Flashcards");
                            System.out.println("* Flashcard Editor");
                        }
                    } catch (java.lang.NullPointerException e) {
                        // make the File[] 0 so that way its not null for later
                        jsons = new File[0];
                    }

                    System.out.println("What would you like to do? [type one of the above]");
                    String game = IO.nextLine().toLowerCase();
                    if (game.equals("flashcards")) {
                        gameEnum = Game.FLASHCARDS;
                        break;
                    } else if (game.equals("scenarios")) {
                        gameEnum = Game.SCENARIOS;
                        break;
                    } else if (game.equals("flashcard creator")) {
                        gameEnum = Game.JSON_CREATOR;
                        break;
                    } else if (game.equals("custom flashcards") && (jsons.length != 0)) {
                        gameEnum = Game.CUSTOM_FLASHCARDS;
                        break;
                    } else if (game.equals("flashcard editor") && (jsons.length != 0)) {
                        gameEnum = Game.JSON_EDITOR;
                        break;
                    } else {
                        System.out.println("Try again.");
                    }
                case EXIT:
                    System.out.println("Would you like to exit? [y to exit]");
                    if (IO.nextLine().equals("y")) {
                        run = false;
                    } else {
                        gameEnum = Game.NONE;
                    }
                    break;
            }
        }
        IO.close();
    }
}