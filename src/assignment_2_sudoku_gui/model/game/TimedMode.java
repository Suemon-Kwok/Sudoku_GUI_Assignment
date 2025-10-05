/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.model.game;

import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import assignment_2_sudoku_gui.model.enums.DifficultyLevel;
import assignment_2_sudoku_gui.generators.PuzzleGenerator;

/**
 * Timed game mode implementation.
 * Reused from CUI version.
 */
public class TimedMode extends GameMode {
    
    private long timeLimit;
    
    /**
     * Constructor initializes timed mode with time limit
     */
    public TimedMode(DifficultyLevel difficulty, long timeLimit) {
        super("Timed", difficulty);
        this.timeLimit = timeLimit;
    }
    
    /**
     * Sets up puzzle for timed mode gameplay
     */
    @Override
    public void setupGame(SudokuPuzzle puzzle) {
        PuzzleGenerator generator = new PuzzleGenerator();
        generator.generatePuzzle(puzzle, difficulty);
    }
    
    /**
     * Returns reduced hint count for timed mode
     */
    @Override
    public int getHintCount() {
        return switch (difficulty) {
            case EASY -> 3;
            case MEDIUM -> 2;
            case HARD -> 1;
            case EXPERT -> 0;
        };
    }
    
    /**
     * Gets the time limit for this mode
     */
    public long getTimeLimit() { 
        return timeLimit; 
    }
}