package whyxzee.terminalpractice.scenarios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

// use properties class and .config for scenario settings

public class ScenarioSettings extends JPanel implements ActionListener {
    //
    // Labels
    //
    JLabel timerLabel = new JLabel("Toggle timer");
    JLabel timerLengthLabel = new JLabel("Length of timer");
    JLabel answerChoiceLabel = new JLabel("Number of answer choices");
    JLabel maxNumLabel = new JLabel("Max number to generate");

    public ScenarioSettings() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
