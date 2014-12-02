package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sam
 */
public class Goal {

    private int topLeftX;
    private int topLeftY;
    private int width = 200;
    private int height = 40;
    private double rotation;

    private Color color;

    public Node node;
    public Node collisionNode;

    public Goal(String color, int topLeftX, int topLeftY) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;

//        width = (int) Math.floor((double) triangleWidth * 0.4);
//        height = (int) Math.floor((double) triangleWidth * 0.04);
        switch (color) {
            case "RED":
                rotation = 0;
                this.color = Color.web("#dd4540");
                break;
            case "BLUE":
                rotation = -60;
                this.color = Color.web("#4d7fdd");
                break;
            default:
                rotation = 60;
                this.color = Color.web("#009587");
                break;
        }

        this.node = createRect();
        this.collisionNode = createCollisionNode();
    }

    private Node createRect() {
        Rectangle r = new Rectangle();

        r.setWidth(width);
        r.setHeight(height);
        r.setFill(color);
        r.setLayoutX(topLeftX);
        r.setLayoutY(topLeftY);
        r.setArcWidth(10);
        r.setArcHeight(10);

        RotateTransition t = new RotateTransition(Duration.millis(1), r);
        t.setByAngle(rotation);
        t.setAutoReverse(false);
        t.play();

        System.out.println("Value: " + Utils.toPixelPosX(topLeftY));
        System.out.println("Value2: " + (int) Math.floor(Utils.toPixelPosX(topLeftY)));
        System.out.println("LAYOUT: " + Utils.pixelEngineToFrame((int) Math.floor(Utils.toPixelPosX(topLeftY))));

        return r;
    }

    public Node createCollisionNode() {
        Vec2 TopLeft = new Vec2(topLeftX, topLeftY + 20);
        Vec2 TopRight = new Vec2((float) (topLeftX + width), topLeftY + 20);

        Line line = new Line(TopLeft.x, TopLeft.y, TopRight.x, TopRight.y);
        line.setStroke(Color.WHITE);

        RotateTransition t = new RotateTransition(Duration.millis(1), line);
        t.setByAngle(rotation);
        t.setAutoReverse(false);
        t.play();

        return line;
    }
}
