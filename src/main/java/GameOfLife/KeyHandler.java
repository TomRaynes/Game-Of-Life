package GameOfLife;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyHandler implements KeyListener {
    private final Panel panel;

    public KeyHandler(Panel panel) {
        panel.addKeyListener(this);
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent event) {

        try {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_SPACE -> panel.toggleRunSimulation();
                case KeyEvent.VK_R -> panel.randomiseGrid();
                case KeyEvent.VK_C -> panel.resetSimulationState();
                case KeyEvent.VK_L -> panel.loadPreset();
                case KeyEvent.VK_UP -> panel.adjustSpeed(true);
                case KeyEvent.VK_DOWN -> panel.adjustSpeed(false);
                case KeyEvent.VK_S -> panel.savePreset();
            }
        }
        catch (IOException e) {
            System.err.println("ERROR: Unable to load preset");
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
