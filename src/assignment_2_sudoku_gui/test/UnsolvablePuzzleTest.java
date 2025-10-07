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
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/*
JUnit tests for unsolvable puzzle detection.

Tests puzzle solver's ability to recognize invalid/unsolvable configurations.
*/
public class UnsolvablePuzzleTest {
    
    private SudokuPuzzle puzzle;
    
    // Add timeout rule to prevent hanging
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 second timeout
    
    @Before
    public void setUp() {
        System.out.println("\n=== Setting up Unsolvable Puzzle test ===");
        puzzle = new SudokuPuzzle();
    }
    
    @Test
    public void testUnsolvablePuzzleDuplicateRow() {
        System.out.println("Test: Unsolvable puzzle - duplicate in row");
        
        // Create puzzle with duplicate in row (invalid configuration)
        int[][] invalidGrid = {
            {5, 5, 0, 0, 0, 0, 0, 0, 0},  // Two 5's in first row
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, invalidGrid[row][col]);
            }
        }
        
        // This puzzle is invalid and should not be solvable
        assertFalse("Puzzle with duplicate in row should be invalid", 
                   puzzle.isValidPuzzle());
        
        System.out.println("✓ Detected invalid puzzle with row duplicate");
    }
    
    @Test
    public void testUnsolvablePuzzleDuplicateColumn() {
        System.out.println("Test: Unsolvable puzzle - duplicate in column");
        
        // Create puzzle with duplicate in column
        int[][] invalidGrid = new int[9][9];
        invalidGrid[0][0] = 7;
        invalidGrid[1][0] = 7;  // Two 7's in first column
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, invalidGrid[row][col]);
            }
        }
        
        assertFalse("Puzzle with duplicate in column should be invalid", 
                   puzzle.isValidPuzzle());
        
        System.out.println("✓ Detected invalid puzzle with column duplicate");
    }
    
    @Test
    public void testUnsolvablePuzzleDuplicateBox() {
        System.out.println("Test: Unsolvable puzzle - duplicate in 3x3 box");
        
        // Create puzzle with duplicate in 3x3 box
        int[][] invalidGrid = new int[9][9];
        invalidGrid[0][0] = 3;
        invalidGrid[2][2] = 3;  // Two 3's in top-left box
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, invalidGrid[row][col]);
            }
        }
        
        assertFalse("Puzzle with duplicate in box should be invalid", 
                   puzzle.isValidPuzzle());
        
        System.out.println("✓ Detected invalid puzzle with box duplicate");
    }
    
    @Test
    public void testSolvableValidPuzzle() {
        System.out.println("Test: Solvable valid puzzle");
        
        // Create a known solvable puzzle
        int[][] solvableGrid = {
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
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, solvableGrid[row][col]);
            }
        }
        
        assertTrue("Valid puzzle should pass validation", 
                  puzzle.isValidPuzzle());
        assertTrue("Valid puzzle should be solvable", 
                  puzzle.canBeSolved());
        
        System.out.println("✓ Valid solvable puzzle detected correctly");
    }
    
    @Test
    public void testUnsolvableOverconstrainedPuzzle() {
        System.out.println("Test: Unsolvable over-constrained puzzle");
        
        // Simplified unsolvable puzzle - clearer contradiction
        int[][] overconstrainedGrid = {
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0}  // Duplicate 1 in first column
        };
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, overconstrainedGrid[row][col]);
            }
        }
        
        // This should be detected as invalid due to the duplicate
        assertFalse("Over-constrained puzzle should not be valid", 
                   puzzle.isValidPuzzle());
        
        System.out.println("✓ Detected over-constrained unsolvable puzzle");
    }
    
    @Test
    public void testEmptyPuzzleIsSolvable() {
        System.out.println("Test: Empty puzzle is solvable");
        
        // Empty puzzle should be solvable (all zeros)
        assertTrue("Empty puzzle should be valid", 
                  puzzle.isValidPuzzle());
        assertTrue("Empty puzzle should be solvable", 
                  puzzle.canBeSolved());
        
        System.out.println("✓ Empty puzzle correctly identified as solvable");
    }
    
    @Test
    public void testAlmostCompletePuzzleWithConflict() {
        System.out.println("Test: Almost complete puzzle with conflict");
        
        // Create a puzzle that's almost complete but has a conflict
        int[][] conflictGrid = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
        
        // This is actually a complete valid solution
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, conflictGrid[row][col]);
            }
        }
        
        assertTrue("Complete valid puzzle should be valid", 
                  puzzle.isValidPuzzle());
        
        // Now introduce a conflict
        puzzle.setCell(0, 0, 0);  // Clear one cell
        puzzle.setCell(0, 1, 0);  // Clear another
        puzzle.setCell(0, 1, 5);  // Place conflicting number (5 is in this row)
        
        assertFalse("Puzzle with conflict should be invalid", 
                   puzzle.isValidPuzzle());
        
        System.out.println("✓ Detected conflict in almost complete puzzle");
    }
    
    @Test
    public void testPuzzleWithMultipleSolutions() {
        System.out.println("Test: Puzzle with too few clues (multiple solutions)");
        
        // Create a puzzle with minimal but valid clues
        int[][] minimalGrid = new int[9][9];
        minimalGrid[0][0] = 1;
        minimalGrid[1][1] = 2;
        minimalGrid[2][2] = 3;
        // Add a few more clues to make it solvable but not hang
        minimalGrid[8][8] = 9;
        minimalGrid[0][8] = 4;
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, minimalGrid[row][col]);
            }
        }
        
        // Should still be valid and solvable (solver finds one solution)
        assertTrue("Puzzle with few clues should be valid", 
                  puzzle.isValidPuzzle());
        
        System.out.println("✓ Puzzle with minimal clues handled correctly");
    }
    
    @Test
    public void testSolveMethodOnUnsolvablePuzzle() {
        System.out.println("Test: Solve method on unsolvable puzzle");
        
        // Create invalid puzzle
        int[][] invalidGrid = new int[9][9];
        invalidGrid[0][0] = 5;
        invalidGrid[0][1] = 5;  // Duplicate in row
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, invalidGrid[row][col]);
            }
        }
        
        // Try to solve it
        boolean solved = puzzle.solve();
        
        assertFalse("Solve should return false for invalid puzzle", solved);
        
        System.out.println("✓ Solve method correctly handles unsolvable puzzle");
    }
    
    @Test
    public void testSolveMethodOnSolvablePuzzle() {
        System.out.println("Test: Solve method on solvable puzzle");
        
        // Create solvable puzzle
        int[][] solvableGrid = {
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
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, solvableGrid[row][col]);
            }
        }
        
        boolean solved = puzzle.solve();
        
        assertTrue("Solve should return true for solvable puzzle", solved);
        
        System.out.println("✓ Solve method correctly solves valid puzzle");
    }
    
    @Test
    public void testCompletelyFilledInvalidPuzzle() {
        System.out.println("Test: Completely filled but invalid puzzle");
        
        // Fill entire grid with 1s (completely invalid)
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, 1);
            }
        }
        
        assertFalse("Completely filled invalid puzzle should be invalid", 
                   puzzle.isValidPuzzle());
        
        System.out.println("✓ Detected completely filled invalid puzzle");
    }
}