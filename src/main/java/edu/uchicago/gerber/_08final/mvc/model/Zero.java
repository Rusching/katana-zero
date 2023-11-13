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
public class Zero extends Sprite{

    // states
    private boolean isIdle = true;

    // indicated if the up key is pressed
    private boolean isJumping = false;

    // jump is divided into 'up' and 'down' procedures. It is
    // distinguished by the 'isFalling' flag: true is up and false is down.
    private boolean isFalling = false;

    // indicates if the player is on the ground
    private boolean isOnGround = true;
    // indicates if the down key is pressed
    private boolean isRolling = false;

    // indicates if the left or right key are pressed
    private boolean isRunning = false;
    // By default, zero is facing right and textures are facing right
    private boolean isFacingLeft = false;
    private int run2IdleFlag = 0;

    public static final int MIN_RADIUS = 18;


    private double maximum_jump_time = 1000;
    // velocity
    private double x_velocity = 0;
    private double x_accelerate = 0.7;
    private double x_slowdown_accelerate = 5;

    private double y_velocity = 0;
    private double max_x_velocity = 9;
    private double max_y_velocity = 9;

    private final double initial_y_velocity = -6;
    public final double gravityG = -0.3;


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
            renderRasterFlip((Graphics2D) g, pics.get(currentPicIdx));
        } else {
            renderRaster((Graphics2D) g, pics.get(currentPicIdx));
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
