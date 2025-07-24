package GameOfLife;

public interface LifeConstants {
    int GRID_SIZE = 65;
    int BORDER_START_POS = 15;
    int CELL_SIZE = 11;
    int EDGE_CELL_POS = BORDER_START_POS + 2;
    int BORDER_END_POS = EDGE_CELL_POS + CELL_SIZE * GRID_SIZE - BORDER_START_POS + 1;
    int SCREEN_SIZE = CELL_SIZE * GRID_SIZE + 2*EDGE_CELL_POS;
    int CELL_OCCUPANT_SIZE = 10;
    int EVOLVE_TIME_MAX = 2000000;
    int MAX_SPEED = 200;
    int MIN_SPEED = 1;
    double SPEED_INCREMENT = 1.2;
    int CURSOR_CENTRE = 8;
}
