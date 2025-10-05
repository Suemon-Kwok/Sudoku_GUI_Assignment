/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.model.puzzle;

import assignment_2_sudoku_gui.model.util.Move;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of Sudoku puzzle.
 * Adapted from CUI version for GUI use.
 */
public class SudokuPuzzle extends Puzzle {
    
    private int[][] originalGrid;
    private int[][] solutionGrid;
    private List<Move> moveHistory;
    private boolean isSolved;
    
    /**
     * Constructor initializes Sudoku-specific properties
     */
    public SudokuPuzzle() {
        super(9, "Sudoku");
        this.originalGrid = new int[9][9];
        this.solutionGrid = new int[9][9];
        this.moveHistory = new ArrayList<>();
        this.isSolved = false;
    }
    
    /**
     * Solves the puzzle using backtracking algorithm
     */
    @Override
    public boolean solve() {
        copyGrid(grid, solutionGrid);
        return solveSudoku(solutionGrid, 0, 0);
    }
    
    /**
     * Recursive backtracking solver implementation
     */
    private boolean solveSudoku(int[][] board, int row, int col) {
        if (row == 9) return true;
        if (col == 9) return solveSudoku(board, row + 1, 0);
        if (board[row][col] != 0) return solveSudoku(board, row, col + 1);
        
        for (int num = 1; num <= 9; num++) {
            if (isValidMoveForBoard(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board, row, col + 1)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }
    
    /**
     * Validates move during solving process
     */
    private boolean isValidMoveForBoard(int[][] board, int row, int col, int num) {
        // Check row
        for (int c = 0; c < 9; c++) {
            if (board[row][c] == num) return false;
        }
        
        // Check column
        for (int r = 0; r < 9; r++) {
            if (board[r][col] == num) return false;
        }
        
        // Check 3x3 box
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (board[r][c] == num) return false;
            }
        }
        return true;
    }
    
    /**
     * Validates move against Sudoku rules
     */
    @Override
    public boolean isValidMove(int row, int col, int num) {
        int originalValue = grid[row][col];
        grid[row][col] = 0;
        
        boolean isValid = isValidRow(row, num) && 
                          isValidColumn(col, num) && 
                          isValidBox(row, col, num);
        
        grid[row][col] = originalValue;
        return isValid;
    }
    
    /**
     * Row validation helper
     */
    private boolean isValidRow(int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (grid[row][col] == num) return false;
        }
        return true;
    }
    
    /**
     * Column validation helper
     */
    private boolean isValidColumn(int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (grid[row][col] == num) return false;
        }
        return true;
    }
    
    /**
     * 3x3 box validation helper
     */
    private boolean isValidBox(int row, int col, int num) {
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (grid[r][c] == num) return false;
            }
        }
        return true;
    }
    
    /**
     * Validates entire puzzle for duplicates
     */
    public boolean isCompletelyValid() {
        for (int row = 0; row < 9; row++) {
            if (!isRowValid(row)) return false;
        }
        
        for (int col = 0; col < 9; col++) {
            if (!isColumnValid(col)) return false;
        }
        
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                if (!isBoxValid(boxRow, boxCol)) return false;
            }
        }
        return true;
    }
    
    /**
     * Row validation (duplicate check)
     */
    private boolean isRowValid(int row) {
        boolean[] used = new boolean[10];
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
     * Column validation (duplicate check)
     */
    private boolean isColumnValid(int col) {
        boolean[] used = new boolean[10];
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
     * 3x3 box validation (duplicate check)
     */
    private boolean isBoxValid(int startRow, int startCol) {
        boolean[] used = new boolean[10];
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
    
    @Override
    public boolean isValidPuzzle() {
        return isCompletelyValid();
    }
    
    /**
     * Records player move in history
     */
    public void makeMove(int row, int col, int value) {
        if (originalGrid[row][col] == 0) {
            Move move = new Move(row, col, grid[row][col], value);
            moveHistory.add(move);
            setCell(row, col, value);
            checkIfSolved();
        }
    }
    
    /**
     * Reverts last move from history
     */
    public boolean undoMove() {
        if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.remove(moveHistory.size() - 1);
            setCell(lastMove.getRow(), lastMove.getCol(), lastMove.getOldValue());
            isSolved = false;
            return true;
        }
        return false;
    }
    
    /**
     * Checks if puzzle is fully solved
     */
    private void checkIfSolved() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    isSolved = false;
                    return;
                }
            }
        }
        isSolved = isValidPuzzle();
    }
    
    /**
     * Copies grid data from source to destination
     */
    private void copyGrid(int[][] source, int[][] destination) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, 9);
        }
    }
    
    /**
     * Sets current grid as original state (clues)
     */
    public void setOriginalGrid() {
        copyGrid(grid, originalGrid);
    }
    
    /**
     * Checks if cell contains original clue
     */
    public boolean isCellOriginal(int row, int col) {
        return originalGrid[row][col] != 0;
    }
    
    /**
     * Checks if puzzle is solved
     */
    public boolean isSolved() { 
        return isSolved; 
    }
    
    /**
     * Returns copy of move history
     */
    public List<Move> getMoveHistory() {  
        return new ArrayList<>(moveHistory);
    }
    
    /**
     * Checks if the puzzle can be solved
     */
    public boolean canBeSolved() {
        int[][] tempGrid = new int[9][9];
        copyGrid(grid, tempGrid);
        return solveSudoku(tempGrid, 0, 0);
    }
    
    /**
     * Gets solution result
     */
    public SolutionResult getSolutionResult() {
        if (solve()) {
            return new SolutionResult(true, "Solution found", solutionGrid);
        }
        return new SolutionResult(false, "No solution exists", null);
    }
    
    /**
     * Inner class for solution results
     */
    public static class SolutionResult {
        private final boolean solved;
        private final String message;
        private final int[][] solution;
        
        public SolutionResult(boolean solved, String message, int[][] solution) {
            this.solved = solved;
            this.message = message;
            this.solution = solution;
        }
        
        public boolean isSolved() { return solved; }
        public String getMessage() { return message; }
        public int[][] getSolution() { return solution; }
    }
}