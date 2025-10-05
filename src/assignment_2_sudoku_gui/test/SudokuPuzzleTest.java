/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.test;

import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for SudokuPuzzle class.
 * Tests core puzzle functionality and validation logic.
 */
public class SudokuPuzzleTest {
    
    private SudokuPuzzle puzzle;
    
    /**
     * Set up test fixture before each test
     */
    @Before
    public void setUp() {
        puzzle = new SudokuPuzzle();
    }
    
    /**
     * Test puzzle initialization
     */
    @Test
    public void testPuzzleInitialization() {
        assertNotNull("Puzzle should be initialized", puzzle);
        assertEquals("Puzzle size should be 9", 9, puzzle.getSize());
        assertEquals("Puzzle name should be Sudoku", "Sudoku", puzzle.getName());
        assertNotNull("Grid should be initialized", puzzle.getGrid());
    }
    
    /**
     * Test valid move placement
     */
    @Test
    public void testValidMove() {
        // Place a number in empty grid
        puzzle.setCell(0, 0, 5);
        
        // Should be valid to place different number in same row but different column
        assertTrue("Should accept valid move in row", 
                   puzzle.isValidMove(0, 1, 3));
        
        // Should be valid to place different number in same column but different row
        assertTrue("Should accept valid move in column", 
                   puzzle.isValidMove(1, 0, 3));
    }
    
    /**
     * Test invalid move detection - row conflict
     */
    @Test
    public void testInvalidMoveRowConflict() {
        puzzle.setCell(0, 0, 5);
        
        // Try to place same number in same row
        assertFalse("Should reject duplicate in row", 
                    puzzle.isValidMove(0, 5, 5));
    }
    
    /**
     * Test invalid move detection - column conflict
     */
    @Test
    public void testInvalidMoveColumnConflict() {
        puzzle.setCell(0, 0, 5);
        
        // Try to place same number in same column
        assertFalse("Should reject duplicate in column", 
                    puzzle.isValidMove(5, 0, 5));
    }
    
    /**
     * Test invalid move detection - box conflict
     */
    @Test
    public void testInvalidMoveBoxConflict() {
        puzzle.setCell(0, 0, 5);
        
        // Try to place same number in same 3x3 box
        assertFalse("Should reject duplicate in box", 
                    puzzle.isValidMove(2, 2, 5));
    }
    
    /**
     * Test cell modification
     */
    @Test
    public void testSetCell() {
        puzzle.setCell(4, 4, 7);
        assertEquals("Cell value should be set correctly", 
                     7, puzzle.getGrid()[4][4]);
    }
    
    /**
     * Test original grid marking
     */
    @Test
    public void testOriginalGridMarking() {
        puzzle.setCell(0, 0, 5);
        puzzle.setCell(1, 1, 3);
        puzzle.setOriginalGrid();
        
        assertTrue("Cell should be marked as original", 
                   puzzle.isCellOriginal(0, 0));
        assertTrue("Cell should be marked as original", 
                   puzzle.isCellOriginal(1, 1));
        assertFalse("Empty cell should not be original", 
                    puzzle.isCellOriginal(2, 2));
    }
    
    /**
     * Test move history with undo
     */
    @Test
    public void testMoveHistoryAndUndo() {
        puzzle.setOriginalGrid(); // Mark as original so moves are allowed
        
        puzzle.makeMove(0, 0, 5);
        assertEquals("Move should be recorded", 
                     5, puzzle.getGrid()[0][0]);
        
        assertTrue("Undo should succeed", puzzle.undoMove());
        assertEquals("Cell should be cleared after undo", 
                     0, puzzle.getGrid()[0][0]);
    }
    
    /**
     * Test undo with no moves
     */
    @Test
    public void testUndoWithNoMoves() {
        assertFalse("Undo should fail with no moves", 
                    puzzle.undoMove());
    }
    
    /**
     * Test multiple moves and undos
     */
    @Test
    public void testMultipleMovesAndUndos() {
        puzzle.setOriginalGrid();
        
        puzzle.makeMove(0, 0, 5);
        puzzle.makeMove(0, 1, 3);
        puzzle.makeMove(0, 2, 7);
        
        assertEquals("First move should be recorded", 
                     5, puzzle.getGrid()[0][0]);
        assertEquals("Second move should be recorded", 
                     3, puzzle.getGrid()[0][1]);
        assertEquals("Third move should be recorded", 
                     7, puzzle.getGrid()[0][2]);
        
        puzzle.undoMove();
        assertEquals("Last move should be undone", 
                     0, puzzle.getGrid()[0][2]);
        
        puzzle.undoMove();
        assertEquals("Second last move should be undone", 
                     0, puzzle.getGrid()[0][1]);
    }
    
    /**
     * Test puzzle validation with valid configuration
     */
    @Test
    public void testValidPuzzleConfiguration() {
        // Create a valid partial puzzle
        puzzle.setCell(0, 0, 5);
        puzzle.setCell(0, 1, 3);
        puzzle.setCell(1, 0, 6);
        
        assertTrue("Valid configuration should pass validation", 
                   puzzle.isValidPuzzle());
    }
    
    /**
     * Test puzzle validation with invalid configuration
     */
    @Test
    public void testInvalidPuzzleConfiguration() {
        // Create invalid puzzle with duplicate in row
        puzzle.setCell(0, 0, 5);
        puzzle.setCell(0, 1, 5); // Duplicate
        
        assertFalse("Invalid configuration should fail validation", 
                    puzzle.isValidPuzzle());
    }
    
    /**
     * Test puzzle solving capability
     */
    @Test
    public void testPuzzleSolving() {
        // Create a simple solvable puzzle
        int[][] simple = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                puzzle.setCell(i, j, simple[i][j]);
            }
        }
        
        assertTrue("Puzzle should be solvable", puzzle.canBeSolved());
    }
    
    /**
     * Test that original cells cannot be modified
     */
    @Test
    public void testOriginalCellsCannotBeModified() {
        puzzle.setCell(0, 0, 5);
        puzzle.setOriginalGrid();
        
        // Try to make move on original cell
        puzzle.makeMove(0, 0, 7);
        
        // Value should remain unchanged
        assertEquals("Original cell should not be modified", 
                     5, puzzle.getGrid()[0][0]);
    }
}