package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sam
 */
public class TriangleLine {

    public Node node;

    private final int width = Utils.WIDTH;
    private final int height = Utils.HEIGHT;
    private final int leftBottomX;
    private final int bottomLineY = 664;
    private final int topY;

    private float positionXL;
    private float positionYL;
    private float positionXR;
    private float positionYR;

    private float positionXC;
    private float positionYC;

    private BodyType bodyType;

    public TriangleLine(int screenHeight, float positionXL, float positionYL, float positionXR, float positionYR, float positionXC, float positionYC) {
//        width = (int) Math.floor(screenHeight * 1.1);  
//        height = (int) Math.floor(Math.tan(45) * (width / 2));
//        bottomLineY = (int) Math.floor(screenHeight - (screenHeight * 0.1)); 

        topY = bottomLineY - height;
        leftBottomX = 5;

//        positionX = Utils.toPosX(leftBottomX);
//        positionY = Utils.toPosY(bottomLineY);
        this.positionXL = positionXL;
        this.positionYL = positionYL;
        this.positionXR = positionXR;
        this.positionYR = positionYR;

        this.positionXC = positionXC;
        this.positionYC = positionYC;

        node = createLinePieceAB();
    }

    private Node createLinePieceAB() {
        Vec2 VecL = new Vec2(positionXL, positionYL);
        Vec2 VecR = new Vec2(positionXR, positionYR);
        Vec2 VecC = new Vec2(positionXC, positionYC);
        Vec2[] vecAbAB = new Vec2[]{VecL, VecR, VecC};

        //Line AAb = new Line(Utils.toPixelPosX(A.x), Utils.toPixelPosY(A.y), Utils.toPixelPosX(Ab.x), Utils.toPixelPosY(Ab.y));
        Line bottom = new Line(Utils.toPixelPosX(positionXL) + 30, Utils.toPixelPosY(positionYL) - 40, Utils.toPixelPosX(positionXR) + 30, Utils.toPixelPosY(positionYR) - 40);
        bottom.setStroke(Color.BLACK);
        bottom.setStrokeWidth(3.0);

        Line right = new Line(Utils.toPixelPosX(positionXC) + 40, Utils.toPixelPosY(positionYC) - 10, Utils.toPixelPosX(positionXR) + 40, Utils.toPixelPosY(positionYR) - 10);
        right.setStroke(Color.BLACK);
        right.setStrokeWidth(3.0);

        Group lineGroup = new Group();
        lineGroup.getChildren().addAll(bottom, right);

        createJboxLinePiece(vecAbAB, 3);

        return lineGroup;
    }

    private void createJboxLinePiece(Vec2[] vertices, int verticesSize) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(vertices[0].x, vertices[0].y);

//        PolygonShape line = new PolygonShape();
//        line.set(vertices, verticesSize);
        ChainShape s = new ChainShape();
        s.createChain(vertices, verticesSize);

        FixtureDef fd = new FixtureDef();
        fd.shape = s;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        Body body = Utils.world.createBody(bd);
        body.createFixture(fd);
        //body.setTransform(null, positionX);
    }
}
