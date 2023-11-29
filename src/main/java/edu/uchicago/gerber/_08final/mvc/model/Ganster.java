package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.GameOp;
import edu.uchicago.gerber._08final.mvc.controller.Sound;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

@Data
public class Ganster extends Character {


    // image path
    private static String gangsterImgPathPrefix = "Gangster/";

    private enum enemyActions {
        ATTACK,
        HURT_GROUND,
        IDLE,
        WALK,
        RUN
    }

    private enemyActions action = enemyActions.IDLE;
    protected static Map<?, ArrayList<BufferedImage>> rasterPicMaps;

    static {
        Map<enemyActions, ArrayList<BufferedImage>> rasterMaps = new HashMap<>();

        ArrayList<BufferedImage> rasterMapIdle = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapIdle.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_idle/%d.png", i)));}
        rasterMaps.put(enemyActions.IDLE, rasterMapIdle);

        ArrayList<BufferedImage> rasterMapHurtGround = new ArrayList<>();
        for (int i = 0; i < 14; i++) {rasterMapHurtGround.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_hurtground/%d.png", i)));}
        rasterMaps.put(enemyActions.HURT_GROUND, rasterMapHurtGround);

        ArrayList<BufferedImage> rasterMapWalk = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapWalk.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_walk/%d.png", i)));}
        rasterMaps.put(enemyActions.WALK, rasterMapWalk);

        ArrayList<BufferedImage> rasterMapRun = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapRun.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_run/%d.png", i)));}
        rasterMaps.put(enemyActions.RUN, rasterMapRun);

        rasterPicMaps = rasterMaps;
    }
    public static boolean loadResources() {
        return true;
    }

    public Ganster(Point center) {
        setTeam(Team.ENEMY);
        setRadius(MIN_RADIUS);
        setCenter(center);
        setBoundingType(BoundingType.RECTANGLE);

        setHurtGroundFrames(14);
        setMaxXVelocity(15);
        setAttackRadius(300);
        setTotalPreAttackFrames(10);

        setBoundingBox(new Rectangle(getCenter().x - BLOCK_SIZE / 2, getCenter().y - BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE));
    }

    @Override
    public boolean isProtected() {
        return isProtected;
    }
    @Override
    public void setChasing(boolean state) {
        isChasing = state;
        if (!isProtected && !isAttack) {
            if (state) {
                action = enemyActions.RUN;
            } else {
                action = enemyActions.IDLE;
            }
        }
    }
    @Override
    public void draw(Graphics g) {
        ArrayList<BufferedImage> pics = new ArrayList<>();
        int offsetX = 0, offsetY = 0;


        switch (action) {
            case IDLE:
                pics = rasterPicMaps.get(enemyActions.IDLE);
                offsetY = -13;
                break;
            case RUN:
                pics = rasterPicMaps.get(enemyActions.RUN);
                break;
            case WALK:
                pics = rasterPicMaps.get(enemyActions.WALK);
                break;
            case ATTACK:
                pics = rasterPicMaps.get(enemyActions.ATTACK);
                break;
            case HURT_GROUND:
                pics = rasterPicMaps.get(enemyActions.HURT_GROUND);
                offsetY = 5;
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
                currentPicIdx = hurtGroundFrames - 1;
            }
        } else if (isAttack) {
            if (currentAttackIdx < attackFrames) {
                currentPicIdx = currentAttackIdx;
                if (CommandCenter.getInstance().getFrame() % 2 == 0) {
                    currentAttackIdx += 1;
                }
            } else {
                // currentAttachIdx == 7
                currentAttackIdx = 0;
                isAttack = false;
            }
        }
        if (isFacingLeft || atLeft) {
            renderRasterFlipFromRect((Graphics2D) g, pics.get(currentPicIdx), offsetX, offsetY);
        } else {
            renderRasterFromRect((Graphics2D) g, pics.get(currentPicIdx), offsetX, offsetY);
        }
//        g.setColor(Color.RED);
//        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().getViewX(), getCenter().y - getRadius() - CommandCenter.getInstance().getViewY(), getRadius() *2, getRadius() *2);
//        g.drawOval(getCenter().x - getViewRadius() - CommandCenter.getInstance().getViewX(), getCenter().y - getViewRadius() - CommandCenter.getInstance().getViewY(), getViewRadius() *2, getViewRadius() *2);
//        g.setColor(Color.GREEN);
//        g.drawOval(getCenter().x - getAttackRadius() - CommandCenter.getInstance().getViewX(), getCenter().y - getAttackRadius() - CommandCenter.getInstance().getViewY(), getAttackRadius() *2, getAttackRadius() *2);
    }
    @Override

    public void getHurt(Sprite obj) {
        setDeltaX((getCenter().x - obj.getCenter().x) * 2);
        setDeltaY(getCenter().y - obj.getCenter().y);
        action = enemyActions.HURT_GROUND;
        setProtected(true);
        setHurtGround(true);

        setYVelocity(0);
        setChasing(false);
        double theta = obj.getTheta();
        bloodDebris = new BloodDebris(theta, center);
        CommandCenter.getInstance().getOpsQueue().enqueue(bloodDebris, GameOp.Action.ADD);
    }

    @Override
    public void attack() {
        if (canAttack) {
            if (currentPreAttackFrame < totalPreAttackFrames) {
                if (CommandCenter.getInstance().getFrame() % 2 == 0) { currentPreAttackFrame += 1;}
            } else {
                // finish preparation
                if (currentAttackIntervalFrame == 0) {
                    currentPreAttackFrame = 0;
                    currentAttackIntervalFrame = totalAttackIntervalFrames;
                    Bullet bullet = new Bullet(getCenter(), atLeft);
                    CommandCenter.getInstance().getOpsQueue().enqueue(bullet, GameOp.Action.ADD);
                    Sound.playSound("Bullet/gun_fire.wav");
                    Sound.playSound("Bullet/sound_enemy_shotgun_reload_01.wav");
                } else {
                    if (CommandCenter.getInstance().getFrame() % 2 == 0) { currentAttackIntervalFrame -= 1;}
                }

            }
        }

    }
    @Override
    public void move() {
        super.move();

        // compute the x change
        if (!isAttack) {
            if (isChasing) {
                // bound the maximum speed

                if (abs(xVelocity) < maxXVelocity) {
                    if (atLeft) {
                        xVelocity -= xAccelerate;
                    } else {
                        xVelocity += xAccelerate;
                    }
                }

                if (atLeft && xVelocity > 0) {
                    xVelocity = -xVelocity;
                }
                if (!atLeft && xVelocity < 0) {
                    xVelocity = -xVelocity;
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

            // compute y change
            if (!isOnPlatform()) {
                if (abs(yVelocity) <= maxYVelocity) {
                    yVelocity -= gravityG;
                }
            }
            // apply the coordinates change
            setDeltaX(xVelocity);
            setDeltaY(yVelocity);
        }

    }
}
