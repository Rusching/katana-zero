package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class Zero extends Sprite{
    enum FaceDirection {LEFT, RIGHT}
    private FaceDirection faceDirection = FaceDirection.RIGHT;
    private boolean isJumping = false;
    private boolean isWalking = false;
    private boolean isOnGround = true;
    private boolean isRolling = false;
    private boolean isRunning = false;
    public static final int MIN_RADIUS = 28;
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
        STAND,
        THREAT,
        THROW_SWORD,
        WALK
    }

    public Zero() {
        setTeam(Team.FRIEND);
        setRadius(MIN_RADIUS);
        setBoundingType(BoundingType.RECTANGLE);


        Map<Actions, ArrayList<BufferedImage>> rasterMaps = new HashMap<>();

        ArrayList<BufferedImage> rasterMapIdle = new ArrayList<>();
        for (int i = 0; i < 11; i++) {rasterMapIdle.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("stand/spr_idle_%d.png", i)));}
        rasterMaps.put(Actions.STAND, rasterMapIdle);

        ArrayList<BufferedImage> rasterMapRun = new ArrayList<>();
        for (int i = 0; i < 10; i++) {rasterMapRun.add(loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("run/spr_run_%d.png", i)));}
        rasterMaps.put(Actions.RUN, rasterMapRun);

        setRasterMaps(rasterMaps);
    }

    @Override
    public void draw(Graphics g) {

//        if (isOnGround && !isWalking && !isRunning) {
            // idle
        ArrayList<BufferedImage> pics = getRasterMaps().get(Actions.STAND);
        int currentPicIdx = (int) ((CommandCenter.getInstance().getFrame() / 2) % pics.size());
        renderRaster((Graphics2D) g, pics.get(currentPicIdx));
//        }



    }

    @Override
    public void move() {
        super.move();



    }
}
