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
    
    /**
     * Set up test fixture before each test
     */
    @Before
    public void setUp() {
        controller = new GameControllerGUI();
    }
    
    /**
     * Test controller initialization
     */
    @Test
    public void testControllerInitialization() {
        assertNotNull("Controller should be initialized", controller);
        assertFalse("Game should not be in progress initially", 
                   controller.isGameInProgress());
        assertNull("Current puzzle should be null initially", 
                  controller.getCurrentPuzzle());
    }
    
    /**
     * Test starting a new classic game
     */
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
    
    /**
     * Test starting a new timed game
     */
    @Test
    public void testStartNewTimedGame() {
        controller.startNewGame(true, DifficultyLevel.MEDIUM);
        
        assertTrue("Game should be in progress", 
                  controller.isGameInProgress());
        assertEquals("Mode should be Timed", "Timed", 
                    controller.getCurrentMode().getModeName());
    }
    
    /**
     * Test making a valid move
     */
    @Test
    public void testMakeValidMove() {
        controller.startNewGame(false, DifficultyLevel.EASY);
        SudokuPuzzle puzzle = controller.getCurrentPuzzle();
        
        // Find an empty cell
        int emptyRow = -1, emptyCol = -1;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle.getGrid()[row][col] == 0) {
                    emptyRow = row;
                    emptyCol = col;
                    break;
                }
            }
            if (emptyRow != -1) break;
        }
        
        if (emptyRow != -1) {
            // Find a valid number for this cell
            for (int num = 1; num <= 9; num++) {
                if (puzzle.isValidMove(emptyRow, emptyCol, num)) {
                    controller.makeMove(emptyRow, emptyCol, num);
                    assertEquals("Move should be applied", num, 
                               puzzle.getGrid()[emptyRow][emptyCol]);
                    break;
                }
            }
        }
    }
    
    /**
     * Test undo functionality
     */
    @Test
    public void testUndoMove() {
        controller.startNewGame(false, DifficultyLevel.EASY);
        SudokuPuzzle puzzle = controller.getCurrentPuzzle();
        
        // Find an empty cell and make a move
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle.getGrid()[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (puzzle.isValidMove(row, col, num)) {
                            controller.makeMove(row, col, num);
                            
                            // Now undo
                            assertTrue("Undo should succeed", 
                                     controller.undoMove());
                            assertEquals("Cell should be cleared", 0, 
                                       puzzle.getGrid()[row][col]);
                            return;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Test getting a hint
     */
    @Test
    public void testGetHint() {
        controller.startNew