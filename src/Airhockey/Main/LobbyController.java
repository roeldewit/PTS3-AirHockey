/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import Airhockey.User.Player;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
    ArrayList<User> users;
    Database database;
    User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLobbyLists();

    }

    public LobbyController() {
        database = new Database();

        chatItems = FXCollections.observableArrayList();
        ratingItems = FXCollections.observableArrayList();
        gameItems = FXCollections.observableArrayList();
    }

    public void startGame() {
        gameItems.add(tfDescription.getText());
        lvOpenGames.setItems(gameItems);

        primaryStage = (Stage) btStartGame.getScene().getWindow();
        primaryStage.close();
        try {
            User user = database.getUser("TestUser5");
            ArrayList<Player> playerList = new ArrayList();
            Player player = new Player(0, user);
            playerList.add(player);
            playerList.add(new Player(1, new User("TestUser6")));
            playerList.add(new Player(2, new User("TestUser7")));
            //Game g = new Game(primaryStage, false, false);
            Game multiGame = new Game(primaryStage, playerList, new ArrayList());
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

    private void setLobbyLists() {
        try {
            users = database.getUsers();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (User user : users) {
            ratingItems.add(user.getUsername() + " : " + user.getRating());
        }
        lvRatingTable.setItems(ratingItems);
    }

}
