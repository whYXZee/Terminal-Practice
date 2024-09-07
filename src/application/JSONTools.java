package application;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONTools {
    public static void create() throws FileNotFoundException {
        // Setting the variables
        boolean loop = true;
        JSONObject jsonO = new JSONObject();

        // Adding needed paramaters
        System.out.println("What is the subject of the set?");
        jsonO.put("subject", RunApplication.IO.nextLine().toLowerCase());
        System.out.println("What is the name of the set?");
        jsonO.put("setName", RunApplication.IO.nextLine().toLowerCase());

        // Adding terms
        Map<String, String> map = new HashMap<String, String>();
        while (loop) {
            System.out.println("What is the question?");
            String question = RunApplication.IO.nextLine();
            System.out.println("What is the answer?");
            String answer = RunApplication.IO.nextLine();
            map.put(question, answer);
            // Check to end loop
            System.out.println("Add more terms? [n to exit]");
            if (RunApplication.IO.nextLine().toLowerCase().equals("n")) {
                loop = false;
            }
        }
        jsonO.put("termList", map);

        // Export the JSON
        System.out.println("What do you want to call the file?");
        PrintWriter pw = new PrintWriter("./src/customjson/" + RunApplication.IO.nextLine() + ".json");
        pw.write(jsonO.toJSONString());
        pw.flush();
        pw.close();
    }

    public static ArrayList<String> getCustomSubjects() {
        File directory = new File("./src/customjson/");
        File[] jsons = directory.listFiles();
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
        File[] jsons = directory.listFiles();
        ArrayList<String> output = new ArrayList<String>();
        if (directory != null) {
            for (File i : jsons) {
                try {
                    JSONObject jsonO = (JSONObject) new JSONParser().parse(new FileReader(i));
                    if (((String) jsonO.get("subject")).equals(subject) && !output.contains(jsonO.get("setName"))) {
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

    public static File getJSONPath(String subject, String set, String path) {
        File directory = new File(path);
        File[] jsons = directory.listFiles();
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
                }

            }
        }
        return null;
    }

    public static HashMap<String, String> getCustomHashMap(String subject, String set, String path) {
        try {
            JSONObject jsonO = (JSONObject) new JSONParser()
                    .parse(new FileReader(getJSONPath(subject, set, path)));
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

    public static void edit(File path) {
        try {
            // Getting the file
            JSONObject jsonO = (JSONObject) new JSONParser().parse(new FileReader(path));
            PrintWriter pw = new PrintWriter(path);

            // Setting the variables
            String subject = (String) jsonO.get("subject");
            String setName = (String) jsonO.get("setName");
            String editMode = "";
            boolean loop = true;
            HashMap<String, String> terms = (HashMap<String, String>) jsonO.get("termList");

            // Edit Loop
            while (loop) {
                System.out.println("* Subject\n* Set Name\n* Edit Terms\n* Add Terms\nWhat would you like to edit?");
                editMode = RunApplication.IO.nextLine().toLowerCase();

                if (editMode.equals("subject")) {
                    System.out.println("What would you like to change the subject of the set to?");
                    subject = RunApplication.IO.nextLine().toLowerCase();

                } else if (editMode.equals("set name")) {
                    System.out.println("What is the name of the set?");
                    setName = RunApplication.IO.nextLine().toLowerCase();

                } else if (editMode.equals("edit terms")) {
                    for (String i : terms.keySet()) {
                        System.out.println(
                                "Would you like to change the following term? [y to edit]\n" + i + ": " + terms.get(i));
                        if (RunApplication.IO.nextLine().toLowerCase().equals("y")) {
                            terms.remove(i);
                            System.out.println("What is the question?");
                            String question = RunApplication.IO.nextLine();
                            System.out.println("What is the answer?");
                            String answer = RunApplication.IO.nextLine().toLowerCase();
                            terms.put(question, answer);
                        }
                    }

                } else if (editMode.equals("add terms")) {
                    boolean addTermLoop = true;
                    while (addTermLoop) {
                        System.out.println("What is the question?");
                        String question = RunApplication.IO.nextLine();
                        System.out.println("What is the answer?");
                        String answer = RunApplication.IO.nextLine().toLowerCase();
                        terms.put(question, answer);
                        System.out.println("Continue adding terms? [n to exit]");
                        if (RunApplication.IO.nextLine().toLowerCase().equals("n")) {
                            addTermLoop = false;
                        }
                    }
                }
                // Check to end loop
                System.out.println("Continue editing? [n to exit]");
                if (RunApplication.IO.nextLine().toLowerCase().equals("n")) {
                    loop = false;
                    jsonO.put("subject", subject);
                    jsonO.put("setName", setName);
                    jsonO.put("termList", terms);
                }
            }

            // Writing
            pw.write(jsonO.toJSONString());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } catch (ParseException e) {

        }
    }
}
