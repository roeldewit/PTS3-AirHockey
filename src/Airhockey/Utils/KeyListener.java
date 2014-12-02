package Airhockey.Utils;

import Airhockey.Main.Renderer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class KeyListener implements EventHandler<KeyEvent> {

    private Renderer renderer;
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
                    //renderer.createBatMovementTimer(Renderer.LEFT);
                    System.out.println("LEEFFFTTT");
                } else if (keyCode == KeyCode.RIGHT) {
                    renderer.startBatMovement(Renderer.RIGHT);
                    //renderer.createBatMovementTimer(Renderer.RIGHT);
                }
            }
        }
        if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            System.out.println("KEYRELEASED");
            if (keyCode == KeyCode.LEFT) {
                renderer.stopBatMovement();
                //renderer.cancelBatMovementTimer();
            } else if (keyCode == KeyCode.RIGHT) {
                renderer.stopBatMovement();
                //renderer.cancelBatMovementTimer();
            }
            keyAlreadyPressed = false;
        }

    }

//    private boolean left;
//    private boolean right;
//    private boolean ballCanStart;
//    private final Renderer renderer;
//
//    private static final int LEFT = 0;
//    private static final int RIGHT = 1;
//
//    public KeyListener(Renderer renderer) {
//        this.renderer = renderer;
//        left = true;
//        right = true;
//        ballCanStart = true;
//    }
//
//    @Override
//    public void keyTyped(java.awt.event.KeyListener e) {
//        //Not Implemented
//    }
//
//    @Override
//    public void keyPressed(java.awt.event.KeyListener e) {
////        if (e.getKeyCode() == VK_LEFT) {
////            if (left) {
////                renderer.createBatMovementTimer(LEFT);
////                setFalse();
////            }
////        } else if (e.getKeyCode() == VK_RIGHT) {
////            if (right) {
////                renderer.createBatMovementTimer(RIGHT);
////                setFalse();
////            }
////        }
//
////        if (e.getKeyCode() == VK_SPACE) {
////            if (ballCanStart == true) {
////                renderer.startBall();
////                ballCanStart = false;
////            }
////        }
//    }
//
//    @Override
//    public void keyReleased(java.awt.event.KeyListener e) {
////        if (e.getKeyCode() == VK_LEFT) {
////            setTrue();
////            renderer.cancelBatMovementTimer();
////        } else if (e.getKeyCode() == VK_RIGHT) {
////            setTrue();
////            renderer.cancelBatMovementTimer();
////        }
//    }
//
//    public void setTrue() {
//        left = true;
//        right = true;
//    }
//
//    public void setFalse() {
//        left = false;
//        right = false;
//    }
//
//    public void setBallCanStart() {
//        ballCanStart = true;
//    }
//
//    @Override
//    public void handle(KeyListener event) {
//        if (event.getc) {
//            
//        }
//    }
}
