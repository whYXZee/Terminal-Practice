package whyxzee.terminalpractice.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import whyxzee.terminalpractice.flashcards.FlashcardConstants;
import whyxzee.terminalpractice.flashcards.JSONImport;
import whyxzee.terminalpractice.flashcards.JSONTools;
import whyxzee.terminalpractice.scenarios.ScenarioConstants;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class GameSelection extends JPanel implements ActionListener {
    GameDaemon daemon;
    Semaphore gameSemaphore = new Semaphore(0);
    // Vars
    boolean isCustomAvailable;

    // Labels
    JLabel menuLabel = new JLabel("Terminal Practice");
    JLabel splashText = new JLabel(AppConstants.splash);

    // Button Constants
    JButton flashcardButton = new JButton("Flashcards");
    JButton drillsButton = new JButton("Drills");
    JButton scenariosButton = new JButton("Scenarios");
    JButton creatorButton = new JButton("Create Flashcards");
    JButton editorButton = new JButton("Edit Flashcards");
    JButton customFlashcardsButton = new JButton("Custom Flashcards");
    JButton customDrillsButton = new JButton("Custom Drills");
    JButton shareButton = new JButton("Share Custom Sets");
    JButton importButton = new JButton("Import Custom Sets");
    JButton removeButton = new JButton("Remove Custom Sets");
    JButton[] buttonArray = { flashcardButton, drillsButton, scenariosButton, customFlashcardsButton,
            customDrillsButton, creatorButton, editorButton, shareButton, importButton, removeButton };

    // Panels
    JPanel buttonPanel = new JPanel();

    public GameSelection(boolean isCustomAvailable) {
        this.isCustomAvailable = isCustomAvailable;

        daemon = new GameDaemon(this);
        daemon.setDaemon(true);
        daemon.start();

        // Layout
        this.setLayout(new GridBagLayout());

        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        // Creating buttons
        int tick = 0;
        for (JButton i : buttonArray) {
            i.setActionCommand(i.getText());
            i.addActionListener(this);
            i.setPreferredSize(AppConstants.smallButtonDimension);
            i.setFont(AppConstants.medFont);

            buttonPanel.add(i, grid);
            grid.gridy++;

            tick++;
            if (tick == 5) {
                tick = 0;
                grid.gridy -= 5;
                grid.gridx++;
            }
        }

        flashcardButton.setToolTipText("Go through pre-made flashcards.");
        flashcardButton.setMnemonic(KeyEvent.VK_F);

        drillsButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
        drillsButton.setMnemonic(KeyEvent.VK_D);

        scenariosButton.setToolTipText("Answer randomized questions.");
        scenariosButton.setMnemonic(KeyEvent.VK_S);

        customFlashcardsButton.setEnabled(isCustomAvailable);
        if (customFlashcardsButton.isEnabled()) {
            customFlashcardsButton.setToolTipText("Go through custom-made flashcards.");
        } else {
            customFlashcardsButton.setToolTipText("No custom sets detected.");
        }

        customDrillsButton.setEnabled(isCustomAvailable);
        if (customDrillsButton.isEnabled()) {
            customDrillsButton.setToolTipText("Answer questions regarding custom-made sets.");
        } else {
            customDrillsButton.setToolTipText("No custom sets detected.");
        }

        // creatorButton.setMnemonic(KeyEvent.VK_B);
        creatorButton.setToolTipText("Create your own flashcard and drill sets.");

        editorButton.setMnemonic(KeyEvent.VK_E);
        editorButton.setEnabled(isCustomAvailable);
        if (editorButton.isEnabled()) {
            editorButton.setToolTipText("Edit custom sets.");
        } else {
            editorButton.setToolTipText("No custom sets detected.");
        }

        shareButton.setEnabled(isCustomAvailable);
        if (shareButton.isEnabled()) {
            shareButton.setToolTipText("Share custom sets.");
        } else {
            shareButton.setToolTipText("No custom sets detected.");
        }

        importButton.setToolTipText("Import custom sets to be used.");
        importButton.setMnemonic(KeyEvent.VK_I);

        // removeButton.setEnabled(isCustomAvailable);
        // if (shareButton.isEnabled()) {
        // shareButton.setToolTipText("Remove custom sets.");
        // } else {
        // shareButton.setToolTipText("No custom sets detected.");
        // }
        removeButton.setEnabled(false);
        removeButton.setToolTipText("Coming soon!");

        // Labels
        menuLabel.setFont(AppConstants.biggerFont);
        splashText.setFont(AppConstants.medFont);

        // Adding everything
        grid.gridy = 0;
        grid.gridx = 0;

        this.add(menuLabel, grid);
        grid.gridy++;
        this.add(splashText, grid);
        grid.gridy++;
        this.add(buttonPanel, grid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Flashcards")) {
            AppConstants.gameEnum = AppConstants.Game.FLASHCARDS;
            AppConstants.subjectSet = FlashcardConstants.flashcardHashMap.keySet();
            new SubjectUI(AppConstants.subjectSet).display();

        } else if (action.equals("Drills")) {
            AppConstants.gameEnum = AppConstants.Game.DRILLS;
            AppConstants.subjectSet = FlashcardConstants.flashcardHashMap.keySet();
            new SubjectUI(AppConstants.subjectSet).display();

        } else if (action.equals("Scenarios")) {
            AppConstants.gameEnum = AppConstants.Game.SCENARIOS;
            AppConstants.subjectSet = ScenarioConstants.scenarioHashMap.keySet();
            new SubjectUI(AppConstants.subjectSet).display();

        } else if (action.equals("Create Flashcards")) {
            AppConstants.gameEnum = AppConstants.Game.JSON_CREATOR;
            AppConstants.semaphore.release();

        } else if (action.equals("Edit Flashcards")) {
            AppConstants.gameEnum = AppConstants.Game.JSON_EDITOR;
            AppConstants.subjectSet = new HashSet<String>(JSONTools.getCustomSubjects());
            new SubjectUI(AppConstants.subjectSet).display();

        } else if (action.equals("Custom Drills")) {
            AppConstants.gameEnum = AppConstants.Game.CUSTOM_DRILLS;
            AppConstants.subjectSet = new HashSet<String>(JSONTools.getCustomSubjects());
            new SubjectUI(AppConstants.subjectSet).display();

        } else if (action.equals("Custom Flashcards")) {
            AppConstants.gameEnum = AppConstants.Game.CUSTOM_FLASHCARDS;
            AppConstants.subjectSet = new HashSet<String>(JSONTools.getCustomSubjects());
            new SubjectUI(AppConstants.subjectSet).display();

        } else if (action.equals("Share Custom Sets")) {
            AppConstants.gameEnum = AppConstants.Game.JSON_SHARING;
            AppConstants.subjectSet = new HashSet<String>(JSONTools.getCustomSubjects());
            new SubjectUI(AppConstants.subjectSet).display();

        } else if (action.equals("Import Custom Sets")) {
            AppConstants.gameEnum = AppConstants.Game.JSON_IMPORTING;
            new JSONImport().display();
            AppConstants.semaphore.release();

        } else if (action.equals("Remove Custom Sets")) {
            AppConstants.gameEnum = AppConstants.Game.JSON_DELETION;
            AppConstants.subjectSet = new HashSet<String>(JSONTools.getCustomSubjects());
            new SubjectUI(AppConstants.subjectSet).display();
        }
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
    }

    public void resize() {
        for (JButton i : buttonArray) {
            i.setFont(AppConstants.medFont);
            i.setPreferredSize(AppConstants.smallButtonDimension);
        }

        menuLabel.setFont(AppConstants.biggerFont);
        splashText.setFont(AppConstants.medFont);
    }
}

class GameDaemon extends Thread {
    private GameSelection ui;

    public GameDaemon(GameSelection ui) {
        super("MenuFrameDaemon");
        this.ui = ui;
    }

    public void run() {
        boolean shouldRun = true;
        while (shouldRun) {
            switch (AppConstants.gameEnum) {
                case NONE:
                    ui.resize();
                    break;
                default:
                    shouldRun = false;
                    break;
            }
        }
    }
}
