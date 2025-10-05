/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.controller;

import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import assignment_2_sudoku_gui.model.game.GameMode;
import assignment_2_sudoku_gui.model.game.ClassicMode;
import assignment_2_sudoku_gui.model.game.TimedMode;
import assignment_2_sudoku_gui.model.enums.DifficultyLevel;
import assignment_2_sudoku_gui.model.util.Move;
import assignment_2_sudoku_gui.strategies.BasicHintStrategy;
import assignment_2_sudoku_gui.strategies.HintStrategy;
import assignment_2_sudoku_gui.database.DatabaseManager;
import assignment_2_sudoku_gui.database.SavedGameData;
import assignment_2_sudoku_gui.ui.StatusPanel;
import assignment_2_sudoku_gui.ui.GamePanel;
import javax.swing.JOptionPane;
import java.awt.Color;

/**
 * Main game controller for GUI version.
 * Manages game logic, state, and coordinates between model and view.
 * Demonstrates MVC pattern and separation of concerns.
 */
public class GameControllerGUI {
    
    private SudokuPuzzle currentPuzzle;
    private GameMode currentMode;
    private boolean gameInProgress;
    private long gameStartTime;
    private HintStrategy hintStrategy;
    
    private StatusPanel statusPanel;
    private GamePanel gamePanel;
    
    /**
     * Constructor initializes controller
     */
    public GameControllerGUI() {
        this.gameInProgress = false;
        this.hintStrategy = new BasicHintStrategy();
    }
    
