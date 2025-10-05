/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.test;

import assignment_2_sudoku_gui.controller.GameControllerGUI;
import assignment_2_sudoku_gui.model.enums.DifficultyLevel;
import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for GameControllerGUI class.
 * Tests game flow and state management.
 */
public class GameControllerGUITest {
    
    private GameControllerGUI controller;
    
    @Before
    public void setUp() {
        controller = new GameControllerGUI();
    }
    
    @Test
    public void testControllerInitialization() {
        assertNotNull("Controller should be initialized", controller);
        assertFalse("Game should not be in progress initially", 
                   controller.isGameInProgress());
        assertNull("Current puzzle should be null initially", 
                  controller.getCurrentPuzzle());
    }
    
    @Test
    public void testStartNewClassicGame() {
        controller.startNewGame(false, DifficultyLevel.EASY);
        
        assertTrue("Game should be in progress", 
                  controller.isGameInProgress());
        assertNotNull("Current puzzle should exist", 
                     controller.getCurrentPuzzle());
        assertNotNull("Current mode should exist", 
                     controller.getCurrentMode());
        assertEquals("Mode should be Classic", "Classic", 
                    controller.getCurrentMode().getModeName());
    }
    
    @Test
    public void testStartNewTimedGame() {
        controller.startNewGame(true, DifficultyLevel.MEDIUM);
        
        assertTrue("Game should be in progress", 
                  controller.isGameInProgress());
        assertEquals("Mode should be Timed", "Timed", 
                    controller.getCurrentMode().getModeName());
    }
    
    @Test
    public void testGetHint() {
        controller.startNewGame(false, DifficultyLevel.EASY);
        
        String hint = controller.getHint();
        
        assertNotNull("Hint should not be null", hint);
        assertFalse("Hint should not be empty", hint.isEmpty());
    }
    
    @Test
    public void testUndoMove() {
        controller.startNewGame(false, DifficultyLevel.EASY);
        SudokuPuzzle puzzle = controller.getCurrentPuzzle();
        
        // Find empty cell and make a move
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle.getGrid()[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (puzzle.isValidMove(row, col, num)) {
                            controller.makeMove(row, col, num);
                            assertTrue("Undo should succeed", controller.undoMove());
                            return;
                        }
                    }
                }
            }
        }
    }
}