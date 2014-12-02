package Airhockey.Elements;

import Airhockey.Utils.Utils;
import Airhockey.Properties.PropertiesManager;

import javafx.scene.paint.Color;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author Roel
 */
public class LeftEnemyBat extends Bat {

    private float speed;

    public LeftEnemyBat(int id, float postionX, float postionY, Color color) {
        super(id, postionX, postionY, color);

        String difficulty = PropertiesManager.loadProperty("LEB-Difficulty");
        switch (difficulty) {
            case "EASY":
                speed = 4.0f;
                break;
            case "MEDIUM":
                speed = 7.0f;
                break;
            case "HARD":
                speed = 10.0f;
                break;
            case "VERY_HARD":
                speed = 12.0f;
                break;
        }
    }

    public void moveUp(Body puckBody) {
        Body body = (Body) node.getUserData();

        Vec2 linearVelocity = puckBody.getLinearVelocity();
        float verticalVelocity = Utils.toPixelPosY(linearVelocity.y);

        int vv = (int) Math.abs(verticalVelocity);
        if (vv > speed) {
            body.setLinearVelocity(new Vec2(speed, speed));
        } else {
            body.setLinearVelocity(new Vec2(Utils.toPosX((float) vv), Utils.toPosY((float) vv)));
        }

        float xpos = Utils.toPixelPosX(body.getPosition().x);
        float ypos = Utils.toPixelPosY(body.getPosition().y);
        setPosition(xpos, ypos);
    }

    public void moveDown(Body puckBody) {
        Body body = (Body) node.getUserData();

        Vec2 linearVelocity = puckBody.getLinearVelocity();
        float verticalVelocity = Utils.toPixelPosY(linearVelocity.y);

        int vv = (int) Math.abs(verticalVelocity);
        if (vv > speed) {
            body.setLinearVelocity(new Vec2(-speed, -speed));
        } else {
            body.setLinearVelocity(new Vec2(Utils.toPosX((float) -vv), Utils.toPosY((float) -vv)));
        }
        float xpos = Utils.toPixelPosX(body.getPosition().x);
        float ypos = Utils.toPixelPosY(body.getPosition().y);
        setPosition(xpos, ypos);
    }

    public void stop() {
        Body body = (Body) node.getUserData();
        body.setLinearVelocity(new Vec2(0.0f, 0.0f));
    }
}
