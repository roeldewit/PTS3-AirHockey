package Airhockey.Main;

import Airhockey.Rmi.*;
import Airhockey.User.Player;
import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    IMainLobby mainLobby;
    private Registry register;
    private String ipMainServer;
    private int portMainServer;
    Database database;

    HashMap<String, User> hashMapUsernameToUser;

    Stage primaryStage;

    public Lobby(Stage primaryStage) {
        LobbySetUp(primaryStage);
        this.primaryStage = primaryStage;
        database = new Database();
        hashMapUsernameToUser = new HashMap();
        
        try {
            users = database.getUsers();

            for (User dbuser : users) {
                hashMapUsernameToUser.put(dbuser.getUsername(), dbuser);
            }

            connectToMainServer();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        getInitialChatbox();
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

    private void connectToMainServer() {

        this.mainLobby = null;

        try {
            register = LocateRegistry.getRegistry(ipMainServer, portMainServer);
        } catch (RemoteException ex) {
            System.err.println("Unable to find registry");
            System.exit(-1);
        }

        try {
            this.mainLobby = ((IMainLobby) register.lookup("GameController"));
        } catch (RemoteException | NotBoundException exc) {
            System.err.println("Unable to find gamecontroller");
            System.exit(-1);
        }
    }

    private void getInitialChatbox() {
        SerializableChatBox serializableChatBox = mainLobby.getChatBox();
        chatbox = new Chatbox();

        for (SerializableChatBoxLine serializableChatBoxLine : serializableChatBox.lines) {
            chatbox.writeLine(new ChatboxLine(serializableChatBoxLine.text, hashMapUsernameToUser.get(serializableChatBoxLine.player)));
        }
    }

    private Game joinGame(int id , String usern) throws RemoteException {
        SerializableGame serializableGame = mainLobby.getWaitingGames().get(id);
        mainLobby.joinGame(id, usern);
        ArrayList<Player> players = new ArrayList<>();

        int i = 0;
        for (String username : serializableGame.usernames) {
            User user = hashMapUsernameToUser.get(username);

            if (i < 3) {
                players.add(new Player(i, user));
            }
        }

        // to do invullen  eigen speler
        return new Game(primaryStage, serializableGame.hostIP, 1099, players, null);
    }

    private ArrayList<SerializableGame> getRunningGames() {
        return mainLobby.getBusyGames();
    }

    private ArrayList<SerializableGame> getWaitingGames() {
        return mainLobby.getWaitingGames();
    }

    private void startGame(SerializableGame serializableGame){
        mainLobby.startGame(serializableGame);
    }
    
    /*
     public LobbyController(String ipMainServer, int portMainServer) {
     db = new Database();

     this.ipMainServer = ipMainServer;
     this.portMainServer = portMainServer;

     connectToMainServer();

     try {
     for (User dbuser : db.getUsers()) {
     hashMapUsernameToUser.put(dbuser.getUsername(), dbuser);
     }
     } catch (Exception e) {
     throw new RuntimeException(e.getMessage());
     }

     SerializableChatBox serChatBox = mainLobby.getChatBox();

     chatItems = FXCollections.observableArrayList();
     ratingItems = FXCollections.observableArrayList("TestUser1 : 21", "TestUser2 : 19");
     gameItems = FXCollections.observableArrayList();
     lvRatingTable = new ListView();
     lvRatingTable.setItems(ratingItems);
     }
     */
}
