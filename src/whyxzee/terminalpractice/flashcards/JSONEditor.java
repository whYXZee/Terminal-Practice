package whyxzee.terminalpractice.flashcards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
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

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Semaphore;

import whyxzee.terminalpractice.application.RunApplication;

public class JSONEditor extends JPanel implements ActionListener {
    Semaphore semaphore = new Semaphore(0);

    public static String subject = "";
    public static String set = "";
    public static boolean restrict = false;
    public static long beginningCharIndex = 0;
    // public static String file = "";
    public static String questions = "";
    public static String answers = "";
    boolean loop = true;
    File file = new File("./src/whyxzee/terminalpractice/flashcards/custom/.gitkeep");

    GridBagConstraints grid = new GridBagConstraints();

    JLabel subjectLabel = new JLabel("What is the name of the subject?");
    JLabel setLabel = new JLabel("What is the name of the set?");
    JLabel charIndexLabel = new JLabel("Which character would you like to check with the restriction? [starting at 0]");

    JTextField subjectText = new JTextField();
    JTextField setText = new JTextField();
    JButton addTerm = new JButton("Add Term");
    JButton doneButton = new JButton("Done");
    JLabel restrictLabel = new JLabel("Would you like to add the ability to select what should be chosen?");
    JRadioButton noRadioButton = new JRadioButton("No");
    JRadioButton yesRadioButton = new JRadioButton("Yes");
    ButtonGroup buttonGroup = new ButtonGroup();
    JFormattedTextField charIndexField = new JFormattedTextField(NumberFormat.getNumberInstance());
    JTextArea questionBox = new JTextArea(10, RunApplication.getColumns() * 2);
    JTextArea answerBox = new JTextArea(10, RunApplication.getColumns() * 2);

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
            @SuppressWarnings("unchecked")
            HashMap<String, String> terms = (HashMap<String, String>) jsonO.get("termList");
            Set<String> keySet = terms.keySet();
            questions = JSONTools.arrayListToString(keySet);
            answers = JSONTools.answersFromKey(terms, keySet);

            // Creating required text fields
            this.add(subjectLabel, grid);
            grid.gridx++;
            subjectText.setColumns(RunApplication.getColumns() * 2);
            subjectText.setText(subject);
            // subjectText.addActionListener(this);
            // subjectText.setActionCommand("subject");
            this.add(subjectText, grid);
            grid.gridy++;
            grid.gridx--;

            this.add(setLabel, grid);
            grid.gridx++;
            setText.setColumns(RunApplication.getColumns() * 2);
            setText.setText(set);
            // setText.addActionListener(this);
            // setText.setActionCommand("set");
            this.add(setText, grid);
            grid.gridy++;
            grid.gridx--;

            this.add(restrictLabel, grid);
            grid.gridx++;
            yesRadioButton.addActionListener(this);
            yesRadioButton.setActionCommand("yesRestrict");
            yesRadioButton.setSelected(restrict);
            noRadioButton.addActionListener(this);
            noRadioButton.setActionCommand("noRestrict");
            noRadioButton.setSelected(!restrict);
            buttonGroup.add(yesRadioButton);
            buttonGroup.add(noRadioButton);
            this.add(yesRadioButton, grid);
            grid.gridx++;
            this.add(noRadioButton, grid);
            grid.gridy++;
            grid.gridx--;
            grid.gridx--;

            this.add(charIndexLabel, grid);
            grid.gridx++;
            charIndexField.setColumns(RunApplication.getColumns() * 2);
            charIndexField.setValue(beginningCharIndex);
            // charIndexField.addActionListener(this);
            // charIndexField.setActionCommand("subject");
            this.add(charIndexField, grid);
            grid.gridy++;
            grid.gridx--;

            JLabel questionLabel = new JLabel("Questions:");
            this.add(questionLabel, grid);
            grid.gridx++;
            JLabel answerLabel = new JLabel("Answers:");
            this.add(answerLabel, grid);
            grid.gridy++;
            grid.gridx--;

            // Container container = new Container();
            // container.

            // this.add(questionBox, grid);
            JScrollPane questionScrollPane = new JScrollPane(questionBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(questionScrollPane, grid);
            questionBox.setText(questions);
            grid.gridx++;
            // this.add(answerBox, grid);
            JScrollPane answerScrollPane = new JScrollPane(answerBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            this.add(answerScrollPane, grid);
            answerBox.setText(answers);
            grid.gridy++;
            grid.gridx--;

            // Creating "done" button
            doneButton.setActionCommand("done");
            doneButton.setHorizontalTextPosition(AbstractButton.CENTER);
            doneButton.setVerticalTextPosition(AbstractButton.CENTER);
            doneButton.setPreferredSize(new Dimension(150, 25));
            doneButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
            doneButton.setMnemonic(KeyEvent.VK_D);
            doneButton.addActionListener(this);
            this.add(doneButton, grid);
            grid.gridy++;

            display();
            while (loop) {
                semaphore.acquire();
            }
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (semaphore.hasQueuedThreads()) {
            subject = subjectText.getText();
            set = setText.getText();
            questions = questionBox.getText();
            answers = answerBox.getText();
            beginningCharIndex = ((Number) charIndexField.getValue()).longValue();

            if (e.getActionCommand().equals("yesRestrict") || e.getActionCommand().equals("noRestrict")) {
                restrict = !restrict;
            } else if (e.getActionCommand().equals("done") && checkIfDone()) {
                JSONTools.editJSON(this.file);
                loop = false;
                RunApplication.gameEnum = RunApplication.Game.NONE;
                RunApplication.semaphore.release();
            }
            semaphore.release();
        }
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }

    private boolean checkIfDone() {
        System.out.println("checking if done");
        boolean returnFalse = true;
        if (subject.equals("")) {
            returnFalse = false;
        }
        if (set.equals("")) {
            returnFalse = false;
        }
        return returnFalse;
    }
}
