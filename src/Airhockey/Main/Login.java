package Airhockey.Main;

import Airhockey.Utils.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author martijn
 */
public class Login extends Application {

    @FXML
    Button btLogin;

    @FXML
    RadioButton rbEasy;

    @FXML
    RadioButton rbNormal;

    @FXML
    RadioButton rbHard;

    @FXML
    TextField tfUsername;

    @FXML
    TextField tfPassword;

    @FXML
    Button btStartSingleGame;

    Database db;
    Stage primaryStage;
    final ToggleGroup group = new ToggleGroup();

    @Override
    public void start(Stage primaryStage
    ) {

        Login(primaryStage);
        this.primaryStage = primaryStage;

        rbEasy = new RadioButton();
        rbEasy.setToggleGroup(group);
        rbEasy.setSelected(true);

        rbNormal = new RadioButton();
        rbNormal.setToggleGroup(group);

        rbHard = new RadioButton();
        rbHard.setToggleGroup(group);
    }

    public void Login(Stage primaryStage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void startSingleGame() {
        primaryStage = (Stage) btLogin.getScene().getWindow();
        primaryStage.close();
        Game g = new Game(primaryStage, false, false);
    }

    public void actionlogin() {
        try {
            db = new Database();
            if (db.loginCheck(tfUsername.getText(), tfPassword.getText())) {
                primaryStage = (Stage) btLogin.getScene().getWindow();
                primaryStage.close();
                Lobby lobby = new Lobby(primaryStage);
                System.out.println("User: " + tfUsername.getText() + " logged in!");
            } else {
                primaryStage = (Stage) btLogin.getScene().getWindow();
                primaryStage.close();
                Lobby lobby = new Lobby(primaryStage);
                System.out.println("Logged in (no user)!");
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actionCreateAccount() {
        primaryStage = (Stage) btLogin.getScene().getWindow();
        primaryStage.close();
        CreateAccount createAccount = new CreateAccount(primaryStage);
    }
}
