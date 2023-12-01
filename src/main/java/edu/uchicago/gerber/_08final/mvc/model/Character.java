package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CollisionDetection;
import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Sound;
import lombok.Data;

import java.awt.*;
import java.util.List;
import java.util.Timer;

import static java.lang.Math.abs;

@Data
public abstract class Character extends Sprite{


    // jump is divided into 'up' and 'down' procedures. It is
    // distinguished by the 'isFalling' flag: true is up and false is down.
    protected boolean isFalling = false;

    // rolling related fields
    protected boolean isRolling = false;
    protected final int rollFrames = 7;
    protected int currentRollIdx = 0;

    // attack related fields
    protected boolean isAttack = false;
    protected static int attackFrames = 7;
    protected int currentAttackIdx = 0;

    // flip related fields
    protected boolean isFlipping = false;
    protected final int flipFrames = 11;
    protected int currentFlipIdx = 0;

    // indicates if the left or right key are pressed
    protected boolean isRunning = false;

    // By default, zero is facing right and textures are facing right
    protected boolean isFacingLeft = false;
    protected int run2IdleFlag = 0;

    // min radius
    public static final int MIN_RADIUS = 36;

    // blood effect
    protected boolean isProtected = false;
    protected BloodDebris bloodDebris = null;

    // hurt related fields
    protected boolean isHurtGround = false;
    protected int hurtGroundFrames = 16;
    protected int currentHurtGroundIdx = 0;

    // slow motion
    protected boolean isSlowMotion = false;
    protected Timer slowMotionTimer = new Timer();
    protected int maxSlowMotionDuration = 5000;

    // view related fields (enemy)
    protected int viewRadius = 300;
    protected boolean isNoticed = false;
    protected boolean atLeft = false;
    protected boolean isChasing = false;

    // attack related fields (enemy)
    protected int attackRadius = 36;
    protected boolean canAttack = false;
    protected int totalPreAttackFrames = 5;
    protected int currentPreAttackFrame = 0;
    protected int totalAttackIntervalFrames = 15;
    protected int currentAttackIntervalFrame = 0;

    // velocity related fields

    // x-axis
    protected double xVelocity = 0;
    protected double xAccelerate = 1.4;
    protected double xSlowdownAccelerate = 10;

    // y-axis
    protected double yVelocity = 0;
    protected double maxXVelocity = 18;
    protected double maxYVelocity = 30;
    protected  final double initialYVelocity = -24;
    protected final double gravityG = -1.6;

    /**
     * this method checks if a character is on the left wall. Firstly move the character
     * left 2 pixels and find if there are collision between it and the walls. If we can
     * find one then the character is on the left wall otherwise it is not. Then set back
     * the x-axis position
     * @return the indicator whether the character is on the left wall
     */
    public boolean isOnLeftWall() {
        setCenterX(center.x - 2);
        Block block = findCollisionWall();
        setCenterX(center.x + 2);
        if (block != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * the similar to the isOnLeftWall()
     * @return the indicator whether the character is on the right wall
     */
    public boolean isOnRightWall() {
        setCenterX(center.x + 2);
        Block block = findCollisionWall();
        setCenterX(center.x - 2);
        if (block != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * this method checks if a character is on the platform. Firstly move the character
     * down 2 pixels and find if there are collision between it and the walls. If we can
     * find one then the character is on the platform otherwise it is not. Then set back
     * the y-axis position
     * @return the indicator whether the character is on the platform
     */
    public boolean isOnPlatform() {
        setCenterY(center.y + 2);
        Block block = findCollisionWall();
        setCenterY(center.y - 2);
        if (block != null) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void draw(Graphics g) {}
    abstract public void getHurt(Sprite obj);
    @Override
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * This method finds and returns the first wall that collides with the character
     * to help to address the collision and set all characters on the platform
     * @return the block the first collides with the character. If none return null
     */
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

    public void attack() {}
    @Override
    public void move() {

        // here is the part for AA-BB collision detection to make sure
        // all movable characters stand on the platform and not running
        // into walls. Firstly check the y-axis collisions and then check
        // the x-axis collisions and if there are collisions, reset the x,y values

        // y-axis collision check
        double newYPos = center.y + getDeltaY();
        setCenterY((int) newYPos);
        Block blockCollision = findCollisionWall();
        if (blockCollision != null) {
            if (getDeltaY() > 0) {
                setCenterY(blockCollision.boundingBox.y - boundingBox.height / 2);
                setYVelocity(0);
                Sound.playSound("Zero/player_land.wav");
            } else if (getDeltaY() < 0) {
                setCenterY(blockCollision.boundingBox.y + blockCollision.boundingBox.height + boundingBox.height / 2);
                setYVelocity(0);
            }
            setDeltaY(0);
        }

        // x-axis collision check
        double newXPos = center.x + getDeltaX();
        setCenterX((int) newXPos);
        blockCollision = findCollisionWall();
        if (blockCollision != null) {
            if (getDeltaX() > 0) {
                setCenterX(blockCollision.boundingBox.x - boundingBox.width / 2);
                setXVelocity(0);
            } else if (getDeltaX() < 0) {
                setCenterX(blockCollision.boundingBox.x + blockCollision.boundingBox.width + boundingBox.width / 2);
                setXVelocity(0);
            }
            setDeltaX(0);
        }

        // set the bounding box if exists. only blocks, characters have bounding box
        if (boundingBox != null) {
            boundingBox.x = center.x - boundingBox.width / 2;
            boundingBox.y = center.y - boundingBox.height / 2;
        }
    }
}
