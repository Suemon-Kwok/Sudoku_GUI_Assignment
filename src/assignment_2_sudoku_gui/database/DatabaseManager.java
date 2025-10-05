/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Singleton class managing Derby embedded database connections and operations.

Handles automatic database setup and all CRUD operations.

Uses embedded mode - no need to start Derby server manually.
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    
    // EMBEDDED MODE URL - Database will be created in project root
    private static final String DB_URL = "jdbc:derby:SudokuDB;create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    
    private Connection connection;
    
    
    //Private constructor for singleton pattern
    
    private DatabaseManager() {
        try {
            // Load Derby embedded driver
            Class.forName(DRIVER);
            System.out.println("Derby driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Derby driver not found. Make sure derby.jar is in classpath.");
            throw new RuntimeException("Derby driver not found", e);
        }
    }
    
    
    //Gets singleton instance with thread-safe double-checked locking
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    
    //Initializes database and creates tables if they don't exist
    
    public void initializeDatabase() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established: " + DB_URL);
            System.out.println("Database location: " + System.getProperty("user.dir") + "\\SudokuDB");
            createTables();
            System.out.println("Database tables initialized successfully");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            throw e;
        }
    }
    
    
    //Creates necessary database tables
    
    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            // Saved games table
            String createGamesTable = """
                CREATE TABLE SavedGames (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    gameName VARCHAR(100) NOT NULL UNIQUE,
                    gridData VARCHAR(500) NOT NULL,
                    originalGrid VARCHAR(500) NOT NULL,
                    difficulty VARCHAR(20) NOT NULL,
                    gameMode VARCHAR(20) NOT NULL,
                    playTime BIGINT DEFAULT 0,
                    saveDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Statistics table
            String createStatsTable = """
                CREATE TABLE GameStatistics (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    gamesPlayed INT DEFAULT 0,
                    gamesWon INT DEFAULT 0,
                    totalPlayTime BIGINT DEFAULT 0,
                    easyCompleted INT DEFAULT 0,
                    mediumCompleted INT DEFAULT 0,
                    hardCompleted INT DEFAULT 0,
                    expertCompleted INT DEFAULT 0,
                    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Player profiles table
            String createProfilesTable = """
                CREATE TABLE PlayerProfiles (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    playerName VARCHAR(50) NOT NULL UNIQUE,
                    bestTime BIGINT,
                    totalGames INT DEFAULT 0,
                    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Create SavedGames table
            try {
                stmt.execute(createGamesTable);
                System.out.println("SavedGames table created");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("SavedGames table already exists");
                } else {
                    throw e;
                }
            }
            
            // Create GameStatistics table
            try {
                stmt.execute(createStatsTable);
                System.out.println("GameStatistics table created");
                // Initialize statistics record
                stmt.execute("INSERT INTO GameStatistics (gamesPlayed) VALUES (0)");
                System.out.println("GameStatistics initialized with default values");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("GameStatistics table already exists");
                } else {
                    throw e;
                }
            }
            
            // Create PlayerProfiles table
            try {
                stmt.execute(createProfilesTable);
                System.out.println("PlayerProfiles table created");
            } catch (SQLException e) {
                if (e.getSQLState().equals("X0Y32")) {
                    System.out.println("PlayerProfiles table already exists");
                } else {
                    throw e;
                }
            }
        }
    }
    
    
    //Gets active database connection
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
    
    /*
    Saves a game to the database
    
    Uses separate UPDATE or INSERT logic instead of MERGE for Derby compatibility
     */
    public boolean saveGame(String gameName, String gridData, String originalGrid, 
                           String difficulty, String gameMode, long playTime) {
        
        // First, check if game already exists
        String checkSql = "SELECT COUNT(*) FROM SavedGames WHERE gameName = ?";
        
        try (PreparedStatement checkStmt = getConnection().prepareStatement(checkSql)) {
            checkStmt.setString(1, gameName);
            ResultSet rs = checkStmt.executeQuery();
            
            boolean gameExists = false;
            if (rs.next()) {
                gameExists = rs.getInt(1) > 0;
            }
            
            if (gameExists) {
                // Update existing game
                String updateSql = """
                    UPDATE SavedGames 
                    SET gridData = ?, 
                        originalGrid = ?, 
                        difficulty = ?, 
                        gameMode = ?, 
                        playTime = ?, 
                        saveDate = CURRENT_TIMESTAMP 
                    WHERE gameName = ?
                """;
                
                try (PreparedStatement updateStmt = getConnection().prepareStatement(updateSql)) {
                    updateStmt.setString(1, gridData);
                    updateStmt.setString(2, originalGrid);
                    updateStmt.setString(3, difficulty);
                    updateStmt.setString(4, gameMode);
                    updateStmt.setLong(5, playTime);
                    updateStmt.setString(6, gameName);
                    updateStmt.executeUpdate();
                    System.out.println("Game updated: " + gameName);
                    return true;
                }
                
            } else {
                // Insert new game
                String insertSql = """
                    INSERT INTO SavedGames (gameName, gridData, originalGrid, difficulty, gameMode, playTime) 
                    VALUES (?, ?, ?, ?, ?, ?)
                """;
                
                try (PreparedStatement insertStmt = getConnection().prepareStatement(insertSql)) {
                    insertStmt.setString(1, gameName);
                    insertStmt.setString(2, gridData);
                    insertStmt.setString(3, originalGrid);
                    insertStmt.setString(4, difficulty);
                    insertStmt.setString(5, gameMode);
                    insertStmt.setLong(6, playTime);
                    insertStmt.executeUpdate();
                    System.out.println("Game saved: " + gameName);
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Loads a game from the database
    
    public SavedGameData loadGame(String gameName) {
        String sql = "SELECT * FROM SavedGames WHERE gameName = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Game loaded: " + gameName);
                return new SavedGameData(
                    rs.getString("gameName"),
                    rs.getString("gridData"),
                    rs.getString("originalGrid"),
                    rs.getString("difficulty"),
                    rs.getString("gameMode"),
                    rs.getLong("playTime"),
                    rs.getTimestamp("saveDate")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    
    //Gets list of all saved game names
    
    public List<String> getSavedGameNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT gameName FROM SavedGames ORDER BY saveDate DESC";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                names.add(rs.getString("gameName"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting saved games: " + e.getMessage());
            e.printStackTrace();
        }
        return names;
    }
    
    
    //Deletes a saved game
    
    public boolean deleteGame(String gameName) {
        String sql = "DELETE FROM SavedGames WHERE gameName = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            boolean result = pstmt.executeUpdate() > 0;
            if (result) {
                System.out.println("Game deleted: " + gameName);
            }
            return result;
        } catch (SQLException e) {
            System.err.println("Error deleting game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Updates game statistics
    
    public boolean updateStatistics(int gamesPlayed, int gamesWon, long playTime, String difficulty) {
        String sql = """
            UPDATE GameStatistics SET 
                gamesPlayed = gamesPlayed + ?,
                gamesWon = gamesWon + ?,
                totalPlayTime = totalPlayTime + ?,
                easyCompleted = easyCompleted + ?,
                mediumCompleted = mediumCompleted + ?,
                hardCompleted = hardCompleted + ?,
                expertCompleted = expertCompleted + ?,
                lastUpdated = CURRENT_TIMESTAMP
            WHERE id = 1
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, gamesPlayed);
            pstmt.setInt(2, gamesWon);
            pstmt.setLong(3, playTime);
            pstmt.setInt(4, difficulty.equals("EASY") ? 1 : 0);
            pstmt.setInt(5, difficulty.equals("MEDIUM") ? 1 : 0);
            pstmt.setInt(6, difficulty.equals("HARD") ? 1 : 0);
            pstmt.setInt(7, difficulty.equals("EXPERT") ? 1 : 0);
            boolean result = pstmt.executeUpdate() > 0;
            if (result) {
                System.out.println("Statistics updated");
            }
            return result;
        } catch (SQLException e) {
            System.err.println("Error updating statistics: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Retrieves game statistics
    
    public StatisticsData getStatistics() {
        String sql = "SELECT * FROM GameStatistics WHERE id = 1";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return new StatisticsData(
                    rs.getInt("gamesPlayed"),
                    rs.getInt("gamesWon"),
                    rs.getLong("totalPlayTime"),
                    rs.getInt("easyCompleted"),
                    rs.getInt("mediumCompleted"),
                    rs.getInt("hardCompleted"),
                    rs.getInt("expertCompleted")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting statistics: " + e.getMessage());
            e.printStackTrace();
        }
        return new StatisticsData(0, 0, 0, 0, 0, 0, 0);
    }
    
    
    //Closes database connection
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    //Shuts down Derby database properly
    
    public void shutdownDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            // XJ015 is expected on successful shutdown
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown successfully");
            } else {
                System.err.println("Error during shutdown: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}