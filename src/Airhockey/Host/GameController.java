/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Host;

import Airhockey.Elements.LeftEnemyBat;
import Airhockey.Elements.RightEnemyBat;
import Airhockey.Main.Chatbox;
import Airhockey.Renderer.IRenderer;
import Airhockey.Renderer.Renderer;
import Airhockey.Rmi.IControlPlayer1;
import Airhockey.Rmi.IControlPlayer2;
import Airhockey.Rmi.IControlPlayer3;
import Airhockey.Rmi.IGameController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author pieper126
 */
public class GameController extends UnicastRemoteObject implements IGameController, IControlPlayer1, IControlPlayer2 {

    IRenderer renderer;
    Chatbox chatbox;
    RightEnemyBat rightEnemyBat;
    LeftEnemyBat leftEnemyBat;
    

    public GameController(IRenderer renderer, Chatbox chatbox) throws RemoteException {
        this.renderer = renderer;
        this.chatbox = chatbox;
    }

    public void movePlayer1BatUp() {

    }

    public void movePlayer1BatDown() {
        
    }

    public void movePlayer2BatUp() {
        
    }

    public void movePlayer2BatDown() {
        
    }

    @Override
    public void writeLine(String username, String Text) {
        
    }
}
