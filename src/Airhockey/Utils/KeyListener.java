package Airhockey.Utils;

import Airhockey.Renderer.Renderer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class containing the key listener
 *
 * @author Roel
 */
public class KeyListener implements EventHandler<KeyEvent> {

    private final Renderer renderer;
    private boolean keyAlreadyPressed;

    /**
     * Constructor
     *
     * @param renderer Renderer
     */
    public KeyListener(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Handle a key event
     *
     * @param event Key event
     */
    @Override
    public void handle(KeyEvent event) {
        final KeyCode keyCode = event.getCode();

        if (!keyAlreadyPressed) {
            if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                keyAlreadyPressed = true;
                if (keyCode == KeyCode.LEFT) {
                    renderer.startBatMovement(Renderer.LEFT);
                } else if (keyCode == KeyCode.RIGHT) {
                    renderer.startBatMovement(Renderer.RIGHT);
                }
            }
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            if (keyCode == KeyCode.LEFT) {
                renderer.stopBatMovement();
            } else if (keyCode == KeyCode.RIGHT) {
                renderer.stopBatMovement();
            }
            keyAlreadyPressed = false;
        }
    }
}
