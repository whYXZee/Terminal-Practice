package whyxzee.terminalpractice.flashcards;

import java.awt.Dimension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

// import javax.swing.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import whyxzee.terminalpractice.application.AppConstants;

@SuppressWarnings("unchecked") // cuz I don't wanna deal w/ warnings
public class JSONTools {
    // Vars
    public static Dimension jsonRestrictDimension = new Dimension(10, 10);
    public static Dimension jsonDimension = new Dimension(10, 10);
    public static int jsonColumns = 10;
    public static int jsonRows = 10;

    /**
     * Creates a JSON of the flashcard set.
     * 
     * @throws FileNotFoundException
     */
    public static void createJSON() throws FileNotFoundException {
        // Setting the variables
        JSONObject jsonO = new JSONObject();

        // Adding needed paramaters
        jsonO.put("subject", JSONCreator.subject.toLowerCase());
        jsonO.put("setName", JSONCreator.set.toLowerCase());
        jsonO.put("restrictLetters", JSONCreator.restrict);
        jsonO.put("beginningCharIndex", JSONCreator.beginningCharIndex);

        // Adding terms
        ArrayList<String> questions = parseArrayList(JSONCreator.questions);
        ArrayList<String> answers = parseArrayList(JSONCreator.answers);
        Map<String, String> map = new HashMap<String, String>();
        int termSize = questions.size();
        for (int i = 0; i < termSize; i++) {
            map.put(questions.get(i), answers.get(i));
        }
        jsonO.put("termList", map);

        // Export the JSON
        PrintWriter pw = new PrintWriter(
                "./src/whyxzee/terminalpractice/flashcards/custom/" + JSONCreator.file + ".json");
        pw.write(jsonO.toJSONString());
        pw.flush();
        pw.close();
    }

    /**
     * Makes the ArrayList size equal to the termSize.
     * 
     * @param input
     * @param termSize
     * @return
     */
    private static ArrayList<String> equalizeTerms(ArrayList<String> input, int termSize) {
        int size = input.size();
        if (termSize != input.size()) {
            for (int i = 0; i < termSize - size; i++) {
                input.add("");
            }
        }
        return input;
    }

    /**
     * Gets the custom subjects available.
     * 
     * @return
     */
    public static ArrayList<String> getCustomSubjects() {
        File directory = new File("./src/whyxzee/terminalpractice/flashcards/custom/");
        File[] jsons = removeGitKeep(directory.listFiles());
        ArrayList<String> output = new ArrayList<String>();
        if (directory != null) {
            for (File i : jsons) {
                try {
                    JSONObject jsonO = (JSONObject) new JSONParser().parse(new FileReader(i));
                    if (!output.contains(jsonO.get("subject"))) {
                        output.add((String) jsonO.get("subject"));
                    }
                } catch (IOException e) {
                    System.out.println("IOException");
                } catch (ParseException e) {
                    System.out.println("ParseException; getcustomSubjects(); " + i);
                }

            }
        }
        return output;
    }

    /**
     * Gets the custom sets available.
     * 
     * @param subject
     * @return
     */
    public static ArrayList<String> getCustomSets(String subject) {
        File directory = new File("./src/whyxzee/terminalpractice/flashcards/custom/");
        File[] jsons = removeGitKeep(directory.listFiles());
        ArrayList<String> output = new ArrayList<String>();
        if (directory != null) {
            for (File i : jsons) {
                try {
                    JSONObject jsonO = (JSONObject) new JSONParser().parse(new FileReader(i));
                    // removed conditional && !output.contains(jsonO.get("setName")
                    if (((String) jsonO.get("subject")).equals(subject)) {
                        output.add((String) jsonO.get("setName"));
                    }
                } catch (IOException e) {
                    System.out.println("IOException");
                } catch (ParseException e) {
                    System.out.println("ParseException at " + i);
                }
            }
        }
        return output;
    }

    /**
     * Parses an ArrayList<String> from the inputted string.
     * 
     * @return
     */
    public static ArrayList<String> parseArrayList(String input) {
        ArrayList<String> output = new ArrayList<String>();
        String stringedChar = "";
        for (Character i : input.toCharArray()) {
            if (i.equals(';')) {
                output.add(stringedChar);
                stringedChar = "";
            } else if (i.equals('\n')) {

            } else {
                stringedChar = stringedChar + i;
            }
        }
        return output;
    }

