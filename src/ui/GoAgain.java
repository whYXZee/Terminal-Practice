package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

import application.RunApplication;

public class GoAgain extends JPanel implements ActionListener {

    public GoAgain() {
        // Creating buttons
        JLabel label = new JLabel("Would you like to repeat this exercise?");
        this.add(label);

        JButton continueButton = new JButton("Yes");
        continueButton.setHorizontalTextPosition(AbstractButton.CENTER);
        continueButton.setVerticalTextPosition(AbstractButton.CENTER);
        continueButton.setActionCommand("continue");
        continueButton.setMnemonic(KeyEvent.VK_Y);

        JButton endButton = new JButton("No");
        endButton.setHorizontalTextPosition(AbstractButton.CENTER);
        endButton.setVerticalTextPosition(AbstractButton.CENTER);
        endButton.setActionCommand("end");
        continueButton.setMnemonic(KeyEvent.VK_N);

        // Listening functions, makes the buttons work
        continueButton.addActionListener(this);
        endButton.addActionListener(this);

        // Adding the buttons to the screen
        this.add(continueButton);
        this.add(endButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("continue")) {
        } else if (e.getActionCommand().equals("end")) {
            RunApplication.gameEnum = RunApplication.Game.NONE;
        }
        RunApplication.semaphore.release();
    }

    public void display() {
        RunApplication.frame.setContentPane(this);
        RunApplication.frame.setVisible(true);
    }
}
