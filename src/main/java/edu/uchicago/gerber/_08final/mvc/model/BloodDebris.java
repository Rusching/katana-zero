package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class BloodDebris extends Sprite {

    public static String bloodPathPrefix = "Blood/";

    public double theta;

    protected boolean isSplatter = true;
    protected final int splatterFrames = 3;
    protected int currentSplatterIdx = 0;

    public final int BG_BLOOD_TYPES = 9;
    public int bgBloodIdx;
    public boolean bgBloodLeft = false;

    public final int BLOOD_SPLATTER_DIRS = 16;
    public int splatterIdx0;
    public int splatterIdx1;
    public int splatterIdx2;
    public int splatterIdx3;
    public int splatterIdx4;
    public int splatterIdx5;
    public BloodDebris(double theta, Point center) {
        setTeam(Team.BLOOD);
        /*
        there are 2 types of bloods: static blood and blood splatter.
        static blood would remain on the wall and display like a texture.
        blood splatter is more like a debris and display for 3 frames.

        static blood has 9 different shapes. Every time get one randomly.

        blood splatter has 16 directions, each with 3 variations. Increased by 22.5 degrees.
        Every time select the corresponding angle and its left and right neighbors, and 3 other random
        directions (total 6 splatters). When display end then vanish.
         */

        // load textures
        Map<Integer, ArrayList<BufferedImage>> rasterMaps = new HashMap<>();

        ArrayList<BufferedImage> rasterMapBgBlood = new ArrayList<>();
        for (int i = 0; i < 9; ++i) { rasterMapBgBlood.add(loadGraphic(imgPathPrefix + bloodPathPrefix + String.format("bg_blood/%d.png", i)));}
        rasterMaps.put(16, rasterMapBgBlood);

        ArrayList<ArrayList<BufferedImage>> bloodSplatterImgLists = new ArrayList<>();
        for (int i = 0; i < 16; i++) { bloodSplatterImgLists.add(new ArrayList<>());}

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 3; j++) { bloodSplatterImgLists.get(i).add(loadGraphic(imgPathPrefix + bloodPathPrefix + String.format("spr_bloodsplatter_dir/%d_%d.png", i, j + 1)));}
            rasterMaps.put(i, bloodSplatterImgLists.get(i));
        }

        setRasterMaps(rasterMaps);
        // set angle
        setTheta(theta);
        setCenter(center);

        setBgBloodIdx(Game.R.nextInt(BG_BLOOD_TYPES));
        setBgBloodLeft(Game.R.nextInt(2) == 0);
        setSplatterIdx();
    }

    public void setSplatterIdx() {
        double degree = Math.toDegrees(theta);

        if (degree <= 0 && degree > -22.5) {
            splatterIdx0 = 0;
        } else if (degree <= -22.5 && degree > -45) {
            splatterIdx0 = 1;
        } else if (degree <= -45 && degree > -67.5) {
            splatterIdx0 = 2;
        } else if (degree <= -67.5 && degree > -90) {
            splatterIdx0 = 3;
        } else if (degree <= -90 && degree > -112.5) {
            splatterIdx0 = 4;
        } else if (degree <= -112.5 && degree > -135) {
            splatterIdx0 = 5;
        } else if (degree <= -135 && degree > -157.5) {
            splatterIdx0 = 6;
        } else if (degree <= -157.5 && degree > -180) {
            splatterIdx0 = 7;
        } else if (degree <= 180 && degree > 157.5) {
            splatterIdx0 = 8;
        } else if (degree <= 157.5 && degree > 135) {
            splatterIdx0 = 9;
        } else if (degree <= 135 && degree > 112.5) {
            splatterIdx0 = 10;
        } else if (degree <= 112.5 && degree > 90) {
            splatterIdx0 = 11;
        } else if (degree <= 90 && degree > 67.5) {
            splatterIdx0 = 12;
        } else if (degree <= 67.5 && degree > 45) {
            splatterIdx0 = 13;
        } else if (degree <= 45 && degree > 22.5) {
            splatterIdx0 = 14;
        } else {
            //  degree <= 22.5 && degree > 0
            splatterIdx0 = 15;
        }
        splatterIdx1 = (splatterIdx0 + BLOOD_SPLATTER_DIRS - 1) % BLOOD_SPLATTER_DIRS;
        splatterIdx2 = (splatterIdx0 + 1) % BLOOD_SPLATTER_DIRS;
        splatterIdx3 = Game.R.nextInt(BLOOD_SPLATTER_DIRS);
        splatterIdx4 = Game.R.nextInt(BLOOD_SPLATTER_DIRS);
        splatterIdx5 = Game.R.nextInt(BLOOD_SPLATTER_DIRS);
        System.out.println("Splatter idx: " + splatterIdx0);
    }
    @Override
    public void draw(Graphics g) {
        // display bg blood
        ArrayList<BufferedImage> bg_pics = getRasterMaps().get(16);
        if (bgBloodLeft) {
            renderRasterFlip((Graphics2D) g, bg_pics.get(bgBloodIdx));
        } else {
            renderRaster((Graphics2D) g, bg_pics.get(bgBloodIdx));
        }

        // display blood splatters

        ArrayList<BufferedImage> splatter_pics_0 = getRasterMaps().get(splatterIdx0);
        ArrayList<BufferedImage> splatter_pics_1 = getRasterMaps().get(splatterIdx1);
        ArrayList<BufferedImage> splatter_pics_2 = getRasterMaps().get(splatterIdx2);
        ArrayList<BufferedImage> splatter_pics_3 = getRasterMaps().get(splatterIdx3);
        ArrayList<BufferedImage> splatter_pics_4 = getRasterMaps().get(splatterIdx4);
        ArrayList<BufferedImage> splatter_pics_5 = getRasterMaps().get(splatterIdx5);

        int currentPicIdx;
        if (isSplatter) {
            if (currentSplatterIdx < splatterFrames) {
                currentPicIdx = currentSplatterIdx;
                if (CommandCenter.getInstance().getFrame() % 8 == 0) {
                    currentSplatterIdx += 1;
                }
                renderRasterScale((Graphics2D) g, splatter_pics_0.get(currentPicIdx), 3, 3);
                renderRasterScale((Graphics2D) g, splatter_pics_1.get(currentPicIdx), 3, 3);
                renderRasterScale((Graphics2D) g, splatter_pics_2.get(currentPicIdx), 3, 3);
                renderRasterScale((Graphics2D) g, splatter_pics_3.get(currentPicIdx), 3, 3);
                renderRasterScale((Graphics2D) g, splatter_pics_4.get(currentPicIdx), 3, 3);
                renderRasterScale((Graphics2D) g, splatter_pics_5.get(currentPicIdx), 3, 3);
                System.out.println("Should render here " + currentPicIdx);
                System.out.println();
            } else {
                isSplatter = false;
            }
        }


    }
}