/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.ui;

import assignment_2_sudoku_gui.controller.GameControllerGUI;
import assignment_2_sudoku_gui.model.enums.DifficultyLevel;
import assignment_2_sudoku_gui.database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
Control panel containing all game control buttons.

Implements user interaction for game management.
 */
public class ControlPanel extends JPanel {
    
    private GameControllerGUI controller;
    private GamePanel gamePanel;
    
    private JButton newGameBtn;
    private JButton saveGameBtn;
    private JButton loadGameBtn;
    private JButton undoBtn;
    private JButton hintBtn;
    private JButton solveBtn;
    private JComboBox<String> gameModeCombo;
    private JComboBox<String> difficultyCombo;
    
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237);
    private static final Color BUTTON_TEXT_COLOR = Color.BLACK;  // Changed to BLACK
    
    
    //Constructor initializes the control panel
    
    public ControlPanel(GameControllerGUI controller, GamePanel gamePanel) {
        this.controller = controller;
        this.gamePanel = gamePanel;
        
        setupPanel();
        createComponents();
        layoutComponents();
    }
    
    
    //Sets up panel properties
    
    private void setupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Game Controls"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setPreferredSize(new Dimension(200, 0));
    }
    
    
    //Creates all control components
    
    private void createComponents() {
        // Game mode selector
        String[] gameModes = {"Classic Mode", "Timed Mode"};
        gameModeCombo = new JComboBox<>(gameModes);
        gameModeCombo.setMaximumSize(new Dimension(180, 30));
        gameModeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Difficulty selector
        String[] difficulties = {"Easy", "Medium", "Hard", "Expert"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setMaximumSize(new Dimension(180, 30));
        difficultyCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons
        newGameBtn = createStyledButton("New Game");
        newGameBtn.addActionListener(e -> handleNewGame());
        
        saveGameBtn = createStyledButton("Save Game");
        saveGameBtn.addActionListener(e -> handleSaveGame());
        
        loadGameBtn = createStyledButton("Load Game");
        loadGameBtn.addActionListener(e -> handleLoadGame());
        
        undoBtn = createStyledButton("Undo");
        undoBtn.addActionListener(e -> handleUndo());
        
        hintBtn = createStyledButton("Hint");
        hintBtn.addActionListener(e -> handleHint());
        
        solveBtn = createStyledButton("Auto Solve");
        solveBtn.addActionListener(e -> handleSolve());
    }
    
    
    //Creates a styled button with consistent appearance
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 35));
        
        // Use light background color so black text is visible
        button.setBackground(new Color(220, 230, 240));  // Light blue-gray
        button.setForeground(BUTTON_TEXT_COLOR);  // Black text
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 130, 160), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Add hover effect with lighter color
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(180, 200, 220));  // Darker on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 230, 240));  // Back to light
            }
        });
        
        return button;
    }
    
    
    //Arranges components in the panel
    
    private void layoutComponents() {
        // Game settings section
        add(createSectionLabel("Game Settings"));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(new JLabel("Game Mode:"));
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(gameModeCombo);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(new JLabel("Difficulty:"));
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(difficultyCombo);
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Game control section
        add(createSectionLabel("Game Actions"));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(newGameBtn);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(createSectionLabel("File Operations"));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(saveGameBtn);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(loadGameBtn);
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        add(createSectionLabel("Game Assistance"));
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        add(undoBtn);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(hintBtn);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(solveBtn);
        
        add(Box.createVerticalGlue());
    }
    
    
    //Creates a styled section label
    
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    
    //Handles new game creation
    
    public void handleNewGame() {
        // Get selected game mode
        String modeStr = (String) gameModeCombo.getSelectedItem();
        boolean isTimedMode = modeStr.equals("Timed Mode");
        
        // Get selected difficulty
        String diffStr = (String) difficultyCombo.getSelectedItem();
        DifficultyLevel difficulty = DifficultyLevel.valueOf(diffStr.toUpperCase());
        
        // Confirm if game is in progress
        if (controller.isGameInProgress()) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Starting a new game will lose current progress.\nContinue?",
                "Confirm New Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Start new game through controller
        controller.startNewGame(isTimedMode, difficulty);
        gamePanel.updateGrid(controller.getCurrentPuzzle());
    }
    
    
    //Handles game save operation
    
    public void handleSaveGame() {
        if (!controller.isGameInProgress()) {
            JOptionPane.showMessageDialog(this,
                "No game in progress to save.",
                "Cannot Save",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Prompt for save name
        String saveName = JOptionPane.showInputDialog(this,
            "Enter a name for this saved game:",
            "Save Game",
            JOptionPane.QUESTION_MESSAGE);
        
        if (saveName == null || saveName.trim().isEmpty()) {
            return; // User cancelled or entered empty name
        }
        
        saveName = saveName.trim();
        
        // Check if name already exists
        List<String> existingGames = DatabaseManager.getInstance().getSavedGameNames();
        if (existingGames.contains(saveName)) {
            int choice = JOptionPane.showConfirmDialog(this,
                "A saved game with this name already exists.\nOverwrite it?",
                "Confirm Overwrite",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Save game
        if (controller.saveGame(saveName)) {
            JOptionPane.showMessageDialog(this,
                "Game saved successfully!",
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to save game. Please try again.",
                "Save Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //Handles game load operation
    
    public void handleLoadGame() {
        // Get list of saved games
        List<String> savedGames = DatabaseManager.getInstance().getSavedGameNames();
        
        if (savedGames.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No saved games found.",
                "No Saved Games",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Confirm if game is in progress
        if (controller.isGameInProgress()) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Loading a game will lose current progress.\nContinue?",
                "Confirm Load",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Show load dialog
        LoadGameDialog loadDialog = new LoadGameDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), savedGames);
        loadDialog.setVisible(true);
        
        String selectedGame = loadDialog.getSelectedGame();
        if (selectedGame != null) {
            if (controller.loadGame(selectedGame)) {
                gamePanel.updateGrid(controller.getCurrentPuzzle());
                
                // Update combo boxes to match loaded game
                updateControlsFromLoadedGame();
                
                JOptionPane.showMessageDialog(this,
                    "Game loaded successfully!",
                    "Load Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to load game. The save file may be corrupted.",
                    "Load Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    //Updates control panel state from loaded game
    
    private void updateControlsFromLoadedGame() {
        if (controller.getCurrentMode() != null) {
            gameModeCombo.setSelectedItem(controller.getCurrentMode().getModeName());
            
            String difficulty = controller.getCurrentMode().getDifficulty().name();
            difficultyCombo.setSelectedItem(
                difficulty.charAt(0) + difficulty.substring(1).toLowerCase());
        }
    }
    
    
    //Handles undo operation
    
    public void handleUndo() {
        if (!controller.isGameInProgress()) {
            JOptionPane.showMessageDialog(this,
                "No game in progress.",
                "Cannot Undo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (controller.undoMove()) {
            gamePanel.updateGrid(controller.getCurrentPuzzle());
        } else {
            JOptionPane.showMessageDialog(this,
                "No moves to undo.",
                "Cannot Undo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    //Handles hint request
    
    public void handleHint() {
        if (!controller.isGameInProgress()) {
            JOptionPane.showMessageDialog(this,
                "No game in progress.",
                "Cannot Give Hint",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String hint = controller.getHint();
        
        if (hint.startsWith("Only")) {
            // Specific hint - try to highlight the cell
            try {
                // Parse hint to find cell location
                String[] parts = hint.split(" ");
                String location = parts[parts.length - 1];
                char rowChar = location.charAt(0);
                int colNum = Integer.parseInt(location.substring(1));
                
                int row = rowChar - 'A';
                int col = colNum - 1;
                
                gamePanel.highlightCell(row, col);
            } catch (Exception e) {
                // If parsing fails, just show the hint
            }
        }
        
        JOptionPane.showMessageDialog(this,
            hint,
            "Hint",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    //Handles auto-solve request
    
    public void handleSolve() {
        if (!controller.isGameInProgress()) {
            JOptionPane.showMessageDialog(this,
                "No game in progress.",
                "Cannot Solve",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(this,
            "This will automatically solve the entire puzzle.\n" +
            "This action cannot be undone and won't count towards statistics.\n" +
            "Continue?",
            "Confirm Auto Solve",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            if (controller.solvePuzzle()) {
                gamePanel.updateGrid(controller.getCurrentPuzzle());
                JOptionPane.showMessageDialog(this,
                    "Puzzle solved automatically!",
                    "Puzzle Solved",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Unable to solve this puzzle.",
                    "Solve Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    //Enables/disables controls based on game state
    
    public void setControlsEnabled(boolean enabled) {
        saveGameBtn.setEnabled(enabled);
        undoBtn.setEnabled(enabled);
        hintBtn.setEnabled(enabled);
        solveBtn.setEnabled(enabled);
    }
}