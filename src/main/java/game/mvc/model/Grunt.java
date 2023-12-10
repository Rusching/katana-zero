package game.mvc.model;

import game.mvc.controller.CommandCenter;
import game.mvc.controller.GameOp;
import game.mvc.controller.Sound;
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

    Punch punch = null;
    private enum enemyActions {
        ATTACK,
        HURT_FLY,
        HURT_FALL,
        HURT_GROUND,
        IDLE,
        WALK,
        RUN
    }

    private enemyActions action = enemyActions.IDLE;
    private static Map<?, ArrayList<BufferedImage>> rasterPicMaps;

    static {
        Map<enemyActions, ArrayList<BufferedImage>> rasterMaps = new HashMap<>();

        ArrayList<BufferedImage> rasterMapIdle = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapIdle.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_idle/%d.png", i)));}
        rasterMaps.put(enemyActions.IDLE, rasterMapIdle);

        ArrayList<BufferedImage> rasterMapHurtFly = new ArrayList<>();
        for (int i = 0; i < 2; i++) {rasterMapHurtFly.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_hurtfly/%d.png", i)));}
        rasterMaps.put(enemyActions.HURT_FLY, rasterMapHurtFly);

        ArrayList<BufferedImage> rasterMapHurtFall = new ArrayList<>();
        for (int i = 0; i < 2; i++) {rasterMapHurtFall.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_fall/%d.png", i)));}
        rasterMaps.put(enemyActions.HURT_FALL, rasterMapHurtFall);

        ArrayList<BufferedImage> rasterMapHurtGround = new ArrayList<>();
        for (int i = 0; i < 16; i++) {rasterMapHurtGround.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_hurtground/%d.png", i)));}
        rasterMaps.put(enemyActions.HURT_GROUND, rasterMapHurtGround);

        ArrayList<BufferedImage> rasterMapWalk = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapWalk.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_walk/%d.png", i)));}
        rasterMaps.put(enemyActions.WALK, rasterMapWalk);

        ArrayList<BufferedImage> rasterMapRun = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapRun.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_run/%d.png", i)));}
        rasterMaps.put(enemyActions.RUN, rasterMapRun);

        ArrayList<BufferedImage> rasterMapAttack = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapAttack.add(loadGraphic(imgPathPrefix + gruntImgPathPrefix + String.format("spr_grunt_attack/%d.png", i)));}
        rasterMaps.put(enemyActions.ATTACK, rasterMapAttack);

        attackFrames = rasterMapAttack.size();

        rasterPicMaps = rasterMaps;
    }
    public static boolean loadResources() {
        return true;
    }
    public Grunt(Point center) {
        setTeam(Team.ENEMY);
        setRadius(MIN_RADIUS);
        setCenter(center);
        setBoundingType(BoundingType.RECTANGLE);

        setHurtGroundFrames(16);
        setMaxXVelocity(15);

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
                offsetY = -3;
                break;
        }

        int currentPicIdx = (int) ((CommandCenter.getInstance().getFrame() / 2) % pics.size());

        // the following part is because the animation is not a loop type,
        // so the picture sequence number that should be presented is recalculated.
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
                CommandCenter.getInstance().getOpsQueue().enqueue(punch, GameOp.Action.REMOVE);
                punch = null;
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
                    isAttack = true;
                    action = enemyActions.ATTACK;
                    punch = new Punch(getCenter());
                    CommandCenter.getInstance().getOpsQueue().enqueue(punch, GameOp.Action.ADD);
                    Sound.playSound("Enemy/punch.wav");
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
