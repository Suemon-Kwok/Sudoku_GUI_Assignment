/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.model.util;

/**
 * Immutable class representing a single move.
 * Reused from CUI version.
 */
public class Move {
    
    private final int row;
    private final int col;
    private final int oldValue;
    private final int newValue;
    private final long timestamp;
    
    public Move(int row, int col, int oldValue, int newValue) {
        this.row = row;
        this.col = col;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.timestamp = System.currentTimeMillis();
    }
    
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getOldValue() { return oldValue; }
    public int getNewValue() { return newValue; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("Move: (%d,%d) %d->%d at %d", 
               row, col, oldValue, newValue, timestamp);
    }
}