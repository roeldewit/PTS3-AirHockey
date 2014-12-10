package Airhockey.Main;

import Airhockey.Client.ClientController;
import Airhockey.Client.ClientListener;
import Airhockey.Renderer.*;
import Airhockey.Elements.Bat;
import Airhockey.Host.RmiServer;
import Airhockey.Rmi.Goal;
import Airhockey.User.*;
import Airhockey.Utils.ScoreCalculator;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javafx.stage.Stage;

/**
 *
 * @author Roel
 */
public class Game {

    private int id;
    private int round = 1;
    private Player owner;
    private IRenderer renderer;
    private ArrayList<Player> players;
    private ArrayList<User> users;
    private Player currentPlayer;

//    private ArrayList<Spectator> spectators;
    private Chatbox chatbox;
    private ScoreCalculator scoreCalculator;
    private boolean isHost;

    // data of the host
    private String ipHost;
    private int port;
    private ClientListener clientListener;
    private ClientController clientController;
    private RmiServer rmiServer;

    public Game(Stage primaryStage, boolean isHost) {
        this.isHost = isHost;
        players = new ArrayList<>();

        addPlayer(new User("Henk"));
        addPlayer(new User("Piet"));
        addPlayer(new User("DienMam"));

        renderer = new Renderer(primaryStage, this, false);

    }

    /**
     * Constructor used to start a game as a Host
     *
     * @param primaryStage
     * @param players
     * @param users
     * @throws RemoteException
     */
    public Game(Stage primaryStage, ArrayList<Player> players, ArrayList<User> users) throws RemoteException {
        this.players = players;
        this.owner = players.get(0);
        this.chatbox = new Chatbox();
        this.users = users;
        this.currentPlayer = players.get(0);

        this.renderer = new Renderer(primaryStage, this, true);
        this.rmiServer = new RmiServer((Renderer) renderer, chatbox, users);
    }

    /**
     * Constructor used to start a game as a client
     *
     * @param primaryStage
     * @param ipHost
     * @param port
     * @param players
     * @param currentPlayer
     * @throws RemoteException
     */
    public Game(Stage primaryStage, String ipHost, int port, ArrayList<Player> players, Player currentPlayer) throws RemoteException {
        this.ipHost = ipHost;
        this.port = port;
        this.players = players;
        this.owner = players.get(0);
        this.currentPlayer = currentPlayer;

        joinGame(ipHost, port, currentPlayer.user.getUsername());
        this.renderer = new ClientRenderer(primaryStage, this);
    }

    public void addPlayer(User user) {
        Player player = new Player(players.size() + 1, user);
        players.add(player);
    }

    public void startGame(Player owner) {
        throw new UnsupportedOperationException();
    }

    public void leaveGame(User user) {
        /*zodra een speler het speelveld van een spel, waarvoor inmiddels ten minste 1 ronde is
         voltooid, vroegtijdig verlaat, bijvoorbeeld vanwege een slechte internetverbinding1
         ,
         wordt het spel als beëindigd verklaard. De eindscore S wordt dan als volgt
         gecorrigeerd:
         S := (S-20)*10/n + 20
         waarbij n het aantal gespeelde ronden voorstelt (1≤n≤10).
         Als de ratingscore voor de weggevallen speler beter is dan zijn huidige rating, wordt
         de ratingscore genegeerd. Als de ratingscore van een nog aanwezige speler slechter is
         dan zijn/haar huidige rating, wordt deze ratingscore ook niet verwerkt*/
    }

    public void addBatToPlayer(int playerId, Bat bat) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                player.setBat(bat);
            }
        }
    }

    public void setGoal(Bat batWhoMadeGoal, Bat batWhoFailed) {
        if (owner.equals(currentPlayer)) {

            int i = 0;
            int idPersonWhoMadeGoal = -1;
            int idPersonWhoFailed = -1;
            for (Player player : players) {
                if (batWhoMadeGoal == null) {
                    if (player.getBat() == batWhoFailed) {
                        player.downScore();
                        renderer.setTextFields("PLAYER" + player.getId() + "_SCORE", player.getScore() + "");
                    }
                } else {
                    if (player.getBat() == batWhoMadeGoal) {
                        player.upScore();
                        renderer.setTextFields("PLAYER" + player.getId() + "_SCORE", player.getScore() + "");
                        idPersonWhoMadeGoal = i;
                    } else if (player.getBat() == batWhoFailed) {
                        player.downScore();
                        renderer.setTextFields("PLAYER" + player.getId() + "_SCORE", player.getScore() + "");
                        idPersonWhoFailed = i;
                    }
                }
                i++;
            }

            round++;

            try {
                rmiServer.getPublisher().informListeners(new Goal(idPersonWhoMadeGoal, idPersonWhoFailed, round));
            } catch (RemoteException e) {
                stop();
            }
        } else {
            for (Player player : players) {
                if (batWhoMadeGoal == null) {
                    if (player.getBat() == batWhoFailed) {
                        player.downScore();
                        renderer.setTextFields("PLAYER" + player.getId() + "_SCORE", player.getScore() + "");
                    }
                } else {
                    if (player.getBat() == batWhoMadeGoal) {
                        player.upScore();
                        renderer.setTextFields("PLAYER" + player.getId() + "_SCORE", player.getScore() + "");
                    } else if (player.getBat() == batWhoFailed) {
                        player.downScore();
                        renderer.setTextFields("PLAYER" + player.getId() + "_SCORE", player.getScore() + "");
                    }
                }
            }

            round++;
        }

        if (round == 10) {
            stop();
        } else {
            renderer.resetRound(round);
        }

    }

    public String getUsername(int id) {
        return players.get(id - 1).user.getUsername();
    }

    public RmiServer getRmiServer() {
        return rmiServer;
    }

    public ClientController getClientController() {
        return clientController;
    }

    private void stop() {
        //renderer.resetRound();
    }

    private void joinGame(String iphost, int port, String username) throws RemoteException {
        this.clientListener = new ClientListener((ClientRenderer) renderer);
        this.clientListener.connectToHost(iphost, port);
        this.clientController = new ClientController(ipHost, port, username);
    }
}
