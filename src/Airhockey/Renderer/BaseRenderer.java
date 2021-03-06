/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Airhockey.Renderer;

import Airhockey.Elements.*;
import Airhockey.Main.Game;
import Airhockey.Utils.Utils;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Sam
 */
class BaseRenderer implements IRenderer {

    protected Label player1NameLabel;
    protected Label player2NameLabel;
    protected Label player3NameLabel;
    protected Label player1ScoreLabel;
    protected Label player2ScoreLabel;
    protected Label player3ScoreLabel;
    protected Label roundTextLabel;
    protected Label roundNumberLabel;

    protected final Group root = new Group();
    protected final Group mainRoot = new Group();

    protected final Stage primaryStage;
    protected final Game game;

    protected Puck puck;
    protected Bat bat;
    protected LeftBat leftBat;
    protected RightBat rightBat;
    protected TriangleLine triangle;
    protected TriangleLeftLine triangleLeft;
    protected Goal redGoal;
    protected Goal blueGoal;
    protected Goal greenGoal;

    protected Body puckBody;

    protected ParallelTransition parallelTransition;

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

    protected void createScreenStuff() {
        player1NameLabel = new Label(game.getUsername(1).toUpperCase() + ": ");
        player2NameLabel = new Label(game.getUsername(2).toUpperCase() + ": ");
        player3NameLabel = new Label(game.getUsername(3).toUpperCase() + ": ");
        player1ScoreLabel = new Label("20");
        player2ScoreLabel = new Label("20");
        player3ScoreLabel = new Label("20");
        roundTextLabel = new Label("ROUND:");
        roundNumberLabel = new Label("1");

        player1NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player2NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player3NameLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player1ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player2ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        player3ScoreLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        roundTextLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        roundNumberLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));

        player1NameLabel.setTextFill(Color.web(Constants.COLOR_RED));
        player2NameLabel.setTextFill(Color.web(Constants.COLOR_BLUE));
        player3NameLabel.setTextFill(Color.web(Constants.COLOR_GREEN));
        player1ScoreLabel.setTextFill(Color.WHITE);
        player2ScoreLabel.setTextFill(Color.WHITE);
        player3ScoreLabel.setTextFill(Color.WHITE);
        roundTextLabel.setTextFill(Color.web(Constants.COLOR_ORANGE));
        roundNumberLabel.setTextFill(Color.WHITE);

        player1NameLabel.relocate(850, 10);
        player2NameLabel.relocate(850, 40);
        player3NameLabel.relocate(850, 70);
        player1ScoreLabel.relocate(970, 10);
        player2ScoreLabel.relocate(970, 40);
        player3ScoreLabel.relocate(970, 70);
        roundTextLabel.relocate(30, 10);
        roundNumberLabel.relocate(140, 10);

        root.getChildren().addAll(player1NameLabel, player2NameLabel, player3NameLabel, player1ScoreLabel, player2ScoreLabel, player3ScoreLabel);
        root.getChildren().addAll(roundTextLabel, roundNumberLabel);
    }

    protected void drawShapes() {
        Canvas canvas = new Canvas(Utils.WIDTH, Utils.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        double centerPointX = Utils.WIDTH / 2;
        double centerPointY = Utils.HEIGHT / 2;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeOval(centerPointX - 100, centerPointY - 60, 200, 200);

        //gc.strokeArc(600, 200, 100, 300, 140, 180, ArcType.ROUND);
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

    protected Group createChatBox() {
        ListView listView = new ListView();

        TextField textField = new TextField();
        Button btn = new Button();
        btn.setText("Send");
        btn.setStyle("-fx-font: 14px Roboto;  -fx-padding: 5 10 5 10; -fx-background-color: #D23641; -fx-text-fill: white;  -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 1,1,1,1 );");

        BorderPane chatBoxBorderPane = new BorderPane();
        HBox bottom = HBoxBuilder.create().children(textField, btn).build();
        chatBoxBorderPane.setCenter(listView);
        chatBoxBorderPane.setBottom(bottom);
        chatBoxBorderPane.setMinHeight(Utils.HEIGHT - 40);
        chatBoxBorderPane.setMaxWidth(244);

        btn.setOnAction((ActionEvent e) -> {
            textField.clear();
        });

        Group chatBoxGroup = new Group();
        chatBoxGroup.getChildren().add(chatBoxBorderPane);

        return chatBoxGroup;
    }

    protected void newRoundTransition(int round) {
        Label roundLabel = new Label();
        roundLabel.setText("Round " + round);
        roundLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        roundLabel.setTextFill(Color.web(Constants.COLOR_ORANGE));
        roundLabel.relocate(460, 340);

        root.getChildren().add(roundLabel);

        FadeTransition fadeTransition
                = new FadeTransition(Duration.millis(2000), roundLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        ScaleTransition scaleTransition
                = new ScaleTransition(Duration.millis(2000), roundLabel);
        scaleTransition.setFromX(2f);
        scaleTransition.setFromY(2f);
        scaleTransition.setToX(8f);
        scaleTransition.setToY(8f);

        parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                scaleTransition
        );

        parallelTransition.playFromStart();
    }

    protected void createFixedItems() {
        triangle = new TriangleLine(0, 3f, 5f, 88f, 5f, 48f, 95f);
        triangleLeft = new TriangleLeftLine(0, 3f, 5f, 48f, 95f);

        redGoal = new Goal(Constants.GOAL_RED, 340, 670);
        blueGoal = new Goal(Constants.GOAL_BLUE, 124, 330);
        greenGoal = new Goal(Constants.GOAL_GREEN, 548, 330);

        root.getChildren().add(triangle.node);
        root.getChildren().add(triangleLeft.node);
        root.getChildren().addAll(redGoal.node);
        root.getChildren().addAll(blueGoal.node);
        root.getChildren().addAll(greenGoal.node);
    }

    protected void correctPuckSpeed() {
        Vec2 vec = puckBody.getLinearVelocity();
        Vec2 puckBodyCenter = puckBody.getWorldCenter();

        if (Math.abs(vec.x) > Math.abs(vec.y)) {
            if (vec.x > 20) {
                puckBody.applyLinearImpulse(new Vec2(-1.0f, 0.0f), puckBodyCenter);
            } else if (vec.x >= 0 && vec.x < 12) {
                puckBody.applyLinearImpulse(new Vec2(1.0f, 1.0f), puckBodyCenter);
            } else if (vec.x > -12 && vec.x <= 0) {
                puckBody.applyLinearImpulse(new Vec2(-1.0f, -1.0f), puckBodyCenter);
            } else if (vec.x < -20) {
                puckBody.applyLinearImpulse(new Vec2(1.0f, 0.0f), puckBodyCenter);
            }
        } else {
            if (vec.y > 20) {
                puckBody.applyLinearImpulse(new Vec2(0.0f, -1.0f), puckBodyCenter);
            } else if (vec.y >= 0 && vec.y < 12) {
                puckBody.applyLinearImpulse(new Vec2(1.0f, 1.0f), puckBodyCenter);
            } else if (vec.y > -12 && vec.y <= 0) {
                puckBody.applyLinearImpulse(new Vec2(-1.0f, -1.0f), puckBodyCenter);
            } else if (vec.y < -20) {
                puckBody.applyLinearImpulse(new Vec2(0.0f, 1.0f), puckBodyCenter);
            }
        }
    }
}
