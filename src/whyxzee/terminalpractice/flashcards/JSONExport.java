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

public class JSONExport extends JPanel {
    public static JFileChooser fc = new JFileChooser(new JFileChooser().getFileSystemView().getDefaultDirectory());

    public JSONExport(File fileToShare) {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON (.json)", "json"));
        fc.setAcceptAllFileFilterUsed(false);

        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File save = fc.getSelectedFile();

            FileChannel chosenChannel = null;
            FileInputStream chosen = null;
            FileChannel saveChannel = null;
            FileOutputStream saveStream = null;

            try {
                chosen = new FileInputStream(fileToShare.getPath());
                chosenChannel = chosen.getChannel();

                saveStream = new FileOutputStream(save.getPath() + ".json");
                saveChannel = saveStream.getChannel();

                saveChannel.transferFrom(chosenChannel, 0, chosenChannel.size());

                saveStream.close();
                saveChannel.close();
                chosen.close();
                chosenChannel.close();

                JOptionPane.showMessageDialog(AppConstants.frame,
                        save.getName() + " successfully exported!", "Set Exported", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e1) {
                JOptionPane.showMessageDialog(AppConstants.frame, "File not found: " + e1, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(AppConstants.frame, "No file saved.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
    }
}
