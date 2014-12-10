/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Host;

import Airhockey.Main.Chatbox;
import Airhockey.Main.ChatboxLine;
import Airhockey.Renderer.*;
import Airhockey.Rmi.*;
import Airhockey.User.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author pieper126
 */
public class GameController extends UnicastRemoteObject implements IChatController, IControlPlayer1, IControlPlayer2, IGameController {

    Renderer renderer;
    Chatbox chatbox;
    BatController batcontroller;
    ArrayList<User> users;

    public GameController(Renderer renderer, Chatbox chatbox, ArrayList<User> users) throws RemoteException {
        this.renderer = renderer;
        this.chatbox = chatbox;
        batcontroller = renderer.getBatController();
        this.users = users;
    }

    @Override
    public void movePlayer1BatUp() {
        batcontroller.startLeftBatMovement(BatController.UP);
    }

    @Override
    public void movePlayer1BatDown() {
        batcontroller.startLeftBatMovement(BatController.DOWN);
    }

    @Override
    public void movePlayer2BatUp() {
        batcontroller.startRightBatMovement(BatController.UP);
    }

    @Override
    public void movePlayer2BatDown() {
        batcontroller.startRightBatMovement(BatController.DOWN);
    }

    @Override
    public void stopPlayer1Bat() {
        batcontroller.stopLeftBatMovement();
    }

    @Override
    public void stopPlayer2Bat() {
        batcontroller.stopRightBatMovement();
    }

    @Override
    public void writeLine(String username, String Text) {
        int indexOfUser = users.indexOf(username);

        chatbox.writeLine(new ChatboxLine(Text, users.get(indexOfUser)));
    }

    @Override
    public int getPlayerNumber(String username) {
        return users.indexOf(username);
    }

}
