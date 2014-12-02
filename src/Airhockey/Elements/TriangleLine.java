package Airhockey.Elements;

import Airhockey.Utils.Utils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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

    private float positionX;
    private float positionY;

    private float positionXL;
    private float positionYL;
    private float positionXR;
    private float positionYR;

    private BodyType bodyType;

    public TriangleLine(int screenHeight, float positionXL, float positionYL, int positionXR, int positionYR) {
//        width = (int) Math.floor(screenHeight * 1.1);  
//        height = (int) Math.floor(Math.tan(45) * (width / 2));
//        bottomLineY = (int) Math.floor(screenHeight - (screenHeight * 0.1)); 

        topY = bottomLineY - height;
        leftBottomX = 5;

        positionX = Utils.toPosX(leftBottomX);
        positionY = Utils.toPosY(bottomLineY);

        this.positionXL = positionXL;
        this.positionYL = positionYL;
        this.positionXR = positionXR;
        this.positionYR = positionYR;

        node = Create();
    }

    private Group Create() {
//        Rotate rotationMatrixLeft = new Rotate(60, A.x, A.y);
        Group linePieceAB = createLinePieceAB();
        //Group linePieceBC = createLinePieceBC();
        //Group linePieceAC = createLinePieceAC();

        Group triangle = new Group(linePieceAB/*,linePieceBC/*,linePieceAC*/);
        return triangle;

    }

    private Group createLinePieceAB() {
        // create the vectors for the linePiece
        Vec2 Ab = new Vec2(Utils.toPosX((int) (leftBottomX + (width * 0.3))), positionY);
        Vec2 Ba = new Vec2(Utils.toPosX((int) (leftBottomX + (width * 0.7))), positionY);
        Vec2 B = new Vec2(Utils.toPosX(leftBottomX + width), positionY);

        Vec2 VecL = new Vec2(positionXL, positionYL);
        Vec2 VecR = new Vec2(positionXR, positionYR);
        Vec2[] vecAbAB = new Vec2[]{VecL, VecR, VecL, VecR};

        // creates lineAB
        Group LineAB = new Group();
        //Line AAb = new Line(Utils.toPixelPosX(A.x), Utils.toPixelPosY(A.y), Utils.toPixelPosX(Ab.x), Utils.toPixelPosY(Ab.y));
        Line ABA = new Line(Utils.toPixelPosX(positionXL) + 50, Utils.toPixelPosY(positionYL), Utils.toPixelPosX(positionXR) + 50, Utils.toPixelPosY(positionYR));
        ABA.setStroke(Color.PINK);
        ABA.setStrokeWidth(3.0);
        //LineAB.getChildren().add(AAb);
        LineAB.getChildren().add(ABA);

        createJboxLinePiece(vecAbAB, 4);

        return LineAB;
    }

    private void handlesJbox(Shape shape, BodyDef bodyDef) {
    }

    private void createJboxLinePiece(Vec2[] vertices, int verticesSize) {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(vertices[0].x, Utils.toPosY(bottomLineY + 100));

        PolygonShape line = new PolygonShape();
        line.set(vertices, verticesSize);

        FixtureDef fd = new FixtureDef();
        fd.shape = line;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        Body body = Utils.world.createBody(bd);
        body.createFixture(fd);
    }
}
