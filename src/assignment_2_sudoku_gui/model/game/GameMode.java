/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.model.game;

import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import assignment_2_sudoku_gui.model.enums.DifficultyLevel;

/**
 * Abstract base class for game modes.
 * Reused from CUI version.
 */
public abstract class GameMode {
    
    protected String modeName;
    protected DifficultyLevel difficulty;
    
    /**
     * Constructor initializes game mode properties
     */
    public GameMode(String modeName, DifficultyLevel difficulty) {
        this.modeName = modeName;
        this.difficulty = difficulty;
    }
    
    /**
     * Abstract method to setup game with mode-specific configuration
     */
    public abstract void setupGame(SudokuPuzzle puzzle);
    
    /**
     * Abstract method to get number of available hints
     */
    public abstract int getHintCount();
    
    /**
     * Gets the mode name
     */
    public String getModeName() { 
        return modeName; 
    }
    
    /**
     * Gets the difficulty level
     */
    public DifficultyLevel getDifficulty() { 
        return difficulty; 
    }
}