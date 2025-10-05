/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.ui;

import assignment_2_sudoku_gui.database.DatabaseManager;
import assignment_2_sudoku_gui.database.SavedGameData;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/*
Dialog for loading saved games.

Displays list of saved games with details.
 */
public class LoadGameDialog extends JDialog {
    
    private JList<String> gameList;
    private JTextArea detailsArea;
    private JButton loadButton;
    private JButton deleteButton;
    private JButton cancelButton;
    
    private String selectedGame;
    private List<String> savedGames;
    
    
    //Constructor initializes the load dialog
    
    public LoadGameDialog(JFrame parent, List<String> savedGames) {
        super(parent, "Load Saved Game", true);
        this.savedGames = savedGames;
        this.selectedGame = null;
        
        setupDialog();
        createComponents();
        layoutComponents();
    }
    
    
    //Sets up dialog properties
    
    private void setupDialog() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    
    //Creates dialog components
    
    private void createComponents() {
        // Game list
        gameList = new JList<>(savedGames.toArray(new String[0]));
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setFont(new Font("Arial", Font.PLAIN, 12));
        gameList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetails();
            }
        });
        
        // Details area
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        detailsArea.setBackground(new Color(245, 245, 245));
        detailsArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Buttons
        loadButton = new JButton("Load");
        loadButton.addActionListener(e -> handleLoad());
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> handleDelete());
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancel());
    }
    
    
    //Arranges components in dialog
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Top: Instructions
        JLabel instructionLabel = new JLabel("Select a saved game to load:");
        instructionLabel.setBorder(new EmptyBorder(10, 10, 0, 10));
        add(instructionLabel, BorderLayout.NORTH);
        
        // Center: Split pane with list and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        JScrollPane listScroll = new JScrollPane(gameList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Saved Games"));
        splitPane.setLeftComponent(listScroll);
        
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Game Details"));
        splitPane.setRightComponent(detailsScroll);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Bottom: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    
    //Updates details area with selected game info
    
    private void updateDetails() {
        String selected = gameList.getSelectedValue();
        if (selected == null) {
            detailsArea.setText("No game selected");
            loadButton.setEnabled(false);
            deleteButton.setEnabled(false);
            return;
        }
        
        loadButton.setEnabled(true);
        deleteButton.setEnabled(true);
        
        // Load game details from database
        SavedGameData gameData = DatabaseManager.getInstance().loadGame(selected);
        if (gameData != null) {
            StringBuilder details = new StringBuilder();
            details.append("Game Name: ").append(gameData.getGameName()).append("\n\n");
            details.append("Difficulty: ").append(gameData.getDifficulty()).append("\n");
            details.append("Game Mode: ").append(gameData.getGameMode()).append("\n");
            details.append("Play Time: ").append(formatTime(gameData.getPlayTime())).append("\n");
            details.append("Saved: ").append(gameData.getSaveDate()).append("\n");
            
            detailsArea.setText(details.toString());
        } else {
            detailsArea.setText("Error loading game details");
        }
    }
    
    
    //Formats milliseconds to readable time
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    
    //Handles load button click
    
    private void handleLoad() {
        selectedGame = gameList.getSelectedValue();
        if (selectedGame != null) {
            dispose();
        }
    }
    
    
    //Handles delete button click
    
    private void handleDelete() {
        String selected = gameList.getSelectedValue();
        if (selected == null) {
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this saved game?\n" +
            "This action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            if (DatabaseManager.getInstance().deleteGame(selected)) {
                savedGames.remove(selected);
                gameList.setListData(savedGames.toArray(new String[0]));
                detailsArea.setText("No game selected");
                
                JOptionPane.showMessageDialog(this,
                    "Game deleted successfully.",
                    "Delete Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to delete game.",
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    //Handles cancel button click
    
    private void handleCancel() {
        selectedGame = null;
        dispose();
    }
    
    
    //Gets the selected game name
    
    public String getSelectedGame() {
        return selectedGame;
    }
}