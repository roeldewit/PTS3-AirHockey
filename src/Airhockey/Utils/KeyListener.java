package Airhockey.Utils;

import Airhockey.Main.Renderer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Roel
 */
public class KeyListener implements EventHandler<KeyEvent> {

    private final Renderer renderer;
    private boolean keyAlreadyPressed;

    public KeyListener(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(KeyEvent event) {
        final KeyCode keyCode = event.getCode();

        if (!keyAlreadyPressed) {
            if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                keyAlreadyPressed = true;
                if (keyCode == KeyCode.LEFT) {
                    renderer.startBatMovement(Renderer.LEFT);
                    System.out.println("LEEFFFTTT");
                } else if (keyCode == KeyCode.RIGHT) {
                    renderer.startBatMovement(Renderer.RIGHT);
                }
            }
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            System.out.println("KEYRELEASED");
            if (keyCode == KeyCode.LEFT) {
                renderer.stopBatMovement();
            } else if (keyCode == KeyCode.RIGHT) {
                renderer.stopBatMovement();
            }
            keyAlreadyPressed = false;
        }
    }
}
