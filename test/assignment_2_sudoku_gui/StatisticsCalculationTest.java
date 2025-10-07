/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui;

import assignment_2_sudoku_gui.database.StatisticsData;
import org.junit.Test;
import static org.junit.Assert.*;

/*
JUnit tests for statistics calculation accuracy.

Tests win rate, average play time, and difficulty tracking calculations.
*/
public class StatisticsCalculationTest {
    
    
    @Test
    public void testWinRateCalculationZeroGames() {
        System.out.println("\n=== Test: Win rate with zero games ===");
        
        StatisticsData stats = new StatisticsData(0, 0, 0, 0, 0, 0, 0);
        
        double winRate = stats.getWinRate();
        
        assertEquals("Win rate should be 0 when no games played", 
                    0.0, winRate, 0.01);
        
        System.out.println("✓ Zero games win rate calculated correctly: " + winRate + "%");
    }
    
    
    @Test
    public void testWinRateCalculationPerfectRecord() {
        System.out.println("\n=== Test: Win rate with perfect record ===");
        
        StatisticsData stats = new StatisticsData(10, 10, 60000, 5, 3, 2, 0);
        
        double winRate = stats.getWinRate();
        
        assertEquals("Win rate should be 100% when all games won", 
                    100.0, winRate, 0.01);
        
        System.out.println("✓ Perfect record win rate: " + winRate + "%");
    }
    
    
    @Test
    public void testWinRateCalculationPartialWins() {
        System.out.println("\n=== Test: Win rate with partial wins ===");
        
        // 7 wins out of 10 games = 70%
        StatisticsData stats = new StatisticsData(10, 7, 120000, 4, 2, 1, 0);
        
        double winRate = stats.getWinRate();
        
        assertEquals("Win rate should be 70% for 7/10 wins", 
                    70.0, winRate, 0.01);
        
        System.out.println("✓ Partial win rate calculated correctly: " + winRate + "%");
    }
    
    
    @Test
    public void testWinRateCalculationNoWins() {
        System.out.println("\n=== Test: Win rate with no wins ===");
        
        StatisticsData stats = new StatisticsData(5, 0, 300000, 0, 0, 0, 0);
        
        double winRate = stats.getWinRate();
        
        assertEquals("Win rate should be 0% when no games won", 
                    0.0, winRate, 0.01);
        
        System.out.println("✓ No wins rate calculated correctly: " + winRate + "%");
    }
    
    
    @Test
    public void testWinRateCalculationDecimalPrecision() {
        System.out.println("\n=== Test: Win rate decimal precision ===");
        
        // 1 win out of 3 games = 33.333...%
        StatisticsData stats = new StatisticsData(3, 1, 90000, 1, 0, 0, 0);
        
        double winRate = stats.getWinRate();
        
        assertTrue("Win rate should be approximately 33.33%", 
                  Math.abs(winRate - 33.33) < 0.01);
        
        System.out.println("✓ Decimal precision win rate: " + 
                          String.format("%.2f", winRate) + "%");
    }
    
    
    @Test
    public void testAveragePlayTimeZeroGames() {
        System.out.println("\n=== Test: Average play time with zero games ===");
        
        StatisticsData stats = new StatisticsData(0, 0, 0, 0, 0, 0, 0);
        
        long avgTime = stats.getAveragePlayTime();
        
        assertEquals("Average play time should be 0 when no games played", 
                    0L, avgTime);
        
        System.out.println("✓ Zero games average time: " + avgTime + "ms");
    }
    
    
    @Test
    public void testAveragePlayTimeCalculation() {
        System.out.println("\n=== Test: Average play time calculation ===");
        
        // Total: 300000ms, Games: 5, Average: 60000ms (1 minute)
        StatisticsData stats = new StatisticsData(5, 3, 300000, 2, 1, 0, 0);
        
        long avgTime = stats.getAveragePlayTime();
        
        assertEquals("Average play time should be 60000ms", 
                    60000L, avgTime);
        
        System.out.println("✓ Average play time calculated correctly: " + avgTime + "ms");
    }
    
    
    @Test
    public void testAveragePlayTimeLongGames() {
        System.out.println("\n=== Test: Average play time for long games ===");
        
        // Total: 36000000ms (10 hours), Games: 10, Average: 3600000ms (1 hour)
        StatisticsData stats = new StatisticsData(10, 8, 36000000, 5, 3, 0, 0);
        
        long avgTime = stats.getAveragePlayTime();
        
        assertEquals("Average play time should be 3600000ms (1 hour)", 
                    3600000L, avgTime);
        
        long minutes = avgTime / 60000;
        System.out.println("✓ Long games average: " + minutes + " minutes");
    }
    
    
    @Test
    public void testAveragePlayTimeShortGames() {
        System.out.println("\n=== Test: Average play time for short games ===");
        
        // Total: 50000ms, Games: 10, Average: 5000ms (5 seconds)
        StatisticsData stats = new StatisticsData(10, 6, 50000, 6, 0, 0, 0);
        
        long avgTime = stats.getAveragePlayTime();
        
        assertEquals("Average play time should be 5000ms", 
                    5000L, avgTime);
        
        System.out.println("✓ Short games average: " + avgTime + "ms");
    }
    
    
    @Test
    public void testDifficultyCompletionTracking() {
        System.out.println("\n=== Test: Difficulty completion tracking ===");
        
        StatisticsData stats = new StatisticsData(20, 15, 600000, 8, 5, 2, 0);
        
        assertEquals("Easy completions should be 8", 
                    8, stats.getEasyCompleted());
        assertEquals("Medium completions should be 5", 
                    5, stats.getMediumCompleted());
        assertEquals("Hard completions should be 2", 
                    2, stats.getHardCompleted());
        assertEquals("Expert completions should be 0", 
                    0, stats.getExpertCompleted());
        
        // Total completions should match games won
        int totalCompletions = stats.getEasyCompleted() + 
                              stats.getMediumCompleted() + 
                              stats.getHardCompleted() + 
                              stats.getExpertCompleted();
        
        assertEquals("Total completions should match games won", 
                    stats.getGamesWon(), totalCompletions);
        
        System.out.println("✓ Difficulty tracking accurate:");
        System.out.println("  Easy: " + stats.getEasyCompleted());
        System.out.println("  Medium: " + stats.getMediumCompleted());
        System.out.println("  Hard: " + stats.getHardCompleted());
        System.out.println("  Expert: " + stats.getExpertCompleted());
    }
    
    
    @Test
    public void testStatisticsDataImmutability() {
        System.out.println("\n=== Test: Statistics data immutability ===");
        
        StatisticsData stats = new StatisticsData(10, 7, 120000, 4, 2, 1, 0);
        
        int originalGamesPlayed = stats.getGamesPlayed();
        int originalGamesWon = stats.getGamesWon();
        long originalTotalTime = stats.getTotalPlayTime();
        
        // Call getters multiple times
        for (int i = 0; i < 5; i++) {
            assertEquals("Games played should remain constant", 
                        originalGamesPlayed, stats.getGamesPlayed());
            assertEquals("Games won should remain constant", 
                        originalGamesWon, stats.getGamesWon());
            assertEquals("Total play time should remain constant", 
                        originalTotalTime, stats.getTotalPlayTime());
        }
        
        System.out.println("✓ Statistics data is properly immutable");
    }
    
    
    @Test
    public void testWinRateWithLargeNumbers() {
        System.out.println("\n=== Test: Win rate with large numbers ===");
        
        // 999 wins out of 1000 games
        StatisticsData stats = new StatisticsData(1000, 999, 60000000, 
                                                  500, 300, 150, 49);
        
        double winRate = stats.getWinRate();
        
        assertEquals("Win rate should be 99.9% for 999/1000", 
                    99.9, winRate, 0.01);
        
        System.out.println("✓ Large numbers win rate: " + winRate + "%");
    }
    
    
    @Test
    public void testAveragePlayTimeRounding() {
        System.out.println("\n=== Test: Average play time integer rounding ===");
        
        // Total: 100000ms, Games: 3, Average: 33333.33ms -> 33333ms (integer division)
        StatisticsData stats = new StatisticsData(3, 2, 100000, 2, 0, 0, 0);
        
        long avgTime = stats.getAveragePlayTime();
        
        assertEquals("Average should use integer division (33333ms)", 
                    33333L, avgTime);
        
        System.out.println("✓ Integer rounding handled correctly: " + avgTime + "ms");
    }
    
    
    @Test
    public void testStatisticsWithAllDifficulties() {
        System.out.println("\n=== Test: Statistics with all difficulties ===");
        
        StatisticsData stats = new StatisticsData(40, 40, 1200000, 10, 10, 10, 10);
        
        // All difficulties should have completions
        assertTrue("Easy should have completions", 
                  stats.getEasyCompleted() > 0);
        assertTrue("Medium should have completions", 
                  stats.getMediumCompleted() > 0);
        assertTrue("Hard should have completions", 
                  stats.getHardCompleted() > 0);
        assertTrue("Expert should have completions", 
                  stats.getExpertCompleted() > 0);
        
        // Total should be evenly distributed
        assertEquals("Each difficulty should have 10 completions", 
                    10, stats.getEasyCompleted());
        
        System.out.println("✓ All difficulty levels tracked correctly");
    }
    
    
    @Test
    public void testStatisticsEdgeCaseMaxValues() {
        System.out.println("\n=== Test: Statistics with maximum values ===");
        
        // Test with very large values
        StatisticsData stats = new StatisticsData(
            Integer.MAX_VALUE / 2, 
            Integer.MAX_VALUE / 4, 
            Long.MAX_VALUE / 2, 
            1000000, 
            1000000, 
            1000000, 
            1000000
        );
        
        assertTrue("Games played should handle large values", 
                  stats.getGamesPlayed() > 0);
        assertTrue("Win rate should be calculated correctly", 
                  stats.getWinRate() >= 0 && stats.getWinRate() <= 100);
        
        System.out.println("✓ Large values handled without overflow");
        System.out.println("  Games played: " + stats.getGamesPlayed());
        System.out.println("  Win rate: " + String.format("%.2f", stats.getWinRate()) + "%");
    }
    
    
    @Test
    public void testStatisticsConsistency() {
        System.out.println("\n=== Test: Statistics internal consistency ===");
        
        StatisticsData stats = new StatisticsData(15, 10, 450000, 5, 3, 2, 0);
        
        // Games won should never exceed games played
        assertTrue("Games won should not exceed games played", 
                  stats.getGamesWon() <= stats.getGamesPlayed());
        
        // Difficulty completions should not exceed games won
        int totalCompletions = stats.getEasyCompleted() + 
                              stats.getMediumCompleted() + 
                              stats.getHardCompleted() + 
                              stats.getExpertCompleted();
        
        assertEquals("Total completions should match games won", 
                    stats.getGamesWon(), totalCompletions);
        
        // Win rate should be logical
        double expectedWinRate = (double) stats.getGamesWon() / 
                                stats.getGamesPlayed() * 100;
        assertEquals("Win rate calculation should be accurate", 
                    expectedWinRate, stats.getWinRate(), 0.01);
        
        System.out.println("✓ All statistics are internally consistent");
    }
}