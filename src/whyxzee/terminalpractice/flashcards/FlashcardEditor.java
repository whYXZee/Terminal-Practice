package whyxzee.terminalpractice.flashcards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Semaphore;

import whyxzee.terminalpractice.application.AppConstants;

/**
 * UI menu for editing flashcards.
 */
public class FlashcardEditor extends JPanel implements ActionListener {
    JSONEditorDaemon daemon;
    Semaphore semaphore = new Semaphore(0);

    public static boolean restrict = false;
    private boolean loop = true;
    public static long beginningCharIndex = 0;
    private static int numAnswers = 0;
    private static int numQuestions = 0;
    public static String subject = "";
    public static String set = "";
    public static String questions = "";
    public static String answers = "";
    File file = new File("./src/whyxzee/terminalpractice/flashcards/custom/.gitkeep");

    // Labels
    JLabel gameLabel = new JLabel("Set Editor");
    JLabel subjectLabel = new JLabel("What is the name of the subject?");
    JLabel setLabel = new JLabel("What is the name of the set?");
    JLabel restrictLabel = new JLabel("Would you like to add the ability to select what should be chosen?");
    JLabel charIndexLabel = new JLabel(
            "Which character (including spaces) would you like to check for the restriction?");
    JLabel questionLabel = new JLabel("Questions:");
    JLabel answerLabel = new JLabel("Answers:");

    // Text fields
    JTextField subjectText = new JTextField();
    JTextField setText = new JTextField();
    JFormattedTextField charIndexField = new JFormattedTextField(NumberFormat.getNumberInstance());
    JTextArea questionBox = new JTextArea(10, JSONTools.jsonColumns);
    JTextArea answerBox = new JTextArea(10, JSONTools.jsonColumns);

    // Buttons
    JButton addTerm = new JButton("Add Term");
    JButton doneButton = new JButton("Done");
    JButton backButton = new JButton("Go Back");
    ButtonGroup buttonGroup = new ButtonGroup();
    JRadioButton noRadioButton = new JRadioButton("No");
    JRadioButton yesRadioButton = new JRadioButton("Yes");

    // Panels
    JPanel restrictionButtons = new JPanel();
    JPanel optionsPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    JScrollPane answerScrollPane = new JScrollPane();
    JScrollPane questionScrollPane = new JScrollPane();

    GridBagConstraints grid = new GridBagConstraints();
    GridBagConstraints optionsGrid = new GridBagConstraints();
    GridBagConstraints restrictGrid = new GridBagConstraints();

