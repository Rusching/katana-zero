package edu.uchicago.gerber._08final.mvc.model;


import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class JumpDebris extends Sprite{

    private int index = 0;

    private static String zeroImgPathPrefix = "ZeroSprites/";
    protected static Map<?, BufferedImage> rasterPicMap;

    static {
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        for (int i = 0; i < 4; i++) {rasterMap.put(i, loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("jump/JumpCloud/spr_jumpcloud_%d.png", i)));}
        rasterPicMap = rasterMap;
    }
    public static boolean loadResources() {
        return true;
    }
    public JumpDebris(Point charCenter) {

        //DEBRIS means that this sprite is inert, and does not interact with other teams.
        setTeam(Team.DEBRIS);



        //expire it out after it has done its animation. Multiply by 2 to slow down the animation
        setExpiry(rasterPicMap.size());
        setCenter(charCenter);
    }


    @Override
    public void draw(Graphics g) {


        renderRaster((Graphics2D) g, rasterPicMap.get(index));
        //hold the image for two frames to slow down the dust cloud animation
        //we already have a simple decrement-to-zero counter with expiry; see move() method of Sprite.
        index++;


    }
}
