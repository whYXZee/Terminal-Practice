package whyxzee.terminalpractice.application;

import whyxzee.terminalpractice.flashcards.*;
import whyxzee.terminalpractice.resources.English;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class RunApplication {

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

    // public static final Scanner IO = new Scanner(new InputStreamReader(System.in,
    // Charset.forName("ISO-8859-1")));

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
    static ConfigureGoal getGoal = new ConfigureGoal(0, false);
    static SubjectUI chooseSubject = new SubjectUI(new HashSet<String>());
    static SetScenarioUI chooseSet = new SetScenarioUI(new HashSet<String>());

    public static void runApp() throws InterruptedException, FileNotFoundException {
        // Declaring variables
        boolean run = true;
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
                        chooseSubject = new SubjectUI(FlashcardConstants.flashcardHashMap.keySet());
                        chooseSubject.display();
                        semaphore.acquire();
                    }
                    if (set.equals("")) {
                        chooseSet = new SetScenarioUI(FlashcardConstants.flashcardHashMap.get(subject).keySet());
                        chooseSet.display();
                        semaphore.acquire();
                    }

                    json = FlashcardConstants.flashcardHashMap.get(subject).get(set);

                    // Playing the flashcard
                    getGoal = new ConfigureGoal(JSONTools.getCustomHashMap(json).size(), true);
                    getGoal.display();
                    semaphore.acquire();
                    if (JSONTools.getRestriction(json)) {
                        RestrictTerms restrictTerms = new RestrictTerms();
                        restrictTerms.display();
                        semaphore.acquire();
                    } else {
                        whitelistArray.addAll(English.characters.keySet());
                        bannedLetters.removeAll(whitelistArray);
                    }

                    RunDrillsUI drills = new RunDrillsUI(JSONTools.getCustomHashMap(json), bannedLetters,
                            JSONTools.getBeginningCharIndex(json));
                    drills.display();
                    semaphore.acquire();

                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case SCENARIOS:
                    if (subject.equals("")) {
                        chooseSubject = new SubjectUI(ScenarioConstants.scenarioHashMap.keySet());
                        chooseSubject.display();
                        semaphore.acquire();
                    }

                    if (set.equals("")) {
                        chooseSet = new SetScenarioUI(
                                new HashSet<String>(ScenarioConstants.scenarioHashMap.get(subject)));
                        chooseSet.display();
                        semaphore.acquire();
                    }

                    // Choosing the # of times to play
                    ConfigureGoal goalScenario = new ConfigureGoal(0, false);
                    goalScenario.display();
                    semaphore.acquire();

                    // Playing the scenario.
                    ScenarioConstants.runScenario();

                    // Repeat the scenario or nah
                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case JSON_CREATOR:
                    JSONCreator creator = new JSONCreator();
                    creator.display();
                    semaphore.acquire();
                    break;
                case JSON_EDITOR:
                    chooseSubject = new SubjectUI(new HashSet<String>(JSONTools.getCustomSubjects()));
                    chooseSubject.display();
                    semaphore.acquire();

                    chooseSet = new SetScenarioUI(new HashSet<String>(JSONTools.getCustomSets(subject)));
                    chooseSet.display();
                    semaphore.acquire();

                    JSONEditor editor = new JSONEditor(JSONTools.getJSONPath(subject, set,
                            "./src/whyxzee/terminalpractice/flashcards/custom/"));
                    editor.display();
                    semaphore.acquire();
                    break;

                case CUSTOM_DRILLS:
                    if (subject.equals("")) {
                        chooseSubject = new SubjectUI(new HashSet<String>(JSONTools.getCustomSubjects()));
                        chooseSubject.display();
                        semaphore.acquire();
                    }

                    if (set.equals("")) {
                        chooseSet = new SetScenarioUI(new HashSet<String>(JSONTools.getCustomSets(subject)));
                        chooseSet.display();
                        semaphore.acquire();
                    }

                    json = JSONTools.getJSONPath(subject, set, "./src/whyxzee/terminalpractice/flashcards/custom/");

                    // Playing the flashcard
                    getGoal = new ConfigureGoal(JSONTools.getCustomHashMap(json).size(), true);
                    getGoal.display();
                    semaphore.acquire();
                    if (JSONTools.getRestriction(json)) {
                        RestrictTerms restrictTerms = new RestrictTerms();
                        restrictTerms.display();
                        semaphore.acquire();
                    } else {
                        whitelistArray.addAll(English.characters.keySet());
                        bannedLetters.removeAll(whitelistArray);
                    }

                    RunDrillsUI customDrills = new RunDrillsUI(JSONTools.getCustomHashMap(json), bannedLetters,
                            JSONTools.getBeginningCharIndex(json));
                    customDrills.display();
                    semaphore.acquire();

                    loopQuestion.display();
                    semaphore.acquire();
                    break;
                case NONE:
                    subject = "";
                    set = "";
                    boolean jsonsAvailable = false;
                    File[] jsons = JSONTools
                            .removeGitKeep(new File("./src/whyxzee/terminalpractice/flashcards/custom/").listFiles());
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
        // IO.close();
    }

    private static String getRandomSplash() {
        switch (new Random().nextInt(8) + 1) {
            case 1:
                return "0 != 1, and 0! = 1"; // factorial & boolean algebra joke
            case 2:
                return "Mitochondria is the powerhouse of the cell";
            case 3:
                return "Sodium Bromide";
            case 4:
                return "1 + 1 = 10";
            case 5:
                return "My problems are sqrt(-1)";
            case 6:
                return "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";
            case 7:
                return "123 is the same as 132"; // combination joke
            case 8:
                return "21 P 2 = -34, trust"; // joke due to digit limits from originally using longs
            default:
                return "";
        }
    }

    public static void getFontSize() {
        int width = frame.getBounds().width;
        fontSize = width / 20;
    }

    public static int getColumns() {
        return frame.getBounds().width / 100;
    }
}