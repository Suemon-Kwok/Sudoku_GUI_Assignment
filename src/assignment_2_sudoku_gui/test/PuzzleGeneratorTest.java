/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.test;

import assignment_2_sudoku_gui.generators.PuzzleGenerator;
import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import assignment_2_sudoku_gui.model.enums.DifficultyLevel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for PuzzleGenerator class.
 * Tests puzzle generation and validation.
 */
public class PuzzleGeneratorTest {
    
    private PuzzleGenerator generator;
    private SudokuPuzzle puzzle;
    
    /**
     * Set up test fixture before each test
     */
    @Before
    public void setUp() {
        generator = new PuzzleGenerator();
        puzzle = new SudokuPuzzle();
    }
    
    /**
     * Test generator initialization
     */
    @Test
    public void testGeneratorInitialization() {
        assertNotNull("Generator should be initialized", generator);
    }
    
    /**
     * Test easy puzzle generation
     */
    @Test
    public void testGenerateEasyPuzzle() {
        generator.generatePuzzle(puzzle, DifficultyLevel.EASY);
        
        assertNotNull("Puzzle should be generated", puzzle);
        assertTrue("Puzzle should be valid", puzzle.isValidPuzzle());
        
        int clueCount = countClues(puzzle);
        assertEquals("Easy puzzle should have 40 clues", 
                    40, clueCount);
    }
    
    /**
     * Test medium puzzle generation
     */
    @Test
    public void testGenerateMediumPuzzle() {
        generator.generatePuzzle(puzzle, DifficultyLevel.MEDIUM);
        
        assertTrue("Puzzle should be valid", puzzle.isValidPuzzle());
        
        int clueCount = countClues(puzzle);
        assertEquals("Medium puzzle should have 30 clues", 
                    30, clueCount);
    }
    
    /**
     * Test hard puzzle generation
     */
    @Test
    public void testGenerateHardPuzzle() {
        generator.generatePuzzle(puzzle, DifficultyLevel.HARD);
        
        assertTrue("Puzzle should be valid", puzzle.isValidPuzzle());
        
        int clueCount = countClues(puzzle);
        assertEquals("Hard puzzle should have 25 clues", 
                    25, clueCount);
    }
    
    /**
     * Test expert puzzle generation
     */
    @Test
    public void testGenerateExpertPuzzle() {
        generator.generatePuzzle(puzzle, DifficultyLevel.EXPERT);
        
        assertTrue("Puzzle should be valid", puzzle.isValidPuzzle());
        
        int clueCount = countClues(puzzle);
        assertEquals("Expert puzzle should have 20 clues", 
                    20, clueCount);
    }
    
    /**
     * Test generated puzzle is solvable
     */
    @Test
    public void testGeneratedPuzzleIsSolvable() {
        generator.generatePuzzle(puzzle, DifficultyLevel.MEDIUM);
        
        assertTrue("Generated puzzle should be solvable", 
                  puzzle.canBeSolved());
    }
    
    /**
     * Test generated puzzle has no duplicates
     */
    @Test
    public void testGeneratedPuzzleHasNoDuplicates() {
        generator.generatePuzzle(puzzle, DifficultyLevel.EASY);
        
        // Check all rows
        for (int row = 0; row < 9; row++) {
            assertTrue("Row should have no duplicates", 
                      isRowValid(puzzle, row));
        }
        
        // Check all columns
        for (int col = 0; col < 9; col++) {
            assertTrue("Column should have no duplicates", 
                      isColumnValid(puzzle, col));
        }
        
        // Check all boxes
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                assertTrue("Box should have no duplicates", 
                          isBoxValid(puzzle, boxRow, boxCol));
            }
        }
    }
    
    /**
     * Test multiple puzzle generations
     */
    @Test
    public void testMultiplePuzzleGenerations() {
        for (int i = 0; i < 5; i++) {
            puzzle = new SudokuPuzzle();
            generator.generatePuzzle(puzzle, DifficultyLevel.MEDIUM);
            
            assertTrue("Puzzle " + i + " should be valid", 
                      puzzle.isValidPuzzle());
            assertTrue("Puzzle " + i + " should be solvable", 
                      puzzle.canBeSolved());
        }
    }
    
    /**
     * Helper method to count clues in puzzle
     */
    private int countClues(SudokuPuzzle puzzle) {
        int count = 0;
        int[][] grid = puzzle.getGrid();
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] != 0) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * Helper method to validate row
     */
    private boolean isRowValid(SudokuPuzzle puzzle, int row) {
        boolean[] used = new boolean[10];
        int[][] grid = puzzle.getGrid();
        
        for (int col = 0; col < 9; col++) {
            int num = grid[row][col];
            if (num != 0) {
                if (used[num]) return false;
                used[num] = true;
            }
        }
        return true;
    }
    
    /**
     * Helper method to validate column
     */
    private boolean isColumnValid(SudokuPuzzle puzzle, int col) {
        boolean[] used = new boolean[10];
        int[][] grid = puzzle.getGrid();
        
        for (int row = 0; row < 9; row++) {
            int num = grid[row][col];
            if (num != 0) {
                if (used[num]) return false;
                used[num] = true;
            }
        }
        return true;
    }
    
    /**
     * Helper method to validate 3x3 box
     */
    private boolean isBoxValid(SudokuPuzzle puzzle, int startRow, int startCol) {
        boolean[] used = new boolean[10];
        int[][] grid = puzzle.getGrid();
        
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                int num = grid[row][col];
                if (num != 0) {
                    if (used[num]) return false;
                    used[num] = true;
                }
            }
        }
        return true;
    }
}