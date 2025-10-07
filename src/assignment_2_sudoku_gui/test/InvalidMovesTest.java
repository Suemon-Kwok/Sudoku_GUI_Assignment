/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.test;

import assignment_2_sudoku_gui.*;
import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*
JUnit tests for invalid move detection in Sudoku.

Tests various rule violations and edge cases.
*/
public class InvalidMovesTest {
    
    private SudokuPuzzle puzzle;
    
    
    @Before
    public void setUp() {
        System.out.println("\n=== Setting up Invalid Moves test ===");
        puzzle = new SudokuPuzzle();
    }
    
    
    @Test
    public void testInvalidMoveRowDuplicate() {
        System.out.println("Test: Invalid move - row duplicate");
        
        // Set up puzzle with a number in row
        puzzle.setCell(0, 0, 5);
        
        // Try to place same number in same row
        boolean isValid = puzzle.isValidMove(0, 8, 5);
        
        assertFalse("Should reject duplicate number in same row", isValid);
        
        System.out.println("✓ Row duplicate correctly rejected");
    }
    
    
    @Test
    public void testInvalidMoveColumnDuplicate() {
        System.out.println("Test: Invalid move - column duplicate");
        
        // Set up puzzle with a number in column
        puzzle.setCell(0, 0, 7);
        
        // Try to place same number in same column
        boolean isValid = puzzle.isValidMove(8, 0, 7);
        
        assertFalse("Should reject duplicate number in same column", isValid);
        
        System.out.println("✓ Column duplicate correctly rejected");
    }
    
    
    @Test
    public void testInvalidMoveBoxDuplicate() {
        System.out.println("Test: Invalid move - 3x3 box duplicate");
        
        // Set up puzzle with a number in top-left box
        puzzle.setCell(0, 0, 9);
        
        // Try to place same number in same 3x3 box
        boolean isValid = puzzle.isValidMove(2, 2, 9);
        
        assertFalse("Should reject duplicate number in same 3x3 box", isValid);
        
        System.out.println("✓ Box duplicate correctly rejected");
    }
    
    
    @Test
    public void testInvalidMoveMultipleViolations() {
        System.out.println("Test: Invalid move - multiple rule violations");
        
        // Set up a puzzle with numbers that would cause multiple violations
        puzzle.setCell(0, 0, 4);  // Row 0, Column 0
        puzzle.setCell(0, 5, 4);  // Row 0 (row violation)
        puzzle.setCell(5, 3, 4);  // Column 3 (column violation)
        puzzle.setCell(1, 1, 4);  // Box 0 (box violation)
        
        // Try to place 4 at position that violates row rule
        boolean isValidRow = puzzle.isValidMove(0, 3, 4);
        assertFalse("Should reject move that violates row rule", isValidRow);
        
        // Try to place 4 at position that violates column rule
        boolean isValidCol = puzzle.isValidMove(3, 5, 4);
        assertFalse("Should reject move that violates column rule", isValidCol);
        
        // Try to place 4 at position that violates box rule
        boolean isValidBox = puzzle.isValidMove(2, 2, 4);
        assertFalse("Should reject move that violates box rule", isValidBox);
        
        System.out.println("✓ All rule violations correctly detected");
    }
    
    
    @Test
    public void testValidMoveAfterInvalidAttempts() {
        System.out.println("Test: Valid move after invalid attempts");
        
        // Set up puzzle
        puzzle.setCell(0, 0, 3);
        
        // Try invalid move
        boolean invalid = puzzle.isValidMove(0, 1, 3);
        assertFalse("Invalid move should be rejected", invalid);
        
        // Try valid move in different location
        boolean valid = puzzle.isValidMove(5, 5, 3);
        assertTrue("Valid move should be accepted", valid);
        
        System.out.println("✓ Valid moves accepted after invalid attempts");
    }
    
    
    @Test
    public void testInvalidMoveOnOriginalCell() {
        System.out.println("Test: Attempt to modify original clue cell");
        
        // Set up original puzzle
        puzzle.setCell(0, 0, 8);
        puzzle.setOriginalGrid();
        
        // Verify cell is marked as original
        assertTrue("Cell should be marked as original", 
                  puzzle.isCellOriginal(0, 0));
        
        // Attempt to make move on original cell
        puzzle.makeMove(0, 0, 5);
        
        // Verify cell value hasn't changed
        assertEquals("Original cell value should not change", 
                    8, puzzle.getGrid()[0][0]);
        
        System.out.println("✓ Original cells protected from modification");
    }
    
    
    @Test
    public void testInvalidMoveBoundaryNumbers() {
        System.out.println("Test: Invalid moves with boundary numbers");
        
        // Place number 1 (minimum valid number)
        puzzle.setCell(0, 0, 1);
        boolean invalid1 = puzzle.isValidMove(0, 1, 1);
        assertFalse("Should reject duplicate 1 in row", invalid1);
        
        // Place number 9 (maximum valid number)
        puzzle.setCell(1, 0, 9);
        boolean invalid9 = puzzle.isValidMove(1, 1, 9);
        assertFalse("Should reject duplicate 9 in row", invalid9);
        
        System.out.println("✓ Boundary numbers validated correctly");
    }
    
    
    @Test
    public void testInvalidMoveComplexScenario() {
        System.out.println("Test: Invalid move in complex scenario");
        
        // Create a more complex puzzle state
        int[][] complexGrid = {
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
        
        // Set up complex grid
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, complexGrid[row][col]);
            }
        }
        
        // Test various invalid moves
        assertFalse("Should reject 5 at (0,2) - duplicate in row", 
                   puzzle.isValidMove(0, 2, 5));
        assertFalse("Should reject 6 at (2,0) - duplicate in column", 
                   puzzle.isValidMove(2, 0, 6));
        assertFalse("Should reject 3 at (1,1) - duplicate in box", 
                   puzzle.isValidMove(1, 1, 3));
        
        // Test valid moves still work
        assertTrue("Should accept 4 at (0,2) - valid move", 
                  puzzle.isValidMove(0, 2, 4));
        assertTrue("Should accept 2 at (2,0) - valid move", 
                  puzzle.isValidMove(2, 0, 2));
        
        System.out.println("✓ Complex scenario handled correctly");
    }
    
    
    @Test
    public void testInvalidMoveAllPositionsInRow() {
        System.out.println("Test: Fill row and test all positions invalid");
        
        // Fill row 0 with numbers 1-8
        for (int col = 0; col < 8; col++) {
            puzzle.setCell(0, col, col + 1);
        }
        
        // Try to place any of these numbers in the last cell of row 0
        for (int num = 1; num <= 8; num++) {
            boolean isValid = puzzle.isValidMove(0, 8, num);
            assertFalse("Should reject " + num + " in filled row", isValid);
        }
        
        // Only 9 should be valid
        assertTrue("Should accept 9 as the only valid number", 
                  puzzle.isValidMove(0, 8, 9));
        
        System.out.println("✓ Row constraint validation working correctly");
    }
    
    
    @Test
    public void testInvalidMoveEmptyPuzzle() {
        System.out.println("Test: All moves valid in empty puzzle");
        
        // In an empty puzzle, any number should be valid in any position
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                for (int num = 1; num <= 9; num++) {
                    assertTrue("All moves should be valid in empty puzzle", 
                              puzzle.isValidMove(row, col, num));
                }
            }
        }
        
        System.out.println("✓ Empty puzzle allows all valid moves");
    }
    
    
    @Test
    public void testInvalidMoveAfterUndo() {
        System.out.println("Test: Validation after undo operation");
        
        puzzle.setOriginalGrid();
        
        // Make a move
        puzzle.makeMove(0, 0, 5);
        
        // This should now be invalid
        assertFalse("Should be invalid after placing 5", 
                   puzzle.isValidMove(0, 1, 5));
        
        // Undo the move
        puzzle.undoMove();
        
        // Now it should be valid again
        assertTrue("Should be valid again after undo", 
                  puzzle.isValidMove(0, 1, 5));
        
        System.out.println("✓ Validation works correctly after undo");
    }
}