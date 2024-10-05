package whyxzee.terminalpractice.application;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class GoAgain extends JPanel implements ActionListener {
    GoAgainDaemon daemon;

    // Vars
    public static boolean activeScreen = false;

    // UI Components
    JLabel label = new JLabel("Would you like to repeat this exercise?");
    JButton continueButton = new JButton("Yes");
    JButton endButton = new JButton("No");
    GridBagConstraints grid = new GridBagConstraints();

    public GoAgain() {
        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(8, 8, 8, 8);
        grid.anchor = GridBagConstraints.CENTER;

        // Creating buttons
        label.setFont(AppConstants.bigFont);
        this.add(label, grid);
        grid.gridy++;

        // Continue button
        continueButton.addActionListener(this);
        continueButton.setMnemonic(KeyEvent.VK_Y);
        continueButton.setFont(AppConstants.medFont);
        continueButton.setPreferredSize(AppConstants.smallButtonDimension);
        this.add(continueButton, grid);
        grid.gridy++;

        // End button
        endButton.setActionCommand("end");
        endButton.addActionListener(this);
        endButton.setMnemonic(KeyEvent.VK_N);
        endButton.setFont(AppConstants.medFont);
        endButton.setPreferredSize(AppConstants.smallButtonDimension);
        this.add(endButton, grid);
        grid.gridy++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("end")) {
            AppConstants.gameEnum = AppConstants.Game.NONE;
        }
        activeScreen = false;
        AppConstants.semaphore.release();
    }

    public void display() {
        activeScreen = true;
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);

        daemon = new GoAgainDaemon(this);
        daemon.setDaemon(true);
        daemon.start();
    }

    /**
     * Resizes components of GoAgain.
     */
    public void resize() {
        label.setFont(AppConstants.bigFont);
        continueButton.setFont(AppConstants.medFont);
        continueButton.setPreferredSize(AppConstants.smallButtonDimension);
        endButton.setFont(AppConstants.medFont);
        endButton.setPreferredSize(AppConstants.smallButtonDimension);
    }
}

class GoAgainDaemon extends Thread {
    private GoAgain ui;

    public GoAgainDaemon(GoAgain ui) {
        super("GoAgainDaemon");
        this.ui = ui;
    }

    public void run() {
        while (GoAgain.activeScreen) {
            ui.resize();
        }
    }
}