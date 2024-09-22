package whyxzee.terminalpractice.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import whyxzee.terminalpractice.flashcards.FlashcardConstants;
import whyxzee.terminalpractice.flashcards.JSONTools;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class SubjectUI extends JPanel implements ActionListener {
    List<JButton> buttonList = new ArrayList<JButton>();

    public SubjectUI(Set<String> inputSet) {

        // Layout
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

        JLabel subjectLabel = new JLabel("Choose a subject:");
        subjectLabel.setFont(AppConstants.bigFont);
        this.add(subjectLabel, grid);
        grid.gridy++;

        // adding data for every button
        for (JButton i : buttonList) {
            i.setActionCommand(i.getText().toLowerCase());
            i.addActionListener(this); // Makes the buttons work
            i.setFont(AppConstants.medFont);
            i.setPreferredSize(AppConstants.wideButtonDimension);

            this.add(i, grid); // adds the button to the panel
            grid.gridy++;
        }

        // Back button
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(this);
        backButton.setActionCommand("back");
        backButton.setFont(AppConstants.smallFont);
        this.add(backButton, grid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("back")) {
            new GameSelection(AppConstants.checkCustom()).display();
        } else {
            for (JButton i : buttonList) {
                if (e.getActionCommand().equals(i.getText().toLowerCase())) {
                    AppConstants.subject = i.getText().toLowerCase();
                }
            }
            switch (AppConstants.gameEnum) {
                case DRILLS:
                    new SetScenarioUI(FlashcardConstants.flashcardHashMap.get(AppConstants.subject).keySet())
                            .display();
                    break;
                case SCENARIOS:
                    new SetScenarioUI(
                            new HashSet<String>(ScenarioConstants.scenarioHashMap.get(AppConstants.subject)))
                            .display();
                    break;
                case JSON_EDITOR:
                    new SetScenarioUI(new HashSet<String>(JSONTools.getCustomSets(AppConstants.subject))).display();
                    break;
                case CUSTOM_DRILLS:
                    new SetScenarioUI(new HashSet<String>(JSONTools.getCustomSets(AppConstants.subject))).display();
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