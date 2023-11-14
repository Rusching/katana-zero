package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

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
        setBoundingBox(new Rectangle(getCenter().x - 18, getCenter().y - 18, BLOCK_SIZE, BLOCK_SIZE));

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

        setRasterMaps(rasterMaps);
    }

    @Override
    public void draw(Graphics g) {
        ArrayList<BufferedImage> pics;
        if (isOnGround && !isRunning) {
            // idle
        //            if (run2IdleFlag > 0) {
        //                pics = getRasterMaps().get(Actions.RUN2IDLE);
        //            } else {
                pics = getRasterMaps().get(Actions.STAND);
//            }
        } else {
            // run
            pics = getRasterMaps().get(Actions.RUN);
        }

//        if (CommandCenter.getInstance().getFrame() % 2 == 0) {run2IdleFlag -= 1;}
        int currentPicIdx = (int) ((CommandCenter.getInstance().getFrame() / 2) % pics.size());
        if (isFacingLeft) {
            renderRasterFlipFromRect((Graphics2D) g, pics.get(currentPicIdx));
        } else {
            renderRasterFromRect((Graphics2D) g, pics.get(currentPicIdx));
        }
    }

    @Override
    public void move() {
        super.move();
        if (isRunning) {
            if (abs(x_velocity) < max_x_velocity) {
                if (isFacingLeft) {
                    x_velocity -= x_accelerate;
                } else {
                    x_velocity += x_accelerate;
                }
            }
            setDeltaX(x_velocity);
        } else {
            if (x_velocity != 0) {
                if (x_velocity > 0) {
                    x_velocity = x_velocity > x_slowdown_accelerate? x_velocity - x_slowdown_accelerate: 0;
                } else {
                    x_velocity = abs(x_velocity) > x_slowdown_accelerate? x_velocity + x_slowdown_accelerate: 0;
                }
            }
            setDeltaX(x_velocity);
        }

        if (isJumping) {

            setDeltaY(y_velocity);
            if (abs(y_velocity) < max_y_velocity) {
                y_velocity -= gravityG;
            }
        }

    }
}
