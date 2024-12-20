package whyxzee.terminalpractice.flashcards;

import whyxzee.terminalpractice.application.AppConstants;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FlashcardUI extends JPanel implements ActionListener {
    private static FlashcardDaemon daemon;
    private static Semaphore semaphore = new Semaphore(0);

    // Vars
    private static boolean isQuestion = true;
    private static boolean run = true;
    private static int index = 0;
    private static String question = "";
    private HashMap<String, String> set = new HashMap<String, String>();
    private List<String> terms = new ArrayList<String>();

    // UI Constants
    private static JButton nextButton = new JButton("Next");
    private static JButton backButton = new JButton("Back");
    private static JButton flipButton = new JButton("Flip");
    private static JButton goToButton = new JButton("Go to certain card");
    private static JButton endButton = new JButton("End practice");
    private static JLabel cardTracker = new JLabel("");
    private static JPanel flashcard = new JPanel();
    private static JPanel flashcardButtons = new JPanel();
    private static JPanel extraButtons = new JPanel();

    private static JButton[] flashcardControls = { backButton, flipButton, nextButton };
    private JLabel[] flashcardText = {};

    private static GridBagConstraints grid = new GridBagConstraints();
    private static GridBagConstraints flashcardGrid = new GridBagConstraints();
    private static GridBagConstraints flashcardButtonsGrid = new GridBagConstraints();

    public FlashcardUI(HashMap<String, String> set) throws InterruptedException {
        // Resetting vars
        this.set = set;
        index = 0;
        run = true;
        terms = new ArrayList<String>(set.keySet());
        // question = (String) set.keySet().toArray()[0];
        question = terms.get(0);
        flashcardText = AppConstants.divideLabel(question);

        // Layout
        this.setLayout(new GridBagLayout());
        grid.gridx = 0;
        grid.gridy = 0;
        grid.insets = new Insets(2, 8, 2, 8);
        grid.anchor = GridBagConstraints.CENTER;

        flashcardButtonsGrid.gridx = 0;
        flashcardButtonsGrid.gridy = 0;
        flashcardButtonsGrid.insets = new Insets(2, 8, 2, 8);
        flashcardButtonsGrid.anchor = GridBagConstraints.CENTER;

        daemon = new FlashcardDaemon(this);
        daemon.setDaemon(true);
        daemon.start();

        //
        // Setting data
        //

        // Card tracker
        cardTracker.setFont(AppConstants.biggerFont);
        this.add(cardTracker, grid);
        grid.gridy++;

        // Text in flashcard
        for (JLabel j : flashcardText) {
            j.setFont(AppConstants.bigFont);
        }

        // Flashcard
        flashcard.setLayout(new GridBagLayout());
        flashcard.setPreferredSize(AppConstants.flashcardDimension);
        this.add(new JScrollPane(flashcard, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), grid);
        grid.gridy++;

        // Flashcard buttons
        flashcardButtons.setLayout(new GridBagLayout());
        flashcardButtons.setPreferredSize(
                new Dimension(AppConstants.flashcardDimension.width,
                        AppConstants.smallButtonDimension.height + 16));
        for (JButton j : flashcardControls) {
            j.addActionListener(this);
            j.setActionCommand(j.getText().toLowerCase());
            j.setFont(AppConstants.medFont);
            j.setPreferredSize(AppConstants.smallButtonDimension);
            flashcardButtons.add(j, flashcardButtonsGrid);
            flashcardButtonsGrid.gridx++;
        }
        this.add(flashcardButtons, grid);
        grid.gridy++;

        backButton.setMnemonic(KeyEvent.VK_B);
        backButton.setToolTipText("Go to the previous term.");

        flipButton.setMnemonic(KeyEvent.VK_F);
        flipButton.setToolTipText("Flip the flashcard.");

        nextButton.setMnemonic(KeyEvent.VK_N);
        nextButton.setToolTipText("Go to the next term");

        // Extra buttons
        extraButtons.setLayout(new GridBagLayout());
        extraButtons.setPreferredSize(
                new Dimension(AppConstants.flashcardDimension.width,
                        AppConstants.smallButtonDimension.height + 16));

        endButton.addActionListener(this);
        endButton.setActionCommand("end");
        endButton.setPreferredSize(AppConstants.smallButtonDimension);
        endButton.setFont(AppConstants.medFont);
        endButton.setToolTipText("Go to the main menu.");
        endButton.setMnemonic(KeyEvent.VK_E);
        extraButtons.add(endButton, flashcardButtonsGrid);
        flashcardButtonsGrid.gridx++;

        goToButton.addActionListener(this);
        goToButton.setActionCommand("go to");
        goToButton.setPreferredSize(AppConstants.smallButtonDimension);
        goToButton.setFont(AppConstants.medFont);
        goToButton.setToolTipText("Choose which card to go to, instead of spamming next/back.");
        goToButton.setMnemonic(KeyEvent.VK_G);
        extraButtons.add(goToButton, flashcardButtonsGrid);

        this.add(extraButtons, grid);

        while (run) {
            // flashcard.removeAll();

            // Text
            cardTracker.setText("Card " + (index + 1) + "/" + set.keySet().size());

            // Adding the flashcard panel
            flashcardGrid.gridy = 0;
            for (JLabel j : flashcardText) {
                j.setFont(AppConstants.bigFont);
                flashcard.add(j, flashcardGrid);
                flashcardGrid.gridy++;
            }

            //
            // Setting the flashcard buttons
            //
            if (index + 1 == set.keySet().size()) {
                nextButton.setEnabled(false);
            } else {
                nextButton.setEnabled(true);
            }

            if (index == 0) {
                backButton.setEnabled(false);
            } else {
                backButton.setEnabled(true);
            }

            display();
            semaphore.acquire();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Declaring vars
        String action = e.getActionCommand();

        // System.out.println("action: " + action + " question: " + question + " index:"
        // + index);

        // if (semaphore.hasQueuedThreads()) {
        if (action.equals("next")) {
            for (JLabel j : flashcardText) {
                flashcard.remove(j);
            }

            index++;
            isQuestion = true;
            // question = (String) set.keySet().toArray()[index];
            question = terms.get(index);
            flashcardText = AppConstants.divideLabel(question);
        } else if (action.equals("back")) {
            for (JLabel j : flashcardText) {
                flashcard.remove(j);
            }

            index--;
            isQuestion = true;
            // question = (String) set.keySet().toArray()[index];
            question = terms.get(index);
            flashcardText = AppConstants.divideLabel(question);
        } else if (action.equals("flip")) {
            for (JLabel j : flashcardText) {
                flashcard.remove(j);
            }

            if (isQuestion) {
                isQuestion = false;
                flashcardText = AppConstants.divideLabel(new AnswerSet(set.get(question)).toString());
            } else {
                isQuestion = true;
                flashcardText = AppConstants.divideLabel(question);
            }
        } else if (action.equals("end")) {
            run = false;
            AppConstants.gameEnum = AppConstants.Game.NONE;
            AppConstants.semaphore.release();
        } else if (action.equals("go to")) {
            boolean getInput = true;
            while (getInput) {
                try {
                    int formerIndex = index;
                    String input = JOptionPane.showInputDialog("What card would you like to go to?", index + 1);
                    if (input == null) {
                        index = formerIndex;
                        getInput = false;
                    } else {
                        index = Integer.valueOf(input) - 1;

                        if (index >= 0 && set.keySet().size() >= index + 1) {
                            for (JLabel j : flashcardText) {
                                flashcard.remove(j);
                            }

                            getInput = false;
                            isQuestion = true;
                            // question = (String) set.keySet().toArray()[index];
                            question = terms.get(index);
                            flashcardText = AppConstants.divideLabel(question);
                        } else {
                            JOptionPane.showMessageDialog(AppConstants.frame,
                                    "Invalid input, please put a number in between 1 and " + set.keySet().size()
                                            + ".",
                                    "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException error) {
                    JOptionPane.showMessageDialog(AppConstants.frame, "Invalid input, please put numbers",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        semaphore.release();
        // }
    }

    /**
     * Displays the flashcard UI.
     */
    public void display() {
        AppConstants.frame.setContentPane(this);
        AppConstants.frame.setVisible(true);
    }

    /**
     * Resizes components of the flashcard UI.
     */
    public void resize() {
        cardTracker.setFont(AppConstants.biggerFont);

        flashcard.setPreferredSize(AppConstants.flashcardDimension);

        for (JLabel j : flashcardText) {
            j.setFont(AppConstants.bigFont);
        }

        for (JButton j : flashcardControls) {
            j.setFont(AppConstants.medFont);
            j.setPreferredSize(AppConstants.smallButtonDimension);
        }

        flashcardButtons.setPreferredSize(
                new Dimension(AppConstants.flashcardDimension.width,
                        AppConstants.smallButtonDimension.height + 16));
        extraButtons.setPreferredSize(
                new Dimension(AppConstants.flashcardDimension.width,
                        AppConstants.smallButtonDimension.height + 16));

        endButton.setPreferredSize(AppConstants.smallButtonDimension);
        endButton.setFont(AppConstants.medFont);

        goToButton.setPreferredSize(AppConstants.smallButtonDimension);
        goToButton.setFont(AppConstants.medFont);
    }

    /**
     * Removes listeners on each button. Removes the "ghost input" that occurs
     * when creating more than one flashcard window.
     */
    public void disableListeners() {
        nextButton.removeActionListener(this);
        backButton.removeActionListener(this);
        flipButton.removeActionListener(this);
        goToButton.removeActionListener(this);
        endButton.removeActionListener(this);

        flashcard.removeAll();
    }
}

// Cuases "flashing", fix later
class FlashcardDaemon extends Thread {
    FlashcardUI ui;

    public FlashcardDaemon(FlashcardUI ui) {
        super("FlashcardDaemon");
        this.ui = ui;
    }

    public void run() {
        boolean shouldRun = true;
        while (shouldRun) {
            switch (AppConstants.gameEnum) {
                case FLASHCARDS:
                    ui.resize();
                    break;
                case CUSTOM_FLASHCARDS:
                    ui.resize();
                    break;
                default:
                    shouldRun = false;
                    ui.disableListeners();
                    break;
            }
        }
    }
}