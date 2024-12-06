package whyxzee.terminalpractice.flashcards;

import whyxzee.terminalpractice.application.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JSONImport extends JPanel {
    public static JFileChooser fc = new JFileChooser("./src/whyxzee/terminalpractice/flashcards/custom");

    public JSONImport() {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON (.json)", "json"));
        fc.setAcceptAllFileFilterUsed(false);

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File copy = fc.getSelectedFile();

            FileChannel inputChannel = null;
            FileInputStream input = null;
            FileChannel outputChannel = null;
            FileOutputStream output = null;

            try {
                input = new FileInputStream(copy);
                inputChannel = input.getChannel();

                output = new FileOutputStream(
                        "./src/whyxzee/terminalpractice/flashcards/custom/" + copy.getName());
                outputChannel = output.getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

                input.close();
                inputChannel.close();
                output.close();
                outputChannel.close();

                JOptionPane.showMessageDialog(AppConstants.frame,
                        copy.getName() + " successfully moved!", "Set Imported", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e1) {
                JOptionPane.showMessageDialog(AppConstants.frame, "File not found: " + e1, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(AppConstants.frame, "No file selected.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
    }
}