    public FlashcardEditor(File file) throws InterruptedException {
        this.file = file;
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        restrictionButtons.setLayout(new GridBagLayout());
        restrictGrid.gridx = 0;
        restrictGrid.gridy = 0;
        restrictGrid.insets = new Insets(8, 8, 8, 8);
        restrictGrid.anchor = GridBagConstraints.WEST;

        optionsPanel.setLayout(new GridBagLayout());
        optionsPanel.setPreferredSize(JSONTools.jsonDimension);
        optionsGrid.gridx = 0;
        optionsGrid.gridy = 0;
        optionsGrid.insets = new Insets(8, 8, 8, 8);
        optionsGrid.anchor = GridBagConstraints.EAST;

        // Getting the variables
        try {
            JSONObject jsonO = (JSONObject) new JSONParser().parse(new FileReader(file));

            gameLabel.setFont(AppConstants.biggerFont);
            this.add(gameLabel, grid);
            grid.gridy++;

            // Setting the variables
            subject = (String) jsonO.get("subject");
            set = (String) jsonO.get("setName");
            restrict = (boolean) jsonO.get("restrictLetters");
            beginningCharIndex = (long) jsonO.get("beginningCharIndex");
            numAnswers = 0;
            numQuestions = 0;

            @SuppressWarnings("unchecked")
            HashMap<String, String> terms = (HashMap<String, String>) jsonO.get("termList");
            Set<String> keySet = terms.keySet();
            questions = JSONTools.arrayListToString(keySet);
            answers = JSONTools.answersFromKey(terms, keySet);

            // Subject
            subjectLabel.setFont(AppConstants.smallFont);
            optionsPanel.add(subjectLabel, optionsGrid);
            optionsGrid.gridx++;
            subjectText.setFont(AppConstants.smallFont);
            subjectText.setColumns(JSONTools.jsonColumns);
            subjectText.setText(subject);
            optionsPanel.add(subjectText, optionsGrid);
            optionsGrid.gridy++;
            optionsGrid.gridx--;

            // Set name
            setLabel.setFont(AppConstants.smallFont);
            optionsPanel.add(setLabel, optionsGrid);
            optionsGrid.gridx++;
            setText.setColumns(JSONTools.jsonColumns);
            setText.setText(set);
            setText.setFont(AppConstants.smallFont);
            optionsPanel.add(setText, optionsGrid);
            optionsGrid.gridy++;
            optionsGrid.gridx--;

            // Restriction
            restrictLabel.setFont(AppConstants.smallFont);
            optionsPanel.add(restrictLabel, optionsGrid);
            optionsGrid.gridx++;
            yesRadioButton.addActionListener(this);
            yesRadioButton.setActionCommand("yesRestrict");
            yesRadioButton.setSelected(restrict);
            yesRadioButton.setFont(AppConstants.smallFont);
            noRadioButton.addActionListener(this);
            noRadioButton.setActionCommand("noRestrict");
            noRadioButton.setSelected(!restrict);
            noRadioButton.setFont(AppConstants.smallFont);
            buttonGroup.add(yesRadioButton);
            buttonGroup.add(noRadioButton);
            restrictionButtons.add(yesRadioButton, restrictGrid);
            restrictGrid.gridx++;
            restrictionButtons.add(noRadioButton, restrictGrid);
            optionsPanel.add(restrictionButtons, optionsGrid);
            optionsGrid.gridy++;
            optionsGrid.gridx--;

            // Restriction index
            charIndexLabel.setFont(AppConstants.smallFont);
            optionsPanel.add(charIndexLabel, optionsGrid);
            optionsGrid.gridx++;
            charIndexField.setColumns(JSONTools.jsonColumns);
            charIndexField.setValue(beginningCharIndex + 1);
            charIndexField.setFont(AppConstants.smallFont);
            optionsPanel.add(charIndexField, optionsGrid);
            optionsGrid.gridy++;
            optionsGrid.gridx--;

            // Questions and answers
            questionLabel.setFont(AppConstants.smallFont);
            optionsPanel.add(questionLabel, optionsGrid);
            optionsGrid.gridx++;
            answerLabel.setFont(AppConstants.smallFont);
            optionsPanel.add(answerLabel, optionsGrid);
            optionsGrid.gridy++;
            optionsGrid.gridx--;

            questionScrollPane = new JScrollPane(questionBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            optionsPanel.add(questionScrollPane, optionsGrid);
            questionBox.setText(questions);
            questionBox.setFont(AppConstants.smallFont);
            optionsGrid.gridx++;
            answerScrollPane = new JScrollPane(answerBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            optionsPanel.add(answerScrollPane, optionsGrid);
            answerBox.setText(answers);
            answerBox.setFont(AppConstants.smallFont);
            optionsGrid.gridy++;
            optionsGrid.gridx--;

            this.add(optionsPanel, grid);
            grid.gridy++;

            optionsGrid.gridy = 0;
            optionsGrid.gridx = 0;

            // Creating "done" button
            doneButton.setActionCommand("done");
            doneButton.addActionListener(this);
            doneButton.setMnemonic(KeyEvent.VK_D);
            doneButton.setPreferredSize(AppConstants.smallButtonDimension);
            doneButton.setFont(AppConstants.medFont);
            doneButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
            buttonsPanel.add(doneButton, optionsGrid);
            optionsGrid.gridx++;

            // Back button
            backButton.setActionCommand("back");
            backButton.addActionListener(this);
            backButton.setMnemonic(KeyEvent.VK_B);
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setFont(AppConstants.medFont);
            backButton.setToolTipText("Go back to the menu.");
            buttonsPanel.add(backButton, optionsGrid);
            optionsGrid.gridx++;

            this.add(buttonsPanel, grid);

            JOptionPane.showMessageDialog(AppConstants.frame,
                    "Terms:\n - Each question and answer are separated by a ';'\n - Multiple answers can be put by separating them with a ',' without spaces",
                    "Editor Information", JOptionPane.INFORMATION_MESSAGE);

            display();

            // daemon = new JSONEditorDaemon(this);
            // daemon.setDaemon(true);
            // daemon.start();

            while (loop) {
                // From the charIndex
                charIndexField.setEnabled(restrict);

                semaphore.acquire();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (semaphore.hasQueuedThreads()) {
            String action = e.getActionCommand();
            subject = subjectText.getText();
            set = setText.getText();
            questions = questionBox.getText();
            answers = answerBox.getText();
            beginningCharIndex = ((Number) charIndexField.getValue()).longValue() - 1;

            if (action.equals("back")) {
                loop = false;
                AppConstants.gameEnum = AppConstants.Game.NONE;
                AppConstants.semaphore.release();
            } else if (action.equals("yesRestrict") || action.equals("noRestrict")) {
                restrict = !restrict;
                beginningCharIndex = 0;
                charIndexField.setValue(beginningCharIndex + 1);
            } else if (action.equals("done") && checkIfDone()) {
                JSONTools.editJSON(this.file);
                loop = false;
                AppConstants.gameEnum = AppConstants.Game.NONE;
                AppConstants.semaphore.release();
            }
            semaphore.release();
        }
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);

        daemon = new JSONEditorDaemon(this);
        daemon.setDaemon(true);
        daemon.start();
    }

    private boolean checkIfDone() {
        // Updating values
        numQuestions = JSONTools.parseArrayList(questions).size();

        // Checking answers and index numbers
        ArrayList<String> answerList = JSONTools.parseArrayList(answers);
        numAnswers = answerList.size();
        int maxChar = 0;
        for (String i : answerList) {
            int maxCharsInAnswer = new AnswerSet(i).maxCharactersInAnswer(maxChar);
            if (maxCharsInAnswer > maxChar) {
                maxChar = maxCharsInAnswer;
            }
        }

        // Setting vars
        boolean returnFalse = true;
        ArrayList<String> missing = new ArrayList<String>();

        if (subject.equals("")) {
            returnFalse = false;
            missing.add("no subject argument");
        }
        if (set.equals("")) {
            returnFalse = false;
            missing.add("no set name argument");
        }
        if ((beginningCharIndex == -1) && restrict) {
            returnFalse = false;
            missing.add("zero character index");
        } else if ((beginningCharIndex < 0) && restrict) {
            returnFalse = false;
            missing.add("negative chararacter index");
        }
        if ((beginningCharIndex > maxChar) && restrict) {
            returnFalse = false;
            missing.add("beginning character index is greater than the maximum characters in an answer.");
        }
        if (numAnswers != numQuestions) {
            returnFalse = false;
            missing.add("there are an unequal amount of questions and answers");
        }
        if (numAnswers == 0 && numQuestions == 0) {
            returnFalse = false;
            missing.add("there are no questions nor answers");
        }

        if (!returnFalse) {
            JOptionPane.showMessageDialog(AppConstants.frame,
                    "Error: " + missing + ".", "Missing arguments", JOptionPane.ERROR_MESSAGE);
        }
        return returnFalse;
    }

    /**
     * Resizes the sizes of components.
     */
    public void resize() {
        buttonsPanel.setPreferredSize(new Dimension(AppConstants.flashcardDimension.width,
                AppConstants.smallButtonDimension.height + 16));
        optionsPanel.setPreferredSize(JSONTools.jsonDimension);

        gameLabel.setFont(AppConstants.biggerFont);

        subjectLabel.setFont(AppConstants.smallFont);
        subjectText.setColumns(JSONTools.jsonColumns);
        subjectText.setFont(AppConstants.smallFont);

        setLabel.setFont(AppConstants.smallFont);
        setText.setColumns(JSONTools.jsonColumns);
        setText.setFont(AppConstants.smallFont);

        restrictLabel.setFont(AppConstants.smallFont);
        yesRadioButton.setFont(AppConstants.smallFont);
        noRadioButton.setFont(AppConstants.smallFont);

        charIndexLabel.setFont(AppConstants.smallFont);
        charIndexField.setColumns(JSONTools.jsonColumns);
        charIndexField.setFont(AppConstants.smallFont);

        questionLabel.setFont(AppConstants.smallFont);
        answerLabel.setFont(AppConstants.smallFont);
        questionBox.setFont(AppConstants.smallFont);
        answerBox.setFont(AppConstants.smallFont);

        answerScrollPane.setPreferredSize(JSONTools.jsonTextBoxes);
        questionScrollPane.setPreferredSize(JSONTools.jsonTextBoxes);

        doneButton.setPreferredSize(AppConstants.smallButtonDimension);
        doneButton.setFont(AppConstants.medFont);

        backButton.setPreferredSize(AppConstants.smallButtonDimension);
        backButton.setFont(AppConstants.medFont);
    }
}

/**
 * The custom daemon for the JSON Creator.
 */
class JSONEditorDaemon extends Thread {
    private FlashcardEditor ui;

    public JSONEditorDaemon(FlashcardEditor ui) {
        super("EditorFrameDaemon");
        this.ui = ui;
    }

    public void run() {
        boolean shouldRun = true;
        while (shouldRun) {
            switch (AppConstants.gameEnum) {
                case JSON_EDITOR:
                    ui.resize();
                    break;
                default:
                    shouldRun = false;
                    break;
            }
        }
    }
}
