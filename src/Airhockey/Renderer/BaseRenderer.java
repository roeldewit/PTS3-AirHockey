/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Renderer;

import Airhockey.Main.Game;
import Airhockey.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Sam
 */
public class BaseRenderer implements IRenderer {

    protected Label player1NameLabel;
    protected Label player2NameLabel;
    protected Label player3NameLabel;
    protected Label player1ScoreLabel;
    protected Label player2ScoreLabel;
    protected Label player3ScoreLabel;

    protected final Group root = new Group();
    protected final Group mainRoot = new Group();

    protected final Stage primaryStage;
    protected final Game game;

    public BaseRenderer(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    @Override
    public void setTextFields(String field, String value) {
        switch (field) {
            case "PLAYER1_SCORE":
                player1ScoreLabel.setText(value);
                break;
            case "PLAYER2_SCORE":
                player2ScoreLabel.setText(value);
                break;
            case "PLAYER3_SCORE":
                player3ScoreLabel.setText(value);
                break;
        }
    }

    @Override
    public void resetRound(int round) {
    }

    protected void addLabels() {
        player1NameLabel = new Label(game.getUsername(1) + ": ");
        player2NameLabel = new Label(game.getUsername(2) + ": ");
        player3NameLabel = new Label(game.getUsername(3) + ": ");
        player1ScoreLabel = new Label("20");
        player2ScoreLabel = new Label("20");
        player3ScoreLabel = new Label("20");

        player1NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player2NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player3NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player1ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player2ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player3ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));

        player1NameLabel.setTextFill(Color.web("#dd4540"));
        player2NameLabel.setTextFill(Color.web("#4d7fdd"));
        player3NameLabel.setTextFill(Color.web("#009587"));

        player1NameLabel.relocate(850, 10);
        player2NameLabel.relocate(850, 40);
        player3NameLabel.relocate(850, 70);
        player1ScoreLabel.relocate(970, 10);
        player2ScoreLabel.relocate(970, 40);
        player3ScoreLabel.relocate(970, 70);

        root.getChildren().addAll(player1NameLabel, player2NameLabel, player3NameLabel, player1ScoreLabel, player2ScoreLabel, player3ScoreLabel);
    }

    protected void drawShapes() {
        Canvas canvas = new Canvas(Utils.WIDTH, Utils.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        double centerPointX = Utils.WIDTH / 2;
        double centerPointY = Utils.HEIGHT / 2;

        gc.setStroke(Color.web("#dd4540"));
        gc.setLineWidth(3);
        gc.strokeOval(centerPointX - 100, centerPointY - 60, 200, 200);
    }

    protected void showPopupWindow() {
        final Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage.getScene().getWindow());
        dialogStage.centerOnScreen();

        Button okButton = new Button("Close");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialogStage.close();
            }
        });

        Label label = new Label("GAME OVER");
        label.setFont(Font.font("Roboto", 24.0));
        label.setTextFill(Color.web("#009587"));
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

    protected Group createChatBox() {
        ListView listView = new ListView();

        TextField textField = new TextField();
        Button btn = new Button();
        btn.setText("Send");

        BorderPane chatBoxBorderPane = new BorderPane();
        HBox bottom = HBoxBuilder.create().children(textField, btn).build();
        chatBoxBorderPane.setCenter(listView);
        chatBoxBorderPane.setBottom(bottom);
        chatBoxBorderPane.setMinHeight(Utils.HEIGHT - 40);
        chatBoxBorderPane.setMaxWidth(244);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textField.clear();
            }
        });

        Group chatBoxGroup = new Group();
        chatBoxGroup.getChildren().add(chatBoxBorderPane);

        return chatBoxGroup;
    }

}
