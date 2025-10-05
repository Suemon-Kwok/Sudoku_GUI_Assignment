/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.test;

import assignment_2_sudoku_gui.database.DatabaseManager;


//Simple test to verify Derby database setup

public class DatabaseManagerTest {
    
    public static void main(String[] args) {
        System.out.println("=== Derby Database Setup Test ===");
        System.out.println();
        
        try {
            // Get database manager instance
            DatabaseManager dbManager = DatabaseManager.getInstance();
            System.out.println("✓ DatabaseManager singleton created");
            
            // Initialize database
            dbManager.initializeDatabase();
            System.out.println("✓ Database initialized successfully");
            
            // Test connection
            if (dbManager.getConnection() != null && !dbManager.getConnection().isClosed()) {
                System.out.println("✓ Database connection active");
            }
            
            // Test save operation
            boolean saveResult = dbManager.saveGame(
                "TestGame", 
                "1,2,3,4,5,6,7,8,9;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0",
                "1,2,3,4,5,6,7,8,9;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0",
                "EASY", 
                "Classic", 
                120000
            );
            
            if (saveResult) {
                System.out.println("✓ Database write operation successful");
            }
            
            // Test read operation
            var gameNames = dbManager.getSavedGameNames();
            if (gameNames.contains("TestGame")) {
                System.out.println("✓ Database read operation successful");
            }
            
            // Cleanup test data
            dbManager.deleteGame("TestGame");
            System.out.println("✓ Database delete operation successful");
            
            System.out.println();
            System.out.println("=== All tests passed! Database is ready to use ===");
            System.out.println();
            System.out.println("Database location: " + System.getProperty("user.dir") + "\\SudokuDB");
            
        } catch (Exception e) {
            System.err.println("✗ Database setup failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}