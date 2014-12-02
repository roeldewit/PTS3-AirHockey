package Airhockey.Main;

import Airhockey.Utils.ScoreCalculator;
import Airhockey.User.User;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 5, 5, 50));

        //Adding GridPaneDesc
        GridPane gridPaneDesc = new GridPane();
        gridPaneDesc.setPadding(new Insets(5, 50, 50, 0));

        //Implementing Nodes for GridPaneDesc
        Label lblDesc = new Label();
        lblDesc.setText("Description: ");
        final TextField txtDesc = new TextField();
        Button btnStartGame = new Button("Start new Game!");
        
        //Action for btnLogin
        btnStartGame.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {

                primaryStage.close();
                Renderer r = new Renderer(primaryStage);
            }

        });

        //Adding Nodes to GridPane layout
        gridPaneDesc.add(lblDesc, 0, 0);
        gridPaneDesc.add(txtDesc, 1, 0);
        gridPaneDesc.add(btnStartGame, 1, 1);

        //Adding GrindPaneGameList
        GridPane gridPaneGameList = new GridPane();
        gridPaneGameList.setPadding(new Insets(25, 50, 5, 5));

        //Implementing Nodes for GripPaneGameList
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList(
                "Game1", "Game2", "Game3", "Game4");
        list.setItems(items);

        //Adding Nodes to GridPane layout
        gridPaneGameList.add(list, 0, 0);

        //Adding GrindPaneGameList
        GridPane gridPaneScore = new GridPane();
        gridPaneScore.setPadding(new Insets(5, 50, 5, 5));

        //Implementing Nodes for GripPaneGameList
        Label lblScore = new Label();
        lblScore.setText("Rating Table (Last 5 games)");

        ListView<String> scoreList = new ListView<String>();
        ObservableList<String> scoreItems = FXCollections.observableArrayList(
                "Game1", "Game2", "Game3", "Game4");
        list.setItems(scoreItems);

        //Adding Nodes to GridPane layout
        gridPaneScore.add(lblScore, 0, 0);
        gridPaneScore.add(scoreList, 0, 1);

        //Add GridPane layout to BorderPane Layout
        bp.setLeft(gridPaneGameList);
        bp.setTop(gridPaneDesc);
        bp.setRight(gridPaneScore);
        
        //Chatbox
        chatbox.display();

        //Adding BorderPane to the scene
        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);

        //primaryStage.setResizable(false);
        primaryStage.show();
    }
}
