package GameOfLife;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter implements MousePos {

    private final Panel panel;
    private boolean isHeld = false;
    private boolean hasNewCell;
    private int row, col = -1;

    public MouseHandler(Panel panel) {
        MouseMotionHandler motion = new MouseMotionHandler(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(motion);
        this.panel = panel;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isHeld() {
        return isHeld;
    }

    public boolean insideGrid() {
        return panel.insideGrid(row, col);
    }

    public boolean hasNewCell() {
        return hasNewCell;
    }

    public void setHasNewCell(boolean hasNewCell) {
        this.hasNewCell = hasNewCell;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        updateMousePos(this, e);
        panel.setOccupyCells(!panel.cellIsOccupied(row, col));
        hasNewCell = true;
        isHeld = true;
    }

    public void mouseReleased(MouseEvent e) {
        isHeld = false;
    }
}
