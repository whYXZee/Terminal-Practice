package application;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ui.JSONCreator;
import ui.JSONEditor;

@SuppressWarnings("unchecked") // cuz I don't wanna deal w/ warnings
public class JSONTools {
    public static void create() throws FileNotFoundException {
        // Setting the variables
        boolean loop = true;
        JSONObject jsonO = new JSONObject();

        // Adding needed paramaters
        jsonO.put("subject", JSONCreator.subject);
        jsonO.put("setName", JSONCreator.set);
        jsonO.put("restrictLetters", JSONCreator.restrict);

        // Adding terms
        ArrayList<String> questions = parseArrayList(JSONCreator.questions);
        ArrayList<String> answers = parseArrayList(JSONCreator.answers);
        Map<String, String> map = new HashMap<String, String>();
        int termSize = questions.size();
        if (termSize < answers.size()) {
            termSize = answers.size();
        }
        questions = equalizeTerms(questions, termSize);
        answers = equalizeTerms(answers, termSize);
        for (int i = 0; i < termSize; i++) {
            map.put(questions.get(i), answers.get(i));
        }
        jsonO.put("termList", map);

        // Export the JSON
        PrintWriter pw = new PrintWriter("./src/customjson/" + JSONCreator.file + ".json");
        pw.write(jsonO.toJSONString());
        pw.flush();
        pw.close();
    }

    private static ArrayList<String> equalizeTerms(ArrayList<String> input, int termSize) {
        int size = input.size();
        if (termSize != input.size()) {
            System.out.println("termSize: " + termSize + " input size: " + size);
            System.out.println(termSize - size);
            for (int i = 0; i < termSize - size; i++) {
                System.out.println(i);
                input.add("");
            }
        }
        return input;
    }

    public static ArrayList<String> getCustomSubjects() {
        File directory = new File("./src/customjson/");
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

    public static ArrayList<String> getCustomSets(String subject) {
        File directory = new File("./src/customjson/");
        File[] jsons = removeGitKeep(directory.listFiles());
        ArrayList<String> output = new ArrayList<String>();
        if (directory != null) {
            for (File i : jsons) {
                System.out.println(i.getPath());
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
    private static ArrayList<String> parseArrayList(String input) {
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
     * @return path as "./src/"
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
                    // System.out.println("path: " + path);
                }

            }
        }
        return null;
    }

    /**
     * Gets the hashmap from a JSON.
     * 
     * @param path : path of the file, starting from "./src/"
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

    public static String getRestriction(File path) {
        try {
            JSONObject jsonO = (JSONObject) new JSONParser()
                    .parse(new FileReader(path));
            return (String) jsonO.get("restrictLetters");
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return "n";
    }

    /**
     * Removes the ".gitkeep" file from "./src/customjson/"
     * 
     * @param jsons
     * @return
     */
    public static File[] removeGitKeep(File[] jsons) {
        File[] output = {};
        ArrayList<File> recordFiles = new ArrayList<File>();
        for (File i : jsons) {
            System.out.println("remove gitkeep: " + i.getPath());
            if (!i.equals(new File("./src/customjson/.gitkeep"))) {
                // all other files will be jsons, so void the gitkeep
                System.out.println("adding: " + i.getPath());
                recordFiles.add(i);
                output = recordFiles.toArray(new File[recordFiles.size()]);
            }
        }
        return output;
    }

    public static void edit(File path) {
        System.out.println(path.getName());
        try {
            // Getting the file
            JSONObject jsonO = new JSONObject();
            PrintWriter pw = new PrintWriter(path);

            jsonO.put("subject", JSONEditor.subject);
            jsonO.put("setName", JSONEditor.set);
            jsonO.put("restrictLetters", JSONEditor.restrict);

            // Adding terms
            ArrayList<String> questions = parseArrayList(JSONEditor.questions);
            ArrayList<String> answers = parseArrayList(JSONEditor.answers);
            Map<String, String> map = new HashMap<String, String>();
            System.out.println(questions.size());
            System.out.println(answers.size());
            int termSize = questions.size();
            if (termSize < answers.size()) {
                termSize = answers.size();
            }
            System.out.println(termSize);
            questions = equalizeTerms(questions, termSize);
            answers = equalizeTerms(answers, termSize);
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
}