    /**
     * Sets the status panel reference
     */
    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }
    
    /**
     * Sets the game panel reference
     */
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    /**
     * Starts a new game with specified mode and difficulty
     */
    public void startNewGame(boolean isTimedMode, DifficultyLevel difficulty) {
        // Create appropriate game mode
        if (isTimedMode) {
            currentMode = new TimedMode(difficulty, 1800); // 30 minutes
        } else {
            currentMode = new ClassicMode(difficulty);
        }
        
        // Create and setup puzzle
        currentPuzzle = new SudokuPuzzle();
        currentMode.setupGame(currentPuzzle);
        
        // Update game state
        gameInProgress = true;
        gameStartTime = System.currentTimeMillis();
        
        // Update status panel
        if (statusPanel != null) {
            statusPanel.startTimer();
            statusPanel.setStatus("Game in progress", new Color(0, 100, 200));
            statusPanel.setMode(currentMode.getModeName());
            statusPanel.setDifficulty(difficulty.name());
        }
    }
    
    /**
     * Makes a move on the puzzle
     */
    public void makeMove(int row, int col, int value) {
        if (!gameInProgress || currentPuzzle == null) {
            return;
        }
        
        // Check if cell is original clue
        if (currentPuzzle.isCellOriginal(row, col)) {
            if (statusPanel != null) {
                statusPanel.setStatus("Cannot modify original clues!", Color.RED);
            }
            return;
        }
        
        // Validate move
        if (value != 0 && !currentPuzzle.isValidMove(row, col, value)) {
            if (statusPanel != null) {
                statusPanel.setStatus("Invalid move - breaks Sudoku rules", Color.RED);
            }
            return;
        }
        
        // Make the move
        currentPuzzle.makeMove(row, col, value);
        
        // Update display
        if (gamePanel != null) {
            gamePanel.updateGrid(currentPuzzle);
        }
        
        // Check if puzzle is solved
        if (currentPuzzle.isSolved()) {
            handlePuzzleCompletion();
        } else {
            if (statusPanel != null) {
                statusPanel.setStatus("Game in progress", new Color(0, 100, 200));
            }
        }
    }
    
    /**
     * Handles puzzle completion
     */
    private void handlePuzzleCompletion() {
        gameInProgress = false;
        
        if (statusPanel != null) {
            statusPanel.stopTimer();
            statusPanel.setStatus("Puzzle completed!", new Color(0, 150, 0));
        }
        
        long playTime = System.currentTimeMillis() - gameStartTime;
        
        // Update database statistics
        DatabaseManager.getInstance().updateStatistics(
            1, 1, playTime, currentMode.getDifficulty().name());
        
        // Show completion message
        String timeStr = formatTime(playTime);
        JOptionPane.showMessageDialog(null,
            "Congratulations! You completed the puzzle!\n" +
            "Time: " + timeStr + "\n" +
            "Difficulty: " + currentMode.getDifficulty().name(),
            "Puzzle Completed",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Undoes the last move
     */
    public boolean undoMove() {
        if (!gameInProgress || currentPuzzle == null) {
            return false;
        }
        
        boolean success = currentPuzzle.undoMove();
        
        if (success && statusPanel != null) {
            statusPanel.setStatus("Move undone", new Color(0, 100, 200));
        }
        
        return success;
    }
    
    /**
     * Gets a hint for the current puzzle
     */
    public String getHint() {
        if (!gameInProgress || currentPuzzle == null) {
            return "No game in progress";
        }
        
        return hintStrategy.generateHint(currentPuzzle);
    }
    
    /**
     * Automatically solves the puzzle
     */
    public boolean solvePuzzle() {
        if (!gameInProgress || currentPuzzle == null) {
            return false;
        }
        
        if (currentPuzzle.solve()) {
            int[][] solution = currentPuzzle.getSolutionResult().getSolution();
            
            // Copy solution to current puzzle
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    currentPuzzle.setCell(row, col, solution[row][col]);
                }
            }
            
            gameInProgress = false;
            
            if (statusPanel != null) {
                statusPanel.stopTimer();
                statusPanel.setStatus("Puzzle auto-solved", new Color(200, 100, 0));
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Saves the current game to database
     */
    public boolean saveGame(String gameName) {
        if (!gameInProgress || currentPuzzle == null) {
            return false;
        }
        
        // Convert grid to string format
        String gridData = gridToString(currentPuzzle.getGrid());
        String originalGrid = gridToString(getOriginalGrid());
        
        long playTime = statusPanel != null ? statusPanel.getElapsedTime() : 0;
        
        return DatabaseManager.getInstance().saveGame(
            gameName,
            gridData,
            originalGrid,
            currentMode.getDifficulty().name(),
            currentMode.getModeName(),
            playTime
        );
    }
    
    /**
     * Loads a game from database
     */
    public boolean loadGame(String gameName) {
        SavedGameData gameData = DatabaseManager.getInstance().loadGame(gameName);
        
        if (gameData == null) {
            return false;
        }
        
        try {
            // Parse difficulty and mode
            DifficultyLevel difficulty = DifficultyLevel.valueOf(gameData.getDifficulty());
            boolean isTimedMode = gameData.getGameMode().equals("Timed");
            
            // Create game mode
            if (isTimedMode) {
                currentMode = new TimedMode(difficulty, 1800);
            } else {
                currentMode = new ClassicMode(difficulty);
            }
            
            // Create puzzle and load data
            currentPuzzle = new SudokuPuzzle();
            
            int[][] grid = stringToGrid(gameData.getGridData());
            int[][] original = stringToGrid(gameData.getOriginalGrid());
            
            // Set grid data
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    currentPuzzle.setCell(row, col, grid[row][col]);
                }
            }
            
            // Set original grid
            setOriginalGrid(original);
            currentPuzzle.setOriginalGrid();
            
            // Update game state
            gameInProgress = true;
            gameStartTime = System.currentTimeMillis() - gameData.getPlayTime();
            
            // Update status panel
            if (statusPanel != null) {
                statusPanel.startTimer();
                statusPanel.setStatus("Game loaded", new Color(0, 100, 200));
                statusPanel.setMode(currentMode.getModeName());
                statusPanel.setDifficulty(difficulty.name());
            }
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Converts grid to string format for database storage
     */
    private String gridToString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                sb.append(grid[row][col]);
                if (col < 8) sb.append(",");
            }
            if (row < 8) sb.append(";");
        }
        return sb.toString();
    }
    
    /**
     * Converts string format to grid array
     */
    private int[][] stringToGrid(String data) {
        int[][] grid = new int[9][9];
        String[] rows = data.split(";");
        
        for (int row = 0; row < 9 && row < rows.length; row++) {
            String[] values = rows[row].split(",");
            for (int col = 0; col < 9 && col < values.length; col++) {
                grid[row][col] = Integer.parseInt(values[col].trim());
            }
        }
        
        return grid;
    }
    
    /**
     * Gets original grid from current puzzle
     */
    private int[][] getOriginalGrid() {
        int[][] original = new int[9][9];
        int[][] current = currentPuzzle.getGrid();
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (currentPuzzle.isCellOriginal(row, col)) {
                    original[row][col] = current[row][col];
                }
            }
        }
        
        return original;
    }
    
    /**
     * Sets original grid for puzzle
     */
    private void setOriginalGrid(int[][] original) {
        // This is a workaround since we need access to originalGrid
        // In a real implementation, you might add a setter to SudokuPuzzle
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (original[row][col] != 0) {
                    currentPuzzle.setCell(row, col, original[row][col]);
                }
            }
        }
    }
    
    /**
     * Formats milliseconds to readable time
     */
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    /**
     * Checks if game is currently in progress
     */
    public boolean isGameInProgress() {
        return gameInProgress;
    }
    
    /**
     * Gets current puzzle
     */
    public SudokuPuzzle getCurrentPuzzle() {
        return currentPuzzle;
    }
    
    /**
     * Gets current game mode
     */
    public GameMode getCurrentMode() {
        return currentMode;
    }
}