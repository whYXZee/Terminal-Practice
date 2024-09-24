package whyxzee.terminalpractice.scenarios;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import whyxzee.terminalpractice.application.AppConstants;

public class ScenarioUI extends JPanel {

    public ScenarioUI() throws InterruptedException {
    }

    public void display() {
        AppConstants.frame.setContentPane(new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        AppConstants.frame.setVisible(true);
    }
}
