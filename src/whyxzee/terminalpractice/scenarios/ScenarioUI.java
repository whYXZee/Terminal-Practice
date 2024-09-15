package whyxzee.terminalpractice.scenarios;

import javax.swing.JPanel;

import whyxzee.terminalpractice.application.RunApplication;

public class ScenarioUI extends JPanel {

    public ScenarioUI() throws InterruptedException {
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
