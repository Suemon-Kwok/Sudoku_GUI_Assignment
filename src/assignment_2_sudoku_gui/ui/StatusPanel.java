/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Status panel displaying game information and timer.
 * Shows current game state and elapsed time.
 */
public class StatusPanel extends JPanel {
    
    private JLabel statusLabel;
    private JLabel timerLabel;
    private JLabel modeLabel;
    private JLabel difficultyLabel;
    
    private Timer displayTimer;
    private long gameStartTime;
    private boolean isTimerRunning;
    
    /**
     * Constructor initializes the status panel
     */
    public StatusPanel() {
        setupPanel();
        createComponents();
        setupTimer();
    }
    
    /**
     * Sets up panel properties
     */
    private void setupPanel() {
        setLayout(new GridLayout(2, 2, 10, 5));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Game Status"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setPreferredSize(new Dimension(0, 80));
    }
    
    /**
     * Creates status display components
     */
    private void createComponents() {
        statusLabel = new JLabel("Ready to play", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(new Color(0, 128, 0));
        
        timerLabel = new JLabel("Time: 00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        modeLabel = new JLabel("Mode: -", SwingConstants.CENTER);
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        difficultyLabel = new JLabel("Difficulty: -", SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        add(statusLabel);
        add(timerLabel);
        add(modeLabel);
        add(difficultyLabel);
    }
    
    /**
     * Sets up the display timer
     */
    private void setupTimer() {
        displayTimer = new Timer(1000, e -> updateTimerDisplay());
        isTimerRunning = false;
    }
    
    /**
     * Updates timer display
     */
    private void updateTimerDisplay() {
        if (isTimerRunning) {
            long elapsed = System.currentTimeMillis() - gameStartTime;
            timerLabel.setText("Time: " + formatTime(elapsed));
        }
    }
    
    /**
     * Formats milliseconds to MM:SS format
     */
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    /**
     * Starts the timer
     */
    public void startTimer() {
        gameStartTime = System.currentTimeMillis();
        isTimerRunning = true;
        displayTimer.start();
    }
    
    /**
     * Stops the timer
     */
    public void stopTimer() {
        isTimerRunning = false;
        displayTimer.stop();
    }
    
    /**
     * Resets the timer
     */
    public void resetTimer() {
        stopTimer();
        timerLabel.setText("Time: 00:00");
    }
    
    /**
     * Gets elapsed time in milliseconds
     */
    public long getElapsedTime() {
        if (isTimerRunning) {
            return System.currentTimeMillis() - gameStartTime;
        }
        return 0;
    }
    
    /**
     * Updates status message
     */
    public void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
    /**
     * Updates game mode display
     */
    public void setMode(String mode) {
        modeLabel.setText("Mode: " + mode);
    }
    
    /**
     * Updates difficulty display
     */
    public void setDifficulty(String difficulty) {
        difficultyLabel.setText("Difficulty: " + difficulty);
    }
    
    /**
     * Resets all status information
     */
    public void reset() {
        resetTimer();
        setStatus("Ready to play", new Color(0, 128, 0));
        setMode("-");
        setDifficulty("-");
    }
}