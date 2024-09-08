package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

import application.RunApplication;

public class GameSelection extends JPanel implements ActionListener {
    boolean isCustomAvailable;

    public GameSelection(boolean isCustomAvailable) {
        this.isCustomAvailable = isCustomAvailable;

        // Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        // Creating buttons
        JButton drillsButton = new JButton("Drills");
        drillsButton.setActionCommand("drills");
        drillsButton.setHorizontalTextPosition(AbstractButton.CENTER);
        drillsButton.setVerticalTextPosition(AbstractButton.CENTER);
        drillsButton.setPreferredSize(new Dimension(150, 25));
        drillsButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
        drillsButton.setMnemonic(KeyEvent.VK_D);

        JButton scenariosButton = new JButton("Scenarios");
        scenariosButton.setActionCommand("scenarios");
        scenariosButton.setHorizontalTextPosition(AbstractButton.CENTER);
        scenariosButton.setVerticalTextPosition(AbstractButton.CENTER);
        scenariosButton.setPreferredSize(new Dimension(150, 25));
        scenariosButton.setToolTipText("Answer randomized questions.");
        scenariosButton.setMnemonic(KeyEvent.VK_S);

        JButton creatorButton = new JButton("Create Flashcards");
        creatorButton.setActionCommand("flashcard creator");
        creatorButton.setHorizontalTextPosition(AbstractButton.CENTER);
        creatorButton.setVerticalTextPosition(AbstractButton.CENTER);
        creatorButton.setPreferredSize(new Dimension(150, 25));
        creatorButton.setMnemonic(KeyEvent.VK_B);
        // creatorButton.setEnabled(false);
        creatorButton.setToolTipText("Currently unavailable, the UI isn't created yet.");

        JButton customDrills = new JButton("Custom Drills");
        customDrills.setActionCommand("custom drills");
        customDrills.setHorizontalTextPosition(AbstractButton.CENTER);
        customDrills.setVerticalTextPosition(AbstractButton.CENTER);
        customDrills.setPreferredSize(new Dimension(150, 25));
        customDrills.setMnemonic(KeyEvent.VK_C);
        customDrills.setEnabled(isCustomAvailable);
        if (customDrills.isEnabled()) {
            customDrills.setToolTipText("Answer questions regarding custom-made flashcards.");
        } else {
            customDrills.setToolTipText("No custom flashcards detected.");
        }

        // Listening functions, makes the buttons work
        drillsButton.addActionListener(this);
        scenariosButton.addActionListener(this);
        creatorButton.addActionListener(this);
        customDrills.addActionListener(this);

        // Adding the buttons to the screen
        this.add(drillsButton, grid);
        grid.gridy++;
        this.add(scenariosButton, grid);
        grid.gridy++;
        this.add(creatorButton, grid);
        grid.gridy++;
        this.add(customDrills, grid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("flashcards")) {

        } else if (e.getActionCommand().equals("drills")) {
            RunApplication.gameEnum = RunApplication.Game.DRILLS;
        } else if (e.getActionCommand().equals("scenarios")) {
            RunApplication.gameEnum = RunApplication.Game.SCENARIOS;
        } else if (e.getActionCommand().equals("custom drills")) {
            RunApplication.gameEnum = RunApplication.Game.CUSTOM_DRILLS;
        } else if (e.getActionCommand().equals("flashcard creator")) {
            RunApplication.gameEnum = RunApplication.Game.JSON_CREATOR;
        }
        RunApplication.semaphore.release();
    }

    public void display() {
        // this.setOpaque(true);
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
