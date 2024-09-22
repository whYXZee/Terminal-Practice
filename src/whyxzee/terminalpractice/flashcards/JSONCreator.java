package whyxzee.terminalpractice.flashcards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import whyxzee.terminalpractice.application.AppConstants;

public class JSONCreator extends JPanel implements ActionListener {
    Semaphore semaphore = new Semaphore(0);

    // Variables
    public static boolean restrict = false;
    private boolean loop = true;
    private static int numQuestions = 0;
    private static int numAnswers = 0;
    public static long beginningCharIndex = 0;
    public static String subject = "";
    public static String set = "";
    public static String file = "";
    public static String questions = "";
    public static String answers = "";

    GridBagConstraints grid = new GridBagConstraints();

    // Labels
    JLabel subjectLabel = new JLabel("What is the name of the subject?");
    JLabel setLabel = new JLabel("What is the name of the set?");
    JLabel charIndexLabel = new JLabel("Which character would you like to check with the restriction? [starting at 0]");
    JLabel restrictLabel = new JLabel("Would you like to add the ability to select what should be chosen?");

    // Text fields
    JTextField subjectText = new JTextField();
    JTextField setText = new JTextField();
    JTextField jsonName = new JTextField();
    JFormattedTextField charIndexField = new JFormattedTextField(NumberFormat.getNumberInstance());
    JTextArea questionBox = new JTextArea(10, AppConstants.jsonColumns);
    JTextArea answerBox = new JTextArea(10, AppConstants.jsonColumns);

    // Buttons
    JButton addTerm = new JButton("Add Term");
    JButton doneButton = new JButton("Done");
    JButton backButton = new JButton("Go back");
    ButtonGroup buttonGroup = new ButtonGroup();
    JRadioButton noRadioButton = new JRadioButton("No");
    JRadioButton yesRadioButton = new JRadioButton("Yes");

    JScrollPane scrollPane = new JScrollPane();

    public JSONCreator() throws InterruptedException {
        // Resetting the values of each text
        subject = "";
        set = "";
        restrict = false;
        questions = "";
        answers = "";
        file = "";
        numAnswers = 0;
        numQuestions = 0;

        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.EAST;

        // Subject
        subjectLabel.setFont(AppConstants.smallFont);
        this.add(subjectLabel, grid);
        grid.gridx++;
        subjectText.setActionCommand("subject");
        subjectText.addActionListener(this);
        subjectText.setColumns(AppConstants.jsonColumns);
        subjectText.setFont(AppConstants.smallFont);
        this.add(subjectText, grid);
        grid.gridy++;
        grid.gridx--;

        // Set name
        setLabel.setFont(AppConstants.smallFont);
        this.add(setLabel, grid);
        grid.gridx++;
        setText.setActionCommand("set");
        setText.addActionListener(this);
        setText.setColumns(AppConstants.jsonColumns);
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

        // File name
        JLabel nameLabel = new JLabel("What is the name of the file?");
        nameLabel.setFont(AppConstants.smallFont);
        this.add(nameLabel, grid);
        grid.gridx++;
        jsonName.setColumns(AppConstants.jsonColumns);
        jsonName.setText(file);
        jsonName.setFont(AppConstants.smallFont);
        this.add(jsonName, grid);
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
        answerBox.setFont(AppConstants.smallFont);
        answerBox.setText(answers);
        grid.gridy++;
        grid.gridx--;

        // Creating "done" button
        doneButton.setActionCommand("done");
        doneButton.addActionListener(this);
        doneButton.setMnemonic(KeyEvent.VK_D);
        doneButton.setPreferredSize(AppConstants.smallButtonDimension);
        doneButton.setFont(AppConstants.medFont);
        doneButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
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

        while (loop) {
            display();
            semaphore.acquire();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (semaphore.hasQueuedThreads()) {
            String action = e.getActionCommand();
            subject = subjectText.getText();
            set = setText.getText();
            file = jsonName.getText();
            questions = questionBox.getText();
            answers = answerBox.getText();
            beginningCharIndex = ((Number) charIndexField.getValue()).longValue();

            if (action.equals("back")) {
                loop = false;
                AppConstants.gameEnum = AppConstants.Game.NONE;
                AppConstants.semaphore.release();
            } else if (action.equals("yesRestrict") || (action.equals("noRestrict"))) {
                restrict = !restrict;
            } else if (action.equals("done") && checkIfDone()) {
                try {
                    JSONTools.createJSON();
                    loop = false;
                    AppConstants.gameEnum = AppConstants.Game.NONE;
                    AppConstants.semaphore.release();
                } catch (FileNotFoundException error) {
                }
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

        // Checking what is missing
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
        if (file.equals("")) {
            returnFalse = false;
            missing.add("no file name argument");
        }
        if (numAnswers != numQuestions) {
            returnFalse = false;
            missing.add("there are an unequal amount of questions and answers");
        }
        if (numAnswers == 0 && numQuestions == 0) {
            returnFalse = false;
            missing.add("there are no questions nor answers.");
        }

        if (!returnFalse) {
            JOptionPane.showMessageDialog(AppConstants.frame,
                    "Error: " + missing + ".", "Missing arguments", JOptionPane.ERROR_MESSAGE);
        }

        return returnFalse;
    }
}
