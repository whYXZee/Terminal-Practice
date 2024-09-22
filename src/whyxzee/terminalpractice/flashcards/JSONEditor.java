package whyxzee.terminalpractice.flashcards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
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

public class JSONEditor extends JPanel implements ActionListener {
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

    GridBagConstraints grid = new GridBagConstraints();

    // Labels
    JLabel subjectLabel = new JLabel("What is the name of the subject?");
    JLabel setLabel = new JLabel("What is the name of the set?");
    JLabel restrictLabel = new JLabel("Would you like to add the ability to select what should be chosen?");
    JLabel charIndexLabel = new JLabel("Which character would you like to check with the restriction? [starting at 0]");

    // Text fields
    JTextField subjectText = new JTextField();
    JTextField setText = new JTextField();
    JFormattedTextField charIndexField = new JFormattedTextField(NumberFormat.getNumberInstance());
    JTextArea questionBox = new JTextArea(10, AppConstants.jsonColumns);
    JTextArea answerBox = new JTextArea(10, AppConstants.jsonColumns);

    // Buttons
    JButton addTerm = new JButton("Add Term");
    JButton doneButton = new JButton("Done");
    JButton backButton = new JButton("Go Back");
    ButtonGroup buttonGroup = new ButtonGroup();
    JRadioButton noRadioButton = new JRadioButton("No");
    JRadioButton yesRadioButton = new JRadioButton("Yes");

    JScrollPane scrollPane = new JScrollPane();

    public JSONEditor(File file) throws InterruptedException {
        this.file = file;
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.EAST;

        // Getting the variables
        try {
            JSONObject jsonO = (JSONObject) new JSONParser().parse(new FileReader(file));

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
            this.add(subjectLabel, grid);
            grid.gridx++;
            subjectText.setFont(AppConstants.smallFont);
            subjectText.setColumns(AppConstants.jsonColumns);
            subjectText.setText(subject);
            this.add(subjectText, grid);
            grid.gridy++;
            grid.gridx--;

            // Set name
            setLabel.setFont(AppConstants.smallFont);
            this.add(setLabel, grid);
            grid.gridx++;
            setText.setColumns(AppConstants.jsonColumns);
            setText.setText(set);
            setText.setFont(AppConstants.smallFont);
            this.add(setText, grid);
            grid.gridy++;
            grid.gridx--;

            // Restriction
            restrictLabel.setFont(AppConstants.smallFont);
            this.add(restrictLabel, grid);
            grid.gridx++;
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
            this.add(yesRadioButton, grid);
            grid.gridx++;
            this.add(noRadioButton, grid);
            grid.gridy++;
            grid.gridx -= 2;

            // Restriction index
            charIndexLabel.setFont(AppConstants.smallFont);
            this.add(charIndexLabel, grid);
            grid.gridx++;
            charIndexField.setColumns(AppConstants.jsonColumns);
            charIndexField.setValue(beginningCharIndex);
            charIndexField.setFont(AppConstants.smallFont);
            this.add(charIndexField, grid);
            grid.gridy++;
            grid.gridx--;

            // Questions and answers
            JLabel questionLabel = new JLabel("Questions:");
            questionLabel.setFont(AppConstants.smallFont);
            this.add(questionLabel, grid);
            grid.gridx++;
            JLabel answerLabel = new JLabel("Answers:");
            answerLabel.setFont(AppConstants.smallFont);
            this.add(answerLabel, grid);
            grid.gridy++;
            grid.gridx--;

            JScrollPane questionScrollPane = new JScrollPane(questionBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(questionScrollPane, grid);
            questionBox.setText(questions);
            questionBox.setFont(AppConstants.smallFont);
            grid.gridx++;
            JScrollPane answerScrollPane = new JScrollPane(answerBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(answerScrollPane, grid);
            answerBox.setText(answers);
            answerBox.setFont(AppConstants.smallFont);
            grid.gridy++;
            grid.gridx--;

            // Creating "done" button
            doneButton.setActionCommand("done");
            doneButton.setMnemonic(KeyEvent.VK_D);
            doneButton.setPreferredSize(AppConstants.smallButtonDimension);
            doneButton.setFont(AppConstants.medFont);
            doneButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
            doneButton.addActionListener(this);
            this.add(doneButton, grid);
            grid.gridy++;

            // Back button
            backButton.setActionCommand("back");
            backButton.addActionListener(this);
            backButton.setMnemonic(KeyEvent.VK_B);
            backButton.setPreferredSize(AppConstants.smallButtonDimension);
            backButton.setFont(AppConstants.medFont);
            backButton.setToolTipText("Go back to the menu.");
            this.add(backButton, grid);
            grid.gridy++;

            JOptionPane.showMessageDialog(AppConstants.frame,
                    "Terms:\n - Each question and answer are separated by a ';'\n - Multiple answers can be put by separating them with a ',' without spaces",
                    "Editor Information", JOptionPane.ERROR_MESSAGE);

            display();
            while (loop) {
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
            beginningCharIndex = ((Number) charIndexField.getValue()).longValue();

            if (action.equals("back")) {
                loop = false;
                AppConstants.gameEnum = AppConstants.Game.NONE;
                AppConstants.semaphore.release();
            } else if (action.equals("yesRestrict") || action.equals("noRestrict")) {
                restrict = !restrict;
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
    }

    private boolean checkIfDone() {
        // Updating values
        numQuestions = JSONTools.parseArrayList(questions).size();
        numAnswers = JSONTools.parseArrayList(answers).size();

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
        if (beginningCharIndex < 0) {
            returnFalse = false;
            missing.add("negative chararacter index");
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
}
