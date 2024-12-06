package whyxzee.terminalpractice.scenarios;

import whyxzee.terminalpractice.application.AppConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Collections;

public class RunOmegaScenario extends JPanel implements ActionListener {
    public RunOmegaScenario() throws InterruptedException {
        // Scenarios
        ArrayList<ScenarioUI> scenarios = new ArrayList<ScenarioUI>();

        // Timer
        boolean repeatTimerQuestion = true;
        while (repeatTimerQuestion) {
            String timerString = JOptionPane.showInputDialog(
                    "How many seconds do you want on each question?\n(leave blank or X out if you don't want a timer)",
                    0);
            if (timerString == null || timerString.equals("0")) {
                repeatTimerQuestion = false;
                ScenarioTools.timerEnabled = false;
            } else {
                try {
                    ScenarioTools.timerInMS = Integer.valueOf(timerString);
                    ScenarioTools.timerEnabled = true;
                    repeatTimerQuestion = false;

                    // to avoid going over an hour
                    if (ScenarioTools.timerInMS > 3600) {
                        ScenarioTools.timerInMS = 3600;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(AppConstants.frame, "Invalid input, please put in a number.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        for (String i : AppConstants.compiledScenarios) {
            scenarios.add(ScenarioTools.getScenario(i));
        }

        ScenarioUI currentScenario = null;
        AppConstants.isOmega = true;

        for (int i = 0; i < AppConstants.goal; i++) {
            // Randomizes the scenario
            Collections.shuffle(scenarios);
            currentScenario = scenarios.get(0);

            currentScenario.omega(i);

            if (!AppConstants.isOmega) {
                break;
            }
        }

        // add counter for correct questions
        // currentScenario.closingMessage();
        AppConstants.semaphore.release();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
