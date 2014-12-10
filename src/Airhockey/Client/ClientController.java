/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Client;

import Airhockey.Rmi.IChatController;
import Airhockey.Rmi.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author pieper126
 */
public class ClientController {

    private final String ipHost;
    private final int portHost;
    private Registry register;

    private final String username;

    private IGameController gamecontroller;
    private IChatController chatcontroller;
    private IControlPlayer1 controlPlayer1;
    private IControlPlayer2 controlPlayer2;

    private int playerNumber;

    public ClientController(String ipHost, int portHost, String username) {
        this.ipHost = ipHost;
        this.portHost = portHost;
        this.username = username;

        connectToHost();
    }

    private void connectToHost() {

        this.gamecontroller = null;

        try {
            register = LocateRegistry.getRegistry(ipHost, portHost);
        } catch (RemoteException ex) {
            System.err.println("Unable to find registry");
            System.exit(-1);
        }

        try {
            this.gamecontroller = ((IGameController) register.lookup("GameController"));
            playerNumber = this.gamecontroller.getPlayerNumber(username);
        } catch (RemoteException | NotBoundException exc) {
            System.err.println("Unable to find gamecontroller");
            System.exit(-1);
        }

        this.chatcontroller = (IChatController) gamecontroller;

        switch (playerNumber) {
            case 1:
                this.controlPlayer1 = (IControlPlayer1) gamecontroller;
                break;
            case 2:
                this.controlPlayer2 = (IControlPlayer2) gamecontroller;
                break;
        }
    }

    public void moveLeft() {
        if (playerNumber == 1) {
            controlPlayer1.movePlayer1BatUp();
        } else {
            controlPlayer2.movePlayer2BatUp();
        }
    }

    public void moveRight() {
        if (playerNumber == 1) {
            controlPlayer1.movePlayer1BatDown();
        } else {
            controlPlayer2.movePlayer2BatUp();
        }
    }

    public void stopMovement() {
        if (playerNumber == 1) {
            controlPlayer1.stopPlayer1Bat();
        } else {
            controlPlayer2.stopPlayer2Bat();
        }
    }
    
    public int getPlayerNumber(){
        return playerNumber; 
    }

}
