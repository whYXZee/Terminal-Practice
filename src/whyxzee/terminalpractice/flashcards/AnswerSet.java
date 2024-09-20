package whyxzee.terminalpractice.flashcards;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ArrayList of Strings consisting of answers to flashcards.
 */
public class AnswerSet {
    public ArrayList<String> answerSet = new ArrayList<String>();

    /**
     * Parses the input into a manipulatable ArrayList.
     * 
     * @param input answers are separated with commas (",") with no spaces
     *              in-between.
     */
    public AnswerSet(String input) {
        // Declaring variables
        // ArrayList<String> answerSet = new ArrayList<String>();
        String parse = "";

        // Parsing the input into a set of answers.
        for (int i = 0; i < input.toCharArray().length; i++) {
            if (i + 1 == input.toCharArray().length) {
                parse = parse + input.toCharArray()[i];
                answerSet.add(parse);
            } else if (input.toCharArray()[i] != ',') {
                parse = parse + input.toCharArray()[i];
            } else {
                answerSet.add(parse);
                parse = "";
            }
        }

        // this.answerSet = answerSet;
    }

    /**
     * 
     * @param input
     * @param bannedChars
     * @return
     */
    public boolean answerIsAllowed(ArrayList<String> bannedChars, int beginCharIndex) {
        for (String i : answerSet) {
            try {
                String charToCheck = Character.toString(i.toCharArray()[beginCharIndex]).toLowerCase();
                if (!bannedChars.contains(charToCheck)) {
                    return true;
                }
            } catch (IndexOutOfBoundsException error) {

            }

        }
        return false;
    }

    /**
     * Returns the total number of answers with the given letter restriction.
     * 
     */
    public static int totalAnswers(HashMap<String, String> hashmap, ArrayList<String> bannedChars, int beginCharIndex) {
        int output = 0;
        for (String i : hashmap.keySet()) {
            AnswerSet answerSet = new AnswerSet(hashmap.get(i));
            if (answerSet.answerIsAllowed(bannedChars, beginCharIndex)) {
                output++;
            }
        }
        return output;
    }

    /**
     * Checks if the input is in the answer set.
     * 
     * @param input what is being checked in the set
     * @return {@code true} if the answer is inside, {@code false} if not.
     */
    public boolean inSet(String input) {
        // Declaring variables
        char[] inputCharArray = input.toCharArray();
        ArrayList<char[]> answerArray = new ArrayList<char[]>();

        // Making the answer array
        for (String answer : answerSet) {
            answerArray.add(answer.toCharArray());
        }

        // Going through the answers and checking if the characters match
        for (char[] i : answerArray) {
            int charsCorrect = 0;
            try {
                for (int j = 0; j < inputCharArray.length; j++) {
                    if (Character.toLowerCase(inputCharArray[j]) == Character.toLowerCase(i[j])) {
                        charsCorrect++;
                    }
                }
            } catch (IndexOutOfBoundsException e) {

            }
            if (charsCorrect == i.length) {
                String word = "";
                for (Character j : i) {
                    word += j;
                }
                answerSet.remove(word);
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the number of answers in the answer set.
     * 
     * @return size of the answer set.
     */
    public int sizeAnswers() {
        return answerSet.size();
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < answerSet.size(); i++) {
            if (answerSet.size() == 1 || i + 1 == answerSet.size()) {
                output = output + answerSet.get(i);
            } else {
                output = output + answerSet.get(i) + ", ";
            }
        }
        return output;
    }
}
