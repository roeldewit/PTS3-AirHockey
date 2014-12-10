package Airhockey.Main;

import Airhockey.Renderer.Renderer;
import Airhockey.Renderer.IRenderer;
import Airhockey.Renderer.ClientRenderer;
import Airhockey.Elements.Bat;
import Airhockey.User.Player;
import Airhockey.User.User;
import Airhockey.Utils.ScoreCalculator;
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
//    private ArrayList<Spectator> spectators;
    private Chatbox chatbox;
    private ScoreCalculator scoreCalculator;
    private boolean isHost;

    public Game(Stage primaryStage, boolean isHost) {
        this.isHost = isHost;
        players = new ArrayList<>();

        addPlayer(new User("Henk"));
        addPlayer(new User("Piet"));
        addPlayer(new User("DienMam"));

        if (isHost) {
            renderer = new Renderer(primaryStage, this);
        } else {
            renderer = new ClientRenderer(primaryStage, this);
        }
        //scoreCalculator = new ScoreCalculator(player1ScoreLabel, player2ScoreLabel, player3ScoreLabel);
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
        if (round == 10) {
            stop();
        } else {
            renderer.resetRound(round);
        }
    }

    public String getUsername(int id) {
        return players.get(id - 1).user.getUsername();
    }

    private void stop() {
        //renderer.resetRound();
    }
}
