package Airhockey.Utils;

import Airhockey.Client.ClientController;
import Airhockey.Renderer.ClientRenderer;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ClientKeyListener implements EventHandler<KeyEvent> {

    private final ClientRenderer renderer;
    //private GameController gameController;
    private boolean keyAlreadyPressed;
    private ClientController clientcontroller;

    public ClientKeyListener(ClientRenderer renderer, ClientController clientController) {
        this.renderer = renderer;
        this.clientcontroller = clientController;
    }

    @Override
    public void handle(KeyEvent event) {
        final KeyCode keyCode = event.getCode();

        if (!keyAlreadyPressed) {
            if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                keyAlreadyPressed = true;
                if (keyCode == KeyCode.LEFT) {
                    try {
                        clientcontroller.moveLeft();
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientKeyListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (keyCode == KeyCode.RIGHT) {
                    try {
                        clientcontroller.moveRight();
//                    renderer.startTimer();
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientKeyListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            System.out.println("sfasdf");
            if (keyCode == KeyCode.LEFT) {
                try {
                    clientcontroller.stopMovement();
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientKeyListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (keyCode == KeyCode.RIGHT) {
                try {
                    clientcontroller.stopMovement();
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientKeyListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            keyAlreadyPressed = false;
        }
    }
}
