package edu.uchicago.gerber._08final.mvc.model;


import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * when the player's attack hits some enemies then there would be
 * a purple hit slash effect
 */
@Data
public class HitSlashDebris extends Sprite{

    private int index = 0;
    private static String zeroImgPathPrefix = "ZeroSprites/";
    private double theta;
    private static Map<?, BufferedImage> rasterPicMap;

    static {
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        for (int i = 0; i < 4; i++) {rasterMap.put(i, loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("hitSlash/%d.png", i)));}
        rasterPicMap = rasterMap;
    }

    public static boolean loadResources() {
        return true;
    }
    public HitSlashDebris(Point katanaCenter, Point enemyCenter) {

        setCenter(enemyCenter);
        setTeam(Team.DEBRIS);
        setExpiry(rasterPicMap.size());

        // calculate the orientation
        int diffX = enemyCenter.x - katanaCenter.x;
        int diffY = enemyCenter.y - katanaCenter.y;
        double angle = Math.toDegrees(Math.atan2(diffY, diffX));
        setOrientation((int) angle);
    }

    @Override
    public void draw(Graphics g) {
        renderRaster((Graphics2D) g, rasterPicMap.get(index));
        index++;
    }
}
