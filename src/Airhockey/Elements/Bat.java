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

/**
 *
 * @author Roel
 */
public class Bat {

    private final int id;
    private final Color color;

    protected final float positionX;
    protected final float positionY;

    protected final float diameter;
    protected final float radius;

    public Node node;
    public Node imageNode;

    private final BodyType bodyType;
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
        Circle bat = new Circle();
        bat.setRadius(radius);
        bat.setFill(Color.TRANSPARENT);

        bat.setLayoutX(Utils.toPixelPosX(positionX));
        bat.setLayoutY(Utils.toPixelPosY(positionY));
        bat.setCache(true);

        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(positionX, positionY);

        CircleShape cs = new CircleShape();
        cs.m_radius = radius * 0.1f;

        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.0f;
        fd.friction = 0.0f;
        fd.restitution = 0.0f;

        body = Utils.world.createBody(bd);
        body.createFixture(fd);
        bat.setUserData(body);
        return bat;
    }

    private Node createImageNode() {
        Image image;
        if (color.equals(Color.RED)) {
            image = new Image(getClass().getResourceAsStream("Images/RedBatSmaller.png"), diameter, diameter, false, false);
        } else if (color.equals(Color.BLUE)) {
            image = new Image(getClass().getResourceAsStream("Images/BlueBatSmaller.png"), diameter, diameter, false, false);
        } else {
            image = new Image(getClass().getResourceAsStream("Images/GreenBatSmaller.png"), diameter, diameter, false, false);
        }
        ImageView imageView = new ImageView(image);
        imageView.relocate(Utils.toPixelPosX(positionX) - radius, Utils.toPixelPosY(positionY) - radius);
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
