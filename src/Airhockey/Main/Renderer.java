/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package Airhockey.Main;

import Airhockey.Elements.Bat;
import Airhockey.Elements.Goal;
import Airhockey.Elements.LeftEnemyBat;
import Airhockey.Elements.Puck;
import Airhockey.Elements.RightEnemyBat;
import Airhockey.Elements.TriangleLeftLine;
import Airhockey.Elements.TriangleLine;
import Airhockey.Properties.PropertiesManager;
import Airhockey.Utils.KeyListener;
import Airhockey.Utils.Utils;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
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
    private boolean canImpulsBall = true;
    private boolean batSideMovementLeft = false;
    private boolean batSideMovementRight = false;

    private Popup popupWindow;
    private Timeline timeline;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private Puck puck;
    private Bat bat;
    private LeftEnemyBat leftEnemyBat;
    private RightEnemyBat rightEnemyBat;
    private TriangleLine triangle;
    private TriangleLeftLine triangleLeft;
    private TriangleLine triangleRight;

    private Goal redGoal;
    private Goal blueGoal;
    private Goal greenGoal;

    private Body batBody;
    private Body puckBody;

    private Button startButton;
    private Button testButton;
    private final Group root = new Group();
    private final Group mainRoot = new Group();
    //private final Group root2 = new Group();

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

    private Bat lastHittedBat;
    private boolean canStopBatLeft;
    private boolean canStopBatRight;

    public Renderer(Stage primaryStage) {
        this.primaryStage = primaryStage;
        game = new Game(this);
        start();
    }

    public void start() {
        primaryStage.setTitle("Airhockey");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();

        KeyListener keyListener = new KeyListener(this);
        PropertiesManager.saveProperty("LEB-Difficulty", "EASY");
        PropertiesManager.saveProperty("REB-Difficulty", "EASY");

        //Create a group for holding all objects on the screen
        final Scene scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web("#e0e0e0"));
        //final Scene scene2 = new Scene(root2, Utils.WIDTH, Utils.HEIGHT, Color.WHITE);

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(root);
        mainBorderPane.setRight(createChatBox());
        mainRoot.getChildren().add(mainBorderPane);

        Canvas canvas = new Canvas(Utils.WIDTH, Utils.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        root.getChildren().add(canvas);

        addItems();
        addLabels();

//        //Add ground to the application, this is where balls will land
        Utils.addGround(100, 10);
//
////        //Add left and right walls so balls will not move outside the viewing area.
        Utils.addWall(0, 100, 1, 100); //Left wall
        Utils.addWall(99, 100, 1, 100); //Right wall
        Utils.addWall(0, 99, 100, 1);
        /**
         * Set ActionEvent and duration to the KeyFrame. The ActionEvent is trigged when KeyFrame execution is over.
         */
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.seconds(1.0 / 60.0); // Set duration for frame.
        MyHandler eventHandler = new MyHandler();
        KeyFrame frame = new KeyFrame(duration, eventHandler, null, null);
        timeline.getKeyFrames().add(frame);

        scene.setOnKeyPressed(keyListener);
        scene.setOnKeyReleased(keyListener);

        //Create button to start simulation.
        startButton = new Button();
        startButton.setLayoutX((Utils.WIDTH / 20));
        startButton.setLayoutY((30));
        startButton.setText("Start");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.playFromStart();
                startButton.setDisable(true);
            }
        });

        //Create button to start simulation.
        testButton = new Button();
        testButton.setLayoutX((Utils.WIDTH / 20));
        testButton.setLayoutY((70));
        testButton.setText("Test");
        testButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //game.setGoal(bat, leftEnemyBat);               
                showPopupWindow();
                stop();
            }
        });

        root.getChildren().add(startButton);
        root.getChildren().add(testButton);

        setUpGame();

        Utils.world.setContactListener(new ContactListenerZ());

        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println(Math.floor(Math.tan(45) * (15 / 2)));
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

    public void setUpGame() {
        game.addBatToPlayer(1, bat);
        game.addBatToPlayer(2, leftEnemyBat);
        game.addBatToPlayer(3, rightEnemyBat);
    }

    private void addItems() {
        puck = new Puck(50, 50);
        bat = new Bat(1, 40, 20, Color.RED);
        leftEnemyBat = new LeftEnemyBat(2, 35, 50, Color.BLUE);
        rightEnemyBat = new RightEnemyBat(3, 65, 50, Color.GREEN);

        triangleLeft = new TriangleLeftLine(0, 3f, 5f, 48f, 95f);
        triangle = new TriangleLine(0, 3f, 5f, 88f, 5f, 48f, 95f);

        redGoal = new Goal("RED", 410, 670);
        blueGoal = new Goal("BLUE", 190, 340);
        greenGoal = new Goal("GREEN", 640, 340);

        root.getChildren().add(puck.node);
        root.getChildren().add(triangle.node);
        root.getChildren().add(triangleLeft.node);
//        root.getChildren().add(triangleRight.node);
        root.getChildren().addAll(redGoal.node, redGoal.collisionNode);
        root.getChildren().addAll(blueGoal.node, blueGoal.collisionNode);
        root.getChildren().addAll(greenGoal.node, greenGoal.collisionNode);
        root.getChildren().addAll(bat.node, bat.imageNode);
        root.getChildren().addAll(leftEnemyBat.node, leftEnemyBat.imageNode);
        root.getChildren().addAll(rightEnemyBat.node, rightEnemyBat.imageNode);

        redGoalShape = (Shape) redGoal.collisionNode;
        blueGoalShape = (Shape) blueGoal.collisionNode;
        greenGoalShape = (Shape) greenGoal.collisionNode;
        puckShape = (Shape) puck.node;
    }

    private void addLabels() {
        popupWindow = new Popup();
        popupWindow.setX(300);
        popupWindow.setY(200);
        popupWindow.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));

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

            //Move balls to the new position computed by JBox2D
            puckBody = (Body) puck.node.getUserData();
            batBody = (Body) bat.node.getUserData();

            if (canImpulsBall) {
                Random randomizer = new Random();
                int horizontalMovement = randomizer.nextInt(8) - 4;
                horizontalMovement = (horizontalMovement > -1) ? horizontalMovement + 8 : horizontalMovement - 8;

                int verticalMovement = randomizer.nextInt(8) - 4;
                verticalMovement = (verticalMovement > -1) ? verticalMovement + 8 : verticalMovement - 8;

                puckBody.applyLinearImpulse(new Vec2((float) horizontalMovement, (float) verticalMovement), puckBody.getWorldCenter());
                canImpulsBall = false;
            }

            float xpos = Utils.toPixelPosX(puckBody.getPosition().x);
            float ypos = Utils.toPixelPosY(puckBody.getPosition().y);
            puck.node.setLayoutX(xpos);
            puck.node.setLayoutY(ypos);

            float xposB = Utils.toPixelPosX(batBody.getPosition().x);
            float yposB = Utils.toPixelPosY(batBody.getPosition().y);

            bat.setPosition(xposB, yposB);

            controlBatMovement(xposB);

            moveLeftEnemyBat(puckBody);
            moveRightEnemyBat(puckBody);

            //checkGoal();
        }
    }

    private void controlBatMovement(float xPosB) {
        if (xPosB > 200) {
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
        if (xPosB < 800) {
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

    private synchronized void moveLeftEnemyBat(Body puckBody) {
        float puckPositionY = Utils.toPixelPosY(puckBody.getPosition().y);

        Body leftEnemyBatBody = (Body) leftEnemyBat.node.getUserData();
        float leftEnemyBatPositionY = Utils.toPixelPosY(leftEnemyBatBody.getPosition().y);

        if (puckPositionY > leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY < 600) {
                leftEnemyBat.moveDown(puckBody);
            } else {
                leftEnemyBat.stop();
            }
        } else if (puckPositionY < leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY > 300) {
                leftEnemyBat.moveUp(puckBody);
            } else {
                leftEnemyBat.stop();
            }
        }
    }

    private synchronized void moveRightEnemyBat(Body puckBody) {
        float puckPositionY = Utils.toPixelPosY(puckBody.getPosition().y);

        Body rightEnemyBatBody = (Body) rightEnemyBat.node.getUserData();
        float rightEnemyBatPositionY = Utils.toPixelPosY(rightEnemyBatBody.getPosition().y);

        if (puckPositionY - 1 > rightEnemyBatPositionY + 1) {
            if (rightEnemyBatPositionY < 600) {
                rightEnemyBat.moveDown(puckBody);
            } else {
                rightEnemyBat.stop();
            }
        } else if (puckPositionY < rightEnemyBatPositionY) {
            if (rightEnemyBatPositionY > 300) {
                rightEnemyBat.moveUp(puckBody);
            } else {
                rightEnemyBat.stop();
            }
        }
    }

    protected void setTextFields(String field, String value) {
        switch (field) {
            case "PLAYER1_NAME":
                player1NameLabel.setText(value);
                break;
            case "PLAYER2_NAME":
                player2NameLabel.setText(value);
                break;
            case "PLAYER3_NAME":
                player3NameLabel.setText(value);
                break;
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

    private void drawShapes(GraphicsContext gc) {
        double centerPointX = Utils.WIDTH / 2;
        double centerPointY = Utils.HEIGHT / 2;

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

        root.getChildren().removeAll(puck.node);
        root.getChildren().removeAll(bat.node, bat.imageNode);
        root.getChildren().removeAll(leftEnemyBat.node, leftEnemyBat.imageNode);
        root.getChildren().removeAll(rightEnemyBat.node, rightEnemyBat.imageNode);

        newRoundTransition(round);

        puck = new Puck(50, 50);
        bat = new Bat(1, 40, 20, Color.RED);
        leftEnemyBat = new LeftEnemyBat(2, 35, 50, Color.BLUE);
        rightEnemyBat = new RightEnemyBat(3, 65, 50, Color.GREEN);

        puckShape = (Shape) puck.node;
        root.getChildren().add(puck.node);
        root.getChildren().addAll(bat.node, bat.imageNode);
        root.getChildren().addAll(leftEnemyBat.node, leftEnemyBat.imageNode);
        root.getChildren().addAll(rightEnemyBat.node, rightEnemyBat.imageNode);
    }

    private void newRoundTransition(int round) {
        Label roundLabel = new Label();
        roundLabel.setText("Round " + round);
        roundLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24.0));
        roundLabel.setTextFill(Color.web("#009587"));
        roundLabel.relocate(460, 340);

        root.getChildren().add(roundLabel);

        FadeTransition fadeTransition
                = new FadeTransition(Duration.millis(1000), roundLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        ScaleTransition scaleTransition
                = new ScaleTransition(Duration.millis(1000), roundLabel);
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
}
