package whyxzee.terminalpractice.application;

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

public class SubjectUI extends JPanel implements ActionListener {
    List<JButton> buttonList = new ArrayList<JButton>();
    // int maxButtonWidth = 150;

    public SubjectUI(Set<String> inputSet) {
        // Layout
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
        for (JButton i : buttonList) {
            i.setVerticalTextPosition(AbstractButton.CENTER);
            i.setActionCommand(i.getText().toLowerCase());
            i.addActionListener(this); // Makes the buttons work
            this.add(i, grid); // adds the button to the panel
            grid.gridy++;
            // System.out.println(i.getPreferredSize().width);
            // if (i.getPreferredSize().width > maxButtonWidth) {
            // maxButtonWidth = i.getPreferredSize().width;
            // }
        }
        for (JButton i : buttonList) {
            i.setPreferredSize(new Dimension(200, 25));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (JButton i : buttonList) {
            if (e.getActionCommand().equals(i.getText().toLowerCase())) {
                RunApplication.subject = i.getText().toLowerCase();
            }
        }
        RunApplication.semaphore.release();
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
