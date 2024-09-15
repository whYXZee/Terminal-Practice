package scenarios.algebra;

import application.Scenario;
import whyxzee.terminalpractice.application.RunApplication;
import whyxzee.terminalpractice.resources.AlgebraFunctions;
import whyxzee.terminalpractice.resources.Equation;
import whyxzee.terminalpractice.resources.Fraction;

import java.util.ArrayList;

public class QuadFactors extends Scenario {
    @Override
    public void runScenario() throws InterruptedException {
        int correct = 0;
        String input;
        for (int i = 0; i < RunApplication.goal; i++) {
            Equation eq = randomize();
            System.out.println("Question " + (i + 1) + "/" + RunApplication.goal);
            System.out.println("Find the factors of x: " + eq);

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

    private Equation randomize() {
        String a, c;
        if (Math.random() > .5) {
            a = "-" + Integer.toString(Scenario.rng.nextInt(4) + 1);
        } else {
            a = Integer.toString(Scenario.rng.nextInt(4) + 1);
        }
        if (Math.random() > .5) {
            c = "-" + Integer.toString(Scenario.rng.nextInt(40) + 1);
        } else {
            c = Integer.toString(Scenario.rng.nextInt(40) + 1);
        }
        return new Equation(new ArrayList<String>() {
            {
                add(a + "x^2");
                add(AlgebraFunctions.multiplication(a + "*" + c) + "x");
                add(c);
            }
        });
    }

    private String solve(Equation eq) {
        String output = "";
        String bTerm;
        float bVal = (float) AlgebraFunctions.getCoefficient(eq.termArray[1]);
        ArrayList<Float> divisors = AlgebraFunctions.findAllDivisors(
                Float.valueOf(AlgebraFunctions.getCoefficient(eq.termArray[0])), Float.valueOf(eq.termArray[2]));
        for (Float i : divisors) {
            for (Float j : divisors) {
                if (i + j == bVal) {
                    bTerm = i + "x+" + j + "x";
                } else if (i - j == bVal) {
                    bTerm = i + "x-" + j + "x";
                } else if (j - i == bVal) {
                    bTerm = j + "x-" + i + "x";
                }
            }
        }

        return output;
    }
}
