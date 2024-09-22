package whyxzee.terminalpractice.application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;

import whyxzee.terminalpractice.flashcards.JSONTools;
import whyxzee.terminalpractice.flashcards.RestrictTerms;
import whyxzee.terminalpractice.resources.English;

import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

import java.text.NumberFormat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ConfigureGoal extends JPanel implements PropertyChangeListener {
    JFormattedTextField goalField = new JFormattedTextField(NumberFormat.getNumberInstance());
    int setSize;
    boolean releasePause;
    GridBagConstraints grid = new GridBagConstraints();

    JLabel goalLabel = new JLabel();

    public ConfigureGoal(int setSize) {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        this.setSize = setSize;
        switch (AppConstants.gameEnum) {
            case DRILLS:
                goalLabel = new JLabel("How many terms would you like to practice? (The max terms is " + setSize + ")");
                goalLabel.setFont(AppConstants.medFont);
                this.add(goalLabel, grid);
                grid.gridy++;

                goalField.setValue(0);
                goalField.setFont(AppConstants.smallFont);
                goalField.setHorizontalAlignment(JFormattedTextField.CENTER);
                goalField.setColumns(AppConstants.answerColumns); // how big the text field is
                goalField.addPropertyChangeListener("value", this);
                this.add(goalField, grid);
                break;
            case SCENARIOS:
                goalLabel = new JLabel("How many questions would you like to practice?");
                this.add(goalLabel, grid);
                goalLabel.setFont(AppConstants.bigFont);
                grid.gridy++;

                goalField.setValue(0);
                goalField.setFont(AppConstants.smallFont);
                goalField.setColumns(AppConstants.answerColumns); // how big the text field is
                goalField.setHorizontalAlignment(JFormattedTextField.CENTER);
                goalField.addPropertyChangeListener("value", this);
                this.add(goalField, grid);
                grid.gridy++;
                break;
            case CUSTOM_DRILLS:
                goalLabel = new JLabel("How many terms would you like to practice? (The max terms is " + setSize + ")");
                goalLabel.setFont(AppConstants.medFont);
                this.add(goalLabel, grid);
                grid.gridy++;

                goalField.setValue(0);
                goalField.setFont(AppConstants.smallFont);
                goalField.setHorizontalAlignment(JFormattedTextField.CENTER);
                goalField.setColumns(AppConstants.answerColumns); // how big the text field is
                goalField.addPropertyChangeListener("value", this);
                this.add(goalField, grid);
                break;
            default:
                break;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Getting the value
        AppConstants.goal = ((Number) goalField.getValue()).intValue();

        // Conditionals
        switch (AppConstants.gameEnum) {
            case DRILLS:
                if (AppConstants.goal > setSize) {
                    AppConstants.goal = setSize;
                    releasePause = true;
                } else if (AppConstants.goal < 1) {
                    JOptionPane.showMessageDialog(AppConstants.frame, "The goal cannot be less than one.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    releasePause = true;
                }

                if (releasePause) {
                    if (JSONTools.getRestriction(AppConstants.json)) {
                        new RestrictTerms().display();
                    } else { // to reset the restriction between games
                        AppConstants.whitelistArray.addAll(English.characters.keySet());
                        AppConstants.bannedLetters.removeAll(AppConstants.whitelistArray);
                        AppConstants.semaphore.release();
                    }
                }
                break;
            case SCENARIOS:
                if (AppConstants.goal < 1) {
                    System.out.println("test");
                    JOptionPane.showMessageDialog(AppConstants.frame, "The goal cannot be less than one.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    releasePause = true;
                }

                if (releasePause) {
                    AppConstants.semaphore.release();
                }
                break;
            case CUSTOM_DRILLS:
                if (AppConstants.goal > setSize) {
                    AppConstants.goal = setSize;
                    releasePause = true;
                } else if (AppConstants.goal < 1) {
                    JOptionPane.showMessageDialog(AppConstants.frame, "The goal cannot be less than one.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    releasePause = true;
                }

                if (releasePause) {
                    if (JSONTools.getRestriction(AppConstants.json)) {
                        new RestrictTerms().display();
                    } else { // to reset the restriction between games
                        AppConstants.whitelistArray.addAll(English.characters.keySet());
                        AppConstants.bannedLetters.removeAll(AppConstants.whitelistArray);
                        AppConstants.semaphore.release();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
        goalField.requestFocusInWindow();
    }
}
