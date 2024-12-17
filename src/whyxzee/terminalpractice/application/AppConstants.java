package whyxzee.terminalpractice.application;

import whyxzee.terminalpractice.flashcards.*;
import whyxzee.terminalpractice.scenarios.RunOmegaScenario;
import whyxzee.terminalpractice.scenarios.ScenarioTools;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * General-use methods and constants for all classes. Also contains the methods
 * for running the app.
 */
public class AppConstants {
    //
    // Global Variables
    //
    public static Game gameEnum;
    public static Set<String> subjectSet = new HashSet<String>();
    public static Semaphore semaphore = new Semaphore(0);

    // Game
    public static String subject = "";
    public static String set = "";
    public static File json = new File("");
    public static int goal = 0;
    public static ArrayList<String> bannedLetters = new ArrayList<String>();
    public static ArrayList<String> whitelistArray = new ArrayList<String>();
    public static HashMap<String, String> compiledHashMap = new HashMap<String, String>();
    public static ArrayList<String> compiledScenarios = new ArrayList<String>();
    public static boolean isOmega = false;

    // UI
    static GoAgain loopQuestion = new GoAgain();

    static String splash = getRandomSplash();
    public static JFrame frame = new JFrame("Terminal Practice: " + splash);

    public static Font biggerFont = new Font("Arial", Font.PLAIN, 10);
    public static Font bigFont = new Font("Arial", Font.PLAIN, 10);
    public static Font medFont = new Font("Arial", Font.PLAIN, 10);
    public static Font smallFont = new Font("Arial", Font.PLAIN, 10);
    public static int answerColumns = 10;
    public static int width = 10;
    public static int height = 10;
    public static Dimension smallButtonDimension = new Dimension(10, 10);
    public static Dimension wideButtonDimension = new Dimension(10, 10);
    public static Dimension flashcardDimension = new Dimension(10, 10);
    public static Dimension extraButtonDimension = new Dimension(10, 10);

    public enum Game {
        FLASHCARDS,
        DRILLS,
        SCENARIOS,
        CUSTOM_FLASHCARDS,
        CUSTOM_DRILLS,
        JSON_EDITOR,
        JSON_CREATOR,
        JSON_IMPORT,
        OMEGA_DRILL,
        OMEGA_SCENARIO,
        NONE
    }

