package whyxzee.terminalpractice.flashcards;

import whyxzee.terminalpractice.application.AppConstants;
import whyxzee.terminalpractice.resources.English;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.ArrayList;

public class RestrictTerms extends JPanel implements ActionListener {
    GridBagConstraints grid = new GridBagConstraints();
    GridBagConstraints checkBoxGrid = new GridBagConstraints();

    // Checkboxes
    JCheckBox a = new JCheckBox("A");
    JCheckBox b = new JCheckBox("B");
    JCheckBox c = new JCheckBox("C");
    JCheckBox d = new JCheckBox("D");
    JCheckBox e = new JCheckBox("E");
    JCheckBox f = new JCheckBox("F");
    JCheckBox g = new JCheckBox("G");
    JCheckBox h = new JCheckBox("H");
    JCheckBox i = new JCheckBox("I");
    JCheckBox j = new JCheckBox("J");
    JCheckBox k = new JCheckBox("K");
    JCheckBox l = new JCheckBox("L");
    JCheckBox m = new JCheckBox("M");
    JCheckBox n = new JCheckBox("N");
    JCheckBox o = new JCheckBox("O");
    JCheckBox p = new JCheckBox("P");
    JCheckBox q = new JCheckBox("Q");
    JCheckBox r = new JCheckBox("R");
    JCheckBox s = new JCheckBox("S");
    JCheckBox t = new JCheckBox("T");
    JCheckBox u = new JCheckBox("U");
    JCheckBox v = new JCheckBox("V");
    JCheckBox w = new JCheckBox("W");
    JCheckBox x = new JCheckBox("X");
    JCheckBox y = new JCheckBox("Y");
    JCheckBox z = new JCheckBox("Z");
    JCheckBox all = new JCheckBox("All");

    JCheckBox[] array = { a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, all };

    public RestrictTerms() {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        checkBoxGrid.gridx = 0;
        checkBoxGrid.gridy = 0;
        checkBoxGrid.insets = new Insets(8, 8, 8, 8);
        checkBoxGrid.anchor = GridBagConstraints.CENTER;

        AppConstants.bannedLetters = new ArrayList<String>(English.characters.keySet());
        AppConstants.whitelistArray = new ArrayList<String>();
        JLabel restrictLabel = new JLabel(
                "What letters would you like to practice?");
        restrictLabel.setFont(AppConstants.bigFont);
        this.add(restrictLabel, grid);
        grid.gridy++;

        // Adding the checkboxes
        int rowTick = 0; // for creating rows
        JPanel checkBoxes = new JPanel();
        checkBoxes.setLayout(new GridBagLayout());
        all.setMnemonic(KeyEvent.VK_A);
        for (JCheckBox i : array) {
            rowTick++;
            i.setActionCommand(i.getText());
            i.addActionListener(this);
            checkBoxes.add(i, checkBoxGrid);
            checkBoxGrid.gridx++;
            if (rowTick == 3) {
                rowTick = 0;
                checkBoxGrid.gridx -= 3;
                checkBoxGrid.gridy++;
            }
        }
        this.add(checkBoxes, grid);
        grid.gridy++;

        JButton continueButton = new JButton("Ready?");
        continueButton.setActionCommand("ready");
        continueButton.addActionListener(this);
        continueButton.setMnemonic(KeyEvent.VK_R);
        this.add(continueButton, grid);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ready")) {
            for (JCheckBox i : array) {
                if (!i.equals(all) && i.isSelected()) {
                    AppConstants.whitelistArray.add(i.getText().toLowerCase());
                }
            }

            // to prevent no letters being selected.
            if (AppConstants.whitelistArray.size() == 0) {
                JOptionPane.showMessageDialog(AppConstants.frame,
                        "No letters have been selected. Please select a letter or select \"all\".", "Selection Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                AppConstants.bannedLetters.removeAll(AppConstants.whitelistArray);
                AppConstants.semaphore.release();
            }
        } else if (e.getActionCommand().equals("All")) {
            for (JCheckBox i : array) {
                if (!i.equals(all)) {
                    i.setSelected(all.isSelected());
                }
            }
        }
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
    }
}
