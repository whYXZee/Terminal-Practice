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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import application.RunApplication;

public class JSONCreator extends JPanel implements ActionListener {
    boolean isAllFilled = false;
    boolean creating = true;
    int termNumber = 1;

    Semaphore semaphore = new Semaphore(0);

    String subject = "";
    String set = "";
    String restrict = "";
    boolean yesSelected = false;
    String file = "";

    JTextField[] questions = { new JTextField() };
    String[] questionAmounts = { "" };
    JTextField[] answers = { new JTextField() };
    String[] answerAmounts = { "" };

    GridBagConstraints grid = new GridBagConstraints();

    JLabel subjectLabel = new JLabel("What is the name of the subject?");
    JLabel setLabel = new JLabel("What is the name of the set?");

    JTextField subjectText = new JTextField();
    JTextField setText = new JTextField();
    JTextField jsonName = new JTextField();
    JButton addTerm = new JButton("Add Term");
    JButton doneButton = new JButton("Done");

    public JSONCreator() throws InterruptedException {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.EAST;

        while (creating) {
            // Creating required text fields
            this.add(subjectLabel, grid);
            grid.gridx++;
            subjectText.setColumns(10);
            subjectText.setText(subject);
            subjectText.addActionListener(new actionListener());
            subjectText.setActionCommand("subject");
            this.add(subjectText, grid);
            grid.gridy++;
            grid.gridx--;

            this.add(setLabel, grid);
            grid.gridx++;
            setText.setColumns(10);
            setText.setText(set);
            setText.addActionListener(new actionListener());
            setText.setActionCommand("set");
            this.add(setText, grid);
            grid.gridy++;
            grid.gridx--;

            JLabel restrictLabel = new JLabel("Would you like to add the ability to select what should be chosen?");
            this.add(restrictLabel, grid);
            grid.gridx++;
            JRadioButton yesRadioButton = new JRadioButton("Yes");
            yesRadioButton.addActionListener(new actionListener());
            yesRadioButton.setActionCommand("yesRestrict");
            yesRadioButton.setSelected(yesSelected);
            JRadioButton noRadioButton = new JRadioButton("No");
            noRadioButton.addActionListener(new actionListener());
            noRadioButton.setActionCommand("noRestrict");
            noRadioButton.setSelected(!yesSelected);
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(yesRadioButton);
            buttonGroup.add(noRadioButton);
            this.add(yesRadioButton, grid);
            grid.gridx++;
            this.add(noRadioButton, grid);
            grid.gridy++;
            grid.gridx--;
            grid.gridx--;

            JLabel nameLabel = new JLabel("What is the name of the file?");
            this.add(nameLabel, grid);
            grid.gridx++;
            jsonName.setColumns(10);
            jsonName.setText(file);
            this.add(jsonName, grid);
            grid.gridy++;
            grid.gridx--;

            JLabel questionLabel = new JLabel("Questions:");
            this.add(questionLabel, grid);
            grid.gridx++;
            JLabel answerLabel = new JLabel("Answers:");
            this.add(answerLabel, grid);
            grid.gridy++;
            grid.gridx--;

            System.out.println("reality check");

            for (int i = 0; i < questions.length; i++) {
                System.out.println("adding to the UI: " + i);
                JTextField question = questions[i];
                question.setColumns(10);
                question.setText(questionAmounts[i]);
                this.add(question, grid);
                grid.gridx++;

                JTextField answer = answers[i];
                answer.setColumns(10);
                answer.setText(answerAmounts[i]);
                this.add(answer, grid);
                grid.gridy++;
                grid.gridx--;
            }

            // Creating "add term" button
            addTerm.setActionCommand("addTerm");
            addTerm.setHorizontalTextPosition(AbstractButton.CENTER);
            addTerm.setVerticalTextPosition(AbstractButton.CENTER);
            addTerm.setPreferredSize(new Dimension(150, 25));
            addTerm.setToolTipText("Answer questions regarding pre-made flashcard content.");
            addTerm.setMnemonic(KeyEvent.VK_A);
            addTerm.addActionListener(new actionListener());
            this.add(addTerm, grid);
            grid.gridy++;

            // Creating "done" button
            doneButton.setActionCommand("done");
            doneButton.setHorizontalTextPosition(AbstractButton.CENTER);
            doneButton.setVerticalTextPosition(AbstractButton.CENTER);
            doneButton.setPreferredSize(new Dimension(150, 25));
            doneButton.setToolTipText("Answer questions regarding pre-made flashcard content.");
            doneButton.setMnemonic(KeyEvent.VK_D);
            doneButton.addActionListener(new actionListener());
            doneButton.setEnabled(isAllFilled);
            this.add(doneButton, grid);
            grid.gridy++;

            display();
            semaphore.acquire();
            this.removeAll();
        }
    }

    static class actionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            subject = subjectText.getText();
            set = setText.getText();
            file = jsonName.getText();
    
            if (e.getActionCommand().equals("yesRestrict")) {
                restrict = "y";
                yesSelected = true;
            } else if (e.getActionCommand().equals("noRestrict")) {
                restrict = "n";
                yesSelected = false;
            }
            for (int i = 0; i < questions.length; i++) {
                questionAmounts[i] = questions[i].getText();
                answerAmounts[i] = answers[i].getText();
            }
    
            if (e.getActionCommand().equals("addTerm")) {
                questionAmounts = Arrays.asList(questionAmounts).toArray(new String[questionAmounts.length + 1]);
                questions = Arrays.asList(questions).toArray(new JTextField[questions.length + 1]);
                answerAmounts = Arrays.asList(answerAmounts).toArray(new String[answerAmounts.length + 1]);
                answers = Arrays.asList(answers).toArray(new JTextField[answers.length + 1]);
    
                questionAmounts[questionAmounts.length - 1] = "";
                questions[questions.length - 1] = new JTextField();
                answerAmounts[answerAmounts.length - 1] = "";
                answers[answers.length - 1] = new JTextField();
                System.out.println("ran");
            }

            isAllFilled = checkIfDone();
    
            if (e.getActionCommand().equals("done")) {
                creating = false;
            }
            semaphore.release();
        }
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }

    private boolean checkIfDone() {
        boolean returnFalse = false;
        if (subject.equals("")) {
            returnFalse = true;
        }
        if (set.equals("")) {
            returnFalse = true;
        }
        if (file.equals("")) {
            returnFalse = true;
        }
        if (restrict.equals("")) {
            returnFalse = true;
        }
        return !returnFalse;
    }
}
