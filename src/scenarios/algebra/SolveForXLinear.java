package scenarios.algebra;

import application.RunApplication;
import application.Scenario;
import resources.Equation;
import resources.Fraction;
import resources.MathFunctions;

import java.util.ArrayList;

public class SolveForXLinear extends Scenario {
    @Override
    public void runScenario(int goal, boolean buffer) throws InterruptedException {
        int correct = 0;
        String input;
        for (int i = 0; i < goal; i++) {
            Equation eq = randomize();
            System.out.println("Question " + (i + 1) + "/" + goal);
            System.out.println("Solve for x: " + eq);
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
        String xPowOne, signPowOne, xPowZero, signPowZero;
        double aRandom = Math.random(); // randomize to see if the "a" value is a fraction, one, or another number
        if (aRandom > .6) {
            xPowOne = Integer.toString(Scenario.rng.nextInt(9) + 1) + "x/" + Integer
                    .toString(Scenario.rng.nextInt(9) + 1);
        } else if (aRandom > .3) {
            xPowOne = "x";
        } else {
            xPowOne = Integer.toString(Scenario.rng.nextInt(5) + 1) + "x";
        }
        if (Math.random() > .5) { // randomze if it's positive or negative
            signPowOne = "-";
        } else {
            signPowOne = "";
        }

        xPowZero = Integer.toString(Scenario.rng.nextInt(10) + 1);
        if (Math.random() > .5) { // randomize if it's positive or negative
            signPowZero = "-";
        } else {
            signPowZero = "";
        }

        return new Equation(new ArrayList<String>() {
            {
                add(signPowOne + xPowOne);
                add(signPowZero + xPowZero);
            }
        });
    }

    private String solve(Equation eq) {
        // Equation eq = Equation.toEquation(input);
        String output = "";
        if (Fraction.isFraction(eq.termArray[0])) {
            output = Fraction.divideFraction("-" + eq.termArray[1],
                    MathFunctions.getFracCoefficient(eq.termArray[0])).toString();
        } else {
            output = MathFunctions
                    .division("-" + eq.termArray[1] + "/" + MathFunctions.getCoefficient(eq.termArray[0]));
        }
        return output;
    }
}
