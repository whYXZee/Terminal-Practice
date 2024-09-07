package application;

import resources.AnswerSet;
import resources.English;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class Flashcard {
    public static void runFlashcard(boolean buffer, HashMap<String, String> set) throws InterruptedException {
        // Shuffling terms
        ArrayList<String> shuffled = new ArrayList<String>(set.keySet());
        Collections.shuffle(shuffled);

        // Getting the goal amount
        boolean numberChosen = false;
        int goal = 0;
        System.out.println("\nHow many questions you like to practice? [This set has " + shuffled.size() + " terms]");
        while (!numberChosen) {
            try {
                goal = RunApplication.IO.nextInt();
                if (goal >= 1) {
                    numberChosen = true;
                } else if (goal > shuffled.size()) {
                    numberChosen = true;
                } else {
                    goal = 10;
                    numberChosen = true;
                }
            } catch (java.lang.NullPointerException e) {
                System.out.println("Try again.");
            }
        }
        int correct, termsCompleted;
        correct = termsCompleted = 0;
        ArrayList<String> bannedLetters = banLetters(buffer);
        // System.out.println(bannedLetters);
        for (String i : shuffled) {
            // System.out.println(i);
            AnswerSet answers = new AnswerSet(set.get(i));
            // System.out.println(answers);
            if (Character.toString(i.charAt(0)).equals("-")) {
                if (!bannedLetters.contains(Character.toString(i.charAt(1)))) {
                    System.out.println("Question " + (termsCompleted + 1) + "/" + goal + ":");
                    System.out.println(i);
                    String input = RunApplication.IO.nextLine();
                    if (answers.inSet(input)) {
                        if (answers.sizeAnswers() == 0) {
                            System.out.println("Correct!");
                        } else {
                            System.out.println(
                                    "Correct! Other answers include: \"" + answers + "\".");
                        }
                        correct++;
                    } else {
                        System.out.println("Incorrect, answers include: \"" + answers +
                                "\".");
                    }
                    termsCompleted++;
                    Thread.sleep(2000);
                }
            } else if (!bannedLetters.contains(Character.toString(i.charAt(0)))) {
                System.out.println("Question " + (termsCompleted + 1) + "/" + goal + ":");
                System.out.println(i);
                if (answers.inSet(RunApplication.IO.nextLine())) {
                    if (answers.sizeAnswers() == 0) {
                        System.out.println("Correct!");
                    } else {
                        System.out.println(
                                "Correct! Other answers include: \"" + answers + "\".");
                    }
                    correct++;
                } else {
                    System.out.println("Incorrect, answers include: \"" + answers + "\".");
                }
                termsCompleted++;
                Thread.sleep(2000);
            }
            if (termsCompleted == goal) {
                break;
            }
        }
        System.out.println("Congratulations, you got " + correct + " correct!");
    }

    public static ArrayList<String> banLetters(boolean buffer) {
        String letters;
        if (buffer) {
            letters = RunApplication.IO.nextLine();
            buffer = false;
        }
        ArrayList<String> bannedLetters = new ArrayList<String>(English.characters.keySet());
        System.out.println("What letters would you like to practice? ");
        letters = RunApplication.IO.nextLine();
        ArrayList<String> whitelistArray = new ArrayList<String>();
        if (letters.equals("all") || letters.equals("")) {
            bannedLetters.clear();
        } else {
            for (Character i : letters.toCharArray()) {
                whitelistArray.add(Character.toString(i));
            }
        }
        bannedLetters.removeAll(whitelistArray);
        return bannedLetters;
    }
}
