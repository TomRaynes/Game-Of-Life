package GameOfLife;

import java.awt.event.MouseEvent;

import static GameOfLife.LifeConstants.*;

public interface MousePos {
    default void updateMousePos(MouseHandler mouse, MouseEvent e) {
        int newCol = (e.getX() - EDGE_CELL_POS - 1) / CELL_SIZE;
        int newRow = (e.getY() - EDGE_CELL_POS - 1) / CELL_SIZE;

        if (newRow != mouse.getRow() || newCol != mouse.getCol()) {
            mouse.setRow(newRow);
            mouse.setCol(newCol);
            mouse.setHasNewCell(true);
        }
    }
}
