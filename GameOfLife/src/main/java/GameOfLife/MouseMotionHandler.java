package GameOfLife;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseMotionHandler extends MouseMotionAdapter implements MousePos {
    private final MouseHandler mouse;

    public MouseMotionHandler(MouseHandler mouse) {
        this.mouse = mouse;
    }

    public void mouseDragged(MouseEvent e) {
        updateMousePos(mouse, e);
    }
    public void mouseMoved(MouseEvent e) {
        mouse.setHasNewCell(false);
    }
}
