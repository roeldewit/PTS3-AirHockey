/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Client;

/**
 *
 * @author pieper126
 */
<<<<<<< HEAD
import Airhockey.Renderer.IRenderer;
import Airhockey.Rmi.*;
=======
import Airhockey.Renderer.Renderer;
import Airhockey.Rmi.Location;
import Airhockey.Rmi.RemotePropertyListener;
import Airhockey.Rmi.RemotePublisher;
>>>>>>> 8094a0231503a23a0461c06dcbf95c94b7e904bf
import java.beans.PropertyChangeEvent;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class ClientListener extends UnicastRemoteObject implements RemotePropertyListener {

    private RemotePublisher publisher;
    private final IRenderer renderer;

    public ClientListener(IRenderer renderer) throws RemoteException {
        this.renderer = renderer;
    }

    public void connectToHost(String ipHost, int port) throws RemoteException {
        Registry register = null;
        this.publisher = null;
        try {
            register = LocateRegistry.getRegistry(ipHost, port);
        } catch (RemoteException ex) {
            System.err.println("Unable to find registry");
            System.exit(-1);
        }
        try {
            this.publisher = ((RemotePublisher) register.lookup("Publisher"));
            this.publisher.addListener(this, "locations");
        } catch (RemoteException | NotBoundException exc) {
            System.err.println("Unable to find effectenbeurs: " + exc.getMessage());
            System.exit(-1);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        Location[] locations = (Location[]) evt.getNewValue();
<<<<<<< HEAD
=======

//        this.renderer.setPuckLocation(locations[0]);
//        this.renderer.setBatLocation(locations[1]);
//        this.renderer.setLeftEnemyBat(locations[2]);
//        this.renderer.setRightEnemyBat(locations[3]);
>>>>>>> 8094a0231503a23a0461c06dcbf95c94b7e904bf
    }
}
