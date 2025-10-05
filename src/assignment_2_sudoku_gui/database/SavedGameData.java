/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.database;

import java.sql.Timestamp;

/**
 * Immutable data transfer object for saved game data.
 * Demonstrates immutable design pattern.
 */
public class SavedGameData {
    private final String gameName;
    private final String gridData;
    private final String originalGrid;
    private final String difficulty;
    private final String gameMode;
    private final long playTime;
    private final Timestamp saveDate;
    
    public SavedGameData(String gameName, String gridData, String originalGrid,
                        String difficulty, String gameMode, long playTime, Timestamp saveDate) {
        this.gameName = gameName;
        this.gridData = gridData;
        this.originalGrid = originalGrid;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.playTime = playTime;
        this.saveDate = saveDate;
    }
    
    public String getGameName() { return gameName; }
    public String getGridData() { return gridData; }
    public String getOriginalGrid() { return originalGrid; }
    public String getDifficulty() { return difficulty; }
    public String getGameMode() { return gameMode; }
    public long getPlayTime() { return playTime; }
    public Timestamp getSaveDate() { return saveDate; }
}