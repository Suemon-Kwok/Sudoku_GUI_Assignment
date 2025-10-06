/*
Name: Suemon Kwok

Student ID: 14883335

Software Construction COMP603 / ENSE 600
*/

package assignment_2_sudoku_gui.ui;

import assignment_2_sudoku_gui.controller.GameControllerGUI;
import assignment_2_sudoku_gui.model.puzzle.SudokuPuzzle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
Panel displaying the Sudoku grid with interactive cells.

Handles cell selection and input validation.
Includes coordinate system with row labels (A-I) and column numbers (1-9).
 */
public class GamePanel extends JPanel {
    
    private GameControllerGUI controller;
    private SudokuCell[][] cells;
    private SudokuCell selectedCell;
    
    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 60;
    private static final int LABEL_SIZE = 30;  // Size for coordinate labels
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color GRID_COLOR = new Color(0, 0, 0);
    private static final Color THICK_GRID_COLOR = new Color(0, 0, 0);
    private static final Color SELECTED_COLOR = new Color(187, 222, 251);
    private static final Color FIXED_CELL_COLOR = new Color(230, 230, 230);
    private static final Color LABEL_COLOR = new Color(70, 130, 180);
    
    
    //Constructor initializes the game panel
    
    public GamePanel(GameControllerGUI controller) {
        this.controller = controller;
        this.cells = new SudokuCell[GRID_SIZE][GRID_SIZE];
        
        setupPanel();
        createCells();
        setupKeyBindings();
    }
    
    
    //Sets up panel properties with coordinate labels
    
