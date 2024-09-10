package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Collections;
// import java.util.HashMap;
import java.util.List;
import java.util.Set;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
// import java.awt.Font;

import application.RunApplication;

public class SetScenarioUI extends JPanel implements ActionListener {
    List<JButton> buttonList = new ArrayList<JButton>();

    public SetScenarioUI(Set<String> inputSet) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        // Organizing the list of strings
        List<String> list = new ArrayList<String>(inputSet);
        Collections.sort(list);

        // Creating buttons from hashmap
        for (String i : list) {
            buttonList.add(new JButton(RunApplication.capitalize(i)));
        }

        // adding data for every button
        for (int i = 0; i < buttonList.size(); i++) {
            JButton index = buttonList.get(i);
            index.addActionListener(this);
            index.setActionCommand(index.getText().toLowerCase());

            index.setHorizontalAlignment(AbstractButton.CENTER);
            index.setVerticalTextPosition(AbstractButton.CENTER);
            index.setPreferredSize(new Dimension(250, 25));

            this.add(index, grid);
            grid.gridy++;
        }

        JButton backButton = new JButton("Go Back");
        backButton.setActionCommand("go back");
        backButton.addActionListener(this);
        // this.add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("go back")) {
            RunApplication.set = "";
            RunApplication.subject = "";
        }
        for (JButton i : buttonList) {
            if (e.getActionCommand().equals(i.getText().toLowerCase())) {
                RunApplication.set = i.getText().toLowerCase();
            }
        }
        RunApplication.semaphore.release();
    }

    public void display() {
        // this.setOpaque(true);
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
