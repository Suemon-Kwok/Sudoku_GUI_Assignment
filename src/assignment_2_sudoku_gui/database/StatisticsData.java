/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.database;

/**
 * Immutable data transfer object for statistics data.
 */
public class StatisticsData {
    private final int gamesPlayed;
    private final int gamesWon;
    private final long totalPlayTime;
    private final int easyCompleted;
    private final int mediumCompleted;
    private final int hardCompleted;
    private final int expertCompleted;
    
    public StatisticsData(int gamesPlayed, int gamesWon, long totalPlayTime,
                         int easyCompleted, int mediumCompleted, 
                         int hardCompleted, int expertCompleted) {
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.totalPlayTime = totalPlayTime;
        this.easyCompleted = easyCompleted;
        this.mediumCompleted = mediumCompleted;
        this.hardCompleted = hardCompleted;
        this.expertCompleted = expertCompleted;
    }
    
    public int getGamesPlayed() { return gamesPlayed; }
    public int getGamesWon() { return gamesWon; }
    public long getTotalPlayTime() { return totalPlayTime; }
    public int getEasyCompleted() { return easyCompleted; }
    public int getMediumCompleted() { return mediumCompleted; }
    public int getHardCompleted() { return hardCompleted; }
    public int getExpertCompleted() { return expertCompleted; }
    
    public double getWinRate() {
        return gamesPlayed > 0 ? (double) gamesWon / gamesPlayed * 100 : 0;
    }
    
    public long getAveragePlayTime() {
        return gamesPlayed > 0 ? totalPlayTime / gamesPlayed : 0;
    }
}