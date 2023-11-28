package edu.uchicago.gerber._08final.mvc.model;


import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Data
public class BulletReflectionDebris extends Sprite{

    private int index = 0;
    private static String zeroImgPathPrefix = "ZeroSprites/";
    private double theta;
    private static Map<?, BufferedImage> rasterPicMap;

    static {
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {rasterMap.put(i, loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("bulletReflect/%d.png", i)));}
        rasterPicMap = rasterMap;
    }

    public static boolean loadResources() {
        return true;
    }
    public BulletReflectionDebris(Point center) {

        setCenter(center);
        setTeam(Team.DEBRIS);
        setExpiry(rasterPicMap.size());
    }

    @Override
    public void draw(Graphics g) {
        renderRaster((Graphics2D) g, rasterPicMap.get(index));
        index++;
    }
}
