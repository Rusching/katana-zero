package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Sound;
import lombok.Data;

import java.awt.*;
import java.util.List;

import static java.lang.Math.abs;

@Data
public class Character extends Sprite{

    // states
    protected boolean isIdle = true;

    protected boolean isInAir = false;

    // jump is divided into 'up' and 'down' procedures. It is
    // distinguished by the 'isFalling' flag: true is up and false is down.
    protected boolean isFalling = false;

    // relevant to rolling
    protected boolean isRolling = false;
    protected final int rollFrames = 7;
    protected int currentRollIdx = 0;

    protected boolean isAttack = false;
    protected final int attackFrames = 7;
    protected int currentAttachIdx = 0;
    // indicates if the left or right key are pressed
    protected boolean isRunning = false;
    // By default, zero is facing right and textures are facing right
    protected boolean isFacingLeft = false;
    protected int run2IdleFlag = 0;

    public static final int MIN_RADIUS = 18;


    protected double maximum_jump_time = 1000;
    // velocity
    protected double x_velocity = 0;
    protected double x_accelerate = 1.4;
    protected double x_slowdown_accelerate = 10;

    protected double y_velocity = 0;
    protected double max_x_velocity = 18;
    protected double max_y_velocity = 30;

    protected  final double initial_y_velocity = -24;
    public final double gravityG = -1.6;

    public boolean isOnPlatform() {
        setCenterY(center.y += 5);
        Block block = findCollisionWall();
        setCenterY(center.y -= 5);
        if (block != null) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void draw(Graphics g) {

    }

    public Block findCollisionWall() {
        List<Movable> blocks = CommandCenter.getInstance().getMovFloors();
        for (Movable block: blocks) {
            Rectangle blockRect = ((Block) (block)).getBoundingBox();

            boolean noXOverlap = boundingBox.x + boundingBox.width <= blockRect.x || boundingBox.x >= blockRect.x + blockRect.width;
            boolean noYOverlap = boundingBox.y + boundingBox.height <= blockRect.y || boundingBox.y >= blockRect.y + blockRect.height;
            if (!noXOverlap && !noYOverlap) {
                return (Block) (block);
            }
        }
        return null;
    }
    @Override
    public void move() {
        //right-bounds reached
        if (center.x > Game.DIM.width) {
            setCenter(new Point(1, center.y));
            //left-bounds reached
        } else if (center.x < 0) {
            setCenter(new Point(Game.DIM.width - 1, center.y));
            //bottom-bounds reached
        } else if (center.y > Game.DIM.height) {
            setCenter(new Point(center.x, 1));
            //top-bounds reached
        } else if (center.y < 0) {
            setCenter(new Point(center.x, Game.DIM.height - 1));
            //in-bounds
        } else {
            double newYPos = center.y + getDeltaY();
            setCenterY((int) newYPos);
            Block blockCollision = findCollisionWall();
            if (blockCollision != null) {
                if (getDeltaY() > 0) {
                    setCenterY(blockCollision.boundingBox.y - boundingBox.height / 2);
                    setY_velocity(0);
                    Sound.playSound("Zero/player_land.wav");
                } else if (getDeltaY() < 0) {
                    setCenterY(blockCollision.boundingBox.y + blockCollision.boundingBox.height + boundingBox.height / 2);
                    setY_velocity(0);
                }
                setDeltaY(0);
            }

            double newXPos = center.x + getDeltaX();
            setCenterX((int) newXPos);
            blockCollision = findCollisionWall();
            if (blockCollision != null) {
                if (getDeltaX() > 0) {
                    setCenterX(blockCollision.boundingBox.x - boundingBox.width / 2);
                    setX_velocity(0);
                } else if (getDeltaX() < 0) {
                    setCenterX(blockCollision.boundingBox.x + blockCollision.boundingBox.width + boundingBox.width / 2);
                    setX_velocity(0);
                }
                setDeltaX(0);
            }
            if (boundingBox != null) {
                boundingBox.x = center.x - boundingBox.width / 2;
                boundingBox.y = center.y - boundingBox.height / 2;
            }
//            setCenter(new Point((int) newXPos, (int) newYPos));
        }
    }
}
