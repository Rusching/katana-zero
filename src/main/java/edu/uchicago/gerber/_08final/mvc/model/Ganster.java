package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
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

    Punch punch = null;
    public enum gruntActions {
        ATTACK,
        HURT_GROUND,
        IDLE,
        WALK,
        RUN
    }

    public gruntActions action = gruntActions.IDLE;


    public Ganster(Point center) {
        setTeam(Team.ENEMY);
        setRadius(MIN_RADIUS);
        setCenter(center);
        setBoundingType(BoundingType.RECTANGLE);

        setHurtGroundFrames(14);
        setMaxXVelocity(15);
        setAttackRadius(300);
        setTotalPreAttackFrames(20);

        setBoundingBox(new Rectangle(getCenter().x - BLOCK_SIZE / 2, getCenter().y - BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE));

        Map<gruntActions, ArrayList<BufferedImage>> rasterMaps = new HashMap<>();

        ArrayList<BufferedImage> rasterMapIdle = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapIdle.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_idle/%d.png", i)));}
        rasterMaps.put(gruntActions.IDLE, rasterMapIdle);

        ArrayList<BufferedImage> rasterMapHurtGround = new ArrayList<>();
        for (int i = 0; i < 14; i++) {rasterMapHurtGround.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_hurtground/%d.png", i)));}
        rasterMaps.put(gruntActions.HURT_GROUND, rasterMapHurtGround);

        ArrayList<BufferedImage> rasterMapWalk = new ArrayList<>();
        for (int i = 0; i < 8; i++) {rasterMapWalk.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_walk/%d.png", i)));}
        rasterMaps.put(gruntActions.WALK, rasterMapWalk);

        ArrayList<BufferedImage> rasterMapRun = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapRun.add(loadGraphic(imgPathPrefix + gangsterImgPathPrefix + String.format("spr_gangster_run/%d.png", i)));}
        rasterMaps.put(gruntActions.RUN, rasterMapRun);

        setRasterMaps(rasterMaps);
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
                action = gruntActions.RUN;
            } else {
                action = gruntActions.IDLE;
            }
        }
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
        g.setColor(Color.RED);
        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().viewX, getCenter().y - getRadius() - CommandCenter.getInstance().viewY, getRadius() *2, getRadius() *2);
        g.drawOval(getCenter().x - getViewRadius() - CommandCenter.getInstance().viewX, getCenter().y - getViewRadius() - CommandCenter.getInstance().viewY, getViewRadius() *2, getViewRadius() *2);
        g.setColor(Color.GREEN);
        g.drawOval(getCenter().x - getAttackRadius() - CommandCenter.getInstance().viewX, getCenter().y - getAttackRadius() - CommandCenter.getInstance().viewY, getAttackRadius() *2, getAttackRadius() *2);
    }

    public void getHurt(Katana currentKatana) {
        setDeltaX((getCenter().x - currentKatana.getCenter().x) * 2);
        setDeltaY(getCenter().y - currentKatana.getCenter().y);
        action = Ganster.gruntActions.HURT_GROUND;
        setProtected(true);
        setHurtGround(true);

        setYVelocity(0);
        setChasing(false);
        Sound.playSound(String.format("Enemy/sound_enemy_death_sword_0%d.wav", Game.R.nextInt(2)));
        double theta = currentKatana.getTheta();
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
                    Bullet bullet = new Bullet(getCenter());
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