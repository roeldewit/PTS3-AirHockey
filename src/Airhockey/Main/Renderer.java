package Airhockey.Main;

import Airhockey.Elements.*;
import Airhockey.Properties.PropertiesManager;
import Airhockey.Utils.KeyListener;
import Airhockey.Utils.Utils;
import java.util.Random;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author Sam
 */
public class Renderer {

    private final Stage primaryStage;

    private Timeline timeline;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private Puck puck;
    private Bat bat;
    private LeftEnemyBat leftEnemyBat;
    private RightEnemyBat rightEnemyBat;
    private TriangleLine triangle;
    private TriangleLeftLine triangleLeft;

    private Bat lastHittedBat;

    private Goal redGoal;
    private Goal blueGoal;
    private Goal greenGoal;

    private Body batBody;
    private Body puckBody;

    private Button startButton;
    private final Group root = new Group();
    private final Group mainRoot = new Group();

    private Label player1NameLabel;
    private Label player2NameLabel;
    private Label player3NameLabel;
    private Label player1ScoreLabel;
    private Label player2ScoreLabel;
    private Label player3ScoreLabel;

    private final Game game;

    private Shape redGoalShape;
    private Shape blueGoalShape;
    private Shape greenGoalShape;
    private Shape puckShape;

    private boolean canImpulsBall = true;
    private boolean batSideMovementLeft = false;
    private boolean batSideMovementRight = false;
    private boolean canStopBatLeft;
    private boolean canStopBatRight;

    public Renderer(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
        start();
    }

    public void start() {
        primaryStage.setTitle("Airhockey");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();

        PropertiesManager.saveProperty("LEB-Difficulty", "HARD");
        PropertiesManager.saveProperty("REB-Difficulty", "VERY_HARD");

        final Scene scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web("#e0e0e0"));

        KeyListener keyListener = new KeyListener(this);
        scene.setOnKeyPressed(keyListener);
        scene.setOnKeyReleased(keyListener);
        Utils.world.setContactListener(new ContactListenerZ());

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(root);
        mainBorderPane.setRight(createChatBox());
        mainRoot.getChildren().add(mainBorderPane);

        drawShapes();
        createMovableItems();
        createFixedItems();
        addLabels();