    /**
     * Gets the path of a JSON.
     * 
     * @param subject
     * @param set
     * @param path
     * @return path as "./src/whyxzee/terminalpractice/flashcards/custom/"
     */
    public static File getJSONPath(String subject, String set, String path) {
        File directory = new File(path);
        File[] jsons = removeGitKeep(directory.listFiles());
        if (directory != null) {
            for (File i : jsons) {
                try {
                    if (i.getPath().contains(".json")) {
                        JSONObject jsonO = (JSONObject) new JSONParser().parse(new FileReader(i));
                        if (((String) jsonO.get("subject")).equals(subject) && ((String) jsonO.get("setName"))
                                .equals(set)) {
                            return i;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("IOException");
                } catch (ParseException e) {
                    System.out.println("ParseException; getJSONPath; " + i);
                } catch (NullPointerException e) {
                    System.out.println("subject: " + subject);
                    System.out.println("set: " + set);
                }

            }
        }
        return null;
    }

    /**
     * Gets the hashmap from a JSON.
     * 
     * @param path : path of the file, starting from
     *             "./src/whyxzee/terminalpractice/flashcards/custom/"
     */
    public static HashMap<String, String> getCustomHashMap(File path) {
        try {
            JSONObject jsonO = (JSONObject) new JSONParser()
                    .parse(new FileReader(path));
            return (HashMap<String, String>) jsonO.get("termList");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Gets if the set allows you to restrict letters.
     * 
     * @param path
     * @return
     */
    public static boolean getRestriction(File path) {
        try {
            JSONObject jsonO = (JSONObject) new JSONParser()
                    .parse(new FileReader(path));
            return (boolean) jsonO.get("restrictLetters");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Removes the ".gitkeep" file from
     * "./src/whyxzee/terminalpractice/flashcards/custom/" when performing file
     * checks.
     * 
     * @param jsons
     * @return
     */
    public static File[] removeGitKeep(File[] jsons) {
        File[] output = {};
        ArrayList<File> recordFiles = new ArrayList<File>();
        for (File i : jsons) {
            if (!i.equals(new File("./src/whyxzee/terminalpractice/flashcards/custom/.gitkeep"))) {
                // all other files will be jsons, so void the gitkeep
                recordFiles.add(i);
                output = recordFiles.toArray(new File[recordFiles.size()]);
            }
        }
        return output;
    }

    /**
     * Edits a selected JSON.
     * 
     * @param path path of the JSON file, starting with "./src/".
     */
    public static void editJSON(File path) {
        try {
            // Getting the file
            JSONObject jsonO = new JSONObject();
            PrintWriter pw = new PrintWriter(path);

            jsonO.put("subject", JSONEditor.subject.toLowerCase());
            jsonO.put("setName", JSONEditor.set.toLowerCase());
            jsonO.put("restrictLetters", JSONEditor.restrict);
            jsonO.put("beginningCharIndex", JSONEditor.beginningCharIndex);

            // Adding terms
            ArrayList<String> questions = parseArrayList(JSONEditor.questions);
            ArrayList<String> answers = parseArrayList(JSONEditor.answers);
            Map<String, String> map = new HashMap<String, String>();
            int termSize = questions.size();
            // if (termSize < answers.size()) {
            // termSize = answers.size();
            // }
            // questions = equalizeTerms(questions, termSize);
            // answers = equalizeTerms(answers, termSize);
            for (int i = 0; i < termSize; i++) {
                map.put(questions.get(i), answers.get(i));
            }
            jsonO.put("termList", map);

            // Writing
            pw.write(jsonO.toJSONString());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
    }

    public static long getBeginningCharIndex(File path) {
        try {
            JSONObject jsonO = (JSONObject) new JSONParser()
                    .parse(new FileReader(path));
            return (long) jsonO.get("beginningCharIndex");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return 0;
    }

    public static String arrayListToString(Set<String> input) {
        String output = "";
        for (String i : input) {
            output = output + i + ";\n";
        }
        return output;
    }

    public static String answersFromKey(HashMap<String, String> map, Set<String> keys) {
        String output = "";
        for (String i : keys) {
            output = output + map.get(i) + ";\n";
        }
        return output;
    }

    public static void resize() {
        jsonRestrictDimension = new Dimension(jsonColumns, AppConstants.height / 15);
        jsonDimension = new Dimension((int) (AppConstants.width / 1.25), (int) (AppConstants.height / 1.4));
        jsonColumns = AppConstants.width / 50;
        jsonRows = AppConstants.height / 150;
    }
}