    private void setupPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(
            CELL_SIZE * GRID_SIZE + LABEL_SIZE, 
            CELL_SIZE * GRID_SIZE + LABEL_SIZE
        ));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(THICK_GRID_COLOR, 3));
    }
    
    
    //Creates all cell components with coordinate system
    
    private void createCells() {
        // Main panel to hold everything
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Top panel for column numbers (1-9)
        JPanel topLabelPanel = createTopLabelPanel();
        mainPanel.add(topLabelPanel, BorderLayout.NORTH);
        
        // Left panel for row letters (A-I)
        JPanel leftLabelPanel = createLeftLabelPanel();
        mainPanel.add(leftLabelPanel, BorderLayout.WEST);
        
        // Center panel for the grid
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 0, 0));
        gridPanel.setBackground(BACKGROUND_COLOR);
        gridPanel.setBorder(BorderFactory.createLineBorder(THICK_GRID_COLOR, 2));
        
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                SudokuCell cell = new SudokuCell(row, col);
                cells[row][col] = cell;
                
                // Add mouse listener for cell selection
                final int r = row;
                final int c = col;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleCellClick(r, c);
                    }
                });
                
                // Add custom border for 3x3 boxes
                cell.setBorder(createCellBorder(row, col));
                
                gridPanel.add(cell);
            }
        }
        
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    
    //Creates top label panel with column numbers (1-9) - ADJUSTED LEFT
    
    private JPanel createTopLabelPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setPreferredSize(new Dimension(
            CELL_SIZE * GRID_SIZE + LABEL_SIZE, 
            LABEL_SIZE
        ));
        outerPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel panel = new JPanel(new GridLayout(1, GRID_SIZE));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Column numbers 1-9
        for (int col = 1; col <= GRID_SIZE; col++) {
            JLabel label = new JLabel(String.valueOf(col), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(LABEL_COLOR);
            label.setPreferredSize(new Dimension(CELL_SIZE, LABEL_SIZE));
            
            // Add separator lines for 3x3 boxes
            if (col == 4 || col == 7) {
                label.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, THICK_GRID_COLOR));
            }
            
            panel.add(label);
        }
        
        // Add left margin to shift numbers left - ADJUST THIS NUMBER (25) TO FINE-TUNE
        outerPanel.add(Box.createHorizontalStrut(27), BorderLayout.WEST);
        outerPanel.add(panel, BorderLayout.CENTER);
        
        return outerPanel;
    }
    
    
    //Creates left label panel with row letters (A-I)
    
    private JPanel createLeftLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(GRID_SIZE, 1));
        panel.setPreferredSize(new Dimension(LABEL_SIZE, CELL_SIZE * GRID_SIZE));
        panel.setBackground(BACKGROUND_COLOR);
        
        // Row letters A-I
        for (int row = 0; row < GRID_SIZE; row++) {
            char rowLetter = (char)('A' + row);
            JLabel label = new JLabel(String.valueOf(rowLetter), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(LABEL_COLOR);
            label.setPreferredSize(new Dimension(LABEL_SIZE, CELL_SIZE));
            
            // Add separator lines for 3x3 boxes
            if (row == 3 || row == 6) {
                label.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, THICK_GRID_COLOR));
            }
            
            panel.add(label);
        }
        
        return panel;
    }
    
    
    //Creates custom border for cell based on position
    
    private javax.swing.border.Border createCellBorder(int row, int col) {
        int top = (row % 3 == 0) ? 2 : 1;
        int left = (col % 3 == 0) ? 2 : 1;
        int bottom = (row == 8) ? 2 : ((row % 3 == 2) ? 2 : 1);
        int right = (col == 8) ? 2 : ((col % 3 == 2) ? 2 : 1);
        
        return BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR);
    }
    
    
    //Sets up keyboard shortcuts
    
    private void setupKeyBindings() {
        // Number input (1-9)
        for (int i = 1; i <= 9; i++) {
            final int num = i;
            String key = "number" + i;
            
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(String.valueOf(i)), key);
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("NUMPAD" + i), key);
            
            getActionMap().put(key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedCell != null) {
                        handleNumberInput(num);
                    }
                }
            });
        }
        
        // Clear cell (Delete, Backspace, 0)
        String[] clearKeys = {"DELETE", "BACK_SPACE", "0", "NUMPAD0"};
        for (String keyStr : clearKeys) {
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(keyStr), "clear");
        }
        getActionMap().put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCell != null) {
                    handleNumberInput(0);
                }
            }
        });
        
        // Arrow key navigation
        setupArrowKeyNavigation();
    }
    
    
    //Sets up arrow key navigation between cells
    
    private void setupArrowKeyNavigation() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "up");
        getActionMap().put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCell != null && selectedCell.row > 0) {
                    handleCellClick(selectedCell.row - 1, selectedCell.col);
                }
            }
        });
        
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "down");
        getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCell != null && selectedCell.row < 8) {
                    handleCellClick(selectedCell.row + 1, selectedCell.col);
                }
            }
        });
        
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "left");
        getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCell != null && selectedCell.col > 0) {
                    handleCellClick(selectedCell.row, selectedCell.col - 1);
                }
            }
        });
        
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "right");
        getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCell != null && selectedCell.col < 8) {
                    handleCellClick(selectedCell.row, selectedCell.col + 1);
                }
            }
        });
    }
    
    
    //Handles cell click for selection
    
    private void handleCellClick(int row, int col) {
        SudokuCell cell = cells[row][col];
        
        // Don't allow selection of fixed cells
        if (cell.isFixed()) {
            return;
        }
        
        // Deselect previous cell
        if (selectedCell != null) {
            selectedCell.setSelected(false);
        }
        
        // Select new cell
        selectedCell = cell;
        selectedCell.setSelected(true);
        requestFocusInWindow();
    }
    
    
    //Handles number input for selected cell
    
    private void handleNumberInput(int num) {
        if (selectedCell == null || selectedCell.isFixed()) {
            return;
        }
        
        int row = selectedCell.row;
        int col = selectedCell.col;
        
        // Let controller handle the move
        controller.makeMove(row, col, num);
    }
    
    
    //Updates grid display from puzzle state
    
    public void updateGrid(SudokuPuzzle puzzle) {
        if (puzzle == null) {
            clearGrid();
            return;
        }
        
        int[][] grid = puzzle.getGrid();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int value = grid[row][col];
                cells[row][col].setValue(value);
                cells[row][col].setFixed(puzzle.isCellOriginal(row, col));
            }
        }
        repaint();
    }
    
    
    //Clears all cells in the grid
    
    public void clearGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setValue(0);
                cells[row][col].setFixed(false);
            }
        }
        selectedCell = null;
        repaint();
    }
    
    
    //Highlights a specific cell (for hints)
    
    public void highlightCell(int row, int col) {
        if (selectedCell != null) {
            selectedCell.setSelected(false);
        }
        selectedCell = cells[row][col];
        selectedCell.setSelected(true);
        
        // Scroll to the cell if needed (visual feedback)
        cells[row][col].requestFocusInWindow();
        
        repaint();
    }
    
    
    //Inner class representing a single Sudoku cell
    
    private class SudokuCell extends JPanel {
        private int row;
        private int col;
        private int value;
        private boolean isFixed;
        private boolean isSelected;
        private JLabel label;
        
        public SudokuCell(int row, int col) {
            this.row = row;
            this.col = col;
            this.value = 0;
            this.isFixed = false;
            this.isSelected = false;
            
            setLayout(new BorderLayout());
            setBackground(BACKGROUND_COLOR);
            
            label = new JLabel("", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 24));
            add(label, BorderLayout.CENTER);
        }
        
        public void setValue(int value) {
            this.value = value;
            label.setText(value == 0 ? "" : String.valueOf(value));
            
            if (isFixed) {
                label.setForeground(Color.BLACK);
            } else {
                label.setForeground(new Color(0, 102, 204));
            }
        }
        
        public void setFixed(boolean fixed) {
            this.isFixed = fixed;
            setBackground(fixed ? FIXED_CELL_COLOR : BACKGROUND_COLOR);
            
            if (fixed && value != 0) {
                label.setForeground(Color.BLACK);
            }
        }
        
        public void setSelected(boolean selected) {
            this.isSelected = selected;
            setBackground(selected ? SELECTED_COLOR : 
                         (isFixed ? FIXED_CELL_COLOR : BACKGROUND_COLOR));
        }
        
        public boolean isFixed() {
            return isFixed;
        }
        
        public int getValue() {
            return value;
        }
    }
}