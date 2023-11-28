package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Line2D;
import java.nio.channels.Pipe;
import java.util.ArrayList;

@Data
public class Katana extends Sprite {

    private static int katanaRadius = 36;

    private double theta;

    private HitSlashDebris hitSlashDebris = null;
    private BulletReflectionDebris bulletReflectionDebris = null;
    public Katana(int attackX, int attackY, Point zeroCenter, int zeroRadius) {

        setTeam(Team.KATANA);
        Point katanaCenter = findIntersection(zeroCenter, zeroRadius, attackX + CommandCenter.getInstance().getViewX(), attackY + CommandCenter.getInstance().getViewY());
//        System.out.println("Click Position: " + (attackX + CommandCenter.getInstance().viewX) + " " + (attackY + CommandCenter.getInstance().viewY));
//        System.out.println("Zero Position: " + (zeroCenter.x) + " " + (zeroCenter.y));
//        System.out.println("Katana Position: " + katanaCenter.x + " " + katanaCenter.y);

        setCenter(katanaCenter);
        setRadius(katanaRadius);
    }

    public Point findIntersection(Point circleCenter, double radius, int x_p, int y_p) {
        int x_c = circleCenter.x;
        int y_c = circleCenter.y;

        if (x_c == x_p && y_c == y_p) {
            // exactly the same point
            return new Point(x_c, y_c);
        } else {
            // Calculate the angle theta
            double dx = x_p - x_c;
            double dy = y_p - y_c;
            double theta = Math.atan2(dy, dx);
            // this is radians;
            setTheta(theta);

            // Calculate intersection points
            double x_intersection = x_c + radius * Math.cos(theta);
            double y_intersection = y_c + radius * Math.sin(theta);
            return new Point((int) x_intersection, (int) y_intersection);
        }
    }

    @Override
    public void draw(Graphics g) {
//        g.setColor(Color.RED);
//        g.drawOval(getCenter().x - getRadius() - CommandCenter.getInstance().getViewX(), getCenter().y - getRadius() - CommandCenter.getInstance().getViewY(), getRadius() *2, getRadius() *2);
    }
}
