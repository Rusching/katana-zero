package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@Data
public class Zero extends Character{


    // image path
    private static String zeroImgPathPrefix = "ZeroSprites/";

    enum Actions {
        ATTACK,
        BIG_SLASH,
        BLOOD,
        CROUCH,
        DRAW_SWORD,
        FALL,
        FIRE_SLASH,
        HURT,
        IDLE,
        JUMP,
        WALL_SLIDE,
        FLIP,
        KICK,
        LOOK_AROUND,
        NORMAL_SLASH,
        OLD_SLASH,
        PHONE,
        PLAY_SONG,
        RAINBOW_SLASH,
        ROLL,
        RUN,
        RUN2IDLE,
        STAND,
        THREAT,
        THROW_SWORD,
        WALK
    }

    public Zero() {
        setTeam(Team.FRIEND);
        setRadius(MIN_RADIUS);
        setBoundingType(BoundingType.RECTANGLE);
        setBoundingBox(new Rectangle(getCenter().x - BLOCK_SIZE / 2, getCenter().y - BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE));

        Map<Actions, ArrayList<BufferedImage>> rasterMaps = new HashMap<>();

        ArrayList<BufferedImage> rasterMapIdle = new ArrayList<>();
        for (int i = 0; i < 11; i++) {rasterMapIdle.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("stand/spr_idle_%d.png", i)));}
        rasterMaps.put(Actions.STAND, rasterMapIdle);

        ArrayList<BufferedImage> rasterMapRun = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapRun.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("run/spr_run_%d.png", i)));}
        rasterMaps.put(Actions.RUN, rasterMapRun);

        ArrayList<BufferedImage> rasterMapRun2Idle = new ArrayList<>();
        for (int i = 0; i < 5; i++) {rasterMapRun2Idle.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("run/spr_run_to_idle_%d.png", i)));}
        rasterMaps.put(Actions.RUN2IDLE, rasterMapRun2Idle);

        ArrayList<BufferedImage> rasterMapJump = new ArrayList<>();
        for (int i = 0; i < 4; i++) {rasterMapJump.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("jump/spr_jump_%d.png", i)));}
        rasterMaps.put(Actions.JUMP, rasterMapJump);

        ArrayList<BufferedImage> rasterMapFall = new ArrayList<>();
        for (int i = 0; i < 4; i++) {rasterMapFall.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("fall/spr_fall_%d.png", i)));}
        rasterMaps.put(Actions.FALL, rasterMapFall);

        ArrayList<BufferedImage> rasterMapRoll = new ArrayList<>();
        for (int i = 0; i < 7; i++) {rasterMapRoll.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("roll/spr_roll_%d.png", i)));}
        rasterMaps.put(Actions.ROLL, rasterMapRoll);

        ArrayList<BufferedImage> rasterMapAttack = new ArrayList<>();
        for (int i = 0; i < 7; i++) {rasterMapAttack.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("attack/spr_attack_%d.png", i)));}
        rasterMaps.put(Actions.ATTACK, rasterMapAttack);

        ArrayList<BufferedImage> rasterMapFlip = new ArrayList<>();
        for (int i = 0; i < 11; i++) {rasterMapFlip.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("flip/spr_flip_%d.png", i)));}
        rasterMaps.put(Actions.FLIP, rasterMapFlip);

        ArrayList<BufferedImage> rasterMapWallSlide = new ArrayList<>();
        rasterMapWallSlide.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + "wallSlide/spr_wallSlide_0.png"));
        rasterMaps.put(Actions.WALL_SLIDE, rasterMapWallSlide);

        setRasterMaps(rasterMaps);
    }

    @Override
    public void draw(Graphics g) {
        ArrayList<BufferedImage> pics;
        int offsetX = 0, offsetY = 0;

        if (isAttack) {
            // attack
            pics = getRasterMaps().get(Actions.ATTACK);
        } else if (isRolling) {
            // roll
            pics = getRasterMaps().get(Actions.ROLL);
            offsetY = 1;
        } else if (isFlipping) {
            if ((isOnLeftWall() || isOnRightWall()) && currentFlipIdx != 0) {
                // flip terminate to wall slide
                pics = getRasterMaps().get(Actions.WALL_SLIDE);
                offsetX = 20;
            } else {
                // flip
                pics = getRasterMaps().get(Actions.FLIP);
            }
        } else if ((isOnLeftWall() || isOnRightWall()) && isRunning && !isOnPlatform()) {
            // wall slide
            pics = getRasterMaps().get(Actions.WALL_SLIDE);
            offsetX = 20;
        } else if (isOnPlatform()) {
            if (isRunning) {
                // run
                pics = getRasterMaps().get(Actions.RUN);
                offsetY = 1;
            } else {
                // stand
                pics = getRasterMaps().get(Actions.STAND);
                offsetY = 1;
            }
        } else {
            // in air
            if (getDeltaY() <= 0) {
                // up
                pics = getRasterMaps().get(Actions.JUMP);
            } else {
                // down
                pics = getRasterMaps().get(Actions.FALL);
            }
        }

        int currentPicIdx = (int) ((CommandCenter.getInstance().getFrame() / 2) % pics.size());

        if (isAttack) {
            if (currentAttachIdx < attackFrames) {
                currentPicIdx = currentAttachIdx;
                currentAttachIdx += 1;
            } else {
                // currentAttachIdx == 7
                currentAttachIdx = 0;
                isAttack = false;
            }
        } else if (isRolling) {
            if (currentRollIdx < rollFrames) {
                currentPicIdx = currentRollIdx;
                currentRollIdx += 1;
            } else {
                // currentRollIdx == 7
                currentRollIdx = 0;
                isRolling = false;
            }
        } else if (isFlipping) {
            if (!(isOnLeftWall() || isOnRightWall())) {
                if (currentFlipIdx < flipFrames) {
                    currentPicIdx = currentFlipIdx;
                    currentFlipIdx += 1;
                } else {
                    // currentFlipIdx == 11
                    currentFlipIdx = 0;
                    isFlipping = false;
                }
            } else if (currentFlipIdx != 0){
                currentFlipIdx = 0;
                isFlipping = false;
            }

        }

        if (isFacingLeft) {
            renderRasterFlipFromRect((Graphics2D) g, pics.get(currentPicIdx), offsetX, offsetY);
        } else {
            renderRasterFromRect((Graphics2D) g, pics.get(currentPicIdx), offsetX, offsetY);
        }
    }

    public void scrollMap() {
        int leftBoundary = CommandCenter.getInstance().viewX + CommandCenter.getInstance().leftMargin;
        if (boundingBox.x < leftBoundary) {
            CommandCenter.getInstance().viewX -= (leftBoundary - boundingBox.x);
        }

        int rightBoundary = CommandCenter.getInstance().viewX + Game.dimensionWidth - CommandCenter.getInstance().rightMargin;
        if (boundingBox.x + boundingBox.width > rightBoundary) {
            CommandCenter.getInstance().viewX += (boundingBox.x + boundingBox.width - rightBoundary);
        }

        int topBoundary = CommandCenter.getInstance().viewY + CommandCenter.getInstance().verticalMargin;
        if (boundingBox.y < topBoundary) {
            CommandCenter.getInstance().viewY -= (topBoundary - boundingBox.y);
        }

        int bottomBoundary = CommandCenter.getInstance().viewY + Game.dimensionHeight - CommandCenter.getInstance().verticalMargin;
        if (boundingBox.y + boundingBox.height > bottomBoundary) {
            CommandCenter.getInstance().viewY += (boundingBox.y + boundingBox.height - bottomBoundary);
        }
    }

    @Override
    public void move() {
        super.move();

        // compute the x change
        if (!isFlipping) {
            if (isRunning) {
                // bound the maximum speed
                if (isFacingLeft) {
                    if (xVelocity < -maxXVelocity) {
                        xVelocity = -maxXVelocity;
                    }
                } else {
                    if (xVelocity > maxXVelocity) {
                        xVelocity = maxXVelocity;
                    }
                }

                // wall sliding
                if (!isOnPlatform() && (isOnLeftWall() || isOnRightWall())) {
                    yVelocity = 3;
                    xVelocity = 0;

                    // accelerate
                } else if (abs(xVelocity) < maxXVelocity) {
                    if (isFacingLeft) {
                        xVelocity -= xAccelerate;
                    } else {
                        xVelocity += xAccelerate;
                    }
                }
            } else {
                // slow down
                if (xVelocity != 0) {
                    if (xVelocity > 0) {
                        xVelocity = xVelocity > xSlowdownAccelerate ? xVelocity - xSlowdownAccelerate : 0;
                    } else {
                        xVelocity = abs(xVelocity) > xSlowdownAccelerate ? xVelocity + xSlowdownAccelerate : 0;
                    }
                }
            }
        }
        // compute y change
        if (!isOnPlatform()) {
            if (abs(yVelocity) <= maxYVelocity) {
                yVelocity -= gravityG;
            }
        }

        // apply the coordinates change
        setDeltaX(xVelocity);
        setDeltaY(yVelocity);

        // modify the viewX and viewY so that the map move
        scrollMap();

//        if (!isOnPlatform()) {
//            setDeltaY(y_velocity);
//            if (abs(y_velocity) < max_y_velocity) {
//                y_velocity -= gravityG;
//            }
//        }
    }
}
