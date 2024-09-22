package whyxzee.terminalpractice.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import whyxzee.terminalpractice.flashcards.FlashcardConstants;
import whyxzee.terminalpractice.flashcards.JSONTools;

import java.util.ArrayList;
import java.util.Collections;
// import java.util.HashMap;
import java.util.List;
import java.util.Set;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class SetScenarioUI extends JPanel implements ActionListener {
    List<JButton> buttonList = new ArrayList<JButton>();

    public SetScenarioUI(Set<String> inputSet) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        // Organizing the list of strings
        List<String> list = new ArrayList<String>(inputSet);
        Collections.sort(list);

        // Creating buttons from hashmap
        for (String i : list) {
            buttonList.add(new JButton(AppConstants.capitalize(i)));
        }

        JLabel setLabel = new JLabel();
        switch (AppConstants.gameEnum) {
            case SCENARIOS:
                setLabel = new JLabel("Choose a scenario:");
                break;
            default:
                setLabel = new JLabel("Choose a set:");
                break;
        }
        setLabel.setFont(AppConstants.bigFont);
        this.add(setLabel, grid);
        grid.gridy++;

        // adding data for every button
        for (int i = 0; i < buttonList.size(); i++) {
            JButton index = buttonList.get(i);
            index.addActionListener(this);
            index.setActionCommand(index.getText().toLowerCase());

            index.setPreferredSize(AppConstants.wideButtonDimension);
            index.setFont(AppConstants.medFont);

            this.add(index, grid);
            grid.gridy++;
        }

        // Back button
        JButton backButton = new JButton("Go Back");
        backButton.setActionCommand("back");
        backButton.addActionListener(this);
        backButton.setFont(AppConstants.smallFont);
        this.add(backButton, grid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("back")) {
            new SubjectUI(AppConstants.subjectSet).display();
        } else {
            for (JButton i : buttonList) {
                if (e.getActionCommand().equals(i.getText().toLowerCase())) {
                    AppConstants.set = i.getText().toLowerCase();
                }
            }
            switch (AppConstants.gameEnum) {
                case DRILLS:
                    AppConstants.json = FlashcardConstants.flashcardHashMap.get(AppConstants.subject)
                            .get(AppConstants.set);
                    new ConfigureGoal(JSONTools.getCustomHashMap(AppConstants.json).size()).display();
                    break;
                case SCENARIOS:
                    new ConfigureGoal(0).display();
                    break;
                case JSON_EDITOR:
                    AppConstants.semaphore.release();
                    break;
                case CUSTOM_DRILLS:
                    AppConstants.json = JSONTools.getJSONPath(AppConstants.subject, AppConstants.set,
                            "./src/whyxzee/terminalpractice/flashcards/custom/");
                    new ConfigureGoal(JSONTools.getCustomHashMap(AppConstants.json).size()).display();
                    break;
                default:
                    break;
            }

        }
    }

    public void display() {
        AppConstants.frame.setContentPane(new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        AppConstants.frame.setVisible(true);
    }
}
