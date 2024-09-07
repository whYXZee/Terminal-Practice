package scenarios.algebra;

import application.RunApplication;
import application.Scenario;
import resources.Equation;

import java.util.ArrayList;

public class AlgebraMemory extends Scenario {
    @Override
    public void runScenario(int goal, boolean buffer) throws InterruptedException {
        int correct = 0;
        String input;
        for (int i = 0; i < goal; i++) {
            Equation eq = randomize();
            System.out.println("Question " + (i + 1) + "/" + goal);
            System.out.println("Remember: " + eq);
            Thread.sleep(1250);
            System.out.println("\033[H\033[2J");
            if (buffer) {
                buffer = false;
                input = RunApplication.IO.nextLine();
            }
            input = RunApplication.IO.nextLine();
            if (eq.toString().equals(input)) {
                System.out.println("Correct! Moving on:");
                correct++;
            } else if (input.equals("stop")) {
                break;
            } else {
                System.out.println("Incorrect! The correct answer was: " + eq);
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
            if (i == 0 && termNumber == 3) {
                term = term + "x^2";
            } else if ((i == 0 && termNumber == 2) || (i == 1 && termNumber == 3)) {
                term = term + "x";
            }
            // System.out.println(term);
            output.add(term);
        }
        return new Equation(output);
    }

    // private String solve(Equation eq) {
    // String output = MathFunctions.addition(eq.termArray[0] + "+" +
    // eq.termArray[1]);
    // // for (String i : eq.termArray) {
    // // System.out.println(i);
    // // }
    // if (eq.termArray.length == 3) {
    // // System.out.println("yip " + output);
    // output = MathFunctions.addition(output + "+" + eq.termArray[2]);
    // // System.out.println("yip2 " + output);
    // }
    // return output;
    // }
}
