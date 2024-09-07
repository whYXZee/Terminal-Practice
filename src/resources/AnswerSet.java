package resources;

import java.util.ArrayList;

public class AnswerSet {
    public ArrayList<String> answerSet;

    public AnswerSet(String input) {
        ArrayList<String> answerSet = new ArrayList<String>();
        String parse = "";
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
        this.answerSet = answerSet;
    }

    public boolean inSet(String input) {
        if (answerSet.contains(input)) {
            answerSet.remove(input);
            return true;
        }
        return false;
    }

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