    /**
     * Runs the app
     * 
     * @throws InterruptedException  from semaphores
     * @throws FileNotFoundException from JSON parsing
     */
    public static void runApp() throws InterruptedException, FileNotFoundException {
        // Declaring variables
        boolean run = true;
        AppConstants.gameEnum = Game.NONE;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Setting the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.5));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);

        // Setting the UI
        width = frame.getBounds().width;
        height = frame.getBounds().height;
        biggerFont = new Font("Arial", Font.PLAIN, width / 20); // standard
        bigFont = new Font("Arial", Font.PLAIN, width / 40);
        medFont = new Font("Arial", Font.PLAIN, width / 60);
        smallFont = new Font("Arial", Font.PLAIN, width / 80);
        answerColumns = width / 75;

        smallButtonDimension = new Dimension(width / 5, height / 15);
        wideButtonDimension = new Dimension(width / 2, height / 15);
        extraButtonDimension = new Dimension(width / 10, height / 15);

        // Declaring UI screens
        GoAgain loopQuestion = new GoAgain();

        // Daemon thread
        FrameDaemon frameDaemon = new FrameDaemon();
        frameDaemon.setDaemon(true);
        frameDaemon.start();

        while (run) {
            switch (gameEnum) {
                case FLASHCARDS:
                    new FlashcardUI(JSONTools.getCustomHashMap(json)).display();
                    semaphore.acquire();
                    break;
                case DRILLS:
                    new RunDrillsUI(JSONTools.getCustomHashMap(json), bannedLetters,
                            JSONTools.getBeginningCharIndex(json)).display();
                    semaphore.acquire();

                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case SCENARIOS:
                    ScenarioTools.getScenario(set).normalRun();
                    // ScenarioConstants.runScenario();

                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case JSON_CREATOR:
                    new FlashcardCreator().display();
                    semaphore.acquire();
                    break;
                case JSON_EDITOR:
                    new FlashcardEditor(
                            JSONTools.getJSONPath(subject, set, "./src/whyxzee/terminalpractice/flashcards/custom/"))
                            .display();
                    semaphore.acquire();
                    break;

                case CUSTOM_FLASHCARDS:
                    new FlashcardUI(JSONTools.getCustomHashMap(json)).display();
                    semaphore.acquire();
                    break;
                case CUSTOM_DRILLS:
                    new RunDrillsUI(JSONTools.getCustomHashMap(json), bannedLetters,
                            JSONTools.getBeginningCharIndex(json)).display();
                    semaphore.acquire();

                    loopQuestion.display();
                    semaphore.acquire();
                    break;

                case JSON_IMPORT:
                    gameEnum = Game.NONE;
                    break;

                case OMEGA_DRILL:
                    new RunBlandDrillsUI(compiledHashMap);
                    semaphore.acquire();

                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case OMEGA_SCENARIO:
                    new RunOmegaScenario();
                    semaphore.acquire();

                    loopQuestion.display();
                    semaphore.acquire();

                case NONE:
                    subject = "";
                    set = "";

                    new GameSelection(checkCustom()).display();
                    semaphore.acquire();
                    break;
            }
        }
    }

    /**
     * Divides the input into seperate labels.
     * 
     * @param input
     * @return
     */
    public static JLabel[] divideLabel(String input) {
        // Declaring variables
        ArrayList<JLabel> output = new ArrayList<JLabel>();
        char[] inputArray = input.toCharArray();

        // Cutting up the input
        String labelContent = "";
        int offset = 0;
        for (int i = 0; i < Math.ceil(inputArray.length / 52.0); i++) {
            for (int j = 0; j < 52; j++) {
                try {
                    labelContent += inputArray[j + (i * 52) + offset];

                    // Detection to see if the line cuts off in the middle of a word
                    if (j == 51 && inputArray[j + 1 + (i * 52) + offset] != ' '
                            && inputArray[j + (i * 52) + offset] != ' ') {
                        offset++;
                        while (inputArray[j + (i * 52) + offset] != ' ') {
                            labelContent += inputArray[j + (i * 52) + offset];
                            offset++;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            output.add(new JLabel(labelContent));
            labelContent = "";
        }
        return output.toArray(new JLabel[output.size()]);
    }

    /**
     * Capitalizes each word of the input.
     * 
     * @param input
     */
    public static String capitalize(String input) {
        String string = "";
        for (int j = 0; j < input.toCharArray().length; j++) {
            if (j == 0 || input.toCharArray()[j - 1] == ' ' || (input.toCharArray()[j - 1] == '.'
                    && Character.isLetter(input.toCharArray()[j])) || (input.toCharArray()[j - 1] == '('
                            && Character.isLetter(input.toCharArray()[j]))) {
                string = string + Character.toString(input.toCharArray()[j]).toUpperCase();
            } else {
                string = string + input.toCharArray()[j];
            }
        }
        return string;
    }

    /**
     * Gets a random splash-text from a set list.
     */
    private static String getRandomSplash() {
        switch (new Random().nextInt(16)) {
            case 0:
                return "case 0";
            case 1:
                return "0 != 1, and 0! = 1"; // factorial & boolean algebra joke
            case 2:
                return "Mitochondria is the powerhouse of the cell";
            case 3:
                return "Sodium Bromide"; // NaBrO
            case 4:
                return "1 + 1 = 10"; // binary joke
            case 5:
                return "My problems are sqrt(-1)"; // imaginary problems
            case 6: // PI
                return "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";
            case 7:
                return "123 is the same as 132"; // combination joke
            case 8:
                return "21 P 2 = -34, trust"; // joke due to bit limits from originally using longs
            case 9:
                return "SOH CAH TOA";
            case 10:
                return "ihatebeans"; // reference to the json used to test a lot of things
            case 11:
                return "7 + 1 = 10"; // octal joke
            case 12:
                return "Can now turn two negatives into a positive!";
            case 13:
                return "V1.1.1 doesn't exist, it can't hurt you.";
            case 14:
                return "It's \u03a9 time";
            case 15:
                return "I have 0 clue how the drill selection works, but it does!";
            default:
                return "";
        }
    }

    /**
     * Checks if there is a custom set available.
     * 
     * @return {@code true} if there is a file in the custom folder (excluding the
     *         .gitignore file), {@code false} if otherwise.
     */
    public static boolean checkCustom() {
        File[] jsons = JSONTools
                .removeGitKeep(new File("./src/whyxzee/terminalpractice/flashcards/custom/").listFiles());
        try {
            if (jsons.length != 0) {
                return true;
            }
        } catch (java.lang.NullPointerException e) {

        }
        return false;
    }

    public static void resize() {
        biggerFont = new Font("Arial", Font.PLAIN, width / 20); // standard
        bigFont = new Font("Arial", Font.PLAIN, width / 40);
        medFont = new Font("Arial", Font.PLAIN, width / 60);
        smallFont = new Font("Arial", Font.PLAIN, width / 80);
        answerColumns = width / 75;

        smallButtonDimension = new Dimension(width / 5, height / 15);
        wideButtonDimension = new Dimension(width / 2, height / 15);
        flashcardDimension = new Dimension((int) (width / 1.25), height / 2);
        extraButtonDimension = new Dimension(width / 10, height / 15);
    }
}

class FrameDaemon extends Thread {
    public FrameDaemon() {
        super("FrameDaemon");
    }

    public void run() {
        while (true) {
            AppConstants.width = AppConstants.frame.getBounds().width;
            AppConstants.height = AppConstants.frame.getBounds().height;

            AppConstants.resize();
            JSONTools.resize();

            try {
                // System.out.println("width: " + AppConstants.width + " height: " +
                // AppConstants.height);
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
        }
    }
}