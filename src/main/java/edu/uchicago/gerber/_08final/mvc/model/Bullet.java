package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Utils;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Bullet extends Sprite {

    public static int bulletRadius = 16;
    public int xVelocity = 54;
    public int yVelocity = 0;

    public boolean isReflected = false;
    public Bullet(Point center) {

        setTeam(Team.BULLET);


        setRadius(bulletRadius);

        setXVelocity(xVelocity);
        setYVelocity(yVelocity);

        HashMap<Integer, BufferedImage> rasterMap = new HashMap<>();
        rasterMap.put(0, loadGraphic(imgPathPrefix + "bullet.png"));
        setRasterMap(rasterMap);

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
        renderRaster((Graphics2D) g, getRasterMap().get(0));
        g.setColor(Color.ORANGE);
        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().viewX, getCenter().y - getRadius() - CommandCenter.getInstance().viewY, getRadius() *2, getRadius() *2);

    }
}
