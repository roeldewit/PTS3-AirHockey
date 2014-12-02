package Airhockey.Main;

import Airhockey.Utils.ScoreCalculator;
import Airhockey.Elements.Bat;
import Airhockey.User.Player;
import Airhockey.User.User;
import java.util.ArrayList;
import javafx.application.Application;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class Game {

    private int id;
    private int round = 10;
    private Player owner;
    private Renderer renderer;
    private ArrayList<Player> players;
//    private ArrayList<Spectator> spectators;
    private Result result;
    private Chatbox chatbox;
    private ScoreCalculator scoreCalculator;

    public Game(Renderer renderer) {
        this.renderer = renderer;
        players = new ArrayList<>();

        addPlayer(new User("Henk"));
        addPlayer(new User("Piet"));
        addPlayer(new User("DienMam"));
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

        round--;
        if (round == 0) {
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
