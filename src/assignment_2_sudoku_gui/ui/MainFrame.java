/*
Name: Suemon Kwok
Student ID: 14883335
Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.ui;

import assignment_2_sudoku_gui.controller.GameControllerGUI;
import assignment_2_sudoku_gui.database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application frame containing all GUI components.
 * Implements MVC pattern with controller coordination.
 */
public class MainFrame extends JFrame {
    
    private GameControllerGUI gameController;
    private GamePanel gamePanel;
    private ControlPanel controlPanel;
    private StatusPanel statusPanel;
    
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 900;
    
    /**
     * Constructor initializes the main frame and all components
     */
    public MainFrame() {
        super("Sudoku Game - GUI Version");
        
        gameController = new GameControllerGUI();
        
        initializeComponents();
        setupLayout();
        setupMenuBar();
        configureFrame();
    }
    
    /**
     * Initializes all GUI components
     */
    private void initializeComponents() {
        gamePanel = new GamePanel(gameController);
        controlPanel = new ControlPanel(gameController, gamePanel);
        statusPanel = new StatusPanel();
        
        // Set status panel reference in controller
        gameController.setStatusPanel(statusPanel);
        gameController.setGamePanel(gamePanel);
    }
    
    /**
     * Sets up the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Add padding around components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Center: Game grid
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        
        // Right: Control buttons
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        // Bottom: Status information
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Sets up the menu bar with File, Game, and Help menus
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newGameItem.addActionListener(e -> controlPanel.handleNewGame());
        
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        saveGameItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveGameItem.addActionListener(e -> controlPanel.handleSaveGame());
        
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        loadGameItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        loadGameItem.addActionListener(e -> controlPanel.handleLoadGame());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitItem.addActionListener(e -> handleExit());
        
        fileMenu.add(newGameItem);
        fileMenu.addSeparator();
        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Game Menu
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');
        
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undoItem.addActionListener(e -> controlPanel.handleUndo());
        
        JMenuItem hintItem = new JMenuItem("Hint");
        hintItem.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
        hintItem.addActionListener(e -> controlPanel.handleHint());
        
        JMenuItem solveItem = new JMenuItem("Auto Solve");
        solveItem.addActionListener(e -> controlPanel.handleSolve());
        
        JMenuItem statsItem = new JMenuItem("Statistics");
        statsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
        statsItem.addActionListener(e -> showStatistics());
        
        gameMenu.add(undoItem);
        gameMenu.add(hintItem);
        gameMenu.addSeparator();
        gameMenu.add(solveItem);
        gameMenu.addSeparator();
        gameMenu.add(statsItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        
        JMenuItem instructionsItem = new JMenuItem("How to Play");
        instructionsItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        instructionsItem.addActionListener(e -> showInstructions());
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(instructionsItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Configures frame properties
     */
    private void configureFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);
        
        // Add window listener for proper cleanup
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });
    }
    
    /**
     * Shows game statistics dialog
     */
    private void showStatistics() {
        StatisticsDialog dialog = new StatisticsDialog(this);
        dialog.setVisible(true);
    }
    
    /**
     * Shows instructions dialog
     */
    private void showInstructions() {
        String instructions = """
            HOW TO PLAY SUDOKU
            
            OBJECTIVE:
            Fill the 9×9 grid so that each row, column, and 3×3 box
            contains all digits from 1 to 9 exactly once.
            
            CONTROLS:
            • Click on a cell to select it
            • Type a number (1-9) to fill the cell
            • Press DELETE or 0 to clear a cell
            • Use buttons or menu shortcuts for game actions
            
            GAME MODES:
            • Classic Mode - Traditional Sudoku without time limit
            • Timed Mode - Race against time to complete the puzzle
            
            DIFFICULTY LEVELS:
            • Easy - 40 clues provided
            • Medium - 30 clues provided
            • Hard - 25 clues provided
            • Expert - 20 clues provided
            
            FEATURES:
            • Hint - Get help finding the next move
            • Undo - Reverse your last move
            • Save/Load - Save progress and continue later
            • Statistics - Track your performance
            
            TIPS:
            • Start with rows, columns, or boxes with the most numbers
            • Look for cells with only one possible number
            • Use the process of elimination
            """;
        
        JTextArea textArea = new JTextArea(instructions);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
            "How to Play Sudoku", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows about dialog
     */
    private void showAbout() {
        String about = """
            Sudoku Game - GUI Version
            
            Created by: Suemon Kwok
            Student ID: 14883335
            Course: COMP603 / ENSE 600
            Software Construction
            
            Version: 2.0
            
            Features:
            • Graphical User Interface
            • Multiple difficulty levels
            • Classic and Timed game modes
            • Derby database integration
            • Save/Load functionality
            • Statistics tracking
            • Hint system
            • Undo functionality
            
            © 2024 All Rights Reserved
            """;
        
        JOptionPane.showMessageDialog(this, about, 
            "About Sudoku Game", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Handles application exit with cleanup
     */
    private void handleExit() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?\nAny unsaved progress will be lost.",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Cleanup database connections
            DatabaseManager.getInstance().closeConnection();
            DatabaseManager.getInstance().shutdownDatabase();
            
            // Exit application
            System.exit(0);
        }
    }
}