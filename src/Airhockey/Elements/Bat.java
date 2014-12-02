package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class Bat {

    private final int id;
    private final Color color;

    protected float positionX;
    protected float positionY;

    protected float diameter;
    protected float radius = 40;
    //private final Goal goal;

    public Node node;
    public Node imageNode;

    private BodyType bodyType;
    private Body body;

    public Bat(int id, float positionX, float positionY, Color color) {
        this.id = id;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
        this.bodyType = BodyType.KINEMATIC;
        this.radius = 40;
        this.diameter = radius * 2.0f;
        node = create();
        imageNode = createImageNode();
    }

    private Node create() {
        //Create an UI for bat - JavaFX code
        Circle bat = new Circle();
        bat.setRadius(radius);
        bat.setFill(Color.TRANSPARENT); //set look and feel 

        bat.setLayoutX(Utils.toPixelPosX(positionX));
        bat.setLayoutY(Utils.toPixelPosY(positionY));

        bat.setCache(true); //Cache this object for better performance

        //Create an JBox2D body defination for bat.
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(positionX, positionY);

        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;  //We need to convert radius to JBox2D equivalent

        // Create a fixture for bat
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;


        /*
         * Virtual invisible JBox2D body of bat. Bodies have velocity and position. 
         * Forces, torques, and impulses can be applied to these bodies.
         */
        body = Utils.world.createBody(bd);
        body.createFixture(fd);
        bat.setUserData(body);
        return bat;
    }

    public Node createImageNode() {
        Image image;
        if (color.equals(Color.RED)) {
            image = new Image(getClass().getResourceAsStream("Images/RedBatSmall.png"), diameter + 10, diameter + 10, false, false);
        } else if (color.equals(Color.BLUE)) {
            image = new Image(getClass().getResourceAsStream("Images/BlueBatSmall.png"), diameter + 10, diameter + 10, false, false);
        } else {
            image = new Image(getClass().getResourceAsStream("Images/GreenBatSmall2.png"), diameter + 10, diameter + 10, false, false);
        }
        ImageView imageView = new ImageView(image);
        imageView.relocate(Utils.toPixelPosX(positionX) - radius - 5, Utils.toPixelPosY(positionY) - radius - 5);
        return imageView;
    }

    public void setPosition(float xPosition, float yPosition) {
        node.setLayoutX(xPosition);
        node.setLayoutY(yPosition);
        imageNode.setLayoutX(xPosition - radius);
        imageNode.setLayoutY(yPosition - radius);
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public Fixture getFixture() {
        return body.getFixtureList();
    }

    public Body getBody() {
        return body;
    }
}
