package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.util.ArrayList;

import application.RunApplication;
import resources.English;

// fix this
public class RestrictTerms extends JPanel implements ActionListener {
    JTextField restrictedField = new JTextField();
    GridBagConstraints grid = new GridBagConstraints();

    public RestrictTerms(String restrict) {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;
        RunApplication.getFontSize();

        RunApplication.bannedLetters = new ArrayList<String>(English.characters.keySet());
        RunApplication.whitelistArray = new ArrayList<String>();
        JLabel restrictLabel = new JLabel(
                "What letters would you like to practice?");
        restrictLabel.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 2));
        this.add(restrictLabel, grid);
        grid.gridy++;

        // restrictedField.setValue(setSize);
        restrictedField.setFont(new Font("Arial", Font.PLAIN, RunApplication.fontSize / 4));
        restrictedField.setHorizontalAlignment(JFormattedTextField.CENTER);
        restrictedField.setColumns(RunApplication.getColumns()); // how big the text field is
        restrictedField.addActionListener(this);
        this.add(restrictedField, grid);
        grid.gridy++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (restrictedField.getText().equals("all") || restrictedField.getText().equals("")) {
            for (Character i : restrictedField.getText().toCharArray()) {
                RunApplication.whitelistArray.add(Character.toString(i));
            }
            RunApplication.bannedLetters.removeAll(RunApplication.whitelistArray);
        } else {
            RunApplication.bannedLetters.clear();
        }
        RunApplication.semaphore.release();
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
        restrictedField.requestFocusInWindow();
    }
}
