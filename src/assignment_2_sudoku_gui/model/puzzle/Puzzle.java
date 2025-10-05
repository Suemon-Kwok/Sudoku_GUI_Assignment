/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.model.puzzle;

/*
Abstract base class for all puzzle types.

Implements Template Method pattern for puzzle solving.

Reused from CUI version with minor adaptations.
 */
public abstract class Puzzle {
    
    protected int size;
    public int[][] grid;
    protected String name;
    
    
    //Constructor initializes puzzle properties
    
    public Puzzle(int size, String name) {
        this.size = size;
        this.name = name;
        this.grid = new int[size][size];
    }
    
    
    //Template method defines solving algorithm structure
    
    public final boolean solvePuzzle() {
        if (!isValidPuzzle()) return false;
        return solve();
    }
    
    
    //Solves the puzzle using subclass-specific algorithm
    
    public abstract boolean solve();
    
    
    //Validates if a move is legal according to puzzle rules
    
    public abstract boolean isValidMove(int row, int col, int num);
    
    
    //Validates the entire puzzle state
    
    public abstract boolean isValidPuzzle();
    
    
    //Returns grid size
    
    public int getSize() { 
        return size; 
    }
    
    
    //Returns puzzle name
    
    public String getName() { 
        return name; 
    }
    
    
    //Returns reference to puzzle grid
    
    public int[][] getGrid() { 
        return grid; 
    }
    
    
    //Sets cell value if coordinates are valid
    
    public void setCell(int row, int col, int value) {
        if (isValidCoordinate(row, col)) {
            grid[row][col] = value;
        }
    }
    
    
    //Checks if coordinates are within grid bounds
    
    protected boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}