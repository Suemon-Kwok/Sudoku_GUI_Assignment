/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.test;

import assignment_2_sudoku_gui.database.DatabaseManager;
import assignment_2_sudoku_gui.database.SavedGameData;
import assignment_2_sudoku_gui.database.StatisticsData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/*
JUnit tests for DatabaseManager class.

Tests database initialization, CRUD operations, and connection management.
*/
public class DatabaseManagerTest {
    
    private DatabaseManager dbManager;
    private static final String TEST_GAME_NAME = "JUnitTestGame";
    private static final String TEST_GRID_DATA = "5,3,0,0,7,0,0,0,0;6,0,0,1,9,5,0,0,0;0,9,8,0,0,0,0,6,0;8,0,0,0,6,0,0,0,3;4,0,0,8,0,3,0,0,1;7,0,0,0,2,0,0,0,6;0,6,0,0,0,0,2,8,0;0,0,0,4,1,9,0,0,5;0,0,0,0,8,0,0,7,9";
    private static final String TEST_ORIGINAL_GRID = "5,3,0,0,7,0,0,0,0;6,0,0,1,9,5,0,0,0;0,9,8,0,0,0,0,6,0;8,0,0,0,6,0,0,0,3;4,0,0,8,0,3,0,0,1;7,0,0,0,2,0,0,0,6;0,6,0,0,0,0,2,8,0;0,0,0,4,1,9,0,0,5;0,0,0,0,8,0,0,7,9";
    
    
    /**
     * Main method to run tests standalone
     */
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Running DatabaseManager JUnit Tests    ");
        System.out.println("===========================================\n");
        
        Result result = JUnitCore.runClasses(DatabaseManagerTest.class);
        
        System.out.println("\n===========================================");
        System.out.println("              TEST RESULTS                 ");
        System.out.println("===========================================");
        System.out.println("Total tests run: " + result.getRunCount());
        System.out.println("Tests passed: " + (result.getRunCount() - result.getFailureCount()));
        System.out.println("Tests failed: " + result.getFailureCount());
        System.out.println("Time taken: " + result.getRunTime() + "ms");
        
        if (result.getFailureCount() > 0) {
            System.out.println("\n--- FAILURES ---");
            for (Failure failure : result.getFailures()) {
                System.out.println("❌ " + failure.getTestHeader());
                System.out.println("   " + failure.getMessage());
                System.out.println("   " + failure.getTrace());
            }
        }
        
