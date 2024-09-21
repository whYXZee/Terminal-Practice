package whyxzee.terminalpractice.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GameSelection extends JPanel implements ActionListener {
    boolean isCustomAvailable;

    // Constants
    JButton flashcardButton = new JButton("Flashcards");
    JButton drillsButton = new JButton("Drills");
    JButton scenariosButton = new JButton("Scenarios");
    JButton creatorButton = new JButton("Create Flashcards");
    JButton editorButton = new JButton("Edit Flashcards");
    JButton customDrillsButton = new JButton("Custom Drills");
    JButton shareButton = new JButton("Share Custom Flashcards");
    JButton importButton = new JButton("Import Custom Flashcards");
    JButton removeButton = new JButton("Remove Custom Flashcards");

    JButton[] buttonArray = { drillsButton, scenariosButton, creatorButton, editorButton, customDrillsButton };

    public GameSelection(boolean isCustomAvailable) {
        this.isCustomAvailable = isCustomAvailable;

        // Layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        RunApplication.getFontSize();

        // Creating buttons
        for (JButton i : buttonArray) {
            i.setActionCommand(i.getText());
            i.addActionListener(this);
            i.setPreferredSize(new Dimension(300, 50));
            i.setFont(new Font("Arial", Font.BOLD, RunApplication.fontSize / 3));

            this.add(i, grid);
            grid.gridy++;
        }

        drillsButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
        drillsButton.setMnemonic(KeyEvent.VK_D);

        scenariosButton.setToolTipText("Answer randomized questions.");
        scenariosButton.setMnemonic(KeyEvent.VK_S);

        // creatorButton.setMnemonic(KeyEvent.VK_B);
        creatorButton.setToolTipText("Create your own flashcard sets");

        editorButton.setMnemonic(KeyEvent.VK_E);
        editorButton.setEnabled(isCustomAvailable);
        if (editorButton.isEnabled()) {
            editorButton.setToolTipText("Edit custom flashcards.");
        } else {
            editorButton.setToolTipText("No custom flashcards detected.");
        }

        customDrillsButton.setEnabled(isCustomAvailable);
        if (customDrillsButton.isEnabled()) {
            customDrillsButton.setToolTipText("Answer questions regarding custom-made flashcards.");
        } else {
            customDrillsButton.setToolTipText("No custom flashcards detected.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Flashcards")) {

        } else if (action.equals("Drills")) {
            RunApplication.gameEnum = RunApplication.Game.DRILLS;
        } else if (action.equals("Scenarios")) {
            RunApplication.gameEnum = RunApplication.Game.SCENARIOS;
        } else if (action.equals("Create Flashcards")) {
            RunApplication.gameEnum = RunApplication.Game.JSON_CREATOR;
        } else if (action.equals("Edit Flashcards")) {
            RunApplication.gameEnum = RunApplication.Game.JSON_EDITOR;
        } else if (action.equals("Custom Drills")) {
            RunApplication.gameEnum = RunApplication.Game.CUSTOM_DRILLS;
        }
        RunApplication.semaphore.release();
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
