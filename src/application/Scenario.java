package application;

import java.util.Random;

import ui.scenarios.algebra.*;
import ui.scenarios.physics.*;

public class Scenario {
    public static final Random rng = new Random();

    public static void runScenario() throws InterruptedException {
        switch (RunApplication.set) {
            case "algebraic memory":
                new AlgebraMemory().display();
                break;
            case "simplify equations":
                new SimplifyEquation().display();
                break;
            case "solve for x (linear)":
                new SolveForXLinear().display();
                break;

            case "projectile motion":
                new ProjectileMotion().display();
        }
    }
}
