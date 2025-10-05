/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui;

import assignment_2_sudoku_gui.ui.MainFrame;
import assignment_2_sudoku_gui.database.DatabaseManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for the Sudoku GUI application.
 * Initializes database and launches the GUI on the Event Dispatch Thread.
 */
public class Assignment_2_Sudoku_GUI {
    
    public static void main(String[] args) {
        // Initialize database on startup
        try {
            DatabaseManager.getInstance().initializeDatabase();
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fall back to default look and feel
                System.err.println("Could not set system look and feel");
            }
            
            // Create and display main frame
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}