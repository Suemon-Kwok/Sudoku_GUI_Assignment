/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.ui;

import assignment_2_sudoku_gui.database.DatabaseManager;
import assignment_2_sudoku_gui.database.StatisticsData;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dialog displaying game statistics from database.
 * Shows comprehensive player performance data.
 */
public class StatisticsDialog extends JDialog {
    
    private StatisticsData stats;
    
    /**
     * Constructor initializes the statistics dialog
     */
    public StatisticsDialog(JFrame parent) {
        super(parent, "Game Statistics", true);
        
        loadStatistics();
        setupDialog();
        createComponents();
    }
    
    /**
     * Loads statistics from database
     */
    private void loadStatistics() {
        stats = DatabaseManager.getInstance().getStatistics();
    }
    
    /**
     * Sets up dialog properties
     */
    private void setupDialog() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    /**
     * Creates and layouts dialog components
     */
    private void createComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Game Statistics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Overall statistics
        contentPanel.add(createSectionPanel("Overall Statistics",
            createOverallStatsPanel()));
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Difficulty breakdown
        contentPanel.add(createSectionPanel("Completions by Difficulty",
            createDifficultyStatsPanel()));
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates a section panel with title and content
     */
    private JPanel createSectionPanel(String title, JPanel content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates panel with overall statistics
     */
    private JPanel createOverallStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        panel.add(new JLabel("Games Played:"));
        panel.add(new JLabel(String.valueOf(stats.getGamesPlayed())));
        
        panel.add(new JLabel("Games Won:"));
        panel.add(new JLabel(String.valueOf(stats.getGamesWon())));
        
        panel.add(new JLabel("Win Rate:"));
        panel.add(new JLabel(String.format("%.1f%%", stats.getWinRate())));
        
        panel.add(new JLabel("Average Play Time:"));
        panel.add(new JLabel(formatTime(stats.getAveragePlayTime())));
        
        return panel;
    }
    
    /**
     * Creates panel with difficulty statistics
     */
    private JPanel createDifficultyStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        panel.add(new JLabel("Easy:"));
        panel.add(createProgressPanel(stats.getEasyCompleted()));
        
        panel.add(new JLabel("Medium:"));
        panel.add(createProgressPanel(stats.getMediumCompleted()));
        
        panel.add(new JLabel("Hard:"));
        panel.add(createProgressPanel(stats.getHardCompleted()));
        
        panel.add(new JLabel("Expert:"));
        panel.add(createProgressPanel(stats.getExpertCompleted()));
        
        return panel;
    }
    
    /**
     * Creates a progress panel showing completion count
     */
    private JPanel createProgressPanel(int count) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        
        JLabel countLabel = new JLabel(String.valueOf(count));
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(countLabel, BorderLayout.WEST);
        
        // Simple visual indicator
        JProgressBar bar = new JProgressBar(0, Math.max(count, 10));
        bar.setValue(count);
        bar.setStringPainted(false);
        bar.setPreferredSize(new Dimension(150, 20));
        panel.add(bar, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Formats milliseconds to readable time
     */
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}