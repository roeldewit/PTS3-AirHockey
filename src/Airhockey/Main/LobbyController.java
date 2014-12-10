/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Main;

import java.net.URL;
import java.util.ResourceBundle;
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

    Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void startGame() {
        primaryStage = (Stage) btStartGame.getScene().getWindow();
        primaryStage.close();
        Game g = new Game(primaryStage, true);
    }

}
