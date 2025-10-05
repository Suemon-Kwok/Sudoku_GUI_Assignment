/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.model.enums;

/**
 * Enumeration defining difficulty levels.
 * Reused from CUI version.
 */
public enum DifficultyLevel {
    EASY(40, "Easy - 40 clues"),
    MEDIUM(30, "Medium - 30 clues"),
    HARD(25, "Hard - 25 clues"),
    EXPERT(20, "Expert - 20 clues");
    
    private final int clueCount;
    private final String description;
    
    DifficultyLevel(int clueCount, String description) {
        this.clueCount = clueCount;
        this.description = description;
    }
    
    public int getClueCount() { 
        return clueCount; 
    }
    
    public String getDescription() { 
        return description; 
    }
}