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
        super(primaryStage, game);
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

    @Override
    public void resetRound(int round) {
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
