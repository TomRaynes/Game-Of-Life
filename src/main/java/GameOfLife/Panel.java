package GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static javax.swing.JOptionPane.showInputDialog;

public class Panel extends JPanel implements Runnable, LifeConstants {

    Thread thread;
    boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];
    KeyHandler keyHandler;
    MouseHandler mouse;
    boolean runSimulation = false;
    private int evolveTime = 0;
    private double speed = 5;
    private boolean occupyCells = true;

    public Panel() {
        keyHandler = new KeyHandler(this);
        mouse = new MouseHandler(this);
        this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        Image cursorImage = Toolkit.getDefaultToolkit().getImage("Assets/cursor.png");
        Point hotspot = new Point(CURSOR_CENTRE, CURSOR_CENTRE);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, hotspot, "crosshair"));
    }

    public void startThread() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            evolveTime++;
            selectCells();

            if (runSimulation && evolveTime * speed >= EVOLVE_TIME_MAX) {
                evolveGrid();
                evolveTime = 0;
            }
            repaint();
        }
    }

    public void toggleRunSimulation() {
        runSimulation = !runSimulation;
    }

    public void resetSimulationState() {
        speed = 10;
        runSimulation = false;
        clearGrid();
    }
    private void evolveGrid() {
        boolean[][] nextGen = new boolean[GRID_SIZE][GRID_SIZE];

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                int neighbours = getNeighbourCount(grid, row, col);

                // Cell is occupied
                if (grid[row][col]) {
                    // living cell persists into next generation if 2 or 3 neighbours are also alive
                    if (neighbours == 2 || neighbours == 3) {
                        nextGen[row][col] = true;
                    }
                }
                // Cell is unoccupied
                else {
                    // dead cell becomes alive if it has 3 living neighbours
                    if (neighbours == 3) {
                        nextGen[row][col] = true;
                    }
                }
            }
        }
        grid = nextGen;
    }
    private int getNeighbourCount(boolean[][] grid, int row, int col) {
        int count = 0;
        int[] directions = {-1, 0, 1};

        for (int deltaRow : directions) {
            for (int deltaCol : directions) {
                if (deltaRow == 0 && deltaCol == 0) {
                    continue; // skip the cell itself
                }
                int newRow = row + deltaRow;
                int newCol = col + deltaCol;

                if (insideGrid(newRow, newCol) && grid[newRow][newCol]) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean insideGrid(int row, int col) {
        return col >= 0 && col < GRID_SIZE && row >= 0 && row < GRID_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(java.awt.Color.black);
        g2.setColor(Color.green);
        // draw border
        g2.drawRect(BORDER_START_POS, BORDER_START_POS, BORDER_END_POS, BORDER_END_POS);
        // draw occupied cells
        fillGrid(g2);
    }
    public void fillGrid(Graphics2D g2) {

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                if (grid[row][col]) {
                    fillCell(g2, row, col);
                }
            }
        }
    }
    public void fillCell(Graphics2D g2, int row, int col) {
        int x = col * CELL_SIZE + EDGE_CELL_POS;
        int y = row * CELL_SIZE + EDGE_CELL_POS;
        g2.setColor(Color.green);
        g2.fillRect(x, y, CELL_OCCUPANT_SIZE, CELL_OCCUPANT_SIZE);
    }
    public void selectCells() {
        if (mouse.hasNewCell() && mouse.insideGrid() && mouse.isHeld()) {
            grid[mouse.getRow()][mouse.getCol()] = occupyCells;
            mouse.setHasNewCell(false);
        }
    }

    public boolean cellIsOccupied(int row, int col) {
        return grid[row][col];
    }

    public void setOccupyCells(boolean occupy) {
        occupyCells = occupy;
    }

    public void randomiseGrid() {

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = Math.random() > 0.5;
            }
        }
    }
    public void clearGrid() {

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = false;
            }
        }
    }
    public void loadPreset() throws FileNotFoundException {
        File fp = getFile();

        if (fp == null) {
            return;
        }
        Scanner reader = new Scanner(fp);

        while (reader.hasNextLine()) {

            for (int row = 0; row < GRID_SIZE; row++) {
                String line = reader.nextLine();

                for (int col = 0; col < GRID_SIZE; col++) {
                    char ch = line.charAt(col);

                    grid[row][col] = ch == 'X';
                }
            }
        }
    }

    public static File getFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Preset");
        fileChooser.setCurrentDirectory(new File("Presets"));
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    public void savePreset() {
        String presetName = getSavedPresetName();

        if (presetName == null) {
            return;
        }
        try (FileWriter writer = new FileWriter("Presets" + File.separator + presetName + ".GoL")) {

            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (grid[row][col]) {
                        writer.write("X");
                    }
                    else writer.write(".");
                }
                writer.write("\n");
            }
        }
        catch (IOException e) {
            System.out.println("Error saving preset");
        }
    }

    public String getSavedPresetName() {
        return showInputDialog(null, "Name:",
                          "Save As Preset", JOptionPane.PLAIN_MESSAGE);
    }
    public void adjustSpeed(boolean increase) {

        if (increase) {
            if (speed < MAX_SPEED) {
                speed *= SPEED_INCREMENT;
            }
        }
        else if (speed > MIN_SPEED) {
            speed /= SPEED_INCREMENT;
        }
    }
}
