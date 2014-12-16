/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.Rmi.*;
import Airhockey.Rmi.SerializableChatBox;
import Airhockey.User.Player;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 *
 * @author martijn
 */
public class LobbyController implements Initializable {

    @FXML
    Button btStartGame;

    @FXML
    TextField tfChatbox;

    @FXML
    ListView lvChatbox;

    @FXML
    ListView lvRatingTable;

    @FXML
    TextField tfDescription;

    @FXML
    ListView lvOpenGames;

    Stage primaryStage;
    ObservableList<String> chatItems;
    ObservableList<String> ratingItems;
    ObservableList<String> gameItems;
    Database db;
    User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public LobbyController() {
        db = new Database();

        chatItems = FXCollections.observableArrayList();
        ratingItems = FXCollections.observableArrayList("TestUser1 : 21", "TestUser2 : 19");
        gameItems = FXCollections.observableArrayList();
        lvRatingTable = new ListView();
        lvRatingTable.setItems(ratingItems);
    }

    public void startGame() {
        gameItems.add(tfDescription.getText());
        lvOpenGames.setItems(gameItems);

        primaryStage = (Stage) btStartGame.getScene().getWindow();
        primaryStage.close();
        try {
            ArrayList<Player> playerList = new ArrayList();
            Player player = new Player(0, db.getUser("TestUser5"));
            playerList.add(player);
            //Game g = new Game(primaryStage, false, false);
            Game multiGame = new Game(primaryStage, playerList, db.getUsers());
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chatboxSend() {
        if (tfChatbox.getText() != "") {
            chatItems.add(tfChatbox.getText());
            lvChatbox.setItems(chatItems);
        }
    }
}
