package whyxzee.terminalpractice.application;

import whyxzee.terminalpractice.flashcards.FlashcardConstants;
import whyxzee.terminalpractice.flashcards.JSONTools;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class OmegaDrillSelection extends JPanel implements ActionListener {
    private static ODrillDaemon daemon;

    // Vars
    public static boolean activeScreen = false;
    List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
    List<JLabel> subjectLabels = new ArrayList<JLabel>();
    ArrayList<String> selectedSets = new ArrayList<String>();

    // UI Components
    JLabel setLabel = new JLabel();
    JButton doneButton = new JButton("Ready?");
    JButton backButton = new JButton("Go Back");

    public OmegaDrillSelection() {
        // Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        List<String> sets = new ArrayList<String>(FlashcardConstants.flashcardHashMap.keySet());
        sets.addAll(JSONTools.getCustomSubjects());
        List<Integer> setsInSubject = new ArrayList<Integer>();

        // Creating buttons from hashmap
        for (String i : sets) {
            // So the sets are alphabetized
            List<String> list = new ArrayList<String>();
            try {
                list = new ArrayList<String>(FlashcardConstants.flashcardHashMap.get(i).keySet());
            } catch (NullPointerException e) {
                list = new ArrayList<String>(JSONTools.getCustomSets(i));
            }
            Collections.sort(list);

            int tempSetsInSubject = 0;

            for (String j : list) {
                checkboxes.add(new JCheckBox(j));
                tempSetsInSubject++;
            }
            setsInSubject.add(tempSetsInSubject);
        }

        setLabel = new JLabel("Choose sets to practice:");
        setLabel.setFont(AppConstants.biggerFont);
        this.add(setLabel, grid);
        grid.gridy++;

        // adding data for every button
        int setIndex = 0;
        int subjectIndex = 0;
        for (String i : sets) {
            subjectLabels.add(new JLabel(i));
            subjectLabels.get(subjectIndex).setFont(AppConstants.bigFont);
            this.add(subjectLabels.get(subjectIndex), grid);
            grid.gridy++;

            for (int j = 0; j < setsInSubject.get(subjectIndex); j++) {
                JCheckBox checkboxIndex = checkboxes.get(setIndex);

                checkboxIndex.addActionListener(this);
                checkboxIndex.setActionCommand(checkboxIndex.getText());
                checkboxIndex.setFont(AppConstants.smallFont);

                this.add(checkboxIndex, grid);
                grid.gridy++;
                setIndex++;
            }

            subjectIndex++;
        }

        // done button
        doneButton.setActionCommand("done");
        doneButton.addActionListener(this);
        doneButton.setPreferredSize(AppConstants.wideButtonDimension);
        doneButton.setFont(AppConstants.medFont);
        this.add(doneButton, grid);
        grid.gridy++;

        // Back button
        backButton.setActionCommand("back");
        backButton.addActionListener(this);
        backButton.setFont(AppConstants.medFont);
        backButton.setPreferredSize(AppConstants.wideButtonDimension);
        this.add(backButton, grid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("back")) {
            AppConstants.gameEnum = AppConstants.Game.NONE;
            new GameSelection(AppConstants.checkCustom()).display();

        } else if (action.equals("done")) {
            for (JCheckBox i : checkboxes) {
                if (i.isSelected()) {
                    selectedSets.add(i.getText());
                }
            }

            if (selectedSets.size() == 0) {
                // if nothing is selected, don't move on
                JOptionPane.showMessageDialog(AppConstants.frame,
                        "No sets were selected. Please select at least one set before moving on.",
                        "Set Selection Error",
                        JOptionPane.ERROR_MESSAGE);

            } else {
                HashMap<String, String> finalSet = new HashMap<String, String>();
                for (String i : selectedSets) {
                    File setFile = FlashcardConstants.getFileGeneric(i);
                    if (setFile == null) {
                        for (JLabel j : subjectLabels) {
                            setFile = JSONTools.getJSONPath(j.getText(), i,
                                    "./src/whyxzee/terminalpractice/flashcards/custom/");
                            if (setFile != null) {
                                break;
                            }
                        }
                    }

                    if (setFile != null) {
                        finalSet.putAll(JSONTools.getCustomHashMap(setFile));
                    }
                }
                AppConstants.compiledHashMap = finalSet;
                activeScreen = false;
                AppConstants.semaphore.release();
            }
        }
    }

    public void display() {
        activeScreen = true;
        AppConstants.frame.setContentPane(new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        AppConstants.frame.setVisible(true);

        daemon = new ODrillDaemon(this);
        daemon.setDaemon(true);
        daemon.start();
    }

    public void resize() {
        setLabel.setFont(AppConstants.biggerFont);

        doneButton.setFont(AppConstants.medFont);
        doneButton.setPreferredSize(AppConstants.wideButtonDimension);

        backButton.setFont(AppConstants.medFont);
        backButton.setPreferredSize(AppConstants.wideButtonDimension);

        try {
            for (JLabel i : subjectLabels) {
                i.setFont(AppConstants.bigFont);
            }
            for (JCheckBox i : checkboxes) {
                i.setFont(AppConstants.smallFont);
            }
        } catch (java.util.ConcurrentModificationException e) {

        }
    }
}

class ODrillDaemon extends Thread {
    OmegaDrillSelection ui;

    public ODrillDaemon(OmegaDrillSelection ui) {
        super("Omega Drill Daemon");
        this.ui = ui;
    }

    public void run() {
        while (OmegaDrillSelection.activeScreen) {
            ui.resize();
        }
    }
}
