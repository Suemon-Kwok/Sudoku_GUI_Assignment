/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.strategies;

import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic hint strategy implementation.
 * Reused from CUI version with same logic.
 */
public class BasicHintStrategy extends HintStrategy {
    
    @Override
    public String generateHint(SudokuPuzzle puzzle) {
        // Search for cells with only one valid number
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle.getGrid()[row][col] == 0) {
                    List<Integer> possibleValues = new ArrayList<>();
                    
                    for (int num = 1; num <= 9; num++) {
                        if (puzzle.isValidMove(row, col, num)) {
                            possibleValues.add(num);
                        }
                    }
                    
                    if (possibleValues.size() == 1) {
                        char rowChar = (char)('A' + row);
                        return "Only " + possibleValues.get(0) + " can go in " + 
                               rowChar + (col + 1);
                    }
                }
            }
        }
        
        return "Look for cells with only one possible number.";
    }
}