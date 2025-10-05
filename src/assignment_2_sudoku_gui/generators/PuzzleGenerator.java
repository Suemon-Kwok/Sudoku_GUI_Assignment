/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.generators;

import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import assignment_2_sudoku_gui.model.enums.DifficultyLevel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generator class for creating valid Sudoku puzzles.
 * Reused from CUI version with same algorithms.
 */
public class PuzzleGenerator {
    
    private Random random;
    
    /**
     * Constructor initializes random generator
     */
    public PuzzleGenerator() {
        this.random = new Random();
    }
    
    /**
     * Generates a complete puzzle with specified difficulty
     */
    public void generatePuzzle(SudokuPuzzle puzzle, DifficultyLevel difficulty) {
        clearPuzzle(puzzle);
        
        if (!generateCompleteSolution(puzzle)) {
            generatePuzzle(puzzle, difficulty);
            return;
        }
        
        if (!puzzle.isCompletelyValid()) {
            generatePuzzle(puzzle, difficulty);
            return;
        }
        
        removeNumbersWithValidation(puzzle, difficulty.getClueCount());
        puzzle.setOriginalGrid();
        
        if (!puzzle.isCompletelyValid()) {
            generatePuzzle(puzzle, difficulty);
        }
    }
    
    /**
     * Clears all cells in the puzzle grid
     */
    private void clearPuzzle(SudokuPuzzle puzzle) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle.setCell(row, col, 0);
            }
        }
    }
    
    /**
     * Generates a complete valid Sudoku solution
     */
    private boolean generateCompleteSolution(SudokuPuzzle puzzle) {
        return fillGridRandomly(puzzle, 0, 0);
    }
    
    /**
     * Recursively fills grid using backtracking with randomized number order
     */
    private boolean fillGridRandomly(SudokuPuzzle puzzle, int row, int col) {
        if (row == 9) return true;
        if (col == 9) return fillGridRandomly(puzzle, row + 1, 0);
        if (puzzle.getGrid()[row][col] != 0) 
            return fillGridRandomly(puzzle, row, col + 1);
        
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) numbers.add(i);
        Collections.shuffle(numbers, random);
        
        for (int num : numbers) {
            if (puzzle.isValidMove(row, col, num)) {
                puzzle.setCell(row, col, num);
                if (fillGridRandomly(puzzle, row, col + 1)) return true;
                puzzle.setCell(row, col, 0);
            }
        }
        return false;
    }
    
    /**
     * Removes numbers from complete solution while maintaining solvability
     */
    private void removeNumbersWithValidation(SudokuPuzzle puzzle, int cluesToKeep) {
        int cellsToRemove = 81 - cluesToKeep;
        List<int[]> positions = new ArrayList<>();
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle.getGrid()[row][col] != 0) {
                    positions.add(new int[]{row, col});
                }
            }
        }
        
        Collections.shuffle(positions, random);
        
        int removed = 0;
        for (int[] pos : positions) {
            if (removed >= cellsToRemove) break;
            
            int row = pos[0];
            int col = pos[1];
            int originalValue = puzzle.getGrid()[row][col];
            
            puzzle.setCell(row, col, 0);
            
            if (hasUniqueSolution(puzzle)) {
                removed++;
            } else {
                puzzle.setCell(row, col, originalValue);
            }
        }
    }
    
    /**
     * Checks if puzzle has a unique solution
     */
    private boolean hasUniqueSolution(SudokuPuzzle puzzle) {
        int[][] testGrid = new int[9][9];
        copyGrid(puzzle.getGrid(), testGrid);
        
        SudokuPuzzle testPuzzle = new SudokuPuzzle();
        copyGrid(testGrid, testPuzzle.grid);
        
        return testPuzzle.canBeSolved();
    }
    
    /**
     * Utility method to copy grid data
     */
    private void copyGrid(int[][] source, int[][] destination) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, 9);
        }
    }
}