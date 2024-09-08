package application;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;

import java.util.Random;

import ui.*;

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
                Constants.printArrays(new HashSet<>(map));
                System.out.println("[type one of the above]");
                String subject = IO.nextLine().toLowerCase();
                if (Constants.inArray(new HashSet<>(map), subject)) {
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
            Constants.printArrays(new HashSet<>(map));
            System.out.println("[type one of the above]");
            try {
                String set = IO.nextLine().toLowerCase();
                if (Constants.inArray(new HashSet<>(map), set)) {
                    return set;
                }
            } catch (java.lang.NullPointerException e) {
                System.out.println("Try again.");
            }
        }
    }

    public enum Game {
        DRILLS,
        SCENARIOS,
        CUSTOM_DRILLS,
        JSON_EDITOR,
        JSON_CREATOR,
        NONE
    }

    public static Game gameEnum;
    public static String subject = "";
    public static String set = "";
    public static int goal = 0;
    public static Semaphore semaphore = new Semaphore(0);
    static String splash = getRandomSplash();
    public static JFrame frame = new JFrame("Terminal Practice: " + splash);
    public static ArrayList<String> bannedLetters = new ArrayList<String>();
    public static ArrayList<String> whitelistArray = new ArrayList<String>();
    public static int fontSize = 14;

    // Reuseable panels
    static GoAgain loopQuestion = new GoAgain();

    public static void runApp() throws InterruptedException, FileNotFoundException {
        // Declaring variables
        boolean run, buffer, numberChosen;
        run = buffer = true;
        numberChosen = false;
        File json;
        RunApplication.gameEnum = Game.NONE;

        // Setting the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        while (run) {
            switch (gameEnum) {
                case DRILLS:
                    // Getting the set
                    if (subject.equals("")) {
                        SubjectUI chooseSubject = new SubjectUI(Constants.flashcardHashMap.keySet());
                        chooseSubject.display();
                        semaphore.acquire();
                    }
                    if (set.equals("")) {
                        SetScenarioUI chooseSet = new SetScenarioUI(Constants.flashcardHashMap.get(subject).keySet());
                        chooseSet.display();
                        semaphore.acquire();
                    }

                    json = Constants.flashcardHashMap.get(subject).get(set);

                    // Playing the flashcard
                    ConfigureGoal getGoal = new ConfigureGoal(JSONTools.getCustomHashMap(json).size(), true);
                    getGoal.display();
                    semaphore.acquire();
                    if (JSONTools.getRestriction(json).equals("y")) {
                        RestrictTerms restrictTerms = new RestrictTerms(JSONTools.getRestriction(json));
                        restrictTerms.display();
                        semaphore.acquire();
                    }

                    RunDrillsUI drills = new RunDrillsUI(JSONTools.getCustomHashMap(json), bannedLetters);
                    drills.display();
                    semaphore.acquire();

                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case SCENARIOS:
                    if (subject.equals("")) {
                        SubjectUI chooseSubject = new SubjectUI(Constants.scenarioHashMap.keySet());
                        chooseSubject.display();
                        semaphore.acquire();
                    }
                    if (set.equals("")) {
                        SetScenarioUI chooseSet = new SetScenarioUI(
                                new HashSet<String>(Constants.scenarioHashMap.get(subject)));
                        chooseSet.display();
                        semaphore.acquire();
                    }

                    // Choosing the # of times to play
                    ConfigureGoal goalScenario = new ConfigureGoal(0, false);
                    goalScenario.display();
                    semaphore.acquire();

                    // Playing the scenario.
                    Scenario.runScenario();

                    // Repeat the scenario or nah
                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case JSON_CREATOR:
                    JSONCreator creator = new JSONCreator();
                    creator.display();
                    break;

                case JSON_EDITOR:
                    subject = chooseSubject(JSONTools.getCustomSubjects());
                    set = chooseSet(JSONTools.getCustomSets(subject));

                    if (!subject.equals("null")) {
                        JSONTools.edit(JSONTools.getJSONPath(subject, set, "./src/customjson/"));
                        // gameEnum = Game.EXIT;
                    }
                    break;

                case CUSTOM_DRILLS:
                    subject = chooseSubject(JSONTools.getCustomSubjects());
                    set = chooseSet(JSONTools.getCustomSets(subject));
                    json = JSONTools.getJSONPath(subject, set, "./src/customjson");

                    // Playing the flashcard.
                    if (!subject.equals("null")) {
                        Flashcard.runFlashcard(buffer, JSONTools.getCustomHashMap(json),
                                JSONTools.getRestriction(json));
                        System.out.println("Would you like to go again? [n to exit]");
                        if (IO.nextLine().equals("n")) {
                            // gameEnum = Game.EXIT;
                            break;
                        } else {
                            buffer = false;
                        }
                    }
                case NONE:
                    subject = "";
                    set = "";
                    boolean jsonsAvailable = false;
                    File[] jsons = JSONTools.removeGitKeep(new File("./src/customjson/").listFiles());
                    try {
                        if (jsons.length != 0) {
                            jsonsAvailable = true;
                        }
                    } catch (java.lang.NullPointerException e) {
                        // make the File[] 0 so that way its not null for later
                        jsons = new File[0];
                    }

                    // Show the window
                    GameSelection selectionScreen = new GameSelection(jsonsAvailable);
                    selectionScreen.display();
                    semaphore.acquire();
                    break;
            }
        }
        IO.close();
    }

    private static String getRandomSplash() {
        Random rng = new Random();
        switch (rng.nextInt(2) + 1) {
            case 1:
                return "Mitochondria is the powerhouse of the cell";
            case 2:
                return "v^2 = u^2 + 2as";
            default:
                return "Sodium Bromide";
        }
    }

    public static void getFontSize() {
        int height = frame.getBounds().height;
        int width = frame.getBounds().width;
        fontSize = width / 20;
    }

    public static int getColumns() {
        return frame.getBounds().width / 100;
    }
}