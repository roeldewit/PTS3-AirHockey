/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Host;

import java.rmi.RemoteException;
import java.rmi.registry.*;

/**
 *
 * @author pieper126
 */
public class HostServer {

    private static final int portNumber = 1099;
    private static final String bindingName = "GameController";
    private Registry registry = null;
    private GameController gameController = null;

    public HostServer() {
        System.out.println("Server: Port number 1099");
        try {
            System.out.println("Server: Game controller created");
        } catch (Exception ex) {
            System.out.println("Server: Cannot create game controller");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            this.gameController = null;
        }
        try {
            this.registry = LocateRegistry.createRegistry(1099);
            System.out.println("Server: Registry created on port number 1099");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            this.registry = null;
        }
        try {
            this.registry.rebind("GameController", this.gameController);
        } catch (Exception ex) {
            System.out.println("Server: Cannot bind game controller");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }
    }
}
