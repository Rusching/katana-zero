package game.mvc.model;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * everytime the player jumps there would
 * be a jump debris
 */
public class JumpDebris extends Sprite{

    private int displayIndex = 0;

    private static String zeroImgPathPrefix = "ZeroSprites/";
    private static Map<?, BufferedImage> rasterPicMap;

    static {
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        for (int i = 0; i < 4; i++) {rasterMap.put(i, loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("jump/JumpCloud/spr_jumpcloud_%d.png", i)));}
        rasterPicMap = rasterMap;
    }
    public static boolean loadResources() {
        return true;
    }
    public JumpDebris(Point charCenter) {
        setTeam(Team.DEBRIS);

        //expire it out after it has done its animation. Multiply by 2 to slow down the animation
        setExpiry(rasterPicMap.size());
        setCenter(charCenter);
    }

    @Override
    public void draw(Graphics g) {
        renderRaster((Graphics2D) g, rasterPicMap.get(displayIndex));
        displayIndex++;
    }
}