        Duration duration = Duration.seconds(1.0 / 60.0);
        MyHandler eventHandler = new MyHandler();
        KeyFrame frame = new KeyFrame(duration, eventHandler, null, null);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);

        startButton = new Button();
        startButton.setLayoutX((Utils.WIDTH / 20));
        startButton.setLayoutY((30));
        startButton.setText("Start");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.playFromStart();
                startButton.setDisable(true);
                startButton.setVisible(false);
            }
        });

        root.getChildren().add(startButton);

        setUpGame();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Group createChatBox() {
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

    public void setUpGame() {
        game.addBatToPlayer(1, bat);
        game.addBatToPlayer(2, leftEnemyBat);
        game.addBatToPlayer(3, rightEnemyBat);
    }

    private void createMovableItems() {
        puck = new Puck(50, 45);

        if (batBody != null) {
            bat = new Bat(1, batBody.getPosition().x, batBody.getPosition().y, Color.RED);
        } else {
            bat = new Bat(1, 40, 18, Color.RED);
        }
        leftEnemyBat = new LeftEnemyBat(2, 34, 50, Color.BLUE);
        rightEnemyBat = new RightEnemyBat(3, 65, 50, Color.GREEN);

        root.getChildren().addAll(puck.node, puck.imageNode);
        root.getChildren().addAll(bat.node, bat.imageNode);
        root.getChildren().addAll(leftEnemyBat.node, leftEnemyBat.imageNode);
        root.getChildren().addAll(rightEnemyBat.node, rightEnemyBat.imageNode);

        puckShape = (Shape) puck.node;
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

        redGoalShape = (Shape) redGoal.collisionNode;
        blueGoalShape = (Shape) blueGoal.collisionNode;
        greenGoalShape = (Shape) greenGoal.collisionNode;
    }

    private void addLabels() {
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

    private void checkGoal() {
        Shape redGoalIntersect = Shape.intersect(redGoalShape, puckShape);
        Shape blueGoalIntersect = Shape.intersect(blueGoalShape, puckShape);
        Shape greenGoalIntersect = Shape.intersect(greenGoalShape, puckShape);

        if (redGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, bat);
        } else if (blueGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, leftEnemyBat);
        } else if (greenGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, leftEnemyBat);
        }
    }

    private class MyHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //Create time step. Set Iteration count 8 for velocity and 3 for positions
            Utils.world.step(1.0f / 40.f, 8, 3);

            puckBody = (Body) puck.node.getUserData();
            batBody = (Body) bat.node.getUserData();

            if (canImpulsBall) {
                Random randomizer = new Random();
                int horizontalMovement = randomizer.nextInt(8) - 4;
                horizontalMovement = (horizontalMovement > -1) ? horizontalMovement + 50 : horizontalMovement - 50;

                int verticalMovement = randomizer.nextInt(8) - 4;
                verticalMovement = (verticalMovement > -1) ? verticalMovement + 50 : verticalMovement - 50;

                puckBody.applyLinearImpulse(new Vec2((float) horizontalMovement, (float) verticalMovement), puckBody.getWorldCenter());
                canImpulsBall = false;
            }

            float puckBodyPosX = Utils.toPixelPosX(puckBody.getPosition().x);
            float puckBodyPosY = Utils.toPixelPosY(puckBody.getPosition().y);
            puck.setPosition(puckBodyPosX, puckBodyPosY);

            float batBodyPosX = Utils.toPixelPosX(batBody.getPosition().x);
            float batBodyPosY = Utils.toPixelPosY(batBody.getPosition().y);
            bat.setPosition(batBodyPosX, batBodyPosY);

            controlBatMovement(batBodyPosX);

            moveLeftEnemyBat(puckBodyPosY);
            moveRightEnemyBat(puckBodyPosY);

            checkGoal();
        }
    }

    /**
     *
     * @param batPositionX
     */
    private void controlBatMovement(float batPositionX) {
        if (batPositionX > 380) {
            canStopBatLeft = true;
            if (batSideMovementLeft) {
                batBody.setLinearVelocity(new Vec2(-25.0f, 0.0f));
                batSideMovementLeft = false;
                batSideMovementRight = false;
            }
        } else {
            if (canStopBatLeft) {
                stopBatMovement();
                canStopBatLeft = false;
            }
        }
        if (batPositionX < 640) {
            canStopBatRight = true;
            if (batSideMovementRight) {
                batBody.setLinearVelocity(new Vec2(25.0f, 0.0f));
                batSideMovementRight = false;
                batSideMovementLeft = false;
            }
        } else {
            if (canStopBatRight) {
                stopBatMovement();
                canStopBatRight = false;
            }
        }
    }

    public void startBatMovement(int direction) {
        if (direction == LEFT) {
            batSideMovementLeft = true;
            batSideMovementRight = false;
        } else if (direction == RIGHT) {
            batSideMovementRight = true;
            batSideMovementLeft = false;
        }
    }

    public void stopBatMovement() {
        batSideMovementLeft = false;
        batSideMovementRight = false;
        batBody.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }

    private synchronized void moveLeftEnemyBat(float puckBodyPosY) {
        Body leftEnemyBatBody = (Body) leftEnemyBat.node.getUserData();
        float leftEnemyBatPositionY = Utils.toPixelPosY(leftEnemyBatBody.getPosition().y);

        leftEnemyBat.stop();
        if (puckBodyPosY > leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY < 490) {
                leftEnemyBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY < leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY > 270) {
                leftEnemyBat.moveUp(puckBody);
            }
        }
    }

    private synchronized void moveRightEnemyBat(float puckBodyPosY) {
        Body rightEnemyBatBody = (Body) rightEnemyBat.node.getUserData();
        float rightEnemyBatPositionY = Utils.toPixelPosY(rightEnemyBatBody.getPosition().y);

        rightEnemyBat.stop();
        if (puckBodyPosY - 5 > rightEnemyBatPositionY + 5) {
            if (rightEnemyBatPositionY < 490) {
                rightEnemyBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY + 5 < rightEnemyBatPositionY - 5) {
            if (rightEnemyBatPositionY > 270) {
                rightEnemyBat.moveUp(puckBody);
            }
        }
    }

    protected void setTextFields(String field, String value) {
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

    private void drawShapes() {
        Canvas canvas = new Canvas(Utils.WIDTH, Utils.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        double centerPointX = Utils.WIDTH / 2;
        double centerPointY = Utils.HEIGHT / 2;

//        gc.setFill(Color.web("#dd4540"));
//        gc.fillOval(centerPointX - 10, centerPointY + 30, 20, 20);
        gc.setStroke(Color.web("#dd4540"));
        gc.setLineWidth(3);
        gc.strokeOval(centerPointX - 100, centerPointY - 60, 200, 200);
    }

    private void showPopupWindow() {
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

    protected void resetRound(int round) {
        timeline.stop();

        Utils.world.destroyBody(puckBody);
        Utils.world.destroyBody(batBody);
        Utils.world.destroyBody(leftEnemyBat.getBody());
        Utils.world.destroyBody(rightEnemyBat.getBody());

        root.getChildren().removeAll(puck.node, puck.imageNode);
        root.getChildren().removeAll(bat.node, bat.imageNode);
        root.getChildren().removeAll(leftEnemyBat.node, leftEnemyBat.imageNode);
        root.getChildren().removeAll(rightEnemyBat.node, rightEnemyBat.imageNode);

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

//        Rectangle rect = new Rectangle(0, 0, 0, 0);
//        rect.setWidth(Utils.WIDTH);
//        rect.setHeight(Utils.HEIGHT);
//        rect.setArcWidth(50);
//        rect.setFill(Color.web("#e0e0e0"));
//
//        root.getChildren().add(rect);
//
//        FillTransition ft = new FillTransition(Duration.millis(5000), rect, Color.web("#e0e0e0"), Color.TRANSPARENT);
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                scaleTransition
        );

        parallelTransition.playFromStart();
        parallelTransition.setOnFinished(new OnAnimationCompletionListener());
    }

    private class OnAnimationCompletionListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            canImpulsBall = true;
            timeline.playFromStart();
        }

    }

    protected void stop() {
        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect.setWidth(Utils.WIDTH);
        rect.setHeight(Utils.HEIGHT);
        rect.setArcWidth(50);

        root.getChildren().add(rect);

        FillTransition ft = new FillTransition(Duration.millis(2000), rect, Color.TRANSPARENT, Color.GRAY);
        ft.playFromStart();

        timeline.stop();
    }

    private class ContactListenerZ implements ContactListener {

        @Override
        public void beginContact(Contact contact) {
            Fixture fA = contact.getFixtureA();
            Fixture fB = contact.getFixtureB();

            if (fA == puck.getFixture() || fB == puck.getFixture()) {
                if (fA == bat.getFixture() || fB == bat.getFixture()) {
                    lastHittedBat = bat;
                } else if (fA == leftEnemyBat.getFixture() || fB == leftEnemyBat.getFixture()) {
                    lastHittedBat = leftEnemyBat;
                } else if (fA == rightEnemyBat.getFixture() || fB == rightEnemyBat.getFixture()) {
                    lastHittedBat = rightEnemyBat;
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {
        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    }
}
