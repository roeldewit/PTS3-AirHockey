package Airhockey.Renderer;

import Airhockey.Utils.ClientKeyListener;
import Airhockey.Elements.*;
import Airhockey.Main.*;
import Airhockey.Properties.PropertiesManager;
import Airhockey.Rmi.Location;
import Airhockey.Utils.Utils;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Sam
 */
public final class ClientRenderer extends BaseRenderer {

    private Puck puck;
    private Bat bat;
    private Bat leftBat;
    private Bat rightBat;
    private TriangleLine triangle;
    private TriangleLeftLine triangleLeft;

    private Goal redGoal;
    private Goal blueGoal;
    private Goal greenGoal;

    int xPosition = 100;
    int yPosition = 100;

    RendererUtilities rendererUtilities;

    public ClientRenderer(Stage primaryStage, Game game) {
        super(primaryStage, game);

        start();
        rendererUtilities = new RendererUtilities(triangle);
    }

    public final void start() {
        primaryStage.setTitle("AirhockeyClient");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();

        PropertiesManager.saveProperty("LEB-Difficulty", "HARD");
        PropertiesManager.saveProperty("REB-Difficulty", "VERY_HARD");

        final Scene scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web("#e0e0e0"));

        ClientKeyListener keyListener = new ClientKeyListener(this);
        scene.setOnKeyPressed(keyListener);
        scene.setOnKeyReleased(keyListener);

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(root);
        mainBorderPane.setRight(createChatBox());
        mainRoot.getChildren().add(mainBorderPane);

        drawShapes();
        createMovableItems();
        createFixedItems();
        addLabels();

        //setUpGame();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setItemPosition(Location location) {

    }

    public void startTimer() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new timerTaskZ(), 0, 1000 / 60);
    }

    public class timerTaskZ extends TimerTask {

        @Override
        public void run() {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    leftBat.setPosition(xPosition, yPosition);

                    bat.setPosition(rendererUtilities.batPositionSideToBottom(yPosition), 600);
                    yPosition++;

                }
            });
        }
    }

    private void createMovableItems() {
        puck = new Puck(50, 45);

        bat = new Bat(1, 40, 18, Color.RED);

        leftBat = new Bat(2, 34, 50, Color.BLUE);
        rightBat = new Bat(3, 65, 50, Color.GREEN);

        root.getChildren().addAll(puck.node, puck.imageNode);
        root.getChildren().addAll(bat.node, bat.imageNode);
        root.getChildren().addAll(leftBat.node, leftBat.imageNode);
        root.getChildren().addAll(rightBat.node, rightBat.imageNode);
    }

    private void createFixedItems() {
        triangle = new TriangleLine(0, 3f, 5f, 88f, 5f, 48f, 95f);
        triangleLeft = new TriangleLeftLine(0, 3f, 5f, 48f, 95f);

        redGoal = new Goal("RED", 340, 670);
        blueGoal = new Goal("BLUE", 124, 330);
        greenGoal = new Goal("GREEN", 548, 330);

        root.getChildren().add(triangle.node);
        root.getChildren().add(triangleLeft.node);
        root.getChildren().addAll(redGoal.node, redGoal.collisionNode);
        root.getChildren().addAll(blueGoal.node, blueGoal.collisionNode);
        root.getChildren().addAll(greenGoal.node, greenGoal.collisionNode);
    }

    @Override
    public void resetRound(int round) {
        root.getChildren().removeAll(puck.node, puck.imageNode);
        root.getChildren().removeAll(bat.node, bat.imageNode);
        root.getChildren().removeAll(leftBat.node, leftBat.imageNode);
        root.getChildren().removeAll(rightBat.node, rightBat.imageNode);

        newRoundTransition(round);

        createMovableItems();
    }

    private void newRoundTransition(int round) {
        Label roundLabel = new Label();
        roundLabel.setText("Round " + round);
        roundLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        roundLabel.setTextFill(Color.web("#009587"));
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

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                scaleTransition
        );

        parallelTransition.playFromStart();
    }

    protected void stop() {
        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect.setWidth(Utils.WIDTH);
        rect.setHeight(Utils.HEIGHT);
        rect.setArcWidth(50);

        root.getChildren().add(rect);

        FillTransition ft = new FillTransition(Duration.millis(2000), rect, Color.TRANSPARENT, Color.GRAY);
        ft.playFromStart();
    }
}
