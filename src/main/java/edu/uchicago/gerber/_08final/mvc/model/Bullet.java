package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Utils;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Bullet extends Sprite {

    private static int bulletRadius = 16;
    private int xVelocity = 50;
    private int yVelocity = 0;

    private boolean isReflected = false;
    protected static Map<?, BufferedImage> rasterPicMap;
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
//        g.setColor(Color.ORANGE);
//        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().getViewX(), getCenter().y - getRadius() - CommandCenter.getInstance().getViewY(), getRadius() *2, getRadius() *2);

    }
}
