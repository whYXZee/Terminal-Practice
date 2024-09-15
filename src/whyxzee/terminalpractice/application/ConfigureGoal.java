package whyxzee.terminalpractice.application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.text.NumberFormat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;

public class ConfigureGoal extends JPanel implements PropertyChangeListener {
    JFormattedTextField goalField = new JFormattedTextField(NumberFormat.getNumberInstance());
    boolean isDrill;
    int setSize;
    boolean releasePause;
    GridBagConstraints grid = new GridBagConstraints();

    public ConfigureGoal(int setSize, boolean isDrill) {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        RunApplication.getFontSize();

        this.setSize = setSize;
        this.isDrill = isDrill;
        if (isDrill) {
            JLabel goalLabel = new JLabel(
                    "How many terms would you like to practice? (The max terms is " + setSize + ")");
            goalLabel.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 3));
            this.add(goalLabel, grid);
            grid.gridy++;

            goalField.setValue(0);
            goalField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
            goalField.setHorizontalAlignment(JFormattedTextField.CENTER);
            goalField.setColumns(RunApplication.getColumns()); // how big the text field is
            goalField.addPropertyChangeListener("value", this);
            this.add(goalField, grid);
        } else {
            JLabel goalLabel = new JLabel("How many questions would you like to practice?");
            this.add(goalLabel, grid);
            goalLabel.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));
            grid.gridy++;

            goalField.setValue(0);
            goalField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
            goalField.setColumns(RunApplication.getColumns()); // how big the text field is
            goalField.setHorizontalAlignment(JFormattedTextField.CENTER);
            goalField.addPropertyChangeListener("value", this);
            this.add(goalField, grid);
            grid.gridy++;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        {
            RunApplication.goal = ((Number) goalField.getValue()).intValue();
            if (isDrill) {
                if (RunApplication.goal > setSize) {
                    RunApplication.goal = setSize;
                    releasePause = true;
                } else if (RunApplication.goal < 1) {
                    JLabel error = new JLabel("Goal less than one, try again.");
                    this.add(error, grid);
                    display();
                } else {
                    releasePause = true;
                }
            } else if (RunApplication.goal < 1) {
                JLabel error = new JLabel("Goal less than one, try again.");
                this.add(error, grid);
                display();
            } else {
                releasePause = true;
            }
            if (releasePause) {
                RunApplication.semaphore.release();
            }
        }
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
        goalField.requestFocusInWindow();
    }
}
