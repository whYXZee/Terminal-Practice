package scenarios.algebra;

import application.RunApplication;
import application.Scenario;
import resources.Equation;
import resources.MathFunctions;

import java.util.ArrayList;

public class SimplifyEquation extends Scenario {
    @Override
    public void runScenario(int goal, boolean buffer) throws InterruptedException {
        int correct = 0;
        String input;
        for (int i = 0; i < goal; i++) {
            Equation eq = randomize();
            System.out.println("Question " + (i + 1) + "/" + goal);
            System.out.println("Simplify: " + eq);
            if (buffer) {
                buffer = false;
                input = RunApplication.IO.nextLine();
            }
            input = RunApplication.IO.nextLine();
            if (solve(eq).equals(input)) {
                System.out.println("Correct! Moving on:");
                correct++;
            } else if (input.equals("stop")) {
                break;
            } else {
                System.out.println("Incorrect! The correct answer was: " + solve(eq));
            }
            Thread.sleep(2000);
        }
        System.out.println("Congratuations, you got " + correct + " correct!");
    }

    // @Override
    private Equation randomize() {
        int termNumber = 2;
        if (Math.random() > .6) {
            termNumber = 3;
        }
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < termNumber; i++) {
            String term = "";
            if (Math.random() > .5) {
                term = "-";
            }
            term = term + Integer.toString(Scenario.rng.nextInt(25) + 1);
            System.out.println(term);
            output.add(term);
        }
        return new Equation(output);
    }

    private String solve(Equation eq) {
        String output = MathFunctions.addition(eq.termArray[0] + "+" + eq.termArray[1]);
        // for (String i : eq.termArray) {
        // System.out.println(i);
        // }
        if (eq.termArray.length == 3) {
            // System.out.println("yip " + output);
            output = MathFunctions.addition(output + "+" + eq.termArray[2]);
            // System.out.println("yip2 " + output);
        }
        return output;
    }
}
