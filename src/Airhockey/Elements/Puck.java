package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.scene.Node;
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
public class Puck {

    public Node node;

    private float positionX;
    private float positionY;

    private int diameter;
    private float radius = 20;

    private BodyType bodyType;
    
    public Body body;
    public FixtureDef fd;

    public Puck(float positionX, float positionY) {
        //this.diameter = (int) Math.floor((double) triangleWidth * 0.04);
        this.radius = Utils.BALL_RADIUS;
        this.positionX = positionX;
        this.positionY = positionY;
        this.bodyType = BodyType.DYNAMIC;
        node = create();
    }

    private Node create() {
        //Create an UI for puck - JavaFX code
        Circle puck = new Circle();
        puck.setRadius(radius);
        puck.setFill(Color.BLACK); //set look and feel 
        //ImageView imageView = new ImageView();

        puck.setLayoutX(Utils.toPixelPosX(positionX));
        puck.setLayoutY(Utils.toPixelPosY(positionY));

        puck.setCache(true); //Cache this object for better performance

        //Create an JBox2D body defination for puck.
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(positionX, positionY);

        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;  //We need to convert radius to JBox2D equivalent

        // Create a fixture for puck
        fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 1.0f;

        /*
         * Virtual invisible JBox2D body of puck. Bodies have velocity and position. 
         * Forces, torques, and impulses can be applied to these bodies.
         */
        body = Utils.world.createBody(bd);
        body.createFixture(fd);
        puck.setUserData(body);
        return puck;
    }
    
    public Fixture getFixture(){
        return body.getFixtureList();
    }

}
