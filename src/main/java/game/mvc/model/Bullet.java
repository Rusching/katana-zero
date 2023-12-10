package game.mvc.model;

import game.mvc.controller.Game;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Data
public class Bullet extends Sprite {

    private static int bulletRadius = 16;
    private int xVelocity = 50;
    private int yVelocity = 0;

    private boolean isReflected = false;
    private static Map<?, BufferedImage> rasterPicMap;
    static {
        HashMap<Integer, BufferedImage> rasterMap = new HashMap<>();
        rasterMap.put(0, loadGraphic(imgPathPrefix + "bullet.png"));
        rasterPicMap = rasterMap;
    }
    public static boolean loadResources() {
        return true;
    }
    public Bullet(Point center, boolean atLeft) {
        setTeam(Team.BULLET);
        setRadius(bulletRadius);
        if (atLeft) {
            setXVelocity(-xVelocity);
        } else {
            setXVelocity(xVelocity);
        }
        setYVelocity(Game.R.nextInt(7) - 3);
        setCenter(center);
    }

    @Override
    public void move() {
        super.move();
        setDeltaX(xVelocity);
        setDeltaY(yVelocity);
    }


    @Override
    public void draw(Graphics g) {
        renderRaster((Graphics2D) g, rasterPicMap.get(0));
    }
}
