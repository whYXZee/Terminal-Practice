package flashcards;

import application.Flashcard;
import application.RunApplication;
import resources.AnswerSet;
import resources.English;

import java.util.ArrayList;
import java.util.Collections;

public class Roots extends Flashcard {
    public void runFlashcard(int goal, boolean buffer) throws InterruptedException {
        int correct, termsCompleted;
        correct = termsCompleted = 0;
        ArrayList<String> shuffled = new ArrayList<String>(English.allRoots.keySet());
        Collections.shuffle(shuffled);
        ArrayList<String> bannedLetters = super.banLetters(buffer);
        for (String i : shuffled) {
            AnswerSet answers = new AnswerSet(English.allRoots.get(i));
            // System.out.println(answers);
            if (Character.toString(i.charAt(0)).equals("-")) {
                if (!bannedLetters.contains(Character.toString(i.charAt(1)))) {
                    System.out.println("Question " + (termsCompleted + 1) + "/" + goal + ":");
                    System.out.println("What is the prefix for: " + i + "?");
                    if (answers.inSet(RunApplication.IO.nextLine().toLowerCase())) {
                        System.out.println("Correct! Other answers include: \"" + answers + "\".");
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
                System.out.println("What is the prefix for: " + i + "?");
                if (answers.inSet(RunApplication.IO.nextLine().toLowerCase())) {
                    System.out.println("Correct! Other answers include: \"" + answers + "\".");
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
}