        if (result.wasSuccessful()) {
            System.out.println("\n✅ ALL TESTS PASSED!");
        } else {
            System.out.println("\n❌ SOME TESTS FAILED!");
        }
        System.out.println("===========================================\n");
    }
    
    
    @Before
    public void setUp() throws SQLException {
        System.out.println("\n=== Setting up DatabaseManager test ===");
        dbManager = DatabaseManager.getInstance();
        dbManager.initializeDatabase();
        
        // Clean up any existing test data
        dbManager.deleteGame(TEST_GAME_NAME);
    }
    
    
    @After
    public void tearDown() {
        System.out.println("=== Cleaning up test data ===");
        // Clean up test data
        dbManager.deleteGame(TEST_GAME_NAME);
        dbManager.deleteGame(TEST_GAME_NAME + "_2");
    }
    
    
    @Test
    public void testGetInstance() {
        System.out.println("Test: DatabaseManager singleton instance");
        
        DatabaseManager instance1 = DatabaseManager.getInstance();
        DatabaseManager instance2 = DatabaseManager.getInstance();
        
        assertNotNull("First instance should not be null", instance1);
        assertNotNull("Second instance should not be null", instance2);
        assertSame("Both instances should be the same (Singleton)", instance1, instance2);
        
        System.out.println("✓ Singleton pattern working correctly");
    }
    
    
    @Test
    public void testInitializeDatabase() throws SQLException {
        System.out.println("Test: Database initialization");
        
        dbManager.initializeDatabase();
        Connection conn = dbManager.getConnection();
        
        assertNotNull("Database connection should not be null", conn);
        assertFalse("Database connection should be open", conn.isClosed());
        
        System.out.println("✓ Database initialized successfully");
    }
    
    
    @Test
    public void testGetConnection() throws SQLException {
        System.out.println("Test: Get database connection");
        
        Connection conn = dbManager.getConnection();
        
        assertNotNull("Connection should not be null", conn);
        assertFalse("Connection should be open", conn.isClosed());
        assertTrue("Connection should be valid", conn.isValid(2));
        
        System.out.println("✓ Database connection is valid");
    }
    
    
    @Test
    public void testSaveGame() {
        System.out.println("Test: Save game to database");
        
        boolean result = dbManager.saveGame(
            TEST_GAME_NAME,
            TEST_GRID_DATA,
            TEST_ORIGINAL_GRID,
            "EASY",
            "Classic",
            120000L
        );
        
        assertTrue("Game should be saved successfully", result);
        
        // Verify game was saved
        List<String> savedGames = dbManager.getSavedGameNames();
        assertTrue("Saved games list should contain test game", 
                  savedGames.contains(TEST_GAME_NAME));
        
        System.out.println("✓ Game saved successfully");
    }
    
    
    @Test
    public void testSaveGameOverwrite() {
        System.out.println("Test: Overwrite existing saved game");
        
        // Save game first time
        boolean firstSave = dbManager.saveGame(
            TEST_GAME_NAME,
            TEST_GRID_DATA,
            TEST_ORIGINAL_GRID,
            "EASY",
            "Classic",
            60000L
        );
        assertTrue("First save should succeed", firstSave);
        
        // Save game again with different data (should overwrite)
        boolean secondSave = dbManager.saveGame(
            TEST_GAME_NAME,
            TEST_GRID_DATA,
            TEST_ORIGINAL_GRID,
            "HARD",
            "Timed",
            120000L
        );
        assertTrue("Second save should succeed (overwrite)", secondSave);
        
        // Verify updated data
        SavedGameData loaded = dbManager.loadGame(TEST_GAME_NAME);
        assertNotNull("Loaded game should not be null", loaded);
        assertEquals("Difficulty should be updated", "HARD", loaded.getDifficulty());
        assertEquals("Game mode should be updated", "Timed", loaded.getGameMode());
        assertEquals("Play time should be updated", 120000L, loaded.getPlayTime());
        
        System.out.println("✓ Game overwrite successful");
    }
    
    
    @Test
    public void testLoadGame() {
        System.out.println("Test: Load game from database");
        
        // First save a game
        dbManager.saveGame(
            TEST_GAME_NAME,
            TEST_GRID_DATA,
            TEST_ORIGINAL_GRID,
            "MEDIUM",
            "Classic",
            90000L
        );
        
        // Load the game
        SavedGameData gameData = dbManager.loadGame(TEST_GAME_NAME);
        
        assertNotNull("Loaded game should not be null", gameData);
        assertEquals("Game name should match", TEST_GAME_NAME, gameData.getGameName());
        assertEquals("Grid data should match", TEST_GRID_DATA, gameData.getGridData());
        assertEquals("Original grid should match", TEST_ORIGINAL_GRID, gameData.getOriginalGrid());
        assertEquals("Difficulty should match", "MEDIUM", gameData.getDifficulty());
        assertEquals("Game mode should match", "Classic", gameData.getGameMode());
        assertEquals("Play time should match", 90000L, gameData.getPlayTime());
        assertNotNull("Save date should not be null", gameData.getSaveDate());
        
        System.out.println("✓ Game loaded successfully with correct data");
    }
    
    
    @Test
    public void testLoadNonExistentGame() {
        System.out.println("Test: Load non-existent game");
        
        SavedGameData gameData = dbManager.loadGame("NonExistentGame12345");
        
        assertNull("Non-existent game should return null", gameData);
        
        System.out.println("✓ Correctly handles non-existent game");
    }
    
    
    @Test
    public void testGetSavedGameNames() {
        System.out.println("Test: Get list of saved game names");
        
        // Save multiple games
        dbManager.saveGame(TEST_GAME_NAME, TEST_GRID_DATA, TEST_ORIGINAL_GRID, 
                          "EASY", "Classic", 60000L);
        dbManager.saveGame(TEST_GAME_NAME + "_2", TEST_GRID_DATA, TEST_ORIGINAL_GRID, 
                          "HARD", "Timed", 120000L);
        
        List<String> savedGames = dbManager.getSavedGameNames();
        
        assertNotNull("Saved games list should not be null", savedGames);
        assertTrue("List should contain first test game", 
                  savedGames.contains(TEST_GAME_NAME));
        assertTrue("List should contain second test game", 
                  savedGames.contains(TEST_GAME_NAME + "_2"));
        
        System.out.println("✓ Retrieved " + savedGames.size() + " saved games");
    }
    
    
    @Test
    public void testDeleteGame() {
        System.out.println("Test: Delete saved game");
        
        // Save a game first
        dbManager.saveGame(TEST_GAME_NAME, TEST_GRID_DATA, TEST_ORIGINAL_GRID, 
                          "EASY", "Classic", 60000L);
        
        // Verify it exists
        assertTrue("Game should exist before deletion", 
                  dbManager.getSavedGameNames().contains(TEST_GAME_NAME));
        
        // Delete the game
        boolean result = dbManager.deleteGame(TEST_GAME_NAME);
        
        assertTrue("Delete operation should succeed", result);
        assertFalse("Game should not exist after deletion", 
                   dbManager.getSavedGameNames().contains(TEST_GAME_NAME));
        
        System.out.println("✓ Game deleted successfully");
    }
    
    
    @Test
    public void testDeleteNonExistentGame() {
        System.out.println("Test: Delete non-existent game");
        
        boolean result = dbManager.deleteGame("NonExistentGame99999");
        
        assertFalse("Deleting non-existent game should return false", result);
        
        System.out.println("✓ Correctly handles deletion of non-existent game");
    }
    
    
    @Test
    public void testUpdateStatistics() {
        System.out.println("Test: Update game statistics");
        
        // Get initial statistics
        StatisticsData initialStats = dbManager.getStatistics();
        int initialGamesPlayed = initialStats.getGamesPlayed();
        int initialGamesWon = initialStats.getGamesWon();
        
        // Update statistics
        boolean result = dbManager.updateStatistics(1, 1, 180000L, "EASY");
        
        assertTrue("Statistics update should succeed", result);
        
        // Get updated statistics
        StatisticsData updatedStats = dbManager.getStatistics();
        
        assertEquals("Games played should increase by 1", 
                    initialGamesPlayed + 1, updatedStats.getGamesPlayed());
        assertEquals("Games won should increase by 1", 
                    initialGamesWon + 1, updatedStats.getGamesWon());
        
        System.out.println("✓ Statistics updated successfully");
    }
    
    
    @Test
    public void testUpdateStatisticsByDifficulty() {
        System.out.println("Test: Update statistics for different difficulties");
        
        // Get initial statistics
        StatisticsData initialStats = dbManager.getStatistics();
        int initialEasy = initialStats.getEasyCompleted();
        int initialMedium = initialStats.getMediumCompleted();
        int initialHard = initialStats.getHardCompleted();
        int initialExpert = initialStats.getExpertCompleted();
        
        // Update each difficulty
        dbManager.updateStatistics(1, 1, 60000L, "EASY");
        dbManager.updateStatistics(1, 1, 90000L, "MEDIUM");
        dbManager.updateStatistics(1, 1, 120000L, "HARD");
        dbManager.updateStatistics(1, 1, 180000L, "EXPERT");
        
        // Get updated statistics
        StatisticsData updatedStats = dbManager.getStatistics();
        
        assertEquals("Easy completions should increase", 
                    initialEasy + 1, updatedStats.getEasyCompleted());
        assertEquals("Medium completions should increase", 
                    initialMedium + 1, updatedStats.getMediumCompleted());
        assertEquals("Hard completions should increase", 
                    initialHard + 1, updatedStats.getHardCompleted());
        assertEquals("Expert completions should increase", 
                    initialExpert + 1, updatedStats.getExpertCompleted());
        
        System.out.println("✓ All difficulty statistics updated correctly");
    }
    
    
    @Test
    public void testGetStatistics() {
        System.out.println("Test: Retrieve game statistics");
        
        StatisticsData stats = dbManager.getStatistics();
        
        assertNotNull("Statistics should not be null", stats);
        assertTrue("Games played should be non-negative", 
                  stats.getGamesPlayed() >= 0);
        assertTrue("Games won should be non-negative", 
                  stats.getGamesWon() >= 0);
        assertTrue("Total play time should be non-negative", 
                  stats.getTotalPlayTime() >= 0);
        assertTrue("Win rate should be between 0 and 100", 
                  stats.getWinRate() >= 0 && stats.getWinRate() <= 100);
        
        System.out.println("✓ Statistics retrieved successfully");
        System.out.println("  Games Played: " + stats.getGamesPlayed());
        System.out.println("  Games Won: " + stats.getGamesWon());
        System.out.println("  Win Rate: " + String.format("%.1f%%", stats.getWinRate()));
    }
    
    
    @Test
    public void testSaveGameWithSpecialCharacters() {
        System.out.println("Test: Save game with special characters in name");
        
        String specialName = "Test-Game_2024";
        
        boolean result = dbManager.saveGame(
            specialName,
            TEST_GRID_DATA,
            TEST_ORIGINAL_GRID,
            "EASY",
            "Classic",
            60000L
        );
        
        assertTrue("Should save game with special characters", result);
        
        SavedGameData loaded = dbManager.loadGame(specialName);
        assertNotNull("Should load game with special characters", loaded);
        assertEquals("Game name should match", specialName, loaded.getGameName());
        
        // Cleanup
        dbManager.deleteGame(specialName);
        
        System.out.println("✓ Handles special characters correctly");
    }
    
    
    @Test
    public void testMultipleSaveAndLoadOperations() {
        System.out.println("Test: Multiple save and load operations");
        
        // Perform multiple save/load cycles
        for (int i = 1; i <= 3; i++) {
            String gameName = TEST_GAME_NAME + "_Multi_" + i;
            
            // Save
            boolean saved = dbManager.saveGame(
                gameName,
                TEST_GRID_DATA,
                TEST_ORIGINAL_GRID,
                "EASY",
                "Classic",
                60000L * i
            );
            assertTrue("Save operation " + i + " should succeed", saved);
            
            // Load
            SavedGameData loaded = dbManager.loadGame(gameName);
            assertNotNull("Load operation " + i + " should succeed", loaded);
            assertEquals("Play time should match for operation " + i, 
                        60000L * i, loaded.getPlayTime());
            
            // Delete
            dbManager.deleteGame(gameName);
        }
        
        System.out.println("✓ Multiple operations completed successfully");
    }
}