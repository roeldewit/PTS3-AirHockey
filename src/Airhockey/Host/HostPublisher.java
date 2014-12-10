/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Host;

import Airhockey.Rmi.GameData;
import Airhockey.Rmi.Goal;
import Airhockey.Rmi.Location;
import Airhockey.Rmi.SerializableChatBoxLine;
import java.rmi.RemoteException;
import org.jbox2d.dynamics.Body;
import Airhockey.Utils.Utils;

/**
 *
 * @author stijn
 */
public class HostPublisher {

    private BasicPublisher publisher;

    // used to send over GameData
    private GameData[] gameData;
    private GameData[] oldGameData;

    // used to send over chatboxlines
    private SerializableChatBoxLine[] chatboxlines;
    private SerializableChatBoxLine[] oldChatboxlines;

    private int consectivePackagesLoss;

    public HostPublisher(BasicPublisher publisher) throws RemoteException {
        this.publisher = publisher;
        consectivePackagesLoss = 0;

        gameData = new Location[5];
        oldGameData = new Location[5];
    }

    public void informListeners(float puckX, float puckY, float batX, float batY, Body leftEnemyBat, Body rightEnemyBat, Goal goal) throws RemoteException {
        oldGameData = gameData.clone();

        gameData[0] = new Location("puck", puckX, puckY);
        gameData[1] = new Location("bat1", batX, batY);

        float xpos = Utils.toPixelPosX(leftEnemyBat.getPosition().x);
        float ypos = Utils.toPixelPosY(leftEnemyBat.getPosition().y);

        gameData[2] = new Location("leftEnemyBat", xpos, ypos);

        xpos = Utils.toPixelPosX(rightEnemyBat.getPosition().x);
        ypos = Utils.toPixelPosY(rightEnemyBat.getPosition().y);
        gameData[3] = new Location("RightEnemyBat", xpos, ypos);

        gameData[4] = goal;

        try {
            publisher.inform(this, "gameData", oldGameData, gameData);
            consectivePackagesLoss = 0;
        } catch (RemoteException e) {
            consectivePackagesLoss++;
            if (consectivePackagesLoss == 12000) {
                throw new RemoteException();
            }
        }
    }

    public void informListeners(SerializableChatBoxLine[] chatboxlines) throws RemoteException {
        oldChatboxlines =  chatboxlines.clone();

        try {
            publisher.inform(this, "chatboxLines", oldChatboxlines, chatboxlines);
            consectivePackagesLoss = 0;
        } catch (RemoteException e) {
            consectivePackagesLoss++;
            if (consectivePackagesLoss == 12000) {
                throw new RemoteException();
            }
        }
    }

}
