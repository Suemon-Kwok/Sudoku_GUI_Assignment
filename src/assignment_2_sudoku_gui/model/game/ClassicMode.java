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
 * Classic game mode implementation.
 * Reused from CUI version.
 */
public class ClassicMode extends GameMode {
    
    /**
     * Constructor sets mode name and difficulty
     */
    public ClassicMode(DifficultyLevel difficulty) {
        super("Classic", difficulty);
    }
    
    /**
     * Sets up puzzle for classic mode gameplay
     */
    @Override
    public void setupGame(SudokuPuzzle puzzle) {
        PuzzleGenerator generator = new PuzzleGenerator();
        generator.generatePuzzle(puzzle, difficulty);
    }
    
    /**
     * Returns hint count based on difficulty level
     */
    @Override
    public int getHintCount() {
        return switch (difficulty) {
            case EASY -> 5;
            case MEDIUM -> 3;
            case HARD -> 2;
            case EXPERT -> 1;
        };
    }
}