package whyxzee.terminalpractice.scenarios;

import javax.swing.JPanel;

import whyxzee.terminalpractice.application.AppConstants;

public class ScenarioUI extends JPanel {

    public ScenarioUI() throws InterruptedException {
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
    }
}
