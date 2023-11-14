package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
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

    // indicates if the player is on the ground
    protected boolean isOnGround = true;
    // indicates if the down key is pressed
    protected boolean isRolling = false;

    // indicates if the left or right key are pressed
    protected boolean isRunning = false;
    // By default, zero is facing right and textures are facing right
    protected boolean isFacingLeft = false;
    protected int run2IdleFlag = 0;

    public static final int MIN_RADIUS = 18;


    protected double maximum_jump_time = 1000;
    // velocity
    protected double x_velocity = 0;
    protected double x_accelerate = 0.7;
    protected double x_slowdown_accelerate = 5;

    protected double y_velocity = 0;
    protected double max_x_velocity = 9;
    protected double max_y_velocity = 15;

    protected  final double initial_y_velocity = -12;
    public final double gravityG = -0.8;


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
                    setCenterY(blockCollision.boundingBox.y - 18);
                    setY_velocity(0);
                } else if (getDeltaY() < 0) {
                    setCenterY(blockCollision.boundingBox.y + blockCollision.boundingBox.height + 18);
                    setY_velocity(0);
                }
                setDeltaY(0);
            }

            double newXPos = center.x + getDeltaX();
            setCenterX((int) newXPos);
            blockCollision = findCollisionWall();
            if (blockCollision != null) {
                if (getDeltaX() > 0) {
                    setCenterX(blockCollision.boundingBox.x - 18);
                    setX_velocity(0);
                } else if (getDeltaX() < 0) {
                    setCenterX(blockCollision.boundingBox.x + blockCollision.boundingBox.width + 18);
                    setX_velocity(0);
                }
                setDeltaX(0);
            }
            if (boundingBox != null) {
                boundingBox.x = center.x - 18;
                boundingBox.y = center.y - 18;
            }
//            setCenter(new Point((int) newXPos, (int) newYPos));
        }
    }
}
