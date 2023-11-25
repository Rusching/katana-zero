package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.GameOp;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

@Data
public class Grunt extends Character {


    // image path
    private static String gruntImgPathPrefix = "Grunt/";

    protected boolean isProtected = false;

    // hurt ground
    protected boolean isHurtGround = false;
    protected final int hurtGroundFrames = 16;
    protected int currentHurtGroundIdx = 0;

    public enum gruntActions {
        ATTACK,
        HURT_FLY,
        HURT_FALL,
        HURT_GROUND,
        IDLE,
        WALK,
        RUN
    }

    public gruntActions action = gruntActions.IDLE;

    public Grunt(Point center) {
        setTeam(Team.ENEMY);
        setRadius(MIN_RADIUS);
        setCenter(center);
        setBoundingType(BoundingType.RECTANGLE);

        setBoundingBox(new Rectangle(getCenter().x - BLOCK_SIZE / 2, getCenter().y - BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE));

        Map<gruntActions, ArrayList<BufferedImage>> rasterMaps = new HashMap<>();

        ArrayList<BufferedImage> rasterMapIdle = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapIdle.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_idle/%d.png", i)));}
        rasterMaps.put(gruntActions.IDLE, rasterMapIdle);

        ArrayList<BufferedImage> rasterMapHurtFly = new ArrayList<>();
        for (int i = 0; i < 2; i++) {rasterMapHurtFly.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_hurtfly/%d.png", i)));}
        rasterMaps.put(gruntActions.HURT_FLY, rasterMapHurtFly);

        ArrayList<BufferedImage> rasterMapHurtFall = new ArrayList<>();
        for (int i = 0; i < 2; i++) {rasterMapHurtFall.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_fall/%d.png", i)));}
        rasterMaps.put(gruntActions.HURT_FALL, rasterMapHurtFall);

        ArrayList<BufferedImage> rasterMapHurtGround = new ArrayList<>();
        for (int i = 0; i < 16; i++) {rasterMapHurtGround.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_hurtground/%d.png", i)));}
        rasterMaps.put(gruntActions.HURT_GROUND, rasterMapHurtGround);

        ArrayList<BufferedImage> rasterMapWalk = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapWalk.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_walk/%d.png", i)));}
        rasterMaps.put(gruntActions.WALK, rasterMapWalk);

        ArrayList<BufferedImage> rasterMapRun = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapRun.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_run/%d.png", i)));}
        rasterMaps.put(gruntActions.RUN, rasterMapRun);

        ArrayList<BufferedImage> rasterMapAttack = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapAttack.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_attack/%d.png", i)));}
        rasterMaps.put(gruntActions.ATTACK, rasterMapAttack);

        setRasterMaps(rasterMaps);
    }

    @Override
    public boolean isProtected() {
        return isProtected;
    }
    @Override
    public void draw(Graphics g) {
        ArrayList<BufferedImage> pics = new ArrayList<>();
        int offsetX = 0, offsetY = 0;


        switch (action) {
            case IDLE:
                pics = getRasterMaps().get(gruntActions.IDLE);
                break;
            case RUN:
                pics = getRasterMaps().get(gruntActions.RUN);
                break;
            case WALK:
                pics = getRasterMaps().get(gruntActions.WALK);
                break;
            case ATTACK:
                pics = getRasterMaps().get(gruntActions.ATTACK);
                break;
            case HURT_GROUND:
                pics = getRasterMaps().get(gruntActions.HURT_GROUND);
                break;
        }

        int currentPicIdx = (int) ((CommandCenter.getInstance().getFrame() / 2) % pics.size());

        if (isHurtGround) {
            if (currentHurtGroundIdx < hurtGroundFrames) {
                currentPicIdx = currentHurtGroundIdx;
                if (CommandCenter.getInstance().getFrame() % 2 == 0) {
                    currentHurtGroundIdx += 1;
                }
            } else {
                // currentAttachIdx == 16
                currentPicIdx = 15;
            }
        }
        if (isFacingLeft) {
            renderRasterFlipFromRect((Graphics2D) g, pics.get(currentPicIdx), offsetX, offsetY);
        } else {
            renderRasterFromRect((Graphics2D) g, pics.get(currentPicIdx), offsetX, offsetY);
        }
        g.setColor(Color.RED);
        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().viewX, getCenter().y - getRadius() - CommandCenter.getInstance().viewY, getRadius() *2, getRadius() *2);
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
//        scrollMap();

//        if (!isOnPlatform()) {
//            setDeltaY(y_velocity);
//            if (abs(y_velocity) < max_y_velocity) {
//                y_velocity -= gravityG;
//            }
//        }
    }
}
