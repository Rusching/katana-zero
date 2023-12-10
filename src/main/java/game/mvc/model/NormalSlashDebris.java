package game.mvc.model;


import game.mvc.controller.CommandCenter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class NormalSlashDebris extends Sprite{
    private int displayIndex = 0;
    private static String zeroImgPathPrefix = "ZeroSprites/";
    private static Map<?, BufferedImage> rasterPicMap;

    static {
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {rasterMap.put(i, loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("normalSlash/spr_slash_%d.png", i)));}
        rasterPicMap = rasterMap;
    }

    public static boolean loadResources() {
        return true;
    }
    public NormalSlashDebris(int attachX, int attachY, Point charCenter) {
        setTeam(Team.DEBRIS);
        setExpiry(rasterPicMap.size());

        // calculate the orientation between click point and player's center
        int diffX = attachX - (charCenter.x - CommandCenter.getInstance().getViewX());
        int diffY = attachY - (charCenter.y - CommandCenter.getInstance().getViewY());
        double angle = Math.toDegrees(Math.atan2(diffY, diffX));
        setOrientation((int) angle);
        setCenter(charCenter);
    }

    @Override
    public void draw(Graphics g) {
        renderRaster((Graphics2D) g, rasterPicMap.get(displayIndex));
        displayIndex++;
    }
}
