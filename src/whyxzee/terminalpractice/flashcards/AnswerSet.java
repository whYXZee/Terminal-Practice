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
    }

    /**
     * Gets the number of answers in the answer set.
     * 
     * @return size of the answer set.
     */
    public int sizeAnswers() {
        return answerSet.size();
    }

    /**
     * 
     */
    public int maxCharactersInAnswer(int maxChars) {
        for (String i : answerSet) {
            int charsInAnswer = i.toCharArray().length - 1;
            if (charsInAnswer > maxChars) {
                maxChars = charsInAnswer;
            }
        }
        return maxChars;
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

    //
    // Conditionals
    //

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
     * Checks if the input is in the answer set.
     * 
     * @param input what is being checked in the set
     * @return {@code true} if the answer is inside, {@code false} if not.
     */
    public boolean inSet(String input) {
        // Declaring variables
        if (input != null) {
            // ensure that case doesn't matter
            input = input.toLowerCase();
            // Create array to iterate through characters
            char[] inputCharArray = input.toCharArray();

            // ArrayList<char[]> answerArray = new ArrayList<char[]>();

            // Making the answer array
            // for (String answer : answerSet) {
            // answerArray.add(answer.toCharArray());
            // }

            // Going through the answers and checking if the characters match
            for (String i : answerSet) {
                char[] answerArray = i.toLowerCase().toCharArray();
                int charsCorrect = 0;
                int offset = 0; // how many chars to offset input check (extra words in input)
                int skip = 0; // how many chars to skip the answer check (extra words in answer)
                try {
                    for (int j = 0; j + offset < inputCharArray.length; j++) {
                        // checks if "the " is present when it shouldn't
                        if (j == 0) {
                            if (hasThe(i)) {
                                // doesn't work rip
                                // checks if "the " isn't present when it should
                                if (inputCharArray[0] != 't') {
                                    if (inputCharArray[1] != 'h') {
                                        if (inputCharArray[2] != 'e') {
                                            if (inputCharArray[3] != ' '
                                                    || (inputCharArray[3] == ' ' && inputCharArray[4] != ' ')) {
                                                skip += 4;
                                            }
                                        }
                                    }
                                }
                            } else {
                                // checks if "the " is present when it shouldn't
                                if (inputCharArray[0] == 't') {
                                    if (inputCharArray[1] == 'h') {
                                        if (inputCharArray[2] == 'e') {
                                            if (inputCharArray[3] == ' ') {
                                                offset += 4;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        try {
                            if (inputCharArray[j + offset] == answerArray[j + skip]) {
                                charsCorrect++;
                            } else if (answerArray[j + skip] == '-' && inputCharArray[j + offset] == ' ') {
                                charsCorrect++;
                            } else if (answerArray[j + skip] == ' ' && inputCharArray[j + offset] == '-') {
                                charsCorrect++;
                            }
                        } catch (IndexOutOfBoundsException e) {

                        }
                    }
                } catch (IndexOutOfBoundsException e) {

                }
                if (charsCorrect == answerArray.length) {
                    // String word = i;
                    // for (Character j : answerArray) {
                    // word += j;
                    // }
                    // So other potential answers are listed
                    answerSet.remove(i);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean hasThe(String input) {
        // remove repeated .toLowerCase() methods
        input = input.toLowerCase();

        // Nested so that way there are no redundant checks
        if (input.charAt(0) == 't') {
            if (input.charAt(1) == 'h') {
                if (input.charAt(2) == 'e') {
                    if (input.charAt(3) == ' ') {
                        return true;
                    }
                }
            }
        }
        return false;
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
