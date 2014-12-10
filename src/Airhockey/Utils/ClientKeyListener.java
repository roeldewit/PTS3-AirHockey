package Airhockey.Utils;

import Airhockey.Renderer.ClientRenderer;
import Airhockey.Utils.*;
import Airhockey.Renderer.Renderer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ClientKeyListener implements EventHandler<KeyEvent> {

    private final ClientRenderer renderer;
    //private GameController gameController;
    private boolean keyAlreadyPressed;

    public ClientKeyListener(ClientRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void handle(KeyEvent event) {
        final KeyCode keyCode = event.getCode();

        if (!keyAlreadyPressed) {
            if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                keyAlreadyPressed = true;
                if (keyCode == KeyCode.LEFT) {
                    //renderer.startBatMovement(Renderer.LEFT);
                } else if (keyCode == KeyCode.RIGHT) {
                    //renderer.startBatMovement(Renderer.RIGHT);
                    renderer.startTimer();
                }
            }
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            System.out.println("sfasdf");
            if (keyCode == KeyCode.LEFT) {
                //renderer.stopBatMovement();
            } else if (keyCode == KeyCode.RIGHT) {
                //renderer.stopBatMovement();
            }
            keyAlreadyPressed = false;
        }
    }
}
