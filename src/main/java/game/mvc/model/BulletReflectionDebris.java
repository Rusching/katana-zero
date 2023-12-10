package game.mvc.model;


import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * everytime the player hits an unreflected bullet with
 * katana then there would be a bullet reflection debris
 */
@Data
public class BulletReflectionDebris extends Sprite{

    private static String zeroImgPathPrefix = "ZeroSprites/";
    private double theta;
    private static Map<?, BufferedImage> rasterPicMap;

    // this index is used to display the reflection animation,
    // indicating which image is to display
    private int displayIndex = 0;

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

        // it would be automatically destroyed after fully displaying
        setExpiry(rasterPicMap.size());
    }

    @Override
    public void draw(Graphics g) {
        renderRaster((Graphics2D) g, rasterPicMap.get(displayIndex));
        displayIndex++;
    }
}
