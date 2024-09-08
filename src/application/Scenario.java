package application;

import java.util.Random;

import ui.scenarios.algebra.*;

public class Scenario {
    public static final Random rng = new Random();

    public static void runScenario() throws InterruptedException {
        switch (RunApplication.set) {
            case "algebraic memory":
                new AlgebraMemory().display();
                break;
        }
    }
}
