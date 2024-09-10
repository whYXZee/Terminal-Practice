package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import application.RunApplication;

public class GoAgain extends JPanel implements ActionListener {
    GridBagConstraints grid = new GridBagConstraints();

    public GoAgain() {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;
        RunApplication.getFontSize();

        // Creating buttons
        JLabel label = new JLabel("Would you like to repeat this exercise?");
        label.setFont(new Font("Arial", Font.PLAIN, 40));
        this.add(label, grid);
        grid.gridy++;

        JButton continueButton = new JButton("Yes");
        // continueButton.setActionCommand("continue");
        // continueButton.setHorizontalTextPosition(AbstractButton.CENTER);
        // continueButton.setVerticalTextPosition(AbstractButton.CENTER);
        continueButton.setPreferredSize(new Dimension(150, 25));
        continueButton.setMnemonic(KeyEvent.VK_Y);

        JButton endButton = new JButton("No");
        endButton.setActionCommand("end");
        // endButton.setHorizontalTextPosition(AbstractButton.CENTER);
        // endButton.setVerticalTextPosition(AbstractButton.CENTER);
        endButton.setPreferredSize(new Dimension(150, 25));
        continueButton.setMnemonic(KeyEvent.VK_N);

        // Listening functions, makes the buttons work
        continueButton.addActionListener(this);
        endButton.addActionListener(this);

        // Adding the buttons to the screen
        this.add(continueButton, grid);
        grid.gridy++;
        this.add(endButton, grid);
        grid.gridy++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("end")) {
            RunApplication.gameEnum = RunApplication.Game.NONE;
        }
        RunApplication.semaphore.release();
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
