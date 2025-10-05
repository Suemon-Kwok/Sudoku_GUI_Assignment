/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.strategies;

import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;

/*
Abstract base class for hint generation strategies.

Reused from CUI version - demonstrates code reusability.
 */
public abstract class HintStrategy {
    
    
    //Abstract method for generating hints based on puzzle state
    
    public abstract String generateHint(SudokuPuzzle puzzle);
}