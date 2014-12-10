package Airhockey.Host;

import Airhockey.Main.Chatbox;
import Airhockey.Renderer.Renderer;
import Airhockey.User.User;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 *
 * @author Roel
 */
public class RmiServer {

    // Set port number
    private static final int portNumber = 1099;

    // Set binding name for game controller
    private static final String bindingNameGameController = "GameController";

    // Set binding name for HostPublisher
    private static final String bindingNamePublisher = "Publisher";

    // References to registry, game controller and HostPublisher
    private Registry registry = null;
    private GameController gameController = null;
    private HostPublisher hostPublisher = null;
    private BasicPublisher publisher = null;

    public RmiServer(Renderer hostsRenederer, Chatbox chatbox, ArrayList<User> users) {
        // Print port number for registry
        System.out.println("Server: Port number " + portNumber);

        // Create game controller
        try {
            gameController = new GameController(hostsRenederer, chatbox, users);
            System.out.println("Server: Game controller created");
        } catch (Exception ex) {
            System.out.println("Server: Cannot create game controller");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            gameController = null;
        }

        // Create HostPublisher
        try {
            publisher = new BasicPublisher(new String[]{"gameData", "chatboxLines"});
            hostPublisher = new HostPublisher(publisher);
            System.out.println("Server: Game controller created");
        } catch (Exception ex) {
            System.out.println("Server: Cannot create game controller");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }

        // Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Bind game controller using registry
        try {
            registry.rebind(bindingNameGameController, gameController);
        } catch (Exception ex) {
            System.out.println("Server: Cannot bind game controller");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }

        // Bind HostPublisher using registry
        try {
            registry.rebind(bindingNamePublisher, publisher);
        } catch (Exception ex) {
            System.out.println("Server: Cannot bind game controller");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }
    }
    
    public HostPublisher getPublisher(){
        return hostPublisher;
    }
}