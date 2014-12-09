package Airhockey.Main;

import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class Lobby {

    private ArrayList<User> users;
    private Chatbox chatbox;
    private ScoreCalculator scoreCalculator;

    public Lobby(Stage primaryStage) {
        chatbox = new Chatbox();
        LobbySetUp(primaryStage);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public Chatbox getChatbox() {
        return chatbox;
    }

    public ScoreCalculator getScoreCalculator() {
        return scoreCalculator;
    }

    private void LobbySetUp(Stage primaryStage) {
 
        Parent root = null;

        try {
            root = FXMLLoader.load(Lobby.class.getResource("LobbyLayout.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
