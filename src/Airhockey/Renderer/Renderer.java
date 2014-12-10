package Airhockey.Renderer;

import Airhockey.Elements.*;
import Airhockey.Main.Game;
import Airhockey.Properties.PropertiesManager;
import Airhockey.Utils.KeyListener;
import Airhockey.Utils.Utils;
import java.util.Random;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
public class Renderer extends BaseRenderer {

    private Timeline timeline;

    private Puck puck;
    private Bat bat;
    private LeftBat leftBat;
    private RightBat rightBat;
    private TriangleLine triangle;
    private TriangleLeftLine triangleLeft;

    private Bat lastHittedBat;

    private Goal redGoal;
    private Goal blueGoal;
    private Goal greenGoal;

    private Body puckBody;
    protected Body batBody;
    protected Body leftBatBody;
    protected Body rightBatBody;

    private Button startButton;

    private Shape redGoalShape;
    private Shape blueGoalShape;
    private Shape greenGoalShape;
    private Shape puckShape;

    private boolean canImpulsBall = true;

    private final boolean isMultiplayer = true;

    private final BatController batController;

    public Renderer(Stage primaryStage, Game game) {
        super(primaryStage, game);
        batController = new BatController(this);
        start();
    }

    public final void start() {
        primaryStage.setTitle("Airhockey");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.setWidth(Utils.WIDTH + 250);
        primaryStage.setHeight(Utils.HEIGHT);
        primaryStage.centerOnScreen();

        PropertiesManager.saveProperty("LEB-Difficulty", "HARD");
        PropertiesManager.saveProperty("REB-Difficulty", "VERY_HARD");

        final Scene scene = new Scene(mainRoot, Utils.WIDTH, Utils.HEIGHT, Color.web("#e0e0e0"));

        KeyListener keyListener = new KeyListener(batController);
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

    public void setUpGame() {
        game.addBatToPlayer(1, bat);
        game.addBatToPlayer(2, leftBat);
        game.addBatToPlayer(3, rightBat);
    }

    private void createMovableItems() {
        puck = new Puck(50, 45);

        if (batBody != null) {
            bat = new Bat(1, batBody.getPosition().x, batBody.getPosition().y, Color.RED);
        } else {
            bat = new Bat(1, 40, 18, Color.RED);
        }
        leftBat = new LeftBat(2, 34, 50, Color.BLUE);
        rightBat = new RightBat(3, 65, 50, Color.GREEN);

        root.getChildren().addAll(puck.node, puck.imageNode);
        root.getChildren().addAll(bat.node, bat.imageNode);
        root.getChildren().addAll(leftBat.node, leftBat.imageNode);
        root.getChildren().addAll(rightBat.node, rightBat.imageNode);

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

    private void checkGoal() {
        Shape redGoalIntersect = Shape.intersect(redGoalShape, puckShape);
        Shape blueGoalIntersect = Shape.intersect(blueGoalShape, puckShape);
        Shape greenGoalIntersect = Shape.intersect(greenGoalShape, puckShape);

        if (redGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, bat);
        } else if (blueGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, leftBat);
        } else if (greenGoalIntersect.getBoundsInLocal().getWidth() != -1) {
            game.setGoal(lastHittedBat, leftBat);
        }
    }

    private class MyHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //Create time step. Set Iteration count 8 for velocity and 3 for positions
            Utils.world.step(1.0f / 40.f, 8, 3);

            puckBody = (Body) puck.node.getUserData();
            batBody = (Body) bat.node.getUserData();
            leftBatBody = (Body) leftBat.node.getUserData();
            rightBatBody = (Body) rightBat.node.getUserData();

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

            batController.controlCenterBat(batBodyPosX);
            //controlBatMovement(batBodyPosX);

            if (isMultiplayer) {
                batController.controlLeftBat(Utils.toPixelPosY(leftBatBody.getPosition().y));
                batController.controlRightBat(Utils.toPixelPosY(rightBatBody.getPosition().y));
            } else {
                moveLeftEnemyBat(puckBodyPosY);
                moveRightEnemyBat(puckBodyPosY);
            }

            checkGoal();
        }
    }

    private synchronized void moveLeftEnemyBat(float puckBodyPosY) {
        Body leftEnemyBatBody = (Body) leftBat.node.getUserData();
        float leftEnemyBatPositionY = Utils.toPixelPosY(leftEnemyBatBody.getPosition().y);

        leftBat.stop();
        if (puckBodyPosY > leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY < 490) {
                leftBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY < leftEnemyBatPositionY) {
            if (leftEnemyBatPositionY > 270) {
                leftBat.moveUp(puckBody);
            }
        }
    }

    private synchronized void moveRightEnemyBat(float puckBodyPosY) {
        Body rightEnemyBatBody = (Body) rightBat.node.getUserData();
        float rightEnemyBatPositionY = Utils.toPixelPosY(rightEnemyBatBody.getPosition().y);

        rightBat.stop();
        if (puckBodyPosY - 5 > rightEnemyBatPositionY + 5) {
            if (rightEnemyBatPositionY < 490) {
                rightBat.moveDown(puckBody);
            }
        } else if (puckBodyPosY + 5 < rightEnemyBatPositionY - 5) {
            if (rightEnemyBatPositionY > 270) {
                rightBat.moveUp(puckBody);
            }
        }
    }

    @Override
    public void resetRound(int round) {
        timeline.stop();

        Utils.world.destroyBody(puckBody);
        Utils.world.destroyBody(batBody);
        Utils.world.destroyBody(leftBat.getBody());
        Utils.world.destroyBody(rightBat.getBody());

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
                } else if (fA == leftBat.getFixture() || fB == leftBat.getFixture()) {
                    lastHittedBat = leftBat;
                } else if (fA == rightBat.getFixture() || fB == rightBat.getFixture()) {
                    lastHittedBat = rightBat;
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
