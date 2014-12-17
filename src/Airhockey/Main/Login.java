package Airhockey.Main;

import Airhockey.Properties.PropertiesManager;
import Airhockey.Renderer.Constants;
import Airhockey.Utils.Database;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    Database database;
    Stage primaryStage;
    final ToggleGroup group = new ToggleGroup();

    @Override
    public void start(Stage primaryStage) {

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
        if (setDifficulty()) {
            primaryStage = (Stage) btLogin.getScene().getWindow();
            primaryStage.close();
            Game g = new Game(primaryStage, false, false);
        }
    }

    public void actionlogin() {
        try {
            database = new Database();
            if (database.loginCheck(tfUsername.getText(), tfPassword.getText())) {
                primaryStage = (Stage) btLogin.getScene().getWindow();
                primaryStage.close();
                Lobby lobby = new Lobby(primaryStage);
                System.out.println("User: " + tfUsername.getText() + " logged in!");
            } else {
//                primaryStage = (Stage) btLogin.getScene().getWindow();
//                primaryStage.close();
//                Lobby lobby = new Lobby(primaryStage);
                showPopupWindow("Invalid login combination!", "Ok");
                System.out.println("Logged in (no user)!");
            }
        } catch (SQLException | IOException | IllegalArgumentException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            // to do add a proper notification
        }
    }

    public void actionCreateAccount() {
        primaryStage = (Stage) btLogin.getScene().getWindow();
        primaryStage.close();
        CreateAccount createAccount = new CreateAccount(primaryStage);
    }

    private boolean setDifficulty() {
        if (rbEasy.isSelected()) {
            PropertiesManager.saveProperty("LEB-Difficulty", "EASY");
            PropertiesManager.saveProperty("REB-Difficulty", "MEDIUM");
            return true;
        } else if (rbNormal.isSelected()) {
            PropertiesManager.saveProperty("LEB-Difficulty", "MEDIUM");
            PropertiesManager.saveProperty("REB-Difficulty", "HARD");
            return true;
        } else if (rbHard.isSelected()) {
            PropertiesManager.saveProperty("LEB-Difficulty", "HARD");
            PropertiesManager.saveProperty("REB-Difficulty", "VERY_HARD");
            return true;
        } else {
            showPopupWindow("No difficulty selected!", "Ok");
            return false;
        }
    }

    protected void showPopupWindow(String message, String buttonText) {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(tfUsername.getScene().getWindow());
        dialogStage.centerOnScreen();

        Button okButton = new Button(buttonText);
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialogStage.close();
            }
        });

        Label label = new Label(message);
        label.setFont(Font.font("Roboto", 24.0));
        label.setTextFill(Color.web(Constants.COLOR_GREEN));
        label.setPadding(new Insets(0, 0, 20, 0));
        label.relocate(100, 10);

        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        vBox.getChildren().add(okButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20, 40, 20, 40));

        Scene dialogScene = new Scene(vBox);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
}
