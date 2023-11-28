package edu.uchicago.gerber._08final.mvc.model;


import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class NormalSlashDebris extends Sprite{

    private int index = 0;

    private static String zeroImgPathPrefix = "ZeroSprites/";
    protected static Map<?, BufferedImage> rasterPicMap;

    static {
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {rasterMap.put(i, loadGraphic(imgPathPrefix + zeroImgPathPrefix + String.format("normalSlash/spr_slash_%d.png", i)));}
        rasterPicMap = rasterMap;
    }

    public static boolean loadResources() {
        return true;
    }
    public NormalSlashDebris(int attachX, int attachY, Point charCenter, Rectangle charRect) {

        //DEBRIS means that this sprite is inert, and does not interact with other teams.
        setTeam(Team.DEBRIS);

        //expire it out after it has done its animation. Multiply by 2 to slow down the animation
        setExpiry(rasterPicMap.size());

        // calculate the orientation
        int diffX = attachX - (charCenter.x - CommandCenter.getInstance().getViewX());
        int diffY = attachY - (charCenter.y - CommandCenter.getInstance().getViewY());
        System.out.println(Math.atan2(diffY, diffX));
        double angle = Math.toDegrees(Math.atan2(diffY, diffX));
        setOrientation((int) angle);

//        System.out.println(String.format("Slash angle: %.2f", angle));
        // calculate the center
//        setCenter(charCenter);
        Point virtualCenter = getLineRectangleIntersection(new Line2D.Float(attachX, attachY, charCenter.x, charCenter.y), charRect);
        if (virtualCenter == null) {
            if (attachY >= charCenter.y) {
                if (attachX <= charCenter.x) {
                    virtualCenter = new Point(charRect.x, charCenter.y);
                } else {
                    virtualCenter = new Point(charRect.x + charRect.width, charCenter.y);
                }
            } else {
                if (attachX <= charCenter.x) {
                    virtualCenter = new Point(charRect.x, charRect.y);
                } else {
                    virtualCenter = new Point(charRect.x + charRect.width, charRect.y);
                }
            }
        }
        setCenter(charCenter);
    }



    public Point getLineIntersection(Line2D line1, Line2D line2) {
        double x1 = line1.getX1();
        double y1 = line1.getY1();
        double x2 = line1.getX2();
        double y2 = line1.getY2();

        double x3 = line2.getX1();
        double y3 = line2.getY1();
        double x4 = line2.getX2();
        double y4 = line2.getY2();

        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0) {
            return null; // Lines are parallel
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;

        if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
            double x = x1 + ua * (x2 - x1);
            double y = y1 + ua * (y2 - y1);
            return new Point((int) x, (int) y);
        }

        return null; // Lines do not intersect
    }

    public Point getLineRectangleIntersection(Line2D line, Rectangle rect) {
        Line2D[] edges = {
                new Line2D.Double(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMinY()), // Top edge
                new Line2D.Double(rect.getMinX(), rect.getMaxY(), rect.getMaxX(), rect.getMaxY()), // Bottom edge
                new Line2D.Double(rect.getMinX(), rect.getMinY(), rect.getMinX(), rect.getMaxY()), // Left edge
                new Line2D.Double(rect.getMaxX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY())  // Right edge
        };

        for (Line2D edge : edges) {
            Point intersection = getLineIntersection(line, edge);
            if (intersection != null) {
                return intersection;
            }
        }
        return null;
    }
    //In this example, we are simply in-order traversing the rasterMap once.
    //However, we could also create a looping animation; think bird flapping over and over.
    //We can also create a hybrid of looping and image-state; think Mario
    //walking (looping), standing (suspended loop), jumping (one state), crouching (another state).
    //See Falcon class for example of image-state.
    @Override
    public void draw(Graphics g) {


        renderRaster((Graphics2D) g, rasterPicMap.get(index));
        //hold the image for two frames to slow down the dust cloud animation
        //we already have a simple decrement-to-zero counter with expiry; see move() method of Sprite.
        index++;


    }
}
